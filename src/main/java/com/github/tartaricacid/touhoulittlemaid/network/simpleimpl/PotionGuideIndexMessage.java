package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPotionGuide;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PotionGuideIndexMessage implements IMessage {
    private int index;

    public PotionGuideIndexMessage() {
    }

    public PotionGuideIndexMessage(int index) {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(index);
    }

    public static class Handler implements IMessageHandler<PotionGuideIndexMessage, IMessage> {
        @Override
        public IMessage onMessage(PotionGuideIndexMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    if (player.getHeldItemMainhand().getItem() == MaidItems.POTION_GUIDE) {
                        ItemPotionGuide.setGuideIndex(player.getHeldItemMainhand(), message.index);
                    }
                });
            }
            return null;
        }
    }
}
