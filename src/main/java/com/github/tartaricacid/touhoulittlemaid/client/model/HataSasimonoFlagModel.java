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
public class HataSasimonoFlagModel extends ModelBase {
    private final ModelRenderer hata;

    public HataSasimonoFlagModel() {
        textureWidth = 16;
        textureHeight = 24;

        hata = new ModelRenderer(this);
        hata.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(hata, -0.0873F, 0.0F, 0.0F);
        hata.cubeList.add(new ModelBox(hata, 0, -8, 0.0F, -47.5F, 3.0F, 0, 24, 8, 0.0F, false));
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        hata.render(scale);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
