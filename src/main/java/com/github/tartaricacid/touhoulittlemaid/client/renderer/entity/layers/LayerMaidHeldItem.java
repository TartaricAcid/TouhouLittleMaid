package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LayerMaidHeldItem implements LayerRenderer<EntityMaid> {
    protected final RenderLiving<EntityMaid> livingEntityRenderer;

    public LayerMaidHeldItem(RenderLiving<EntityMaid> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityMaid entityMaid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack mainRightItem = entityMaid.getHeldItemMainhand();
        ItemStack offLeftItem = entityMaid.getHeldItemOffhand();
        EntityModelJson mainModel = (EntityModelJson) this.livingEntityRenderer.getMainModel();
        if (!mainRightItem.isEmpty() && mainModel.hasRightArm()) {
            this.renderHeldItem(entityMaid, mainRightItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT, mainModel);
        }
        if (!offLeftItem.isEmpty() && mainModel.hasLeftArm()) {
            this.renderHeldItem(entityMaid, offLeftItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT, mainModel);
        }
    }

    private void renderHeldItem(EntityMaid entityMaid, ItemStack itemStack, ItemCameraTransforms.TransformType type, EnumHandSide handSide, EntityModelJson mainModel) {
        if (!itemStack.isEmpty()) {
            GlStateManager.pushMatrix();
            boolean isLeft = handSide == EnumHandSide.LEFT;
            mainModel.postRenderArm(0.0625F, handSide);
            if (mainModel.hasArmPositioningModel(handSide)) {
                mainModel.postRenderArmPositioningModel(0.0625F, handSide);
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0, 0.125f, -0.0625f);
            } else {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate((float) (isLeft ? -1 : 1) / 16.0F, 0.125F, -0.525F);
            }
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entityMaid, itemStack, type, isLeft);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
