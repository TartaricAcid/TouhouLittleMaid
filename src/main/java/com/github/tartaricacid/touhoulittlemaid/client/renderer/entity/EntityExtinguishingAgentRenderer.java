package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class EntityExtinguishingAgentRenderer extends EntityRenderer<EntityExtinguishingAgent> {
    public EntityExtinguishingAgentRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    @Nullable
    public ResourceLocation getTextureLocation(EntityExtinguishingAgent entity) {
        return null;
    }
}
