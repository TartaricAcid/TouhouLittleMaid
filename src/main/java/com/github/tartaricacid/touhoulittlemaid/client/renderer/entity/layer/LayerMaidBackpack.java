package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackBigModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackMiddleModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackSmallModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerMaidBackpack extends RenderLayer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final ResourceLocation SMALL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_small.png");
    private static final ResourceLocation MIDDLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_middle.png");
    private static final ResourceLocation BIG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    private final EntityMaidRenderer renderer;
    private final EntityModel<EntityMaid> smallModel;
    private final EntityModel<EntityMaid> middleModel;
    private final EntityModel<EntityMaid> bigModel;

    public LayerMaidBackpack(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        smallModel = new MaidBackpackSmallModel(modelSet.bakeLayer(MaidBackpackSmallModel.LAYER));
        middleModel = new MaidBackpackMiddleModel(modelSet.bakeLayer(MaidBackpackMiddleModel.LAYER));
        bigModel = new MaidBackpackBigModel(modelSet.bakeLayer(MaidBackpackBigModel.LAYER));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!renderer.getMainInfo().isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
            return;
        }

        // 稍微缩放，避免整数倍的 z-flight
        poseStack.scale(1.01f, 1.01f, 1.01f);
        // [-13, 41, 15]
        if (getParentModel().hasBackpackPositioningModel()) {
            BedrockPart renderer = getParentModel().getBackpackPositioningModel();
            poseStack.translate(renderer.x * 0.0625, 0.0625 * (renderer.y - 23 + 8), 0.0625 * (renderer.z + 4));
        } else {
            poseStack.translate(0, -0.5, 0.25);
        }

        switch (maid.getBackpackLevel()) {
            case BackpackLevel.SMALL:
                renderColoredCutoutModel(smallModel, SMALL, poseStack, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                return;
            case BackpackLevel.MIDDLE:
                renderColoredCutoutModel(middleModel, MIDDLE, poseStack, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                return;
            case BackpackLevel.BIG:
                renderColoredCutoutModel(bigModel, BIG, poseStack, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
            case BackpackLevel.EMPTY:
            default:
        }
    }
}
