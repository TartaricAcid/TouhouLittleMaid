package com.github.tartaricacid.touhoulittlemaid.client.model.backpack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TankBackpackModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer bone466;
    private final ModelRenderer bone36;
    private final ModelRenderer bone10;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone467;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone8;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone9;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bench;
    private final ModelRenderer bench2;

    public TankBackpackModel() {
        texWidth = 128;
        texHeight = 128;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 34.0F, 1.0F);


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

        bone22 = new ModelRenderer(this);
        bone22.setPos(4.0F, -18.9017F, -3.8731F);
        bone466.addChild(bone22);


        bone23 = new ModelRenderer(this);
        bone23.setPos(0.0F, 0.0F, 0.0F);
        bone22.addChild(bone23);
        bone23.texOffs(86, 15).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.025F, true);

        bone30 = new ModelRenderer(this);
        bone30.setPos(0.0F, 0.0F, 0.0F);
        bone22.addChild(bone30);
        bone30.texOffs(89, 18).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 6.0F, 1.0F, 0.0F, true);

        bone31 = new ModelRenderer(this);
        bone31.setPos(-0.5F, 5.5F, 2.0F);
        bone22.addChild(bone31);
        setRotationAngle(bone31, -0.2443F, 0.0F, 0.0F);
        bone31.texOffs(85, 14).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, 0.025F, true);

        bone24 = new ModelRenderer(this);
        bone24.setPos(-4.0F, -18.9017F, -3.8731F);
        bone466.addChild(bone24);


        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, 0.0F, 0.0F);
        bone24.addChild(bone25);
        bone25.texOffs(86, 15).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.025F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(0.0F, 0.0F, 0.0F);
        bone24.addChild(bone26);
        bone26.texOffs(89, 18).addBox(0.0F, -1.0F, -3.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(0.5F, 5.5F, 2.0F);
        bone24.addChild(bone27);
        setRotationAngle(bone27, -0.2443F, 0.0F, 0.0F);
        bone27.texOffs(85, 14).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, 0.025F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -18.3139F, 1.237F);
        bone466.addChild(bone4);
        bone4.texOffs(2, 67).addBox(-5.5F, -2.3361F, -2.487F, 11.0F, 1.0F, 11.0F, -0.25F, false);
        bone4.texOffs(68, 44).addBox(-6.0F, -1.6861F, -3.237F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone4.texOffs(68, 38).addBox(-6.0F, -1.6861F, 7.263F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone4.texOffs(36, 67).addBox(-5.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 13.0F, -0.1F, false);
        bone4.texOffs(54, 69).addBox(4.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 13.0F, -0.1F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 0.0F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.3927F, 0.0F, 0.0F);
        bone5.texOffs(0, 30).addBox(4.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, -0.2F, false);
        bone5.texOffs(12, 14).addBox(-5.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, -0.2F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, -7.2F, 3.0F);
        bone4.addChild(bone8);


        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        bone8.addChild(bone6);
        bone6.texOffs(102, 70).addBox(-4.5F, -2.5F, -2.0F, 9.0F, 8.0F, 4.0F, -0.23F, false);
        bone6.texOffs(94, 91).addBox(-5.0F, -2.2F, -1.5F, 10.0F, 1.0F, 3.0F, -0.23F, false);
        bone6.texOffs(102, 70).addBox(-2.0F, -2.5F, -4.5F, 4.0F, 8.0F, 9.0F, -0.23F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        bone8.addChild(bone7);
        setRotationAngle(bone7, 0.0F, -0.7854F, 0.0F);
        bone7.texOffs(102, 70).addBox(-4.5F, -2.5F, -2.0F, 9.0F, 8.0F, 4.0F, -0.23F, false);
        bone7.texOffs(102, 70).addBox(-2.0F, -2.5F, -4.5F, 4.0F, 8.0F, 9.0F, -0.23F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, -7.0F, 3.0F);
        bone4.addChild(bone19);


        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, 0.0F, 0.0F);
        bone19.addChild(bone20);
        bone20.texOffs(51, 87).addBox(-4.5F, 4.5F, -1.9F, 9.0F, 1.0F, 3.8F, -0.03F, false);
        bone20.texOffs(51, 87).addBox(-1.9F, 4.5F, -4.5F, 3.8F, 1.0F, 9.0F, -0.03F, false);

        bone21 = new ModelRenderer(this);
        bone21.setPos(0.0F, 0.0F, 0.0F);
        bone19.addChild(bone21);
        setRotationAngle(bone21, 0.0F, -0.7854F, 0.0F);
        bone21.texOffs(51, 87).addBox(-4.5F, 4.5F, -1.9F, 9.0F, 1.0F, 3.8F, -0.03F, false);
        bone21.texOffs(51, 87).addBox(-1.9F, 4.5F, -4.5F, 3.8F, 1.0F, 9.0F, -0.03F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, -9.5F, 3.0F);
        bone4.addChild(bone16);


        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, 0.0F, 0.0F);
        bone16.addChild(bone17);
        bone17.texOffs(106, 33).addBox(-4.0F, -2.5F, -1.75F, 8.0F, 1.0F, 3.5F, -0.13F, false);
        bone17.texOffs(106, 33).addBox(-1.75F, -2.5F, -4.0F, 3.5F, 1.0F, 8.0F, -0.13F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, 0.0F, 0.0F);
        bone16.addChild(bone18);
        setRotationAngle(bone18, 0.0F, -0.7854F, 0.0F);
        bone18.texOffs(106, 33).addBox(-4.0F, -2.5F, -1.75F, 8.0F, 1.0F, 3.5F, -0.13F, false);
        bone18.texOffs(106, 33).addBox(-1.75F, -2.5F, -4.0F, 3.5F, 1.0F, 8.0F, -0.13F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, -9.25F, 3.0F);
        bone4.addChild(bone13);


        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, 0.0F, 0.0F);
        bone13.addChild(bone14);
        bone14.texOffs(113, 44).addBox(-2.0F, -2.5F, -0.5F, 4.0F, 1.0F, 1.0F, 0.565F, false);
        bone14.texOffs(113, 44).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.565F, false);
        bone14.texOffs(122, 94).addBox(-1.0F, -3.75F, -0.5F, 2.0F, 1.0F, 1.0F, -0.089F, false);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, 0.0F, 0.0F);
        bone13.addChild(bone15);
        setRotationAngle(bone15, 0.0F, -0.7854F, 0.0F);
        bone15.texOffs(113, 44).addBox(-2.0F, -2.5F, -0.5F, 4.0F, 1.0F, 1.0F, 0.565F, false);
        bone15.texOffs(113, 44).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 1.0F, 4.0F, 0.565F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, 18.7139F, -1.237F);
        bone4.addChild(bone9);


        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, -33.5139F, 4.237F);
        bone9.addChild(bone11);
        bone11.texOffs(106, 53).addBox(-3.5F, 3.5F, -1.5F, 7.0F, 2.0F, 3.0F, -0.089F, false);
        bone11.texOffs(106, 55).addBox(-1.5F, 3.5F, -3.5F, 3.0F, 2.0F, 7.0F, -0.089F, false);
        bone11.texOffs(117, 83).addBox(-1.0F, 2.5F, -4.3F, 2.0F, 2.0F, 1.0F, -0.089F, false);
        bone11.texOffs(123, 88).addBox(-0.5F, 2.3F, 3.3F, 1.0F, 2.0F, 1.0F, -0.089F, false);
        bone11.texOffs(123, 88).addBox(-4.4F, 2.3F, -0.5F, 1.0F, 2.0F, 1.0F, -0.089F, false);
        bone11.texOffs(123, 88).addBox(3.4F, 2.3F, -0.5F, 1.0F, 2.0F, 1.0F, -0.089F, true);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, -33.5139F, 4.237F);
        bone9.addChild(bone12);
        setRotationAngle(bone12, 0.0F, -0.7854F, 0.0F);
        bone12.texOffs(106, 53).addBox(-3.5F, 3.5F, -1.5F, 7.0F, 2.0F, 3.0F, -0.089F, false);
        bone12.texOffs(106, 55).addBox(-1.5F, 3.5F, -3.5F, 3.0F, 2.0F, 7.0F, -0.089F, false);

        bench = new ModelRenderer(this);
        bench.setPos(0.0F, -16.4F, 3.75F);
        bone466.addChild(bench);
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
}
