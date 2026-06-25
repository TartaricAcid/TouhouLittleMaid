package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.sound.OggReader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.JOrbisAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.sounds.SoundSource;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

public class MaidAISoundInstance extends EntityBoundSoundInstance {
    private final byte[] data;

    public MaidAISoundInstance(EntityMaid maid, byte[] data) {
        super(InitSounds.MAID_AI_CHAT.get(), SoundSource.NEUTRAL, 1f, 1f, maid, maid.getId());
        this.data = data;
    }

    @Override
    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary library, Sound sound, boolean looping) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                try {
                    return new Mp3AudioStream(this.data);
                } catch (UnsupportedAudioFileException e) {
                    return this.createFallbackStream();
                }
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error(e);
            }
            return null;
        }, Util.backgroundExecutor());
    }

    private AudioStream createFallbackStream() throws Exception {
        try {
            return new WavAudioStream(this.data);
        } catch (UnsupportedAudioFileException e) {
            OggReader.Type oggType = OggReader.getOggType(this.data);
            if (oggType.equals(OggReader.Type.OPUS)) {
                return new OpusAudioStream(this.data);
            }
            if (oggType.equals(OggReader.Type.VORBIS)) {
                return new JOrbisAudioStream(new ByteArrayInputStream(this.data));
            }
            throw e;
        }
    }
}
