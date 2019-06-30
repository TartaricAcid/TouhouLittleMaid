package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ChangeMaidModeMessage implements IMessage {
    private UUID entityUuid;
    private MaidMode maidMode;

    public ChangeMaidModeMessage() {
    }

    public ChangeMaidModeMessage(UUID entityUuid, MaidMode maidMode) {
        this.entityUuid = entityUuid;
        this.maidMode = maidMode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        maidMode = MaidMode.getMode(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        buf.writeInt(maidMode.getModeIndex());
    }

    public MaidMode getMaidMode() {
        return maidMode;
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public static class Handler implements IMessageHandler<ChangeMaidModeMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeMaidModeMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setMode(message.getMaidMode());
                    }
                });
            }
            return null;
        }
    }
}
