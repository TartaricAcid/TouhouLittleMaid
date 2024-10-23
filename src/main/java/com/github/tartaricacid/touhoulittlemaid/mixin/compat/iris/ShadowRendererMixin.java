package com.github.tartaricacid.touhoulittlemaid.mixin.compat.iris;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.dynamic.DynamicChunkBuffers;
import com.llamalad7.mixinextras.sugar.Local;
import net.irisshaders.iris.mixin.LevelRendererAccessor;
import net.irisshaders.iris.shadows.ShadowRenderer;
import net.minecraft.client.Camera;
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
@Mixin(ShadowRenderer.class)
public class ShadowRendererMixin {
    @Shadow
    public static Matrix4f MODELVIEW;

    @Inject(method = "renderShadows", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/mixin/LevelRendererAccessor;invokeRenderSectionLayer(Lnet/minecraft/client/renderer/RenderType;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal = 2, shift = At.Shift.AFTER))
    public void renderShadow(LevelRendererAccessor levelRenderer, Camera playerCamera, CallbackInfo ci, @Local(name = "cameraX") double cameraX, @Local(name = "cameraY") double cameraY, @Local(name = "cameraZ") double cameraZ, @Local(name = "shadowProjection") Matrix4f shadowProjection) {
        DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.values().forEach(renderType -> levelRenderer.invokeRenderSectionLayer(renderType, cameraX, cameraY, cameraZ, MODELVIEW, shadowProjection));
        DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.values().forEach(renderType -> levelRenderer.invokeRenderSectionLayer(renderType, cameraX, cameraY, cameraZ, MODELVIEW, shadowProjection));
    }
}
