package com.github.tartaricacid.simplebedrockmodel.client.bedrock;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;

/**
 * Interface for Bedrock model providers.
 * Provides methods to get the model, render bounding box, and a map of model parts.
 *
 * @param <T> The type of the Bedrock model provider.
 */
@OnlyIn(Dist.CLIENT)
public interface BedrockModelProvider<T extends BedrockModelProvider<T>> {
    /**
     * Gets the render bounding box for the model.
     *
     * @return The AABB representing the render bounding box.
     */
    AABB getRenderBoundingBox();

    /**
     * Gets a map of model parts.
     *
     * @return A HashMap where the key is a String identifier and the value is a BedrockPart.
     */
    HashMap<String, BedrockPart> getModelMap();
}