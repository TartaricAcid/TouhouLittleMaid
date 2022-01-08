package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class WirelessIOSlotConfigMessage {
    private static final byte[] EMPTY = new byte[]{};
    private final byte[] configData;

    public WirelessIOSlotConfigMessage(byte[] configData) {
        this.configData = configData;
    }

    public WirelessIOSlotConfigMessage() {
        this(EMPTY);
    }

    public static void encode(WirelessIOSlotConfigMessage message, PacketBuffer buf) {
        buf.writeByteArray(message.configData);
    }

    public static WirelessIOSlotConfigMessage decode(PacketBuffer buf) {
        return new WirelessIOSlotConfigMessage(buf.readByteArray());
    }

    public static void handle(WirelessIOSlotConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemStack handItem = sender.getMainHandItem();
                if (handItem.getItem() == InitItems.WIRELESS_IO.get()) {
                    if (message.configData.length > 0) {
                        ItemWirelessIO.setSlotConfig(handItem, message.configData);
                    }
                    NetworkHooks.openGui(sender, (ItemWirelessIO) handItem.getItem(), (buffer) -> buffer.writeItem(handItem));
                }
            });
        }
        context.setPacketHandled(true);
    }
}
