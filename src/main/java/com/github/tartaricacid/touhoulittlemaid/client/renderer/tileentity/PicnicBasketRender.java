package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityPlaceholderModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.PicnicBasketModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PicnicBasketRender extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/picnic_basket.png");
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/item/picnic_basket.png");
    private static final EntityPlaceholderModel GUI_MODEL = new EntityPlaceholderModel();
    private final PicnicBasketModel model;

    public PicnicBasketRender(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new PicnicBasketModel(modelSet.bakeLayer(PicnicBasketModel.LAYER));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (displayContext == ItemTransforms.TransformType.GUI) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            GUI_MODEL.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(GUI_TEXTURE)), packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            RenderType renderType = RenderType.entityCutoutNoCull(TEXTURE);
            model.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, packedOverlay, 1, 1, 1, 1);
            poseStack.popPose();
        }
    }
}
