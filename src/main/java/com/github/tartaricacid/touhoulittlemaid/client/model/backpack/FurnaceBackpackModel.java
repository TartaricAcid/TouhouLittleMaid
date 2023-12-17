package com.github.tartaricacid.touhoulittlemaid.client.model.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class FurnaceBackpackModel extends EntityModel<EntityMaid> {
    private final ModelRenderer main;
    private final ModelRenderer bone466;
    private final ModelRenderer bone36;
    private final ModelRenderer bone10;
    private final ModelRenderer bone26;
    private final ModelRenderer bone6;
    private final ModelRenderer bone8;
    private final ModelRenderer bone7;
    private final ModelRenderer bone9;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone467;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bench;
    private final ModelRenderer bench2;

    public FurnaceBackpackModel() {
        texWidth = 128;
        texHeight = 128;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 33.5F, 1.0F);


        bone466 = new ModelRenderer(this);
        bone466.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(bone466);
        bone466.texOffs(0, 80).addBox(-5.0F, -25.0F, -2.0F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        bone466.texOffs(75, 65).addBox(4.0F, -25.0F, -2.0F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        bone466.texOffs(68, 41).addBox(-6.0F, -22.0F, -2.0F, 12.0F, 1.0F, 1.0F, -0.1F, false);

        bone36 = new ModelRenderer(this);
        bone36.setPos(0.0F, -28.5F, -1.5F);
        bone466.addChild(bone36);
        bone36.texOffs(68, 35).addBox(-6.0F, -8.5F, -0.5F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone36.texOffs(68, 32).addBox(-6.0F, -6.5F, -0.5F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone36.texOffs(72, 0).addBox(-5.0F, -10.5F, -0.5F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        bone36.texOffs(70, 65).addBox(4.0F, -10.5F, -0.5F, 1.0F, 14.0F, 1.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, 0.0F, 0.0F);
        bone36.addChild(bone10);
        setRotationAngle(bone10, -0.3927F, 0.0F, 0.0F);
        bone10.texOffs(68, 47).addBox(-5.5F, -6.9672F, 12.3212F, 11.0F, 1.0F, 1.0F, -0.1F, false);
        bone10.texOffs(35, 33).addBox(-5.0F, -6.9672F, -16.1788F, 1.0F, 1.0F, 30.0F, -0.1F, false);
        bone10.texOffs(0, 0).addBox(-4.5F, -6.9672F, -15.1788F, 9.0F, 1.0F, 28.0F, -0.3F, false);
        bone10.texOffs(49, 0).addBox(4.0F, -6.9672F, -16.1788F, 1.0F, 1.0F, 30.0F, -0.1F, false);
        bone10.texOffs(68, 50).addBox(-5.5F, -6.9672F, -15.6788F, 11.0F, 1.0F, 1.0F, -0.1F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(-4.0F, -19.1517F, -3.8731F);
        bone466.addChild(bone26);


        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        bone26.addChild(bone6);
        bone6.texOffs(86, 15).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.025F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        bone26.addChild(bone8);
        bone8.texOffs(89, 18).addBox(0.0F, -1.0F, -3.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.5F, 5.5F, 2.0F);
        bone26.addChild(bone7);
        setRotationAngle(bone7, -0.2443F, 0.0F, 0.0F);
        bone7.texOffs(85, 14).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, 0.025F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(4.0F, -19.1517F, -3.8731F);
        bone466.addChild(bone9);


        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone11);
        bone11.texOffs(86, 15).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.025F, true);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone12);
        bone12.texOffs(89, 18).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 6.0F, 1.0F, 0.0F, true);

        bone13 = new ModelRenderer(this);
        bone13.setPos(-0.5F, 5.5F, 2.0F);
        bone9.addChild(bone13);
        setRotationAngle(bone13, -0.2443F, 0.0F, 0.0F);
        bone13.texOffs(85, 14).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, 0.025F, true);

        bone = new ModelRenderer(this);
        bone.setPos(-3.5F, -19.5F, -1.5F);
        bone466.addChild(bone);
        setRotationAngle(bone, 0.7854F, 0.0F, 0.0F);
        bone.texOffs(88, 15).addBox(6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.15F, false);
        bone.texOffs(88, 15).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.15F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-3.5F, -13.5F, -1.5F);
        bone466.addChild(bone2);
        setRotationAngle(bone2, 0.7854F, 0.0F, 0.0F);
        bone2.texOffs(88, 15).addBox(6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.15F, false);
        bone2.texOffs(88, 15).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.15F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, -12.3139F, 1.237F);
        bone466.addChild(bone3);
        bone3.texOffs(3, 68).addBox(-5.5F, -2.3361F, -2.487F, 11.0F, 1.0F, 10.0F, -0.25F, false);
        bone3.texOffs(68, 44).addBox(-6.0F, -1.6861F, -3.237F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone3.texOffs(68, 38).addBox(-6.0F, -1.6861F, 5.263F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone3.texOffs(37, 68).addBox(-5.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, -0.1F, false);
        bone3.texOffs(55, 70).addBox(4.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, -0.1F, false);

        bone467 = new ModelRenderer(this);
        bone467.setPos(0.0F, 0.0F, 0.0F);
        bone3.addChild(bone467);
        setRotationAngle(bone467, 0.3927F, 0.0F, 0.0F);
        bone467.texOffs(0, 30).addBox(4.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, -0.2F, false);
        bone467.texOffs(12, 14).addBox(-5.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, -0.2F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -18.3139F, 1.237F);
        bone466.addChild(bone4);
        bone4.texOffs(3, 68).addBox(-5.5F, -2.3361F, -2.487F, 11.0F, 1.0F, 10.0F, -0.25F, false);
        bone4.texOffs(68, 44).addBox(-6.0F, -1.6861F, -3.237F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone4.texOffs(68, 38).addBox(-6.0F, -1.6861F, 5.263F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone4.texOffs(37, 68).addBox(-5.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, -0.1F, false);
        bone4.texOffs(55, 70).addBox(4.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, -0.1F, false);
        bone4.texOffs(0, 30).addBox(-8.0F, -14.0861F, -5.737F, 16.0F, 16.0F, 16.0F, -4.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 0.0F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.3927F, 0.0F, 0.0F);
        bone5.texOffs(0, 30).addBox(4.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, -0.2F, false);
        bone5.texOffs(12, 14).addBox(-5.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, -0.2F, false);

        bench = new ModelRenderer(this);
        bench.setPos(0.0F, -16.4F, 3.75F);
        main.addChild(bench);
        setRotationAngle(bench, 0.0F, -1.5708F, 0.0F);
        bench.texOffs(0, 96).addBox(-10.0F, -8.0F, -10.05F, 16.0F, 16.0F, 16.0F, -6.0F, false);
        bench.texOffs(64, 96).addBox(-6.0F, -8.0F, -10.05F, 16.0F, 16.0F, 16.0F, -6.0F, false);

        bench2 = new ModelRenderer(this);
        bench2.setPos(0.0F, 0.0F, 2.0F);
        bench.addChild(bench2);
        setRotationAngle(bench2, -3.1416F, 0.0F, 3.1416F);
        bench2.texOffs(0, 96).addBox(-10.0F, -8.0F, -8.05F, 16.0F, 16.0F, 16.0F, -6.0F, false);
        bench2.texOffs(64, 96).addBox(-6.0F, -8.0F, -8.05F, 16.0F, 16.0F, 16.0F, -6.0F, false);
    }

    @Override
    public void setupAnim(EntityMaid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
}