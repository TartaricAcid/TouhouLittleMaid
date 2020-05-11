package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class ZunHeadCubeModel extends ModelBase {
    private final ModelRenderer zun;
    private final ModelRenderer head;

    public ZunHeadCubeModel() {
        textureWidth = 64;
        textureHeight = 64;

        zun = new ModelRenderer(this);
        zun.setRotationPoint(2.0F, 14.0F, -3.0F);
        setRotationAngle(zun, -0.2618F, 0.1745F, 0.0F);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -14.0F, 0.0F);
        setRotationAngle(head, 0.1745F, 0.0F, 0.0F);
        zun.addChild(head);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.05F, false));
        head.cubeList.add(new ModelBox(head, 32, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.15F, false));
    }

    @Override
    public void render(@Nonnull Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        zun.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
