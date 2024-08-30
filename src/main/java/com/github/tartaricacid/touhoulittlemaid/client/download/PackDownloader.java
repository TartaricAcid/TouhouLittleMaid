package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import net.minecraft.FileUtil;
import net.minecraft.util.HttpUtil;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalLong;

@SuppressWarnings("UnstableApiUsage")
public class PackDownloader {
    public static Path downloadFile(Path pSaveFile, URL pUrl, Map<String, String> pRequestProperties, HashFunction pHashFunction, int pMaxSize, Proxy pProxy, @Nullable HttpUtil.DownloadProgressListener pProgressListener) {
        HttpURLConnection httpurlconnection = null;
        InputStream inputstream = null;
        if (pProgressListener != null) {
            pProgressListener.requestStart();
        }

        try {
            httpurlconnection = (HttpURLConnection)pUrl.openConnection(pProxy);
            httpurlconnection.setInstanceFollowRedirects(true);
            Objects.requireNonNull(httpurlconnection);
            pRequestProperties.forEach(httpurlconnection::setRequestProperty);
            inputstream = httpurlconnection.getInputStream();
            long i = httpurlconnection.getContentLengthLong();
            OptionalLong optionallong = i != -1L ? OptionalLong.of(i) : OptionalLong.empty();
            try {
                FileUtil.createDirectoriesSafe(pSaveFile.getParent());
            } catch (FileAlreadyExistsException ignored) {
            }
            if (pProgressListener != null) {
                pProgressListener.downloadStart(optionallong);
            }
            if (optionallong.isPresent() && optionallong.getAsLong() > (long)pMaxSize) {
                String string = String.valueOf(optionallong);
                throw new IOException("File size is bigger than maximum allowed (file is " + string + ", limit is " + pMaxSize + ")");
            }

            Path path3 = Files.createTempFile(pSaveFile.getParent(), "download", ".tmp");

            try {
                HashCode hashcode1 = downloadAndHash(pHashFunction, pMaxSize, pProgressListener, inputstream, path3);
                if (!checkExistingFile(pSaveFile, pHashFunction, hashcode1)) {
                    Files.move(path3, pSaveFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    updateModificationTime(pSaveFile);
                }

                if (pProgressListener != null) {
                    pProgressListener.requestFinished(true);
                }
                return pSaveFile;
            } finally {
                Files.deleteIfExists(path3);
            }
        } catch (Throwable throwable) {
            if (httpurlconnection != null) {
                InputStream stream = httpurlconnection.getErrorStream();
                if (stream != null) {
                    try {
                        TouhouLittleMaid.LOGGER.error("HTTP response error: {}", IOUtils.toString(stream, StandardCharsets.UTF_8));
                    } catch (Exception var33) {
                        TouhouLittleMaid.LOGGER.error("Failed to read response from server");
                    }
                }
            }

            if (pProgressListener != null) {
                pProgressListener.requestFinished(false);
            }
            throw new IllegalStateException("Failed to download file " + pUrl, throwable);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private static void updateModificationTime(Path pPath) {
        try {
            Files.setLastModifiedTime(pPath, FileTime.from(Instant.now()));
        } catch (IOException ioException) {
            TouhouLittleMaid.LOGGER.warn("Failed to update modification time of {}", pPath, ioException);
        }
    }

    public static HashCode hashFile(Path pPath, HashFunction pHashFunction) throws IOException {
        Hasher hasher = pHashFunction.newHasher();
        OutputStream outputstream = Funnels.asOutputStream(hasher);

        try {
            InputStream inputstream = Files.newInputStream(pPath);

            try {
                inputstream.transferTo(outputstream);
            } catch (Throwable var9) {
                try {
                    inputstream.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }

            inputstream.close();
        } catch (Throwable var10) {
            try {
                outputstream.close();
            } catch (Throwable var7) {
                var10.addSuppressed(var7);
            }
            throw var10;
        }

        outputstream.close();

        return hasher.hash();
    }

    private static boolean checkExistingFile(Path pPath, HashFunction pHashFunction, HashCode pExpectedHash) throws IOException {
        if (Files.exists(pPath)) {
            HashCode hashcode = hashFile(pPath, pHashFunction);
            if (hashcode.equals(pExpectedHash)) {
                return true;
            }
            TouhouLittleMaid.LOGGER.warn("Mismatched hash of file {}, expected {} but found {}", new Object[]{pPath, pExpectedHash, hashcode});
        }
        return false;
    }

    private static HashCode downloadAndHash(HashFunction pHashFunction, int pMaxSize, @Nullable HttpUtil.DownloadProgressListener pProgressListener, InputStream pStream, Path pOutputPath) throws IOException {
        OutputStream outputstream = Files.newOutputStream(pOutputPath, StandardOpenOption.CREATE);

        HashCode hashcode;
        try {
            Hasher hasher = pHashFunction.newHasher();
            byte[] bytes = new byte[8196];
            long j = 0L;

            int i;
            while((i = pStream.read(bytes)) >= 0) {
                j += i;
                if (pProgressListener != null) {
                    pProgressListener.downloadedBytes(j);
                }
                if (j > (long)pMaxSize) {
                    throw new IOException("File size was bigger than maximum allowed (got >= " + j + ", limit was " + pMaxSize + ")");
                }

                if (Thread.interrupted()) {
                    TouhouLittleMaid.LOGGER.error("INTERRUPTED");
                    throw new IOException("Download interrupted");
                }

                outputstream.write(bytes, 0, i);
                hasher.putBytes(bytes, 0, i);
            }

            hashcode = hasher.hash();
        } catch (Throwable var13) {
            if (outputstream != null) {
                try {
                    outputstream.close();
                } catch (Throwable var12) {
                    var13.addSuppressed(var12);
                }
            }
            throw var13;
        }

        if (outputstream != null) {
            outputstream.close();
        }

        return hashcode;
    }
}
