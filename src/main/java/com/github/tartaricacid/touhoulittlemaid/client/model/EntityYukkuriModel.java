package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.SlimeEntity;


public class EntityYukkuriModel extends EntityModel<SlimeEntity> {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;

    public EntityYukkuriModel() {
        texWidth = 64;
        texHeight = 64;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 24.0F, 0.0F);
        bone.texOffs(0, 18).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        bone.texOffs(0, 0).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, false);
        bone.texOffs(38, 0).addBox(3.0F, -3.75F, -5.0F, 2.0F, 4.0F, 2.0F, -0.35F, true);
        bone.texOffs(38, 0).addBox(-5.0F, -3.75F, -5.0F, 2.0F, 4.0F, 2.0F, -0.35F, false);
        bone.texOffs(0, 0).addBox(-1.5F, -11.0F, -2.0F, 2.0F, 3.0F, 0.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, -6.5F, 4.0F);
        bone.addChild(bone2);
        setRotationAngle(bone2, 0.0F, -0.1745F, -0.4363F);
        bone2.texOffs(48, 14).addBox(-0.8195F, -2.0F, 0.1061F, 7.0F, 4.0F, 1.0F, -0.2F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, -6.5F, 4.0F);
        bone.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.1745F, 0.4363F);
        bone3.texOffs(48, 14).addBox(-6.1805F, -2.0F, 0.1061F, 7.0F, 4.0F, 1.0F, -0.2F, true);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -6.5F, 4.0F);
        bone.addChild(bone4);
        setRotationAngle(bone4, 0.0F, -0.1745F, 2.7053F);
        bone4.texOffs(50, 20).addBox(-0.3195F, -2.0F, 0.1061F, 6.0F, 4.0F, 1.0F, -0.3F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -6.5F, 4.0F);
        bone.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.1745F, -2.7053F);
        bone5.texOffs(50, 20).addBox(-5.6805F, -2.0F, 0.1061F, 6.0F, 4.0F, 1.0F, -0.3F, true);

        bone6 = new ModelRenderer(this);
        bone6.setPos(3.0F, -3.0F, 1.0F);
        bone.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.2618F);
        bone6.texOffs(48, 0).addBox(0.1F, -3.5F, -3.3F, 2.0F, 7.0F, 6.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(-3.0F, -3.0F, 1.0F);
        bone.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, 0.2618F);
        bone7.texOffs(48, 0).addBox(-2.1F, -3.5F, -3.3F, 2.0F, 7.0F, 6.0F, 0.0F, true);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, -8.0F, 6.0F);
        bone.addChild(bone8);
        setRotationAngle(bone8, 0.1745F, 0.0F, 0.0F);
        bone8.texOffs(54, 35).addBox(-2.0F, -0.6042F, -1.0909F, 4.0F, 9.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(SlimeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}