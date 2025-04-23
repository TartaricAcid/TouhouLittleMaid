package com.github.tartaricacid.touhoulittlemaid.client.sound;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.SoundData;
import com.github.tartaricacid.touhoulittlemaid.util.OpusDecoderUtil;
import com.mojang.blaze3d.audio.OggAudioStream;
import io.github.jaredmdobson.concentus.OpusException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.gagravarr.ogg.OggFile;
import org.gagravarr.ogg.OggStreamIdentifier;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

public final class OggReader {
    public static void readSoundDataFromFile(File file, List<SoundData> sounds, Marker marker) {
        Path path = file.toPath();
        try {
            Type type = getOggType(path);
            if (type == Type.VORBIS) {
                try (InputStream stream = Files.newInputStream(path); OggAudioStream audioStream = new OggAudioStream(stream)) {
                    ByteBuffer bytebuffer = audioStream.readAll();
                    sounds.add(new SoundData(bytebuffer, audioStream.getFormat()));
                }
            } else if (type == Type.OPUS) {
                byte[] input = FileUtils.readFileToByteArray(file);
                try {
                    Pair<AudioFormat, byte[]> output = OpusDecoderUtil.decode(input);
                    byte[] pcm = output.getRight();
                    ByteBuffer bytebuffer = BufferUtils.createByteBuffer(pcm.length);
                    bytebuffer.put(pcm);
                    bytebuffer.flip();
                    sounds.add(new SoundData(bytebuffer, output.getLeft()));
                } catch (OpusException e) {
                    LOGGER.error("Error decoding opus file: {}", file.getName(), e);
                }
            }
            LOGGER.debug(marker, "sound: {}", file.getName());
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Error reading sound file: {}", path, e);
        }
    }

    public static void readSoundDataFromZip(ZipFile zipFile, ZipEntry zipEntry, String fileName, List<SoundData> sounds, Marker marker) {
        try {
            Type type = getOggType(zipFile, zipEntry);
            if (type == Type.VORBIS) {
                try (InputStream stream = zipFile.getInputStream(zipEntry); OggAudioStream audioStream = new OggAudioStream(stream)) {
                    ByteBuffer bytebuffer = audioStream.readAll();
                    sounds.add(new SoundData(bytebuffer, audioStream.getFormat()));
                }
            } else if (type == Type.OPUS) {
                try (InputStream stream = zipFile.getInputStream(zipEntry)) {
                    byte[] input = IOUtils.toByteArray(stream);
                    Pair<AudioFormat, byte[]> output = OpusDecoderUtil.decode(input);
                    byte[] pcm = output.getRight();
                    ByteBuffer bytebuffer = BufferUtils.createByteBuffer(pcm.length);
                    bytebuffer.put(pcm);
                    bytebuffer.flip();
                    sounds.add(new SoundData(bytebuffer, output.getLeft()));
                } catch (OpusException e) {
                    LOGGER.error("Error decoding opus file: {}", fileName, e);
                }
            }
            LOGGER.debug(marker, "sound: {}", fileName);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Error reading sound file: {}", fileName, e);
        }
    }

    private static Type getOggType(Path filePath) throws IOException {
        try (InputStream stream = Files.newInputStream(filePath)) {
            return getOggType(stream);
        }
    }

    private static Type getOggType(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        try (InputStream stream = zipFile.getInputStream(zipEntry)) {
            return getOggType(stream);
        }
    }

    public static Type getOggType(byte[] data) throws IOException {
        return getOggType(new ByteArrayInputStream(data));
    }

    private static Type getOggType(InputStream stream) throws IOException {
        try (OggFile oggFile = new OggFile(stream)) {
            OggStreamIdentifier.OggStreamType streamType = OggStreamIdentifier.identifyType(oggFile.getPacketReader().getNextPacket());
            if (streamType == OggStreamIdentifier.OGG_VORBIS) {
                return Type.VORBIS;
            } else if (streamType == OggStreamIdentifier.OPUS_AUDIO || streamType == OggStreamIdentifier.OPUS_AUDIO_ALT) {
                return Type.OPUS;
            } else {
                return Type.UNKNOWN;
            }
        }
    }

    public enum Type {
        VORBIS,
        OPUS,
        UNKNOWN
    }
}
