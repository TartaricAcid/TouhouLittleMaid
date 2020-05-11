package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class HeadCubeModel extends ModelBase {
    private final ModelRenderer head;

    public HeadCubeModel() {
        textureWidth = 64;
        textureHeight = 64;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 6.0F, 1.0F);
        head.cubeList.add(new ModelBox(head, 0, 0, -4.0F, -8.0F, -5.0F, 8, 8, 8, 0.05F, false));
        head.cubeList.add(new ModelBox(head, 32, 0, -4.0F, -8.0F, -5.0F, 8, 8, 8, 0.25F, false));
    }

    @Override
    public void render(@Nonnull Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
    }
}