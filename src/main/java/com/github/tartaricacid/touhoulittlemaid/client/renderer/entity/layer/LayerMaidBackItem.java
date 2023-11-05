package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class LayerMaidBackItem extends LayerRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private final EntityMaidRenderer renderer;

    public LayerMaidBackItem(EntityMaidRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = maid.getMaidInv().getStackInSlot(5);
        if (stack.getItem() instanceof IVanishable) {
            if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                return;
            }
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0.5, -0.25);
            switch (maid.getBackpackLevel()) {
                default:
                case BackpackLevel.EMPTY:
                    matrixStack.translate(0, 0.625, 0.2);
                    break;
                case BackpackLevel.SMALL:
                    matrixStack.translate(0, 0.625, -0.05);
                    break;
                case BackpackLevel.MIDDLE:
                    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-7.5F));
                    matrixStack.translate(0, 0.625, -0.25);
                    break;
                case BackpackLevel.BIG:
                    matrixStack.translate(0, 0, -0.4);
                    break;
            }
            Minecraft.getInstance().getItemInHandRenderer().renderItem(maid, stack, ItemCameraTransforms.TransformType.FIXED, false, matrixStack, bufferIn, packedLightIn);
            matrixStack.popPose();
        }
    }
}
