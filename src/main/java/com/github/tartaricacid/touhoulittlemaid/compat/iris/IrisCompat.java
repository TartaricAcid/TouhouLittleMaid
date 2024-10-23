package com.github.tartaricacid.touhoulittlemaid.compat.iris;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.cache.QuadListBakingVertexConsumer;
import com.github.tartaricacid.touhoulittlemaid.compat.iris.client.DynamicChunkBufferIrisCompat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.loading.LoadingModList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class IrisCompat {
    private static final String ID = "iris";
    private static boolean INSTALLED = false;

    public static void init() {
        INSTALLED = LoadingModList.get().getModFileById(ID) != null;
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }

    public static RenderType unwrapIrisRenderType(RenderType renderType) {
        return IrisCompat.isInstalled() ? DynamicChunkBufferIrisCompat.unwrap(renderType) : renderType;
    }

    @NotNull
    public static MultiBufferSource getRenderType(HashMap<RenderType, QuadListBakingVertexConsumer> builder) {
        if (IrisCompat.isInstalled()) {
            return renderType -> builder.computeIfAbsent(DynamicChunkBufferIrisCompat.unwrap(renderType), QuadListBakingVertexConsumer::new);
        } else {
            return renderType -> builder.computeIfAbsent(renderType, QuadListBakingVertexConsumer::new);
        }
    }
}
