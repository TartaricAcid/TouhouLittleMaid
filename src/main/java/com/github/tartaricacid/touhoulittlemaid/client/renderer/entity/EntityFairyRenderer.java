package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EntityFairyRenderer extends MobRenderer<EntityFairy, EntityFairyModel> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_2.png");

    public EntityFairyRenderer(EntityRendererProvider.Context context) {
        super(context, new EntityFairyModel(context.bakeLayer(EntityFairyModel.LAYER)), 0.5f);
    }

    @Override
    protected void setupRotations(EntityFairy fairy, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(fairy, poseStack, ageInTicks, rotationYaw, partialTicks);
        if (!fairy.onGround()) {
            poseStack.mulPose(Axis.XN.rotation(8 * (float) Math.PI / 180.0f));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFairy entity) {
        switch (entity.getFairyTypeOrdinal()) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            default:
                return TEXTURE_0;
        }
    }
}
