package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class StatueBaseModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;

    public StatueBaseModel() {
        textureWidth = 256;
        textureHeight = 256;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, -5.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -24.0F, -2.0F, -19.0F, 48, 2, 48, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 53, -22.5F, -10.5F, -17.5F, 45, 3, 45, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, -0.1F, 5.0F);
        bone.addChild(bone2);


        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone3);


        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 4.4705F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, 0.1745F, 0.0F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 0, 104, -22.5F, 103.8422F, 0.0F, 45, 7, 0, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(-2.5F, 0.3422F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, 0.1719F);
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, -2.0F, 105.0F, -0.001F, 2, 8, 0, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(2.5F, 0.3422F, 0.0F);
        bone4.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.1719F);
        bone6.cubeList.add(new ModelBox(bone6, 0, 0, 0.0F, 105.0F, 0.001F, 2, 8, 0, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone7);
        setRotationAngle(bone7, 0.0F, -1.5708F, 0.0F);


        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 4.4705F);
        bone7.addChild(bone8);
        setRotationAngle(bone8, 0.1745F, 0.0F, 0.0F);
        bone8.cubeList.add(new ModelBox(bone8, 0, 104, -22.5F, 103.8422F, 0.0F, 45, 7, 0, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(-2.5F, 0.3422F, 0.0F);
        bone8.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, 0.1719F);
        bone9.cubeList.add(new ModelBox(bone9, 0, 0, -2.0F, 105.0F, -0.001F, 2, 8, 0, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(2.5F, 0.3422F, 0.0F);
        bone8.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, -0.1719F);
        bone10.cubeList.add(new ModelBox(bone10, 0, 0, 0.0F, 105.0F, 0.001F, 2, 8, 0, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone11);
        setRotationAngle(bone11, 0.0F, -3.1416F, 0.0F);


        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0F, 4.4705F);
        bone11.addChild(bone12);
        setRotationAngle(bone12, 0.1745F, 0.0F, 0.0F);
        bone12.cubeList.add(new ModelBox(bone12, 0, 104, -22.5F, 103.8422F, 0.0F, 45, 7, 0, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-2.5F, 0.3422F, 0.0F);
        bone12.addChild(bone13);
        setRotationAngle(bone13, 0.0F, 0.0F, 0.1719F);
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, -2.0F, 105.0F, -0.001F, 2, 8, 0, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(2.5F, 0.3422F, 0.0F);
        bone12.addChild(bone14);
        setRotationAngle(bone14, 0.0F, 0.0F, -0.1719F);
        bone14.cubeList.add(new ModelBox(bone14, 0, 0, 0.0F, 105.0F, 0.001F, 2, 8, 0, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, -111.0F, 0.0F);
        bone2.addChild(bone15);
        setRotationAngle(bone15, 0.0F, -4.7124F, 0.0F);


        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 4.4705F);
        bone15.addChild(bone16);
        setRotationAngle(bone16, 0.1745F, 0.0F, 0.0F);
        bone16.cubeList.add(new ModelBox(bone16, 0, 104, -22.5F, 103.8422F, 0.0F, 45, 7, 0, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(-2.5F, 0.3422F, 0.0F);
        bone16.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, 0.1719F);
        bone17.cubeList.add(new ModelBox(bone17, 0, 0, -2.0F, 105.0F, -0.001F, 2, 8, 0, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(2.5F, 0.3422F, 0.0F);
        bone16.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, -0.1719F);
        bone18.cubeList.add(new ModelBox(bone18, 0, 0, 0.0F, 105.0F, 0.001F, 2, 8, 0, 0.0F, false));
    }

    @Override
    public void render(@Nullable Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.scale(0.325f, 0.325f, 0.325f);
        bone.render(scale);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
