package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGui;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record OpenBeaconGuiPackage(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenBeaconGuiPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("open_beacon_gui"));
    public static final StreamCodec<ByteBuf, OpenBeaconGuiPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenBeaconGuiPackage::pos,
            OpenBeaconGuiPackage::new
    );

    public static void handle(OpenBeaconGuiPackage message, IPayloadContext context) {
        context.enqueueWork(() -> handleOpenGui(message));
    }

    private static void handleOpenGui(OpenBeaconGuiPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        BlockEntity te = mc.level.getBlockEntity(message.pos);
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityMaidBeacon) {
            mc.setScreen(new MaidBeaconGui((TileEntityMaidBeacon) te));
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
