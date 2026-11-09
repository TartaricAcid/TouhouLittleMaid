package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.sound.OggReader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.mojang.blaze3d.audio.OggAudioStream;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.AudioStream;
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
                // WAB 合成的语音统一是 WAV：先按文件头认出来，别让 MP3 阅读器先抛一次异常再回头猜。
                if (isWav(this.data)) {
                    return new WavAudioStream(this.data);
                }
                try {
                    return new Mp3AudioStream(this.data);
                } catch (UnsupportedAudioFileException e) {
                    OggReader.Type oggType = OggReader.getOggType(this.data);
                    if (oggType.equals(OggReader.Type.OPUS)) {
                        return new OpusAudioStream(this.data);
                    }
                    if (oggType.equals(OggReader.Type.VORBIS)) {
                        return new OggAudioStream(new ByteArrayInputStream(this.data));
                    }
                }
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error(e);
            }
            return null;
        }, Util.backgroundExecutor());
    }

    private static boolean isWav(byte[] data) {
        return data != null && data.length > 12
               && data[0] == 'R' && data[1] == 'I' && data[2] == 'F' && data[3] == 'F'
               && data[8] == 'W' && data[9] == 'A' && data[10] == 'V' && data[11] == 'E';
    }
}