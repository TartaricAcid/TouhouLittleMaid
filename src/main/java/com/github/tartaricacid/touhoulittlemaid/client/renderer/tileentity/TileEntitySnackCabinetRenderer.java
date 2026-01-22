package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockSnackCabinet;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntitySnackCabinet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySnackCabinetRenderer implements BlockEntityRenderer<TileEntitySnackCabinet> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/bedrock/block/snack_cabinet.png");
    private final SimpleBedrockModel<Entity> model;
    private final BedrockPart full;
    private final BedrockPart half;

    public TileEntitySnackCabinetRenderer(BlockEntityRendererProvider.Context context) {
        this.model = BedrockModelLoader.getModel(BedrockModelLoader.SNACK_CABINET);
        if (this.model == null) {
            throw new IllegalStateException("Snack Cabinet model is null!");
        }
        this.full = this.model.getPart("full");
        this.half = this.model.getPart("half");
    }

    @Override
    public void render(TileEntitySnackCabinet cabinet, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockState = cabinet.getBlockState();
        Direction facing = blockState.getValue(BlockSnackCabinet.FACING);
        int type = blockState.getValue(BlockSnackCabinet.TYPE);

        if (type == BlockSnackCabinet.TYPE_FULL) {
            this.full.visible = true;
            this.half.visible = false;
        } else if (type == BlockSnackCabinet.TYPE_HALF) {
            this.full.visible = false;
            this.half.visible = true;
        } else {
            this.full.visible = false;
            this.half.visible = false;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facing.get2DDataValue() * 90));
        VertexConsumer consumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, combinedLightIn, combinedOverlayIn,
                1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
