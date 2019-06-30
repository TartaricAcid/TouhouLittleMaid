package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ChangeGuiMessage implements IMessage {
    private UUID uuid;
    private int entityId;
    private int guiId;

    public ChangeGuiMessage() {
    }

    public ChangeGuiMessage(UUID uuid, int entityId, int guiId) {
        this.uuid = uuid;
        this.entityId = entityId;
        this.guiId = guiId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        entityId = buf.readInt();
        guiId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(entityId);
        buf.writeInt(guiId);
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getGuiId() {
        return guiId;
    }

    public static class Handler implements IMessageHandler<ChangeGuiMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeGuiMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity player = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getUUID());
                    if (player instanceof EntityPlayer) {
                        ((EntityPlayer) player).openGui(TouhouLittleMaid.INSTANCE, message.getGuiId(), player.getEntityWorld(), message.getEntityId(), 0, 0);
                    }
                });
            }
            return null;
        }
    }
}
