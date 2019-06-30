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
    public ChangeGoheiMessage() {
    }

    public ChangeGoheiMessage(boolean i) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
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
                        item.setGoheiMode(stack, (item.getGoheiMode(stack).getIndex() + 1 > DanmakuType.getLength()) ?
                                DanmakuType.PELLET : DanmakuType.getType(item.getGoheiMode(stack).getIndex() + 1));
                    }
                });
            }
            return null;
        }
    }
}
