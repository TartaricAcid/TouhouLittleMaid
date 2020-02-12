package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/1/20 16:06
 **/
public class EntitySuitcaseModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone4;
    private final ModelRenderer bone3;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone2;

    public EntitySuitcaseModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 23.25F, 0.25F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -15.0F, -20.75F, -11.25F, 30, 32, 26, -11.0F, false));
        bone.cubeList.add(new ModelBox(bone, 86, 60, -5.5F, -14.75F, -3.25F, 11, 10, 10, -4.75F, false));
        bone.cubeList.add(new ModelBox(bone, 111, 126, -3.5F, 0.0F, 2.5F, 1, 1, 1, -0.25F, false));
        bone.cubeList.add(new ModelBox(bone, 111, 126, 2.5F, 0.0F, 2.5F, 1, 1, 1, -0.25F, true));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-4.1F, -4.75F, 1.75F);
        bone.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 94, 0, -4.0F, -5.0F, -5.0F, 8, 10, 9, -3.85F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -13.9F, 0.3F);
        bone.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 124, 122, -2.0F, 0.65F, -0.5F, 1, 5, 1, -0.2F, true));
        bone3.cubeList.add(new ModelBox(bone3, 124, 122, 1.0F, 0.65F, -0.5F, 1, 5, 1, -0.2F, false));
        bone3.cubeList.add(new ModelBox(bone3, 118, 122, 1.0F, -3.35F, -0.5F, 1, 5, 1, -0.25F, false));
        bone3.cubeList.add(new ModelBox(bone3, 118, 122, -2.0F, -3.35F, -0.5F, 1, 5, 1, -0.25F, true));
        bone3.cubeList.add(new ModelBox(bone3, 78, 82, -7.0F, -8.85F, -5.5F, 14, 11, 11, -5.1F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 100, 124, -4.85F, -0.9F, 2.6F, 2, 2, 2, -0.8F, false));
        bone5.cubeList.add(new ModelBox(bone5, 100, 124, 2.85F, -0.9F, 2.6F, 2, 2, 2, -0.8F, true));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 1.5F, 0.0F);
        bone.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 100, 118, -4.85F, -12.1F, 2.6F, 2, 2, 2, -0.8F, false));
        bone6.cubeList.add(new ModelBox(bone6, 100, 118, 2.85F, -12.1F, 2.6F, 2, 2, 2, -0.8F, true));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 1.5F, -0.5F);
        bone.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 100, 112, -4.85F, -12.1F, -0.6F, 2, 2, 2, -0.8F, false));
        bone7.cubeList.add(new ModelBox(bone7, 100, 112, 2.85F, -12.1F, -0.6F, 2, 2, 2, -0.8F, true));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 23.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 114, 108, -5.7F, -1.75F, -1.75F, 3, 4, 4, -1.25F, true));
        bone2.cubeList.add(new ModelBox(bone2, 114, 108, 2.7F, -1.75F, -1.75F, 3, 4, 4, -1.25F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
        bone2.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn.getRidingEntity() != null) {
            bone.rotateAngleX = 50 * 0.01745329251f;
        } else {
            bone.rotateAngleX = 0;
        }
        bone.rotateAngleY = -netHeadYaw * 0.01745329251f / 8;
    }
}
