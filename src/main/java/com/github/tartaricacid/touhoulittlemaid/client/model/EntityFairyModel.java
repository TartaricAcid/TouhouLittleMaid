package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class EntityFairyModel extends EntityModel<EntityFairy> {
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
        super(RenderType::entityTranslucent);

        texWidth = 128;
        texHeight = 128;

        head = new ModelRenderer(this);
        head.setPos(0.0F, 6.0F, 0.0F);
        head.texOffs(24, 24).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        hair = new ModelRenderer(this);
        hair.setPos(3.5F, -0.5F, 3.75F);
        head.addChild(hair);


        hair1 = new ModelRenderer(this);
        hair1.setPos(0.0F, 0.0F, 0.0F);
        hair.addChild(hair1);
        setRotationAngle(hair1, 0.2618F, 0.3491F, -0.0873F);
        hair1.texOffs(30, 23).addBox(-0.75F, 0.0F, -0.25F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair2 = new ModelRenderer(this);
        hair2.setPos(-1.0F, 0.0F, 0.0F);
        hair.addChild(hair2);
        setRotationAngle(hair2, 0.2618F, 0.1745F, -0.0524F);
        hair2.texOffs(28, 23).addBox(-0.6F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair3 = new ModelRenderer(this);
        hair3.setPos(-2.0F, 0.0F, 0.0F);
        hair.addChild(hair3);
        setRotationAngle(hair3, 0.2967F, 0.0873F, -0.0175F);
        hair3.texOffs(26, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair4 = new ModelRenderer(this);
        hair4.setPos(-3.0F, 0.0F, 0.0F);
        hair.addChild(hair4);
        setRotationAngle(hair4, 0.3316F, 0.0F, 0.0F);
        hair4.texOffs(24, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair5 = new ModelRenderer(this);
        hair5.setPos(-4.0F, 0.0F, 0.0F);
        hair.addChild(hair5);
        setRotationAngle(hair5, 0.3316F, 0.0F, 0.0175F);
        hair5.texOffs(6, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair6 = new ModelRenderer(this);
        hair6.setPos(-5.0F, 0.0F, 0.0F);
        hair.addChild(hair6);
        setRotationAngle(hair6, 0.2967F, -0.0873F, 0.0349F);
        hair6.texOffs(4, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair7 = new ModelRenderer(this);
        hair7.setPos(-6.0F, 0.0F, 0.0F);
        hair.addChild(hair7);
        setRotationAngle(hair7, 0.2793F, -0.1745F, 0.0524F);
        hair7.texOffs(2, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        hair8 = new ModelRenderer(this);
        hair8.setPos(-7.0F, 0.0F, 0.0F);
        hair.addChild(hair8);
        setRotationAngle(hair8, 0.2443F, -0.3491F, 0.0698F);
        hair8.texOffs(0, 0).addBox(-0.4F, 0.0F, -0.25F, 1.0F, 9.0F, 0.0F, 0.0F, false);

        blink = new ModelRenderer(this);
        blink.setPos(0.0F, 18.0F, 0.0F);
        head.addChild(blink);
        blink.texOffs(48, 10).addBox(-4.0F, -26.0F, -4.001F, 8.0F, 8.0F, 0.0F, 0.0F, false);

        les = new ModelRenderer(this);
        les.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(les);
        les.texOffs(48, 18).addBox(-4.5F, -8.5F, -1.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        les.texOffs(32, 23).addBox(-2.5F, -9.0F, 0.0F, 5.0F, 1.0F, 0.0F, 0.0F, false);
        les.texOffs(62, 20).addBox(2.5F, -8.75F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les.texOffs(59, 62).addBox(0.5F, -9.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les.texOffs(20, 62).addBox(-1.5F, -9.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les.texOffs(16, 62).addBox(-3.5F, -8.75F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les.texOffs(26, 19).addBox(-5.0F, -6.5F, -0.001F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        les.texOffs(61, 49).addBox(-4.75F, -7.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les.texOffs(24, 19).addBox(4.0F, -6.5F, -0.001F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        les.texOffs(61, 47).addBox(3.75F, -7.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        armRight = new ModelRenderer(this);
        armRight.setPos(-3.0F, 6.5F, 0.0F);
        setRotationAngle(armRight, 0.0F, 0.0F, 0.4363F);
        armRight.texOffs(8, 60).addBox(-2.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);
        armRight.texOffs(48, 56).addBox(-2.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);

        armLeft = new ModelRenderer(this);
        armLeft.setPos(3.0F, 6.5F, 0.0F);
        setRotationAngle(armLeft, 0.0F, 0.0F, -0.4363F);
        armLeft.texOffs(36, 56).addBox(-0.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        armLeft.texOffs(0, 60).addBox(0.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 13.5F, 0.0F);
        body.texOffs(43, 46).addBox(-2.5F, -7.5F, -2.5F, 5.0F, 5.0F, 5.0F, 0.0F, false);
        body.texOffs(24, 40).addBox(-3.0F, -7.499F, -3.0F, 6.0F, 5.0F, 6.0F, 0.0F, false);
        body.texOffs(36, 0).addBox(-3.5F, -2.5F, -3.5F, 7.0F, 3.0F, 7.0F, 0.0F, false);
        body.texOffs(24, 12).addBox(-4.0F, 0.5F, -4.0F, 8.0F, 3.0F, 8.0F, 0.0F, false);
        body.texOffs(0, 0).addBox(-4.5F, 3.5F, -4.5F, 9.0F, 3.0F, 9.0F, 0.0F, false);
        body.texOffs(42, 40).addBox(-3.5F, 0.5F, -2.0F, 7.0F, 2.0F, 4.0F, 0.0F, false);

        les2 = new ModelRenderer(this);
        les2.setPos(0.0F, -7.5F, 0.0F);
        body.addChild(les2);
        les2.texOffs(60, 58).addBox(-5.0F, 13.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 56).addBox(4.0F, 13.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 42).addBox(-5.0F, 13.0F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 40).addBox(4.0F, 13.0F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 5).addBox(-5.0F, 13.0F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 3).addBox(4.0F, 13.0F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 1).addBox(-5.0F, 13.0F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(60, 60).addBox(4.0F, 13.0F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(18, 60).addBox(-3.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(14, 60).addBox(-3.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(6, 60).addBox(-1.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(59, 38).addBox(-1.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(59, 36).addBox(0.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(58, 48).addBox(0.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(58, 46).addBox(2.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les2.texOffs(57, 4).addBox(2.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        les3 = new ModelRenderer(this);
        les3.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(les3);
        les3.texOffs(57, 2).addBox(-3.5F, -7.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(57, 0).addBox(-3.5F, -5.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(57, 57).addBox(-3.5F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(34, 63).addBox(-3.5F, -6.5F, -2.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        les3.texOffs(32, 63).addBox(-3.5F, -6.5F, 3.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        les3.texOffs(56, 37).addBox(-3.5F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(56, 35).addBox(-3.5F, -5.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(45, 56).addBox(-3.5F, -7.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(30, 63).addBox(2.5F, -6.5F, -2.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        les3.texOffs(36, 54).addBox(2.5F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(48, 29).addBox(2.5F, -5.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(48, 27).addBox(2.5F, -7.5F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(28, 63).addBox(2.5F, -6.5F, 3.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        les3.texOffs(48, 25).addBox(2.5F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(48, 23).addBox(2.5F, -5.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        les3.texOffs(44, 10).addBox(2.5F, -7.5F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        apron = new ModelRenderer(this);
        apron.setPos(0.0F, -2.5F, -3.5F);
        body.addChild(apron);
        setRotationAngle(apron, -0.2618F, 0.0F, 0.0F);
        apron.texOffs(27, 0).addBox(-3.5F, 0.0F, -0.001F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        apron.texOffs(27, 7).addBox(-3.5F, 1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(31, 7).addBox(-3.5F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(36, 10).addBox(-3.5F, 5.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(24, 40).addBox(-2.5F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(40, 10).addBox(2.5F, 5.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(24, 42).addBox(1.5F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(42, 42).addBox(-0.5F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(42, 40).addBox(2.5F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        apron.texOffs(24, 44).addBox(2.5F, 1.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        decoration3 = new ModelRenderer(this);
        decoration3.setPos(0.0F, -7.5F, 0.0F);
        body.addChild(decoration3);


        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        decoration3.addChild(bone7);
        setRotationAngle(bone7, 0.0F, -0.1745F, 0.1745F);
        bone7.texOffs(24, 17).addBox(-2.25F, 0.5F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        decoration3.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.1745F, -0.1745F);
        bone8.texOffs(24, 15).addBox(0.25F, 0.5F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, 0.0F, 0.0F);
        decoration3.addChild(bone9);
        setRotationAngle(bone9, -0.0873F, 0.0F, -0.2618F);
        bone9.texOffs(36, 63).addBox(-0.5F, 1.25F, -2.7F, 1.0F, 2.0F, 0.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, 0.0F, 0.0F);
        decoration3.addChild(bone10);
        setRotationAngle(bone10, -0.0873F, 0.0F, 0.2618F);
        bone10.texOffs(62, 35).addBox(-0.5F, 1.25F, -2.7F, 1.0F, 2.0F, 0.0F, 0.0F, false);

        decoration4 = new ModelRenderer(this);
        decoration4.setPos(0.0F, -1.75F, 3.0F);
        body.addChild(decoration4);
        decoration4.texOffs(24, 12).addBox(-1.0F, 0.0F, 0.7F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 0.0F, 0.0F);
        decoration4.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.3491F, 0.0F);


        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, 0.0F, 0.0F);
        bone11.addChild(bone12);
        setRotationAngle(bone12, 0.0F, 0.0F, 0.3491F);


        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, 0.0F, 0.0F);
        bone11.addChild(bone13);
        setRotationAngle(bone13, 0.0F, 0.0F, 0.384F);
        bone13.texOffs(56, 32).addBox(-4.0F, 0.25F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, 0.0F, 0.0F);
        decoration4.addChild(bone14);
        setRotationAngle(bone14, 0.0F, -0.3491F, 0.0F);


        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, 0.0F, 0.0F);
        bone14.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.0F, -0.384F);
        bone16.texOffs(33, 51).addBox(0.0F, 0.25F, 0.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        line = new ModelRenderer(this);
        line.setPos(0.0F, 0.0F, 0.0F);
        decoration4.addChild(line);
        setRotationAngle(line, 0.0F, -0.7854F, -0.5236F);


        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone17);
        bone17.texOffs(26, 62).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone18);
        setRotationAngle(bone18, 0.0F, -0.2618F, 0.0F);
        bone18.texOffs(24, 62).addBox(0.2588F, -1.0F, 0.9659F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone19);
        setRotationAngle(bone19, 0.0F, -0.5236F, 0.0F);
        bone19.texOffs(22, 59).addBox(0.7588F, -1.0F, 1.8319F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone20);
        setRotationAngle(bone20, 0.0F, -0.7854F, 0.0F);
        bone20.texOffs(0, 59).addBox(1.4659F, -1.0F, 2.5391F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone21);
        setRotationAngle(bone21, 0.0F, -1.0472F, 0.0F);
        bone21.texOffs(49, 55).addBox(2.3329F, -1.0F, 3.0391F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone22);
        setRotationAngle(bone22, 0.0F, -1.309F, 0.0F);
        bone22.texOffs(36, 55).addBox(3.2998F, -1.0F, 3.2976F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone23 = new ModelRenderer(this);
        bone23.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone23);
        setRotationAngle(bone23, 0.0F, -1.5708F, 0.0F);
        bone23.texOffs(40, 53).addBox(4.3007F, -1.0F, 3.2971F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone24);
        setRotationAngle(bone24, 0.0F, -1.8326F, 0.0F);
        bone24.texOffs(24, 50).addBox(5.2663F, -1.0F, 3.0376F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, 1.0F, 1.0F);
        line.addChild(bone25);
        setRotationAngle(bone25, 0.0F, -2.0944F, 0.0F);
        bone25.texOffs(28, 43).addBox(6.1329F, -1.0F, 2.537F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        line2 = new ModelRenderer(this);
        line2.setPos(0.0F, 0.0F, 0.0F);
        decoration4.addChild(line2);
        setRotationAngle(line2, 0.0F, -0.7854F, -2.618F);


        bone26 = new ModelRenderer(this);
        bone26.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone26);
        bone26.texOffs(41, 4).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone27);
        setRotationAngle(bone27, 0.0F, -0.2618F, 0.0F);
        bone27.texOffs(41, 2).addBox(0.2588F, -1.0F, 0.9659F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone28 = new ModelRenderer(this);
        bone28.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone28);
        setRotationAngle(bone28, 0.0F, -0.5236F, 0.0F);
        bone28.texOffs(41, 0).addBox(0.7588F, -1.0F, 1.8319F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone29 = new ModelRenderer(this);
        bone29.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone29);
        setRotationAngle(bone29, 0.0F, -0.7854F, 0.0F);
        bone29.texOffs(28, 41).addBox(1.4659F, -1.0F, 2.5391F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone30);
        setRotationAngle(bone30, 0.0F, -1.0472F, 0.0F);
        bone30.texOffs(28, 39).addBox(2.3329F, -1.0F, 3.0391F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone31);
        setRotationAngle(bone31, 0.0F, -1.309F, 0.0F);
        bone31.texOffs(30, 17).addBox(3.2998F, -1.0F, 3.2976F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone32 = new ModelRenderer(this);
        bone32.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone32);
        setRotationAngle(bone32, 0.0F, -1.5708F, 0.0F);
        bone32.texOffs(30, 15).addBox(4.3007F, -1.0F, 3.2971F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone33);
        setRotationAngle(bone33, 0.0F, -1.8326F, 0.0F);
        bone33.texOffs(30, 13).addBox(5.2663F, -1.0F, 3.0376F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        bone34 = new ModelRenderer(this);
        bone34.setPos(0.0F, -1.0F, 1.0F);
        line2.addChild(bone34);
        setRotationAngle(bone34, 0.0F, -2.0944F, 0.0F);
        bone34.texOffs(30, 11).addBox(6.1329F, -1.0F, 2.537F, 0.0F, 2.0F, 1.0F, 0.0F, false);

        legLeft = new ModelRenderer(this);
        legLeft.setPos(2.0F, 15.0F, 0.0F);
        legLeft.texOffs(53, 20).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        legRight = new ModelRenderer(this);
        legRight.setPos(-2.0F, 15.0F, 0.0F);
        legRight.texOffs(24, 51).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        wingLeft = new ModelRenderer(this);
        wingLeft.setPos(1.0F, 11.0F, 4.0F);
        setRotationAngle(wingLeft, -0.0873F, 1.309F, 0.0F);
        wingLeft.texOffs(0, 24).addBox(-0.3951F, -13.0F, -0.0012F, 0.0F, 24.0F, 12.0F, 0.0F, false);

        wingRight = new ModelRenderer(this);
        wingRight.setPos(0.0F, 11.0F, 5.0F);
        setRotationAngle(wingRight, -0.0873F, -1.2217F, 0.0F);
        wingRight.texOffs(0, 0).addBox(-0.6985F, -13.0F, 0.2899F, 0.0F, 24.0F, 12.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(EntityFairy entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;
        armLeft.zRot = MathHelper.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.zRot = -MathHelper.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        if (entityIn.isOnGround()) {
            legLeft.xRot = MathHelper.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legRight.xRot = -MathHelper.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            armLeft.xRot = -MathHelper.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            armRight.xRot = MathHelper.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            wingLeft.yRot = -MathHelper.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
            wingRight.yRot = MathHelper.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        } else {
            legLeft.xRot = 0f;
            legRight.xRot = 0f;
            armLeft.xRot = -0.17453292F;
            armRight.xRot = -0.17453292F;
            head.xRot = head.xRot - 8 * 0.017453292F;
            wingLeft.yRot = -MathHelper.cos(ageInTicks * 0.5f) * 0.4f + 1.2f;
            wingRight.yRot = MathHelper.cos(ageInTicks * 0.5f) * 0.4f - 1.2f;
        }
        float remainder = ageInTicks % 60;
        // 0-10 显示眨眼贴图
        blink.visible = (55 < remainder && remainder < 60);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        armRight.render(matrixStack, buffer, packedLight, packedOverlay);
        armLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        legLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        legRight.render(matrixStack, buffer, packedLight, packedOverlay);
        wingLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        wingRight.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
