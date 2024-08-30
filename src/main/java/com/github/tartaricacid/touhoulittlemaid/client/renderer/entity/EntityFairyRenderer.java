package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EntityFairyRenderer extends MobRenderer<EntityFairy, EntityFairyModel> {
    private static final ResourceLocation TEXTURE_0 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_0.png");
    private static final ResourceLocation TEXTURE_1 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_1.png");
    private static final ResourceLocation TEXTURE_2 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_2.png");
    private static final ResourceLocation TEXTURE_3 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_3.png");
    private static final ResourceLocation TEXTURE_4 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_4.png");
    private static final ResourceLocation TEXTURE_5 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_5.png");
    private static final ResourceLocation TEXTURE_6 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_6.png");
    private static final ResourceLocation TEXTURE_7 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_7.png");
    private static final ResourceLocation TEXTURE_8 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_8.png");
    private static final ResourceLocation TEXTURE_9 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_9.png");
    private static final ResourceLocation TEXTURE_10 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_10.png");
    private static final ResourceLocation TEXTURE_11 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_11.png");
    private static final ResourceLocation TEXTURE_12 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_12.png");
    private static final ResourceLocation TEXTURE_13 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_13.png");
    private static final ResourceLocation TEXTURE_14 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_14.png");
    private static final ResourceLocation TEXTURE_15 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_15.png");
    private static final ResourceLocation TEXTURE_16 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_16.png");
    private static final ResourceLocation TEXTURE_17 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/maid_fairy/maid_fairy_17.png");

    private final NewEntityFairyRenderer newEntityFairyRenderer;

    public EntityFairyRenderer(EntityRendererProvider.Context context) {
        super(context, new EntityFairyModel(context.bakeLayer(EntityFairyModel.LAYER)), 0.5f);
        this.newEntityFairyRenderer = new NewEntityFairyRenderer(context);
    }

    @Override
    public void render(EntityFairy fairy, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (MiscConfig.USE_NEW_MAID_FAIRY_MODEL.get()) {
            newEntityFairyRenderer.render(fairy, entityYaw, partialTicks, poseStack, buffer, packedLight);
        } else {
            super.render(fairy, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    protected void setupRotations(EntityFairy fairy, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
        super.setupRotations(fairy, poseStack, ageInTicks, rotationYaw, partialTicks, scale);
        if (!fairy.onGround()) {
            poseStack.mulPose(Axis.XN.rotation(8 * (float) Math.PI / 180.0f));
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
