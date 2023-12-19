package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnParticleMessage {
    private final int entityId;
    private final Type particleType;

    public SpawnParticleMessage(int entityId, Type particleType) {
        this.entityId = entityId;
        this.particleType = particleType;
    }

    public static void encode(SpawnParticleMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeInt(message.particleType.ordinal());
    }

    public static SpawnParticleMessage decode(FriendlyByteBuf buf) {
        return new SpawnParticleMessage(buf.readInt(), getTypeByIndex(buf.readInt()));
    }

    public static void handle(SpawnParticleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleSpawnParticle(message));
        }
        context.setPacketHandled(true);
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
        EXPLOSION, BUBBLE, HEART
    }
}
