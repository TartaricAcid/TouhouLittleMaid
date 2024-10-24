package com.github.tartaricacid.touhoulittlemaid.mixin.compat.sodium;

import com.github.tartaricacid.touhoulittlemaid.compat.sodium.sodium.SodiumShaderExtension;
import net.caffeinemc.mods.sodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
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
@Mixin(ShaderChunkRenderer.class)
public class ShaderChunkRendererMixin {
    @Shadow
    protected GlProgram<ChunkShaderInterface> activeProgram;

    @Inject(method = "begin", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ChunkShaderInterface;setupState()V"))
    public void begin(TerrainRenderPass pass, CallbackInfo ci) {
        if (activeProgram.getInterface() instanceof SodiumShaderExtension extension) {
            extension.eyelib$setTerrainRenderPass(pass);
        }
    }
}
