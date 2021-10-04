package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AltarModel extends EntityModel<Entity> {
    private final ModelRenderer pillar;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone10;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer pillar2;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer pillar3;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer pillar4;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;
    private final ModelRenderer bone36;
    private final ModelRenderer bone37;
    private final ModelRenderer pillar5;
    private final ModelRenderer bone38;
    private final ModelRenderer bone39;
    private final ModelRenderer bone40;
    private final ModelRenderer bone41;
    private final ModelRenderer bone42;
    private final ModelRenderer bone43;
    private final ModelRenderer bone44;
    private final ModelRenderer bone45;
    private final ModelRenderer bone46;
    private final ModelRenderer pillar6;
    private final ModelRenderer bone47;
    private final ModelRenderer bone48;
    private final ModelRenderer bone49;
    private final ModelRenderer bone50;
    private final ModelRenderer bone51;
    private final ModelRenderer bone52;
    private final ModelRenderer bone53;
    private final ModelRenderer bone54;
    private final ModelRenderer bone55;
    private final ModelRenderer bone;
    private final ModelRenderer bone56;
    private final ModelRenderer bone57;
    private final ModelRenderer bone58;
    private final ModelRenderer bone59;
    private final ModelRenderer bone60;
    private final ModelRenderer bone61;
    private final ModelRenderer bone63;
    private final ModelRenderer bone62;
    private final ModelRenderer bone64;

    public AltarModel() {
        texWidth = 256;
        texHeight = 256;

        pillar = new ModelRenderer(this);
        pillar.setPos(0.0F, 24.0F, 0.0F);
        pillar.texOffs(0, 44).addBox(48.0F, -48.0F, 16.0F, 16.0F, 48.0F, 16.0F, 0.0F, false);
        pillar.texOffs(0, 21).addBox(47.5F, -48.5F, 15.5F, 17.0F, 6.0F, 17.0F, 0.0F, false);
        pillar.texOffs(0, 0).addBox(47.5F, -33.5F, 15.5F, 17.0F, 4.0F, 17.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-4.25F, -27.5F, 14.8F);
        pillar.addChild(bone2);
        setRotationAngle(bone2, -0.1745F, 0.1745F, 0.7854F);
        bone2.texOffs(0, 0).addBox(36.9617F, -42.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone2.texOffs(0, 0).addBox(40.9617F, -38.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone2.texOffs(0, 0).addBox(44.9617F, -34.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(4.25F, -27.5F, 14.8F);
        pillar.addChild(bone3);
        setRotationAngle(bone3, -0.1745F, -0.1745F, -0.7854F);
        bone3.texOffs(0, 0).addBox(37.0311F, 38.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone3.texOffs(0, 0).addBox(33.0311F, 42.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone3.texOffs(0, 0).addBox(29.0311F, 46.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-4.25F, -27.5F, 33.2F);
        pillar.addChild(bone4);
        setRotationAngle(bone4, 0.1745F, -0.1745F, 0.7854F);
        bone4.texOffs(0, 0).addBox(36.9964F, -42.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone4.texOffs(0, 0).addBox(40.9964F, -38.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone4.texOffs(0, 0).addBox(44.9964F, -34.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(4.25F, -27.5F, 33.2F);
        pillar.addChild(bone5);
        setRotationAngle(bone5, 0.1745F, 0.1745F, -0.7854F);
        bone5.texOffs(0, 0).addBox(36.9964F, 38.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone5.texOffs(0, 0).addBox(32.9964F, 42.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone5.texOffs(0, 0).addBox(28.9964F, 46.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, 0.0F, 24.0F);
        pillar.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 1.5708F, 0.0F);


        bone6 = new ModelRenderer(this);
        bone6.setPos(-4.25F, -27.5F, -9.2F);
        bone10.addChild(bone6);
        setRotationAngle(bone6, -0.1745F, 0.1745F, 0.7854F);
        bone6.texOffs(0, 0).addBox(-11.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone6.texOffs(0, 0).addBox(-7.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone6.texOffs(0, 0).addBox(-3.7243F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(4.25F, -27.5F, -9.2F);
        bone10.addChild(bone7);
        setRotationAngle(bone7, -0.1745F, -0.1745F, -0.7854F);
        bone7.texOffs(0, 0).addBox(7.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone7.texOffs(0, 0).addBox(3.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone7.texOffs(0, 0).addBox(-0.2757F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-4.25F, -27.5F, 9.2F);
        bone10.addChild(bone8);
        setRotationAngle(bone8, 0.1745F, -0.1745F, 0.7854F);
        bone8.texOffs(0, 0).addBox(7.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone8.texOffs(0, 0).addBox(11.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone8.texOffs(0, 0).addBox(15.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(4.25F, -27.5F, 9.2F);
        bone10.addChild(bone9);
        setRotationAngle(bone9, 0.1745F, 0.1745F, -0.7854F);
        bone9.texOffs(0, 0).addBox(-11.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone9.texOffs(0, 0).addBox(-15.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone9.texOffs(0, 0).addBox(-19.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        pillar2 = new ModelRenderer(this);
        pillar2.setPos(0.0F, 24.0F, 0.0F);
        pillar2.texOffs(0, 44).addBox(48.0F, -48.0F, -32.0F, 16.0F, 48.0F, 16.0F, 0.0F, false);
        pillar2.texOffs(0, 21).addBox(47.5F, -48.5F, -32.5F, 17.0F, 6.0F, 17.0F, 0.0F, false);
        pillar2.texOffs(0, 0).addBox(47.5F, -33.5F, -32.5F, 17.0F, 4.0F, 17.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(-4.25F, -27.5F, -14.8F);
        pillar2.addChild(bone11);
        setRotationAngle(bone11, 0.1745F, -0.1745F, 0.7854F);
        bone11.texOffs(0, 0).addBox(36.9617F, -42.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone11.texOffs(0, 0).addBox(40.9617F, -38.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone11.texOffs(0, 0).addBox(44.9617F, -34.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(4.25F, -27.5F, -14.8F);
        pillar2.addChild(bone12);
        setRotationAngle(bone12, 0.1745F, 0.1745F, -0.7854F);
        bone12.texOffs(0, 0).addBox(37.0311F, 38.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone12.texOffs(0, 0).addBox(33.0311F, 42.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone12.texOffs(0, 0).addBox(29.0311F, 46.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone13 = new ModelRenderer(this);
        bone13.setPos(-4.25F, -27.5F, -33.2F);
        pillar2.addChild(bone13);
        setRotationAngle(bone13, -0.1745F, 0.1745F, 0.7854F);
        bone13.texOffs(0, 0).addBox(36.9964F, -42.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(40.9964F, -38.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(44.9964F, -34.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(4.25F, -27.5F, -33.2F);
        pillar2.addChild(bone14);
        setRotationAngle(bone14, -0.1745F, -0.1745F, -0.7854F);
        bone14.texOffs(0, 0).addBox(36.9964F, 38.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone14.texOffs(0, 0).addBox(32.9964F, 42.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone14.texOffs(0, 0).addBox(28.9964F, 46.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, 0.0F, -24.0F);
        pillar2.addChild(bone15);
        setRotationAngle(bone15, 0.0F, -1.5708F, 0.0F);


        bone16 = new ModelRenderer(this);
        bone16.setPos(-4.25F, -27.5F, 9.2F);
        bone15.addChild(bone16);
        setRotationAngle(bone16, 0.1745F, -0.1745F, 0.7854F);
        bone16.texOffs(0, 0).addBox(-11.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone16.texOffs(0, 0).addBox(-7.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone16.texOffs(0, 0).addBox(-3.7243F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(4.25F, -27.5F, 9.2F);
        bone15.addChild(bone17);
        setRotationAngle(bone17, 0.1745F, 0.1745F, -0.7854F);
        bone17.texOffs(0, 0).addBox(7.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone17.texOffs(0, 0).addBox(3.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone17.texOffs(0, 0).addBox(-0.2757F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone18 = new ModelRenderer(this);
        bone18.setPos(-4.25F, -27.5F, -9.2F);
        bone15.addChild(bone18);
        setRotationAngle(bone18, -0.1745F, 0.1745F, 0.7854F);
        bone18.texOffs(0, 0).addBox(7.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone18.texOffs(0, 0).addBox(11.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone18.texOffs(0, 0).addBox(15.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(4.25F, -27.5F, -9.2F);
        bone15.addChild(bone19);
        setRotationAngle(bone19, -0.1745F, -0.1745F, -0.7854F);
        bone19.texOffs(0, 0).addBox(-11.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone19.texOffs(0, 0).addBox(-15.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone19.texOffs(0, 0).addBox(-19.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        pillar3 = new ModelRenderer(this);
        pillar3.setPos(0.0F, 24.0F, 0.0F);
        pillar3.texOffs(0, 44).addBox(-64.0F, -48.0F, 16.0F, 16.0F, 48.0F, 16.0F, 0.0F, true);
        pillar3.texOffs(0, 21).addBox(-64.5F, -48.5F, 15.5F, 17.0F, 6.0F, 17.0F, 0.0F, true);
        pillar3.texOffs(0, 0).addBox(-64.5F, -33.5F, 15.5F, 17.0F, 4.0F, 17.0F, 0.0F, true);

        bone20 = new ModelRenderer(this);
        bone20.setPos(4.25F, -27.5F, 14.8F);
        pillar3.addChild(bone20);
        setRotationAngle(bone20, -0.1745F, -0.1745F, -0.7854F);
        bone20.texOffs(0, 0).addBox(-40.9617F, -42.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone20.texOffs(0, 0).addBox(-44.9617F, -38.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone20.texOffs(0, 0).addBox(-48.9617F, -34.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone21 = new ModelRenderer(this);
        bone21.setPos(-4.25F, -27.5F, 14.8F);
        pillar3.addChild(bone21);
        setRotationAngle(bone21, -0.1745F, 0.1745F, 0.7854F);
        bone21.texOffs(0, 0).addBox(-41.0311F, 38.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone21.texOffs(0, 0).addBox(-37.0311F, 42.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone21.texOffs(0, 0).addBox(-33.0311F, 46.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(4.25F, -27.5F, 33.2F);
        pillar3.addChild(bone22);
        setRotationAngle(bone22, 0.1745F, 0.1745F, -0.7854F);
        bone22.texOffs(0, 0).addBox(-40.9964F, -42.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone22.texOffs(0, 0).addBox(-44.9964F, -38.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone22.texOffs(0, 0).addBox(-48.9964F, -34.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone23 = new ModelRenderer(this);
        bone23.setPos(-4.25F, -27.5F, 33.2F);
        pillar3.addChild(bone23);
        setRotationAngle(bone23, 0.1745F, -0.1745F, 0.7854F);
        bone23.texOffs(0, 0).addBox(-40.9964F, 38.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone23.texOffs(0, 0).addBox(-36.9964F, 42.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone23.texOffs(0, 0).addBox(-32.9964F, 46.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, 0.0F, 24.0F);
        pillar3.addChild(bone24);
        setRotationAngle(bone24, 0.0F, -1.5708F, 0.0F);


        bone25 = new ModelRenderer(this);
        bone25.setPos(4.25F, -27.5F, -9.2F);
        bone24.addChild(bone25);
        setRotationAngle(bone25, -0.1745F, -0.1745F, -0.7854F);
        bone25.texOffs(0, 0).addBox(7.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone25.texOffs(0, 0).addBox(3.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone25.texOffs(0, 0).addBox(-0.2757F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone26 = new ModelRenderer(this);
        bone26.setPos(-4.25F, -27.5F, -9.2F);
        bone24.addChild(bone26);
        setRotationAngle(bone26, -0.1745F, 0.1745F, 0.7854F);
        bone26.texOffs(0, 0).addBox(-11.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone26.texOffs(0, 0).addBox(-7.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone26.texOffs(0, 0).addBox(-3.7243F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(4.25F, -27.5F, 9.2F);
        bone24.addChild(bone27);
        setRotationAngle(bone27, 0.1745F, 0.1745F, -0.7854F);
        bone27.texOffs(0, 0).addBox(-11.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone27.texOffs(0, 0).addBox(-15.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone27.texOffs(0, 0).addBox(-19.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone28 = new ModelRenderer(this);
        bone28.setPos(-4.25F, -27.5F, 9.2F);
        bone24.addChild(bone28);
        setRotationAngle(bone28, 0.1745F, -0.1745F, 0.7854F);
        bone28.texOffs(0, 0).addBox(7.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone28.texOffs(0, 0).addBox(11.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone28.texOffs(0, 0).addBox(15.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        pillar4 = new ModelRenderer(this);
        pillar4.setPos(0.0F, 24.0F, 0.0F);
        pillar4.texOffs(0, 44).addBox(-64.0F, -48.0F, -32.0F, 16.0F, 48.0F, 16.0F, 0.0F, true);
        pillar4.texOffs(0, 21).addBox(-64.5F, -48.5F, -32.5F, 17.0F, 6.0F, 17.0F, 0.0F, true);
        pillar4.texOffs(0, 0).addBox(-64.5F, -33.5F, -32.5F, 17.0F, 4.0F, 17.0F, 0.0F, true);

        bone29 = new ModelRenderer(this);
        bone29.setPos(4.25F, -27.5F, -14.8F);
        pillar4.addChild(bone29);
        setRotationAngle(bone29, 0.1745F, 0.1745F, -0.7854F);
        bone29.texOffs(0, 0).addBox(-40.9617F, -42.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone29.texOffs(0, 0).addBox(-44.9617F, -38.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone29.texOffs(0, 0).addBox(-48.9617F, -34.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone30 = new ModelRenderer(this);
        bone30.setPos(-4.25F, -27.5F, -14.8F);
        pillar4.addChild(bone30);
        setRotationAngle(bone30, 0.1745F, -0.1745F, 0.7854F);
        bone30.texOffs(0, 0).addBox(-41.0311F, 38.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone30.texOffs(0, 0).addBox(-37.0311F, 42.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone30.texOffs(0, 0).addBox(-33.0311F, 46.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(4.25F, -27.5F, -33.2F);
        pillar4.addChild(bone31);
        setRotationAngle(bone31, -0.1745F, -0.1745F, -0.7854F);
        bone31.texOffs(0, 0).addBox(-40.9964F, -42.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone31.texOffs(0, 0).addBox(-44.9964F, -38.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone31.texOffs(0, 0).addBox(-48.9964F, -34.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone32 = new ModelRenderer(this);
        bone32.setPos(-4.25F, -27.5F, -33.2F);
        pillar4.addChild(bone32);
        setRotationAngle(bone32, -0.1745F, 0.1745F, 0.7854F);
        bone32.texOffs(0, 0).addBox(-40.9964F, 38.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone32.texOffs(0, 0).addBox(-36.9964F, 42.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone32.texOffs(0, 0).addBox(-32.9964F, 46.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.0F, 0.0F, -24.0F);
        pillar4.addChild(bone33);
        setRotationAngle(bone33, 0.0F, 1.5708F, 0.0F);


        bone34 = new ModelRenderer(this);
        bone34.setPos(4.25F, -27.5F, 9.2F);
        bone33.addChild(bone34);
        setRotationAngle(bone34, 0.1745F, 0.1745F, -0.7854F);
        bone34.texOffs(0, 0).addBox(7.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone34.texOffs(0, 0).addBox(3.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone34.texOffs(0, 0).addBox(-0.2757F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone35 = new ModelRenderer(this);
        bone35.setPos(-4.25F, -27.5F, 9.2F);
        bone33.addChild(bone35);
        setRotationAngle(bone35, 0.1745F, -0.1745F, 0.7854F);
        bone35.texOffs(0, 0).addBox(-11.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone35.texOffs(0, 0).addBox(-7.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone35.texOffs(0, 0).addBox(-3.7243F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone36 = new ModelRenderer(this);
        bone36.setPos(4.25F, -27.5F, -9.2F);
        bone33.addChild(bone36);
        setRotationAngle(bone36, -0.1745F, -0.1745F, -0.7854F);
        bone36.texOffs(0, 0).addBox(-11.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone36.texOffs(0, 0).addBox(-15.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone36.texOffs(0, 0).addBox(-19.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone37 = new ModelRenderer(this);
        bone37.setPos(-4.25F, -27.5F, -9.2F);
        bone33.addChild(bone37);
        setRotationAngle(bone37, -0.1745F, 0.1745F, 0.7854F);
        bone37.texOffs(0, 0).addBox(7.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone37.texOffs(0, 0).addBox(11.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone37.texOffs(0, 0).addBox(15.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        pillar5 = new ModelRenderer(this);
        pillar5.setPos(0.0F, 24.0F, 0.0F);
        pillar5.texOffs(0, 44).addBox(-32.0F, -48.0F, 48.0F, 16.0F, 48.0F, 16.0F, 0.0F, true);
        pillar5.texOffs(0, 21).addBox(-32.5F, -48.5F, 47.5F, 17.0F, 6.0F, 17.0F, 0.0F, true);
        pillar5.texOffs(0, 0).addBox(-32.5F, -33.5F, 47.5F, 17.0F, 4.0F, 17.0F, 0.0F, true);

        bone38 = new ModelRenderer(this);
        bone38.setPos(4.25F, -27.5F, 14.8F);
        pillar5.addChild(bone38);
        setRotationAngle(bone38, -0.1745F, -0.1745F, -0.7854F);
        bone38.texOffs(0, 0).addBox(-13.1213F, -24.731F, 31.1903F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone38.texOffs(0, 0).addBox(-17.1213F, -20.731F, 31.1903F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone38.texOffs(0, 0).addBox(-21.1213F, -16.731F, 31.1903F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone39 = new ModelRenderer(this);
        bone39.setPos(-4.25F, -27.5F, 14.8F);
        pillar5.addChild(bone39);
        setRotationAngle(bone39, -0.1745F, 0.1745F, 0.7854F);
        bone39.texOffs(0, 0).addBox(-24.3042F, 9.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone39.texOffs(0, 0).addBox(-20.3042F, 13.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone39.texOffs(0, 0).addBox(-16.3042F, 17.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone40 = new ModelRenderer(this);
        bone40.setPos(4.25F, -27.5F, 33.2F);
        pillar5.addChild(bone40);
        setRotationAngle(bone40, 0.1745F, 0.1745F, -0.7854F);
        bone40.texOffs(0, 0).addBox(-24.2695F, -13.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone40.texOffs(0, 0).addBox(-28.2695F, -9.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone40.texOffs(0, 0).addBox(-32.2695F, -5.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone41 = new ModelRenderer(this);
        bone41.setPos(-4.25F, -27.5F, 33.2F);
        pillar5.addChild(bone41);
        setRotationAngle(bone41, 0.1745F, -0.1745F, 0.7854F);
        bone41.texOffs(0, 0).addBox(-13.156F, 20.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone41.texOffs(0, 0).addBox(-9.156F, 24.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone41.texOffs(0, 0).addBox(-5.156F, 28.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone42 = new ModelRenderer(this);
        bone42.setPos(0.0F, 0.0F, 24.0F);
        pillar5.addChild(bone42);
        setRotationAngle(bone42, 0.0F, -1.5708F, 0.0F);


        bone43 = new ModelRenderer(this);
        bone43.setPos(4.25F, -27.5F, -9.2F);
        bone42.addChild(bone43);
        setRotationAngle(bone43, -0.1745F, -0.1745F, -0.7854F);
        bone43.texOffs(0, 0).addBox(24.4512F, 16.8617F, 23.536F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone43.texOffs(0, 0).addBox(20.4512F, 20.8617F, 23.536F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone43.texOffs(0, 0).addBox(16.4512F, 24.8617F, 23.536F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone44 = new ModelRenderer(this);
        bone44.setPos(-4.25F, -27.5F, -9.2F);
        bone42.addChild(bone44);
        setRotationAngle(bone44, -0.1745F, 0.1745F, 0.7854F);
        bone44.texOffs(0, 0).addBox(16.1161F, -29.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone44.texOffs(0, 0).addBox(20.1161F, -25.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone44.texOffs(0, 0).addBox(24.1161F, -21.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone45 = new ModelRenderer(this);
        bone45.setPos(4.25F, -27.5F, 9.2F);
        bone42.addChild(bone45);
        setRotationAngle(bone45, 0.1745F, 0.1745F, -0.7854F);
        bone45.texOffs(0, 0).addBox(16.1161F, 25.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone45.texOffs(0, 0).addBox(12.1161F, 29.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone45.texOffs(0, 0).addBox(8.1161F, 33.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone46 = new ModelRenderer(this);
        bone46.setPos(-4.25F, -27.5F, 9.2F);
        bone42.addChild(bone46);
        setRotationAngle(bone46, 0.1745F, -0.1745F, 0.7854F);
        bone46.texOffs(0, 0).addBox(24.4512F, -20.8617F, 23.136F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone46.texOffs(0, 0).addBox(28.4512F, -16.8617F, 23.136F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone46.texOffs(0, 0).addBox(32.4512F, -12.8617F, 23.136F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        pillar6 = new ModelRenderer(this);
        pillar6.setPos(0.0F, 24.0F, 0.0F);
        pillar6.texOffs(0, 44).addBox(16.0F, -48.0F, 48.0F, 16.0F, 48.0F, 16.0F, 0.0F, false);
        pillar6.texOffs(0, 21).addBox(15.5F, -48.5F, 47.5F, 17.0F, 6.0F, 17.0F, 0.0F, false);
        pillar6.texOffs(0, 0).addBox(15.5F, -33.5F, 47.5F, 17.0F, 4.0F, 17.0F, 0.0F, false);

        bone47 = new ModelRenderer(this);
        bone47.setPos(-4.25F, -27.5F, 14.8F);
        pillar6.addChild(bone47);
        setRotationAngle(bone47, -0.1745F, 0.1745F, 0.7854F);
        bone47.texOffs(0, 0).addBox(9.1213F, -24.731F, 31.1903F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone47.texOffs(0, 0).addBox(13.1213F, -20.731F, 31.1903F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone47.texOffs(0, 0).addBox(17.1213F, -16.731F, 31.1903F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone48 = new ModelRenderer(this);
        bone48.setPos(4.25F, -27.5F, 14.8F);
        pillar6.addChild(bone48);
        setRotationAngle(bone48, -0.1745F, -0.1745F, -0.7854F);
        bone48.texOffs(0, 0).addBox(20.3042F, 9.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone48.texOffs(0, 0).addBox(16.3042F, 13.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone48.texOffs(0, 0).addBox(12.3042F, 17.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone49 = new ModelRenderer(this);
        bone49.setPos(-4.25F, -27.5F, 33.2F);
        pillar6.addChild(bone49);
        setRotationAngle(bone49, 0.1745F, -0.1745F, 0.7854F);
        bone49.texOffs(0, 0).addBox(20.2695F, -13.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone49.texOffs(0, 0).addBox(24.2695F, -9.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone49.texOffs(0, 0).addBox(28.2695F, -5.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone50 = new ModelRenderer(this);
        bone50.setPos(4.25F, -27.5F, 33.2F);
        pillar6.addChild(bone50);
        setRotationAngle(bone50, 0.1745F, 0.1745F, -0.7854F);
        bone50.texOffs(0, 0).addBox(9.156F, 20.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone50.texOffs(0, 0).addBox(5.156F, 24.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone50.texOffs(0, 0).addBox(1.156F, 28.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone51 = new ModelRenderer(this);
        bone51.setPos(0.0F, 0.0F, 24.0F);
        pillar6.addChild(bone51);
        setRotationAngle(bone51, 0.0F, 1.5708F, 0.0F);


        bone52 = new ModelRenderer(this);
        bone52.setPos(-4.25F, -27.5F, -9.2F);
        bone51.addChild(bone52);
        setRotationAngle(bone52, -0.1745F, 0.1745F, 0.7854F);
        bone52.texOffs(0, 0).addBox(-28.4512F, 16.8617F, 23.536F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone52.texOffs(0, 0).addBox(-24.4512F, 20.8617F, 23.536F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone52.texOffs(0, 0).addBox(-20.4512F, 24.8617F, 23.536F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone53 = new ModelRenderer(this);
        bone53.setPos(4.25F, -27.5F, -9.2F);
        bone51.addChild(bone53);
        setRotationAngle(bone53, -0.1745F, -0.1745F, -0.7854F);
        bone53.texOffs(0, 0).addBox(-20.1161F, -29.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone53.texOffs(0, 0).addBox(-24.1161F, -25.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone53.texOffs(0, 0).addBox(-28.1161F, -21.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone54 = new ModelRenderer(this);
        bone54.setPos(-4.25F, -27.5F, 9.2F);
        bone51.addChild(bone54);
        setRotationAngle(bone54, 0.1745F, -0.1745F, 0.7854F);
        bone54.texOffs(0, 0).addBox(-20.1161F, 25.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone54.texOffs(0, 0).addBox(-16.1161F, 29.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        bone54.texOffs(0, 0).addBox(-12.1161F, 33.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, 0.0F, false);

        bone55 = new ModelRenderer(this);
        bone55.setPos(4.25F, -27.5F, 9.2F);
        bone51.addChild(bone55);
        setRotationAngle(bone55, 0.1745F, 0.1745F, -0.7854F);
        bone55.texOffs(0, 0).addBox(-28.4512F, -20.8617F, 23.136F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone55.texOffs(0, 0).addBox(-32.4512F, -16.8617F, 23.136F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        bone55.texOffs(0, 0).addBox(-36.4512F, -12.8617F, 23.136F, 4.0F, 4.0F, 0.0F, 0.0F, true);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 24.0F, 0.0F);
        bone.texOffs(64, 39).addBox(-24.0F, -1.0F, -24.0F, 48.0F, 0.0F, 48.0F, 0.0F, false);

        bone56 = new ModelRenderer(this);
        bone56.setPos(0.0F, 24.0F, 0.0F);
        bone56.texOffs(150, 227).addBox(-32.0F, -12.0F, -64.0F, 16.0F, 12.0F, 16.0F, 0.0F, false);
        bone56.texOffs(150, 227).addBox(16.0F, -12.0F, -64.0F, 16.0F, 12.0F, 16.0F, 0.0F, true);
        bone56.texOffs(0, 236).addBox(-60.0F, -101.0F, -58.0F, 120.0F, 6.0F, 4.0F, 0.0F, false);
        bone56.texOffs(2, 176).addBox(-54.5F, -95.0F, -64.0F, 109.0F, 3.0F, 16.0F, 0.0F, false);
        bone56.texOffs(0, 186).addBox(-45.0F, -66.3F, -57.0F, 90.0F, 8.0F, 2.0F, 0.0F, false);
        bone56.texOffs(192, 171).addBox(-32.0F, -42.0F, -64.0F, 16.0F, 30.0F, 16.0F, 0.0F, false);
        bone56.texOffs(192, 171).addBox(16.0F, -42.0F, -64.0F, 16.0F, 30.0F, 16.0F, 0.0F, true);
        bone56.texOffs(153, 188).addBox(16.5F, -55.0F, -63.5F, 15.0F, 13.0F, 15.0F, 0.0F, true);
        bone56.texOffs(153, 188).addBox(-31.5F, -55.0F, -63.5F, 15.0F, 13.0F, 15.0F, 0.0F, false);
        bone56.texOffs(49, 184).addBox(17.0F, -73.0F, -63.0F, 14.0F, 18.0F, 14.0F, 0.0F, true);
        bone56.texOffs(49, 184).addBox(-31.0F, -73.0F, -63.0F, 14.0F, 18.0F, 14.0F, 0.0F, false);
        bone56.texOffs(30, 173).addBox(17.5F, -85.0F, -62.5F, 13.0F, 12.0F, 13.0F, 0.0F, true);
        bone56.texOffs(30, 173).addBox(-30.5F, -85.0F, -62.5F, 13.0F, 12.0F, 13.0F, 0.0F, false);
        bone56.texOffs(0, 192).addBox(17.0F, -92.0F, -63.0F, 14.0F, 7.0F, 14.0F, 0.0F, true);
        bone56.texOffs(0, 192).addBox(-31.0F, -92.0F, -63.0F, 14.0F, 7.0F, 14.0F, 0.0F, false);
        bone56.texOffs(28, 208).addBox(-47.0F, -68.3F, -59.0F, 94.0F, 2.0F, 6.0F, 0.0F, false);
        bone56.texOffs(0, 180).addBox(-50.5F, -92.0F, -60.0F, 101.0F, 5.0F, 8.0F, 0.0F, false);

        bone57 = new ModelRenderer(this);
        bone57.setPos(0.0F, -98.0F, -56.5F);
        bone56.addChild(bone57);
        setRotationAngle(bone57, 0.2618F, 0.0F, 0.0F);
        bone57.texOffs(0, 228).addBox(-55.5F, -1.0F, -14.5F, 111.0F, 2.0F, 14.0F, 0.0F, false);

        bone58 = new ModelRenderer(this);
        bone58.setPos(0.0F, -98.0F, -55.5F);
        bone56.addChild(bone58);
        setRotationAngle(bone58, -0.2618F, 0.0F, 0.0F);
        bone58.texOffs(0, 222).addBox(-55.5F, -1.0F, 0.5F, 111.0F, 2.0F, 14.0F, 0.0F, false);

        bone59 = new ModelRenderer(this);
        bone59.setPos(0.0F, -67.3F, -59.25F);
        bone56.addChild(bone59);
        setRotationAngle(bone59, 0.0873F, 0.0F, 0.0F);
        bone59.texOffs(224, 234).addBox(-6.0F, -20.0F, -1.0F, 12.0F, 20.0F, 2.0F, 0.0F, false);
        bone59.texOffs(230, 7).addBox(-4.5F, -18.5F, -1.7F, 9.0F, 17.0F, 2.0F, 0.0F, false);

        bone60 = new ModelRenderer(this);
        bone60.setPos(0.0F, -67.3F, -52.75F);
        bone56.addChild(bone60);
        setRotationAngle(bone60, -0.0873F, 0.0F, 0.0F);
        bone60.texOffs(224, 234).addBox(-6.0F, -20.0F, -1.0F, 12.0F, 20.0F, 2.0F, 0.0F, false);
        bone60.texOffs(230, 7).addBox(-4.5F, -18.5F, -0.25F, 9.0F, 17.0F, 2.0F, 0.0F, false);

        bone61 = new ModelRenderer(this);
        bone61.setPos(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone61);
        bone61.texOffs(16, 243).addBox(35.25F, -8.0F, -58.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);
        bone61.texOffs(5, 178).addBox(35.25F, -27.0F, -58.0F, 4.0F, 19.0F, 4.0F, 0.0F, true);
        bone61.texOffs(5, 180).addBox(32.0F, -24.0F, -57.0F, 9.0F, 2.0F, 2.0F, 0.0F, true);
        bone61.texOffs(217, 178).addBox(32.0F, -31.0F, -58.5F, 12.0F, 4.0F, 5.0F, 0.0F, true);

        bone63 = new ModelRenderer(this);
        bone63.setPos(38.0F, -31.75F, -56.0F);
        bone61.addChild(bone63);
        setRotationAngle(bone63, 0.0F, 0.0F, 0.0873F);
        bone63.texOffs(204, 236).addBox(-6.0F, -0.5F, -3.0F, 14.0F, 2.0F, 6.0F, 0.0F, true);

        bone62 = new ModelRenderer(this);
        bone62.setPos(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone62);
        bone62.texOffs(16, 243).addBox(-39.25F, -8.0F, -58.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        bone62.texOffs(5, 178).addBox(-39.25F, -27.0F, -58.0F, 4.0F, 19.0F, 4.0F, 0.0F, false);
        bone62.texOffs(5, 180).addBox(-41.0F, -24.0F, -57.0F, 9.0F, 2.0F, 2.0F, 0.0F, false);
        bone62.texOffs(217, 178).addBox(-44.0F, -31.0F, -58.5F, 12.0F, 4.0F, 5.0F, 0.0F, false);

        bone64 = new ModelRenderer(this);
        bone64.setPos(-38.0F, -31.75F, -56.0F);
        bone62.addChild(bone64);
        setRotationAngle(bone64, 0.0F, 0.0F, -0.0873F);
        bone64.texOffs(204, 236).addBox(-8.0F, -0.5F, -3.0F, 14.0F, 2.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        pillar.render(matrixStack, buffer, packedLight, packedOverlay);
        pillar2.render(matrixStack, buffer, packedLight, packedOverlay);
        pillar3.render(matrixStack, buffer, packedLight, packedOverlay);
        pillar4.render(matrixStack, buffer, packedLight, packedOverlay);
        pillar5.render(matrixStack, buffer, packedLight, packedOverlay);
        pillar6.render(matrixStack, buffer, packedLight, packedOverlay);
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
        bone56.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}