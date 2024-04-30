package com.github.tartaricacid.touhoulittlemaid.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Utils {
    private static final MessageDigest DIGEST;

    static {
        try {
            DIGEST = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算字符串的 md5 结果
     */
    public static String md5Hex(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = DIGEST.digest(bytes);
        return toHexString(md5Bytes);
    }

    /**
     * 将 byte 形式的 md5 结果输出为字符串
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
