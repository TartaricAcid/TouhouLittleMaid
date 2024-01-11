package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MaidBannerModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer banner;

    public MaidBannerModel() {
        texWidth = 64;
        texHeight = 64;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 24.0F, 0.0F);
        main.texOffs(34, 49).addBox(-1.0F, -68.0F, 10.0F, 2.0F, 2.0F, 13.0F, 0.1F, false);
        main.texOffs(34, 49).addBox(-1.0F, -68.0F, -3.0F, 2.0F, 2.0F, 13.0F, 0.1F, false);
        main.texOffs(56, 0).addBox(-1.0F, -35.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.05F, false);
        main.texOffs(56, 0).addBox(-1.0F, -70.0F, -1.0F, 2.0F, 35.0F, 2.0F, 0.05F, false);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(bone);
        bone.texOffs(56, 0).addBox(-1.0F, -26.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone.texOffs(56, 0).addBox(-1.0F, -46.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone.texOffs(56, 0).addBox(-1.0F, -36.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone.texOffs(56, 0).addBox(-1.0F, -56.0F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(bone2);
        bone2.texOffs(56, 0).addBox(-1.0F, -66.0F, 6.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone2.texOffs(56, 0).addBox(-1.0F, -66.0F, 16.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        banner = new ModelRenderer(this);
        banner.setPos(0.0F, 24.0F, 0.0F);
        setRotationAngle(banner, 0.0F, -1.5708F, 0.0F);
        banner.texOffs(0, 0).addBox(2.0F, -65.0F, -0.5F, 20.0F, 40.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public ModelRenderer getBanner() {
        return banner;
    }
}
