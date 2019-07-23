package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChangeGoheiMessage implements IMessage {
    private boolean next;

    public ChangeGoheiMessage() {
    }

    public ChangeGoheiMessage(boolean next) {
        this.next = next;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        next = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(next);
    }

    public static class Handler implements IMessageHandler<ChangeGoheiMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeGoheiMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    ItemStack stack = player.getHeldItemMainhand();
                    ItemHakureiGohei item = MaidItems.HAKUREI_GOHEI;
                    if (stack.getItem() == item) {
                        int index = item.getGoheiMode(stack).getIndex() + DanmakuType.getLength() + (message.next ? 1 : -1);
                        index %= DanmakuType.getLength();
                        item.setGoheiMode(stack, DanmakuType.getType(index));
                    }
                });
            }
            return null;
        }
    }
}
