package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ChangePickupDataMessage implements IMessage {
    private UUID entityUuid;
    private boolean pickup;

    public ChangePickupDataMessage() {
    }

    public ChangePickupDataMessage(UUID entityUuid, boolean pickup) {
        this.entityUuid = entityUuid;
        this.pickup = pickup;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        pickup = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        buf.writeBoolean(pickup);
    }

    public boolean isPickup() {
        return pickup;
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public static class Handler implements IMessageHandler<ChangePickupDataMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangePickupDataMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setPickup(message.isPickup());
                    }
                });
            }
            return null;
        }
    }
}
