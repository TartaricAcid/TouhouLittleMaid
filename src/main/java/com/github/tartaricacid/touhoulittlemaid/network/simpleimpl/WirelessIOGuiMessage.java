package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class WirelessIOGuiMessage implements IMessage {
    private boolean isInput;
    private boolean isBlacklist;

    public WirelessIOGuiMessage() {
    }

    public WirelessIOGuiMessage(boolean isInput, boolean isBlacklist) {
        this.isInput = isInput;
        this.isBlacklist = isBlacklist;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isInput = buf.readBoolean();
        isBlacklist = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isInput);
        buf.writeBoolean(isBlacklist);
    }

    public static class Handler implements IMessageHandler<WirelessIOGuiMessage, IMessage> {
        @Override
        public IMessage onMessage(WirelessIOGuiMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    ItemStack stack = player.getHeldItemMainhand();
                    if (stack.getItem() == MaidItems.WIRELESS_IO) {
                        ItemWirelessIO.setIOMode(stack, message.isInput);
                        ItemWirelessIO.setFilterMode(stack, message.isBlacklist);
                    }
                });
            }
            return null;
        }
    }
}
