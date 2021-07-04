package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class EntityFairyRenderer extends MobRenderer<EntityFairy, EntityFairyModel> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy_2.png");

    public EntityFairyRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new EntityFairyModel(), 0.5f);
    }

    @Override
    protected void setupRotations(EntityFairy fairy, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(fairy, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        if (!fairy.isOnGround()) {
            matrixStackIn.mulPose(Vector3f.XN.rotation(8 * (float) Math.PI / 180.0f));
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
