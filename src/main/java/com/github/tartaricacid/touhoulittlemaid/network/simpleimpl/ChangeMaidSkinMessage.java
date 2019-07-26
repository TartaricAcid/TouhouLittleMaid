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
    private int modelFormat;


    public ChangeMaidSkinMessage() {
    }

    public ChangeMaidSkinMessage(UUID entityUuid, String modelLocation, String textureLocation, String modelName, int modelFormat) {
        this.entityUuid = entityUuid;
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
        this.modelName = modelName;
        this.modelFormat = modelFormat;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        modelLocation = ByteBufUtils.readUTF8String(buf);
        textureLocation = ByteBufUtils.readUTF8String(buf);
        modelName = ByteBufUtils.readUTF8String(buf);
        modelFormat = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, modelLocation);
        ByteBufUtils.writeUTF8String(buf, textureLocation);
        ByteBufUtils.writeUTF8String(buf, modelName);
        buf.writeInt(modelFormat);
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

    public int getModelFormat() {
        return modelFormat;
    }

    public static class Handler implements IMessageHandler<ChangeMaidSkinMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeMaidSkinMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        EntityMaid maid = (EntityMaid) entity;
                        maid.setModelLocation(message.getModelLocation());
                        maid.setTextureLocation(message.getTextureLocation());
                        maid.setModelName(message.getModelName());
                        maid.setModelFormat(message.getModelFormat());
                    }
                });
            }
            return null;
        }
    }
}
