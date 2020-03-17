package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ClearMaidPosMessage implements IMessage {
    private UUID entityUuid;

    public ClearMaidPosMessage() {
    }

    public ClearMaidPosMessage(UUID entityUuid) {
        this.entityUuid = entityUuid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
    }

    public static class Handler implements IMessageHandler<ClearMaidPosMessage, IMessage> {
        @Override
        public IMessage onMessage(ClearMaidPosMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.entityUuid);
                    if (entity instanceof EntityMaid && player.equals(((EntityMaid) entity).getOwner())) {
                        EntityMaid maid = (EntityMaid) entity;
                        maid.setCompassMode(ItemKappaCompass.Mode.NONE);
                        player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.maid_pos.clear"));
                    }
                });
            }
            return null;
        }
    }
}
