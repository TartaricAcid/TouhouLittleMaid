package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.client.sound.CustomSoundLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.audio.AudioStreamBuffer;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

public class MaidSoundInstance extends TickableSound {
    private final String id;
    private final EntityMaid maid;
    private final boolean testSound;

    public MaidSoundInstance(SoundEvent soundEvent, String id, EntityMaid maid) {
        this(soundEvent, id, maid, false);
    }

    public MaidSoundInstance(SoundEvent soundEvent, String id, EntityMaid maid, boolean testSound) {
        super(soundEvent, SoundCategory.NEUTRAL);
        this.id = id;
        this.maid = maid;
        this.testSound = testSound;
        this.x = this.maid.getX();
        this.y = this.maid.getY();
        this.z = this.maid.getZ();
    }

    public boolean canPlaySound() {
        return !this.maid.isSilent();
    }

    public void tick() {
        if (this.maid.removed) {
            this.stop();
        } else {
            this.x = this.maid.getX();
            this.y = this.maid.getY();
            this.z = this.maid.getZ();
        }
    }

    public String getId() {
        return id;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public boolean isTestSound() {
        return testSound;
    }

    @Nullable
    public AudioStreamBuffer getSoundBuffer() {
        SoundCache soundCache = CustomSoundLoader.getSoundCache(id);
        if (soundCache != null) {
            return soundCache.getBuffer(location);
        }
        return null;
    }
}
