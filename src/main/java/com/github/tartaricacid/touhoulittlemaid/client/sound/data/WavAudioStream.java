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

public class WavAudioStream implements AudioStream {
    private static final int BUFFER_SIZE = 4096;

    private final AudioInputStream stream;
    private final byte[] frame;

    public WavAudioStream(byte[] data) throws UnsupportedAudioFileException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        AudioInputStream originalInputStream = AudioSystem.getAudioInputStream(inputStream);
        AudioFormat originalFormat = originalInputStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, originalFormat.getSampleRate(), 16,
                originalFormat.getChannels(), originalFormat.getChannels() * 2, originalFormat.getSampleRate(), false);
        if (AudioSystem.isConversionSupported(targetFormat, originalFormat)) {
            this.stream = AudioSystem.getAudioInputStream(targetFormat, originalInputStream);
        } else {
            throw new UnsupportedAudioFileException("WAV PCM_SIGNED 16bit conversion is not supported for format: " + originalFormat);
        }
        this.frame = new byte[Math.max(BUFFER_SIZE, this.stream.getFormat().getFrameSize())];
    }

    @Override
    public AudioFormat getFormat() {
        return this.stream.getFormat();
    }

    @Override
    public ByteBuffer read(int size) throws IOException {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(size);
        int count;
        while (byteBuffer.hasRemaining()
                && (count = this.stream.read(this.frame, 0, Math.min(this.frame.length, byteBuffer.remaining()))) != -1) {
            byteBuffer.put(this.frame, 0, count);
        }
        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }
}
