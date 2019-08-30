package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/30 23:37
 **/
public class SyncPowerPointEntityData implements IMessage {
    private int entityId;
    private int xpValue;

    public SyncPowerPointEntityData() {
    }

    public SyncPowerPointEntityData(EntityPowerPoint powerPoint) {
        this.entityId = powerPoint.getEntityId();
        this.xpValue = powerPoint.xpValue;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.xpValue = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.xpValue);
    }

    public static class Handler implements IMessageHandler<SyncPowerPointEntityData, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncPowerPointEntityData message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
                    if (entity instanceof EntityPowerPoint) {
                        ((EntityPowerPoint) entity).xpValue = message.xpValue;
                    }
                });
            }
            return null;
        }
    }
}
