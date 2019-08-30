package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/13 17:40
 **/
@SideOnly(Side.CLIENT)
public class HataSasimonoFrameModel extends ModelBase {
    private final ModelRenderer sasimono;

    public HataSasimonoFrameModel() {
        ModelRenderer mainHandler;
        ModelRenderer topHandler;
        textureWidth = 64;
        textureHeight = 64;

        sasimono = new ModelRenderer(this);
        sasimono.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(sasimono, -0.0873F, 0.0F, 0.0F);

        mainHandler = new ModelRenderer(this);
        mainHandler.setRotationPoint(0.0F, 0.0F, 0.0F);
        sasimono.addChild(mainHandler);
        mainHandler.cubeList.add(new ModelBox(mainHandler, 16, 16, -0.5F, -49.0F, 2.0F, 1, 40, 1, 0.0F, false));

        topHandler = new ModelRenderer(this);
        topHandler.setRotationPoint(0.0F, 0.0F, 0.0F);
        sasimono.addChild(topHandler);
        topHandler.cubeList.add(new ModelBox(topHandler, 16, 0, -0.5F, -48.5F, 1.5F, 1, 1, 10, 0.0F, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        sasimono.render(scale);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
