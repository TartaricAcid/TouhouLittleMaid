package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class LayerMaidArrow extends LayerRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private final EntityRendererManager dispatcher;

    public LayerMaidArrow(EntityMaidRenderer maidRenderer) {
        super(maidRenderer);
        this.dispatcher = maidRenderer.getDispatcher();
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int i = maid.getArrowCount();
        Random random = new Random(maid.getId());
        if (i > 0) {
            for (int j = 0; j < i; ++j) {
                matrixStackIn.pushPose();
                ModelRenderer modelrenderer = this.getParentModel().getRandomModelPart(random);
                ModelRenderer.ModelBox randomCube = modelrenderer.getRandomCube(random);
                modelrenderer.translateAndRotate(matrixStackIn);
                float xRandom = random.nextFloat();
                float yRandom = random.nextFloat();
                float zRandom = random.nextFloat();
                float xPos = MathHelper.lerp(xRandom, randomCube.minX, randomCube.maxX) / 16.0F;
                float yPos = MathHelper.lerp(yRandom, randomCube.minY, randomCube.maxY) / 16.0F;
                float zPos = MathHelper.lerp(zRandom, randomCube.minZ, randomCube.maxZ) / 16.0F;
                matrixStackIn.translate(xPos, yPos, zPos);
                xRandom = -1.0F * (xRandom * 2.0F - 1.0F);
                yRandom = -1.0F * (yRandom * 2.0F - 1.0F);
                zRandom = -1.0F * (zRandom * 2.0F - 1.0F);
                this.renderStuckItem(matrixStackIn, bufferIn, packedLightIn, maid, xRandom, yRandom, zRandom, partialTicks);
                matrixStackIn.popPose();
            }
        }
    }

    protected void renderStuckItem(MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int packedLightIn, EntityMaid maid, float xRandom, float yRandom, float zRandom, float partialTicks) {
        float xzSqrt = MathHelper.sqrt(xRandom * xRandom + zRandom * zRandom);
        ArrowEntity arrow = new ArrowEntity(maid.level, maid.getX(), maid.getY(), maid.getZ());
        arrow.yRot = (float) (Math.atan2(xRandom, zRandom) * (double) (180F / (float) Math.PI));
        arrow.xRot = (float) (Math.atan2(yRandom, xzSqrt) * (double) (180F / (float) Math.PI));
        arrow.yRotO = arrow.yRot;
        arrow.xRotO = arrow.xRot;
        this.dispatcher.render(arrow, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStack, typeBuffer, packedLightIn);
    }
}
