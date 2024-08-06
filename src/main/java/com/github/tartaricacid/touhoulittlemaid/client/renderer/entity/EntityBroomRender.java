package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BroomModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class EntityBroomRender extends LivingEntityRenderer<EntityBroom, BroomModel> {
    private static final ResourceLocation BROOM_TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/broom.png");

    public EntityBroomRender(EntityRendererProvider.Context context) {
        super(context, new BroomModel(context.bakeLayer(BroomModel.LAYER)), 0.5f);
    }

    @Override
    protected boolean shouldShowName(EntityBroom entity) {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBroom entityBroom) {
        return BROOM_TEXTURE;
    }
}