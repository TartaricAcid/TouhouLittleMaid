package com.github.tartaricacid.touhoulittlemaid.mixin.compat.embeddium;

import org.embeddedt.embeddium.impl.render.chunk.terrain.DefaultTerrainRenderPasses;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.Material;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.parameters.AlphaCutoffParameter;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.parameters.MaterialParameters;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(Material.class)
public class MaterialMixin {
    @Mutable
    @Shadow
    @Final
    public AlphaCutoffParameter alphaCutoff;

    @Mutable
    @Shadow
    @Final
    public int packed;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(TerrainRenderPass pass, AlphaCutoffParameter alphaCutoff, boolean mipped, CallbackInfo ci) {
        this.alphaCutoff = pass == DefaultTerrainRenderPasses.TRANSLUCENT ? AlphaCutoffParameter.ONE_TENTH : alphaCutoff;
        this.packed = MaterialParameters.pack(this.alphaCutoff, mipped);
    }
}
