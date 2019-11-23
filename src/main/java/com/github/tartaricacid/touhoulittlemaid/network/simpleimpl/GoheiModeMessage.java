package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType;
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

public class GoheiModeMessage implements IMessage {
    private boolean next;

    public GoheiModeMessage() {
    }

    public GoheiModeMessage(boolean next) {
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

    public static class Handler implements IMessageHandler<GoheiModeMessage, IMessage> {
        @Override
        public IMessage onMessage(GoheiModeMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    ItemStack stack = player.getHeldItemMainhand();
                    if (MaidItems.HAKUREI_GOHEI instanceof ItemHakureiGohei) {
                        ItemHakureiGohei item = (ItemHakureiGohei) MaidItems.HAKUREI_GOHEI;
                        if (stack.getItem() == item) {
                            // 一处稍微不太好理解的索引更改
                            // 如果 next 为 true，那么会增加索引，到尾部自动跳转到首部
                            // 如果 next 为 false，那么会减少索引，到首部会自动跳转到尾部
                            int index = item.getGoheiMode(stack).getIndex() + DanmakuType.getLength() + (message.next ? 1 : -1);
                            index %= DanmakuType.getLength();
                            item.setGoheiMode(stack, DanmakuType.getType(index));
                        }
                    }
                });
            }
            return null;
        }
    }
}
