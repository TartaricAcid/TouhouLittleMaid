package com.github.tartaricacid.simplebedrockmodel.client.compat.sodium;


import net.neoforged.fml.ModList;

public class SodiumCompat {
    public static final String SODIUM = "sodium";
    public static boolean IS_SODIUM_INSTALLED = false;

    public static void init() {
        IS_SODIUM_INSTALLED = ModList.get().getModContainerById(SODIUM).isPresent();
    }

    public static boolean isSodiumInstalled() {
        return IS_SODIUM_INSTALLED;
    }
}
