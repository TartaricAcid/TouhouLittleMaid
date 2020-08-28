package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class PianoModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;

    public PianoModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -16.0F, -17.0F, -20.0F, 32, 1, 12, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 0, 13, -15.0F, -17.8F, -15.3F, 30, 1, 7, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 0, 21, -16.0F, -19.0F, -20.0F, 32, 2, 5, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 44, 30, 15.0F, -18.0F, -20.0F, 1, 1, 12, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 44, 44, -16.0F, -18.0F, -20.0F, 1, 1, 12, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 22, 41, 13.0F, -19.5F, -18.0F, 2, 1, 2, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 16, 40, 10.5F, -19.5F, -18.0F, 2, 1, 2, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 0, 9, 8.0F, -19.5F, -18.0F, 2, 1, 2, 0.0F, true));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 40, 14.0F, -2.0F, -20.0F, 2, 2, 12, 0.0F, true));
        bone2.cubeList.add(new ModelBox(bone2, 0, 54, 12.0F, -16.0F, -19.0F, 2, 2, 10, 0.0F, true));
        bone2.cubeList.add(new ModelBox(bone2, 28, 28, -16.0F, -2.0F, -20.0F, 2, 2, 12, 0.0F, true));
        bone2.cubeList.add(new ModelBox(bone2, 44, 57, -14.0F, -16.0F, -19.0F, 2, 2, 10, 0.0F, true));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-0.9063F, -8.0774F, -13.0F);
        bone2.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 1.0996F);
        bone3.cubeList.add(new ModelBox(bone3, 36, 42, -1.0F, -15.5F, -1.0F, 2, 31, 2, 0.0F, true));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.9063F, -8.0774F, -14.0F);
        bone2.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, -1.0996F);
        bone4.cubeList.add(new ModelBox(bone4, 28, 42, -1.0F, -15.5F, -2.0F, 2, 31, 2, 0.0F, true));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 0, 40, -4.0F, -7.0F, 2.0F, 2, 7, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 30, 28, 2.0F, -7.0F, 2.0F, 2, 7, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 28, 2.0F, -7.0F, -4.0F, 2, 7, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, -4.0F, -7.0F, -4.0F, 2, 7, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 28, -5.0F, -8.0F, -5.0F, 10, 2, 10, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone.render(f5);
        bone2.render(f5);
        bone5.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}