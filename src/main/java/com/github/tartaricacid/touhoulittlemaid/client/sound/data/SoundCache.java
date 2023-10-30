package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Map;

public class SoundCache {
    private static final RandomSource RANDOM = SoundInstance.createUnseededRandom();
    private final SoundPackInfo info;
    private final Map<ResourceLocation, List<SoundBuffer>> buffers;

    public SoundCache(SoundPackInfo info, Map<ResourceLocation, List<SoundBuffer>> buffers) {
        this.info = info;
        this.buffers = buffers;
    }

    public SoundPackInfo getInfo() {
        return info;
    }

    public SoundBuffer getBuffer(ResourceLocation soundEvent) {
        List<SoundBuffer> soundBuffers = buffers.get(soundEvent);
        if (soundBuffers != null && !soundBuffers.isEmpty()) {
            return soundBuffers.get(RANDOM.nextInt(soundBuffers.size()));
        }
        return null;
    }

    public Map<ResourceLocation, List<SoundBuffer>> getBuffers() {
        return buffers;
    }
}
