package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.client.model.ShrineModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityShrine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TileEntityShrineRenderer implements BlockEntityRenderer<TileEntityShrine> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/shrine.png");
    private final ShrineModel model;

    public TileEntityShrineRenderer(BlockEntityRendererProvider.Context context) {
        model = new ShrineModel(context.bakeLayer(ShrineModel.LAYER));
    }

    @Override
    public void render(TileEntityShrine shrine, float partialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction facing = shrine.getBlockState().getValue(BlockGomoku.FACING);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180 - facing.get2DDataValue() * 90));
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
        model.renderToBuffer(poseStack, buffer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        Level level = shrine.getLevel();
        if (level == null) {
            return;
        }
        ItemStack stack = shrine.getStorageItem();
        poseStack.pushPose();
        poseStack.translate(0.5, 0.85, 0.5);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        float deg = (level.getGameTime() + partialTick) % 360;
        poseStack.mulPose(Vector3f.YN.rotationDegrees(deg));
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, poseStack, bufferIn, 0);
        poseStack.popPose();
    }
}
