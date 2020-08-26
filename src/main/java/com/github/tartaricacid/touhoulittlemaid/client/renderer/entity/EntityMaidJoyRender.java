package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidJoy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityMaidJoyRender extends Render<EntityMaidJoy> {
    public static final EntityMaidJoyRender.Factory FACTORY = new EntityMaidJoyRender.Factory();

    public EntityMaidJoyRender(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMaidJoy entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMaidJoy entity) {
        return null;
    }

    public static class Factory implements IRenderFactory<EntityMaidJoy> {
        @Override
        public Render<? super EntityMaidJoy> createRenderFor(RenderManager manager) {
            return new EntityMaidJoyRender(manager);
        }
    }
}
