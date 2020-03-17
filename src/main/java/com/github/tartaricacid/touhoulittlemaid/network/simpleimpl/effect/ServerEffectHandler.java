package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author TartaricAcid
 * @date 2019/10/17 23:49
 **/
public class ServerEffectHandler implements IMessageHandler<EffectRequest, EffectReply> {
    @Override
    public EffectReply onMessage(EffectRequest message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            if (ctx.getServerHandler().player == null || !ctx.getServerHandler().player.isEntityAlive()) {
                return null;
            }
            Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.getEntityUuid());
            if (entity instanceof EntityMaid && entity.isEntityAlive()) {
                EntityMaid maid = (EntityMaid) entity;
                return new EffectReply(maid.getEntityId(), maid.getActivePotionEffects());
            }
        }
        return null;
    }
}
