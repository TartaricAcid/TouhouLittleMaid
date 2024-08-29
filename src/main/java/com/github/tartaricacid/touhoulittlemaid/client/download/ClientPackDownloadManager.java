package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.ServerConfig;
import com.github.tartaricacid.touhoulittlemaid.util.ZipFileCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.HttpUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public static void downloadClientPack() {
        List<String> downloadList = ServerConfig.CLIENT_PACK_DOWNLOAD_URLS.get();
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
}
