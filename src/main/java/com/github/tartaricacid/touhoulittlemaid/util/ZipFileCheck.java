package com.github.tartaricacid.touhoulittlemaid.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 作者：Yocn <br>
 * 链接：<a href="https://juejin.cn/post/7220985690795933756">https://juejin.cn/post/7220985690795933756</a>
 */
public final class ZipFileCheck {
    // 没有 zip 文件注释时候的目录结束符的偏移量
    private static final int RAW_END_OFFSET = 22;
    // 0x06054b50 占 4 个字节
    private static final int END_OF_DIR_LENGTH = 4;
    // 目录结束标识 0x06054b50 的小端读取方式
    private static final byte[] END_OF_DIR = new byte[]{0x50, 0x4B, 0x05, 0x06};

    public static boolean isZipFile(File file) throws IOException {
        if (file.exists() && file.isFile()) {
            if (file.length() <= RAW_END_OFFSET + END_OF_DIR_LENGTH) {
                return false;
            }
            long fileLength = file.length();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            // seek 到结束标记所在的位置
            randomAccessFile.seek(fileLength - RAW_END_OFFSET);
            byte[] end = new byte[END_OF_DIR_LENGTH];
            // 读取4个字节
            randomAccessFile.read(end);
            // 关掉文件
            randomAccessFile.close();
            return isEndOfDir(end);
        } else {
            return false;
        }
    }

    /**
     * 是否符合文件夹结束标记
     */
    private static boolean isEndOfDir(byte[] src) {
        if (src.length != END_OF_DIR_LENGTH) {
            return false;
        }
        for (int i = 0; i < src.length; i++) {
            if (src[i] != END_OF_DIR[i]) {
                return false;
            }
        }
        return true;
    }
}
