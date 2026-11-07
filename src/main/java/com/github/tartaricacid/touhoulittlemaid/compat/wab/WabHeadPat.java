package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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
 * 前提是主人就在她 5 格半径内。讨摸时头顶冒一句气泡，主人右键她的头部即算摸到，
 * 冒心心并收下这句「好舒服」。只在服务端判定与结算，客户端只负责显示气泡/爱心。
 */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WabHeadPat {
    private static final float CHANCE = 0.30f;
    /** 主人在她半径 5 格的圆内（平方距离）。 */
    private static final double OWNER_RANGE_SQR = 25.0;
    /** 讨摸状态保留时长（tick）：10 秒内没被摸就作罢。 */
    private static final int WINDOW_TICKS = 200;
    /** 两次讨摸之间的冷却，避免刷屏。 */
    private static final int COOLDOWN_TICKS = 1200;
    private static final String TAG_UNTIL = "WabPatUntil";
    private static final String TAG_LAST = "WabPatLast";

    private WabHeadPat() {
    }

    /** 一次「干完事」之后调这个：满足条件就按 30% 概率发起讨摸摸。 */
    public static void maybeRequest(EntityMaid maid) {
        if (maid == null || !maid.isAlive() || !maid.isTame()) return;
        if (maid.level().isClientSide) return;
        if (!(maid.getOwner() instanceof ServerPlayer owner)) return;
        if (owner.distanceToSqr(maid) > OWNER_RANGE_SQR) return;
        long now = maid.level().getGameTime();
        CompoundTag data = maid.getPersistentData();
        if (now - data.getLong(TAG_LAST) < COOLDOWN_TICKS) return;
        if (maid.getRandom().nextFloat() >= CHANCE) return;
        data.putLong(TAG_LAST, now);
        data.putLong(TAG_UNTIL, now + WINDOW_TICKS);
        ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.head_pat.request");
    }

    private static boolean wantsPat(EntityMaid maid) {
        if (maid.level().isClientSide) return false;
        CompoundTag data = maid.getPersistentData();
        long until = data.getLong(TAG_UNTIL);
        if (until <= 0) return false;
        if (maid.level().getGameTime() > until) {
            data.remove(TAG_UNTIL);
            return false;
        }
        return true;
    }

    private static void pet(EntityMaid maid) {
        maid.getPersistentData().remove(TAG_UNTIL);
        ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.head_pat.ok");
        NetworkHandler.sendToNearby(maid, new SpawnParticlePackage(maid.getId(), SpawnParticlePackage.Type.HEART, 12));
        maid.playSound(SoundEvents.CAT_PURR, 0.6f, 1.4f);
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
