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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    public static void copyFolder(String sourcePath, Path targetPath) throws IOException, URISyntaxException {
        URL url = TouhouLittleMaid.class.getResource(sourcePath);
        if (url == null) {
            return;
        }
        URI uri = url.toURI();
        try (Stream<Path> stream = Files.walk(Paths.get(uri), Integer.MAX_VALUE)) {
            stream.forEach(source -> {
                Path target = targetPath.resolve(uri.relativize(source.toUri()).toString());
                try {
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    TouhouLittleMaid.LOGGER.error(e.getMessage());
                }
            });
        }
    }
}
