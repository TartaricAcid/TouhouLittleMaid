package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class EntityMaidModel extends ModelBase {
    //fields
    private ModelRenderer head;
    private ModelRenderer bodyTop;
    private ModelRenderer bodyMiddle;
    private ModelRenderer bodyBottom;
    private ModelRenderer legLeft;
    private ModelRenderer legRight;
    private ModelRenderer armLeft;
    private ModelRenderer armRight;
    private ModelRenderer hair;
    private ModelRenderer hairLeft;
    private ModelRenderer hairRight;
    private ModelRenderer headdressLeftTop;
    private ModelRenderer headdressRightTop;
    private ModelRenderer headdressLeftBottom;
    private ModelRenderer headdressRightBottom;
    private ModelRenderer headdressMiddle;
    private ModelRenderer pantsu;

    public EntityMaidModel() {
        textureWidth = 128;
        textureHeight = 64;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, 6F, 0F);
        head.setTextureSize(128, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        bodyTop = new ModelRenderer(this, 33, 0);
        bodyTop.addBox(-3F, 0F, -3F, 6, 5, 6);
        bodyTop.setRotationPoint(0F, 6F, 0F);
        bodyTop.setTextureSize(128, 64);
        bodyTop.mirror = true;
        setRotation(bodyTop, 0F, 0F, 0F);
        bodyMiddle = new ModelRenderer(this, 0, 17);
        bodyMiddle.addBox(-4F, 0F, -4F, 8, 4, 8);
        bodyMiddle.setRotationPoint(0F, 11F, 0F);
        bodyMiddle.setTextureSize(128, 64);
        bodyMiddle.mirror = true;
        setRotation(bodyMiddle, 0F, 0F, 0F);
        bodyBottom = new ModelRenderer(this, 0, 30);
        bodyBottom.addBox(-5F, 0F, -5F, 10, 5, 10);
        bodyBottom.setRotationPoint(0F, 15F, 0F);
        bodyBottom.setTextureSize(128, 64);
        bodyBottom.mirror = true;
        setRotation(bodyBottom, 0F, 0F, 0F);
        legRight = new ModelRenderer(this, 0, 46);
        legRight.addBox(-1.5F, 0F, -1.5F, 3, 9, 3);
        legRight.setRotationPoint(-2F, 15F, 0F);
        legRight.setTextureSize(128, 64);
        legRight.mirror = true;
        setRotation(legRight, 0F, 0F, 0F);
        legLeft = new ModelRenderer(this, 13, 46);
        legLeft.addBox(-1.5F, 0F, -1.5F, 3, 9, 3);
        legLeft.setRotationPoint(2F, 15F, 0F);
        legLeft.setTextureSize(128, 64);
        legLeft.mirror = true;
        setRotation(legLeft, 0F, 0F, 0F);
        armRight = new ModelRenderer(this, 26, 46);
        armRight.addBox(-1F, 0F, -1F, 2, 8, 2);
        armRight.setRotationPoint(-4F, 6.5F, 0F);
        armRight.setTextureSize(128, 64);
        armRight.mirror = true;
        setRotation(armRight, 0F, 0F, 0.3490659F);
        armLeft = new ModelRenderer(this, 35, 46);
        armLeft.addBox(-1F, 0F, -1F, 2, 8, 2);
        armLeft.setRotationPoint(4F, 6.5F, 0F);
        armLeft.setTextureSize(128, 64);
        armLeft.mirror = true;
        setRotation(armLeft, 0F, 0F, -0.3490659F);
        hair = new ModelRenderer(this, 58, 0);
        hair.addBox(-4F, 1F, 3.8F, 8, 10, 0);
        hair.setRotationPoint(0F, 0F, 0F);
        hair.setTextureSize(128, 64);
        hair.mirror = true;
        setRotation(hair, 0.3490659F, 0F, 0F);
        hairLeft = new ModelRenderer(this, 33, 11);
        hairLeft.addBox(-4F, 0F, -3.5F, 0, 4, 1);
        hairLeft.setRotationPoint(0F, 0F, 0F);
        hairLeft.setTextureSize(128, 64);
        hairLeft.mirror = true;
        setRotation(hairLeft, 0F, 0F, 0.1745329F);
        hairRight = new ModelRenderer(this, 36, 11);
        hairRight.addBox(4F, 0F, -3.5F, 0, 4, 1);
        hairRight.setRotationPoint(0F, 0F, 0F);
        hairRight.setTextureSize(128, 64);
        hairRight.mirror = true;
        setRotation(hairRight, 0F, 0F, -0.1745329F);
        headdressLeftTop = new ModelRenderer(this, 33, 17);
        headdressLeftTop.addBox(2F, -9F, 4F, 8, 5, 1);
        headdressLeftTop.setRotationPoint(0F, 0F, 0F);
        headdressLeftTop.setTextureSize(128, 64);
        headdressLeftTop.mirror = true;
        setRotation(headdressLeftTop, 0F, 0F, -0.3490659F);
        headdressRightTop = new ModelRenderer(this, 52, 17);
        headdressRightTop.addBox(-10F, -9F, 4F, 8, 5, 1);
        headdressRightTop.setRotationPoint(0F, 0F, 0F);
        headdressRightTop.setTextureSize(128, 64);
        headdressRightTop.mirror = true;
        setRotation(headdressRightTop, 0F, 0F, 0.3490659F);
        headdressLeftBottom = new ModelRenderer(this, 41, 24);
        headdressLeftBottom.addBox(-6F, -7F, 4F, 12, 4, 1);
        headdressLeftBottom.setRotationPoint(0F, 0F, 0F);
        headdressLeftBottom.setTextureSize(128, 64);
        headdressLeftBottom.mirror = true;
        setRotation(headdressLeftBottom, 0F, 0F, 0.8726646F);
        headdressRightBottom = new ModelRenderer(this, 41, 30);
        headdressRightBottom.addBox(-6F, -7F, 4F, 12, 4, 1);
        headdressRightBottom.setRotationPoint(0F, 0F, 0F);
        headdressRightBottom.setTextureSize(128, 64);
        headdressRightBottom.mirror = true;
        setRotation(headdressRightBottom, 0F, 0F, -0.8726646F);
        headdressMiddle = new ModelRenderer(this, 41, 36);
        headdressMiddle.addBox(-2F, -9.5F, 3F, 4, 5, 3);
        headdressMiddle.setRotationPoint(0F, 0F, 0F);
        headdressMiddle.setTextureSize(128, 64);
        headdressMiddle.mirror = true;
        setRotation(headdressMiddle, 0F, 0F, 0F);
        pantsu = new ModelRenderer(this, 26, 58);
        pantsu.addBox(-4F, 0F, -2F, 8, 2, 4);
        pantsu.setRotationPoint(0F, 15F, 0F);
        pantsu.setTextureSize(128, 64);
        pantsu.mirror = true;
        setRotation(pantsu, 0F, 0F, 0F);

        head.addChild(hair);
        head.addChild(hairLeft);
        head.addChild(hairRight);
        head.addChild(headdressMiddle);
        head.addChild(headdressLeftBottom);
        head.addChild(headdressLeftTop);
        head.addChild(headdressRightBottom);
        head.addChild(headdressRightTop);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head.render(f5);
        bodyTop.render(f5);
        bodyMiddle.render(f5);
        bodyBottom.render(f5);
        legLeft.render(f5);
        legRight.render(f5);
        armLeft.render(f5);
        armRight.render(f5);
        pantsu.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
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
        this.armLeft.rotateAngleZ = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.34f;
        this.armRight.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.34f;

        EntityMaid entityMaid = (EntityMaid) entityIn;
        if (entityMaid.isSitting()) {
            this.armLeft.rotateAngleX = -0.798f;
            this.armLeft.rotateAngleZ = 0.274f;
            this.armRight.rotateAngleX = -0.798f;
            this.armRight.rotateAngleZ = -0.274f;

            this.legLeft.rotateAngleX = -0.960f;
            this.legLeft.rotateAngleZ = -0.523f;
            this.legRight.rotateAngleX = -0.960f;
            this.legRight.rotateAngleZ = 0.523f;

            GlStateManager.translate(0, 0.1f, 0);
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
