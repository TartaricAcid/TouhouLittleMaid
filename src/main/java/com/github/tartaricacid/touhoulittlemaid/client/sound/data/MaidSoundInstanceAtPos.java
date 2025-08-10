package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.client.sound.CustomSoundLoader;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import javax.annotation.Nullable;

public class MaidSoundInstanceAtPos extends AbstractTickableSoundInstance {
    private final String id;
    private final boolean testSound;

    public MaidSoundInstanceAtPos(SoundEvent soundEvent, String id, double x, double y, double z, float volume, float pitch) {
        this(soundEvent, id, x, y, z, volume, pitch, false);
    }

    public MaidSoundInstanceAtPos(SoundEvent soundEvent, String id, double x, double y, double z, float volume, float pitch, boolean testSound) {
        super(soundEvent, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.id = id;
        this.testSound = testSound;
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public boolean canPlaySound() {
        return true;
    }

    @Override
    public void tick() {
        // 固定坐标播放，无需随实体移动
    }

    public String getId() {
        return id;
    }

    public boolean isTestSound() {
        return testSound;
    }

    @Nullable
    public SoundBuffer getSoundBuffer() {
        SoundCache soundCache = CustomSoundLoader.getSoundCache(id);
        if (soundCache != null) {
            return soundCache.getBuffer(location);
        }
        return null;
    }
}


