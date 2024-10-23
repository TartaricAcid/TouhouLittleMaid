package com.github.tartaricacid.touhoulittlemaid.mixin.compat;

import com.github.tartaricacid.touhoulittlemaid.compat.sodium.client.EntityTextureTerrainRenderPass;
import com.github.tartaricacid.touhoulittlemaid.compat.sodium.client.SodiumShaderExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.irisshaders.iris.pipeline.programs.SodiumShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(SodiumShader.class)
public class SodiumShaderMixin implements SodiumShaderExtension {
    @Unique
    private TerrainRenderPass eyelib$terrainRenderPass;

    @Override
    public void eyelib$setTerrainRenderPass(TerrainRenderPass pass) {
        eyelib$terrainRenderPass = pass;
    }

    @WrapOperation(method = "bindTextures", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/gl/IrisRenderSystem;bindTextureToUnit(III)V", ordinal = 0))
    public void bindTextures(int target, int unit, int texture, Operation<Void> original) {
        original.call(texture, unit, eyelib$terrainRenderPass instanceof EntityTextureTerrainRenderPass entityPass ? entityPass.getTexture().getId() : texture);
    }
}
