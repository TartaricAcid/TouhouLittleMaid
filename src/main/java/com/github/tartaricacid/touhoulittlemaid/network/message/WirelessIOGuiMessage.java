package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WirelessIOGuiMessage {
    private final boolean isMaidToChest;
    private final boolean isBlacklist;

    public WirelessIOGuiMessage(boolean isMaidToChest, boolean isBlacklist) {
        this.isMaidToChest = isMaidToChest;
        this.isBlacklist = isBlacklist;
    }

    public static void encode(WirelessIOGuiMessage message, PacketBuffer buf) {
        buf.writeBoolean(message.isMaidToChest);
        buf.writeBoolean(message.isBlacklist);
    }

    public static WirelessIOGuiMessage decode(PacketBuffer buf) {
        return new WirelessIOGuiMessage(buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(WirelessIOGuiMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemStack handItem = sender.getMainHandItem();
                if (handItem.getItem() == InitItems.WIRELESS_IO.get()) {
                    ItemWirelessIO.setMode(handItem, message.isMaidToChest);
                    ItemWirelessIO.setFilterMode(handItem, message.isBlacklist);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
