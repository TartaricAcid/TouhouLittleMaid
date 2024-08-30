package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.ServerConfig;
import com.github.tartaricacid.touhoulittlemaid.util.HttpUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ZipFileCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 依据服务端配置，下载客户端模型包
 */
@OnlyIn(Dist.CLIENT)
public class ClientPackDownloadManager {
    /**
     * 缓存的配置文件哈希值，用于判断客户端是否需要更新
     */
    private static int CACHE_CONFIG_HASH = 0;

    public static void downloadClientPack() {
        List<String> downloadList = ServerConfig.CLIENT_PACK_DOWNLOAD_URLS.get();
        // 先计算哈希值
        int hashCurrent = hashDownloadList(downloadList);
        // 对比前后哈希值，判断是否需要更新
        if (CACHE_CONFIG_HASH != hashCurrent) {
            // 先清理，把服务端文件从内存中全清干净
            CustomPackLoader.reloadPacks();
            // 然后尝试加载服务端需要下载的文件
            downloadAllPack(downloadList);
            // 更新缓存的哈希值
            CACHE_CONFIG_HASH = hashCurrent;
        }
    }

    private static void downloadAllPack(List<String> downloadList) {
        downloadList.forEach(urlText -> {
            if (StringUtils.isBlank(urlText)) {
                return;
            }
            download(urlText);
        });
    }

    @SuppressWarnings("all")
    private static void download(String urlText) {
        Proxy proxy = Minecraft.getInstance().getProxy();
        try {
            URL url = new URL(urlText);
            String fileName = FilenameUtils.getName(url.getPath());
            File packFile = InfoGetManager.getPackFolder().resolve(fileName).toFile();
            downloadPack(url, packFile, proxy);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void downloadPack(URL url, File packFile, Proxy proxy) {
        String fileName = packFile.getName();
        // 向游戏内玩家发送我们正在下载的提示
        InfoGetManager.sendDownloadMessage(Component.translatable("gui.touhou_little_maid.resources_download.state.downloading", fileName));
        // 开始计时
        StopWatch stopWatch = StopWatch.createStarted();
        // 异步下载
        CompletableFuture downloader = HttpUtil.downloadTo(packFile, url, InfoGetManager.getDownloadHeaders(), InfoGetManager.getPackMaxFileSize(), null, proxy);
        downloader.thenRun(() -> {
            // 如果正常下载完成，停止计时，发送提示，并进行加载
            stopWatch.stop();
            InfoGetManager.sendDownloadMessage(Component.translatable("gui.touhou_little_maid.resources_download.state.downloaded", fileName, stopWatch.getTime(TimeUnit.MILLISECONDS) / 1000.0));
            try {
                reloadPack(packFile);
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).exceptionally(error -> {
            // 异常？那么清理相关内容，并打印提示
            stopWatch.stop();
            TouhouLittleMaid.LOGGER.warn("Failed to download pack file, possibly due to network issues");
            return null;
        });
    }

    private static void reloadPack(File packFile) throws IOException {
        String fileName = packFile.getName();
        // 检查下载文件是否为完整 ZIP 文件
        if (ZipFileCheck.isZipFile(packFile)) {
            CustomPackLoader.readModelFromZipFile(packFile);
        } else {
            TouhouLittleMaid.LOGGER.error("{} file is corrupt and cannot be loaded.", fileName);
        }
    }

    private static int hashDownloadList(List<String> downloadList) {
        int[] hash = new int[]{0};
        downloadList.stream().filter(StringUtils::isNoneBlank).forEach(entry -> hash[0] += entry.hashCode());
        return hash[0];
    }
}