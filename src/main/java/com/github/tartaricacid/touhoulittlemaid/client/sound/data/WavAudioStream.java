package com.github.tartaricacid.touhoulittlemaid.client.sound.data;

import net.minecraft.client.sounds.AudioStream;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * WAB 合成回来的 WAV（16 kHz/16 位/单声道为主）。统一转成 16 位小端 PCM 再交给声音引擎，
 * 采样率与声道保持原样——上游的 MP3/Ogg 两条流也是这么处理的。
 */
public class WavAudioStream implements AudioStream {
    private final AudioInputStream stream;
    private final byte[] frame;

    public WavAudioStream(byte[] data) throws UnsupportedAudioFileException, IOException {
        AudioInputStream original = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        AudioFormat source = original.getFormat();
        AudioFormat target = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, source.getSampleRate(), 16,
                source.getChannels(), source.getChannels() * 2, source.getSampleRate(), false);
        this.stream = AudioSystem.getAudioInputStream(target, original);
        this.frame = new byte[stream.getFormat().getFrameSize()];
    }

    @Override
    public AudioFormat getFormat() {
        return stream.getFormat();
    }

    @Override
    public ByteBuffer read(int size) throws IOException {
        ByteBuffer buffer = BufferUtils.createByteBuffer(size);
        int frames = frame.length == 0 ? 0 : size / frame.length;
        for (int i = 0; i < frames; i++) {
            int count = stream.read(frame);
            if (count <= 0) break;
            buffer.put(frame, 0, count);
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
