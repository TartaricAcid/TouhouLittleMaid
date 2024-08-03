package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGui;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenBeaconGuiMessage {
    private final BlockPos pos;

    public OpenBeaconGuiMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(OpenBeaconGuiMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }

    public static OpenBeaconGuiMessage decode(FriendlyByteBuf buf) {
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
        BlockEntity te = mc.level.getBlockEntity(message.pos);
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityMaidBeacon) {
            mc.setScreen(new MaidBeaconGui((TileEntityMaidBeacon) te));
        }
    }
}
