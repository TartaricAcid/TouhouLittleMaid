package com.github.tartaricacid.touhoulittlemaid.client.render.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityMarisaBroomModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:59
 **/
public class EntityMarisaBroomRender extends RenderLiving<EntityMarisaBroom> {
    public static final EntityMarisaBroomRender.Factory FACTORY = new EntityMarisaBroomRender.Factory();
    private static ResourceLocation resourceLocation = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/marisa_broom.png");

    public EntityMarisaBroomRender(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityMarisaBroom entity) {
        return resourceLocation;
    }

    @Override
    protected void preRenderCallback(EntityMarisaBroom entity, float partialTickTime) {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
    }

    public static class Factory implements IRenderFactory<EntityMarisaBroom> {
        @Override
        public Render<? super EntityMarisaBroom> createRenderFor(RenderManager manager) {
            return new EntityMarisaBroomRender(manager, new EntityMarisaBroomModel(), 0.5f);
        }
    }
}
