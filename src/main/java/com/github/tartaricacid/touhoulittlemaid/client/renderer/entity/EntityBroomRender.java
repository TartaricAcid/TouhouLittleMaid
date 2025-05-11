package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BroomModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader.BROOM;

public class EntityBroomRender extends LivingEntityRenderer<EntityBroom, BroomModel> {
    private static final ResourceLocation BROOM_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/bedrock/entity/broom.png");

    public EntityBroomRender(EntityRendererProvider.Context context) {
        super(context, (BroomModel) BedrockModelLoader.<EntityBroom>getModel(BROOM), 0.5f);
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