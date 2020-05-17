package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class PortableAudioModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone4;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;

    public PortableAudioModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 2, 0, -13.0F, -17.0F, -16.5F, 26, 29, 33, -12.0F, false));
        bone.cubeList.add(new ModelBox(bone, 11, 11, -0.75F, -5.5F, -2.0F, 2, 1, 4, -0.45F, false));
        bone.cubeList.add(new ModelBox(bone, 6, 4, -0.25F, -5.2F, 3.0F, 1, 1, 1, -0.05F, false));
        bone.cubeList.add(new ModelBox(bone, 6, 0, -0.25F, -5.8F, 3.0F, 1, 1, 1, -0.15F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-9.5F, 0.0F, -9.5F);
        bone.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 74, 98, 4.55F, -9.25F, -0.65F, 12, 15, 15, -6.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 74, 66, 4.55F, -9.25F, 4.65F, 12, 15, 15, -6.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 100, 4.75F, -11.1F, -0.9F, 12, 14, 14, -6.2F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 68, 4.75F, -11.1F, 5.9F, 12, 14, 14, -6.2F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, -4.5F, 0.0F);
        setRotationAngle(bone2, 0.0F, 0.0F, -2.2689F);
        bone.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, -0.5F, -2.5F, -5.0F, 1, 1, 10, -0.2F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 11, -0.5F, -2.5F, -4.5F, 1, 1, 9, -0.15F, false));
        bone2.cubeList.add(new ModelBox(bone2, 4, 8, -0.5F, -0.5F, 4.0F, 1, 1, 1, -0.2F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 8, -0.5F, -0.5F, -5.0F, 1, 1, 1, -0.2F, false));
        bone2.cubeList.add(new ModelBox(bone2, 12, 4, -0.5F, -2.5F, 4.25F, 1, 3, 1, -0.3F, false));
        bone2.cubeList.add(new ModelBox(bone2, 11, 11, -0.5F, -2.5F, -5.25F, 1, 3, 1, -0.3F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.25F, -4.95F, 0.0F);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.2443F);
        bone.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -1.0F, -1.0F, 0.05F, 2, 2, 2, -0.75F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 11, -1.0F, -1.0F, -1.35F, 2, 2, 2, -0.75F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 4, -1.0F, -1.0F, -0.65F, 2, 2, 2, -0.75F, false));
        bone3.cubeList.add(new ModelBox(bone3, 12, 0, -1.0F, -1.0F, -2.05F, 2, 2, 2, -0.75F, false));
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