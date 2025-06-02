package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.Maps;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SettingReader {
    private static final String SETTING_FOLDER_NAME = "settings";
    private static final Path SETTINGS_FOLDER = FMLPaths.CONFIGDIR.get().resolve(TouhouLittleMaid.MOD_ID).resolve(SETTING_FOLDER_NAME);
    private static final Map<String, CharacterSetting> SETTINGS = Maps.newHashMap();
    private static final String YAML = ".yml";

    public static void clear() {
        SETTINGS.clear();
    }

    public static void reloadSettings() {
        if (!Files.exists(SETTINGS_FOLDER)) {
            try {
                Files.createDirectories(SETTINGS_FOLDER);
            } catch (IOException e) {
                TouhouLittleMaid.LOGGER.error("Failed to create settings folder", e);
            }
        }
        // 配置文件里可以读 8 层
        try {
            readConfigSetting(SETTINGS_FOLDER, 8);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to read settings file", e);
        }
    }

    public static void readCustomPack(Path rootPath, String domain) {
        Path folder = rootPath.resolve("assets").resolve(domain).resolve(SETTING_FOLDER_NAME);
        if (!folder.toFile().isDirectory()) {
            return;
        }
        try {
            // 模型包里只能读一层
            readConfigSetting(folder, 1);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to read settings from {}", folder, e);
        }
    }

    public static void readCustomPack(ZipFile zipFile, String domain) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        String folder = String.format("assets/%s/%s/", domain, SETTING_FOLDER_NAME);
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (!entry.isDirectory() && entryName.startsWith(folder) && entryName.endsWith(YAML)) {
                TouhouLittleMaid.LOGGER.debug("Loading settings from {}", entryName);
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    CharacterSetting setting = new CharacterSetting(inputStream);
                    setting.getModelId().forEach(id -> SETTINGS.put(id, setting));
                } catch (Exception e) {
                    TouhouLittleMaid.LOGGER.error("Failed to read settings from {}", entryName, e);
                }
            }
        }
    }

    private static void readConfigSetting(Path settingFolder, int maxDepth) throws IOException {
        Files.walkFileTree(settingFolder, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<>() {
            @Override
            @NotNull
            public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attributes) throws IOException {
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(YAML)) {
                    try {
                        CharacterSetting setting = new CharacterSetting(file.toFile());
                        setting.getModelId().forEach(id -> SETTINGS.put(id, setting));
                    } catch (Exception e) {
                        TouhouLittleMaid.LOGGER.error("Failed to read settings from {}", file, e);
                    }
                }
                return super.visitFile(file, attributes);
            }
        });
    }

    public static Optional<CharacterSetting> getSetting(@NotNull String name) {
        return Optional.ofNullable(SETTINGS.get(name));
    }

    public static Set<String> getAllSettingKeys() {
        return SETTINGS.keySet();
    }

    public static Path getSettingsFolder() {
        return SETTINGS_FOLDER;
    }
}
