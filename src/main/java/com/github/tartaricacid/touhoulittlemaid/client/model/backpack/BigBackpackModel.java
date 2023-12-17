package com.github.tartaricacid.touhoulittlemaid.client.model.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BigBackpackModel extends EntityModel<EntityMaid> {
    private final ModelRenderer bone;

    public BigBackpackModel() {
        texWidth = 128;
        texHeight = 128;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 24.0F, 0.0F);
        bone.texOffs(88, 45).addBox(-4.95F, -15.0F, 0.0F, 10.0F, 5.0F, 10.0F, 0.0F, true);
        bone.texOffs(118, 62).addBox(-5.95F, -14.0F, 3.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        bone.texOffs(110, 94).addBox(-7.0F, -4.5F, 2.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);
        bone.texOffs(110, 94).addBox(5.0F, -4.5F, 2.0F, 2.0F, 3.0F, 6.0F, 0.0F, true);
        bone.texOffs(118, 62).addBox(5.05F, -14.0F, 3.0F, 1.0F, 3.0F, 4.0F, 0.0F, true);
        bone.texOffs(88, 15).addBox(-5.0F, -23.0F, 0.05F, 10.0F, 4.0F, 10.0F, 0.0F, false);
        bone.texOffs(88, 30).addBox(-5.05F, -19.0F, 0.0F, 10.0F, 4.0F, 10.0F, 0.0F, true);
        bone.texOffs(88, 0).addBox(-5.0F, -26.0F, 0.0F, 10.0F, 3.0F, 7.0F, 0.0F, false);
        bone.texOffs(57, 0).addBox(-5.0F, -10.0F, 0.0F, 10.0F, 10.0F, 5.0F, 0.0F, true);
        bone.texOffs(0, 0).addBox(-3.5F, -4.0F, -5.25F, 7.0F, 1.0F, 6.0F, 0.0F, false);
        bone.texOffs(31, 0).addBox(2.5F, -10.0F, -5.0F, 1.0F, 8.0F, 5.0F, 0.0F, false);
        bone.texOffs(44, 0).addBox(-3.5F, -10.0F, -5.0F, 1.0F, 8.0F, 5.0F, 0.0F, true);
        bone.texOffs(76, 31).addBox(-2.0F, -9.5F, 10.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        bone.texOffs(64, 32).addBox(-2.0F, -17.5F, 9.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(76, 39).addBox(-1.5F, -2.25F, 10.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        bone.texOffs(57, 15).addBox(-5.0F, -10.0F, 5.0F, 10.0F, 10.0F, 5.0F, 0.0F, false);
        bone.texOffs(0, 63).addBox(2.5F, -26.0F, 0.0F, 1.0F, 26.0F, 10.0F, 0.1F, false);
        bone.texOffs(0, 63).addBox(-3.5F, -26.0F, 0.0F, 1.0F, 26.0F, 10.0F, 0.1F, false);
        bone.texOffs(51, 121).addBox(-3.5F, -25.2F, 7.2F, 1.0F, 2.0F, 2.0F, 0.1F, false);
        bone.texOffs(51, 121).addBox(2.5F, -25.2F, 7.2F, 1.0F, 2.0F, 2.0F, 0.1F, true);
        bone.texOffs(23, 104).addBox(-5.0F, -23.0F, 7.5F, 10.0F, 23.0F, 1.0F, 0.1F, false);
        bone.texOffs(0, 101).addBox(-5.0F, -26.0F, 1.5F, 10.0F, 26.0F, 1.0F, 0.1F, false);
        bone.texOffs(88, 113).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 5.0F, 10.0F, 0.15F, false);
    }

    @Override
    public void setupAnim(EntityMaid entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }
}
