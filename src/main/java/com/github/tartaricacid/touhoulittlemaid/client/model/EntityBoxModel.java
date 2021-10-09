package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class EntityBoxModel extends EntityModel<EntityBox> {
    private final ModelRenderer bottom;
    private final ModelRenderer x1;
    private final ModelRenderer x2;
    private final ModelRenderer z1;
    private final ModelRenderer z2;
    private final ModelRenderer top;

    public EntityBoxModel() {
        texWidth = 256;
        texHeight = 256;

        bottom = new ModelRenderer(this);
        bottom.setPos(0.0F, 24.0F, 0.0F);
        bottom.texOffs(0, 0).addBox(-15.0F, -1.0F, -15.0F, 30.0F, 1.0F, 30.0F, 0.0F, false);

        x1 = new ModelRenderer(this);
        x1.setPos(0.0F, 23.5F, -14.5F);
        x1.texOffs(64, 31).addBox(-14.0F, -30.5F, -0.5F, 28.0F, 30.0F, 1.0F, 0.0F, false);

        x2 = new ModelRenderer(this);
        x2.setPos(0.0F, 23.5F, 14.5F);
        x2.texOffs(64, 31).addBox(-14.0F, -30.5F, -0.5F, 28.0F, 30.0F, 1.0F, 0.0F, false);

        z1 = new ModelRenderer(this);
        z1.setPos(14.5F, 23.5F, 0.0F);
        z1.texOffs(0, 31).addBox(-0.5F, -30.5F, -15.0F, 1.0F, 30.0F, 30.0F, 0.0F, false);

        z2 = new ModelRenderer(this);
        z2.setPos(-14.5F, 23.5F, 0.0F);
        z2.texOffs(0, 31).addBox(-0.5F, -30.5F, -15.0F, 1.0F, 30.0F, 30.0F, 0.0F, false);

        top = new ModelRenderer(this);
        top.setPos(0.0F, 24.0F, 0.0F);
        top.texOffs(0, 0).addBox(-15.0F, -32.0F, -15.0F, 30.0F, 1.0F, 30.0F, 0.0F, false);
        top.texOffs(64, 64).addBox(-16.0F, -32.0F, -16.0F, 32.0F, 6.0F, 1.0F, 0.0F, false);
        top.texOffs(64, 64).addBox(-16.0F, -32.0F, 15.0F, 32.0F, 6.0F, 1.0F, 0.0F, false);
        top.texOffs(32, 61).addBox(-16.0F, -32.0F, -15.0F, 1.0F, 6.0F, 30.0F, 0.0F, false);
        top.texOffs(32, 61).addBox(15.0F, -32.0F, -15.0F, 1.0F, 6.0F, 30.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(EntityBox entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        int stage = entityIn.getOpenStage();
        if (stage == EntityBox.FIRST_STAGE) {
            top.visible = true;
            x1.xRot = 0;
            x2.xRot = 0;
            z1.zRot = 0;
            z2.zRot = 0;
        } else if (stage > EntityBox.SECOND_STAGE) {
            top.visible = false;
            x1.xRot = 0;
            x2.xRot = 0;
            z1.zRot = 0;
            z2.zRot = 0;
        } else {
            top.visible = false;
            x1.xRot = 0.023998277f * (EntityBox.SECOND_STAGE - stage);
            x2.xRot = -0.023998277f * (EntityBox.SECOND_STAGE - stage);
            z1.zRot = 0.023998277f * (EntityBox.SECOND_STAGE - stage);
            z2.zRot = -0.023998277f * (EntityBox.SECOND_STAGE - stage);
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bottom.render(matrixStack, buffer, packedLight, packedOverlay);
        x1.render(matrixStack, buffer, packedLight, packedOverlay);
        x2.render(matrixStack, buffer, packedLight, packedOverlay);
        z1.render(matrixStack, buffer, packedLight, packedOverlay);
        z2.render(matrixStack, buffer, packedLight, packedOverlay);
        top.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
