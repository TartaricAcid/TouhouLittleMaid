package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

public final class GetJarResources {
    private GetJarResources() {
    }

    /**
     * 复制 TouhouLittleMaid 模组的资源文件夹
     *
     * @param folderPath 想要复制的源文件夹地址
     * @param destPath   想要复制到的文件夹地址
     */
    public static void copyTouhouLittleMaidFolder(String folderPath, Path destPath) {
        copyFolder(TouhouLittleMaid.class, folderPath, destPath);
    }

    /**
     * 复制 jar 里面的资源文件夹
     *
     * @param clz        jar 里面的某个类
     * @param folderPath 想要复制的源文件夹地址
     * @param destPath   想要复制到的文件夹地址
     */
    public static void copyFolder(Class<?> clz, String folderPath, Path destPath) {
        try {
            URL url = clz.getResource(folderPath);
            switch (url.getProtocol()) {
                // 在 idea 等环境下，资源部分实际上是以文件形式存在的，会触发下方的 file 条件
                case "file":
                    FileUtils.copyDirectory(new File(url.toURI()), destPath.toFile());
                    break;
                // 实际运行环境，资源部分成为了 jar 文件中的一部分，就需要通过此处进行复制了
                case "jar":
                case "modjar":
                    FileSystem fs = FileSystems.newFileSystem(url.toURI(), Collections.emptyMap());
                    // 因为此时获取得到的是 ZipPath，不能直接 toFile
                    // 所以 FileUtils.copyDirectory 自然也是用不了的，得自己实现复制文件夹功能
                    copyFolderFromJar(fs.getPath(folderPath), destPath);
                    // 需要显示关闭文件系统，否则再次调用会导致游戏崩溃
                    fs.close();
                    break;
                default:
                    TouhouLittleMaid.LOGGER.fatal("Unknown URL Protocol: {}", url.getProtocol());
                    throw new NullPointerException();
            }
        } catch (IOException | URISyntaxException e) {
            TouhouLittleMaid.LOGGER.error(e);
        }
    }

    private static void copyFolderFromJar(Path srcPath, Path destPath) throws IOException {
        // 可能复制去过的那个文件夹不存在，尝试进行创建
        // 注意用的不是 createDirectory
        Files.createDirectories(destPath);
        // walkFileTree 方法遍历文件，更优雅
        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // 文件夹不能复制，只能创建
                Files.createDirectories(destPath.resolve(srcPath.relativize(dir).toString()));
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 文件可以直接复制
                Files.copy(file, destPath.resolve(srcPath.relativize(file).toString()));
                return super.visitFile(file, attrs);
            }
        });
    }
}
