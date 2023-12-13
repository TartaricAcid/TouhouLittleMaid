package com.github.tartaricacid.touhoulittlemaid.client.model.backpack;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class EnderChestBackpackModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer bone466;
    private final ModelRenderer bone36;
    private final ModelRenderer bone10;
    private final ModelRenderer bone4;
    private final ModelRenderer bone6;
    private final ModelRenderer bone8;
    private final ModelRenderer bone7;
    private final ModelRenderer bone5;
    private final ModelRenderer bone9;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone467;

    public EnderChestBackpackModel() {
        texWidth = 128;
        texHeight = 128;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 35.0F, 0.0F);


        bone466 = new ModelRenderer(this);
        bone466.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(bone466);
        bone466.texOffs(0, 80).addBox(-5.0F, -25.0F, -2.0F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        bone466.texOffs(75, 65).addBox(4.0F, -25.0F, -2.0F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        bone466.texOffs(68, 41).addBox(-6.0F, -22.0F, -2.0F, 12.0F, 1.0F, 1.0F, -0.1F, false);
        bone466.texOffs(4, 32).addBox(-7.0F, -24.9F, -3.65F, 14.0F, 14.0F, 14.0F, -3.5F, false);
        bone466.texOffs(49, 39).addBox(-1.0F, -20.4F, 5.85F, 2.0F, 3.0F, 2.0F, -0.5F, false);

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

        bone4 = new ModelRenderer(this);
        bone4.setPos(-4.0F, -20.6517F, -2.8731F);
        bone466.addChild(bone4);


        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        bone4.addChild(bone6);
        bone6.texOffs(86, 15).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.025F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        bone4.addChild(bone8);
        bone8.texOffs(89, 18).addBox(0.0F, -1.0F, -3.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.5F, 5.5F, 2.0F);
        bone4.addChild(bone7);
        setRotationAngle(bone7, -0.2443F, 0.0F, 0.0F);
        bone7.texOffs(85, 14).addBox(-0.5F, 1.4406F, -4.5162F, 1.0F, 1.0F, 5.0F, 0.025F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(4.0F, -20.6517F, -2.8731F);
        bone466.addChild(bone5);


        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, 0.0F, 0.0F);
        bone5.addChild(bone9);
        bone9.texOffs(86, 15).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, 0.025F, true);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 0.0F, 0.0F);
        bone5.addChild(bone11);
        bone11.texOffs(89, 18).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 8.0F, 1.0F, 0.0F, true);

        bone12 = new ModelRenderer(this);
        bone12.setPos(-0.5F, 5.5F, 2.0F);
        bone5.addChild(bone12);
        setRotationAngle(bone12, -0.2443F, 0.0F, 0.0F);
        bone12.texOffs(85, 14).addBox(-0.5F, 1.4406F, -4.5162F, 1.0F, 1.0F, 5.0F, 0.025F, true);

        bone = new ModelRenderer(this);
        bone.setPos(-3.5F, -21.5F, -1.5F);
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
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
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