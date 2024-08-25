package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SpawnParticlePackage(int entityId, Type particleType, int delayTicks) implements CustomPacketPayload {
    public SpawnParticlePackage(int entityId, Type particleType) {
        this(entityId, particleType, 0);
    }

    public static final CustomPacketPayload.Type<SpawnParticlePackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("spawn_particle"));
    public static final StreamCodec<ByteBuf, SpawnParticlePackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SpawnParticlePackage::entityId,
            Type.STREAM_CODEC,
            SpawnParticlePackage::particleType,
            ByteBufCodecs.VAR_INT,
            SpawnParticlePackage::delayTicks,
            SpawnParticlePackage::new
    );

    public static void handle(SpawnParticlePackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            if (message.delayTicks <= 0) {
                context.enqueueWork(() -> handleSpawnParticle(message));
            } else {
                context.enqueueWork(() -> CompletableFuture.runAsync(() -> handleSpawnParticleDelay(message, message.delayTicks), Util.backgroundExecutor()));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSpawnParticleDelay(SpawnParticlePackage message, int delayTicks) {
        try {
            Thread.sleep(delayTicks * 50L);
            Minecraft.getInstance().submitAsync(() -> handleSpawnParticle(message));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleSpawnParticle(SpawnParticlePackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.entityId);
        if (e instanceof EntityMaid maid && e.isAlive()) {
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

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Type {
        /**
         * 粒子类型
         */
        EXPLOSION, BUBBLE, HEART, RANK_UP, HEAL;
        public static final IntFunction<Type> BY_ID =
                ByIdMap.continuous(
                        Type::ordinal,
                        Type.values(),
                        ByIdMap.OutOfBoundsStrategy.ZERO
                );
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(Type.BY_ID, Type::ordinal);
    }
}
