package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * @author TartaricAcid
 */
public class SetScarecrowDataMessage implements IMessage {
    private UUID entityUuid;
    private String playerName;
    private String text;

    public SetScarecrowDataMessage() {
    }

    public SetScarecrowDataMessage(UUID entityUuid, String playerName, String text) {
        this.entityUuid = entityUuid;
        this.playerName = playerName;
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        playerName = ByteBufUtils.readUTF8String(buf);
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, playerName);
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<SetScarecrowDataMessage, IMessage> {
        @Override
        public IMessage onMessage(SetScarecrowDataMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.entityUuid);
                    if (entity instanceof EntityScarecrow) {
                        EntityScarecrow scarecrow = (EntityScarecrow) entity;
                        scarecrow.setCustomNameTag(message.playerName);
                        scarecrow.setText(message.text);
                    }
                });
            }
            return null;
        }
    }
}
