package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Map;

public record SoundCache(SoundPackInfo info, Map<ResourceLocation, List<SoundData>> buffers) {
    private static final RandomSource RANDOM = SoundInstance.createUnseededRandom();

    public SoundBuffer getBuffer(ResourceLocation soundEvent) {
        List<SoundData> soundBuffers = buffers.get(soundEvent);
        if (soundBuffers != null && !soundBuffers.isEmpty()) {
            SoundData soundData = soundBuffers.get(RANDOM.nextInt(soundBuffers.size()));
            return new SoundBuffer(soundData.byteBuffer(), soundData.audioFormat());
        }
        return null;
    }
}
