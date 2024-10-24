package com.github.tartaricacid.touhoulittlemaid.mixin.compat.sodium;

import com.github.tartaricacid.touhoulittlemaid.compat.sodium.sodium.DynamicChunkBufferSodiumCompat;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(RenderRegionManager.class)
public abstract class RenderRegionManagerMixin {
    @WrapOperation(method = "uploadResults(Lnet/caffeinemc/mods/sodium/client/gl/device/CommandList;Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion;Ljava/util/Collection;)V", at = @At(value = "FIELD", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/DefaultTerrainRenderPasses;ALL:[Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;"))
    public TerrainRenderPass[] wrapAllTerrainRenderPasses(Operation<TerrainRenderPass[]> original) {
        return ImmutableList.<TerrainRenderPass>builder().add(original.call()).addAll(DynamicChunkBufferSodiumCompat.DYNAMIC_CUTOUT_PASSES.values()).addAll(DynamicChunkBufferSodiumCompat.DYNAMIC_TRANSLUCENT_PASSES.values()).build().toArray(TerrainRenderPass[]::new);
    }
}
