package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record BeaconAbsorbPackage(float x, float y, float z) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BeaconAbsorbPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("beacon_absorb"));
    public static final StreamCodec<ByteBuf, BeaconAbsorbPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            BeaconAbsorbPackage::x,
            ByteBufCodecs.FLOAT,
            BeaconAbsorbPackage::y,
            ByteBufCodecs.FLOAT,
            BeaconAbsorbPackage::z,
            BeaconAbsorbPackage::new
    );

    public static void handle(BeaconAbsorbPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> spawnParticle(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void spawnParticle(BeaconAbsorbPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            EntityPowerPoint.spawnExplosionParticle(mc.level, message.x, message.y, message.z, mc.level.random);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
