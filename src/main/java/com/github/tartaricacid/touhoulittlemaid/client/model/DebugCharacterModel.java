package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2019/8/11 17:48
 **/
public class DebugCharacterModel extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer glass;
    private final ModelRenderer body;
    private final ModelRenderer leftArm;
    private final ModelRenderer beer;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;

    public DebugCharacterModel() {
        textureWidth = 64;
        textureHeight = 64;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 12, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

        hat = new ModelRenderer(this);
        hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(hat);
        hat.cubeList.add(new ModelBox(hat, 0, 0, -4.5F, -9.0F, -4.5F, 9, 3, 9, 0.0F, false));
        hat.cubeList.add(new ModelBox(hat, 23, 0, -4.5F, -6.0F, -8.5F, 9, 0, 4, 0.0F, false));

        glass = new ModelRenderer(this);
        glass.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(glass);
        glass.cubeList.add(new ModelBox(glass, 27, 4, -4.0F, -4.5F, -4.25F, 8, 3, 0, 0.0F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 0, 28, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        setRotationAngle(leftArm, -0.8727F, -0.1745F, 0.0F);
        leftArm.cubeList.add(new ModelBox(leftArm, 40, 24, -3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));

        beer = new ModelRenderer(this);
        beer.setRotationPoint(0.0F, 0.0F, 0.0F);
        leftArm.addChild(beer);
        beer.cubeList.add(new ModelBox(beer, 0, 44, -2.5F, 9.0F, -6.0F, 3, 3, 6, 0.0F, false));

        rightArm = new ModelRenderer(this);
        rightArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        setRotationAngle(rightArm, -0.4363F, -0.1745F, 0.0F);
        rightArm.cubeList.add(new ModelBox(rightArm, 24, 28, -1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F, false));


        leftLeg = new ModelRenderer(this);
        leftLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        setRotationAngle(leftLeg, -1.4137167F, 0.3141592F, 0.07853982F);
        leftLeg.cubeList.add(new ModelBox(leftLeg, 36, 40, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

        rightLeg = new ModelRenderer(this);
        rightLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        setRotationAngle(rightLeg, -1.4137167F, -0.3141592F, -0.07853982F);
        rightLeg.cubeList.add(new ModelBox(rightLeg, 32, 8, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
        body.render(f5);
        leftArm.render(f5);
        rightArm.render(f5);
        leftLeg.render(f5);
        rightLeg.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
