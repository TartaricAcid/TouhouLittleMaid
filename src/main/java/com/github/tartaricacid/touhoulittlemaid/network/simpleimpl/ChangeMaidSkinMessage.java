package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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

public class ChangeMaidSkinMessage implements IMessage {
    private UUID entityUuid;
    private ResourceLocation location;

    public ChangeMaidSkinMessage() {
    }

    public ChangeMaidSkinMessage(UUID entityUuid, ResourceLocation location) {
        this.entityUuid = entityUuid;
        this.location = location;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        location = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, location.toString());
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public static class Handler implements IMessageHandler<ChangeMaidSkinMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeMaidSkinMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        EntityMaid maid = (EntityMaid) entity;
                        maid.setModel(message.getLocation().toString());
                    }
                });
            }
            return null;
        }
    }
}
