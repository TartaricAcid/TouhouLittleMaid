package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BoxModel extends ModelBase {
    private final ModelRenderer bottom;
    private final ModelRenderer x1;
    private final ModelRenderer x2;
    private final ModelRenderer z1;
    private final ModelRenderer z2;
    private final ModelRenderer top;

    public BoxModel() {
        textureWidth = 256;
        textureHeight = 256;

        bottom = new ModelRenderer(this);
        bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        bottom.cubeList.add(new ModelBox(bottom, 0, 0, -15.0F, -1.0F, -15.0F, 30, 1, 30, 0.0F, false));

        x1 = new ModelRenderer(this);
        x1.setRotationPoint(0.0F, 23.5F, -14.5F);
        x1.cubeList.add(new ModelBox(x1, 64, 31, -14.0F, -30.5F, -0.5F, 28, 30, 1, 0.0F, false));

        x2 = new ModelRenderer(this);
        x2.setRotationPoint(0.0F, 23.5F, 14.5F);
        x2.cubeList.add(new ModelBox(x2, 64, 31, -14.0F, -30.5F, -0.5F, 28, 30, 1, 0.0F, false));

        z1 = new ModelRenderer(this);
        z1.setRotationPoint(14.5F, 23.5F, 0.0F);
        z1.cubeList.add(new ModelBox(z1, 0, 31, -0.5F, -30.5F, -15.0F, 1, 30, 30, 0.0F, false));

        z2 = new ModelRenderer(this);
        z2.setRotationPoint(-14.5F, 23.5F, 0.0F);
        z2.cubeList.add(new ModelBox(z2, 0, 31, -0.5F, -30.5F, -15.0F, 1, 30, 30, 0.0F, false));

        top = new ModelRenderer(this);
        top.setRotationPoint(0.0F, 24.0F, 0.0F);
        top.cubeList.add(new ModelBox(top, 0, 0, -15.0F, -32.0F, -15.0F, 30, 1, 30, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 64, 64, -16.0F, -32.0F, -16.0F, 32, 6, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 64, 64, -16.0F, -32.0F, 15.0F, 32, 6, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 32, 61, -16.0F, -32.0F, -15.0F, 1, 6, 30, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 32, 61, 15.0F, -32.0F, -15.0F, 1, 6, 30, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bottom.render(f5);
        x1.render(f5);
        x2.render(f5);
        z1.render(f5);
        z2.render(f5);
        top.render(f5);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityBox) {
            int stage = ((EntityBox) entityIn).getOpenStage();

            if (stage == EntityBox.FIRST_STAGE) {
                top.isHidden = false;
                x1.rotateAngleX = 0;
                x2.rotateAngleX = 0;
                z1.rotateAngleZ = 0;
                z2.rotateAngleZ = 0;
            } else if (stage > EntityBox.SECOND_STAGE) {
                top.isHidden = true;
                x1.rotateAngleX = 0;
                x2.rotateAngleX = 0;
                z1.rotateAngleZ = 0;
                z2.rotateAngleZ = 0;
            } else {
                top.isHidden = true;
                x1.rotateAngleX = 0.023998277f * (EntityBox.SECOND_STAGE - stage);
                x2.rotateAngleX = -0.023998277f * (EntityBox.SECOND_STAGE - stage);
                z1.rotateAngleZ = 0.023998277f * (EntityBox.SECOND_STAGE - stage);
                z2.rotateAngleZ = -0.023998277f * (EntityBox.SECOND_STAGE - stage);
            }
        }
    }
}