package com.github.tartaricacid.touhoulittlemaid.compat.sodium.embeddium;


import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.Material;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.parameters.AlphaCutoffParameter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Argon4W
 */
public class DynamicChunkBufferEmbeddiumCompat {
    public static final ConcurrentHashMap<RenderType, TerrainRenderPass> DYNAMIC_CUTOUT_PASSES = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<RenderType, Material> DYNAMIC_CUTOUT_MATERIALS = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<RenderType, TerrainRenderPass> DYNAMIC_TRANSLUCENT_PASSES = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<RenderType, Material> DYNAMIC_TRANSLUCENT_MATERIALS = new ConcurrentHashMap<>();

    public static void markCutout(RenderType cutoutRenderType, ResourceLocation textureResourceLocation) {
        EntityTextureTerrainRenderPass cutoutPass = new EntityTextureTerrainRenderPass(cutoutRenderType, false, textureResourceLocation);
        Material material = new Material(cutoutPass, AlphaCutoffParameter.ONE_TENTH, true);

        DYNAMIC_CUTOUT_PASSES.putIfAbsent(cutoutRenderType, cutoutPass);
        DYNAMIC_CUTOUT_MATERIALS.putIfAbsent(cutoutRenderType, material);
    }

    public static void markTranslucent(RenderType translucentRenderType, ResourceLocation textureResourceLocation) {
        EntityTextureTerrainRenderPass translucentPass = new EntityTextureTerrainRenderPass(translucentRenderType, false, textureResourceLocation);
        Material material = new Material(translucentPass, AlphaCutoffParameter.ONE_TENTH, true);

        DYNAMIC_TRANSLUCENT_PASSES.putIfAbsent(translucentRenderType, translucentPass);
        DYNAMIC_TRANSLUCENT_MATERIALS.putIfAbsent(translucentRenderType, material);
    }
}
