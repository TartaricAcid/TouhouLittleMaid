package com.github.tartaricacid.touhoulittlemaid.compat.invtweaks;

import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

public class InvTweaksCompat {
    public static final String INV_TWEAKS_ID = "invtweaks";
    private static boolean INSTALLED = false;

    public static void init() {
        ModFileInfo modFileById = LoadingModList.get().getModFileById(INV_TWEAKS_ID);
        INSTALLED = modFileById != null;
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }
}