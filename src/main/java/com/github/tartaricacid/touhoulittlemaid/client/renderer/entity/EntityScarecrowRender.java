package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.ScarecrowModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.ZunModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerScarecrowAnnex;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 */
public class EntityScarecrowRender extends RenderLivingBase<EntityScarecrow> {
    public static final EntityScarecrowRender.Factory FACTORY = new EntityScarecrowRender.Factory();
    private static ModelBase defaultModel;
    private static ModelBase zunModel;
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/scarecrow.png");
    private static final ResourceLocation ZUN_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/zun.png");

    public EntityScarecrowRender(RenderManager renderManagerIn) {
        super(renderManagerIn, new ScarecrowModel(), 0.5f);
        zunModel = new ZunModel();
        defaultModel = this.mainModel;
        this.addLayer(new LayerScarecrowAnnex());
    }

    @Override
    public void doRender(@Nonnull EntityScarecrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.isSpecial()) {
            this.mainModel = zunModel;
        } else {
            this.mainModel = defaultModel;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected boolean canRenderName(@Nonnull EntityScarecrow entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityScarecrow entity) {
        if (entity.isSpecial()) {
            return ZUN_TEXTURE;
        }
        return DEFAULT_TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityScarecrow> {
        @Override
        public Render<? super EntityScarecrow> createRenderFor(RenderManager manager) {
            return new EntityScarecrowRender(manager);
        }
    }
}
