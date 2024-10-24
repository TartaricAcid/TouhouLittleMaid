package com.github.tartaricacid.touhoulittlemaid.mixin.compat.sodium;

import com.github.tartaricacid.touhoulittlemaid.compat.sodium.sodium.EntityTextureTerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderTextureSlot;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(DefaultShaderInterface.class)
public abstract class DefaultShaderInterfaceMixin {
    @Shadow
    @Deprecated(
            forRemoval = true
    )
    protected abstract void bindTexture(ChunkShaderTextureSlot slot, int textureId);

    @Unique
    private TerrainRenderPass eyelib$terrainRenderPass;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
        eyelib$terrainRenderPass = options.pass();
    }

    @Inject(method = "setupState", at = @At("TAIL"))
    public void setupState(CallbackInfo ci) {
        if (eyelib$terrainRenderPass instanceof EntityTextureTerrainRenderPass pass) {
            bindTexture(ChunkShaderTextureSlot.BLOCK, pass.getTexture().getId());
        }
    }
}
