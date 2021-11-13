package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRendererWithOffset extends ModelRenderer {
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    private float initRotX;
    private float initRotY;
    private float initRotZ;

    public ModelRendererWithOffset(Model model) {
        super(model);
    }

    public ModelRendererWithOffset(Model model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public ModelRendererWithOffset(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
        super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.visible) {
            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(this.offsetX, this.offsetY, this.offsetZ);
                this.translateAndRotate(matrixStackIn);
                this.compile(matrixStackIn.last(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                for (ModelRenderer modelrenderer : this.children) {
                    modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }
                matrixStackIn.popPose();
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
        this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    void setInitRotationAngle(float x, float y, float z) {
        this.initRotX = x;
        this.initRotY = y;
        this.initRotZ = z;
    }

    public float getInitRotX() {
        return initRotX;
    }

    public float getInitRotY() {
        return initRotY;
    }

    public float getInitRotZ() {
        return initRotZ;
    }
}
