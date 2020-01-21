package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntitySuitcaseModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySuitcase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2020/1/20 16:03
 **/
public class EntitySuitcaseRender extends RenderLivingBase<EntitySuitcase> {
    public static final EntitySuitcaseRender.Factory FACTORY = new EntitySuitcaseRender.Factory();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/suitcase.png");

    private EntitySuitcaseRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected boolean canRenderName(EntitySuitcase entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntitySuitcase entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntitySuitcase> {
        @Override
        public Render<? super EntitySuitcase> createRenderFor(RenderManager manager) {
            return new EntitySuitcaseRender(manager, new EntitySuitcaseModel(), 0.5f);
        }
    }
}
