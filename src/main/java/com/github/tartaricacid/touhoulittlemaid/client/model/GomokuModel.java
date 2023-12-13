package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GomokuModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer blackBox;
    private final ModelRenderer bone2;
    private final ModelRenderer whiteBox;
    private final ModelRenderer bone4;

    public GomokuModel() {
        texWidth = 512;
        texHeight = 512;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 24.0F, 0.0F);
        main.texOffs(0, 0).addBox(-75.5F, -61.51F, -75.5F, 151.0F, 151.0F, 151.0F, -59.5F, false);
        main.texOffs(0, 0).addBox(-16.0F, -2.0F, -16.0F, 32.0F, 2.0F, 32.0F, 0.0F, false);

        blackBox = new ModelRenderer(this);
        blackBox.setPos(0.0F, 24.0F, 0.0F);
        blackBox.texOffs(69, 11).addBox(-22.0F, -1.0F, 8.0F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        blackBox.texOffs(12, 99).addBox(-21.5F, -2.95F, 8.5F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        blackBox.texOffs(12, 99).addBox(-20.25F, -3.85F, 8.9F, 2.0F, 1.0F, 2.0F, -0.25F, false);
        blackBox.texOffs(12, 99).addBox(-21.0F, -3.85F, 10.4F, 2.0F, 1.0F, 2.0F, -0.25F, false);
        blackBox.texOffs(69, 11).addBox(-18.0F, -4.0F, 8.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        blackBox.texOffs(69, 11).addBox(-22.0F, -4.0F, 9.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        blackBox.texOffs(69, 11).addBox(-21.0F, -4.0F, 12.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
        blackBox.texOffs(69, 11).addBox(-22.0F, -4.0F, 8.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-18.5F, -4.15F, 9.9F);
        blackBox.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.3927F);
        bone2.texOffs(12, 99).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, -0.25F, false);

        whiteBox = new ModelRenderer(this);
        whiteBox.setPos(0.0F, 24.0F, 0.0F);
        whiteBox.texOffs(69, 11).addBox(17.0F, -1.0F, -13.0F, 5.0F, 1.0F, 5.0F, 0.0F, true);
        whiteBox.texOffs(44, 96).addBox(17.5F, -2.95F, -12.5F, 4.0F, 1.0F, 4.0F, 0.0F, true);
        whiteBox.texOffs(44, 96).addBox(18.25F, -3.85F, -10.9F, 2.0F, 1.0F, 2.0F, -0.25F, true);
        whiteBox.texOffs(44, 96).addBox(19.0F, -3.85F, -12.4F, 2.0F, 1.0F, 2.0F, -0.25F, true);
        whiteBox.texOffs(69, 11).addBox(17.0F, -4.0F, -12.0F, 1.0F, 3.0F, 4.0F, 0.0F, true);
        whiteBox.texOffs(69, 11).addBox(21.0F, -4.0F, -13.0F, 1.0F, 3.0F, 4.0F, 0.0F, true);
        whiteBox.texOffs(69, 11).addBox(17.0F, -4.0F, -13.0F, 4.0F, 3.0F, 1.0F, 0.0F, true);
        whiteBox.texOffs(69, 11).addBox(18.0F, -4.0F, -9.0F, 4.0F, 3.0F, 1.0F, 0.0F, true);

        bone4 = new ModelRenderer(this);
        bone4.setPos(18.5F, -4.15F, -9.9F);
        whiteBox.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.3927F);
        bone4.texOffs(44, 96).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, -0.25F, true);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        blackBox.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        whiteBox.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
