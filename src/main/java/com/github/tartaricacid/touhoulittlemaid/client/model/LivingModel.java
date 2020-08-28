package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LivingModel extends ModelBase {
    private final ModelRenderer bone16;
    private final ModelRenderer side;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer side2;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone14;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone;

    public LivingModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 16.8214F, 3.0322F);
        setRotationAngle(bone16, 0.0F, 3.1416F, 0.0F);
        bone16.cubeList.add(new ModelBox(bone16, 0, 17, -5.0F, 0.1786F, -2.0322F, 10, 1, 10, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 11, 46, -5.0F, -10.1214F, -3.4464F, 10, 5, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 35, 50, -3.5F, 5.6786F, 2.4678F, 7, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 11, 52, -3.0F, -9.6499F, -4.4464F, 6, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 35, 48, -3.5F, 1.1786F, -1.5322F, 7, 1, 1, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 35, 46, -3.5F, -6.6214F, -4.4464F, 7, 1, 1, 0.0F, false));

        side = new ModelRenderer(this);
        side.setRotationPoint(3.0F, 7.1786F, 5.9678F);
        bone16.addChild(side);
        side.cubeList.add(new ModelBox(side, 44, 37, -7.4142F, -5.0F, -4.0F, 1, 4, 2, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 44, 33, -7.4142F, -6.0F, -9.0F, 1, 1, 10, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 4, 17, -7.4142F, -15.4142F, -10.4142F, 1, 9, 1, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 43, 46, -7.415F, -1.5F, -7.0F, 1, 1, 8, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 8, 42, -7.4142F, -1.0F, 0.5F, 1, 1, 1, 0.0F, false));
        side.cubeList.add(new ModelBox(side, 4, 42, -7.4142F, -1.0F, -7.5F, 1, 1, 1, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 0.0F, 3.0F);
        side.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, 0.7854F);
        bone17.cubeList.add(new ModelBox(bone17, 50, 40, -16.1421F, -7.6569F, -13.4142F, 1, 2, 1, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 0.0F, 3.0F);
        side.addChild(bone18);
        setRotationAngle(bone18, 0.7854F, 0.0F, 0.0F);
        bone18.cubeList.add(new ModelBox(bone18, 50, 37, -7.4142F, -14.0208F, -4.9497F, 1, 2, 1, 0.0F, false));

        side2 = new ModelRenderer(this);
        side2.setRotationPoint(-3.0F, 7.1786F, 5.9678F);
        bone16.addChild(side2);
        side2.cubeList.add(new ModelBox(side2, 20, 32, 6.4142F, -5.0F, -4.0F, 1, 4, 2, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 16, 32, 6.4142F, -6.0F, -9.0F, 1, 1, 10, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 0, 17, 6.4142F, -15.4142F, -10.4142F, 1, 9, 1, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 25, 46, 6.415F, -1.5F, -7.0F, 1, 1, 8, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 0, 42, 6.4142F, -1.0F, 0.5F, 1, 1, 1, 0.0F, false));
        side2.cubeList.add(new ModelBox(side2, 6, 0, 6.4142F, -1.0F, -7.5F, 1, 1, 1, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 0.0F, 3.0F);
        side2.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, -0.7854F);
        bone19.cubeList.add(new ModelBox(bone19, 44, 27, 15.1421F, -7.6569F, -13.4142F, 1, 2, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0F, 3.0F);
        side2.addChild(bone20);
        setRotationAngle(bone20, 0.7854F, 0.0F, 0.0F);
        bone20.cubeList.add(new ModelBox(bone20, 0, 32, 6.4142F, -14.0208F, -4.9497F, 1, 2, 1, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 24.0F, 5.0F);


        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.75F, -0.8F, 2.0F);
        bone14.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 28, 32, -14.0F, -20.0F, -22.0F, 3, 4, 3, -0.2F, false));
        bone11.cubeList.add(new ModelBox(bone11, 53, 49, -14.0F, -19.9F, -22.0F, 3, 1, 3, 0.1F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(-12.5F, -22.0F, -20.6F);
        bone11.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 0, 50, -1.5F, -2.0F, 0.0F, 3, 4, 0, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-12.5F, -22.0F, -20.6F);
        bone11.addChild(bone13);
        setRotationAngle(bone13, 0.0F, -1.5708F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 20, 38, -1.5F, -2.0F, 0.0F, 3, 4, 0, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone14.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 0, 46, -8.0F, -17.5F, -24.4F, 2, 2, 2, -0.2F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(-7.0F, -17.25F, -23.5F);
        bone6.addChild(bone7);
        setRotationAngle(bone7, -0.4363F, 0.0873F, 0.0F);
        bone7.cubeList.add(new ModelBox(bone7, 16, 32, -0.5F, -8.75F, -0.5F, 1, 9, 1, -0.1F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, -8.175F, 0.0F);
        bone7.addChild(bone8);
        setRotationAngle(bone8, 0.3054F, 0.1309F, 0.0F);
        bone8.cubeList.add(new ModelBox(bone8, 0, 46, -0.5F, -0.425F, -0.5F, 1, 1, 9, -0.11F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.075F, 8.2F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.5941F, 0.272F, 0.5042F);
        bone9.cubeList.add(new ModelBox(bone9, 28, 39, -0.5F, -0.5335F, -0.442F, 1, 1, 2, -0.2F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(-0.0041F, -0.0998F, 1.1947F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, -0.3054F, -0.6545F, -0.2182F);
        bone10.cubeList.add(new ModelBox(bone10, 32, 39, -1.0F, -0.5F, -0.2F, 2, 1, 1, -0.1F, false));
        bone10.cubeList.add(new ModelBox(bone10, 25, 52, -1.6F, -0.5F, -0.2F, 1, 1, 1, -0.3F, false));
        bone10.cubeList.add(new ModelBox(bone10, 29, 52, 1.0F, -0.5F, -0.2F, 1, 1, 1, 0.1F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(2.0F, 0.0F, 2.0F);
        bone14.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 53, 44, -3.0F, -17.7F, -22.0F, 6, 1, 4, -0.3F, false));
        bone2.cubeList.add(new ModelBox(bone2, 30, 17, -7.5F, -27.75F, -21.0F, 15, 9, 1, -0.3F, false));
        bone2.cubeList.add(new ModelBox(bone2, 44, 27, -5.0F, -17.6F, -17.4F, 12, 1, 5, -0.4F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -17.2F, -21.25F);
        bone2.addChild(bone3);
        setRotationAngle(bone3, -0.1745F, 0.0F, 0.0F);
        bone3.cubeList.add(new ModelBox(bone3, 0, 36, -2.5F, -4.5F, -0.5F, 5, 5, 1, -0.3F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-7.5F, -17.0F, -15.5F);
        bone2.addChild(bone4);
        setRotationAngle(bone4, 0.0F, -0.0873F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 0, 32, -2.0F, -0.1F, -2.0F, 4, 0, 4, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(-7.5F, -17.5F, -15.5F);
        bone2.addChild(bone5);
        setRotationAngle(bone5, 0.0F, -0.0873F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 44, 33, -1.0F, -0.2F, -1.5F, 2, 1, 3, -0.3F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone14.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -16.0F, -17.0F, -24.0F, 32, 1, 16, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 8, 0, 10.0F, -16.0F, -17.0F, 2, 14, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -13.0F, -16.0F, -17.0F, 2, 14, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 28, 32, -13.0F, -2.0F, -22.0F, 2, 2, 12, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 28, -11.0F, -10.0F, -17.0F, 21, 2, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 32, 10.0F, -2.0F, -22.0F, 2, 2, 12, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone16.render(f5);
        bone14.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}