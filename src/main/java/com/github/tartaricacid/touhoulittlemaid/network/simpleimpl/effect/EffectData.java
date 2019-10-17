package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect;

import io.netty.buffer.ByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/10/17 23:28
 **/
public final class EffectData {
    private static final int MAX_DURATION = 32767;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte showParticles;

    public EffectData(ByteBuf buf) {
        effectId = buf.readByte();
        amplifier = buf.readByte();
        duration = buf.readInt();
        showParticles = buf.readByte();
    }

    public EffectData(PotionEffect effect) {
        this.effectId = (byte) (Potion.getIdFromPotion(effect.getPotion()) & 255);
        this.amplifier = (byte) (effect.getAmplifier() & 255);
        if (effect.getDuration() > MAX_DURATION) {
            this.duration = MAX_DURATION;
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

    @SideOnly(Side.CLIENT)
    public boolean isMaxDuration() {
        return this.duration == 32767;
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

    public void write(ByteBuf buf) {
        buf.writeByte(effectId);
        buf.writeByte(amplifier);
        buf.writeInt(duration);
        buf.writeByte(showParticles);
    }
}
