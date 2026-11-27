package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabConfigKeys;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabConfigRuntime;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabRules;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * 抱抱：双向、必须走过去、可中断。
 *
 * <p>一次抱抱是一台四段状态机：<b>APPROACH</b>（寻路到主人 1.5 格内）→ <b>FACE</b>（转身面对面，
 * 主人背对着她就等一会儿）→ <b>HUG</b>（放动作、撒心心）→ 结束。任何一段都不允许瞬移：
 * 走的全是 {@code getNavigation().moveTo}，找不到路就放弃并把原因回给 WAB。
 *
 * <p>谁能发起：主人求抱（{@code from=owner}，她按好感/安全感决定答不答应）、
 * AI 求抱（{@code from=maid}，得开 {@code hug_ai_request_enabled}）、
 * 游戏侧规则（{@code hug_game_side_enabled}，按安全感和心情掷骰，越不安越黏人）。
 * 三条路都先过 {@link WabRules#hugContextAllowed} 的硬闸门：太远、战斗中、刚被拒绝过，一律不抱。
 *
 * <p>中断：主人挪开 0.75 格、任一方受伤、她进入战斗，当场松手；{@link LivingHurtEvent} 上还有一条
 * 立刻取消的旁路，避免动画压着不收。所有状态都存在 {@link #SESSIONS} 里，一 tick 一收，
 * 不会像 NBT 里的姿态标记那样在崩服后卡住。
 */
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WabHug {
    /** 走过去最多给 20 秒，超时就放弃——她不能在半路上无限期地跟着主人跑。 */
    private static final int APPROACH_TIMEOUT = 400;
    /** 面对面最多等 3 秒，主人一直背对着就算了。 */
    private static final int FACE_TIMEOUT = 60;
    /** 原地卡住（距离没变）多久重新寻路一次。 */
    private static final int STALL_TICKS = 40;
    private static final int MAX_REPATH = 3;
    private static final double WALK_SPEED = 0.9;

    private static final String TAG_LAST_HUG = "WabHugLast";
    private static final String TAG_REFUSED_AT = "WabHugRefuseAt";

    private enum Phase { APPROACH, FACE, HUG }

    /** 一次抱抱的运行时状态；只活在服务器内存里，结束就删。 */
    private static final class Session {
        final WabBridge bridge;
        final EntityMaid maid;
        final ServerPlayer owner;
        Phase phase = Phase.APPROACH;
        int phaseTicks;
        int hugTicks;
        int repaths;
        double stallDistance = Double.MAX_VALUE;
        int stallTicks;
        boolean mutual;
        Vec3 anchor = Vec3.ZERO;

        Session(WabBridge bridge, EntityMaid maid, ServerPlayer owner) {
            this.bridge = bridge;
            this.maid = maid;
            this.owner = owner;
        }
    }

    private static final Map<UUID, Session> SESSIONS = new HashMap<>();
    private static int autoTick;

    private WabHug() {
    }

    // ==================== 对外入口（WabFeatures.hug 调） ====================

    /**
     * request：她走过去抱主人（from=maid），或主人求抱、她决定答不答应（from=owner）。
     * 回执里 {@code ok=false} 的 code 就是放弃原因，AI 能直接拿去说人话。
     */
    public static JsonObject request(WabBridge bridge, EntityMaid maid, JsonObject p) {
        String from = WabJson.str(p, "from", "maid").toLowerCase(Locale.ROOT);
        boolean ownerAsked = from.equals("owner") || from.equals("player") || from.equals("owner_asked");
        if (maid == null || !maid.isAlive() || maid.level().isClientSide) {
            return fail("no_maid", "找不到这位女仆。");
        }
        if (bridge == null) return fail("not_connected", "桥没起来，抱抱走不通。");
        ServerPlayer owner = bridge.currentOwner();
        if (owner == null || !maid.isOwnedBy(owner)) return fail("no_owner", "主人不在这个存档里。");
        if (owner.level() != maid.level()) return fail("other_dimension", "主人和她在不同维度。");

        if (!WabConfigRuntime.connected()) return fail("not_connected", "没连上 WAB 时她不主动抱。");
        boolean enabled = WabConfigRuntime.on(WabConfigKeys.HUG_ENABLED);
        if (!enabled) return fail("disabled", "抱抱总开关没开（hug_enabled）。");
        if (!ownerAsked && !WabConfigRuntime.on(WabConfigKeys.HUG_AI_REQUEST_ENABLED)) {
            return fail("disabled", "AI 主动求抱没开（hug_ai_request_enabled）。");
        }

        Session running = SESSIONS.get(maid.getUUID());
        if (running != null) return fail("busy", "她已经在抱了。");
        if (bridge.dispatcher().hasJob(maid)) return fail("busy", "她正在干活，先让她停手。");

        double distance = Math.sqrt(maid.distanceToSqr(owner));
        boolean refusedRecently = refusedRecently(maid, cooldownTicks());
        if (!WabRules.hugContextAllowed(true, maidInCombat(maid), ownerInCombat(owner), refusedRecently,
                distance, WabConfigRuntime.number(WabConfigKeys.HUG_MAX_DISTANCE))) {
            if (refusedRecently) return fail("refused_recently", "主人刚拒绝过，她先不来了。");
            if (maidInCombat(maid) || ownerInCombat(owner)) return fail("in_combat", "有人在打架，这不是抱抱的时候。");
            return fail("too_far", "太远了，走过去抱不现实。");
        }

        if (ownerAsked) {
            int consent = WabRules.hugConsent(true, affection(bridge, maid), safety(bridge, maid),
                    WabConfigRuntime.number(WabConfigKeys.HUG_CLOSENESS_MIN), maid.getRandom().nextDouble());
            if (consent == 0) {
                sad(maid);
                return fail("refused", "她这会儿不想被抱。");
            }
            if (consent == 2) {
                maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.hug.shy");
            }
        }
        begin(bridge, maid, owner);
        JsonObject out = ok(maid, ownerAsked ? "她答应了，正在走过来。" : "她正走过去想抱主人。");
        out.addProperty("phase", "approach");
        out.addProperty("distance", Math.round(distance * 10) / 10.0);
        return out;
    }

    /** 主人拒绝：当场停，记下拒绝时间（一段时间内她不会再提）。 */
    public static JsonObject refuse(WabBridge bridge, EntityMaid maid, JsonObject p) {
        if (maid == null || maid.level().isClientSide) return fail("no_maid", "找不到这位女仆。");
        if (!WabConfigRuntime.on(WabConfigKeys.HUG_PLAYER_REFUSE_ENABLED)) {
            return fail("disabled", "「允许主人拒绝抱抱」没开。");
        }
        Session running = SESSIONS.remove(maid.getUUID());
        if (running != null) stopBody(maid);
        maid.getPersistentData().putLong(TAG_REFUSED_AT, maid.level().getGameTime());
        if (WabConfigRuntime.on(WabConfigKeys.HUG_SAD_ON_REFUSE)) sad(maid);
        if (bridge != null) {
            bridge.sendEvent("hug", maid, running == null ? "主人拒绝了抱抱。" : "主人拒绝了她的抱抱，她只好松开。",
                    0.3, 0, "hug_refused");
        }
        return ok(maid, "主人拒绝了，她记住了。");
    }

    /** 回抱：抱抱进行中主人反手抱住她——时长延长、多撒心心。 */
    public static JsonObject hugBack(WabBridge bridge, EntityMaid maid) {
        Session running = SESSIONS.get(maid.getUUID());
        if (running == null || running.phase != Phase.HUG) {
            return fail("not_hugging", "这会儿没在抱，回抱无从谈起。");
        }
        if (running.mutual) return fail("duplicate", "已经回抱过了。");
        running.mutual = true;
        running.hugTicks = Math.max(0, running.hugTicks - 60);
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.hug.back");
        NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.HEART, 26));
        maid.playSound(SoundEvents.CAT_PURR, 0.7f, 1.3f);
        if (bridge != null) bridge.sendEvent("hug", maid, "主人回抱了她。", 0.5, 0, "hug_back");
        return ok(maid, "她也抱紧了一点。");
    }

    /** 当前状态：给 AI 判断「现在能不能抱」用，只读。 */
    public static JsonObject status(EntityMaid maid) {
        JsonObject out = new JsonObject();
        out.addProperty("enabled", WabConfigRuntime.on(WabConfigKeys.HUG_ENABLED));
        if (maid == null) return out;
        Session running = SESSIONS.get(maid.getUUID());
        out.addProperty("active", running != null);
        out.addProperty("phase", running == null ? "idle" : running.phase.name().toLowerCase(Locale.ROOT));
        out.addProperty("mutual", running != null && running.mutual);
        out.addProperty("refusedRecently", refusedRecently(maid, cooldownTicks()));
        out.addProperty("inCombat", maidInCombat(maid));
        return out;
    }

    /** 当前在跑的桥；没连时是 null，调用方各自判空。 */
    public static WabBridge bridge() {
        return WabBridge.get();
    }

    public static boolean active(EntityMaid maid) {
        return maid != null && SESSIONS.containsKey(maid.getUUID());
    }

    /** 外部打断（换任务、睡觉、死亡、断桥）：静默收手，不刷消息。 */
    public static void cancel(EntityMaid maid, String reason) {
        if (maid == null) return;
        Session running = SESSIONS.remove(maid.getUUID());
        if (running == null) return;
        stopBody(maid);
        running.bridge.sendEvent("hug", maid, "抱抱中断：" + reason, 0.2, 0, "hug_interrupted");
    }

    public static void clear() {
        SESSIONS.clear();
    }

    // ==================== 每 tick 推进 ====================

    /** 由 WabBridge.tick() 每 tick 调用。 */
    public static void tick(WabBridge bridge) {
        if (SESSIONS.isEmpty()) {
            maybeInitiate(bridge);
            return;
        }
        for (UUID id : new ArrayList<>(SESSIONS.keySet())) {
            Session session = SESSIONS.get(id);
            if (session == null) continue;
            step(session);
        }
        maybeInitiate(bridge);
    }

    private static void step(Session s) {
        EntityMaid maid = s.maid;
        ServerPlayer owner = s.owner;
        if (!maid.isAlive() || maid.isRemoved() || maid.level().isClientSide) {
            end(s, "她不在了", false);
            return;
        }
        if (!owner.isAlive() || owner.level() != maid.level() || !maid.isOwnedBy(owner)) {
            end(s, "主人走远了", false);
            return;
        }
        if (maid.hurtTime > 0 || maid.getTarget() != null) {
            end(s, "被打断了", false);
            return;
        }
        if (maid.isSleeping()) {
            end(s, "她睡着了", false);
            return;
        }
        s.phaseTicks++;
        switch (s.phase) {
            case APPROACH -> approach(s);
            case FACE -> faceStep(s);
            case HUG -> hugStep(s);
        }
    }

    private static void approach(Session s) {
        EntityMaid maid = s.maid;
        ServerPlayer owner = s.owner;
        double distance = Math.sqrt(maid.distanceToSqr(owner));
        if (WabRules.hugInReach(distance)) {
            s.phase = Phase.FACE;
            s.phaseTicks = 0;
            maid.getNavigation().stop();
            return;
        }
        if (s.phaseTicks > APPROACH_TIMEOUT) {
            giveUp(s, "cannot_reach", "走了半天也过不去");
            return;
        }
        // 卡住判定：40 tick 内没靠近半格就重新寻路，重来三次还不行才算没路。
        if (distance <= s.stallDistance - 0.5) {
            s.stallDistance = distance;
            s.stallTicks = 0;
        } else if (++s.stallTicks > STALL_TICKS) {
            s.stallTicks = 0;
            s.stallDistance = distance;
            maid.getNavigation().stop();
            if (++s.repaths > MAX_REPATH) {
                giveUp(s, "cannot_reach", "路被挡住了");
                return;
            }
        }
        // 每 20 tick 重发一次目的地：主人边走边动时，一次 moveTo 的目标点很快就过期了。
        if (s.phaseTicks % 20 == 1 && !maid.getNavigation().moveTo(owner, WALK_SPEED)) {
            if (++s.repaths > MAX_REPATH) giveUp(s, "no_path", "找不到能走的路");
        }
        // F13 岩浆规避：她自己和主人脚下都是岩浆时不许靠近，宁可放弃也不踩进去。
        if (maid.isInLava() || owner.isInLava()) {
            giveUp(s, "lava", "脚下是岩浆，不能过去");
        }
        maid.getLookControl().setLookAt(owner, 30f, 30f);
    }

    private static void faceStep(Session s) {
        EntityMaid maid = s.maid;
        ServerPlayer owner = s.owner;
        double distance = Math.sqrt(maid.distanceToSqr(owner));
        if (!WabRules.hugInReach(distance)) {
            s.phase = Phase.APPROACH;
            s.phaseTicks = 0;
            s.stallDistance = distance;
            s.stallTicks = 0;
            return;
        }
        if (!maid.hasLineOfSight(owner)) {
            giveUp(s, "no_sight", "中间隔着东西");
            return;
        }
        face(maid, owner);
        boolean facing = ownerFacing(owner, maid);
        if (s.phaseTicks >= FACE_TIMEOUT) {
            if (!facing) {
                giveUp(s, "owner_turned_away", "主人背对着她");
                return;
            }
            startHug(s);
            return;
        }
        // 主人已经转过来就立刻抱，不必等满 60 tick。
        if (s.phaseTicks >= 8 && facing) startHug(s);
    }

    private static void hugStep(Session s) {
        EntityMaid maid = s.maid;
        ServerPlayer owner = s.owner;
        double moved = Math.sqrt(owner.position().distanceToSqr(s.anchor));
        double distance = Math.sqrt(maid.distanceToSqr(owner));
        if (WabRules.hugInterrupted(moved, maid.hurtTime > 0, owner.hurtTime > 0, maid.getTarget() != null)
                || distance > 3.0) {
            end(s, moved > WabRules.HUG_HOLD_SLACK ? "主人走开了" : "抱抱被打断了", false);
            return;
        }
        // 主人把脸转开就不算抱抱了：这条和「主人挪窝」是两件事，分开判。
        if (s.phaseTicks > 20 && !ownerFacing(owner, maid)) {
            end(s, "主人转开了", false);
            return;
        }
        face(maid, owner);
        if (s.phaseTicks % 15 == 0) {
            NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(),
                    SpawnParticleMessage.Type.HEART, s.mutual ? 8 : 4));
        }
        if (--s.hugTicks <= 0) end(s, "抱完了", true);
    }

    private static void startHug(Session s) {
        EntityMaid maid = s.maid;
        s.phase = Phase.HUG;
        s.phaseTicks = 0;
        s.anchor = s.owner.position();
        int seconds = (int) Math.round(WabConfigRuntime.number(WabConfigKeys.HUG_DURATION_SECONDS));
        s.hugTicks = Mth.clamp(seconds, 1, 10) * 20;
        maid.getNavigation().stop();
        face(maid, s.owner);
        NetworkHandler.sendToNearby(maid, new WabAnimationMessage(maid.getId(), "hug", s.hugTicks + 20));
        NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.HEART, 12));
        maid.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 0.6f, 1.3f);
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.hug.ok");
        maid.getPersistentData().putLong(TAG_LAST_HUG, maid.level().getGameTime());
        s.bridge.sendEvent("hug", maid, "她抱住了主人。", 0.5, 0, "hug");
    }

    private static void end(Session s, String reason, boolean held) {
        SESSIONS.remove(s.maid.getUUID());
        stopBody(s.maid);
        if (!held) {
            s.bridge.sendEvent("hug", s.maid, reason, 0.2, 0, "hug_interrupted");
        }
    }

    /** 放弃：清状态、说明原因、冒一句气泡；绝不留半截动作。 */
    private static void giveUp(Session s, String code, String why) {
        SESSIONS.remove(s.maid.getUUID());
        stopBody(s.maid);
        s.maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.hug.giveup");
        s.bridge.sendEvent("hug", s.maid, "没抱上：" + why + "。", 0.2, 0, code);
    }

    private static void stopBody(EntityMaid maid) {
        maid.getNavigation().stop();
        NetworkHandler.sendToNearby(maid, new WabAnimationMessage(maid.getId(), "", 0));
    }

    // ==================== 游戏侧频率（AI 判断之外的纯代码兜底） ====================

    private static void maybeInitiate(WabBridge bridge) {
        if (++autoTick % 40 != 0) return;
        if (!bridge.connected() || !WabConfigRuntime.connected()) return;
        if (!WabConfigRuntime.on(WabConfigKeys.HUG_ENABLED)) return;
        if (!WabConfigRuntime.on(WabConfigKeys.HUG_GAME_SIDE_ENABLED)) return;
        ServerPlayer owner = bridge.currentOwner();
        if (owner == null) return;
        double maxDistance = WabConfigRuntime.number(WabConfigKeys.HUG_MAX_DISTANCE);
        double probability = WabConfigRuntime.number(WabConfigKeys.HUG_REQUEST_PROBABILITY);
        long cooldownMs = cooldownTicks() * 50L;
        for (EntityMaid maid : bridge.ownedMaids()) {
            if (SESSIONS.containsKey(maid.getUUID())) continue;
            if (!maid.isAlive() || !maid.isOwnedBy(owner) || maid.level() != owner.level()) continue;
            if (bridge.dispatcher().hasJob(maid)) continue;
            double distance = Math.sqrt(maid.distanceToSqr(owner));
            if (!WabRules.hugContextAllowed(true, maidInCombat(maid), ownerInCombat(owner),
                    refusedRecently(maid, cooldownTicks()), distance, maxDistance)) {
                continue;
            }
            long now = maid.level().getGameTime();
            CompoundTag data = maid.getPersistentData();
            long last = Math.max(data.getLong(TAG_LAST_HUG), data.getLong(TAG_REFUSED_AT));
            long elapsed = Math.max(0L, now - last) * 50L;
            if (!WabRules.shouldRequestHug(true, elapsed, cooldownMs, probability,
                    safety(bridge, maid), mood(bridge, maid), maid.getRandom().nextDouble())) {
                continue;
            }
            begin(bridge, maid, owner);
        }
    }

    private static void begin(WabBridge bridge, EntityMaid maid, ServerPlayer owner) {
        Session session = new Session(bridge, maid, owner);
        session.stallDistance = Math.sqrt(maid.distanceToSqr(owner));
        SESSIONS.put(maid.getUUID(), session);
        // 一发起就记时间：半路放弃也算一次尝试，免得没过冷却又每 40 tick 重来一遍。
        maid.getPersistentData().putLong(TAG_LAST_HUG, maid.level().getGameTime());
        if (maid.isInSittingPose()) maid.setInSittingPose(false);
        maid.getNavigation().stop();
        maid.getLookControl().setLookAt(owner, 30f, 30f);
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.hug.request");
    }

    // ==================== 感知 ====================

    /** 好感 0~1：优先用 WAB 推来的权威数值，没有就用模组好感度（满 384 折算）。 */
    private static double affection(WabBridge bridge, EntityMaid maid) {
        WabHud.Stats stats = bridge == null ? null : bridge.stats(maid.getUUID());
        if (stats != null && stats.receivedAt > 0) return clamp01(stats.affection / 100.0);
        return clamp01(maid.getFavorability() / 384.0);
    }

    /** 安全感 0~1：信任减恐惧，各按 0~100 计，中心点 0.5。 */
    private static double safety(WabBridge bridge, EntityMaid maid) {
        JsonObject emotions = WabSensors.loveLoathe(maid);
        int trust;
        int fear;
        if (emotions != null) {
            trust = WabJson.integer(emotions, "trust", 40);
            fear = WabJson.integer(emotions, "fear", 10);
        } else {
            WabHud.Stats stats = bridge == null ? null : bridge.stats(maid.getUUID());
            trust = stats == null ? 40 : stats.trust;
            fear = 10;
        }
        return clamp01((trust - fear + 100) / 200.0);
    }

    /** 心情 0~1：宿主推的 mood 按 −100~100 归一化，没有数值时给中性 0.5。 */
    private static double mood(WabBridge bridge, EntityMaid maid) {
        WabHud.Stats stats = bridge == null ? null : bridge.stats(maid.getUUID());
        if (stats == null || stats.receivedAt == 0) return 0.5;
        return clamp01(0.5 + stats.mood / 200.0);
    }

    private static boolean maidInCombat(EntityMaid maid) {
        return maid.getTarget() != null || maid.hurtTime > 0;
    }

    private static boolean ownerInCombat(ServerPlayer owner) {
        return owner.hurtTime > 0;
    }

    /** 拒绝退让期就是冷却时长：她不会在冷却里再来第二次。 */
    private static long cooldownTicks() {
        double seconds = WabConfigRuntime.number(WabConfigKeys.HUG_COOLDOWN_SECONDS);
        return (long) Math.max(20, Math.min(86_400, seconds * 20));
    }

    private static boolean refusedRecently(EntityMaid maid, long windowTicks) {
        long refusedAt = maid.getPersistentData().getLong(TAG_REFUSED_AT);
        return refusedAt > 0 && maid.level().getGameTime() - refusedAt < windowTicks;
    }

    private static void sad(EntityMaid maid) {
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.hug.sad");
        maid.playSound(SoundEvents.CAT_HISS, 0.4f, 0.8f);
    }

    /** 她转向目标：头、身、脚一起转，模型才不会拧着脖子抱。 */
    private static void face(EntityMaid maid, Entity target) {
        double dx = target.getX() - maid.getX();
        double dz = target.getZ() - maid.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        if (horizontal > 1.0E-4) {
            float yaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
            maid.setYRot(yaw);
            maid.setYBodyRot(yaw);
            maid.setYHeadRot(yaw);
        }
        double dy = target.getEyeY() - maid.getEyeY();
        float pitch = (float) -(Mth.atan2(dy, Math.max(0.001, horizontal)) * (180.0 / Math.PI));
        maid.setXRot(pitch * 0.5f);
        maid.getLookControl().setLookAt(target, 30f, 30f);
    }

    /** 主人有没有转过脸来：视线方向与「主人→她」的夹角在 120° 内算面对面。 */
    private static boolean ownerFacing(ServerPlayer owner, EntityMaid maid) {
        Vec3 look = owner.getLookAngle();
        double dx = maid.getX() - owner.getX();
        double dz = maid.getZ() - owner.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        double lookLength = Math.sqrt(look.x * look.x + look.z * look.z);
        if (length < 0.5 || lookLength < 0.1) return true;
        double cos = (look.x * dx + look.z * dz) / (length * lookLength);
        return cos > -0.5;
    }

    private static double clamp01(double value) {
        if (Double.isNaN(value)) return 0;
        return Math.max(0, Math.min(1, value));
    }

    private static JsonObject ok(EntityMaid maid, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("maid", maid.getUUID().toString());
        out.addProperty("message", message);
        return out;
    }

    private static JsonObject fail(String code, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("ok", false);
        out.addProperty("code", code);
        out.addProperty("error", message);
        return out;
    }

    // ==================== 受伤即中断 ====================

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide || SESSIONS.isEmpty()) return;
        if (event.getEntity() instanceof EntityMaid maid) {
            if (SESSIONS.containsKey(maid.getUUID())) cancel(maid, "她受伤了");
            return;
        }
        if (event.getEntity() instanceof ServerPlayer player) {
            List<UUID> hit = new ArrayList<>();
            for (Session session : SESSIONS.values()) {
                if (session.owner == player) hit.add(session.maid.getUUID());
            }
            for (UUID id : hit) {
                Session session = SESSIONS.get(id);
                if (session != null) cancel(session.maid, "主人受伤了");
            }
        }
    }
}
