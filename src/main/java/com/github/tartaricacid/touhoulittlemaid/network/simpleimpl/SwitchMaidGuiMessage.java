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

public class SwitchMaidGuiMessage implements IMessage {
    private UUID playerUuid;
    private int entityId;
    private int guiId;
    private int formTaskIndex;
    private int startRow;

    public SwitchMaidGuiMessage() {
    }

    public SwitchMaidGuiMessage(UUID playerUuid, int entityId, int guiId, int formTaskIndex) {
        this.playerUuid = playerUuid;
        this.entityId = entityId;
        this.guiId = guiId;
        this.formTaskIndex = formTaskIndex;
    }

    public SwitchMaidGuiMessage(UUID playerUuid, int entityId, int guiId, int formTaskIndex, int startRow) {
        this(playerUuid, entityId, guiId, formTaskIndex);
        this.startRow = startRow;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUuid = new UUID(buf.readLong(), buf.readLong());
        entityId = buf.readInt();
        guiId = buf.readInt();
        formTaskIndex = buf.readInt();
        startRow = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(playerUuid.getMostSignificantBits());
        buf.writeLong(playerUuid.getLeastSignificantBits());
        buf.writeInt(entityId);
        buf.writeInt(guiId);
        buf.writeInt(formTaskIndex);
        buf.writeInt(startRow);
    }

    public UUID getUUID() {
        return playerUuid;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getGuiId() {
        return guiId;
    }

    public int getStartRow() {
        return startRow;
    }

    public static class Handler implements IMessageHandler<SwitchMaidGuiMessage, IMessage> {
        @Override
        public IMessage onMessage(SwitchMaidGuiMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity player = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getUUID());
                    if (player instanceof EntityPlayer) {
                        ((EntityPlayer) player).openGui(TouhouLittleMaid.INSTANCE, message.getGuiId(), player.getEntityWorld(), message.getEntityId(), message.formTaskIndex, message.getStartRow());
                    }
                });
            }
            return null;
        }
    }
}
