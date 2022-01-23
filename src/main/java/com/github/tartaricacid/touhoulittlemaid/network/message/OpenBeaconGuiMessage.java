package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGui;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenBeaconGuiMessage {
    private final BlockPos pos;

    public OpenBeaconGuiMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(OpenBeaconGuiMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
    }

    public static OpenBeaconGuiMessage decode(PacketBuffer buf) {
        return new OpenBeaconGuiMessage(buf.readBlockPos());
    }

    public static void handle(OpenBeaconGuiMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleOpenGui(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOpenGui(OpenBeaconGuiMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        TileEntity te = mc.level.getBlockEntity(message.pos);
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityMaidBeacon) {
            mc.setScreen(new MaidBeaconGui((TileEntityMaidBeacon) te));
        }
    }
}
