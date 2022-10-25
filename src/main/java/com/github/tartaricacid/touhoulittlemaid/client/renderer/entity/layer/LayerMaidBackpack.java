package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackBigModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackMiddleModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackSmallModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerMaidBackpack extends LayerRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final ResourceLocation SMALL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_small.png");
    private static final ResourceLocation MIDDLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_middle.png");
    private static final ResourceLocation BIG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    private final EntityMaidRenderer renderer;
    private final EntityModel<EntityMaid> smallModel = new MaidBackpackSmallModel();
    private final EntityModel<EntityMaid> middleModel = new MaidBackpackMiddleModel();
    private final EntityModel<EntityMaid> bigModel = new MaidBackpackBigModel();

    public LayerMaidBackpack(EntityMaidRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
            return;
        }

        // 稍微缩放，避免整数倍的 z-flight
        matrixStackIn.scale(1.01f, 1.01f, 1.01f);
        // [-13, 41, 15]
        if (getParentModel().hasBackpackPositioningModel()) {
            ModelRenderer renderer = getParentModel().getBackpackPositioningModel();
            matrixStackIn.translate(renderer.x * 0.0625, 0.0625 * (renderer.y - 23 + 8), 0.0625 * (renderer.z + 4));
        } else {
            matrixStackIn.translate(0, -0.5, 0.25);
        }

        switch (maid.getBackpackLevel()) {
            case BackpackLevel.SMALL:
                renderColoredCutoutModel(smallModel, SMALL, matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                return;
            case BackpackLevel.MIDDLE:
                renderColoredCutoutModel(middleModel, MIDDLE, matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                return;
            case BackpackLevel.BIG:
                renderColoredCutoutModel(bigModel, BIG, matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
            case BackpackLevel.EMPTY:
            default:
        }
    }
}
