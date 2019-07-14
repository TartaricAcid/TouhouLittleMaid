package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ChangeMaidSkinMessage implements IMessage {
    private UUID entityUuid;
    private int modelStringLength;
    private int textureStringLength;
    private String modelLocation;
    private String textureLocation;


    public ChangeMaidSkinMessage() {
    }

    public ChangeMaidSkinMessage(UUID entityUuid, int modelStringLength, int textureStringLength, String modelLocation, String textureLocation) {
        this.entityUuid = entityUuid;
        this.modelStringLength = modelStringLength;
        this.textureStringLength = textureStringLength;
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        modelStringLength = buf.readInt();
        textureStringLength = buf.readInt();
        modelLocation = buf.readCharSequence(modelStringLength, StandardCharsets.UTF_8).toString();
        textureLocation = buf.readCharSequence(textureStringLength, StandardCharsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        buf.writeInt(modelStringLength);
        buf.writeInt(textureStringLength);
        buf.writeCharSequence(modelLocation, StandardCharsets.UTF_8);
        buf.writeCharSequence(textureLocation, StandardCharsets.UTF_8);
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

    public static class Handler implements IMessageHandler<ChangeMaidSkinMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeMaidSkinMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setModelLocation(message.getModelLocation());
                        ((EntityMaid) entity).setTextureLocation(message.getTextureLocation());
                    }
                });
            }
            return null;
        }
    }
}
