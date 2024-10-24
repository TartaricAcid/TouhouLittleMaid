package com.github.tartaricacid.touhoulittlemaid.compat.sodium.embeddium;


import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;

/**
 * @author Argon4W
 */
public interface EmbeddiumShaderExtension {
    void eyelib$setTerrainRenderPass(TerrainRenderPass pass);
}
