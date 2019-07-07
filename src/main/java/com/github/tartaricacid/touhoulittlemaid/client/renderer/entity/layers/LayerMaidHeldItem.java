package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityMaidModel;
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

@SideOnly(Side.CLIENT)
public class LayerMaidHeldItem implements LayerRenderer<EntityMaid> {
    protected final RenderLiving<EntityMaid> livingEntityRenderer;

    public LayerMaidHeldItem(RenderLiving<EntityMaid> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    @Override
    public void doRenderLayer(EntityMaid entityMaid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack mainRightItem = entityMaid.getHeldItemMainhand();
        ItemStack offLeftItem = entityMaid.getHeldItemOffhand();

        if (!mainRightItem.isEmpty() || !offLeftItem.isEmpty()) {
            GlStateManager.pushMatrix();
            this.renderHeldItem(entityMaid, mainRightItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(entityMaid, offLeftItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(EntityMaid entityMaid, ItemStack itemStack, ItemCameraTransforms.TransformType type, EnumHandSide handSide) {
        if (!itemStack.isEmpty()) {
            GlStateManager.pushMatrix();
            if (entityMaid.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            ((EntityMaidModel) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.525F);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entityMaid, itemStack, type, flag);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
