package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MaidBackpackBigModel extends ModelBase {
    private final ModelRenderer bone;

    public MaidBackpackBigModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 88, 45, -4.95F, -15.0F, 0.0F, 10, 5, 10, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 118, 62, -5.95F, -14.0F, 3.0F, 1, 3, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 110, 94, -7.0F, -4.5F, 2.0F, 2, 3, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 110, 94, 5.0F, -4.5F, 2.0F, 2, 3, 6, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 118, 62, 5.05F, -14.0F, 3.0F, 1, 3, 4, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 88, 15, -5.0F, -23.0F, 0.05F, 10, 4, 10, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 88, 30, -5.05F, -19.0F, 0.0F, 10, 4, 10, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 88, 0, -5.0F, -26.0F, 0.0F, 10, 3, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 57, 0, -5.0F, -10.0F, 0.0F, 10, 10, 5, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.5F, -4.0F, -5.25F, 7, 1, 6, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 31, 0, 2.5F, -10.0F, -5.0F, 1, 8, 5, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 44, 0, -3.5F, -10.0F, -5.0F, 1, 8, 5, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 76, 31, -2.0F, -9.5F, 10.0F, 4, 5, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 64, 32, -2.0F, -17.5F, 9.5F, 4, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 76, 39, -1.5F, -2.25F, 10.0F, 3, 2, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 57, 15, -5.0F, -10.0F, 5.0F, 10, 10, 5, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 63, 2.5F, -26.0F, 0.0F, 1, 26, 10, 0.1F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 63, -3.5F, -26.0F, 0.0F, 1, 26, 10, 0.1F, false));
        bone.cubeList.add(new ModelBox(bone, 51, 121, -3.5F, -25.2F, 7.2F, 1, 2, 2, 0.1F, false));
        bone.cubeList.add(new ModelBox(bone, 51, 121, 2.5F, -25.2F, 7.2F, 1, 2, 2, 0.1F, true));
        bone.cubeList.add(new ModelBox(bone, 23, 104, -5.0F, -23.0F, 7.5F, 10, 23, 1, 0.1F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 101, -5.0F, -26.0F, 1.5F, 10, 26, 1, 0.1F, false));
        bone.cubeList.add(new ModelBox(bone, 88, 113, -5.0F, -6.0F, 0.0F, 10, 5, 10, 0.15F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}