package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SpawnParticleMessage {
    private final int entityId;
    private final Type particleType;
    private final int delayTicks;

    public SpawnParticleMessage(int entityId, Type particleType, int delayTicks) {
        this.entityId = entityId;
        this.particleType = particleType;
        this.delayTicks = delayTicks;
    }

    public SpawnParticleMessage(int entityId, Type particleType) {
        this(entityId, particleType, 0);
    }

    public static void encode(SpawnParticleMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeInt(message.particleType.ordinal());
        buf.writeVarInt(message.delayTicks);
    }

    public static SpawnParticleMessage decode(FriendlyByteBuf buf) {
        return new SpawnParticleMessage(buf.readInt(), getTypeByIndex(buf.readInt()), buf.readVarInt());
    }

    public static void handle(SpawnParticleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            if (message.delayTicks <= 0) {
                context.enqueueWork(() -> handleSpawnParticle(message));
            } else {
                context.enqueueWork(() -> CompletableFuture.runAsync(() -> handleSpawnParticleDelay(message, message.delayTicks), Util.backgroundExecutor()));
            }
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSpawnParticleDelay(SpawnParticleMessage message, int delayTicks) {
        try {
            Thread.sleep(delayTicks * 50L);
            Minecraft.getInstance().submitAsync(() -> handleSpawnParticle(message));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSpawnParticle(SpawnParticleMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.entityId);
        if (e instanceof EntityMaid && e.isAlive()) {
            EntityMaid maid = (EntityMaid) e;
            switch (message.particleType) {
                case EXPLOSION:
                    maid.spawnExplosionParticle();
                    return;
                case BUBBLE:
                    maid.spawnBubbleParticle();
                    return;
                case HEART:
                    maid.spawnHeartParticle();
                    return;
                case RANK_UP:
                    maid.spawnRankUpParticle();
                    return;
                case HEAL:
                    maid.spawnRestoreHealthParticle(maid.getRandom().nextInt(3) + 7);
                    return;
                default:
            }
        }
    }

    private static Type getTypeByIndex(int index) {
        return Type.values()[Mth.clamp(index, 0, Type.values().length - 1)];
    }

    public enum Type {
        /**
         * 粒子类型
         */
        EXPLOSION, BUBBLE, HEART, RANK_UP, HEAL
    }
}
