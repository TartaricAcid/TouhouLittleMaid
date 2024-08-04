package com.github.tartaricacid.touhoulittlemaid.util;

import org.apache.commons.lang3.StringUtils;

import static net.minecraft.resources.ResourceLocation.isValidNamespace;
import static net.minecraft.resources.ResourceLocation.isValidPath;

public class ResourceLoactionUtil {
    public static boolean isValidResourceLocation(String pLocation) {
        String[] astring = decompose(pLocation, ':');
        return isValidNamespace(StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0]) && isValidPath(astring[1]);
    }

    protected static String[] decompose(String pLocation, char pSeparator) {
        String[] astring = new String[]{"minecraft", pLocation};
        int i = pLocation.indexOf(pSeparator);
        if (i >= 0) {
            astring[1] = pLocation.substring(i + 1);
            if (i >= 1) {
                astring[0] = pLocation.substring(0, i);
            }
        }

        return astring;
    }
}
