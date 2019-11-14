package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BoxModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/11/14 15:11
 **/
public class EntityBoxRender extends Render<EntityBox> {
    public static final EntityBoxRender.Factory FACTORY = new EntityBoxRender.Factory();
    protected ModelBase mainModel;

    private EntityBoxRender(RenderManager renderManager, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManager);
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
    }

    @Override
    @SuppressWarnings("all")
    public void doRender(@Nonnull EntityBox entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 1.6, z);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.disableCull();
        this.mainModel.setRotationAngles(0, 0, 0, 0, 0, 0.0625f, entity);
        Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(entity));
        this.mainModel.render(entity, 0, 0, 0, 0, 0, 0.0625f);
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBox entity) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, String.format("textures/entity/cake_box_%s.png", entity.getTextureIndex()));
    }

    public static class Factory implements IRenderFactory<EntityBox> {
        @Override
        public Render<? super EntityBox> createRenderFor(RenderManager manager) {
            return new EntityBoxRender(manager, new BoxModel(), 0.5f);
        }
    }
}
