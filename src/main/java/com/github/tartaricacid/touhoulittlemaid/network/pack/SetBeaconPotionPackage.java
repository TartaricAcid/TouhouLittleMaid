package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SetBeaconPotionPackage(BlockPos pos, int potionIndex) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetBeaconPotionPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("set_beacon_potion"));
    public static final StreamCodec<ByteBuf, SetBeaconPotionPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SetBeaconPotionPackage::pos,
            ByteBufCodecs.VAR_INT,
            SetBeaconPotionPackage::potionIndex,
            SetBeaconPotionPackage::new
    );

    public static void handle(SetBeaconPotionPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                Level world = sender.level();
                if (world.isLoaded(message.pos)) {
                    BlockEntity te = world.getBlockEntity(message.pos);
                    if (te instanceof TileEntityMaidBeacon) {
                        ((TileEntityMaidBeacon) te).setPotionIndex(message.potionIndex);
                    }
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
