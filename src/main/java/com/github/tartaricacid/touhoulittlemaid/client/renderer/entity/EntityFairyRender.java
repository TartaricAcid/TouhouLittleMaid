package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:59
 **/
@SideOnly(Side.CLIENT)
public class EntityFairyRender extends RenderLiving<EntityFairy> {
    public static final EntityFairyRender.Factory FACTORY = new EntityFairyRender.Factory();
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_2.png");

    private EntityFairyRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    public void doRender(@Nonnull EntityFairy entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityFairy entity) {
        switch (entity.getFairyTypeOrdinal()) {
            case 0:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            default:
                return TEXTURE_0;
        }
    }

    public static class Factory implements IRenderFactory<EntityFairy> {
        @Override
        public Render<? super EntityFairy> createRenderFor(RenderManager manager) {
            return new EntityFairyRender(manager, new EntityFairyModel(), 0.5f);
        }
    }
}
