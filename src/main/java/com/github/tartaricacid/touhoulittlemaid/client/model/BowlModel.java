package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/2/4 14:55
 **/
public class BowlModel extends ModelBiped {
    public static BowlModel INSTANCE = new BowlModel();

    private final ModelRenderer section;
    private final ModelRenderer level1;
    private final ModelRenderer level2;
    private final ModelRenderer level3;
    private final ModelRenderer level4;
    private final ModelRenderer level5;
    private final ModelRenderer section2;
    private final ModelRenderer level6;
    private final ModelRenderer level7;
    private final ModelRenderer level8;
    private final ModelRenderer level9;
    private final ModelRenderer level10;
    private final ModelRenderer section3;
    private final ModelRenderer level11;
    private final ModelRenderer level12;
    private final ModelRenderer level13;
    private final ModelRenderer level14;
    private final ModelRenderer level15;
    private final ModelRenderer section4;
    private final ModelRenderer level16;
    private final ModelRenderer level17;
    private final ModelRenderer level18;
    private final ModelRenderer level19;
    private final ModelRenderer level20;

    public BowlModel() {
        textureWidth = 64;
        textureHeight = 64;

        bipedHeadwear = new ModelRenderer(this);
        bipedHead = new ModelRenderer(this);
        bipedHead.setRotationPoint(0.0F, 11.0F, 0.1F);
        setRotationAngle(bipedHead, 3.1416F, 0.0F, 0.0F);
        bipedHead.cubeList.add(new ModelBox(bipedHead, 32, 8, -2.0F, -7.0F, -2.0F, 4, 1, 4, 0.0F, false));

        section = new ModelRenderer(this);
        section.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHead.addChild(section);

        level1 = new ModelRenderer(this);
        level1.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(level1);
        level1.cubeList.add(new ModelBox(level1, 18, 16, 4.0F, -3.0F, -7.0F, 1, 3, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 42, 3, 4.0F, -4.0F, -6.0F, 1, 1, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 16, 31, 3.0F, -4.0F, -7.0F, 1, 4, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 16, 31, 6.0F, -4.0F, -4.0F, 1, 4, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 42, 3, 5.0F, -4.0F, -5.0F, 1, 1, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 18, 16, 5.0F, -3.0F, -6.0F, 1, 3, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 18, 16, 6.0F, -3.0F, -5.0F, 1, 3, 1, 0.0F, false));
        level1.cubeList.add(new ModelBox(level1, 25, 36, -3.0F, -3.0F, -8.0F, 6, 3, 1, 0.0F, false));

        level2 = new ModelRenderer(this);
        level2.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(level2);
        level2.cubeList.add(new ModelBox(level2, 0, 16, 5.0F, -5.0F, -4.0F, 1, 2, 2, 0.0F, false));
        level2.cubeList.add(new ModelBox(level2, 32, 13, -3.0F, -5.0F, -7.0F, 6, 2, 1, 0.0F, false));
        level2.cubeList.add(new ModelBox(level2, 24, 0, 2.0F, -5.0F, -6.0F, 2, 2, 1, 0.0F, false));
        level2.cubeList.add(new ModelBox(level2, 24, 27, 4.0F, -5.0F, -5.0F, 1, 2, 1, 0.0F, false));

        level3 = new ModelRenderer(this);
        level3.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(level3);
        level3.cubeList.add(new ModelBox(level3, 24, 24, 4.0F, -6.0F, -4.0F, 1, 1, 2, 0.0F, false));
        level3.cubeList.add(new ModelBox(level3, 40, 22, -2.0F, -6.0F, -6.0F, 4, 1, 1, 0.0F, false));
        level3.cubeList.add(new ModelBox(level3, 0, 6, 2.0F, -6.0F, -5.0F, 2, 1, 1, 0.0F, false));

        level4 = new ModelRenderer(this);
        level4.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(level4);
        level4.cubeList.add(new ModelBox(level4, 38, 16, -2.0F, -7.0F, -5.0F, 4, 1, 2, 0.0F, false));
        level4.cubeList.add(new ModelBox(level4, 0, 0, 2.0F, -7.0F, -4.0F, 2, 1, 2, 0.0F, false));

        level5 = new ModelRenderer(this);
        level5.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(level5);
        level5.cubeList.add(new ModelBox(level5, 39, 36, -2.0F, -8.0F, -3.0F, 4, 1, 1, 0.0F, false));

        section2 = new ModelRenderer(this);
        section2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section2, 0.0F, -1.5708F, 0.0F);
        bipedHead.addChild(section2);

