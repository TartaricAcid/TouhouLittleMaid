package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2020/1/12 15:32
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class InfoGetManager {
    private static final String ROOT_URL = "http://tlm.cfpa.team:29434/";
    private static final String INFO_JSON_URL = ROOT_URL + "info.json";
    private static final String INFO_JSON_MD5_URL = ROOT_URL + "info.json.md5";

    private static final Path ROOT_FOLDER = Paths.get(System.getProperty("user.home")).resolve("touhou_little_maid");
    private static final Path INFO_JSON_FILE = ROOT_FOLDER.resolve("info.json");
    private static final Path PACK_FOLDER = ROOT_FOLDER.resolve("file");

    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_ALL = Lists.newArrayList();
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_MAID = Lists.newArrayList();
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_CHAIR = Lists.newArrayList();
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST_SOUND = Lists.newArrayList();

    public static void checkInfoJsonFile() {
        // 判定文件夹是否存在
        if (!ROOT_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(ROOT_FOLDER);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }

        // 检查文件是否存在
        if (!INFO_JSON_FILE.toFile().isFile()) {
            downloadAndReadInfoJsonFile();
        } else {
            try {
                FileInputStream stream = new FileInputStream(INFO_JSON_FILE.toFile());
                downloadAndReadInfoJsonFile(DigestUtils.md5Hex(stream));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    private static void downloadAndReadInfoJsonFile() {
        downloadAndReadInfoJsonFile(null);
    }

    private static void downloadAndReadInfoJsonFile(@Nullable String md5) {
        Thread thread = new Thread(() -> {
            try {
                File file = INFO_JSON_FILE.toFile();
                if (StringUtils.isNotBlank(md5)) {
                    String md5Remote = IOUtils.toString(new URL(INFO_JSON_MD5_URL), StandardCharsets.UTF_8);
                    if (md5Remote.equals(md5)) {
                        TouhouLittleMaid.LOGGER.info("info.json file no update required");
                    } else {
                        TouhouLittleMaid.LOGGER.info("Downloading info.json file...");
                        FileUtils.copyURLToFile(new URL(INFO_JSON_URL), file, 30_000, 30_000);
                        TouhouLittleMaid.LOGGER.info("Downloaded info.json file");
                    }
                } else {
                    TouhouLittleMaid.LOGGER.info("Downloading info.json file...");
                    FileUtils.copyURLToFile(new URL(INFO_JSON_URL), file, 30_000, 30_000);
                    TouhouLittleMaid.LOGGER.info("Downloaded info.json file");
                }
                DOWNLOAD_INFO_LIST_ALL = CommonProxy.GSON.fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8),
                        new TypeToken<List<DownloadInfo>>() {
                        }.getType());
                DOWNLOAD_INFO_LIST_ALL.forEach(DownloadInfo::decorate);
                // 分类
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
                e.printStackTrace();
            }
        }, "Download-Info-Json-File");
        thread.start();
    }

    @SubscribeEvent
    public static void onResourceReload(TextureStitchEvent.Post event) {
        for (DownloadInfo info : DOWNLOAD_INFO_LIST_ALL) {
            @Nonnull HashMap<String, String> langMap = info.getLanguage("en_us");
            for (String key : langMap.keySet()) {
                I18n.i18nLocale.properties.put(key, langMap.get(key));
            }

            String currentLanguage = Minecraft.getMinecraft().gameSettings.language;
            @Nullable HashMap<String, String> currentLangMap = info.getLanguage(currentLanguage);
            if (currentLangMap != null) {
                for (String key : currentLangMap.keySet()) {
                    I18n.i18nLocale.properties.put(key, currentLangMap.get(key));
                }
            }
        }
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public static void downloadResourcesPack(DownloadInfo info) {
        Thread thread = new Thread(() -> {
            info.setStatus(DownloadStatus.DOWNLOADING);
            try {
                URL url = new URL(new URL(ROOT_URL), info.getUrl());
                File fileInMinecraft = Minecraft.getMinecraft().gameDir.toPath().resolve("resourcepacks").resolve(info.getFileName()).toFile();
                File fileInCache = PACK_FOLDER.resolve(info.getFileName()).toFile();
                if (!fileInCache.isFile() || FileUtils.checksumCRC32(fileInCache) != info.getChecksum()) {
                    TouhouLittleMaid.LOGGER.info("Downloading {} file...", info.getFileName());
                    FileUtils.copyURLToFile(url, fileInCache, 60_000, 60_000);
                    TouhouLittleMaid.LOGGER.info("Downloaded {} file", info.getFileName());
                } else {
                    TouhouLittleMaid.LOGGER.info("file {} existed in cache folder", info.getFileName());
                }
                Files.copy(fileInCache.toPath(), fileInMinecraft.toPath(), StandardCopyOption.REPLACE_EXISTING);
                info.setStatus(DownloadStatus.DOWNLOADED);
            } catch (IOException e) {
                info.setStatus(DownloadStatus.NOT_DOWNLOAD);
                e.printStackTrace();
            }
        }, String.format("Download-Resources-Pack-File-%s", info.getName()));
        thread.start();
    }

    public static List<DownloadInfo> getTypedDownloadInfoList(DownloadInfo.TypeEnum typeEnum) {
        switch (typeEnum) {
            default:
            case MAID:
                return DOWNLOAD_INFO_LIST_MAID;
            case CHAIR:
                return DOWNLOAD_INFO_LIST_CHAIR;
            case SOUND:
                return DOWNLOAD_INFO_LIST_SOUND;
        }
    }
}
