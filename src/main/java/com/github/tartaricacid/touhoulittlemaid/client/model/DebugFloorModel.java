package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class DebugFloorModel extends EntityModel<Entity> {
    private final ModelRenderer floor;

    public DebugFloorModel() {
        texWidth = 64;
        texHeight = 32;
        floor = new ModelRenderer(this);
        floor.setPos(0.0F, -8.0F, 0.0F);
        setRotationAngle(floor, -3.1416F, 0.0F, 3.1416F);
        floor.texOffs(0, 0).addBox(-8.0F, 0.0F, -11.0F, 16.0F, 0.0F, 19.0F, 0.0F);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        floor.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}