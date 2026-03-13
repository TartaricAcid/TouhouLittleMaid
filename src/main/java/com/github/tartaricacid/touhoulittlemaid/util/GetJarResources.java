package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public final class GetJarResources {
    private GetJarResources() {
    }

    /**
     * 复制本模组的文件到指定文件夹
     *
     * @param filePath jar 里面的文件地址
     * @param destPath 想要复制到的目录
     * @param fileName 复制后的文件名
     */
    public static void copyTouhouLittleMaidFile(String filePath, Path destPath, String fileName) {
        URL url = TouhouLittleMaid.class.getResource(filePath);
        try {
            if (url != null) {
                FileUtils.copyURLToFile(url, destPath.resolve(fileName).toFile());
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to copy built-in resource {}", filePath, e);
        }
    }

    @Nullable
    public static InputStream readTouhouLittleMaidFile(String filePath) {
        URL url = TouhouLittleMaid.class.getResource(filePath);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to read built-in resource {}", filePath, e);
        }
        return null;
    }

    public static void copyFolder(String sourcePath, Path targetPath) throws IOException, URISyntaxException {
        URL url = TouhouLittleMaid.class.getResource(sourcePath);
        if (url == null) {
            return;
        }
        URI uri = url.toURI();
        Path sourceFolderPath = Paths.get(uri);
        try (Stream<Path> stream = Files.walk(sourceFolderPath, Integer.MAX_VALUE)) {
            stream.forEach(source -> {
                // 尝试对 kilt 的兼容修改，感谢 B 站用户：shiroha233
                // 使用相对路径计算，避免URI转换产生的非法字符
                Path relativePath = sourceFolderPath.relativize(source);
                // 将 JAR 文件系统的相对路径转换为字符串，然后让目标文件系统重新解析
                // 这样避免了不同文件系统 Provider 之间的冲突
                String relativePathString = relativePath.toString().replace('\\', '/');
                Path target = targetPath.resolve(relativePathString);
                try {
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        // 确保目标目录存在
                        Path parentDir = target.getParent();
                        if (parentDir != null && !Files.isDirectory(parentDir)) {
                            Files.createDirectories(parentDir);
                        }
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    TouhouLittleMaid.LOGGER.error("Failed to copy file from {} to target: {}", source, e.getMessage());
                } catch (Exception e) {
                    TouhouLittleMaid.LOGGER.error("Unexpected error during file copy: {}", e.getMessage());
                }
            });
        }
    }
}
