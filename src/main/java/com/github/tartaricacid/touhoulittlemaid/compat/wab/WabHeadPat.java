package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabConfigKeys;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabConfigRuntime;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabRules;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticlePackage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * 摸头：她打完敌对生物、干完一件活、或被喂过之后，有 30% 概率向主人讨摸摸——
 * 前提是主人在她 20 格内，或彼此视线通畅（看得见）。讨摸时头顶冒一句气泡，主人右键她的头部即算摸到，
 * 冒心心并收下这句「好舒服」。只在服务端判定与结算，客户端只负责显示气泡/爱心。
 */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WabHeadPat {
    private static final float CHANCE = 0.30f;
    /** 主人在她半径 20 格的圆内（平方距离）；超出范围时退化为「彼此看得见」。 */
    private static final double OWNER_RANGE_SQR = 400.0;
    /** 讨摸状态保留时长（tick）：10 秒内没被摸就作罢。 */
    private static final int WINDOW_TICKS = 200;
    /** 两次讨摸之间的冷却，避免刷屏。 */
    private static final int COOLDOWN_TICKS = 1200;
    private static final String TAG_UNTIL = "WabPatUntil";
    private static final String TAG_LAST = "WabPatLast";
    private static final String TAG_BUBBLE = "WabPatBubble";
    private static final String TAG_TIER = "WabPatTier";
    private static final String TAG_POSE = "WabPatPose";
    private static final String TAG_SAD = "WabPatSadUntil";

    private WabHeadPat() {
    }

    /** 一次「干完事」之后调这个：满足条件就按 30% 概率发起讨摸摸。 */
    public static void maybeRequest(EntityMaid maid) {
        maybeRequest(maid, false);
    }

    /**
     * @param fromFeed 是否由「被投喂」触发（F02）。投喂走独立概率 0=关闭，
     *                 干完活 / 击杀仍走原来的 R11 概率。
     */
    public static void maybeRequest(EntityMaid maid, boolean fromFeed) {
        if (maid == null || !maid.isAlive() || !maid.isTame()) return;
        if (maid.level().isClientSide) return;
        if (!(maid.getOwner() instanceof ServerPlayer owner)) return;
        if (!withinReach(owner, maid)) return;

        // F01 硬闸门：战斗中、或没掉过血，一律不请求摸摸。断桥时也生效（这是安全约束，不是 WAB 下发功能）。
        // hurtTime 是双线共有的 public 字段（IMaid 也用它判断 isHurt），比版本相关的时间戳 API 稳。
        boolean inCombat = maid.getTarget() != null || maid.hurtTime > 0;
        boolean hasLostHealth = maid.getHealth() < maid.getMaxHealth() - 0.01f;
        if (!WabRules.headpatContextAllowed(inCombat, hasLostHealth)) return;

        long now = maid.level().getGameTime();
        CompoundTag data = maid.getPersistentData();
        if (now - data.getLong(TAG_LAST) < COOLDOWN_TICKS) return;
        if (fromFeed) {
            // F02：投喂后独立概率，0 即关闭；不再借用「干完活」那条概率。
            double feedProbability = WabConfigRuntime.connected()
                    ? WabConfigRuntime.number(WabConfigKeys.HEADPAT_ON_FEED_PROBABILITY) : 0;
            if (!WabRules.headpatOnFeed(feedProbability, maid.getRandom().nextFloat())) return;
        } else {
            // 断桥时保持原版 30% 讨摸；连上 WAB 后一律听配置（保守默认 0.05）。
            double probability = WabConfigRuntime.connected()
                    ? WabConfigRuntime.number(WabConfigKeys.HEADPAT_REQUEST_PROBABILITY) : CHANCE;
            boolean enabled = !WabConfigRuntime.connected()
                    || WabConfigRuntime.on(WabConfigKeys.HEADPAT_ENABLED);
            if (!WabRules.shouldRequestHeadpat(enabled, probability, maid.getRandom().nextFloat())) return;
        }
        int tier = WabRules.headpatTier(
                WabConfigRuntime.integer(WabConfigKeys.HEADPAT_REACTION_TIER),
                WabConfigRuntime.number(WabConfigKeys.HEADPAT_TIER3_PROBABILITY),
                maid.getRandom().nextFloat());
        data.putInt(TAG_TIER, tier);
        data.putLong(TAG_LAST, now);
        data.putLong(TAG_UNTIL, now + WINDOW_TICKS);
        data.putLong(TAG_BUBBLE,
                maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.head_pat.request"));
    }

    /** 主人离她 20 格内，或主人看得见她、或她看得见主人，都算够得着。 */
    private static boolean withinReach(ServerPlayer owner, EntityMaid maid) {
        return owner.distanceToSqr(maid) <= OWNER_RANGE_SQR
                || owner.hasLineOfSight(maid)
                || maid.hasLineOfSight(owner);
    }

    private static boolean wantsPat(EntityMaid maid) {
        if (maid.level().isClientSide) return false;
        CompoundTag data = maid.getPersistentData();
        long until = data.getLong(TAG_UNTIL);
        if (until <= 0) return false;
        if (maid.level().getGameTime() > until) {
            clearRequestBubble(maid);
            data.remove(TAG_UNTIL);
            return false;
        }
        return true;
    }

    private static void pet(EntityMaid maid) {
        clearRequestBubble(maid);
        CompoundTag data = maid.getPersistentData();
        data.remove(TAG_UNTIL);
        int tier = Math.max(1, Math.min(3, data.getInt(TAG_TIER)));
        // 三档反应：档位与姿势写进持久数据，动画层据此播俯身/抱尾巴/缩成团；
        // 小酒壶不俯身（原版特例）。这里先给出可见的粒子、音高与文案强度差异。
        String pose = switch (tier) {
            case 2 -> "hug_tail";
            case 3 -> "curl_up";
            default -> "bend_ears_squint";
        };
        data.putString(TAG_POSE, pose);
        int particles = switch (tier) { case 2 -> 18; case 3 -> 26; default -> 12; };
        float pitch = switch (tier) { case 2 -> 1.6f; case 3 -> 1.9f; default -> 1.4f; };
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.head_pat.ok");
        NetworkHandler.sendToNearby(maid, new SpawnParticlePackage(maid.getId(), SpawnParticlePackage.Type.HEART, particles));
        maid.playSound(SoundEvents.CAT_PURR, 0.6f, pitch);
    }

    /** 玩家拒绝摸头：清掉讨摸气泡，露出伤心表情并留下短暂伤心状态。 */
    public static void refuse(EntityMaid maid) {
        if (maid == null || !maid.isAlive() || maid.level().isClientSide) return;
        clearRequestBubble(maid);
        CompoundTag data = maid.getPersistentData();
        data.remove(TAG_UNTIL);
        data.putString(TAG_POSE, "sad");
        data.putLong(TAG_SAD, maid.level().getGameTime() + 200L);
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.head_pat.sad");
        maid.playSound(SoundEvents.CAT_HISS, 0.4f, 0.8f);
    }

    /** AI 主动请求摸头：走与「干完活」相同的概率与冷却，不受事件触发限制。 */
    public static void requestNow(EntityMaid maid) {
        maybeRequest(maid);
    }

    /** 立刻抹掉「讨摸摸」那条气泡，避免它和「好舒服」叠着留很久。 */
    private static void clearRequestBubble(EntityMaid maid) {
        CompoundTag data = maid.getPersistentData();
        long bubble = data.getLong(TAG_BUBBLE);
        if (bubble > 0 && maid.getChatBubbleManager().getChatBubble(bubble) != null) {
            maid.getChatBubbleManager().removeChatBubble(bubble);
        }
        data.remove(TAG_BUBBLE);
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof Enemy)) return;
        if (event.getSource().getEntity() instanceof EntityMaid maid) maybeRequest(maid);
    }

    /** 右键她的头部：她正讨摸摸时就算摸到，并吞掉这次交互（不再开界面）。 */
    @SubscribeEvent
    public static void onInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!(event.getTarget() instanceof EntityMaid maid) || !maid.isOwnedBy(player)) return;
        if (!wantsPat(maid)) return;
        Vec3 local = event.getLocalPos();
        if (local.y < maid.getBbHeight() * 0.7) return;
        event.setCanceled(true);
        pet(maid);
    }
}