        level6 = new ModelRenderer(this);
        level6.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(level6);
        level6.cubeList.add(new ModelBox(level6, 18, 16, 4.0F, -3.0F, -7.0F, 1, 3, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 42, 3, 4.0F, -4.0F, -6.0F, 1, 1, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 16, 31, 3.0F, -4.0F, -7.0F, 1, 4, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 16, 31, 6.0F, -4.0F, -4.0F, 1, 4, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 42, 3, 5.0F, -4.0F, -5.0F, 1, 1, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 18, 16, 5.0F, -3.0F, -6.0F, 1, 3, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 18, 16, 6.0F, -3.0F, -5.0F, 1, 3, 1, 0.0F, false));
        level6.cubeList.add(new ModelBox(level6, 25, 36, -3.0F, -3.0F, -8.0F, 6, 3, 1, 0.0F, false));

        level7 = new ModelRenderer(this);
        level7.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(level7);
        level7.cubeList.add(new ModelBox(level7, 0, 16, 5.0F, -5.0F, -4.0F, 1, 2, 2, 0.0F, false));
        level7.cubeList.add(new ModelBox(level7, 32, 13, -3.0F, -5.0F, -7.0F, 6, 2, 1, 0.0F, false));
        level7.cubeList.add(new ModelBox(level7, 24, 0, 2.0F, -5.0F, -6.0F, 2, 2, 1, 0.0F, false));
        level7.cubeList.add(new ModelBox(level7, 24, 27, 4.0F, -5.0F, -5.0F, 1, 2, 1, 0.0F, false));

        level8 = new ModelRenderer(this);
        level8.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(level8);
        level8.cubeList.add(new ModelBox(level8, 24, 24, 4.0F, -6.0F, -4.0F, 1, 1, 2, 0.0F, false));
        level8.cubeList.add(new ModelBox(level8, 40, 22, -2.0F, -6.0F, -6.0F, 4, 1, 1, 0.0F, false));
        level8.cubeList.add(new ModelBox(level8, 0, 6, 2.0F, -6.0F, -5.0F, 2, 1, 1, 0.0F, false));

        level9 = new ModelRenderer(this);
        level9.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(level9);
        level9.cubeList.add(new ModelBox(level9, 38, 16, -2.0F, -7.0F, -5.0F, 4, 1, 2, 0.0F, false));
        level9.cubeList.add(new ModelBox(level9, 0, 0, 2.0F, -7.0F, -4.0F, 2, 1, 2, 0.0F, false));

        level10 = new ModelRenderer(this);
        level10.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(level10);
        level10.cubeList.add(new ModelBox(level10, 39, 36, -2.0F, -8.0F, -3.0F, 4, 1, 1, 0.0F, false));

