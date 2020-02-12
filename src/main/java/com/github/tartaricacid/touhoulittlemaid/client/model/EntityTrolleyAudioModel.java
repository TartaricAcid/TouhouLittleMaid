package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/2/2 23:15
 **/
public class EntityTrolleyAudioModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone3;
    private final ModelRenderer bone2;

    public EntityTrolleyAudioModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -15.0F, -21.5F, -11.0F, 30, 32, 27, -11.0F, false));
        bone.cubeList.add(new ModelBox(bone, 4, 62, -11.5F, -16.3F, -3.95F, 24, 24, 18, -9.0F, false));
        bone.cubeList.add(new ModelBox(bone, 51, 105, -8.0F, -14.5F, 0.05F, 13, 13, 10, -5.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.5F, -0.75F, 3.75F, 1, 1, 1, -0.25F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 2.5F, -0.75F, 3.75F, 1, 1, 1, -0.25F, true));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -14.65F, 0.55F);
        bone.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 124, 122, -2.0F, 0.65F, -0.5F, 1, 5, 1, -0.2F, true));
        bone3.cubeList.add(new ModelBox(bone3, 124, 122, 1.0F, 0.65F, -0.5F, 1, 5, 1, -0.2F, false));
        bone3.cubeList.add(new ModelBox(bone3, 118, 122, 1.0F, -3.35F, -0.5F, 1, 5, 1, -0.25F, false));
        bone3.cubeList.add(new ModelBox(bone3, 118, 122, -2.0F, -3.35F, -0.5F, 1, 5, 1, -0.25F, true));
        bone3.cubeList.add(new ModelBox(bone3, 0, 106, -7.0F, -8.85F, -5.5F, 14, 11, 11, -5.1F, false));

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
