package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;

public class LayerMaidBackpack extends LayerRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private final EntityMaidRenderer renderer;

    public LayerMaidBackpack(EntityMaidRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
        BackpackManager.initClient();
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
        IMaidBackpack backpack = maid.getMaidBackpackType();
        BackpackManager.findBackpackModel(backpack.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f));
    }
}
