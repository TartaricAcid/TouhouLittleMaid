package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityExtinguishingAgentRender extends Render<EntityExtinguishingAgent> {
    public static final EntityExtinguishingAgentRender.Factory FACTORY = new EntityExtinguishingAgentRender.Factory();

    public EntityExtinguishingAgentRender(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityExtinguishingAgent entity) {
        return null;
    }

    public static class Factory implements IRenderFactory<EntityExtinguishingAgent> {
        @Override
        public Render<? super EntityExtinguishingAgent> createRenderFor(RenderManager manager) {
            return new EntityExtinguishingAgentRender(manager);
        }
    }
}
