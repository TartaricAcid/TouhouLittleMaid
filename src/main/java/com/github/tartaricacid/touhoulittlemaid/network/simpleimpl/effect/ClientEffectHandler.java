package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author TartaricAcid
 * @date 2019/10/18 0:16
 **/
public class ClientEffectHandler implements IMessageHandler<EffectReply, IMessage> {
    @Override
    public IMessage onMessage(EffectReply message, MessageContext ctx) {
        if (Minecraft.getMinecraft().player == null || !Minecraft.getMinecraft().player.isEntityAlive()) {
            return null;
        }
        if (ctx.side == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.getEntityId());
                if (entity instanceof EntityMaid) {
                    EntityMaid maid = (EntityMaid) entity;
                    maid.clearActivePotions();
                    for (EffectData data : message.getEffectList()) {
                        setMaidEffect(maid, data);
                    }
                }
            });
        }
        return null;
    }

    private void setMaidEffect(EntityMaid maid, EffectData data) {
        Potion potion = Potion.getPotionById(data.getEffectId() & 0xFF);
        if (potion != null) {
            PotionEffect potionEffect = new PotionEffect(potion, data.getDuration(), data.getAmplifier(), data.getIsAmbient(), data.doesShowParticles());
            potionEffect.setPotionDurationMax(data.isMaxDuration());
            maid.addPotionEffect(potionEffect);
        }
    }
}
