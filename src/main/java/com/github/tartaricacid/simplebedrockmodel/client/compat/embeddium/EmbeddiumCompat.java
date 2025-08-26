package com.github.tartaricacid.simplebedrockmodel.client.compat.embeddium;


import net.neoforged.fml.ModList;

public class EmbeddiumCompat {
    public static final String EMBEDDIUM = "embeddium";
    public static boolean IS_EMBEDDIUM_INSTALLED = false;

    public static void init() {
        IS_EMBEDDIUM_INSTALLED = ModList.get().getModContainerById(EMBEDDIUM).isPresent();
    }

    public static boolean isEmbeddiumInstalled() {
        return IS_EMBEDDIUM_INSTALLED;
    }
}
