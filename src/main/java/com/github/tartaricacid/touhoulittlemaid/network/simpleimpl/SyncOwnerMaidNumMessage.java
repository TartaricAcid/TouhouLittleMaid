package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/28 17:27
 **/
public class SyncOwnerMaidNumMessage implements IMessage {
    private int num;

    public SyncOwnerMaidNumMessage() {
    }

    public SyncOwnerMaidNumMessage(int num) {
        this.num = num;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.num = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(num);
    }

    public int getNum() {
        return num;
    }

    public static class Handler implements IMessageHandler<SyncOwnerMaidNumMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncOwnerMaidNumMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    if (player == null) {
                        return;
                    }
                    MaidNumHandler num = player.getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
                    if (num != null) {
                        num.set(message.getNum());
                    }
                });
            }
            return null;
        }
    }
}
