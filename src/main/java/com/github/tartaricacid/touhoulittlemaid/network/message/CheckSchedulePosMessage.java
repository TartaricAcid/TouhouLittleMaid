package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.CheckSchedulePosGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CheckSchedulePosMessage {
    private final ITextComponent tips;

    public CheckSchedulePosMessage(ITextComponent tips) {
        this.tips = tips;
    }

    public static void encode(CheckSchedulePosMessage message, PacketBuffer buf) {
        buf.writeComponent(message.tips);
    }

    public static CheckSchedulePosMessage decode(PacketBuffer buf) {
        return new CheckSchedulePosMessage(buf.readComponent());
    }

    public static void handle(CheckSchedulePosMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(CheckSchedulePosMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (mc.screen instanceof AbstractMaidContainerGui<?>) {
            AbstractMaidContainerGui<?> parent = (AbstractMaidContainerGui<?>) mc.screen;
            mc.setScreen(new CheckSchedulePosGui(parent, message.tips));
        }
    }
}
