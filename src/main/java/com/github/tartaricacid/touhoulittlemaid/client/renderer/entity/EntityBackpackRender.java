package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackBigModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackMiddleModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackSmallModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBackpack;
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
 * @date 2020/1/26 1:12
 **/
public class EntityBackpackRender extends Render<EntityBackpack> {
    public static final EntityBackpackRender.Factory FACTORY = new EntityBackpackRender.Factory();
    private static final ResourceLocation SMALL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_small.png");
    private static final ResourceLocation MIDDLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_middle.png");
    private static final ResourceLocation BIG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    private ModelBase smallModel = new MaidBackpackSmallModel();
    private ModelBase middleModel = new MaidBackpackMiddleModel();
    private ModelBase bigModel = new MaidBackpackBigModel();

    private EntityBackpackRender(RenderManager renderManager, float shadowSizeIn) {
        super(renderManager);
        this.shadowSize = shadowSizeIn;
    }

    @Override
    @SuppressWarnings("all")
    public void doRender(@Nonnull EntityBackpack entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        switch (entity.getBackpackLevel()) {
            default:
            case EMPTY:
                break;
            case SMALL:
                GlStateManager.translate(x, y + 1.5, z);
                GlStateManager.rotate(-entityYaw, 0, 1, 0);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                Minecraft.getMinecraft().renderEngine.bindTexture(SMALL);
                smallModel.render(entity, 0, 0, 0, 0, 0, 0.0625f);
                break;
            case MIDDLE:
                GlStateManager.translate(x, y + 1.6875, z);
                GlStateManager.rotate(-entityYaw, 0, 1, 0);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                Minecraft.getMinecraft().renderEngine.bindTexture(MIDDLE);
                middleModel.render(entity, 0, 0, 0, 0, 0, 0.0625f);
                break;
            case BIG:
                GlStateManager.translate(x, y + 1.5, z);
                GlStateManager.rotate(-entityYaw, 0, 1, 0);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                Minecraft.getMinecraft().renderEngine.bindTexture(BIG);
                bigModel.render(entity, 0, 0, 0, 0, 0, 0.0625f);
                break;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBackpack entity) {
        switch (entity.getBackpackLevel()) {
            case EMPTY:
            default:
            case SMALL:
                return SMALL;
            case MIDDLE:
                return MIDDLE;
            case BIG:
                return BIG;
        }
    }

    public static class Factory implements IRenderFactory<EntityBackpack> {
        @Override
        public Render<? super EntityBackpack> createRenderFor(RenderManager manager) {
            return new EntityBackpackRender(manager, 0.5f);
        }
    }
}
