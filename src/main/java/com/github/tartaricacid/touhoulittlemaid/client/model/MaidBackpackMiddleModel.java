package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MaidBackpackMiddleModel extends ModelBase {
    private final ModelRenderer backpack;

    public MaidBackpackMiddleModel() {
        textureWidth = 64;
        textureHeight = 64;
        backpack = new ModelRenderer(this);
        backpack.setRotationPoint(0.0F, 24.0F, 0.0F);
        backpack.cubeList.add(new ModelBox(backpack, 0, 0, -8.0F, -15.0F, -4.0F, 16, 15, 8, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        backpack.render(f5);
    }
}