package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class SendNameTagMessage implements IMessage {
    private UUID uuid;
    private String name;
    private boolean alwaysShow;

    public SendNameTagMessage() {
    }

    public SendNameTagMessage(UUID uuid, String name, boolean alwaysShow) {
        this.uuid = uuid;
        this.name = name;
        this.alwaysShow = alwaysShow;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        name = ByteBufUtils.readUTF8String(buf);
        alwaysShow = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeBoolean(alwaysShow);
    }

    public static class Handler implements IMessageHandler<SendNameTagMessage, IMessage> {
        @Override
        public IMessage onMessage(SendNameTagMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid);
                    if (entity instanceof EntityMaid) {
                        setMaidNameTag(message, player, (EntityMaid) entity);
                    }
                });
            }
            return null;
        }

        private void setMaidNameTag(SendNameTagMessage message, EntityPlayerMP player, EntityMaid maid) {
            String name = message.name.substring(0, Math.min(32, message.name.length()));
            if (player.equals(maid.getOwner()) && player.getHeldItemMainhand().getItem() == Items.NAME_TAG) {
                maid.setCustomNameTag(name);
                maid.setAlwaysRenderNameTag(message.alwaysShow);
                maid.enablePersistence();
                player.getHeldItemMainhand().shrink(1);
            }
        }
    }
}
