package com.github.tartaricacid.touhoulittlemaid.mixin.compat.embeddium;


import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import com.github.tartaricacid.touhoulittlemaid.compat.sodium.embeddium.DynamicChunkBufferEmbeddiumCompat;
import net.minecraft.client.renderer.RenderType;
import org.embeddedt.embeddium.impl.render.EmbeddiumWorldRenderer;
import org.embeddedt.embeddium.impl.render.chunk.ChunkRenderMatrices;
import org.embeddedt.embeddium.impl.render.chunk.RenderSectionManager;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(EmbeddiumWorldRenderer.class)
public class EmbeddiumWorldRendererMixin {
    @Shadow
    private RenderSectionManager renderSectionManager;

    @Inject(method = "drawChunkLayer", at = @At("HEAD"), cancellable = true)
    public void drawChunkLayer(RenderType renderLayer, Matrix4f normal, double x, double y, double z, CallbackInfo ci) {
        if (DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.containsValue(renderLayer)) {
            ChunkRenderMatrices matrices = ChunkRenderMatrices.from(normal);
            renderSectionManager.renderLayer(matrices, DynamicChunkBufferEmbeddiumCompat.DYNAMIC_CUTOUT_PASSES.get(renderLayer), x, y, z);
            ci.cancel();
            return;
        }

        if (DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.containsValue(renderLayer)) {
            ChunkRenderMatrices matrices = ChunkRenderMatrices.from(normal);
            renderSectionManager.renderLayer(matrices, DynamicChunkBufferEmbeddiumCompat.DYNAMIC_TRANSLUCENT_PASSES.get(renderLayer), x, y, z);
            ci.cancel();
        }
    }
}
