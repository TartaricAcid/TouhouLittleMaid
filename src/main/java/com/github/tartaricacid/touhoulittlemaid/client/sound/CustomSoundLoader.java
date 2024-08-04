package com.github.tartaricacid.touhoulittlemaid.client.sound;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.SoundCache;
import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.sounds.JOrbisAudioStream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

public class CustomSoundLoader {
    public static final Map<String, SoundCache> CACHE = Maps.newHashMap();
    private static final Pattern FILENAME_REG = Pattern.compile("^\\d*\\.ogg$");
    private static final Marker MARKER = MarkerManager.getMarker("CustomSoundLoader");
    private static final String JSON_FILE_NAME = "maid_sound.json";

    public static void clear() {
        CACHE.clear();
    }

    public static SoundCache getSoundCache(String id) {
        return CACHE.get(id);
    }

    public static void loadSoundPack(Path rootPath, String id) {
        File file = rootPath.resolve("assets").resolve(id).resolve(JSON_FILE_NAME).toFile();
        if (!file.isFile()) {
            return;
        }
        LOGGER.debug(MARKER, "Loading {} sound pack: ", id);
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            SoundPackInfo info = CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), new TypeToken<SoundPackInfo>() {
            }.getType());
            info.decorate();
            // 加载图标贴图
            if (info.getIcon() != null) {
                CustomPackLoader.registerFilePackTexture(rootPath, info.getIcon());
            }
            Path soundsFolder = rootPath.resolve("assets").resolve(id).resolve("sounds").resolve("maid");
            SoundCache soundCache = new SoundCache(info, loadSoundEvent(soundsFolder));
            CACHE.put(id, soundCache);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse sound pack in id {}", id);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Loaded {} sound pack.", id);
    }

    private static Map<ResourceLocation, List<SoundBuffer>> loadSoundEvent(Path rootPath) {
        Map<ResourceLocation, List<SoundBuffer>> buffers = Maps.newLinkedHashMap();

        buffers.put(InitSounds.MAID_IDLE.get().getLocation(), loadSounds(rootPath.resolve("mode"), "idle"));
        buffers.put(InitSounds.MAID_ATTACK.get().getLocation(), loadSounds(rootPath.resolve("mode"), "attack"));
        buffers.put(InitSounds.MAID_RANGE_ATTACK.get().getLocation(), loadSounds(rootPath.resolve("mode"), "range_attack"));
        buffers.put(InitSounds.MAID_DANMAKU_ATTACK.get().getLocation(), loadSounds(rootPath.resolve("mode"), "danmaku_attack"));
        buffers.put(InitSounds.MAID_FARM.get().getLocation(), loadSounds(rootPath.resolve("mode"), "farm"));
        buffers.put(InitSounds.MAID_FEED.get().getLocation(), loadSounds(rootPath.resolve("mode"), "feed"));
        buffers.put(InitSounds.MAID_SHEARS.get().getLocation(), loadSounds(rootPath.resolve("mode"), "shears"));
        buffers.put(InitSounds.MAID_MILK.get().getLocation(), loadSounds(rootPath.resolve("mode"), "milk"));
        buffers.put(InitSounds.MAID_TORCH.get().getLocation(), loadSounds(rootPath.resolve("mode"), "torch"));
        buffers.put(InitSounds.MAID_FEED_ANIMAL.get().getLocation(), loadSounds(rootPath.resolve("mode"), "feed_animal"));
        buffers.put(InitSounds.MAID_EXTINGUISHING.get().getLocation(), loadSounds(rootPath.resolve("mode"), "extinguishing"));
        buffers.put(InitSounds.MAID_REMOVE_SNOW.get().getLocation(), loadSounds(rootPath.resolve("mode"), "snow"));
        buffers.put(InitSounds.MAID_BREAK.get().getLocation(), loadSounds(rootPath.resolve("mode"), "break"));
        buffers.put(InitSounds.MAID_FURNACE.get().getLocation(), loadSounds(rootPath.resolve("mode"), "furnace"));
        buffers.put(InitSounds.MAID_BREWING.get().getLocation(), loadSounds(rootPath.resolve("mode"), "brewing"));

        buffers.put(InitSounds.MAID_FIND_TARGET.get().getLocation(), loadSounds(rootPath.resolve("ai"), "find_target"));
        buffers.put(InitSounds.MAID_HURT.get().getLocation(), loadSounds(rootPath.resolve("ai"), "hurt"));
        buffers.put(InitSounds.MAID_HURT_FIRE.get().getLocation(), loadSounds(rootPath.resolve("ai"), "hurt_fire"));
        buffers.put(InitSounds.MAID_PLAYER.get().getLocation(), loadSounds(rootPath.resolve("ai"), "hurt_player"));
        buffers.put(InitSounds.MAID_TAMED.get().getLocation(), loadSounds(rootPath.resolve("ai"), "tamed"));
        buffers.put(InitSounds.MAID_ITEM_GET.get().getLocation(), loadSounds(rootPath.resolve("ai"), "item_get"));
        buffers.put(InitSounds.MAID_DEATH.get().getLocation(), loadSounds(rootPath.resolve("ai"), "death"));

        buffers.put(InitSounds.MAID_COLD.get().getLocation(), loadSounds(rootPath.resolve("environment"), "cold"));
        buffers.put(InitSounds.MAID_HOT.get().getLocation(), loadSounds(rootPath.resolve("environment"), "hot"));
        buffers.put(InitSounds.MAID_RAIN.get().getLocation(), loadSounds(rootPath.resolve("environment"), "rain"));
        buffers.put(InitSounds.MAID_SNOW.get().getLocation(), loadSounds(rootPath.resolve("environment"), "snow"));
        buffers.put(InitSounds.MAID_MORNING.get().getLocation(), loadSounds(rootPath.resolve("environment"), "morning"));
        buffers.put(InitSounds.MAID_NIGHT.get().getLocation(), loadSounds(rootPath.resolve("environment"), "night"));

        buffers.put(InitSounds.MAID_CREDIT.get().getLocation(), loadSounds(rootPath.resolve("other"), "credit"));

        reuseSounds(buffers, InitSounds.MAID_ATTACK.get(), InitSounds.MAID_RANGE_ATTACK.get());
        reuseSounds(buffers, InitSounds.MAID_ATTACK.get(), InitSounds.MAID_DANMAKU_ATTACK.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FARM.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FEED.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_SHEARS.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_MILK.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_TORCH.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FEED_ANIMAL.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_EXTINGUISHING.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_REMOVE_SNOW.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_BREAK.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FURNACE.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_BREWING.get());

        reuseSounds(buffers, InitSounds.MAID_HURT.get(), InitSounds.MAID_HURT_FIRE.get());

        return buffers;
    }

    private static List<SoundBuffer> loadSounds(Path rootPath, String fileName) {
        List<SoundBuffer> sounds = Lists.newArrayList();
        File[] files = rootPath.toFile().listFiles((dir, name) -> checkFileName(fileName, name));
        if (files == null) {
            return sounds;
        }
        for (File file : files) {
            if (file.isFile()) {
                try (InputStream stream = Files.newInputStream(file.toPath()); JOrbisAudioStream audioStream = new JOrbisAudioStream(stream)) {
                    ByteBuffer bytebuffer = audioStream.readAll();
                    sounds.add(new SoundBuffer(bytebuffer, audioStream.getFormat()));
                    LOGGER.debug(MARKER, "sound: {}", file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sounds;
    }

    private static void reuseSounds(Map<ResourceLocation, List<SoundBuffer>> buffers, SoundEvent from, SoundEvent to) {
        List<SoundBuffer> fromBuffers = buffers.get(from.getLocation());
        buffers.get(to.getLocation()).addAll(fromBuffers);
    }

    private static boolean checkFileName(String patterString, String rawString) {
        if (rawString.startsWith(patterString)) {
            String substring = rawString.substring(patterString.length());
            Matcher matcher = FILENAME_REG.matcher(substring);
            return matcher.matches();
        }
        return false;
    }

    public static void loadSoundPack(ZipFile zipFile, String id) {
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", id, JSON_FILE_NAME));
        if (entry == null) {
            return;
        }
        LOGGER.debug(MARKER, "Loading {} sound pack: ", id);
        try (InputStream stream = zipFile.getInputStream(entry)) {
            SoundPackInfo info = CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), new TypeToken<SoundPackInfo>() {
            }.getType());
            info.decorate();
            // 加载图标贴图
            if (info.getIcon() != null) {
                CustomPackLoader.registerZipPackTexture(zipFile.getName(), info.getIcon());
            }
            SoundCache soundCache = new SoundCache(info, loadSoundEvent(zipFile, id));
            CACHE.put(id, soundCache);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse sound pack in id {}", id);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Loaded {} sound pack.", id);
    }

    @NotNull
    private static Map<ResourceLocation, List<SoundBuffer>> loadSoundEvent(ZipFile zipFile, String id) {
        Map<ResourceLocation, List<SoundBuffer>> buffers = Maps.newLinkedHashMap();
        Pattern pattern = Pattern.compile(String.format("assets/%s/sounds/maid/(.*?)/(.*?\\.ogg)", id));
        zipFile.stream().forEach(zipEntry -> {
            if (!zipEntry.isDirectory()) {
                String path = zipEntry.getName();
                Matcher matcher = pattern.matcher(path);
                if (matcher.find()) {
                    String subDir = matcher.group(1);
                    String fileName = matcher.group(2);
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_IDLE.get(), "mode", "idle");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_ATTACK.get(), "mode", "attack");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_RANGE_ATTACK.get(), "mode", "range_attack");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_DANMAKU_ATTACK.get(), "mode", "danmaku_attack");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_FARM.get(), "mode", "farm");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_FEED.get(), "mode", "feed");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_SHEARS.get(), "mode", "shears");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_MILK.get(), "mode", "milk");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_TORCH.get(), "mode", "torch");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_FEED_ANIMAL.get(), "mode", "feed_animal");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_EXTINGUISHING.get(), "mode", "extinguishing");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_REMOVE_SNOW.get(), "mode", "snow");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_BREAK.get(), "mode", "break");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_FURNACE.get(), "mode", "furnace");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_BREWING.get(), "mode", "brewing");

                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_FIND_TARGET.get(), "ai", "find_target");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_HURT.get(), "ai", "hurt");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_HURT_FIRE.get(), "ai", "hurt_fire");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_PLAYER.get(), "ai", "hurt_player");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_TAMED.get(), "ai", "tamed");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_ITEM_GET.get(), "ai", "item_get");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_DEATH.get(), "ai", "death");

                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_COLD.get(), "environment", "cold");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_HOT.get(), "environment", "hot");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_RAIN.get(), "environment", "rain");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_SNOW.get(), "environment", "snow");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_MORNING.get(), "environment", "morning");
                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_NIGHT.get(), "environment", "night");

                    loadSounds(zipFile, buffers, zipEntry, subDir, fileName, InitSounds.MAID_CREDIT.get(), "other", "credit");
                }
            }
        });

        reuseSounds(buffers, InitSounds.MAID_ATTACK.get(), InitSounds.MAID_RANGE_ATTACK.get());
        reuseSounds(buffers, InitSounds.MAID_ATTACK.get(), InitSounds.MAID_DANMAKU_ATTACK.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FARM.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FEED.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_SHEARS.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_MILK.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_TORCH.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FEED_ANIMAL.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_EXTINGUISHING.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_REMOVE_SNOW.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_BREAK.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_FURNACE.get());
        reuseSounds(buffers, InitSounds.MAID_IDLE.get(), InitSounds.MAID_BREWING.get());

        reuseSounds(buffers, InitSounds.MAID_HURT.get(), InitSounds.MAID_HURT_FIRE.get());

        return buffers;
    }

    private static void loadSounds(ZipFile zipFile, Map<ResourceLocation, List<SoundBuffer>> buffers, ZipEntry zipEntry, String subDir, String fileName, SoundEvent soundEvent, String checkSubDir, String checkFileName) {
        List<SoundBuffer> sounds = buffers.computeIfAbsent(soundEvent.getLocation(), res -> Lists.newArrayList());
        if (checkSubDir.equals(subDir) && checkFileName(checkFileName, fileName)) {
            try (InputStream zipEntryStream = zipFile.getInputStream(zipEntry); JOrbisAudioStream audioStream = new JOrbisAudioStream(zipEntryStream)) {
                ByteBuffer bytebuffer = audioStream.readAll();
                sounds.add(new SoundBuffer(bytebuffer, audioStream.getFormat()));
                LOGGER.debug(MARKER, "sound: {}", fileName);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
