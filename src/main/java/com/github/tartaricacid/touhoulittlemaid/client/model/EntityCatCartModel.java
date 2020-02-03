package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/2/3 13:54
 **/
public class EntityCatCartModel extends ModelBase {
    private final ModelRenderer wheel;
    private final ModelRenderer wheelRight;
    private final ModelRenderer section;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer section2;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer section3;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer section4;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer wheelLeft;
    private final ModelRenderer section5;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer section6;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer section7;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer section8;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer cart;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;

    public EntityCatCartModel() {
        textureWidth = 64;
        textureHeight = 64;

        wheel = new ModelRenderer(this);
        wheel.setRotationPoint(0.0F, 19.0F, 0.0F);
        wheel.cubeList.add(new ModelBox(wheel, 34, 17, -6.5F, -0.5273F, -0.5F, 13, 1, 1, 0.0F, false));

        wheelRight = new ModelRenderer(this);
        wheelRight.setRotationPoint(-7.0F, 5.0F, 0.0F);
        wheel.addChild(wheelRight);

        section = new ModelRenderer(this);
        section.setRotationPoint(0.0F, 0.0F, 0.0F);
        wheelRight.addChild(section);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        section.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 3, -0.5F, -5.0F, -0.5F, 1, 4, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone2, -0.3927F, 0.0F, 0.0F);
        section.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, -0.5F, -0.6173F, -2.9239F, 1, 1, 2, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone3, -0.7854F, 0.0F, 0.0F);
        section.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -0.5F, 0.4725F, -4.5549F, 1, 1, 2, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 3, -0.5F, -3.5275F, -4.0549F, 1, 4, 1, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone4, -1.1781F, 0.0F, 0.0F);
        section.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, -0.5F, 2.1035F, -5.6447F, 1, 1, 2, 0.0F, false));

        section2 = new ModelRenderer(this);
        section2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section2, -1.5708F, 0.0F, 0.0F);
        wheelRight.addChild(section2);

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
        section2.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, -0.5F, 4.0273F, -6.0273F, 1, 1, 2, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 3, -0.5F, 0.0273F, -5.5273F, 1, 4, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone6, -0.3927F, 0.0F, 0.0F);
        section2.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 0, 0, -0.5F, 5.9512F, -5.6447F, 1, 1, 2, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
        section2.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 0, 0, -0.5F, 7.5822F, -4.5549F, 1, 1, 2, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 0, 3, -0.5F, 3.5822F, -4.0549F, 1, 4, 1, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, -1.1781F, 0.0F, 0.0F);
        section2.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 0, 0, -0.5F, 8.672F, -2.9239F, 1, 1, 2, 0.0F, false));

        section3 = new ModelRenderer(this);
        section3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section3, 3.1416F, 0.0F, 0.0F);
        wheelRight.addChild(section3);

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
        section3.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 0, 0, -0.5F, 9.0547F, -1.0F, 1, 1, 2, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 0, 3, -0.5F, 5.0547F, -0.5F, 1, 4, 1, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, -0.3927F, 0.0F, 0.0F);
        section3.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 0, 0, -0.5F, 8.672F, 0.9239F, 1, 1, 2, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone11, -0.7854F, 0.0F, 0.0F);
        section3.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 0, 0, -0.5F, 7.5822F, 2.5549F, 1, 1, 2, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 0, 3, -0.5F, 3.5822F, 3.0548F, 1, 4, 1, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone12, -1.1781F, 0.0F, 0.0F);
        section3.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 0, 0, -0.5F, 5.9512F, 3.6447F, 1, 1, 2, 0.0F, false));

        section4 = new ModelRenderer(this);
        section4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section4, 1.5708F, 0.0F, 0.0F);
        wheelRight.addChild(section4);

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, 0.0F, 0.0F);
        section4.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, -0.5F, 4.0273F, 4.0273F, 1, 1, 2, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 3, -0.5F, 0.0273F, 4.5273F, 1, 4, 1, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone14, -0.3927F, 0.0F, 0.0F);
        section4.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 0, 0, -0.5F, 2.1035F, 3.6447F, 1, 1, 2, 0.0F, false));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone15, -0.7854F, 0.0F, 0.0F);
        section4.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 0, 0, -0.5F, 0.4725F, 2.5549F, 1, 1, 2, 0.0F, false));
        bone15.cubeList.add(new ModelBox(bone15, 0, 3, -0.5F, -3.5275F, 3.0548F, 1, 4, 1, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone16, -1.1781F, 0.0F, 0.0F);
        section4.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 0, 0, -0.5F, -0.6173F, 0.9239F, 1, 1, 2, 0.0F, false));

        wheelLeft = new ModelRenderer(this);
        wheelLeft.setRotationPoint(7.0F, 5.0F, 0.0F);
        wheel.addChild(wheelLeft);

        section5 = new ModelRenderer(this);
        section5.setRotationPoint(0.0F, 0.0F, 0.0F);
        wheelLeft.addChild(section5);

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 0.0F, 0.0F);
        section5.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 0, 0, -0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F, false));
        bone17.cubeList.add(new ModelBox(bone17, 0, 3, -0.5F, -5.0F, -0.5F, 1, 4, 1, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone18, -0.3927F, 0.0F, 0.0F);
        section5.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 0, 0, -0.5F, -0.6173F, -2.9239F, 1, 1, 2, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone19, -0.7854F, 0.0F, 0.0F);
        section5.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 0, 0, -0.5F, 0.4725F, -4.5549F, 1, 1, 2, 0.0F, false));
        bone19.cubeList.add(new ModelBox(bone19, 0, 3, -0.5F, -3.5275F, -4.0549F, 1, 4, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone20, -1.1781F, 0.0F, 0.0F);
        section5.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 0, 0, -0.5F, 2.1035F, -5.6447F, 1, 1, 2, 0.0F, false));

        section6 = new ModelRenderer(this);
        section6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section6, -1.5708F, 0.0F, 0.0F);
        wheelLeft.addChild(section6);

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
        section6.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 0, 0, -0.5F, 4.0273F, -6.0273F, 1, 1, 2, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 0, 3, -0.5F, 0.0273F, -5.5273F, 1, 4, 1, 0.0F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone22, -0.3927F, 0.0F, 0.0F);
        section6.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 0, 0, -0.5F, 5.9512F, -5.6447F, 1, 1, 2, 0.0F, false));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone23, -0.7854F, 0.0F, 0.0F);
        section6.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 0, 0, -0.5F, 7.5822F, -4.5549F, 1, 1, 2, 0.0F, false));
        bone23.cubeList.add(new ModelBox(bone23, 0, 3, -0.5F, 3.5822F, -4.0549F, 1, 4, 1, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone24, -1.1781F, 0.0F, 0.0F);
        section6.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 0, 0, -0.5F, 8.672F, -2.9239F, 1, 1, 2, 0.0F, false));

        section7 = new ModelRenderer(this);
        section7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section7, 3.1416F, 0.0F, 0.0F);
        wheelLeft.addChild(section7);

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(0.0F, 0.0F, 0.0F);
        section7.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 0, 0, -0.5F, 9.0547F, -1.0F, 1, 1, 2, 0.0F, false));
        bone25.cubeList.add(new ModelBox(bone25, 0, 3, -0.5F, 5.0547F, -0.5F, 1, 4, 1, 0.0F, false));

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone26, -0.3927F, 0.0F, 0.0F);
        section7.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 0, 0, -0.5F, 8.672F, 0.9239F, 1, 1, 2, 0.0F, false));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone27, -0.7854F, 0.0F, 0.0F);
        section7.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 0, 0, -0.5F, 7.5822F, 2.5549F, 1, 1, 2, 0.0F, false));
        bone27.cubeList.add(new ModelBox(bone27, 0, 3, -0.5F, 3.5822F, 3.0548F, 1, 4, 1, 0.0F, false));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone28, -1.1781F, 0.0F, 0.0F);
        section7.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 0, 0, -0.5F, 5.9512F, 3.6447F, 1, 1, 2, 0.0F, false));

        section8 = new ModelRenderer(this);
        section8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(section8, 1.5708F, 0.0F, 0.0F);
        wheelLeft.addChild(section8);

        bone29 = new ModelRenderer(this);
        bone29.setRotationPoint(0.0F, 0.0F, 0.0F);
        section8.addChild(bone29);
        bone29.cubeList.add(new ModelBox(bone29, 0, 0, -0.5F, 4.0273F, 4.0273F, 1, 1, 2, 0.0F, false));
        bone29.cubeList.add(new ModelBox(bone29, 0, 3, -0.5F, 0.0273F, 4.5273F, 1, 4, 1, 0.0F, false));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone30, -0.3927F, 0.0F, 0.0F);
        section8.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 0, 0, -0.5F, 2.1035F, 3.6447F, 1, 1, 2, 0.0F, false));

        bone31 = new ModelRenderer(this);
        bone31.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone31, -0.7854F, 0.0F, 0.0F);
        section8.addChild(bone31);
        bone31.cubeList.add(new ModelBox(bone31, 0, 0, -0.5F, 0.4725F, 2.5549F, 1, 1, 2, 0.0F, false));
        bone31.cubeList.add(new ModelBox(bone31, 0, 3, -0.5F, -3.5275F, 3.0548F, 1, 4, 1, 0.0F, false));

        bone32 = new ModelRenderer(this);
        bone32.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone32, -1.1781F, 0.0F, 0.0F);
        section8.addChild(bone32);
        bone32.cubeList.add(new ModelBox(bone32, 0, 0, -0.5F, -0.6173F, 0.9239F, 1, 1, 2, 0.0F, false));

        cart = new ModelRenderer(this);
        cart.setRotationPoint(0.0F, 20.0F, 0.0F);
        cart.cubeList.add(new ModelBox(cart, 18, 0, -6.0F, -0.5273F, -5.5F, 12, 1, 11, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 34, -6.0F, -8.5273F, 5.5F, 12, 9, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 4, 4, -6.0F, 0.0F, 5.5F, 1, 4, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 4, 4, 5.0F, 0.0F, 5.5F, 1, 4, 1, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 0, -5.999F, -8.5273F, -10.5F, 1, 8, 16, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 0, 4.999F, -8.5273F, -10.5F, 1, 8, 16, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 0, 24, -4.0F, -2.5273F, -4.0F, 8, 2, 8, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 17, 35, -6.0F, -7.0F, 6.5F, 1, 1, 9, 0.0F, false));
        cart.cubeList.add(new ModelBox(cart, 17, 35, 5.0F, -7.0F, 6.5F, 1, 1, 9, 0.0F, false));

        bone33 = new ModelRenderer(this);
        bone33.setRotationPoint(0.0F, 1.0F, -5.5273F);
        setRotationAngle(bone33, -0.4363F, 0.0F, 0.0F);
        cart.addChild(bone33);
        bone33.cubeList.add(new ModelBox(bone33, 28, 30, -6.0F, -1.4704F, -4.24F, 12, 1, 4, 0.0F, false));

        bone34 = new ModelRenderer(this);
        bone34.setRotationPoint(0.0F, -1.0F, -8.5273F);
        setRotationAngle(bone34, -0.9774F, 0.0F, 0.0F);
        cart.addChild(bone34);
        bone34.cubeList.add(new ModelBox(bone34, 24, 24, -6.0F, -0.5881F, -4.541F, 12, 1, 4, 0.0F, false));

        bone35 = new ModelRenderer(this);
        bone35.setRotationPoint(0.0F, -1.0F, -8.5273F);
        cart.addChild(bone35);
        bone35.cubeList.add(new ModelBox(bone35, 34, 12, -6.0F, -7.5343F, -2.8807F, 12, 4, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        wheel.render(f5);
        cart.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn.getRidingEntity() instanceof AbstractEntityMaid) {
            AbstractEntityMaid maid = (AbstractEntityMaid) entityIn.getRidingEntity();
            cart.rotateAngleX = 20 * 0.01745329251f;
            wheel.rotateAngleX = maid.limbSwing;
        } else {
            cart.rotateAngleX = 0;
        }
    }
}
