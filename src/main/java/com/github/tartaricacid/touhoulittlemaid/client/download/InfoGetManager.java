package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.util.ZipFileCheck;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.HttpUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author TartaricAcid
 * @date 2020/1/12 15:32
 **/
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InfoGetManager {
    /**
     * 线路 1，PCL 作者宅魂 Kill 提供的 CDN
     */
    private static final String ROOT_URL = "https://tlmdl.cfpa.team/";
    private static final String INFO_JSON_URL = ROOT_URL + "info.json";
    /**
     * 线路 2，原文件地址
     */
    private static final String ROOT_URL_BACKUP = "http://tlm.cfpa.team:29434/";
    private static final String INFO_JSON_URL_BACKUP = ROOT_URL_BACKUP + "info.json";
    /**
     * info 文件最大允许 1M
     */
    private static final int INFO_MAX_FILE_SIZE = 1024 * 1024;
    /**
     * 模型文件最大只允许 25M，MOJANG 最大允许的下载上限也是这个大小
     */
    private static final int PACK_MAX_FILE_SIZE = 25 * 1024 * 1024;

    private static final Path ROOT_FOLDER = Paths.get(Minecraft.getInstance().gameDirectory.toURI()).resolve("config").resolve(TouhouLittleMaid.MOD_ID);
    private static final Path INFO_JSON_FILE = ROOT_FOLDER.resolve("info.json");
    private static final Path PACK_FOLDER = ROOT_FOLDER.resolve("file");

    private static boolean USE_BACKUP_URL = false;

    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_ALL = Lists.newArrayList();
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_MAID = Lists.newArrayList();
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_CHAIR = Lists.newArrayList();
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_SOUND = Lists.newArrayList();

    public static Statue STATUE = Statue.FIRST;

    public static Map<String, String> getDownloadHeaders() {
        Map<String, String> map = Maps.newHashMap();
        User user = Minecraft.getInstance().getUser();
        WorldVersion currentVersion = SharedConstants.getCurrentVersion();

        map.put("X-Minecraft-Username", user.getName());
        map.put("X-Minecraft-UUID", user.getUuid());
        map.put("X-Minecraft-Version", currentVersion.getName());
        map.put("X-Minecraft-Version-ID", currentVersion.getId());
        map.put("X-Forge-Version", ForgeVersion.getVersion());
        map.put("X-TLM-Version", ModList.get().getModFileById(TouhouLittleMaid.MOD_ID).versionString());
        map.put("User-Agent", "Minecraft Java/" + currentVersion.getName());

        return map;
    }

    public static void checkInfoJsonFile() {
        // 如果文件夹不存在，首先创建文件夹
        if (!ROOT_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(ROOT_FOLDER);
            } catch (IOException ioe) {
                ioe.fillInStackTrace();
                return;
            }
        }

        // 开始下载 info 文件
        File infoJsonFile = INFO_JSON_FILE.toFile();
        try {
            downloadInfoJson(infoJsonFile);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static String getFileMd5(File file) {
        String md5 = StringUtils.EMPTY;
        // 文件不存在，返回空字符串
        if (!file.isFile()) {
            return md5;
        }
        try (FileInputStream stream = new FileInputStream(file)) {
            md5 = DigestUtils.md5Hex(stream);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        return md5;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void downloadInfoJson(File infoJsonFile) throws IOException {
        Proxy proxy = Minecraft.getInstance().getProxy();
        // 线路 1
        URL infoJsonUrl = new URL(INFO_JSON_URL);
        // 线路 2
        URL infoJsonUrlBackup = new URL(INFO_JSON_URL_BACKUP);

        // 先计算一次 info 文件 MD5，用于检查是否更新过
        String md5Previous = getFileMd5(infoJsonFile);

        // 开始异步下载文件
        // HttpUtil 自带文件比对功能，所以不需要再次验证 MD5 了
        CompletableFuture downloader = HttpUtil.downloadTo(infoJsonFile, infoJsonUrl, getDownloadHeaders(), INFO_MAX_FILE_SIZE, null, proxy);
        downloader.exceptionallyCompose(error -> {
            // 如果线路 1 出现异常，尝试线路 2 下载
            TouhouLittleMaid.LOGGER.warn("Line 1 is inaccessible, try to use line 2");
            USE_BACKUP_URL = true;
            return HttpUtil.downloadTo(infoJsonFile, infoJsonUrlBackup, getDownloadHeaders(), INFO_MAX_FILE_SIZE, null, proxy);
        }).thenRun(() -> {
            // 如果成功，那么加载 info 文件
            loadInfoJson(infoJsonFile);
            TouhouLittleMaid.LOGGER.info("The download info file was successfully updated and loaded");
            // 再次计算 info 文件 MD5
            String md5Current = getFileMd5(infoJsonFile);
            // 比对前后 MD5 大小，用来判断是否更新过，用于游戏内提示
            if (StringUtils.isBlank(md5Previous) || StringUtils.isBlank(md5Current)) {
                return;
            }
            if (md5Previous.equals(md5Current)) {
                STATUE = Statue.NOT_UPDATE;
            } else {
                STATUE = Statue.UPDATE;
            }
        }).exceptionally(error -> {
            // 异常，那么打印错误报告
            TouhouLittleMaid.LOGGER.warn("Failed to download info file, possibly due to network issues");
            return null;
        });
    }

    private static void loadInfoJson(File infoJsonFile) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(infoJsonFile), StandardCharsets.UTF_8)) {
            // 下载信息进行读取加载
            DOWNLOAD_INFO_LIST_ALL = CustomPackLoader.GSON.fromJson(reader, new TypeToken<List<DownloadInfo>>() {
            }.getType());
            DOWNLOAD_INFO_LIST_ALL.forEach(DownloadInfo::decorate);
            // 下载信息分类，用于游戏内显示
            DOWNLOAD_INFO_LIST_ALL.forEach(downloadInfo -> {
                if (downloadInfo.hasType(DownloadInfo.TypeEnum.MAID)) {
                    DOWNLOAD_INFO_LIST_MAID.add(downloadInfo);
                }
                if (downloadInfo.hasType(DownloadInfo.TypeEnum.CHAIR)) {
                    DOWNLOAD_INFO_LIST_CHAIR.add(downloadInfo);
                }
                if (downloadInfo.hasType(DownloadInfo.TypeEnum.SOUND)) {
                    DOWNLOAD_INFO_LIST_SOUND.add(downloadInfo);
                }
            });
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(InfoGetManager::checkInfoJsonFile);
    }

    public static void downloadPack(DownloadInfo info) {
        try {
            Proxy proxy = Minecraft.getInstance().getProxy();
            // 状态设置为下载
            info.setStatus(DownloadStatus.DOWNLOADING);
            // 依据先前情况，选择线路
            String rootUrl = USE_BACKUP_URL ? ROOT_URL_BACKUP : ROOT_URL;
            URL url = new URL(new URL(rootUrl), info.getUrl());
            // 文件先下载到缓存文件夹，然后才复制到模型文件夹中
            File fileInTlmModel = CustomPackLoader.PACK_FOLDER.resolve(info.getFileName()).toFile();
            File fileInCache = PACK_FOLDER.resolve(info.getFileName()).toFile();
            // 缓存文件不存在，或者需要更新
            if (!fileInCache.isFile() || FileUtils.checksumCRC32(fileInCache) != info.getChecksum()) {
                downloadPack(info, fileInCache, url, proxy, fileInTlmModel);
            } else {
                // 存在？那就直接复制加载即可
                reloadPack(info, fileInCache, fileInTlmModel);
                sendDownloadMessage(Component.translatable("gui.touhou_little_maid.resources_download.state.downloaded", info.getFileName(), 0.943));
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void downloadPack(DownloadInfo info, File fileInCache, URL url, Proxy proxy, File fileInTlmModel) {
        // 向游戏内玩家发送我们正在下载的提示
        sendDownloadMessage(Component.translatable("gui.touhou_little_maid.resources_download.state.downloading", info.getFileName()));
        // 开始计时
        StopWatch stopWatch = StopWatch.createStarted();
        // 异步下载
        CompletableFuture downloader = HttpUtil.downloadTo(fileInCache, url, getDownloadHeaders(), PACK_MAX_FILE_SIZE, info, proxy);
        downloader.thenRun(() -> {
            // 如果正常下载完成，停止计时，发送提示，并进行加载
            stopWatch.stop();
            sendDownloadMessage(Component.translatable("gui.touhou_little_maid.resources_download.state.downloaded", info.getFileName(), stopWatch.getTime(TimeUnit.MILLISECONDS) / 1000.0));
            try {
                reloadPack(info, fileInCache, fileInTlmModel);
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).exceptionally(error -> {
            // 异常？那么清理相关内容，并打印提示
            stopWatch.stop();
            info.setStatus(DownloadStatus.NOT_DOWNLOAD);
            TouhouLittleMaid.LOGGER.warn("Failed to download pack file, possibly due to network issues");
            return null;
        });
    }

    private static void reloadPack(DownloadInfo info, File fileInCache, File fileInTlmModel) throws IOException {
        // 检查下载文件是否为完整 ZIP 文件
        if (ZipFileCheck.isZipFile(fileInCache)) {
            // 复制到指定模型文件夹，并进行加载
            Files.copy(fileInCache.toPath(), fileInTlmModel.toPath(), StandardCopyOption.REPLACE_EXISTING);
            CustomPackLoader.readModelFromZipFile(fileInTlmModel);
            ServerCustomPackLoader.reloadPacks();
            info.setStatus(DownloadStatus.DOWNLOADED);
        } else {
            // 否则提示
            info.setStatus(DownloadStatus.NOT_DOWNLOAD);
            TouhouLittleMaid.LOGGER.error("{} file is corrupt and cannot be loaded.", info.getFileName());
        }
    }

    public static void sendDownloadMessage(MutableComponent component) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.sendSystemMessage(component);
        }
    }

    public static List<DownloadInfo> getTypedDownloadInfoList(DownloadInfo.TypeEnum typeEnum) {
        return switch (typeEnum) {
            case CHAIR -> DOWNLOAD_INFO_LIST_CHAIR;
            case SOUND -> DOWNLOAD_INFO_LIST_SOUND;
            default -> DOWNLOAD_INFO_LIST_MAID;
        };
    }

    public static int getPackMaxFileSize() {
        return PACK_MAX_FILE_SIZE;
    }

    public static Path getPackFolder() {
        return PACK_FOLDER;
    }

    public enum Statue {
        FIRST, UPDATE, NOT_UPDATE
    }
}
