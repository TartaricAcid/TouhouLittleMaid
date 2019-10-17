package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

/**
 * @author TartaricAcid
 * @date 2019/10/17 23:27
 **/
public class EffectRequest implements IMessage {
    private UUID entityUuid;

    public EffectRequest() {
    }

    public EffectRequest(UUID entityUuid) {
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

    public UUID getEntityUuid() {
        return entityUuid;
    }
}
