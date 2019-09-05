package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2019/8/31 22:04
 **/
public class AltarModel extends ModelBase {
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
        textureWidth = 256;
        textureHeight = 256;

        pillar = new ModelRenderer(this);
        pillar.setRotationPoint(0.0F, 24.0F, 0.0F);
        pillar.cubeList.add(new ModelBox(pillar, 0, 44, 48.0F, -48.0F, 16.0F, 16, 48, 16, 0.0F, false));
        pillar.cubeList.add(new ModelBox(pillar, 0, 21, 47.5F, -48.5F, 15.5F, 17, 6, 17, 0.0F, false));
        pillar.cubeList.add(new ModelBox(pillar, 0, 0, 47.5F, -33.5F, 15.5F, 17, 4, 17, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(-4.25F, -27.5F, 14.8F);
        setRotationAngle(bone2, -0.1745F, 0.1745F, 0.7854F);
        pillar.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, 36.9617F, -42.2246F, 0.0955F, 4, 4, 0, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, 40.9617F, -38.2246F, 0.0955F, 4, 4, 0, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, 44.9617F, -34.2246F, 0.0955F, 4, 4, 0, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(4.25F, -27.5F, 14.8F);
        setRotationAngle(bone3, -0.1745F, -0.1745F, -0.7854F);
        pillar.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 37.0311F, 38.1562F, 0.3045F, 4, 4, 0, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 33.0311F, 42.1562F, 0.3045F, 4, 4, 0, 0.0F, true));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 29.0311F, 46.1562F, 0.3045F, 4, 4, 0, 0.0F, true));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(-4.25F, -27.5F, 33.2F);
        setRotationAngle(bone4, 0.1745F, -0.1745F, 0.7854F);
        pillar.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, 36.9964F, -42.1904F, -0.0955F, 4, 4, 0, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, 40.9964F, -38.1904F, -0.0955F, 4, 4, 0, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, 44.9964F, -34.1904F, -0.0955F, 4, 4, 0, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(4.25F, -27.5F, 33.2F);
        setRotationAngle(bone5, 0.1745F, 0.1745F, -0.7854F);
        pillar.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, 36.9964F, 38.1904F, -0.3045F, 4, 4, 0, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, 32.9964F, 42.1904F, -0.3045F, 4, 4, 0, 0.0F, true));
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, 28.9964F, 46.1904F, -0.3045F, 4, 4, 0, 0.0F, true));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 24.0F);
        setRotationAngle(bone10, 0.0F, 1.5708F, 0.0F);
        pillar.addChild(bone10);

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(-4.25F, -27.5F, -9.2F);
        setRotationAngle(bone6, -0.1745F, 0.1745F, 0.7854F);
        bone10.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 0, 0, -11.7243F, -11.5766F, 54.5114F, 4, 4, 0, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 0, 0, -7.7243F, -7.5766F, 54.5114F, 4, 4, 0, 0.0F, false));
        bone6.cubeList.add(new ModelBox(bone6, 0, 0, -3.7243F, -3.5766F, 54.5114F, 4, 4, 0, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(4.25F, -27.5F, -9.2F);
        setRotationAngle(bone7, -0.1745F, -0.1745F, -0.7854F);
        bone10.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 0, 0, 7.7243F, -11.5766F, 54.5114F, 4, 4, 0, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 0, 3.7243F, -7.5766F, 54.5114F, 4, 4, 0, 0.0F, true));
        bone7.cubeList.add(new ModelBox(bone7, 0, 0, -0.2757F, -3.5766F, 54.5114F, 4, 4, 0, 0.0F, true));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(-4.25F, -27.5F, 9.2F);
        setRotationAngle(bone8, 0.1745F, -0.1745F, 0.7854F);
        bone10.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 0, 0, 7.7243F, 7.5766F, 54.1114F, 4, 4, 0, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 0, 0, 11.7243F, 11.5766F, 54.1114F, 4, 4, 0, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 0, 0, 15.7243F, 15.5766F, 54.1114F, 4, 4, 0, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(4.25F, -27.5F, 9.2F);
        setRotationAngle(bone9, 0.1745F, 0.1745F, -0.7854F);
        bone10.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 0, 0, -11.7243F, 7.5766F, 54.1114F, 4, 4, 0, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 0, -15.7243F, 11.5766F, 54.1114F, 4, 4, 0, 0.0F, true));
        bone9.cubeList.add(new ModelBox(bone9, 0, 0, -19.7243F, 15.5766F, 54.1114F, 4, 4, 0, 0.0F, true));

        pillar2 = new ModelRenderer(this);
        pillar2.setRotationPoint(0.0F, 24.0F, 0.0F);
        pillar2.cubeList.add(new ModelBox(pillar2, 0, 44, 48.0F, -48.0F, -32.0F, 16, 48, 16, 0.0F, false));
        pillar2.cubeList.add(new ModelBox(pillar2, 0, 21, 47.5F, -48.5F, -32.5F, 17, 6, 17, 0.0F, false));
        pillar2.cubeList.add(new ModelBox(pillar2, 0, 0, 47.5F, -33.5F, -32.5F, 17, 4, 17, 0.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(-4.25F, -27.5F, -14.8F);
        setRotationAngle(bone11, 0.1745F, -0.1745F, 0.7854F);
        pillar2.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 0, 0, 36.9617F, -42.2246F, -0.0955F, 4, 4, 0, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 0, 0, 40.9617F, -38.2246F, -0.0955F, 4, 4, 0, 0.0F, false));
        bone11.cubeList.add(new ModelBox(bone11, 0, 0, 44.9617F, -34.2246F, -0.0955F, 4, 4, 0, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(4.25F, -27.5F, -14.8F);
        setRotationAngle(bone12, 0.1745F, 0.1745F, -0.7854F);
        pillar2.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 0, 0, 37.0311F, 38.1562F, -0.3045F, 4, 4, 0, 0.0F, true));
        bone12.cubeList.add(new ModelBox(bone12, 0, 0, 33.0311F, 42.1562F, -0.3045F, 4, 4, 0, 0.0F, true));
        bone12.cubeList.add(new ModelBox(bone12, 0, 0, 29.0311F, 46.1562F, -0.3045F, 4, 4, 0, 0.0F, true));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-4.25F, -27.5F, -33.2F);
        setRotationAngle(bone13, -0.1745F, 0.1745F, 0.7854F);
        pillar2.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, 36.9964F, -42.1904F, 0.0955F, 4, 4, 0, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, 40.9964F, -38.1904F, 0.0955F, 4, 4, 0, 0.0F, false));
        bone13.cubeList.add(new ModelBox(bone13, 0, 0, 44.9964F, -34.1904F, 0.0955F, 4, 4, 0, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(4.25F, -27.5F, -33.2F);
        setRotationAngle(bone14, -0.1745F, -0.1745F, -0.7854F);
        pillar2.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 0, 0, 36.9964F, 38.1904F, 0.3045F, 4, 4, 0, 0.0F, true));
        bone14.cubeList.add(new ModelBox(bone14, 0, 0, 32.9964F, 42.1904F, 0.3045F, 4, 4, 0, 0.0F, true));
        bone14.cubeList.add(new ModelBox(bone14, 0, 0, 28.9964F, 46.1904F, 0.3045F, 4, 4, 0, 0.0F, true));

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 0.0F, -24.0F);
        setRotationAngle(bone15, 0.0F, -1.5708F, 0.0F);
        pillar2.addChild(bone15);

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(-4.25F, -27.5F, 9.2F);
        setRotationAngle(bone16, 0.1745F, -0.1745F, 0.7854F);
        bone15.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 0, 0, -11.7243F, -11.5766F, -54.5114F, 4, 4, 0, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 0, 0, -7.7243F, -7.5766F, -54.5114F, 4, 4, 0, 0.0F, false));
        bone16.cubeList.add(new ModelBox(bone16, 0, 0, -3.7243F, -3.5766F, -54.5114F, 4, 4, 0, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(4.25F, -27.5F, 9.2F);
        setRotationAngle(bone17, 0.1745F, 0.1745F, -0.7854F);
        bone15.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 0, 0, 7.7243F, -11.5766F, -54.5114F, 4, 4, 0, 0.0F, true));
        bone17.cubeList.add(new ModelBox(bone17, 0, 0, 3.7243F, -7.5766F, -54.5114F, 4, 4, 0, 0.0F, true));
        bone17.cubeList.add(new ModelBox(bone17, 0, 0, -0.2757F, -3.5766F, -54.5114F, 4, 4, 0, 0.0F, true));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(-4.25F, -27.5F, -9.2F);
        setRotationAngle(bone18, -0.1745F, 0.1745F, 0.7854F);
        bone15.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 0, 0, 7.7243F, 7.5766F, -54.1114F, 4, 4, 0, 0.0F, false));
        bone18.cubeList.add(new ModelBox(bone18, 0, 0, 11.7243F, 11.5766F, -54.1114F, 4, 4, 0, 0.0F, false));
        bone18.cubeList.add(new ModelBox(bone18, 0, 0, 15.7243F, 15.5766F, -54.1114F, 4, 4, 0, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(4.25F, -27.5F, -9.2F);
        setRotationAngle(bone19, -0.1745F, -0.1745F, -0.7854F);
        bone15.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 0, 0, -11.7243F, 7.5766F, -54.1114F, 4, 4, 0, 0.0F, true));
        bone19.cubeList.add(new ModelBox(bone19, 0, 0, -15.7243F, 11.5766F, -54.1114F, 4, 4, 0, 0.0F, true));
        bone19.cubeList.add(new ModelBox(bone19, 0, 0, -19.7243F, 15.5766F, -54.1114F, 4, 4, 0, 0.0F, true));

        pillar3 = new ModelRenderer(this);
        pillar3.setRotationPoint(0.0F, 24.0F, 0.0F);
        pillar3.cubeList.add(new ModelBox(pillar3, 0, 44, -64.0F, -48.0F, 16.0F, 16, 48, 16, 0.0F, true));
        pillar3.cubeList.add(new ModelBox(pillar3, 0, 21, -64.5F, -48.5F, 15.5F, 17, 6, 17, 0.0F, true));
        pillar3.cubeList.add(new ModelBox(pillar3, 0, 0, -64.5F, -33.5F, 15.5F, 17, 4, 17, 0.0F, true));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(4.25F, -27.5F, 14.8F);
        setRotationAngle(bone20, -0.1745F, -0.1745F, -0.7854F);
        pillar3.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 0, 0, -40.9617F, -42.2246F, 0.0955F, 4, 4, 0, 0.0F, true));
        bone20.cubeList.add(new ModelBox(bone20, 0, 0, -44.9617F, -38.2246F, 0.0955F, 4, 4, 0, 0.0F, true));
        bone20.cubeList.add(new ModelBox(bone20, 0, 0, -48.9617F, -34.2246F, 0.0955F, 4, 4, 0, 0.0F, true));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(-4.25F, -27.5F, 14.8F);
        setRotationAngle(bone21, -0.1745F, 0.1745F, 0.7854F);
        pillar3.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 0, 0, -41.0311F, 38.1562F, 0.3045F, 4, 4, 0, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 0, 0, -37.0311F, 42.1562F, 0.3045F, 4, 4, 0, 0.0F, false));
        bone21.cubeList.add(new ModelBox(bone21, 0, 0, -33.0311F, 46.1562F, 0.3045F, 4, 4, 0, 0.0F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(4.25F, -27.5F, 33.2F);
        setRotationAngle(bone22, 0.1745F, 0.1745F, -0.7854F);
        pillar3.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 0, 0, -40.9964F, -42.1904F, -0.0955F, 4, 4, 0, 0.0F, true));
        bone22.cubeList.add(new ModelBox(bone22, 0, 0, -44.9964F, -38.1904F, -0.0955F, 4, 4, 0, 0.0F, true));
        bone22.cubeList.add(new ModelBox(bone22, 0, 0, -48.9964F, -34.1904F, -0.0955F, 4, 4, 0, 0.0F, true));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(-4.25F, -27.5F, 33.2F);
        setRotationAngle(bone23, 0.1745F, -0.1745F, 0.7854F);
        pillar3.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 0, 0, -40.9964F, 38.1904F, -0.3045F, 4, 4, 0, 0.0F, false));
        bone23.cubeList.add(new ModelBox(bone23, 0, 0, -36.9964F, 42.1904F, -0.3045F, 4, 4, 0, 0.0F, false));
        bone23.cubeList.add(new ModelBox(bone23, 0, 0, -32.9964F, 46.1904F, -0.3045F, 4, 4, 0, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 0.0F, 24.0F);
        setRotationAngle(bone24, 0.0F, -1.5708F, 0.0F);
        pillar3.addChild(bone24);

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(4.25F, -27.5F, -9.2F);
        setRotationAngle(bone25, -0.1745F, -0.1745F, -0.7854F);
        bone24.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 0, 0, 7.7243F, -11.5766F, 54.5114F, 4, 4, 0, 0.0F, true));
        bone25.cubeList.add(new ModelBox(bone25, 0, 0, 3.7243F, -7.5766F, 54.5114F, 4, 4, 0, 0.0F, true));
        bone25.cubeList.add(new ModelBox(bone25, 0, 0, -0.2757F, -3.5766F, 54.5114F, 4, 4, 0, 0.0F, true));

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(-4.25F, -27.5F, -9.2F);
        setRotationAngle(bone26, -0.1745F, 0.1745F, 0.7854F);
        bone24.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 0, 0, -11.7243F, -11.5766F, 54.5114F, 4, 4, 0, 0.0F, false));
        bone26.cubeList.add(new ModelBox(bone26, 0, 0, -7.7243F, -7.5766F, 54.5114F, 4, 4, 0, 0.0F, false));
        bone26.cubeList.add(new ModelBox(bone26, 0, 0, -3.7243F, -3.5766F, 54.5114F, 4, 4, 0, 0.0F, false));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(4.25F, -27.5F, 9.2F);
        setRotationAngle(bone27, 0.1745F, 0.1745F, -0.7854F);
        bone24.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 0, 0, -11.7243F, 7.5766F, 54.1114F, 4, 4, 0, 0.0F, true));
        bone27.cubeList.add(new ModelBox(bone27, 0, 0, -15.7243F, 11.5766F, 54.1114F, 4, 4, 0, 0.0F, true));
        bone27.cubeList.add(new ModelBox(bone27, 0, 0, -19.7243F, 15.5766F, 54.1114F, 4, 4, 0, 0.0F, true));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(-4.25F, -27.5F, 9.2F);
        setRotationAngle(bone28, 0.1745F, -0.1745F, 0.7854F);
        bone24.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 0, 0, 7.7243F, 7.5766F, 54.1114F, 4, 4, 0, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 0, 0, 11.7243F, 11.5766F, 54.1114F, 4, 4, 0, 0.0F, false));
        bone28.cubeList.add(new ModelBox(bone28, 0, 0, 15.7243F, 15.5766F, 54.1114F, 4, 4, 0, 0.0F, false));

        pillar4 = new ModelRenderer(this);
        pillar4.setRotationPoint(0.0F, 24.0F, 0.0F);
        pillar4.cubeList.add(new ModelBox(pillar4, 0, 44, -64.0F, -48.0F, -32.0F, 16, 48, 16, 0.0F, true));
        pillar4.cubeList.add(new ModelBox(pillar4, 0, 21, -64.5F, -48.5F, -32.5F, 17, 6, 17, 0.0F, true));
        pillar4.cubeList.add(new ModelBox(pillar4, 0, 0, -64.5F, -33.5F, -32.5F, 17, 4, 17, 0.0F, true));

        bone29 = new ModelRenderer(this);
        bone29.setRotationPoint(4.25F, -27.5F, -14.8F);
        setRotationAngle(bone29, 0.1745F, 0.1745F, -0.7854F);
        pillar4.addChild(bone29);
        bone29.cubeList.add(new ModelBox(bone29, 0, 0, -40.9617F, -42.2246F, -0.0955F, 4, 4, 0, 0.0F, true));
        bone29.cubeList.add(new ModelBox(bone29, 0, 0, -44.9617F, -38.2246F, -0.0955F, 4, 4, 0, 0.0F, true));
        bone29.cubeList.add(new ModelBox(bone29, 0, 0, -48.9617F, -34.2246F, -0.0955F, 4, 4, 0, 0.0F, true));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(-4.25F, -27.5F, -14.8F);
        setRotationAngle(bone30, 0.1745F, -0.1745F, 0.7854F);
        pillar4.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 0, 0, -41.0311F, 38.1562F, -0.3045F, 4, 4, 0, 0.0F, false));
        bone30.cubeList.add(new ModelBox(bone30, 0, 0, -37.0311F, 42.1562F, -0.3045F, 4, 4, 0, 0.0F, false));
        bone30.cubeList.add(new ModelBox(bone30, 0, 0, -33.0311F, 46.1562F, -0.3045F, 4, 4, 0, 0.0F, false));

        bone31 = new ModelRenderer(this);
        bone31.setRotationPoint(4.25F, -27.5F, -33.2F);
        setRotationAngle(bone31, -0.1745F, -0.1745F, -0.7854F);
        pillar4.addChild(bone31);
        bone31.cubeList.add(new ModelBox(bone31, 0, 0, -40.9964F, -42.1904F, 0.0955F, 4, 4, 0, 0.0F, true));
        bone31.cubeList.add(new ModelBox(bone31, 0, 0, -44.9964F, -38.1904F, 0.0955F, 4, 4, 0, 0.0F, true));
        bone31.cubeList.add(new ModelBox(bone31, 0, 0, -48.9964F, -34.1904F, 0.0955F, 4, 4, 0, 0.0F, true));

        bone32 = new ModelRenderer(this);
        bone32.setRotationPoint(-4.25F, -27.5F, -33.2F);
        setRotationAngle(bone32, -0.1745F, 0.1745F, 0.7854F);
        pillar4.addChild(bone32);
        bone32.cubeList.add(new ModelBox(bone32, 0, 0, -40.9964F, 38.1904F, 0.3045F, 4, 4, 0, 0.0F, false));
        bone32.cubeList.add(new ModelBox(bone32, 0, 0, -36.9964F, 42.1904F, 0.3045F, 4, 4, 0, 0.0F, false));
        bone32.cubeList.add(new ModelBox(bone32, 0, 0, -32.9964F, 46.1904F, 0.3045F, 4, 4, 0, 0.0F, false));

        bone33 = new ModelRenderer(this);
        bone33.setRotationPoint(0.0F, 0.0F, -24.0F);
        setRotationAngle(bone33, 0.0F, 1.5708F, 0.0F);
        pillar4.addChild(bone33);

        bone34 = new ModelRenderer(this);
        bone34.setRotationPoint(4.25F, -27.5F, 9.2F);
        setRotationAngle(bone34, 0.1745F, 0.1745F, -0.7854F);
        bone33.addChild(bone34);
        bone34.cubeList.add(new ModelBox(bone34, 0, 0, 7.7243F, -11.5766F, -54.5114F, 4, 4, 0, 0.0F, true));
        bone34.cubeList.add(new ModelBox(bone34, 0, 0, 3.7243F, -7.5766F, -54.5114F, 4, 4, 0, 0.0F, true));
        bone34.cubeList.add(new ModelBox(bone34, 0, 0, -0.2757F, -3.5766F, -54.5114F, 4, 4, 0, 0.0F, true));

        bone35 = new ModelRenderer(this);
        bone35.setRotationPoint(-4.25F, -27.5F, 9.2F);
        setRotationAngle(bone35, 0.1745F, -0.1745F, 0.7854F);
        bone33.addChild(bone35);
        bone35.cubeList.add(new ModelBox(bone35, 0, 0, -11.7243F, -11.5766F, -54.5114F, 4, 4, 0, 0.0F, false));
        bone35.cubeList.add(new ModelBox(bone35, 0, 0, -7.7243F, -7.5766F, -54.5114F, 4, 4, 0, 0.0F, false));
        bone35.cubeList.add(new ModelBox(bone35, 0, 0, -3.7243F, -3.5766F, -54.5114F, 4, 4, 0, 0.0F, false));

        bone36 = new ModelRenderer(this);
        bone36.setRotationPoint(4.25F, -27.5F, -9.2F);
        setRotationAngle(bone36, -0.1745F, -0.1745F, -0.7854F);
        bone33.addChild(bone36);
        bone36.cubeList.add(new ModelBox(bone36, 0, 0, -11.7243F, 7.5766F, -54.1114F, 4, 4, 0, 0.0F, true));
        bone36.cubeList.add(new ModelBox(bone36, 0, 0, -15.7243F, 11.5766F, -54.1114F, 4, 4, 0, 0.0F, true));
        bone36.cubeList.add(new ModelBox(bone36, 0, 0, -19.7243F, 15.5766F, -54.1114F, 4, 4, 0, 0.0F, true));

        bone37 = new ModelRenderer(this);
        bone37.setRotationPoint(-4.25F, -27.5F, -9.2F);
        setRotationAngle(bone37, -0.1745F, 0.1745F, 0.7854F);
        bone33.addChild(bone37);
        bone37.cubeList.add(new ModelBox(bone37, 0, 0, 7.7243F, 7.5766F, -54.1114F, 4, 4, 0, 0.0F, false));
        bone37.cubeList.add(new ModelBox(bone37, 0, 0, 11.7243F, 11.5766F, -54.1114F, 4, 4, 0, 0.0F, false));
        bone37.cubeList.add(new ModelBox(bone37, 0, 0, 15.7243F, 15.5766F, -54.1114F, 4, 4, 0, 0.0F, false));

        pillar5 = new ModelRenderer(this);
        pillar5.setRotationPoint(0.0F, 24.0F, 0.0F);
        pillar5.cubeList.add(new ModelBox(pillar5, 0, 44, -32.0F, -48.0F, 48.0F, 16, 48, 16, 0.0F, true));
        pillar5.cubeList.add(new ModelBox(pillar5, 0, 21, -32.5F, -48.5F, 47.5F, 17, 6, 17, 0.0F, true));
        pillar5.cubeList.add(new ModelBox(pillar5, 0, 0, -32.5F, -33.5F, 47.5F, 17, 4, 17, 0.0F, true));

        bone38 = new ModelRenderer(this);
        bone38.setRotationPoint(4.25F, -27.5F, 14.8F);
        setRotationAngle(bone38, -0.1745F, -0.1745F, -0.7854F);
        pillar5.addChild(bone38);
        bone38.cubeList.add(new ModelBox(bone38, 0, 0, -13.1213F, -24.731F, 31.1903F, 4, 4, 0, 0.0F, true));
        bone38.cubeList.add(new ModelBox(bone38, 0, 0, -17.1213F, -20.731F, 31.1903F, 4, 4, 0, 0.0F, true));
        bone38.cubeList.add(new ModelBox(bone38, 0, 0, -21.1213F, -16.731F, 31.1903F, 4, 4, 0, 0.0F, true));

        bone39 = new ModelRenderer(this);
        bone39.setRotationPoint(-4.25F, -27.5F, 14.8F);
        setRotationAngle(bone39, -0.1745F, 0.1745F, 0.7854F);
        pillar5.addChild(bone39);
        bone39.cubeList.add(new ModelBox(bone39, 0, 0, -24.3042F, 9.7179F, 31.2799F, 4, 4, 0, 0.0F, false));
        bone39.cubeList.add(new ModelBox(bone39, 0, 0, -20.3042F, 13.7179F, 31.2799F, 4, 4, 0, 0.0F, false));
        bone39.cubeList.add(new ModelBox(bone39, 0, 0, -16.3042F, 17.7179F, 31.2799F, 4, 4, 0, 0.0F, false));

        bone40 = new ModelRenderer(this);
        bone40.setRotationPoint(4.25F, -27.5F, 33.2F);
        setRotationAngle(bone40, 0.1745F, 0.1745F, -0.7854F);
        pillar5.addChild(bone40);
        bone40.cubeList.add(new ModelBox(bone40, 0, 0, -24.2695F, -13.7521F, 30.8799F, 4, 4, 0, 0.0F, true));
        bone40.cubeList.add(new ModelBox(bone40, 0, 0, -28.2695F, -9.7521F, 30.8799F, 4, 4, 0, 0.0F, true));
        bone40.cubeList.add(new ModelBox(bone40, 0, 0, -32.2695F, -5.7521F, 30.8799F, 4, 4, 0, 0.0F, true));

        bone41 = new ModelRenderer(this);
        bone41.setRotationPoint(-4.25F, -27.5F, 33.2F);
        setRotationAngle(bone41, 0.1745F, -0.1745F, 0.7854F);
        pillar5.addChild(bone41);
        bone41.cubeList.add(new ModelBox(bone41, 0, 0, -13.156F, 20.6968F, 30.7903F, 4, 4, 0, 0.0F, false));
        bone41.cubeList.add(new ModelBox(bone41, 0, 0, -9.156F, 24.6968F, 30.7903F, 4, 4, 0, 0.0F, false));
        bone41.cubeList.add(new ModelBox(bone41, 0, 0, -5.156F, 28.6968F, 30.7903F, 4, 4, 0, 0.0F, false));

        bone42 = new ModelRenderer(this);
        bone42.setRotationPoint(0.0F, 0.0F, 24.0F);
        setRotationAngle(bone42, 0.0F, -1.5708F, 0.0F);
        pillar5.addChild(bone42);

        bone43 = new ModelRenderer(this);
        bone43.setRotationPoint(4.25F, -27.5F, -9.2F);
        setRotationAngle(bone43, -0.1745F, -0.1745F, -0.7854F);
        bone42.addChild(bone43);
        bone43.cubeList.add(new ModelBox(bone43, 0, 0, 24.4512F, 16.8617F, 23.536F, 4, 4, 0, 0.0F, true));
        bone43.cubeList.add(new ModelBox(bone43, 0, 0, 20.4512F, 20.8617F, 23.536F, 4, 4, 0, 0.0F, true));
        bone43.cubeList.add(new ModelBox(bone43, 0, 0, 16.4512F, 24.8617F, 23.536F, 4, 4, 0, 0.0F, true));

        bone44 = new ModelRenderer(this);
        bone44.setRotationPoint(-4.25F, -27.5F, -9.2F);
        setRotationAngle(bone44, -0.1745F, 0.1745F, 0.7854F);
        bone42.addChild(bone44);
        bone44.cubeList.add(new ModelBox(bone44, 0, 0, 16.1161F, -29.0702F, 23.4166F, 4, 4, 0, 0.0F, false));
        bone44.cubeList.add(new ModelBox(bone44, 0, 0, 20.1161F, -25.0702F, 23.4166F, 4, 4, 0, 0.0F, false));
        bone44.cubeList.add(new ModelBox(bone44, 0, 0, 24.1161F, -21.0702F, 23.4166F, 4, 4, 0, 0.0F, false));

        bone45 = new ModelRenderer(this);
        bone45.setRotationPoint(4.25F, -27.5F, 9.2F);
        setRotationAngle(bone45, 0.1745F, 0.1745F, -0.7854F);
        bone42.addChild(bone45);
        bone45.cubeList.add(new ModelBox(bone45, 0, 0, 16.1161F, 25.0702F, 23.0166F, 4, 4, 0, 0.0F, true));
        bone45.cubeList.add(new ModelBox(bone45, 0, 0, 12.1161F, 29.0702F, 23.0166F, 4, 4, 0, 0.0F, true));
        bone45.cubeList.add(new ModelBox(bone45, 0, 0, 8.1161F, 33.0702F, 23.0166F, 4, 4, 0, 0.0F, true));

        bone46 = new ModelRenderer(this);
        bone46.setRotationPoint(-4.25F, -27.5F, 9.2F);
        setRotationAngle(bone46, 0.1745F, -0.1745F, 0.7854F);
        bone42.addChild(bone46);
        bone46.cubeList.add(new ModelBox(bone46, 0, 0, 24.4512F, -20.8617F, 23.136F, 4, 4, 0, 0.0F, false));
        bone46.cubeList.add(new ModelBox(bone46, 0, 0, 28.4512F, -16.8617F, 23.136F, 4, 4, 0, 0.0F, false));
        bone46.cubeList.add(new ModelBox(bone46, 0, 0, 32.4512F, -12.8617F, 23.136F, 4, 4, 0, 0.0F, false));

        pillar6 = new ModelRenderer(this);
        pillar6.setRotationPoint(0.0F, 24.0F, 0.0F);
        pillar6.cubeList.add(new ModelBox(pillar6, 0, 44, 16.0F, -48.0F, 48.0F, 16, 48, 16, 0.0F, false));
        pillar6.cubeList.add(new ModelBox(pillar6, 0, 21, 15.5F, -48.5F, 47.5F, 17, 6, 17, 0.0F, false));
        pillar6.cubeList.add(new ModelBox(pillar6, 0, 0, 15.5F, -33.5F, 47.5F, 17, 4, 17, 0.0F, false));

        bone47 = new ModelRenderer(this);
        bone47.setRotationPoint(-4.25F, -27.5F, 14.8F);
        setRotationAngle(bone47, -0.1745F, 0.1745F, 0.7854F);
        pillar6.addChild(bone47);
        bone47.cubeList.add(new ModelBox(bone47, 0, 0, 9.1213F, -24.731F, 31.1903F, 4, 4, 0, 0.0F, false));
        bone47.cubeList.add(new ModelBox(bone47, 0, 0, 13.1213F, -20.731F, 31.1903F, 4, 4, 0, 0.0F, false));
        bone47.cubeList.add(new ModelBox(bone47, 0, 0, 17.1213F, -16.731F, 31.1903F, 4, 4, 0, 0.0F, false));

        bone48 = new ModelRenderer(this);
        bone48.setRotationPoint(4.25F, -27.5F, 14.8F);
        setRotationAngle(bone48, -0.1745F, -0.1745F, -0.7854F);
        pillar6.addChild(bone48);
        bone48.cubeList.add(new ModelBox(bone48, 0, 0, 20.3042F, 9.7179F, 31.2799F, 4, 4, 0, 0.0F, true));
        bone48.cubeList.add(new ModelBox(bone48, 0, 0, 16.3042F, 13.7179F, 31.2799F, 4, 4, 0, 0.0F, true));
        bone48.cubeList.add(new ModelBox(bone48, 0, 0, 12.3042F, 17.7179F, 31.2799F, 4, 4, 0, 0.0F, true));

        bone49 = new ModelRenderer(this);
        bone49.setRotationPoint(-4.25F, -27.5F, 33.2F);
        setRotationAngle(bone49, 0.1745F, -0.1745F, 0.7854F);
        pillar6.addChild(bone49);
        bone49.cubeList.add(new ModelBox(bone49, 0, 0, 20.2695F, -13.7521F, 30.8799F, 4, 4, 0, 0.0F, false));
        bone49.cubeList.add(new ModelBox(bone49, 0, 0, 24.2695F, -9.7521F, 30.8799F, 4, 4, 0, 0.0F, false));
        bone49.cubeList.add(new ModelBox(bone49, 0, 0, 28.2695F, -5.7521F, 30.8799F, 4, 4, 0, 0.0F, false));

        bone50 = new ModelRenderer(this);
        bone50.setRotationPoint(4.25F, -27.5F, 33.2F);
        setRotationAngle(bone50, 0.1745F, 0.1745F, -0.7854F);
        pillar6.addChild(bone50);
        bone50.cubeList.add(new ModelBox(bone50, 0, 0, 9.156F, 20.6968F, 30.7903F, 4, 4, 0, 0.0F, true));
        bone50.cubeList.add(new ModelBox(bone50, 0, 0, 5.156F, 24.6968F, 30.7903F, 4, 4, 0, 0.0F, true));
        bone50.cubeList.add(new ModelBox(bone50, 0, 0, 1.156F, 28.6968F, 30.7903F, 4, 4, 0, 0.0F, true));

        bone51 = new ModelRenderer(this);
        bone51.setRotationPoint(0.0F, 0.0F, 24.0F);
        setRotationAngle(bone51, 0.0F, 1.5708F, 0.0F);
        pillar6.addChild(bone51);

        bone52 = new ModelRenderer(this);
        bone52.setRotationPoint(-4.25F, -27.5F, -9.2F);
        setRotationAngle(bone52, -0.1745F, 0.1745F, 0.7854F);
        bone51.addChild(bone52);
        bone52.cubeList.add(new ModelBox(bone52, 0, 0, -28.4512F, 16.8617F, 23.536F, 4, 4, 0, 0.0F, false));
        bone52.cubeList.add(new ModelBox(bone52, 0, 0, -24.4512F, 20.8617F, 23.536F, 4, 4, 0, 0.0F, false));
        bone52.cubeList.add(new ModelBox(bone52, 0, 0, -20.4512F, 24.8617F, 23.536F, 4, 4, 0, 0.0F, false));

        bone53 = new ModelRenderer(this);
        bone53.setRotationPoint(4.25F, -27.5F, -9.2F);
        setRotationAngle(bone53, -0.1745F, -0.1745F, -0.7854F);
        bone51.addChild(bone53);
        bone53.cubeList.add(new ModelBox(bone53, 0, 0, -20.1161F, -29.0702F, 23.4166F, 4, 4, 0, 0.0F, true));
        bone53.cubeList.add(new ModelBox(bone53, 0, 0, -24.1161F, -25.0702F, 23.4166F, 4, 4, 0, 0.0F, true));
        bone53.cubeList.add(new ModelBox(bone53, 0, 0, -28.1161F, -21.0702F, 23.4166F, 4, 4, 0, 0.0F, true));

        bone54 = new ModelRenderer(this);
        bone54.setRotationPoint(-4.25F, -27.5F, 9.2F);
        setRotationAngle(bone54, 0.1745F, -0.1745F, 0.7854F);
        bone51.addChild(bone54);
        bone54.cubeList.add(new ModelBox(bone54, 0, 0, -20.1161F, 25.0702F, 23.0166F, 4, 4, 0, 0.0F, false));
        bone54.cubeList.add(new ModelBox(bone54, 0, 0, -16.1161F, 29.0702F, 23.0166F, 4, 4, 0, 0.0F, false));
        bone54.cubeList.add(new ModelBox(bone54, 0, 0, -12.1161F, 33.0702F, 23.0166F, 4, 4, 0, 0.0F, false));

        bone55 = new ModelRenderer(this);
        bone55.setRotationPoint(4.25F, -27.5F, 9.2F);
        setRotationAngle(bone55, 0.1745F, 0.1745F, -0.7854F);
        bone51.addChild(bone55);
        bone55.cubeList.add(new ModelBox(bone55, 0, 0, -28.4512F, -20.8617F, 23.136F, 4, 4, 0, 0.0F, true));
        bone55.cubeList.add(new ModelBox(bone55, 0, 0, -32.4512F, -16.8617F, 23.136F, 4, 4, 0, 0.0F, true));
        bone55.cubeList.add(new ModelBox(bone55, 0, 0, -36.4512F, -12.8617F, 23.136F, 4, 4, 0, 0.0F, true));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 64, 39, -24.0F, -1.0F, -24.0F, 48, 0, 48, 0.0F, false));

        bone56 = new ModelRenderer(this);
        bone56.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone56.cubeList.add(new ModelBox(bone56, 150, 227, -32.0F, -12.0F, -64.0F, 16, 12, 16, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 150, 227, 16.0F, -12.0F, -64.0F, 16, 12, 16, 0.0F, true));
        bone56.cubeList.add(new ModelBox(bone56, 0, 236, -60.0F, -101.0F, -58.0F, 120, 6, 4, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 2, 176, -54.5F, -95.0F, -64.0F, 109, 3, 16, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 0, 186, -45.0F, -66.3F, -57.0F, 90, 8, 2, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 192, 171, -32.0F, -42.0F, -64.0F, 16, 30, 16, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 192, 171, 16.0F, -42.0F, -64.0F, 16, 30, 16, 0.0F, true));
        bone56.cubeList.add(new ModelBox(bone56, 153, 188, 16.5F, -55.0F, -63.5F, 15, 13, 15, 0.0F, true));
        bone56.cubeList.add(new ModelBox(bone56, 153, 188, -31.5F, -55.0F, -63.5F, 15, 13, 15, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 49, 184, 17.0F, -73.0F, -63.0F, 14, 18, 14, 0.0F, true));
        bone56.cubeList.add(new ModelBox(bone56, 49, 184, -31.0F, -73.0F, -63.0F, 14, 18, 14, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 30, 173, 17.5F, -85.0F, -62.5F, 13, 12, 13, 0.0F, true));
        bone56.cubeList.add(new ModelBox(bone56, 30, 173, -30.5F, -85.0F, -62.5F, 13, 12, 13, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 0, 192, 17.0F, -92.0F, -63.0F, 14, 7, 14, 0.0F, true));
        bone56.cubeList.add(new ModelBox(bone56, 0, 192, -31.0F, -92.0F, -63.0F, 14, 7, 14, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 28, 208, -47.0F, -68.3F, -59.0F, 94, 2, 6, 0.0F, false));
        bone56.cubeList.add(new ModelBox(bone56, 0, 180, -50.5F, -92.0F, -60.0F, 101, 5, 8, 0.0F, false));

        bone57 = new ModelRenderer(this);
        bone57.setRotationPoint(0.0F, -98.0F, -56.5F);
        setRotationAngle(bone57, 0.2618F, 0.0F, 0.0F);
        bone56.addChild(bone57);
        bone57.cubeList.add(new ModelBox(bone57, 0, 228, -55.5F, -1.0F, -14.5F, 111, 2, 14, 0.0F, false));

        bone58 = new ModelRenderer(this);
        bone58.setRotationPoint(0.0F, -98.0F, -55.5F);
        setRotationAngle(bone58, -0.2618F, 0.0F, 0.0F);
        bone56.addChild(bone58);
        bone58.cubeList.add(new ModelBox(bone58, 0, 222, -55.5F, -1.0F, 0.5F, 111, 2, 14, 0.0F, false));

        bone59 = new ModelRenderer(this);
        bone59.setRotationPoint(0.0F, -67.3F, -59.25F);
        setRotationAngle(bone59, 0.0873F, 0.0F, 0.0F);
        bone56.addChild(bone59);
        bone59.cubeList.add(new ModelBox(bone59, 224, 234, -6.0F, -20.0F, -1.0F, 12, 20, 2, 0.0F, false));
        bone59.cubeList.add(new ModelBox(bone59, 230, 7, -4.5F, -18.5F, -1.7F, 9, 17, 2, 0.0F, false));

        bone60 = new ModelRenderer(this);
        bone60.setRotationPoint(0.0F, -67.3F, -52.75F);
        setRotationAngle(bone60, -0.0873F, 0.0F, 0.0F);
        bone56.addChild(bone60);
        bone60.cubeList.add(new ModelBox(bone60, 224, 234, -6.0F, -20.0F, -1.0F, 12, 20, 2, 0.0F, false));
        bone60.cubeList.add(new ModelBox(bone60, 230, 7, -4.5F, -18.5F, -0.25F, 9, 17, 2, 0.0F, false));

        bone61 = new ModelRenderer(this);
        bone61.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone61);
        bone61.cubeList.add(new ModelBox(bone61, 16, 243, 35.25F, -8.0F, -58.0F, 4, 8, 4, 0.0F, true));
        bone61.cubeList.add(new ModelBox(bone61, 5, 178, 35.25F, -27.0F, -58.0F, 4, 19, 4, 0.0F, true));
        bone61.cubeList.add(new ModelBox(bone61, 5, 180, 32.0F, -24.0F, -57.0F, 9, 2, 2, 0.0F, true));
        bone61.cubeList.add(new ModelBox(bone61, 217, 178, 32.0F, -31.0F, -58.5F, 12, 4, 5, 0.0F, true));

        bone63 = new ModelRenderer(this);
        bone63.setRotationPoint(38.0F, -31.75F, -56.0F);
        setRotationAngle(bone63, 0.0F, 0.0F, 0.0873F);
        bone61.addChild(bone63);
        bone63.cubeList.add(new ModelBox(bone63, 204, 236, -6.0F, -0.5F, -3.0F, 14, 2, 6, 0.0F, true));

        bone62 = new ModelRenderer(this);
        bone62.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone62);
        bone62.cubeList.add(new ModelBox(bone62, 16, 243, -39.25F, -8.0F, -58.0F, 4, 8, 4, 0.0F, false));
        bone62.cubeList.add(new ModelBox(bone62, 5, 178, -39.25F, -27.0F, -58.0F, 4, 19, 4, 0.0F, false));
        bone62.cubeList.add(new ModelBox(bone62, 5, 180, -41.0F, -24.0F, -57.0F, 9, 2, 2, 0.0F, false));
        bone62.cubeList.add(new ModelBox(bone62, 217, 178, -44.0F, -31.0F, -58.5F, 12, 4, 5, 0.0F, false));

        bone64 = new ModelRenderer(this);
        bone64.setRotationPoint(-38.0F, -31.75F, -56.0F);
        setRotationAngle(bone64, 0.0F, 0.0F, -0.0873F);
        bone62.addChild(bone64);
        bone64.cubeList.add(new ModelBox(bone64, 204, 236, -8.0F, -0.5F, -3.0F, 14, 2, 6, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        pillar.render(f5);
        pillar2.render(f5);
        pillar3.render(f5);
        pillar4.render(f5);
        pillar5.render(f5);
        pillar6.render(f5);
        bone.render(f5);
        bone56.render(f5);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
