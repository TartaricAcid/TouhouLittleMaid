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

public class ChangeHomeDataMessage implements IMessage {
    private UUID entityUuid;
    private boolean home;

    public ChangeHomeDataMessage() {
    }

    public ChangeHomeDataMessage(UUID entityUuid, boolean home) {
        this.entityUuid = entityUuid;
        this.home = home;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        home = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        buf.writeBoolean(home);
    }

    public boolean isHome() {
        return home;
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public static class Handler implements IMessageHandler<ChangeHomeDataMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeHomeDataMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setHome(message.isHome());
                    }
                });
            }
            return null;
        }
    }
}
