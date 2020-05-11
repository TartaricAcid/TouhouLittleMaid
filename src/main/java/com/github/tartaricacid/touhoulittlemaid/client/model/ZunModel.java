package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class ZunModel extends ModelBase {
    private final ModelRenderer ZUN;
    private final ModelRenderer head;
    private final ModelRenderer glasses;
    private final ModelRenderer body;
    private final ModelRenderer armRight;
    private final ModelRenderer handRight;
    private final ModelRenderer armLeft;
    private final ModelRenderer handLeft;
    private final ModelRenderer legRight;
    private final ModelRenderer footRight;
    private final ModelRenderer legLeft;
    private final ModelRenderer bone2;
    private final ModelRenderer chair;
    private final ModelRenderer bone;
    private final ModelRenderer beer;
    private final ModelRenderer name;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;

    public ZunModel() {
        textureWidth = 128;
        textureHeight = 128;

        ZUN = new ModelRenderer(this);
        ZUN.setRotationPoint(2.0F, 14.0F, -3.0F);
        setRotationAngle(ZUN, -0.2618F, 0.1745F, 0.0F);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -14.0F, 0.0F);
        setRotationAngle(head, 0.1745F, 0.0F, 0.0F);
        ZUN.addChild(head);
        head.cubeList.add(new ModelBox(head, 48, 12, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 48, 0, -4.5F, -8.5F, -4.5F, 9, 3, 9, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 21, 42, -4.5F, -5.5F, -7.5F, 9, 0, 3, 0.0F, false));

        glasses = new ModelRenderer(this);
        glasses.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(glasses);
        glasses.cubeList.add(new ModelBox(glasses, 0, 42, -3.5F, -4.3F, -5.0F, 3, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 42, 0.5F, -4.3F, -5.0F, 3, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 42, -3.5F, -2.7F, -5.0F, 3, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 42, 0.5F, -2.7F, -5.0F, 3, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 44, -1.0F, -3.7F, -5.0F, 2, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 44, -4.8F, -3.7F, -4.99F, 2, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 44, 2.8F, -3.7F, -4.99F, 2, 1, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 44, 42, -3.8F, -4.0F, -5.0F, 1, 2, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 44, 42, 0.2F, -4.0F, -5.0F, 1, 2, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 44, 42, -1.2F, -4.0F, -5.0F, 1, 2, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 44, 42, 2.8F, -4.0F, -5.0F, 1, 2, 1, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 38, 42, -4.8F, -3.7F, -4.69F, 1, 1, 4, -0.35F, false));
        glasses.cubeList.add(new ModelBox(glasses, 38, 42, 3.8F, -3.7F, -4.69F, 1, 1, 4, -0.35F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -14.0F, 0.0F);
        ZUN.addChild(body);
        body.cubeList.add(new ModelBox(body, 48, 56, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));

        armRight = new ModelRenderer(this);
        armRight.setRotationPoint(-5.0F, -12.0F, 0.0F);
        setRotationAngle(armRight, -0.6981F, 0.6109F, 0.2618F);
        ZUN.addChild(armRight);
        armRight.cubeList.add(new ModelBox(armRight, 72, 50, -3.0F, -2.0F, -2.0F, 4, 6, 4, 0.0F, false));

        handRight = new ModelRenderer(this);
        handRight.setRotationPoint(-1.0F, 4.0F, 2.0F);
        setRotationAngle(handRight, -0.5236F, 0.0F, 0.0F);
        armRight.addChild(handRight);
        handRight.cubeList.add(new ModelBox(handRight, 28, 66, -2.0F, 0.0F, -4.0F, 4, 6, 4, 0.01F, false));

        armLeft = new ModelRenderer(this);
        armLeft.setRotationPoint(5.0F, -12.0F, 0.0F);
        setRotationAngle(armLeft, -0.5236F, 0.4363F, 0.0F);
        ZUN.addChild(armLeft);
        armLeft.cubeList.add(new ModelBox(armLeft, 72, 40, -1.0F, -2.0F, -2.0F, 4, 6, 4, 0.0F, false));

        handLeft = new ModelRenderer(this);
        handLeft.setRotationPoint(1.0F, 4.0F, 2.0F);
        setRotationAngle(handLeft, -1.1345F, 0.0F, 0.0F);
        armLeft.addChild(handLeft);
        handLeft.cubeList.add(new ModelBox(handLeft, 68, 68, -2.0F, 0.0F, -4.0F, 4, 6, 4, 0.01F, false));

        legRight = new ModelRenderer(this);
        legRight.setRotationPoint(-1.9F, -2.0F, 0.0F);
        setRotationAngle(legRight, -1.1345F, 0.4363F, 0.0F);
        ZUN.addChild(legRight);
        legRight.cubeList.add(new ModelBox(legRight, 40, 72, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F, false));

        footRight = new ModelRenderer(this);
        footRight.setRotationPoint(-0.1F, 6.0F, -2.0F);
        setRotationAngle(footRight, 0.8727F, 0.0F, 0.0F);
        legRight.addChild(footRight);
        footRight.cubeList.add(new ModelBox(footRight, 12, 66, -1.9F, 0.0F, 0.0F, 4, 6, 4, 0.01F, false));

        legLeft = new ModelRenderer(this);
        legLeft.setRotationPoint(1.9F, -2.0F, 0.0F);
        setRotationAngle(legLeft, -1.309F, 0.0873F, 0.0F);
        ZUN.addChild(legLeft);
        legLeft.cubeList.add(new ModelBox(legLeft, 0, 72, -2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.1F, 6.0F, -2.0F);
        setRotationAngle(bone2, 0.8727F, 0.0F, 0.0F);
        legLeft.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 58, -2.1F, 0.0F, 0.0F, 4, 6, 4, 0.01F, false));

        chair = new ModelRenderer(this);
        chair.setRotationPoint(0.0F, 24.0F, 0.0F);
        chair.cubeList.add(new ModelBox(chair, 46, 36, -9.0F, -36.0F, 5.0F, 18, 2, 2, 0.0F, false));
        chair.cubeList.add(new ModelBox(chair, 0, 38, -11.0F, -10.0F, -7.0F, 22, 2, 2, 0.0F, false));
        chair.cubeList.add(new ModelBox(chair, 0, 38, -11.0F, -10.0F, 5.0F, 22, 2, 2, 0.0F, false));
        chair.cubeList.add(new ModelBox(chair, 20, 46, 9.0F, -18.0F, -7.0F, 2, 8, 12, 0.0F, false));
        chair.cubeList.add(new ModelBox(chair, 20, 46, -11.0F, -18.0F, -7.0F, 2, 8, 12, 0.0F, false));
        chair.cubeList.add(new ModelBox(chair, 0, 0, -11.0F, -34.0F, 5.0F, 22, 24, 2, 0.0F, false));
        chair.cubeList.add(new ModelBox(chair, 56, 74, 6.5F, -4.5F, -3.5F, 1, 1, 1, 3.5F, false));
        chair.cubeList.add(new ModelBox(chair, 56, 74, -7.5F, -4.5F, -3.5F, 1, 1, 1, 3.5F, false));
        chair.cubeList.add(new ModelBox(chair, 56, 74, -7.5F, -4.5F, 2.5F, 1, 1, 1, 3.5F, false));
        chair.cubeList.add(new ModelBox(chair, 56, 74, 6.5F, -4.5F, 2.5F, 1, 1, 1, 3.5F, false));
        chair.cubeList.add(new ModelBox(chair, 56, 74, -0.5F, -4.5F, 2.5F, 1, 1, 1, 3.5F, false));
        chair.cubeList.add(new ModelBox(chair, 56, 74, -0.5F, -4.5F, -3.5F, 1, 1, 1, 3.5F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(2.0F, 0.0F, 0.0F);
        setRotationAngle(bone, -1.5708F, 0.0F, 0.0F);
        chair.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 26, -13.0F, -5.0F, -10.0F, 22, 10, 2, 0.0F, false));

        beer = new ModelRenderer(this);
        beer.setRotationPoint(0.0F, 24.0F, 0.0F);
        beer.cubeList.add(new ModelBox(beer, 0, 42, 0.0F, -24.0F, -11.0F, 8, 8, 8, -2.5F, false));
        beer.cubeList.add(new ModelBox(beer, 40, 40, 0.0F, -27.0F, -11.0F, 8, 8, 8, -2.5F, false));
        beer.cubeList.add(new ModelBox(beer, 48, 28, 2.5F, -21.5F, -8.5F, 3, 3, 3, -0.1F, false));
        beer.cubeList.add(new ModelBox(beer, 48, 28, 2.5F, -23.3F, -8.5F, 3, 3, 3, -0.1F, false));

        name = new ModelRenderer(this);
        name.setRotationPoint(0.0F, 23.75F, -1.0F);

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -0.1F, 0.0F);
        setRotationAngle(bone3, 0.0F, -1.5708F, 0.0F);
        name.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 102, -10.01F, -8.9F, 1.001F, 13, 13, 13, -4.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, -0.1F, 0.0F);
        name.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 0, 102, -9.001F, -8.9F, -10.01F, 13, 13, 13, -4.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, -0.1F, 0.0F);
        setRotationAngle(bone5, 0.0F, 1.5708F, 0.0F);
        name.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 0, 102, -2.99F, -8.9F, -4.001F, 13, 13, 13, -4.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, -0.1F, 0.0F);
        setRotationAngle(bone6, 0.0F, 3.1416F, 0.0F);
        name.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 0, 102, -13.999F, -8.9F, -2.99F, 13, 13, 13, -4.0F, false));
    }

    @Override
    public void render(@Nonnull Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        ZUN.render(f5);
        chair.render(f5);
        beer.render(f5);
        name.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
