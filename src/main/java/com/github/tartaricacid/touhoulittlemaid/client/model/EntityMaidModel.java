package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class EntityMaidModel extends ModelBase {
    private final ModelRenderer upperBody;
    private final ModelRenderer head;
    private final ModelRenderer hair;
    private final ModelRenderer hairLeft;
    private final ModelRenderer hairRight;
    private final ModelRenderer headdressLeftTop;
    private final ModelRenderer headdressRightTop;
    private final ModelRenderer headdressLeftBottom;
    private final ModelRenderer headdressRightBottom;
    private final ModelRenderer headdressMiddle;
    private final ModelRenderer hairBack;
    private final ModelRenderer armLeft;
    private final ModelRenderer armRight;
    private final ModelRenderer body;
    private final ModelRenderer legLeft;
    private final ModelRenderer legRight;

    public EntityMaidModel() {
        textureWidth = 128;
        textureHeight = 64;

        upperBody = new ModelRenderer(this);
        upperBody.setRotationPoint(0.0F, 19.0F, 0.0F);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -13.0F, 0.0F);
        upperBody.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

        hair = new ModelRenderer(this);
        hair.setRotationPoint(0.0F, 0.0F, 4.0F);
        setRotationAngle(hair, 0.2618F, 0.0F, 0.0F);
        head.addChild(hair);
        hair.cubeList.add(new ModelBox(hair, 58, 0, -4.0F, 0.0F, 0.0F, 8, 10, 0, 0.0F, false));

        hairLeft = new ModelRenderer(this);
        hairLeft.setRotationPoint(4.0F, 0.0F, -3.0F);
        setRotationAngle(hairLeft, 0.0F, 0.0F, -0.1745F);
        head.addChild(hairLeft);
        hairLeft.cubeList.add(new ModelBox(hairLeft, 33, 11, 0.0F, 0.0F, -0.5F, 0, 4, 1, 0.0F, false));

        hairRight = new ModelRenderer(this);
        hairRight.setRotationPoint(-4.0F, 0.0F, -3.0F);
        setRotationAngle(hairRight, 0.0F, 0.0F, 0.1745F);
        head.addChild(hairRight);
        hairRight.cubeList.add(new ModelBox(hairRight, 36, 11, 0.0F, 0.0F, -0.5F, 0, 4, 1, 0.0F, false));

        headdressLeftTop = new ModelRenderer(this);
        headdressLeftTop.setRotationPoint(-1.0F, -6.5F, 4.5F);
        setRotationAngle(headdressLeftTop, 0.0F, 0.1745F, 0.2618F);
        head.addChild(headdressLeftTop);
        headdressLeftTop.cubeList.add(new ModelBox(headdressLeftTop, 52, 17, -8.0F, -2.5F, -0.5F, 8, 5, 1, 0.0F, false));

        headdressRightTop = new ModelRenderer(this);
        headdressRightTop.setRotationPoint(1.0F, -6.5F, 4.5F);
        setRotationAngle(headdressRightTop, 0.0F, -0.1745F, -0.2618F);
        head.addChild(headdressRightTop);
        headdressRightTop.cubeList.add(new ModelBox(headdressRightTop, 33, 17, 0.0F, -2.5F, -0.5F, 8, 5, 1, 0.0F, false));

        headdressLeftBottom = new ModelRenderer(this);
        headdressLeftBottom.setRotationPoint(0.0F, -6.0F, 4.5F);
        setRotationAngle(headdressLeftBottom, 0.0F, -0.0873F, 0.9599F);
        head.addChild(headdressLeftBottom);
        headdressLeftBottom.cubeList.add(new ModelBox(headdressLeftBottom, 41, 24, 1.0F, -3.0F, -0.5F, 12, 4, 1, 0.0F, false));

        headdressRightBottom = new ModelRenderer(this);
        headdressRightBottom.setRotationPoint(0.0F, -6.0F, 4.5F);
        setRotationAngle(headdressRightBottom, 0.0F, 0.0873F, -0.9599F);
        head.addChild(headdressRightBottom);
        headdressRightBottom.cubeList.add(new ModelBox(headdressRightBottom, 41, 30, -13.0F, -3.0F, -0.5F, 12, 4, 1, 0.0F, false));

        headdressMiddle = new ModelRenderer(this);
        headdressMiddle.setRotationPoint(0.0F, -7.0F, 4.5F);
        head.addChild(headdressMiddle);
        headdressMiddle.cubeList.add(new ModelBox(headdressMiddle, 41, 36, -2.0F, -2.5F, -1.5F, 4, 5, 3, 0.0F, false));

        hairBack = new ModelRenderer(this);
        hairBack.setRotationPoint(0.0F, -6.0F, 6.0F);
        setRotationAngle(hairBack, 0.3491F, 0.0F, -0.2094F);
        head.addChild(hairBack);
        hairBack.cubeList.add(new ModelBox(hairBack, 44, 45, -1.5F, 0.0F, -2.0F, 3, 9, 2, 0.0F, false));

        armLeft = new ModelRenderer(this);
        armLeft.setRotationPoint(3.0F, -12.5F, 0.0F);
        setRotationAngle(armLeft, 0.0F, 0.0F, -0.4363F);
        upperBody.addChild(armLeft);
        armLeft.cubeList.add(new ModelBox(armLeft, 26, 46, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        armRight = new ModelRenderer(this);
        armRight.setRotationPoint(-3.0F, -12.5F, 0.0F);
        setRotationAngle(armRight, 0.0F, 0.0F, 0.4363F);
        upperBody.addChild(armRight);
        armRight.cubeList.add(new ModelBox(armRight, 35, 46, -2.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -5.5F, 0.0F);
        upperBody.addChild(body);
        body.cubeList.add(new ModelBox(body, 33, 0, -3.0F, -7.5F, -3.0F, 6, 5, 6, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 17, -4.0F, -2.5F, -4.0F, 8, 4, 8, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 30, -5.0F, 0.5F, -5.0F, 10, 5, 10, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 26, 58, -4.0F, 1.5F, -2.0F, 8, 2, 4, 0.0F, false));

        legLeft = new ModelRenderer(this);
        legLeft.setRotationPoint(2.0F, 15.0F, 0.0F);
        legLeft.cubeList.add(new ModelBox(legLeft, 0, 46, -1.5F, 0.0F, -1.5F, 3, 9, 3, 0.0F, false));

        legRight = new ModelRenderer(this);
        legRight.setRotationPoint(-2.0F, 15.0F, 0.0F);
        legRight.cubeList.add(new ModelBox(legRight, 13, 46, -1.5F, 0.0F, -1.5F, 3, 9, 3, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        upperBody.render(f5);
        legLeft.render(f5);
        legRight.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.head.rotateAngleX = headPitch / 45f / (float) Math.PI;
        this.head.rotateAngleY = netHeadYaw / 45f / (float) Math.PI;

        // 左脚右脚的运动
        this.legLeft.rotateAngleX = MathHelper.cos(limbSwing * 1.2F) * 0.5F * limbSwingAmount;
        this.legRight.rotateAngleX = -MathHelper.cos(limbSwing * 1.2F) * 0.5F * limbSwingAmount;
        this.legLeft.rotateAngleZ = 0f;
        this.legRight.rotateAngleZ = 0f;

        // 左手右手的运动（这一处还有一个功能，即对数据进行归位）
        this.armLeft.rotateAngleX = -MathHelper.cos(limbSwing * 1.2F) * 1F * limbSwingAmount;
        this.armRight.rotateAngleX = MathHelper.cos(limbSwing * 1.2F) * 1F * limbSwingAmount;
        this.armLeft.rotateAngleY = 0f;
        this.armRight.rotateAngleY = 0f;
        this.armLeft.rotateAngleZ = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        this.armRight.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;

        EntityMaid entityMaid = (EntityMaid) entityIn;
        if (entityMaid.isRiding()) {
            this.legLeft.rotateAngleX = -0.960f;
            this.legLeft.rotateAngleZ = -0.523f;
            this.legRight.rotateAngleX = -0.960f;
            this.legRight.rotateAngleZ = 0.523f;

            GlStateManager.translate(0, 0.3f, 0);
        } else if (entityMaid.isSitting()) {
            this.armLeft.rotateAngleX = -0.798f;
            this.armLeft.rotateAngleZ = 0.274f;
            this.armRight.rotateAngleX = -0.798f;
            this.armRight.rotateAngleZ = -0.274f;

            this.legLeft.rotateAngleX = -0.960f;
            this.legLeft.rotateAngleZ = -0.523f;
            this.legRight.rotateAngleX = -0.960f;
            this.legRight.rotateAngleZ = 0.523f;

            GlStateManager.translate(0, 0.3f, 0);
        } else if (entityMaid.isSneaking()) {
            this.head.rotateAngleX = -0.2f;
            this.head.offsetX = 0f;
            this.head.offsetY = 0.0625f;
            this.upperBody.rotateAngleX = 0.3f;
        } else {
            this.head.offsetX = 0f;
            this.head.offsetY = 0f;
            this.upperBody.rotateAngleX = 0f;
        }

        if (entityMaid.isBegging()) {
            this.head.rotateAngleZ = 0.139f;
        } else {
            this.head.rotateAngleZ = 0f;
        }

        if (entityMaid.isSwingingArms()) {
            this.armLeft.rotateAngleX = -1.396f;
            this.armLeft.rotateAngleY = 0.785f;
            this.armRight.rotateAngleX = -1.396f;
            this.armRight.rotateAngleY = -0.174f;
        }
    }

    public void postRenderArm(float scale, EnumHandSide side) {
        this.getArmForSide(side).postRender(scale);
    }

    private ModelRenderer getArmForSide(EnumHandSide side) {
        return side == EnumHandSide.LEFT ? this.armLeft : this.armRight;
    }
}
