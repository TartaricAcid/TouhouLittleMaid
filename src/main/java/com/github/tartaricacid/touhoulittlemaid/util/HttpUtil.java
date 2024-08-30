package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProgressListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class HttpUtil {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            .setDaemon(true).setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER))
            .setNameFormat("TLM Downloader %d").build();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool(THREAD_FACTORY);
    private static final ListeningExecutorService DOWNLOAD_EXECUTOR = MoreExecutors.listeningDecorator(EXECUTOR_SERVICE);

    private HttpUtil() {
    }

    public static CompletableFuture<?> downloadTo(File saveFile, URL packUrl, Map<String, String> requestProperties, int maxSize, @Nullable ProgressListener listener, Proxy proxy) {
        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            if (listener != null) {
                listener.progressStart(Component.translatable("resourcepack.downloading"));
                listener.progressStage(Component.translatable("resourcepack.requesting"));
            }

            try {
                try {
                    byte[] bytes = new byte[4096];
                    connection = (HttpURLConnection) packUrl.openConnection(proxy);
                    connection.setInstanceFollowRedirects(true);
                    float fileSize = 0.0F;
                    float requestSize = (float) requestProperties.entrySet().size();

                    for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                        if (listener != null) {
                            listener.progressStagePercentage((int) (++fileSize / requestSize * 100.0F));
                        }
                    }

                    inputStream = connection.getInputStream();
                    requestSize = (float) connection.getContentLength();
                    int contentLength = connection.getContentLength();
                    if (listener != null) {
                        listener.progressStage(Component.translatable("resourcepack.progress", String.format(Locale.ROOT, "%.2f", requestSize / 1000.0F / 1000.0F)));
                    }

                    if (saveFile.exists()) {
                        long saveFileLength = saveFile.length();
                        if (saveFileLength == (long) contentLength) {
                            if (listener != null) {
                                listener.stop();
                            }
                            return null;
                        }
                        LOGGER.warn("Deleting {} as it does not match what we currently have ({} vs our {}).", saveFile, contentLength, saveFileLength);
                        FileUtils.deleteQuietly(saveFile);
                    } else if (saveFile.getParentFile() != null) {
                        saveFile.getParentFile().mkdirs();
                    }

                    outputStream = new DataOutputStream(new FileOutputStream(saveFile));
                    if (maxSize > 0 && requestSize > (float) maxSize) {
                        if (listener != null) {
                            listener.stop();
                        }
                        throw new IOException("Filesize is bigger than maximum allowed (file is " + fileSize + ", limit is " + maxSize + ")");
                    }

                    int readSize;
                    while ((readSize = inputStream.read(bytes)) >= 0) {
                        fileSize += (float) readSize;
                        if (listener != null) {
                            listener.progressStagePercentage((int) (fileSize / requestSize * 100.0F));
                        }

                        if (maxSize > 0 && fileSize > (float) maxSize) {
                            if (listener != null) {
                                listener.stop();
                            }
                            throw new IOException("Filesize was bigger than maximum allowed (got >= " + fileSize + ", limit was " + maxSize + ")");
                        }

                        if (Thread.interrupted()) {
                            LOGGER.error("INTERRUPTED");
                            if (listener != null) {
                                listener.stop();
                            }
                            return null;
                        }

                        outputStream.write(bytes, 0, readSize);
                    }

                    if (listener != null) {
                        listener.stop();
                        return null;
                    }
                } catch (Throwable throwable) {
                    LOGGER.error("Failed to download file", throwable);
                    if (connection != null) {
                        InputStream errorStream = connection.getErrorStream();
                        try {
                            LOGGER.error("HTTP response error: {}", IOUtils.toString(errorStream, StandardCharsets.UTF_8));
                        } catch (IOException ioexception) {
                            LOGGER.error("Failed to read response from server");
                        }
                    }

                    if (listener != null) {
                        listener.stop();
                        return null;
                    }
                }

                return null;
            } finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        }, DOWNLOAD_EXECUTOR);
    }
}
