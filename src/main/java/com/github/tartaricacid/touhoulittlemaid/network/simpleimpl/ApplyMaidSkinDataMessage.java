package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ApplyMaidSkinDataMessage implements IMessage {
    private UUID entityUuid;
    private ResourceLocation modelId;

    public ApplyMaidSkinDataMessage() {
    }

    public ApplyMaidSkinDataMessage(UUID entityUuid, ResourceLocation modelId) {
        this.entityUuid = entityUuid;
        this.modelId = modelId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityUuid = new UUID(buf.readLong(), buf.readLong());
        modelId = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(entityUuid.getMostSignificantBits());
        buf.writeLong(entityUuid.getLeastSignificantBits());
        ByteBufUtils.writeUTF8String(buf, modelId.toString());
    }

    private UUID getEntityUuid() {
        return entityUuid;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }

    public static class Handler implements IMessageHandler<ApplyMaidSkinDataMessage, IMessage> {
        @Override
        public IMessage onMessage(ApplyMaidSkinDataMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
                    if (entity instanceof EntityMaid) {
                        applyModelId((EntityMaid) entity, message.getModelId().toString());
                    }
                });
            }
            return null;
        }
    }

    private static void applyModelId(EntityMaid maid, String modelId) {
        EntityLivingBase owner = maid.getOwner();
        if (owner instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) owner;
            if (player.isCreative() || !GeneralConfig.MAID_CONFIG.maidCannotChangeModel) {
                maid.setModelId(modelId);
            } else {
                sendMessageToOwner(player);
            }
        }
    }

    private static void sendMessageToOwner(EntityPlayer entityPlayer) {
        if (entityPlayer.isEntityAlive()) {
            entityPlayer.sendMessage(new TextComponentTranslation("message.touhou_little_maid.change_model.disabled"));
        }
    }
}
