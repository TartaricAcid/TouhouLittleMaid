package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LivingModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone2;

    public LivingModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 32, 32, -8.0F, -20.0F, -16.0F, 17, 9, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 24, 24, -4.0F, -12.0F, -13.0F, 10, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -12.0F, -11.0F, -19.0F, 26, 11, 12, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-25.0F, 1.0F, 2.0F);
        bone.addChild(bone3);


        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(15.5F, -12.25F, -11.5F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.0436F);
        bone4.cubeList.add(new ModelBox(bone4, 4, 0, -0.5F, -10.75F, -0.5F, 1, 11, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, -10.25F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, 1.789F);
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, -0.5F, -10.5F, -0.5F, 1, 11, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.5F, -9.0F, 0.25F);
        bone5.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.3491F, 0.0F);
        bone6.cubeList.add(new ModelBox(bone6, 8, 8, -0.5F, -1.0F, 2.75F, 1, 1, 1, 0.3F, false));
        bone6.cubeList.add(new ModelBox(bone6, 0, 39, -0.5F, -1.0F, -2.25F, 1, 1, 6, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 23, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
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