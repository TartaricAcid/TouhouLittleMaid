package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TombstoneModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer bone8;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;

    public TombstoneModel() {
        texWidth = 64;
        texHeight = 64;

        main = new ModelRenderer(this);
        main.setPos(0.5F, 17.1F, -0.5F);
        main.texOffs(0, 29).addBox(-6.0F, -2.0F, -3.0F, 11.0F, 2.0F, 7.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-7.0F, 0.0F, -6.0F, 13.0F, 1.0F, 13.0F, 0.0F, false);
        main.texOffs(0, 15).addBox(-6.0F, 1.0F, -5.0F, 11.0F, 2.0F, 11.0F, 0.0F, false);
        main.texOffs(34, 15).addBox(-4.0F, 4.3F, -3.0F, 7.0F, 1.0F, 7.0F, 0.0F, false);
        main.texOffs(33, 33).addBox(-4.5F, -12.0F, -1.5F, 8.0F, 10.0F, 4.0F, 0.0F, false);
        main.texOffs(16, 48).addBox(-8.5F, -16.0F, -7.025F, 16.0F, 16.0F, 0.0F, -5.0F, false);
        main.texOffs(40, 0).addBox(-3.0F, -14.0F, -2.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-0.5F, 4.5F, 0.5F);
        main.addChild(bone8);


        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        bone8.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, 0.3927F);
        bone6.texOffs(38, 23).addBox(2.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        bone8.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, -0.3927F);
        bone7.texOffs(38, 23).addBox(-3.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(-0.5F, 4.5F, 0.5F);
        main.addChild(bone9);
        setRotationAngle(bone9, 0.0F, -1.5708F, 0.0F);


        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.3927F);
        bone10.texOffs(38, 23).addBox(2.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.0F, -0.3927F);
        bone11.texOffs(38, 23).addBox(-3.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(3.0F, -6.75F, 0.5F);
        main.addChild(bone);
        setRotationAngle(bone, 0.0F, 0.0F, 0.1309F);
        bone.texOffs(0, 38).addBox(-0.5642F, -4.9083F, -2.5F, 2.0F, 10.0F, 5.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-1.2F, -5.25F, 0.0F);
        bone.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -1.0036F);
        bone2.texOffs(15, 38).addBox(-0.872F, -1.5934F, -2.5F, 2.0F, 4.0F, 5.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-4.0F, -6.75F, 0.5F);
        main.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.1309F);
        bone3.texOffs(0, 38).addBox(-1.4358F, -4.9083F, -2.5F, 2.0F, 10.0F, 5.0F, 0.0F, true);

        bone4 = new ModelRenderer(this);
        bone4.setPos(1.2F, -5.25F, 0.0F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 1.0036F);
        bone4.texOffs(15, 38).addBox(-1.128F, -1.5934F, -2.5F, 2.0F, 4.0F, 5.0F, 0.0F, true);
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