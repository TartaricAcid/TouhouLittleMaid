package com.github.tartaricacid.touhoulittlemaid.client.sound.record;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.util.VisibleForDebug;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MicrophoneManager {
    private static final AudioFormat DEFAULT_FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static final int MAX_RECORD_TIME_SECONDS = 20;
    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static final AtomicBoolean IS_RECORDING = new AtomicBoolean();
    private static CompletableFuture<?> TASK = null;

    @Nullable
    public static Mixer.Info getMicrophoneInfo(AudioFormat audioFormat) {
        List<Mixer.Info> allMicrophoneInfo = getAllMicrophoneInfo(audioFormat);
        if (allMicrophoneInfo.isEmpty()) {
            return null;
        }
        String name = AIConfig.STT_MICROPHONE.get();
        if (name.isBlank()) {
            return allMicrophoneInfo.get(0);
        }
        for (Mixer.Info info : allMicrophoneInfo) {
            if (info.getName().equals(name)) {
                return info;
            }
        }
        return allMicrophoneInfo.get(0);
    }

    public static String[] getAllMicrophoneName() {
        Mixer.Info[] allInfos = AudioSystem.getMixerInfo();
        List<String> names = Lists.newArrayList();
        for (Mixer.Info info : allInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, DEFAULT_FORMAT);
            if (mixer.isLineSupported(lineInfo)) {
                names.add(info.getName());
            }
        }
        String[] result = names.toArray(new String[0]);
        if (result.length == 0) {
            return new String[]{Component.translatable("mco.configure.world.slot.empty").getString()};
        }
        return result;
    }

    private static List<Mixer.Info> getAllMicrophoneInfo(AudioFormat format) {
        List<Mixer.Info> output = Lists.newArrayList();
        Mixer.Info[] allInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : allInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, format);
            if (mixer.isLineSupported(lineInfo)) {
                output.add(info);
            }
        }
        return output;
    }

    @Nullable
    private static TargetDataLine getMicrophone(String deviceName, AudioFormat format) throws LineUnavailableException {
        Mixer.Info[] allInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : allInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, format);
            if (mixer.isLineSupported(lineInfo) && info.getName().equals(deviceName)) {
                return (TargetDataLine) mixer.getLine(lineInfo);
            }
        }
        return null;
    }


    public static void startRecord(String deviceName, AudioFormat format, Consumer<byte[]> consumer) {
        // 先判断是否已经有线程在执行了
        if (TASK != null && !TASK.isDone()) {
            IS_RECORDING.set(false);
        }

        TASK = CompletableFuture.supplyAsync(() -> {
            // 开始录音
            doRecord(deviceName, format, consumer);
            return null;
        }, SERVICE).orTimeout(MAX_RECORD_TIME_SECONDS, TimeUnit.SECONDS).exceptionally(throwable -> {
            IS_RECORDING.set(false);
            return null;
        });
    }

    public static void stopRecord() {
        IS_RECORDING.set(false);
    }

    private static void doRecord(String deviceName, AudioFormat format, Consumer<byte[]> consumer) {
        try (TargetDataLine dataLine = getMicrophone(deviceName, format)) {
            if (dataLine == null) {
                TouhouLittleMaid.LOGGER.error("Microphone Device is not found: {}", deviceName);
                return;
            }
            TouhouLittleMaid.LOGGER.debug("Microphone start record...");

            IS_RECORDING.set(true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            dataLine.open(format);
            dataLine.start();

            while (IS_RECORDING.get()) {
                int bytesRead = dataLine.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    stream.write(buffer, 0, bytesRead);
                }
            }

            dataLine.stop();
            dataLine.flush();

            byte[] byteArray = pcmToWav(stream.toByteArray(), format);
            // debugFile(byteArray);
            consumer.accept(byteArray);

            TouhouLittleMaid.LOGGER.debug("Microphone stop record...");
        } catch (LineUnavailableException e) {
            TouhouLittleMaid.LOGGER.error("Microphone is not found: {}", e.getMessage());
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Microphone record error: {}", e.getMessage());
        } finally {
            IS_RECORDING.set(false);
        }
    }

    private static byte[] pcmToWav(byte[] pcmData, AudioFormat format) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int length = pcmData.length / format.getFrameSize();
        try (ByteArrayInputStream input = new ByteArrayInputStream(pcmData);
             AudioInputStream audio = new AudioInputStream(input, format, length)) {
            AudioSystem.write(audio, AudioFileFormat.Type.WAVE, output);
        }
        return output.toByteArray();
    }

    @VisibleForDebug
    private static void debugFile(byte[] byteArray) {
        try {
            FileUtils.writeByteArrayToFile(new File("test.wav"), byteArray);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to write test.wav file: {}", e.getMessage());
        }
    }
}
