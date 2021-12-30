package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class StatueBaseModel extends EntityModel<Entity> {
    private final ModelRenderer bone;

    public StatueBaseModel() {
        texWidth = 256;
        texHeight = 256;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 24.0F, -5.0F);
        bone.texOffs(0, 0).addBox(-24.0F, -2.0F, -19.0F, 48.0F, 2.0F, 48.0F, 0.0F, false);
        bone.texOffs(0, 53).addBox(-22.5F, -10.5F, -17.5F, 45.0F, 3.0F, 45.0F, 0.0F, false);

        ModelRenderer bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, -0.1F, 5.0F);
        bone.addChild(bone2);

        ModelRenderer bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone3);

        ModelRenderer bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, 0.0F, 4.4705F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, 0.1745F, 0.0F, 0.0F);
        bone4.texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, 0.0F, false);

        ModelRenderer bone5 = new ModelRenderer(this);
        bone5.setPos(-2.5F, 0.3422F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, 0.1719F);
        bone5.texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone6 = new ModelRenderer(this);
        bone6.setPos(2.5F, 0.3422F, 0.0F);
        bone4.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.1719F);
        bone6.texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone7);
        setRotationAngle(bone7, 0.0F, -1.5708F, 0.0F);

        ModelRenderer bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 4.4705F);
        bone7.addChild(bone8);
        setRotationAngle(bone8, 0.1745F, 0.0F, 0.0F);
        bone8.texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, 0.0F, false);

        ModelRenderer bone9 = new ModelRenderer(this);
        bone9.setPos(-2.5F, 0.3422F, 0.0F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, 0.1719F);
        bone9.texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone10 = new ModelRenderer(this);
        bone10.setPos(2.5F, 0.3422F, 0.0F);
        bone8.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, -0.1719F);
        bone10.texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone11);
        setRotationAngle(bone11, 0.0F, -3.1416F, 0.0F);

        ModelRenderer bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, 0.0F, 4.4705F);
        bone11.addChild(bone12);
        setRotationAngle(bone12, 0.1745F, 0.0F, 0.0F);
        bone12.texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, 0.0F, false);

        ModelRenderer bone13 = new ModelRenderer(this);
        bone13.setPos(-2.5F, 0.3422F, 0.0F);
        bone12.addChild(bone13);
        setRotationAngle(bone13, 0.0F, 0.0F, 0.1719F);
        bone13.texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone14 = new ModelRenderer(this);
        bone14.setPos(2.5F, 0.3422F, 0.0F);
        bone12.addChild(bone14);
        setRotationAngle(bone14, 0.0F, 0.0F, -0.1719F);
        bone14.texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone15);
        setRotationAngle(bone15, 0.0F, -4.7124F, 0.0F);

        ModelRenderer bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, 0.0F, 4.4705F);
        bone15.addChild(bone16);
        setRotationAngle(bone16, 0.1745F, 0.0F, 0.0F);
        bone16.texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, 0.0F, false);

        ModelRenderer bone17 = new ModelRenderer(this);
        bone17.setPos(-2.5F, 0.3422F, 0.0F);
        bone16.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, 0.1719F);
        bone17.texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer bone18 = new ModelRenderer(this);
        bone18.setPos(2.5F, 0.3422F, 0.0F);
        bone16.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, -0.1719F);
        bone18.texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.scale(0.325f, 0.325f, 0.325f);
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}