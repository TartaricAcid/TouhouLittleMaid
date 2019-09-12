package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:50
 **/
@SideOnly(Side.CLIENT)
public class EntityFairyModel extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer hair;
    private final ModelRenderer hair1;
    private final ModelRenderer hair2;
    private final ModelRenderer hair3;
    private final ModelRenderer hair4;
    private final ModelRenderer hair5;
    private final ModelRenderer hair6;
    private final ModelRenderer hair7;
    private final ModelRenderer hair8;
    private final ModelRenderer blink;
    private final ModelRenderer les;
    private final ModelRenderer armRight;
    private final ModelRenderer armLeft;
    private final ModelRenderer body;
    private final ModelRenderer les2;
    private final ModelRenderer les3;
    private final ModelRenderer apron;
    private final ModelRenderer decoration3;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer decoration4;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone16;
    private final ModelRenderer line;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer line2;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer legLeft;
    private final ModelRenderer legRight;
    private final ModelRenderer wingLeft;
    private final ModelRenderer wingRight;

    public EntityFairyModel() {
        textureWidth = 128;
        textureHeight = 128;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 6.0F, 0.0F);
        head.cubeList.add(new ModelBox(head, 24, 24, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

        hair = new ModelRenderer(this);
        hair.setRotationPoint(3.5F, -0.5F, 3.75F);
        head.addChild(hair);

        hair1 = new ModelRenderer(this);
        hair1.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(hair1, 0.2618F, 0.3491F, -0.0873F);
        hair.addChild(hair1);
        hair1.cubeList.add(new ModelBox(hair1, 30, 23, -0.75F, 0.0F, -0.25F, 1, 9, 0, 0.0F, false));

        hair2 = new ModelRenderer(this);
        hair2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        setRotationAngle(hair2, 0.2618F, 0.1745F, -0.0524F);
        hair.addChild(hair2);
        hair2.cubeList.add(new ModelBox(hair2, 28, 23, -0.6F, 0.0F, 0.0F, 1, 9, 0, 0.0F, false));

        hair3 = new ModelRenderer(this);
        hair3.setRotationPoint(-2.0F, 0.0F, 0.0F);
        setRotationAngle(hair3, 0.2967F, 0.0873F, -0.0175F);
        hair.addChild(hair3);
        hair3.cubeList.add(new ModelBox(hair3, 26, 23, -0.5F, 0.0F, 0.0F, 1, 9, 0, 0.0F, false));

        hair4 = new ModelRenderer(this);
        hair4.setRotationPoint(-3.0F, 0.0F, 0.0F);
        setRotationAngle(hair4, 0.3316F, 0.0F, 0.0F);
        hair.addChild(hair4);
        hair4.cubeList.add(new ModelBox(hair4, 24, 23, -0.5F, 0.0F, 0.0F, 1, 9, 0, 0.0F, false));

        hair5 = new ModelRenderer(this);
        hair5.setRotationPoint(-4.0F, 0.0F, 0.0F);
        setRotationAngle(hair5, 0.3316F, 0.0F, 0.0175F);
        hair.addChild(hair5);
        hair5.cubeList.add(new ModelBox(hair5, 6, 0, -0.5F, 0.0F, 0.0F, 1, 9, 0, 0.0F, false));

        hair6 = new ModelRenderer(this);
        hair6.setRotationPoint(-5.0F, 0.0F, 0.0F);
        setRotationAngle(hair6, 0.2967F, -0.0873F, 0.0349F);
        hair.addChild(hair6);
        hair6.cubeList.add(new ModelBox(hair6, 4, 0, -0.5F, 0.0F, 0.0F, 1, 9, 0, 0.0F, false));

        hair7 = new ModelRenderer(this);
        hair7.setRotationPoint(-6.0F, 0.0F, 0.0F);
        setRotationAngle(hair7, 0.2793F, -0.1745F, 0.0524F);
        hair.addChild(hair7);
        hair7.cubeList.add(new ModelBox(hair7, 2, 0, -0.5F, 0.0F, 0.0F, 1, 9, 0, 0.0F, false));

        hair8 = new ModelRenderer(this);
        hair8.setRotationPoint(-7.0F, 0.0F, 0.0F);
        setRotationAngle(hair8, 0.2443F, -0.3491F, 0.0698F);
        hair.addChild(hair8);
        hair8.cubeList.add(new ModelBox(hair8, 0, 0, -0.4F, 0.0F, -0.25F, 1, 9, 0, 0.0F, false));

        blink = new ModelRenderer(this);
        blink.setRotationPoint(0.0F, 18.0F, 0.0F);
        head.addChild(blink);
        blink.cubeList.add(new ModelBox(blink, 48, 10, -4.0F, -26.0F, -4.001F, 8, 8, 0, 0.0F, false));

        les = new ModelRenderer(this);
        les.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(les);
        les.cubeList.add(new ModelBox(les, 48, 18, -4.5F, -8.5F, -1.0F, 9, 1, 1, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 32, 23, -2.5F, -9.0F, 0.0F, 5, 1, 0, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 62, 20, 2.5F, -8.75F, -1.0F, 1, 1, 1, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 59, 62, 0.5F, -9.0F, -1.0F, 1, 1, 1, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 20, 62, -1.5F, -9.0F, -1.0F, 1, 1, 1, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 16, 62, -3.5F, -8.75F, -1.0F, 1, 1, 1, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 26, 19, -5.0F, -6.5F, -0.001F, 1, 1, 0, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 61, 49, -4.75F, -7.5F, -1.0F, 1, 1, 1, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 24, 19, 4.0F, -6.5F, -0.001F, 1, 1, 0, 0.0F, false));
        les.cubeList.add(new ModelBox(les, 61, 47, 3.75F, -7.5F, -1.0F, 1, 1, 1, 0.0F, false));

        armRight = new ModelRenderer(this);
        armRight.setRotationPoint(-3.0F, 6.5F, 0.0F);
        setRotationAngle(armRight, 0.0F, 0.0F, 0.4363F);
        armRight.cubeList.add(new ModelBox(armRight, 8, 60, -2.0F, 1.0F, -1.0F, 2, 8, 2, 0.0F, false));
        armRight.cubeList.add(new ModelBox(armRight, 48, 56, -2.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F, false));

        armLeft = new ModelRenderer(this);
        armLeft.setRotationPoint(3.0F, 6.5F, 0.0F);
        setRotationAngle(armLeft, 0.0F, 0.0F, -0.4363F);
        armLeft.cubeList.add(new ModelBox(armLeft, 36, 56, -0.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F, false));
        armLeft.cubeList.add(new ModelBox(armLeft, 0, 60, 0.0F, 1.0F, -1.0F, 2, 8, 2, 0.0F, false));

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 13.5F, 0.0F);
        body.cubeList.add(new ModelBox(body, 43, 46, -2.5F, -7.5F, -2.5F, 5, 5, 5, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 24, 40, -3.0F, -7.499F, -3.0F, 6, 5, 6, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 36, 0, -3.5F, -2.5F, -3.5F, 7, 3, 7, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 24, 12, -4.0F, 0.5F, -4.0F, 8, 3, 8, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 0, -4.5F, 3.5F, -4.5F, 9, 3, 9, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 42, 40, -3.5F, 0.5F, -2.0F, 7, 2, 4, 0.0F, false));

        les2 = new ModelRenderer(this);
        les2.setRotationPoint(0.0F, -7.5F, 0.0F);
        body.addChild(les2);
        les2.cubeList.add(new ModelBox(les2, 60, 58, -5.0F, 13.0F, 2.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 56, 4.0F, 13.0F, 2.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 42, -5.0F, 13.0F, 0.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 40, 4.0F, 13.0F, 0.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 5, -5.0F, 13.0F, -1.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 3, 4.0F, 13.0F, -1.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 1, -5.0F, 13.0F, -3.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 60, 60, 4.0F, 13.0F, -3.5F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 18, 60, -3.5F, 13.0F, -5.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 14, 60, -3.5F, 13.0F, 4.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 6, 60, -1.5F, 13.0F, -5.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 59, 38, -1.5F, 13.0F, 4.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 59, 36, 0.5F, 13.0F, -5.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 58, 48, 0.5F, 13.0F, 4.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 58, 46, 2.5F, 13.0F, -5.0F, 1, 1, 1, 0.0F, false));
        les2.cubeList.add(new ModelBox(les2, 57, 4, 2.5F, 13.0F, 4.0F, 1, 1, 1, 0.0F, false));

        les3 = new ModelRenderer(this);
        les3.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(les3);
        les3.cubeList.add(new ModelBox(les3, 57, 2, -3.5F, -7.5F, -3.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 57, 0, -3.5F, -5.5F, -3.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 57, 57, -3.5F, -3.5F, -3.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 34, 63, -3.5F, -6.5F, -2.0F, 1, 3, 0, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 32, 63, -3.5F, -6.5F, 3.0F, 1, 3, 0, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 56, 37, -3.5F, -3.5F, 2.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 56, 35, -3.5F, -5.5F, 2.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 45, 56, -3.5F, -7.5F, 2.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 30, 63, 2.5F, -6.5F, -2.0F, 1, 3, 0, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 36, 54, 2.5F, -3.5F, -3.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 48, 29, 2.5F, -5.5F, -3.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 48, 27, 2.5F, -7.5F, -3.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 28, 63, 2.5F, -6.5F, 3.0F, 1, 3, 0, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 48, 25, 2.5F, -3.5F, 2.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 48, 23, 2.5F, -5.5F, 2.0F, 1, 1, 1, 0.0F, false));
        les3.cubeList.add(new ModelBox(les3, 44, 10, 2.5F, -7.5F, 2.0F, 1, 1, 1, 0.0F, false));

        apron = new ModelRenderer(this);
        apron.setRotationPoint(0.0F, -2.5F, -3.5F);
        setRotationAngle(apron, -0.2618F, 0.0F, 0.0F);
        body.addChild(apron);
        apron.cubeList.add(new ModelBox(apron, 27, 0, -3.5F, 0.0F, -0.001F, 7, 7, 0, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 27, 7, -3.5F, 1.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 31, 7, -3.5F, 3.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 36, 10, -3.5F, 5.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 24, 40, -2.5F, 6.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 40, 10, 2.5F, 5.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 24, 42, 1.5F, 6.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 42, 42, -0.5F, 6.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 42, 40, 2.5F, 3.0F, -0.5F, 1, 1, 1, 0.0F, false));
        apron.cubeList.add(new ModelBox(apron, 24, 44, 2.5F, 1.0F, -0.5F, 1, 1, 1, 0.0F, false));

        decoration3 = new ModelRenderer(this);
        decoration3.setRotationPoint(0.0F, -7.5F, 0.0F);
        body.addChild(decoration3);

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone7, 0.0F, -0.1745F, 0.1745F);
        decoration3.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 24, 17, -2.25F, 0.5F, -3.0F, 2, 1, 1, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, 0.0F, 0.1745F, -0.1745F);
        decoration3.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 24, 15, 0.25F, 0.5F, -3.0F, 2, 1, 1, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone9, -0.0873F, 0.0F, -0.2618F);
        decoration3.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 36, 63, -0.5F, 1.25F, -2.7F, 1, 2, 0, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, -0.0873F, 0.0F, 0.2618F);
        decoration3.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 62, 35, -0.5F, 1.25F, -2.7F, 1, 2, 0, 0.0F, false));

        decoration4 = new ModelRenderer(this);
        decoration4.setRotationPoint(0.0F, -1.75F, 3.0F);
        body.addChild(decoration4);
        decoration4.cubeList.add(new ModelBox(decoration4, 24, 12, -1.0F, 0.0F, 0.7F, 2, 2, 1, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone11, 0.0F, 0.3491F, 0.0F);
        decoration4.addChild(bone11);

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone12, 0.0F, 0.0F, 0.3491F);
        bone11.addChild(bone12);

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone13, 0.0F, 0.0F, 0.384F);
        bone11.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 56, 32, -4.0F, 0.25F, 0.0F, 4, 2, 1, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone14, 0.0F, -0.3491F, 0.0F);
        decoration4.addChild(bone14);

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone16, 0.0F, 0.0F, -0.384F);
        bone14.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 33, 51, 0.0F, 0.25F, 0.0F, 4, 2, 1, 0.0F, false));

        line = new ModelRenderer(this);
        line.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(line, 0.0F, -0.7854F, -0.5236F);
        decoration4.addChild(line);

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 1.0F, 1.0F);
        line.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 26, 62, 0.0F, -1.0F, 0.0F, 0, 2, 1, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone18, 0.0F, -0.2618F, 0.0F);
        line.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 24, 62, 0.2588F, -1.0F, 0.9659F, 0, 2, 1, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone19, 0.0F, -0.5236F, 0.0F);
        line.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 22, 59, 0.7588F, -1.0F, 1.8319F, 0, 2, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone20, 0.0F, -0.7854F, 0.0F);
        line.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 0, 59, 1.4659F, -1.0F, 2.5391F, 0, 2, 1, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone21, 0.0F, -1.0472F, 0.0F);
        line.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 49, 55, 2.3329F, -1.0F, 3.0391F, 0, 2, 1, 0.0F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone22, 0.0F, -1.309F, 0.0F);
        line.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 36, 55, 3.2998F, -1.0F, 3.2976F, 0, 2, 1, 0.0F, false));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone23, 0.0F, -1.5708F, 0.0F);
        line.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 40, 53, 4.3007F, -1.0F, 3.2971F, 0, 2, 1, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone24, 0.0F, -1.8326F, 0.0F);
        line.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 24, 50, 5.2663F, -1.0F, 3.0376F, 0, 2, 1, 0.0F, false));

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(0.0F, 1.0F, 1.0F);
        setRotationAngle(bone25, 0.0F, -2.0944F, 0.0F);
        line.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 28, 43, 6.1329F, -1.0F, 2.537F, 0, 2, 1, 0.0F, false));

        line2 = new ModelRenderer(this);
        line2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(line2, 0.0F, -0.7854F, -2.618F);
        decoration4.addChild(line2);

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(0.0F, -1.0F, 1.0F);
        line2.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 41, 4, 0.0F, -1.0F, 0.0F, 0, 2, 1, 0.0F, false));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone27, 0.0F, -0.2618F, 0.0F);
        line2.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 41, 2, 0.2588F, -1.0F, 0.9659F, 0, 2, 1, 0.0F, false));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone28, 0.0F, -0.5236F, 0.0F);
        line2.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 41, 0, 0.7588F, -1.0F, 1.8319F, 0, 2, 1, 0.0F, false));

        bone29 = new ModelRenderer(this);
        bone29.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone29, 0.0F, -0.7854F, 0.0F);
        line2.addChild(bone29);
        bone29.cubeList.add(new ModelBox(bone29, 28, 41, 1.4659F, -1.0F, 2.5391F, 0, 2, 1, 0.0F, false));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone30, 0.0F, -1.0472F, 0.0F);
        line2.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 28, 39, 2.3329F, -1.0F, 3.0391F, 0, 2, 1, 0.0F, false));

        bone31 = new ModelRenderer(this);
        bone31.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone31, 0.0F, -1.309F, 0.0F);
        line2.addChild(bone31);
        bone31.cubeList.add(new ModelBox(bone31, 30, 17, 3.2998F, -1.0F, 3.2976F, 0, 2, 1, 0.0F, false));

        bone32 = new ModelRenderer(this);
        bone32.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone32, 0.0F, -1.5708F, 0.0F);
        line2.addChild(bone32);
        bone32.cubeList.add(new ModelBox(bone32, 30, 15, 4.3007F, -1.0F, 3.2971F, 0, 2, 1, 0.0F, false));

        bone33 = new ModelRenderer(this);
        bone33.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone33, 0.0F, -1.8326F, 0.0F);
        line2.addChild(bone33);
        bone33.cubeList.add(new ModelBox(bone33, 30, 13, 5.2663F, -1.0F, 3.0376F, 0, 2, 1, 0.0F, false));

        bone34 = new ModelRenderer(this);
        bone34.setRotationPoint(0.0F, -1.0F, 1.0F);
        setRotationAngle(bone34, 0.0F, -2.0944F, 0.0F);
        line2.addChild(bone34);
        bone34.cubeList.add(new ModelBox(bone34, 30, 11, 6.1329F, -1.0F, 2.537F, 0, 2, 1, 0.0F, false));

        legLeft = new ModelRenderer(this);
        legLeft.setRotationPoint(2.0F, 15.0F, 0.0F);
        legLeft.cubeList.add(new ModelBox(legLeft, 53, 20, -1.5F, 0.0F, -1.5F, 3, 9, 3, 0.0F, false));

        legRight = new ModelRenderer(this);
        legRight.setRotationPoint(-2.0F, 15.0F, 0.0F);
        legRight.cubeList.add(new ModelBox(legRight, 24, 51, -1.5F, 0.0F, -1.5F, 3, 9, 3, 0.0F, false));

        wingLeft = new ModelRenderer(this);
        wingLeft.setRotationPoint(1.0F, 11.0F, 4.0F);
        setRotationAngle(wingLeft, -0.0873F, 1.309F, 0.0F);
        wingLeft.cubeList.add(new ModelBox(wingLeft, 0, 24, -0.3951F, -13.0F, -0.0012F, 0, 24, 12, 0.0F, false));

        wingRight = new ModelRenderer(this);
        wingRight.setRotationPoint(0.0F, 11.0F, 5.0F);
        setRotationAngle(wingRight, -0.0873F, -1.2217F, 0.0F);
        wingRight.cubeList.add(new ModelBox(wingRight, 0, 0, -0.6985F, -13.0F, 0.2899F, 0, 24, 12, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
        armRight.render(f5);
        armLeft.render(f5);
        body.render(f5);
        legLeft.render(f5);
        legRight.render(f5);
        wingLeft.render(f5);
        wingRight.render(f5);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        head.rotateAngleX = headPitch * 0.017453292F;
        head.rotateAngleY = netHeadYaw * 0.017453292F;
        armLeft.rotateAngleZ = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.rotateAngleZ = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        if (entityIn.onGround) {
            legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legRight.rotateAngleX = -MathHelper.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            armLeft.rotateAngleX = -MathHelper.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            armRight.rotateAngleX = MathHelper.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            wingLeft.rotateAngleY = -MathHelper.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
            wingRight.rotateAngleY = MathHelper.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        } else {
            legLeft.rotateAngleX = 0f;
            legRight.rotateAngleX = 0f;
            armLeft.rotateAngleX = -0.17453292F;
            armRight.rotateAngleX = -0.17453292F;
            head.rotateAngleX = head.rotateAngleX - 8 * 0.017453292F;
            wingLeft.rotateAngleY = -MathHelper.cos(ageInTicks * 0.5f) * 0.4f + 1.2f;
            wingRight.rotateAngleY = MathHelper.cos(ageInTicks * 0.5f) * 0.4f - 1.2f;
            GlStateManager.rotate(8, 1, 0, 0);
        }
        float remainder = ageInTicks % 60;
        // 0-10 显示眨眼贴图
        blink.isHidden = !(55 < remainder && remainder < 60);
    }
}
