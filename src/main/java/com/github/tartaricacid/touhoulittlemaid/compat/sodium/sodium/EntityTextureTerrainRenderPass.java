package com.github.tartaricacid.touhoulittlemaid.compat.sodium.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Argon4W
 */
public class EntityTextureTerrainRenderPass extends TerrainRenderPass {
    private final ResourceLocation textureResourceLocation;

    public EntityTextureTerrainRenderPass(RenderType renderType, boolean isTranslucent, ResourceLocation textureResourceLocation) {
        super(renderType, isTranslucent, true);
        this.textureResourceLocation = textureResourceLocation;
    }

    public AbstractTexture getTexture() {
        return Minecraft.getInstance().getTextureManager().getTexture(textureResourceLocation);
    }

    @Override
    public String toString() {
        return "[EntityTextureTerrainRenderPass: " + textureResourceLocation + "]";
    }
}
