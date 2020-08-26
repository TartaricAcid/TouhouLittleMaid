package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class DrumModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;

    public DrumModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 40, 40, 9.0F, -11.0F, -13.0F, 10, 11, 10, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -6.0F, -23.0F, -19.0F, 12, 23, 12, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 35, -19.0F, -17.0F, -12.0F, 10, 17, 10, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 48, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
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