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

    private WabHeadPat() {
    }

    /** 一次「干完事」之后调这个：满足条件就按 30% 概率发起讨摸摸。 */
    public static void maybeRequest(EntityMaid maid) {
        if (maid == null || !maid.isAlive() || !maid.isTame()) return;
        if (maid.level().isClientSide) return;
        if (!(maid.getOwner() instanceof ServerPlayer owner)) return;
        if (!withinReach(owner, maid)) return;
        long now = maid.level().getGameTime();
        CompoundTag data = maid.getPersistentData();
        if (now - data.getLong(TAG_LAST) < COOLDOWN_TICKS) return;
        // 断桥时保持原版 30% 讨摸；连上 WAB 后一律听配置（保守默认 0.05）。
        double probability = WabConfigRuntime.connected()
                ? WabConfigRuntime.number(WabConfigKeys.HEADPAT_REQUEST_PROBABILITY) : CHANCE;
        boolean enabled = !WabConfigRuntime.connected()
                || WabConfigRuntime.on(WabConfigKeys.HEADPAT_ENABLED);
        if (!WabRules.shouldRequestHeadpat(enabled, probability, maid.getRandom().nextFloat())) return;
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
        maid.getPersistentData().remove(TAG_UNTIL);
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.head_pat.ok");
        NetworkHandler.sendToNearby(maid, new SpawnParticlePackage(maid.getId(), SpawnParticlePackage.Type.HEART, 12));
        maid.playSound(SoundEvents.CAT_PURR, 0.6f, 1.4f);
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
