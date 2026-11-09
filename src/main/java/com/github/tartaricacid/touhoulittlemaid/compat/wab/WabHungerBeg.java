package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

/**
 * 模组侧的饥饿讨食兜底：连桥时按 tick 检查绑定的女仆，饿到阈值就冒一句气泡向主人讨吃的。
 * 阈值与方向由 WabConfig 决定（默认数值越大越饿、阈值 14），和插件里的每回合检查共用同一个饥饿值。
 * 只在桥连着（WAB 接管饥饿）时跑；断桥时这就是普通饥饿，不主动讨食。
 */
public final class WabHungerBeg {
    private static final long COOLDOWN_TICKS = 2400;
    private static final String TAG_LAST = "WabHungerBegLast";
    private static final String BUBBLE = "chat_bubble.touhou_little_maid.hungry.beg";

    private WabHungerBeg() {
    }

    public static void tick(WabBridge bridge) {
        if (!WabConfig.hungerBeg()) return;
        for (EntityMaid maid : bridge.ownedMaids()) {
            maybeBeg(maid);
        }
    }

    private static void maybeBeg(EntityMaid maid) {
        if (maid == null || !maid.isAlive() || maid.level().isClientSide) return;
        if (!(maid.getOwner() instanceof ServerPlayer owner)) return;
        if (!withinReach(owner, maid)) return;
        int hunger = maid.getHunger();
        int threshold = WabConfig.hungerBegThreshold();
        boolean hungry = WabConfig.hungerBegHigher() ? (hunger >= threshold) : (hunger <= threshold);
        if (!hungry) return;
        long now = maid.level().getGameTime();
        CompoundTag data = maid.getPersistentData();
        if (now - data.getLong(TAG_LAST) < COOLDOWN_TICKS) return;
        data.putLong(TAG_LAST, now);
        maid.getChatBubbleManager().addTextChatBubble(BUBBLE);
    }

    private static boolean withinReach(ServerPlayer owner, EntityMaid maid) {
        return owner.distanceToSqr(maid) <= 400.0
                || owner.hasLineOfSight(maid)
                || maid.hasLineOfSight(owner);
    }
}
