package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/28 17:27
 **/
public class SyncMaidEffectMessage implements IMessage {
    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte showParticles;

    public SyncMaidEffectMessage() {
    }

    public SyncMaidEffectMessage(int entityIdIn, PotionEffect effect) {
        this.entityId = entityIdIn;
        this.effectId = (byte) (Potion.getIdFromPotion(effect.getPotion()) & 255);
        this.amplifier = (byte) (effect.getAmplifier() & 255);

        if (effect.getDuration() > 32767) {
            this.duration = 32767;
        } else {
            this.duration = effect.getDuration();
        }

        this.showParticles = 0;

        if (effect.getIsAmbient()) {
            this.showParticles = (byte) (this.showParticles | 1);
        }

        if (effect.doesShowParticles()) {
            this.showParticles = (byte) (this.showParticles | 2);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        effectId = buf.readByte();
        amplifier = buf.readByte();
        duration = buf.readInt();
        showParticles = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(effectId);
        buf.writeByte(amplifier);
        buf.writeInt(duration);
        buf.writeByte(showParticles);
    }

    @SideOnly(Side.CLIENT)
    public boolean isMaxDuration() {
        return this.duration == 32767;
    }

    @SideOnly(Side.CLIENT)
    public int getEntityId() {
        return this.entityId;
    }

    @SideOnly(Side.CLIENT)
    public byte getEffectId() {
        return this.effectId;
    }

    @SideOnly(Side.CLIENT)
    public byte getAmplifier() {
        return this.amplifier;
    }

    @SideOnly(Side.CLIENT)
    public int getDuration() {
        return this.duration;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesShowParticles() {
        return (this.showParticles & 2) == 2;
    }

    @SideOnly(Side.CLIENT)
    public boolean getIsAmbient() {
        return (this.showParticles & 1) == 1;
    }

    public static class Handler implements IMessageHandler<SyncMaidEffectMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncMaidEffectMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.getEntityId());
                    if (entity instanceof EntityMaid) {
                        Potion potion = Potion.getPotionById(message.getEffectId() & 0xFF);
                        if (potion != null) {
                            PotionEffect potionEffect = new PotionEffect(potion, message.getDuration(), message.getAmplifier(), message.getIsAmbient(), message.doesShowParticles());
                            potionEffect.setPotionDurationMax(message.isMaxDuration());
                            ((EntityMaid) entity).addPotionEffect(potionEffect);
                        }
                    }
                });
            }
            return null;
        }
    }
}
