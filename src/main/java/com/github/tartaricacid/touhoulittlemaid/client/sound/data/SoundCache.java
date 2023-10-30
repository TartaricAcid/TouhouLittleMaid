package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import net.minecraft.client.audio.AudioStreamBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoundCache {
    private static final Random RANDOM = new Random();
    private final SoundPackInfo info;
    private final Map<ResourceLocation, List<AudioStreamBuffer>> buffers;

    public SoundCache(SoundPackInfo info, Map<ResourceLocation, List<AudioStreamBuffer>> buffers) {
        this.info = info;
        this.buffers = buffers;
    }

    public SoundPackInfo getInfo() {
        return info;
    }

    public AudioStreamBuffer getBuffer(ResourceLocation soundEvent) {
        List<AudioStreamBuffer> soundBuffers = buffers.get(soundEvent);
        if (soundBuffers != null && !soundBuffers.isEmpty()) {
            return soundBuffers.get(RANDOM.nextInt(soundBuffers.size()));
        }
        return null;
    }

    public Map<ResourceLocation, List<AudioStreamBuffer>> getBuffers() {
        return buffers;
    }
}
