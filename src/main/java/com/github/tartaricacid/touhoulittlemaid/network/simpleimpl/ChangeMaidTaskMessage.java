package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import java.util.UUID;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.internal.LittleMaidAPIImpl;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChangeMaidTaskMessage implements IMessage {
    private UUID entityUuid;
    private IMaidTask task;

    public ChangeMaidTaskMessage() {
    }

    public ChangeMaidTaskMessage(UUID entityUuid, IMaidTask task) {
        this.entityUuid = entityUuid;
        this.task = task;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        ResourceLocation uid = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        task = LittleMaidAPI.findTask(uid).or(LittleMaidAPIImpl.IDLE_TASK);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, task.getUid().toString());
    }

    public IMaidTask getTask() {
        return task;
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public static class Handler implements IMessageHandler<ChangeMaidTaskMessage, IMessage> {
        @Override
        public IMessage onMessage(ChangeMaidTaskMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        ((EntityMaid) entity).setTask(message.task);
                    }
                });
            }
            return null;
        }
    }
}
