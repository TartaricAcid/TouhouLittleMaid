package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateMaidSleepYawMessage implements IMessage {
    private int entityId;
    private float yaw;

    public UpdateMaidSleepYawMessage() {
    }

    public UpdateMaidSleepYawMessage(int entityId, float yaw) {
        this.entityId = entityId;
        this.yaw = yaw;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        yaw = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(yaw);
    }

    public static class Handler implements IMessageHandler<UpdateMaidSleepYawMessage, IMessage> {
        @Override
        public IMessage onMessage(UpdateMaidSleepYawMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
                    if (entity instanceof EntityMaid) {
                        entity.rotationYaw = message.yaw;
                        ((EntityMaid) entity).rotationYawHead = message.yaw;
                    }
                });
            }
            return null;
        }
    }
}
