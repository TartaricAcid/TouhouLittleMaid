package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.NewEntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NewEntityFairyRenderer extends MobRenderer<EntityFairy, NewEntityFairyModel> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_3.png");
    private static final ResourceLocation TEXTURE_4 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_4.png");
    private static final ResourceLocation TEXTURE_5 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_5.png");
    private static final ResourceLocation TEXTURE_6 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_6.png");
    private static final ResourceLocation TEXTURE_7 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_7.png");
    private static final ResourceLocation TEXTURE_8 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_8.png");
    private static final ResourceLocation TEXTURE_9 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_9.png");
    private static final ResourceLocation TEXTURE_10 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_10.png");
    private static final ResourceLocation TEXTURE_11 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_11.png");
    private static final ResourceLocation TEXTURE_12 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_12.png");
    private static final ResourceLocation TEXTURE_13 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_13.png");
    private static final ResourceLocation TEXTURE_14 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_14.png");
    private static final ResourceLocation TEXTURE_15 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_15.png");
    private static final ResourceLocation TEXTURE_16 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_16.png");
    private static final ResourceLocation TEXTURE_17 = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/new_maid_fairy/maid_fairy_17.png");

    public NewEntityFairyRenderer(EntityRendererProvider.Context context) {
        super(context, new NewEntityFairyModel(context.bakeLayer(NewEntityFairyModel.LAYER)), 0.5f);
    }

    @Override
    protected void setupRotations(EntityFairy fairy, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(fairy, poseStack, ageInTicks, rotationYaw, partialTicks);
        if (!fairy.isOnGround()) {
            poseStack.mulPose(Vector3f.XN.rotation(8 * (float) Math.PI / 180.0f));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFairy entity) {
        return switch (entity.getFairyTypeOrdinal()) {
            case 1 -> TEXTURE_1;
            case 2 -> TEXTURE_2;
            case 3 -> TEXTURE_3;
            case 4 -> TEXTURE_4;
            case 5 -> TEXTURE_5;
            case 6 -> TEXTURE_6;
            case 7 -> TEXTURE_7;
            case 8 -> TEXTURE_8;
            case 9 -> TEXTURE_9;
            case 10 -> TEXTURE_10;
            case 11 -> TEXTURE_11;
            case 12 -> TEXTURE_12;
            case 13 -> TEXTURE_13;
            case 14 -> TEXTURE_14;
            case 15 -> TEXTURE_15;
            case 16 -> TEXTURE_16;
            case 17 -> TEXTURE_17;
            default -> TEXTURE_0;
        };
    }
}
