package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetCompassModeMessage implements IMessage {
    ItemKappaCompass.Mode mode;

    public SetCompassModeMessage() {
    }

    public SetCompassModeMessage(ItemKappaCompass.Mode mode) {
        this.mode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mode = ItemKappaCompass.Mode.getModeByIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.mode.ordinal());
    }

    public static class Handler implements IMessageHandler<SetCompassModeMessage, IMessage> {
        @Override
        public IMessage onMessage(SetCompassModeMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    ItemStack stack = ctx.getServerHandler().player.getHeldItemMainhand();
                    ItemKappaCompass.setMode(stack, message.mode);
                });
            }
            return null;
        }
    }
}
