package com.github.tartaricacid.touhoulittlemaid.entity.info;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

public class CommonDefaultPack {
    private static final String CUSTOM_PACK_DIR_NAME = "tlm_custom_pack";
    private static final String DEFAULT_PACK_NAME = "touhou_little_maid-1.0.0";
    private static final String LEGACY_PACK_NAME = "touhou_little_maid-1.0.0.zip";
    private static final Path PACK_FOLDER = FMLPaths.GAMEDIR.get().resolve(CUSTOM_PACK_DIR_NAME);
    private static final Marker MARKER = MarkerManager.getMarker("CommonDefaultPack");

    public static void initCommonDefaultPack() {
        TouhouLittleMaid.LOGGER.info("common default pack init start...");
        StopWatch watch = StopWatch.createStarted();
        {
            File packFolder = PACK_FOLDER.resolve(DEFAULT_PACK_NAME).toFile();
            createCustomPackFolder(packFolder);
            archiveLegacyDefaultPack();
            unpackDefaultPack(packFolder);
        }
        watch.stop();
        double time = watch.getTime(TimeUnit.MICROSECONDS) / 1000.0;
        TouhouLittleMaid.LOGGER.info("common default pack init finished, cost time: {} ms", time);
    }

    private static void createCustomPackFolder(File packFolder) {
        if (!packFolder.isDirectory()) {
            try {
                Files.createDirectories(packFolder.toPath());
            } catch (IOException e) {
                LOGGER.error(MARKER, "Failed to create folder {}", packFolder.getAbsolutePath(), e);
            }
        }
    }

    private static void archiveLegacyDefaultPack() {
        File legacyFile = PACK_FOLDER.resolve(LEGACY_PACK_NAME).toFile();
        if (legacyFile.isFile()) {
            try {
                String disabledFileName = LEGACY_PACK_NAME + ".disabled";
                FileUtils.moveFile(legacyFile, legacyFile.toPath().resolveSibling(disabledFileName).toFile());
            } catch (IOException e) {
                LOGGER.error(MARKER, "Failed to rename legacy default pack", e);
            }
        }
    }

    private static void unpackDefaultPack(File packFolder) {
        // 不管存不存在，强行覆盖
        String jarDefaultPackPath = "/assets/%s/%s/%s".formatted(TouhouLittleMaid.MOD_ID, CUSTOM_PACK_DIR_NAME, DEFAULT_PACK_NAME);
        try {
            GetJarResources.copyFolder(jarDefaultPackPath, packFolder.toPath());
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(MARKER, "Failed to unpack default pack: {}", jarDefaultPackPath, e);
        }
    }
}
