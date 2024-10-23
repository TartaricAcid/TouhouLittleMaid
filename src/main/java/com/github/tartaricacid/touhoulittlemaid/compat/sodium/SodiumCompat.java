package com.github.tartaricacid.touhoulittlemaid.compat.sodium;

import com.github.tartaricacid.touhoulittlemaid.compat.sodium.client.DynamicChunkBufferSodiumCompat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.LoadingModList;

public final class SodiumCompat {
    private static final String ID = "sodium";
    private static boolean INSTALLED;

    public static void init() {
        INSTALLED = LoadingModList.get().getModFileById(ID) != null;
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }

    public static RenderType addSodiumCutoutPass(ResourceLocation resourceLocation, RenderType cutoutRenderType) {
        return Util.make(cutoutRenderType, renderType -> {
            if (isInstalled()) {
                DynamicChunkBufferSodiumCompat.markCutout(renderType, resourceLocation);
            }
        });
    }

    public static RenderType addSodiumTranslucentPass(ResourceLocation resourceLocation, RenderType translucentRenderType) {
        return Util.make(translucentRenderType, renderType -> {
            if (isInstalled()) {
                DynamicChunkBufferSodiumCompat.markTranslucent(renderType, resourceLocation);
            }
        });
    }
}
