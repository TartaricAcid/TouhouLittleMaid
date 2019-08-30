package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/29 15:29
 **/
@SideOnly(Side.CLIENT)
public class EntityRinnosukeModel extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer glasses;
    private final ModelRenderer body;
    private final ModelRenderer bone3;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bag;
    private final ModelRenderer leftArm;
    private final ModelRenderer bone4;
    private final ModelRenderer rightArm;
    private final ModelRenderer bone5;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;

    public EntityRinnosukeModel() {
        textureWidth = 128;
        textureHeight = 128;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 8, 74, -0.5F, -11.0F, -1.0F, 4, 3, 0, 0.0F, false));
        head.cubeList.add(new ModelBox(head, 28, 16, -3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F, false));

        glasses = new ModelRenderer(this);
        glasses.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(glasses);
        glasses.cubeList.add(new ModelBox(glasses, 8, 81, -3.0F, -2.0F, -3.5F, 2, 1, 1, 0.0F, false));
        glasses.cubeList.add(new ModelBox(glasses, 0, 83, -3.999F, -3.0F, -3.5F, 1, 1, 1, 0.0F, false));
        glasses.cubeList.add(new ModelBox(glasses, 22, 70, 2.999F, -3.0F, -3.5F, 1, 1, 1, 0.0F, false));
        glasses.cubeList.add(new ModelBox(glasses, 8, 79, 1.0F, -2.0F, -3.5F, 2, 1, 1, 0.0F, false));
        glasses.cubeList.add(new ModelBox(glasses, 8, 77, -1.0F, -3.0F, -3.5F, 2, 1, 1, 0.0F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 12.0F, 0.0F);
        body.cubeList.add(new ModelBox(body, 32, 0, -4.0F, -12.0F, -2.0F, 8, 12, 4, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 16, -4.5F, -7.0F, -2.5F, 9, 6, 5, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(4.5F, 0.0F, 0.0F);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.0873F);
        body.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 10, 57, 0.0872F, -0.9962F, -2.5F, 0, 8, 5, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(-4.5F, 0.0F, 0.0F);
        setRotationAngle(bone, 0.0F, 0.0F, 0.0873F);
        body.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 57, -0.0872F, -0.9962F, -2.5F, 0, 8, 5, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, -2.5F);
        setRotationAngle(bone2, -0.0873F, 0.0F, 0.0F);
        body.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 24, 42, -4.5F, -0.9962F, -0.0872F, 9, 8, 0, 0.0F, false));

        bag = new ModelRenderer(this);
        bag.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(bag);
        bag.cubeList.add(new ModelBox(bag, 4, 83, -0.5F, -1.5F, -4.5F, 1, 1, 1, 0.0F, false));
        bag.cubeList.add(new ModelBox(bag, 8, 70, -2.5F, -3.5F, -4.5F, 5, 2, 2, 0.0F, false));
        bag.cubeList.add(new ModelBox(bag, 20, 57, -3.0F, -3.0F, -4.0F, 6, 3, 2, 0.0F, false));

        leftArm = new ModelRenderer(this);
        leftArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        leftArm.cubeList.add(new ModelBox(leftArm, 32, 29, -3.0F, -2.0F, -2.0F, 4, 9, 4, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(1.0F, 0.0F, 0.0F);
        leftArm.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 0, 70, -3.0F, -1.0F, -1.0F, 2, 11, 2, 0.0F, false));

        rightArm = new ModelRenderer(this);
        rightArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        rightArm.cubeList.add(new ModelBox(rightArm, 16, 29, -1.0F, -2.0F, -2.0F, 4, 9, 4, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(-1.0F, 0.0F, 0.0F);
        rightArm.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 36, 57, 1.0F, -1.0F, -1.0F, 2, 11, 2, 0.0F, false));

        leftLeg = new ModelRenderer(this);
        leftLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 29, -2.0F, 0.0F, -2.0F, 4, 9, 4, 0.0F, false));
        leftLeg.cubeList.add(new ModelBox(leftLeg, 12, 42, -1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F, false));

        rightLeg = new ModelRenderer(this);
        rightLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        rightLeg.cubeList.add(new ModelBox(rightLeg, 52, 16, -2.0F, 0.0F, -2.0F, 4, 9, 4, 0.0F, false));
        rightLeg.cubeList.add(new ModelBox(rightLeg, 0, 42, -1.3F, 0.0F, -1.5F, 3, 12, 3, 0.0F, false));
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

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        head.rotateAngleX = headPitch * 0.017453292F;
        head.rotateAngleY = netHeadYaw * 0.017453292F;
        leftArm.rotateAngleX = -MathHelper.cos(limbSwing * 0.8f) * 0.7F * limbSwingAmount;
        leftArm.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.05f;
        rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.8f) * 0.7F * limbSwingAmount;
        rightArm.rotateAngleZ = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.05f;
        leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.8f) * limbSwingAmount;
        rightLeg.rotateAngleX = -MathHelper.cos(limbSwing * 0.8f) * limbSwingAmount;
    }
}
