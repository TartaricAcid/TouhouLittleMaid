package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class MaidShowHelmetMessage implements IMessage {
    private UUID uuid;
    private boolean show;

    public MaidShowHelmetMessage() {
    }

    public MaidShowHelmetMessage(UUID uuid, boolean show) {
        this.uuid = uuid;
        this.show = show;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        show = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeBoolean(show);
    }

    public static class Handler implements IMessageHandler<MaidShowHelmetMessage, IMessage> {
        @Override
        public IMessage onMessage(MaidShowHelmetMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid);
                    if (entity instanceof EntityMaid && player.equals(((EntityMaid) entity).getOwner())) {
                        ((EntityMaid) entity).setShowHelmet(message.show);
                    }
                });
            }
            return null;
        }
    }
}
