package com.github.tartaricacid.touhoulittlemaid.mixin.compat.embeddium;


import com.github.tartaricacid.touhoulittlemaid.compat.sodium.embeddium.DynamicChunkBufferEmbeddiumCompat;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.embeddedt.embeddium.impl.render.chunk.region.RenderRegionManager;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(RenderRegionManager.class)
public abstract class RenderRegionManagerMixin {
    @WrapOperation(method = "uploadMeshes(Lorg/embeddedt/embeddium/impl/gl/device/CommandList;Lorg/embeddedt/embeddium/impl/render/chunk/region/RenderRegion;Ljava/util/Collection;)V", at = @At(value = "FIELD", target = "Lorg/embeddedt/embeddium/impl/render/chunk/terrain/DefaultTerrainRenderPasses;ALL:[Lorg/embeddedt/embeddium/impl/render/chunk/terrain/TerrainRenderPass;"))
    public TerrainRenderPass[] wrapAllTerrainRenderPasses(Operation<TerrainRenderPass[]> original) {
        return ImmutableList.<TerrainRenderPass>builder().add(original.call()).addAll(DynamicChunkBufferEmbeddiumCompat.DYNAMIC_CUTOUT_PASSES.values()).addAll(DynamicChunkBufferEmbeddiumCompat.DYNAMIC_TRANSLUCENT_PASSES.values()).build().toArray(TerrainRenderPass[]::new);
    }
}
