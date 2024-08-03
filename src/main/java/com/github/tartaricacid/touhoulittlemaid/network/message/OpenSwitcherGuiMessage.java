package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.block.ModelSwitcherGui;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenSwitcherGuiMessage {
    private final BlockPos pos;

    public OpenSwitcherGuiMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(OpenSwitcherGuiMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }

    public static OpenSwitcherGuiMessage decode(FriendlyByteBuf buf) {
        return new OpenSwitcherGuiMessage(buf.readBlockPos());
    }

    public static void handle(OpenSwitcherGuiMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleOpenGui(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOpenGui(OpenSwitcherGuiMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        BlockEntity te = mc.level.getBlockEntity(message.pos);
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityModelSwitcher) {
            mc.setScreen(new ModelSwitcherGui((TileEntityModelSwitcher) te));
        }
    }
}
