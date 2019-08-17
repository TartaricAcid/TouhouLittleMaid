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

/**
 * @author TartaricAcid
 * @date 2019/8/14 10:48
 **/
public class SetMaidSasimonoCRC32 implements IMessage {
    private UUID entityUuid;
    private Long crc32;
    private boolean isShow;

    public SetMaidSasimonoCRC32() {
    }

    public SetMaidSasimonoCRC32(UUID entityUuid, Long crc32, boolean isShow) {
        this.entityUuid = entityUuid;
        this.crc32 = crc32;
        this.isShow = isShow;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        crc32 = buf.readLong();
        isShow = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        buf.writeLong(crc32);
        buf.writeBoolean(isShow);
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public Long getCRC32() {
        return crc32;
    }

    public boolean isShow() {
        return isShow;
    }

    public static class Handler implements IMessageHandler<SetMaidSasimonoCRC32, IMessage> {
        @Override
        public IMessage onMessage(SetMaidSasimonoCRC32 message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setSasimonoCRC32(message.getCRC32());
                        ((EntityMaid) entity).setShowSasimono(message.isShow());
                    }
                });
            }
            return null;
        }
    }
}
