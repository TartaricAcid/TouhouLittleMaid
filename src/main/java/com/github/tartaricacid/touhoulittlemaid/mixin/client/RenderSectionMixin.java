package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

/**
 * @author Argon4W
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public class RenderSectionMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;chunkBufferLayers()Ljava/util/List;"))
    public List<RenderType> wrapChunkBufferLayers(Operation<List<RenderType>> original) {
        return ImmutableList.<RenderType>builder().addAll(original.call()).addAll(DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.values()).addAll(DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.values()).build();
    }
}
