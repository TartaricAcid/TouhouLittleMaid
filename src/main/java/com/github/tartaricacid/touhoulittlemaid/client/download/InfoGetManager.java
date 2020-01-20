package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2020/1/12 15:32
 **/
@SideOnly(Side.CLIENT)
public class InfoGetManager {
    private static final String INFO_JSON_URL = "https://gitlab.com/TartaricAcid/download/raw/master/info.json";
    public static List<DownloadInfo> DOWNLOAD_INFO_LIST = Lists.newArrayList();

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public static void downloadAndReadInfoJsonFile() {
        Thread thread = new Thread(() -> {
            try {
                File file = Minecraft.getMinecraft().gameDir.toPath().resolve("resourcepacks").resolve("info.json").toFile();
                TouhouLittleMaid.LOGGER.info("Downloading info.json file...");
                FileUtils.copyURLToFile(new URL(INFO_JSON_URL), file, 30_000, 30_000);
                TouhouLittleMaid.LOGGER.info("Downloaded info.json file");
                DOWNLOAD_INFO_LIST = CommonProxy.GSON.fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8),
                        new TypeToken<List<DownloadInfo>>() {
                        }.getType());
                DOWNLOAD_INFO_LIST.forEach(DownloadInfo::decorate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Download-Info-Json-File");
        thread.start();
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public static void downloadResourcesPack(DownloadInfo info) {
        Thread thread = new Thread(() -> {
            info.setStatus(DownloadStatus.DOWNLOADING);
            try {
                URL url = new URL(info.getUrl());
                File file = Minecraft.getMinecraft().gameDir.toPath().resolve("resourcepacks").resolve(info.getFileName()).toFile();
                TouhouLittleMaid.LOGGER.info("Downloading {} file...", info.getFileName());
                FileUtils.copyURLToFile(url, file, 60_000, 60_000);
                TouhouLittleMaid.LOGGER.info("Downloaded {} file", info.getFileName());
                info.setStatus(DownloadStatus.DOWNLOADED);
            } catch (IOException e) {
                info.setStatus(DownloadStatus.NOT_DOWNLOAD);
                e.printStackTrace();
            }
        }, String.format("Download-Resources-Pack-File-%s", info.getName()));
        thread.start();
    }
}
