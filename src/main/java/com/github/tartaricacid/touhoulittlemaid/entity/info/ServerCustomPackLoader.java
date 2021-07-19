package com.github.tartaricacid.touhoulittlemaid.entity.info;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.info.models.ServerChairModels;
import com.github.tartaricacid.touhoulittlemaid.entity.info.models.ServerMaidModels;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

public final class ServerCustomPackLoader {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();
    public static final ServerMaidModels SERVER_MAID_MODELS = ServerMaidModels.getInstance();
    public static final ServerChairModels SERVER_CHAIR_MODELS = ServerChairModels.getInstance();
    private static final Map<Long, Path> CRC32_FILE_MAP = Maps.newHashMap();
    private static final String CUSTOM_PACK_DIR_NAME = "tlm_custom_pack";
    private static final String DEFAULT_PACK_NAME = "touhou_little_maid-1.0.0.zip";
    private static final Path PACK_FOLDER = Paths.get(CUSTOM_PACK_DIR_NAME);
    private static final Marker MARKER = MarkerManager.getMarker("CustomPackLoader");
    private static final Pattern DOMAIN = Pattern.compile("^assets/([\\w.]+)/$");

    public static void reloadPacks() {
        SERVER_MAID_MODELS.clearAll();
        SERVER_CHAIR_MODELS.clearAll();
        CRC32_FILE_MAP.clear();
        initPacks();
    }

    private static void initPacks() {
        File packFolder = PACK_FOLDER.toFile();
        if (!packFolder.isDirectory()) {
            try {
                Files.createDirectories(packFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        checkDefaultPack();
        loadPacks(packFolder);
    }

    private static void checkDefaultPack() {
        // 不管存不存在，强行覆盖
        String jarDefaultPackPath = String.format("/assets/%s/%s/%s", TouhouLittleMaid.MOD_ID, CUSTOM_PACK_DIR_NAME, DEFAULT_PACK_NAME);
        GetJarResources.copyTouhouLittleMaidFile(jarDefaultPackPath, PACK_FOLDER, DEFAULT_PACK_NAME);
    }

    private static void loadPacks(File packFolder) {
        File[] files = packFolder.listFiles(((dir, name) -> true));
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                readModelFromZipFile(file);
            }
            if (file.isDirectory()) {
                readModelFromFolder(file);
            }
        }
    }

    public static void readModelFromFolder(File root) {
        File[] domainFiles = root.toPath().resolve("assets").toFile().listFiles((dir, name) -> true);
        if (domainFiles == null) {
            return;
        }
        for (File domainDir : domainFiles) {
            if (domainDir.isDirectory()) {
                Path rootPath = root.toPath();
                String domain = domainDir.getName();
                loadMaidModelPack(rootPath, domain);
                loadChairModelPack(rootPath, domain);
            }
        }
    }

    public static void readModelFromZipFile(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> iteration = zipFile.entries();
            while (iteration.hasMoreElements()) {
                Matcher matcher = DOMAIN.matcher(iteration.nextElement().getName());
                if (matcher.find()) {
                    String domain = matcher.group(1);
                    loadMaidModelPack(zipFile, domain);
                    loadChairModelPack(zipFile, domain);
                    // 文件夹形式的不记录 crc32，也不往客户端同步
                    loadCrc32Info(file);
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void loadCrc32Info(File file) throws IOException {
        long crc32 = FileUtils.checksumCRC32(file);
        CRC32_FILE_MAP.putIfAbsent(crc32, file.toPath());
    }

    private static void loadMaidModelPack(Path rootPath, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        File file = rootPath.resolve("assets").resolve(domain).resolve(SERVER_MAID_MODELS.getJsonFileName()).toFile();
        if (!file.isFile()) {
            return;
        }
        try (InputStream stream = new FileInputStream(file)) {
            CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<MaidModelInfo>>() {
                    }.getType());
            pack.decorate();
            for (MaidModelInfo maidModelInfo : pack.getModelList()) {
                if (maidModelInfo.getEasterEgg() == null) {
                    String id = maidModelInfo.getModelId().toString();
                    SERVER_MAID_MODELS.putInfo(id, maidModelInfo);
                    LOGGER.debug(MARKER, "Loaded model info: {}", id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadMaidModelPack(ZipFile zipFile, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, SERVER_MAID_MODELS.getJsonFileName()));
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<MaidModelInfo>>() {
                    }.getType());
            pack.decorate();
            for (MaidModelInfo maidModelInfo : pack.getModelList()) {
                if (maidModelInfo.getEasterEgg() == null) {
                    String id = maidModelInfo.getModelId().toString();
                    SERVER_MAID_MODELS.putInfo(id, maidModelInfo);
                    LOGGER.debug(MARKER, "Loaded model info: {}", id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadChairModelPack(ZipFile zipFile, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, SERVER_CHAIR_MODELS.getJsonFileName()));
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            CustomModelPack<ChairModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<ChairModelInfo>>() {
                    }.getType());
            pack.decorate();
            for (ChairModelInfo chairModelInfo : pack.getModelList()) {
                String id = chairModelInfo.getModelId().toString();
                SERVER_CHAIR_MODELS.putInfo(id, chairModelInfo);
                LOGGER.debug(MARKER, "Loaded model info: {}", id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadChairModelPack(Path rootPath, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        File file = rootPath.resolve("assets").resolve(domain).resolve(SERVER_CHAIR_MODELS.getJsonFileName()).toFile();
        if (!file.isFile()) {
            return;
        }
        try (InputStream stream = new FileInputStream(file)) {
            CustomModelPack<ChairModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<ChairModelInfo>>() {
                    }.getType());
            pack.decorate();
            for (ChairModelInfo chairModelInfo : pack.getModelList()) {
                String id = chairModelInfo.getModelId().toString();
                SERVER_CHAIR_MODELS.putInfo(id, chairModelInfo);
                LOGGER.debug(MARKER, "Loaded model info: {}", id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    public static Map<Long, Path> getCrc32FileMap() {
        return CRC32_FILE_MAP;
    }
}