        section3 = new ModelRenderer(this);
        section3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section3, 0.0F, 3.1416F, 0.0F);
        bipedHead.addChild(section3);

        level11 = new ModelRenderer(this);
        level11.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(level11);
        level11.cubeList.add(new ModelBox(level11, 18, 16, 4.0F, -3.0F, -7.0F, 1, 3, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 42, 3, 4.0F, -4.0F, -6.0F, 1, 1, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 16, 31, 3.0F, -4.0F, -7.0F, 1, 4, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 16, 31, 6.0F, -4.0F, -4.0F, 1, 4, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 42, 3, 5.0F, -4.0F, -5.0F, 1, 1, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 18, 16, 5.0F, -3.0F, -6.0F, 1, 3, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 18, 16, 6.0F, -3.0F, -5.0F, 1, 3, 1, 0.0F, false));
        level11.cubeList.add(new ModelBox(level11, 25, 36, -3.0F, -3.0F, -8.0F, 6, 3, 1, 0.0F, false));

        level12 = new ModelRenderer(this);
        level12.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(level12);
        level12.cubeList.add(new ModelBox(level12, 0, 16, 5.0F, -5.0F, -4.0F, 1, 2, 2, 0.0F, false));
        level12.cubeList.add(new ModelBox(level12, 32, 13, -3.0F, -5.0F, -7.0F, 6, 2, 1, 0.0F, false));
        level12.cubeList.add(new ModelBox(level12, 24, 0, 2.0F, -5.0F, -6.0F, 2, 2, 1, 0.0F, false));
        level12.cubeList.add(new ModelBox(level12, 24, 27, 4.0F, -5.0F, -5.0F, 1, 2, 1, 0.0F, false));

        level13 = new ModelRenderer(this);
        level13.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(level13);
        level13.cubeList.add(new ModelBox(level13, 24, 24, 4.0F, -6.0F, -4.0F, 1, 1, 2, 0.0F, false));
        level13.cubeList.add(new ModelBox(level13, 40, 22, -2.0F, -6.0F, -6.0F, 4, 1, 1, 0.0F, false));
        level13.cubeList.add(new ModelBox(level13, 0, 6, 2.0F, -6.0F, -5.0F, 2, 1, 1, 0.0F, false));

        level14 = new ModelRenderer(this);
        level14.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(level14);
        level14.cubeList.add(new ModelBox(level14, 38, 16, -2.0F, -7.0F, -5.0F, 4, 1, 2, 0.0F, false));
        level14.cubeList.add(new ModelBox(level14, 0, 0, 2.0F, -7.0F, -4.0F, 2, 1, 2, 0.0F, false));

        level15 = new ModelRenderer(this);
        level15.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(level15);
        level15.cubeList.add(new ModelBox(level15, 39, 36, -2.0F, -8.0F, -3.0F, 4, 1, 1, 0.0F, false));

        section4 = new ModelRenderer(this);
        section4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section4, 0.0F, 1.5708F, 0.0F);
        bipedHead.addChild(section4);

        level16 = new ModelRenderer(this);
        level16.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(level16);
        level16.cubeList.add(new ModelBox(level16, 18, 16, 4.0F, -3.0F, -7.0F, 1, 3, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 42, 3, 4.0F, -4.0F, -6.0F, 1, 1, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 16, 31, 3.0F, -4.0F, -7.0F, 1, 4, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 16, 31, 6.0F, -4.0F, -4.0F, 1, 4, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 42, 3, 5.0F, -4.0F, -5.0F, 1, 1, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 18, 16, 5.0F, -3.0F, -6.0F, 1, 3, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 18, 16, 6.0F, -3.0F, -5.0F, 1, 3, 1, 0.0F, false));
        level16.cubeList.add(new ModelBox(level16, 25, 36, -3.0F, -3.0F, -8.0F, 6, 3, 1, 0.0F, false));

        level17 = new ModelRenderer(this);
        level17.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(level17);
        level17.cubeList.add(new ModelBox(level17, 0, 16, 5.0F, -5.0F, -4.0F, 1, 2, 2, 0.0F, false));
        level17.cubeList.add(new ModelBox(level17, 32, 13, -3.0F, -5.0F, -7.0F, 6, 2, 1, 0.0F, false));
        level17.cubeList.add(new ModelBox(level17, 24, 0, 2.0F, -5.0F, -6.0F, 2, 2, 1, 0.0F, false));
        level17.cubeList.add(new ModelBox(level17, 24, 27, 4.0F, -5.0F, -5.0F, 1, 2, 1, 0.0F, false));

        level18 = new ModelRenderer(this);
        level18.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(level18);
        level18.cubeList.add(new ModelBox(level18, 24, 24, 4.0F, -6.0F, -4.0F, 1, 1, 2, 0.0F, false));
        level18.cubeList.add(new ModelBox(level18, 40, 22, -2.0F, -6.0F, -6.0F, 4, 1, 1, 0.0F, false));
        level18.cubeList.add(new ModelBox(level18, 0, 6, 2.0F, -6.0F, -5.0F, 2, 1, 1, 0.0F, false));

        level19 = new ModelRenderer(this);
        level19.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(level19);
        level19.cubeList.add(new ModelBox(level19, 38, 16, -2.0F, -7.0F, -5.0F, 4, 1, 2, 0.0F, false));
        level19.cubeList.add(new ModelBox(level19, 0, 0, 2.0F, -7.0F, -4.0F, 2, 1, 2, 0.0F, false));

        level20 = new ModelRenderer(this);
        level20.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(level20);
        level20.cubeList.add(new ModelBox(level20, 39, 36, -2.0F, -8.0F, -3.0F, 4, 1, 1, 0.0F, false));
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        bipedHead.rotateAngleX = 3.14f;
        bipedHead.offsetY = -1f;
    }
}
