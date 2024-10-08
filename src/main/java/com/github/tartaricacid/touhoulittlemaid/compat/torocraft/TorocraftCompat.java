package com.github.tartaricacid.touhoulittlemaid.compat.torocraft;

import net.minecraftforge.fml.loading.LoadingModList;

public class TorocraftCompat {
    private static final String MOD_ID = "torohealth";
    private static boolean INSTALLED;

    public static void init() {
        INSTALLED = LoadingModList.get().getModFileById(MOD_ID) != null;
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }
}