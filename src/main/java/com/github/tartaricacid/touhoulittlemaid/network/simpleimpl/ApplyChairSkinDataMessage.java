package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ApplyChairSkinDataMessage implements IMessage {
    private UUID entityUuid;
    private ResourceLocation modelId;
    private float mountedHeight;
    private boolean tameableCanRide;

    public ApplyChairSkinDataMessage() {
    }

    public ApplyChairSkinDataMessage(UUID entityUuid, ResourceLocation modelId, float mountedHeight, boolean tameableCanRide) {
        this.entityUuid = entityUuid;
        this.modelId = modelId;
        this.mountedHeight = mountedHeight;
        this.tameableCanRide = tameableCanRide;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        modelId = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        mountedHeight = buf.readFloat();
        tameableCanRide = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, modelId.toString());
        buf.writeFloat(mountedHeight);
        buf.writeBoolean(tameableCanRide);
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }

    public float getMountedHeight() {
        return mountedHeight;
    }

    public boolean isTameableCanRide() {
        return tameableCanRide;
    }

    public static class Handler implements IMessageHandler<ApplyChairSkinDataMessage, IMessage> {
        @Override
        public IMessage onMessage(ApplyChairSkinDataMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityChair) {
                        EntityChair chair = (EntityChair) entity;
                        chair.setModelId(message.getModelId().toString());
                        chair.setMountedHeight(message.getMountedHeight());
                        chair.setTameableCanRide(message.isTameableCanRide());
                    }
                });
            }
            return null;
        }
    }
}
