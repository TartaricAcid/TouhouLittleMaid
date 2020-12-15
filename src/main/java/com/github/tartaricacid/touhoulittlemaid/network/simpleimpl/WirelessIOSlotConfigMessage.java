package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class WirelessIOSlotConfigMessage implements IMessage {
    private byte[] configData;
    private boolean isClose;

    public WirelessIOSlotConfigMessage() {
    }

    public WirelessIOSlotConfigMessage(byte[] configData, boolean isClose) {
        this.configData = configData;
        this.isClose = isClose;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        configData = new byte[length];
        for (int i = 0; i < length; i++) {
            configData[i] = buf.readByte();
        }
        isClose = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(configData.length);
        for (byte configDatum : configData) {
            buf.writeByte(configDatum);
        }
        buf.writeBoolean(isClose);
    }

    public static class Handler implements IMessageHandler<WirelessIOSlotConfigMessage, IMessage> {
        @Override
        public IMessage onMessage(WirelessIOSlotConfigMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    ItemStack stack = player.getHeldItemMainhand();
                    if (stack.getItem() == MaidItems.WIRELESS_IO) {
                        ItemWirelessIO.setSlotConfig(stack, message.configData);
                        if (!message.isClose) {
                            player.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.WIRELESS_IO.getId(), player.world, 0, 0, 0);
                        }
                    }
                });
            }
            return null;
        }
    }
}
