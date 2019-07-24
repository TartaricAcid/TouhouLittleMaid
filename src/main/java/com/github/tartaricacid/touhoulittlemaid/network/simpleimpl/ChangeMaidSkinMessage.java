package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ChangeMaidSkinMessage implements IMessage {
    private UUID entityUuid;
    private String modelLocation;
    private String textureLocation;
    private String modelName;


    public ChangeMaidSkinMessage() {
    }

    public ChangeMaidSkinMessage(UUID entityUuid, String modelLocation, String textureLocation, String modelName) {
        this.entityUuid = entityUuid;
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
        this.modelName = modelName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        modelLocation = ByteBufUtils.readUTF8String(buf);
        textureLocation = ByteBufUtils.readUTF8String(buf);
        modelName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, modelLocation);
        ByteBufUtils.writeUTF8String(buf, textureLocation);
        ByteBufUtils.writeUTF8String(buf, modelName);
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public String getModelLocation() {
        return modelLocation;
    }

    public String getTextureLocation() {
        return textureLocation;
    }

    public String getModelName() {
        return modelName;
    }

    public static class Handler implements IMessageHandler<ChangeMaidSkinMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeMaidSkinMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setModelLocation(message.getModelLocation());
                        ((EntityMaid) entity).setTextureLocation(message.getTextureLocation());
                        ((EntityMaid) entity).setModelName(message.getModelName());
                    }
                });
            }
            return null;
        }
    }
}
