package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import noppes.npcs.entity.EntityCustomNpc;

import java.util.UUID;

/**
 * @author TartaricAcid
 * @date 2019/12/20 18:31
 **/
public class SendNpcMaidModelMessage implements IMessage {
    private UUID entityUuid;
    private String modelId;
    private String modelRes;

    public SendNpcMaidModelMessage() {
    }

    public SendNpcMaidModelMessage(UUID entityUuid, String modelId, String modelRes) {
        this.entityUuid = entityUuid;
        this.modelId = modelId;
        this.modelRes = modelRes;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        modelId = ByteBufUtils.readUTF8String(buf);
        modelRes = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, modelId);
        ByteBufUtils.writeUTF8String(buf, modelRes);
    }

    private UUID getEntityUuid() {
        return entityUuid;
    }

    public String getModelId() {
        return modelId;
    }

    public String getModelRes() {
        return modelRes;
    }

    public static class Handler implements IMessageHandler<SendNpcMaidModelMessage, IMessage> {
        @Override
        public IMessage onMessage(SendNpcMaidModelMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityCustomNpc) {
                        applyModel((EntityCustomNpc) entity, message.getModelId(), message.getModelRes());
                    }
                });
            }
            return null;
        }

        private static void applyModel(EntityCustomNpc npc, String modelId, String modelRes) {
            Entity entity = npc.modelData.getEntity(npc);
            if (entity instanceof EntityMaid) {
                npc.modelData.extra.setString("ModelId", modelId);
                npc.display.setSkinTexture(modelRes);
            }
        }
    }
}
