package com.github.tartaricacid.touhoulittlemaid.util;

import org.apache.commons.lang3.StringUtils;

public final class DataUtils {
    public static boolean isValidResourceLocation(String name) {
        String[] strings = decompose(name, ':');
        return isValidNamespace(StringUtils.isEmpty(strings[0]) ? "minecraft" : strings[0]) && isValidPath(strings[1]);
    }

    public static String[] decompose(String name, char split) {
        String[] strings = new String[]{"minecraft", name};
        int index = name.indexOf(split);
        if (index >= 0) {
            strings[1] = name.substring(index + 1);
            if (index >= 1) {
                strings[0] = name.substring(0, index);
            }
        }
        return strings;
    }

    public static boolean validPathChar(char charValue) {
        return charValue == '_' || charValue == '-' || charValue >= 'a' && charValue <= 'z' || charValue >= '0' && charValue <= '9' || charValue == '/' || charValue == '.';
    }

    private static boolean validNamespaceChar(char charValue) {
        return charValue == '_' || charValue == '-' || charValue >= 'a' && charValue <= 'z' || charValue >= '0' && charValue <= '9' || charValue == '.';
    }

    public static boolean isValidPath(String path) {
        for (int i = 0; i < path.length(); ++i) {
            if (!validPathChar(path.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidNamespace(String name) {
        for (int i = 0; i < name.length(); ++i) {
            if (!validNamespaceChar(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
