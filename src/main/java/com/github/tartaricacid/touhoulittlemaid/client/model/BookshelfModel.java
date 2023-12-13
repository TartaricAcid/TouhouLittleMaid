package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BookshelfModel extends EntityModel<Entity> {
    private final ModelRenderer main;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone9;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer bone29;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;
    private final ModelRenderer bone202;
    private final ModelRenderer bone203;
    private final ModelRenderer bone204;
    private final ModelRenderer bone205;
    private final ModelRenderer bone206;
    private final ModelRenderer bone207;
    private final ModelRenderer bone208;
    private final ModelRenderer bone209;
    private final ModelRenderer bone210;
    private final ModelRenderer bone37;
    private final ModelRenderer bone38;
    private final ModelRenderer bone41;
    private final ModelRenderer bone40;
    private final ModelRenderer bone39;
    private final ModelRenderer bone42;
    private final ModelRenderer bone43;
    private final ModelRenderer bone44;
    private final ModelRenderer bone45;
    private final ModelRenderer bone127;
    private final ModelRenderer bone100;
    private final ModelRenderer bone101;
    private final ModelRenderer bone102;
    private final ModelRenderer bone103;
    private final ModelRenderer bone104;
    private final ModelRenderer bone105;
    private final ModelRenderer bone106;
    private final ModelRenderer bone107;
    private final ModelRenderer bone108;
    private final ModelRenderer bone109;
    private final ModelRenderer bone110;
    private final ModelRenderer bone111;
    private final ModelRenderer bone112;
    private final ModelRenderer bone113;
    private final ModelRenderer bone114;
    private final ModelRenderer bone115;
    private final ModelRenderer bone116;
    private final ModelRenderer bone117;
    private final ModelRenderer bone118;
    private final ModelRenderer bone119;
    private final ModelRenderer bone120;
    private final ModelRenderer bone121;
    private final ModelRenderer bone122;
    private final ModelRenderer bone123;
    private final ModelRenderer bone124;
    private final ModelRenderer bone125;
    private final ModelRenderer bone126;
    private final ModelRenderer bone91;
    private final ModelRenderer bone92;
    private final ModelRenderer bone93;
    private final ModelRenderer bone94;
    private final ModelRenderer bone95;
    private final ModelRenderer bone96;
    private final ModelRenderer bone97;
    private final ModelRenderer bone98;
    private final ModelRenderer bone99;
    private final ModelRenderer bone193;
    private final ModelRenderer bone194;
    private final ModelRenderer bone195;
    private final ModelRenderer bone196;
    private final ModelRenderer bone197;
    private final ModelRenderer bone198;
    private final ModelRenderer bone199;
    private final ModelRenderer bone200;
    private final ModelRenderer bone201;
    private final ModelRenderer bone73;
    private final ModelRenderer bone74;
    private final ModelRenderer bone75;
    private final ModelRenderer bone76;
    private final ModelRenderer bone77;
    private final ModelRenderer bone78;
    private final ModelRenderer bone79;
    private final ModelRenderer bone80;
    private final ModelRenderer bone81;
    private final ModelRenderer bone82;
    private final ModelRenderer bone83;
    private final ModelRenderer bone84;
    private final ModelRenderer bone85;
    private final ModelRenderer bone86;
    private final ModelRenderer bone87;
    private final ModelRenderer bone88;
    private final ModelRenderer bone89;
    private final ModelRenderer bone90;
    private final ModelRenderer bone128;
    private final ModelRenderer bone129;
    private final ModelRenderer bone130;
    private final ModelRenderer bone131;
    private final ModelRenderer bone132;
    private final ModelRenderer bone133;
    private final ModelRenderer bone134;
    private final ModelRenderer bone135;
    private final ModelRenderer bone136;
    private final ModelRenderer bone137;
    private final ModelRenderer bone138;
    private final ModelRenderer bone139;
    private final ModelRenderer bone140;
    private final ModelRenderer bone141;
    private final ModelRenderer bone142;
    private final ModelRenderer bone143;
    private final ModelRenderer bone144;
    private final ModelRenderer bone145;
    private final ModelRenderer bone146;
    private final ModelRenderer bone147;
    private final ModelRenderer bone148;
    private final ModelRenderer bone149;
    private final ModelRenderer bone150;
    private final ModelRenderer bone151;
    private final ModelRenderer bone152;
    private final ModelRenderer bone153;
    private final ModelRenderer bone154;
    private final ModelRenderer bone155;
    private final ModelRenderer bone156;
    private final ModelRenderer bone157;
    private final ModelRenderer bone158;
    private final ModelRenderer bone159;
    private final ModelRenderer bone160;
    private final ModelRenderer bone161;
    private final ModelRenderer bone162;
    private final ModelRenderer bone163;
    private final ModelRenderer bone164;
    private final ModelRenderer bone165;
    private final ModelRenderer bone166;
    private final ModelRenderer bone167;
    private final ModelRenderer bone168;
    private final ModelRenderer bone169;
    private final ModelRenderer bone170;
    private final ModelRenderer bone171;
    private final ModelRenderer bone172;
    private final ModelRenderer bone173;
    private final ModelRenderer bone174;
    private final ModelRenderer bone175;
    private final ModelRenderer bone176;
    private final ModelRenderer bone177;
    private final ModelRenderer bone178;
    private final ModelRenderer bone179;
    private final ModelRenderer bone180;
    private final ModelRenderer bone181;
    private final ModelRenderer bone182;
    private final ModelRenderer bone183;
    private final ModelRenderer bone184;
    private final ModelRenderer bone185;
    private final ModelRenderer bone186;
    private final ModelRenderer bone187;
    private final ModelRenderer bone188;
    private final ModelRenderer bone189;
    private final ModelRenderer bone190;
    private final ModelRenderer bone191;
    private final ModelRenderer bone192;
    private final ModelRenderer bone211;
    private final ModelRenderer bone212;
    private final ModelRenderer bone213;
    private final ModelRenderer bone214;
    private final ModelRenderer bone215;
    private final ModelRenderer bone216;
    private final ModelRenderer bone217;
    private final ModelRenderer bone218;
    private final ModelRenderer bone219;
    private final ModelRenderer bone220;
    private final ModelRenderer bone221;
    private final ModelRenderer bone222;
    private final ModelRenderer bone223;
    private final ModelRenderer bone224;
    private final ModelRenderer bone225;
    private final ModelRenderer bone226;
    private final ModelRenderer bone227;
    private final ModelRenderer bone228;
    private final ModelRenderer bone238;
    private final ModelRenderer bone239;
    private final ModelRenderer bone240;
    private final ModelRenderer bone241;
    private final ModelRenderer bone242;
    private final ModelRenderer bone243;
    private final ModelRenderer bone244;
    private final ModelRenderer bone245;
    private final ModelRenderer bone246;
    private final ModelRenderer bone46;
    private final ModelRenderer bone47;
    private final ModelRenderer bone48;
    private final ModelRenderer bone49;
    private final ModelRenderer bone50;
    private final ModelRenderer bone51;
    private final ModelRenderer bone52;
    private final ModelRenderer bone53;
    private final ModelRenderer bone54;
    private final ModelRenderer bone247;
    private final ModelRenderer bone248;
    private final ModelRenderer bone249;
    private final ModelRenderer bone250;
    private final ModelRenderer bone251;
    private final ModelRenderer bone252;
    private final ModelRenderer bone253;
    private final ModelRenderer bone254;
    private final ModelRenderer bone255;
    private final ModelRenderer bone55;
    private final ModelRenderer bone56;
    private final ModelRenderer bone57;
    private final ModelRenderer bone58;
    private final ModelRenderer bone59;
    private final ModelRenderer bone60;
    private final ModelRenderer bone61;
    private final ModelRenderer bone62;
    private final ModelRenderer bone63;
    private final ModelRenderer bone64;
    private final ModelRenderer bone65;
    private final ModelRenderer bone66;
    private final ModelRenderer bone67;
    private final ModelRenderer bone68;
    private final ModelRenderer bone69;
    private final ModelRenderer bone70;
    private final ModelRenderer bone71;
    private final ModelRenderer bone72;
    private final ModelRenderer bone36;
    private final ModelRenderer bone229;
    private final ModelRenderer book;
    private final ModelRenderer bone230;
    private final ModelRenderer bone231;
    private final ModelRenderer bone232;
    private final ModelRenderer bone233;
    private final ModelRenderer bone234;
    private final ModelRenderer bone235;
    private final ModelRenderer bone236;
    private final ModelRenderer bone257;
    private final ModelRenderer bone258;
    private final ModelRenderer bone259;
    private final ModelRenderer bone260;
    private final ModelRenderer bone261;

    public BookshelfModel() {
        texWidth = 256;
        texHeight = 256;

        main = new ModelRenderer(this);
        main.setPos(0.0F, 24.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setPos(-6.0F, 0.0F, 3.0F);
        main.addChild(bone);
        bone.texOffs(0, 0).addBox(-8.0F, -42.0F, 23.0F, 32.0F, 41.0F, 1.0F, 0.0F, false);
        bone.texOffs(0, 58).addBox(-8.0F, -1.0F, 11.0F, 32.0F, 1.0F, 13.0F, 0.0F, false);
        bone.texOffs(67, 28).addBox(0.0F, -1.0F, -23.25F, 20.0F, 1.0F, 7.0F, 0.0F, false);
        bone.texOffs(67, 37).addBox(10.0F, -1.0F, -15.75F, 11.0F, 1.0F, 4.0F, 0.0F, false);
        bone.texOffs(120, 130).addBox(17.0F, -27.0F, -14.25F, 1.0F, 26.0F, 1.0F, 0.0F, false);
        bone.texOffs(122, 28).addBox(3.0F, -9.0F, -23.25F, 12.0F, 1.0F, 7.0F, 0.0F, false);
        bone.texOffs(98, 37).addBox(0.0F, -5.0F, -23.25F, 15.0F, 1.0F, 7.0F, 0.0F, false);
        bone.texOffs(67, 14).addBox(-7.0F, -29.0F, 11.0F, 30.0F, 1.0F, 12.0F, 0.0F, false);
        bone.texOffs(67, 0).addBox(-7.0F, -15.0F, 11.0F, 30.0F, 1.0F, 12.0F, 0.0F, false);
        bone.texOffs(27, 73).addBox(23.0F, -42.0F, 11.0F, 1.0F, 41.0F, 12.0F, 0.0F, false);
        bone.texOffs(0, 73).addBox(-8.0F, -42.0F, 11.0F, 1.0F, 41.0F, 12.0F, 0.0F, false);
        bone.texOffs(0, 43).addBox(-8.0F, -43.0F, 11.0F, 32.0F, 1.0F, 13.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(12.9651F, 15.1173F, -11.75F);
        bone.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, 1.1781F);
        bone2.texOffs(42, 73).addBox(-12.2744F, -13.2846F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        bone2.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 1.1781F);
        bone3.texOffs(42, 73).addBox(-17.5879F, 5.3324F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, 0.0F, 0.0F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 1.1781F);
        bone4.texOffs(42, 73).addBox(-1.8041F, 18.2897F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 2.0F, 0.0F);
        bone4.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, 1.1781F);
        bone5.texOffs(42, 73).addBox(15.2832F, 7.2832F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(-5.0F, 0.0F, 0.0F);
        bone.addChild(bone9);


        bone6 = new ModelRenderer(this);
        bone6.setPos(-3.9651F, 15.1173F, -11.75F);
        bone9.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, -1.1781F);
        bone6.texOffs(42, 73).addBox(17.3974F, 1.4975F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        bone6.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, -1.1781F);
        bone7.texOffs(42, 73).addBox(5.2742F, 16.6461F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        bone7.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, -1.1781F);
        bone8.texOffs(42, 73).addBox(-13.978F, 12.1668F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-3.9651F, 11.8827F, -11.75F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, 1.1781F);
        bone10.texOffs(42, 73).addBox(-12.1668F, -14.7434F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 0.0F, 0.0F);
        bone10.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.0F, 1.1781F);
        bone11.texOffs(42, 73).addBox(-17.3532F, 4.9813F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, 0.0F, 0.0F);
        bone11.addChild(bone12);
        setRotationAngle(bone12, 0.0F, 0.0F, 1.1781F);
        bone12.texOffs(42, 73).addBox(-1.7321F, 16.3974F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, -2.0F, 0.0F);
        bone12.addChild(bone13);
        setRotationAngle(bone13, 0.0F, 0.0F, 1.1781F);
        bone13.texOffs(42, 73).addBox(15.7168F, 7.7168F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone14 = new ModelRenderer(this);
        bone14.setPos(-1.6259F, -15.2706F, -5.75F);
        bone.addChild(bone14);


        bone15 = new ModelRenderer(this);
        bone15.setPos(9.591F, 26.3879F, -6.0F);
        bone14.addChild(bone15);
        setRotationAngle(bone15, 0.0F, 0.0F, 1.1781F);
        bone15.texOffs(42, 73).addBox(-12.2744F, -13.2846F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, 0.0F, 0.0F);
        bone15.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.0F, 1.1781F);
        bone16.texOffs(42, 73).addBox(-17.5879F, 5.3324F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, 0.0F, 0.0F);
        bone16.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, 1.1781F);
        bone17.texOffs(42, 73).addBox(-1.8041F, 18.2897F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(9.591F, 23.1533F, -6.0F);
        bone14.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, -1.1781F);
        bone18.texOffs(42, 73).addBox(17.2897F, 0.0387F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, 0.0F, 0.0F);
        bone18.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, -1.1781F);
        bone19.texOffs(42, 73).addBox(5.0395F, 16.295F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, 0.0F, 0.0F);
        bone19.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.0F, -1.1781F);
        bone20.texOffs(42, 73).addBox(-14.0499F, 10.2744F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setPos(0.0F, -2.0F, 0.0F);
        bone20.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.0F, -1.1781F);
        bone21.texOffs(42, 73).addBox(-16.7168F, -8.2832F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(5.6259F, -15.2706F, -5.75F);
        bone.addChild(bone22);


        bone26 = new ModelRenderer(this);
        bone26.setPos(-11.591F, 23.1533F, -6.0F);
        bone22.addChild(bone26);
        setRotationAngle(bone26, 0.0F, 0.0F, 1.1781F);
        bone26.texOffs(42, 73).addBox(-12.1668F, -14.7434F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone27 = new ModelRenderer(this);
        bone27.setPos(0.0F, 0.0F, 0.0F);
        bone26.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.0F, 1.1781F);
        bone27.texOffs(42, 73).addBox(-17.3532F, 4.9813F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone28 = new ModelRenderer(this);
        bone28.setPos(0.0F, 0.0F, 0.0F);
        bone27.addChild(bone28);
        setRotationAngle(bone28, 0.0F, 0.0F, 1.1781F);
        bone28.texOffs(42, 73).addBox(-1.7321F, 16.3974F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone29 = new ModelRenderer(this);
        bone29.setPos(0.0F, -2.0F, 0.0F);
        bone28.addChild(bone29);
        setRotationAngle(bone29, 0.0F, 0.0F, 1.1781F);
        bone29.texOffs(42, 73).addBox(15.7168F, 7.7168F, -11.5F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone23 = new ModelRenderer(this);
        bone23.setPos(3.3391F, 4.1533F, -3.0F);
        bone22.addChild(bone23);
        setRotationAngle(bone23, 0.0F, 0.0F, -1.1781F);
        bone23.texOffs(0, 43).addBox(18.2136F, -0.344F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, 0.0F, 0.0F);
        bone23.addChild(bone24);
        setRotationAngle(bone24, 0.0F, 0.0F, -1.1781F);
        bone24.texOffs(0, 43).addBox(5.7466F, 17.0021F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, 0.0F, 0.0F);
        bone24.addChild(bone25);
        setRotationAngle(bone25, 0.0F, 0.0F, -1.1781F);
        bone25.texOffs(0, 43).addBox(-14.4326F, 11.1983F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(0.0F, -2.0F, 0.0F);
        bone25.addChild(bone30);
        setRotationAngle(bone30, 0.0F, 0.0F, -1.1781F);


        bone31 = new ModelRenderer(this);
        bone31.setPos(0.0F, 2.0F, 0.0F);
        bone30.addChild(bone31);
        setRotationAngle(bone31, 0.0F, 0.0F, 1.9635F);
        bone31.texOffs(0, 43).addBox(0.9241F, 18.3795F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone32 = new ModelRenderer(this);
        bone32.setPos(0.0F, 0.0F, 0.0F);
        bone31.addChild(bone32);
        setRotationAngle(bone32, 0.0F, 0.0F, -1.1781F);
        bone32.texOffs(0, 43).addBox(-18.168F, 8.1939F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.0F, 0.0F, 0.0F);
        bone32.addChild(bone33);
        setRotationAngle(bone33, 0.0F, 0.0F, -1.1781F);
        bone33.texOffs(0, 43).addBox(-15.4466F, -14.2667F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone34 = new ModelRenderer(this);
        bone34.setPos(0.0F, -2.0F, 0.0F);
        bone33.addChild(bone34);
        setRotationAngle(bone34, 0.0F, 0.0F, -1.1781F);
        bone34.texOffs(0, 43).addBox(5.4218F, -18.9651F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone35 = new ModelRenderer(this);
        bone35.setPos(-4.3391F, 0.9301F, 3.0F);
        bone34.addChild(bone35);
        setRotationAngle(bone35, 0.0F, 0.0F, 0.3927F);
        bone35.texOffs(123, 102).addBox(-0.5768F, -19.7702F, -13.53F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        bone35.texOffs(15, 73).addBox(1.0181F, -19.4269F, -13.0F, 1.0F, 1.0F, 10.0F, -0.2F, false);
        bone35.texOffs(15, 73).addBox(-0.2319F, -19.4269F, -13.0F, 1.0F, 1.0F, 10.0F, -0.2F, false);

        bone202 = new ModelRenderer(this);
        bone202.setPos(-4.1083F, 20.9945F, -6.7269F);
        bone22.addChild(bone202);
        setRotationAngle(bone202, 1.3963F, 0.0F, -1.5708F);
        bone202.texOffs(0, 127).addBox(15.0F, -11.4893F, -13.2676F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone202.texOffs(141, 95).addBox(14.5F, -12.9893F, -14.7676F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone202.texOffs(54, 90).addBox(15.5F, -12.9893F, -13.7676F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone202.texOffs(54, 90).addBox(13.5F, -12.9893F, -13.7676F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone203 = new ModelRenderer(this);
        bone203.setPos(-22.7479F, 10.9769F, 50.0489F);
        bone202.addChild(bone203);
        bone203.texOffs(141, 67).addBox(38.2479F, -23.9662F, -63.8165F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone203.texOffs(141, 67).addBox(38.2479F, -23.9662F, -63.8165F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone204 = new ModelRenderer(this);
        bone204.setPos(0.0F, 0.0F, 0.0F);
        bone203.addChild(bone204);
        setRotationAngle(bone204, 0.0F, -1.1781F, 0.0F);
        bone204.texOffs(89, 137).addBox(-44.2474F, -23.9662F, -62.1464F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone204.texOffs(89, 137).addBox(-44.2474F, -23.9662F, -62.1464F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone205 = new ModelRenderer(this);
        bone205.setPos(0.0F, 0.0F, 0.0F);
        bone203.addChild(bone205);
        setRotationAngle(bone205, 0.0F, -0.7854F, 0.0F);
        bone205.texOffs(89, 137).addBox(-18.1704F, -23.9662F, -73.9654F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone205.texOffs(89, 137).addBox(-18.1704F, -23.9662F, -73.9654F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone206 = new ModelRenderer(this);
        bone206.setPos(0.0F, 0.0F, 0.0F);
        bone203.addChild(bone206);
        setRotationAngle(bone206, 0.0F, -0.3927F, 0.0F);
        bone206.texOffs(89, 137).addBox(10.4446F, -23.9662F, -74.9054F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone206.texOffs(89, 137).addBox(10.4446F, -23.9662F, -74.9054F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone207 = new ModelRenderer(this);
        bone207.setPos(-27.2479F, 10.9769F, 50.0489F);
        bone202.addChild(bone207);
        bone207.texOffs(141, 67).addBox(40.7411F, -23.9662F, -63.8097F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone207.texOffs(141, 67).addBox(40.7411F, -23.9662F, -63.8097F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone208 = new ModelRenderer(this);
        bone208.setPos(0.0F, 0.0F, 0.0F);
        bone207.addChild(bone208);
        setRotationAngle(bone208, 0.0F, 1.1781F, 0.0F);
        bone208.texOffs(89, 137).addBox(72.6169F, -23.9662F, 13.6041F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone208.texOffs(89, 137).addBox(72.6169F, -23.9662F, 13.6041F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone209 = new ModelRenderer(this);
        bone209.setPos(0.0F, 0.0F, 0.0F);
        bone207.addChild(bone209);
        setRotationAngle(bone209, 0.0F, 0.7854F, 0.0F);
        bone209.texOffs(89, 137).addBox(73.1405F, -23.9662F, -15.9855F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone209.texOffs(89, 137).addBox(73.1405F, -23.9662F, -15.9855F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone210 = new ModelRenderer(this);
        bone210.setPos(0.0F, 0.0F, 0.0F);
        bone207.addChild(bone210);
        setRotationAngle(bone210, 0.0F, 0.3927F, 0.0F);
        bone210.texOffs(89, 137).addBox(62.3008F, -23.9662F, -43.5232F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone210.texOffs(89, 137).addBox(62.3008F, -23.9662F, -43.5232F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone37 = new ModelRenderer(this);
        bone37.setPos(-21.25F, 11.0F, 15.75F);
        bone.addChild(bone37);
        setRotationAngle(bone37, 0.0F, 0.3927F, 0.0F);
        bone37.texOffs(118, 46).addBox(8.9525F, -21.0F, -8.3296F, 4.0F, 10.0F, 10.0F, -0.25F, false);
        bone37.texOffs(99, 58).addBox(11.4525F, -22.5F, -8.8296F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone37.texOffs(99, 58).addBox(7.4525F, -22.5F, -8.8296F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone37.texOffs(25, 131).addBox(8.4525F, -22.5F, -9.8296F, 5.0F, 13.0F, 3.0F, -1.25F, false);

        bone38 = new ModelRenderer(this);
        bone38.setPos(2.5F, 0.0F, 1.5F);
        bone37.addChild(bone38);
        bone38.texOffs(0, 148).addBox(8.9525F, -22.5F, -10.3296F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone41 = new ModelRenderer(this);
        bone41.setPos(0.0F, 0.0F, 0.0F);
        bone38.addChild(bone41);
        setRotationAngle(bone41, 0.0F, -1.1781F, 0.0F);
        bone41.texOffs(102, 147).addBox(-6.0428F, -22.5F, -14.6124F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone40 = new ModelRenderer(this);
        bone40.setPos(0.0F, 0.0F, 0.0F);
        bone38.addChild(bone40);
        setRotationAngle(bone40, 0.0F, -0.7854F, 0.0F);
        bone40.texOffs(102, 147).addBox(-1.0644F, -22.5F, -15.4294F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone39 = new ModelRenderer(this);
        bone39.setPos(0.0F, 0.0F, 0.0F);
        bone38.addChild(bone39);
        setRotationAngle(bone39, 0.0F, -0.3927F, 0.0F);
        bone39.texOffs(102, 147).addBox(3.8477F, -22.5F, -14.279F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone42 = new ModelRenderer(this);
        bone42.setPos(-2.0F, 0.0F, 1.5F);
        bone37.addChild(bone42);
        bone42.texOffs(0, 148).addBox(9.4457F, -22.5F, -10.3227F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone43 = new ModelRenderer(this);
        bone43.setPos(0.0F, 0.0F, 0.0F);
        bone42.addChild(bone43);
        setRotationAngle(bone43, 0.0F, 1.1781F, 0.0F);
        bone43.texOffs(102, 147).addBox(11.2252F, -22.5F, 5.1595F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone44 = new ModelRenderer(this);
        bone44.setPos(0.0F, 0.0F, 0.0F);
        bone42.addChild(bone44);
        setRotationAngle(bone44, 0.0F, 0.7854F, 0.0F);
        bone44.texOffs(102, 147).addBox(13.1903F, -22.5F, -0.2938F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone45 = new ModelRenderer(this);
        bone45.setPos(0.0F, 0.0F, 0.0F);
        bone42.addChild(bone45);
        setRotationAngle(bone45, 0.0F, 0.3927F, 0.0F);
        bone45.texOffs(102, 147).addBox(12.919F, -22.5F, -6.084F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone127 = new ModelRenderer(this);
        bone127.setPos(-13.0F, 11.0F, 24.5F);
        bone.addChild(bone127);


        bone100 = new ModelRenderer(this);
        bone100.setPos(22.0F, 0.0F, 0.0F);
        bone127.addChild(bone100);
        bone100.texOffs(99, 58).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone100.texOffs(99, 58).addBox(5.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone100.texOffs(25, 131).addBox(6.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, false);
        bone100.texOffs(118, 46).addBox(6.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, -0.25F, false);
        bone100.texOffs(99, 58).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone100.texOffs(99, 58).addBox(5.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone100.texOffs(25, 131).addBox(6.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, false);

        bone101 = new ModelRenderer(this);
        bone101.setPos(2.5F, 0.0F, 1.5F);
        bone100.addChild(bone101);
        bone101.texOffs(0, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone101.texOffs(0, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone102 = new ModelRenderer(this);
        bone102.setPos(0.0F, 0.0F, 0.0F);
        bone101.addChild(bone102);
        setRotationAngle(bone102, 0.0F, -1.1781F, 0.0F);
        bone102.texOffs(102, 147).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone102.texOffs(102, 147).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone103 = new ModelRenderer(this);
        bone103.setPos(0.0F, 0.0F, 0.0F);
        bone101.addChild(bone103);
        setRotationAngle(bone103, 0.0F, -0.7854F, 0.0F);
        bone103.texOffs(102, 147).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone103.texOffs(102, 147).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone104 = new ModelRenderer(this);
        bone104.setPos(0.0F, 0.0F, 0.0F);
        bone101.addChild(bone104);
        setRotationAngle(bone104, 0.0F, -0.3927F, 0.0F);
        bone104.texOffs(102, 147).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone104.texOffs(102, 147).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone105 = new ModelRenderer(this);
        bone105.setPos(-2.0F, 0.0F, 1.5F);
        bone100.addChild(bone105);
        bone105.texOffs(0, 148).addBox(6.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone105.texOffs(0, 148).addBox(6.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone106 = new ModelRenderer(this);
        bone106.setPos(0.0F, 0.0F, 0.0F);
        bone105.addChild(bone106);
        setRotationAngle(bone106, 0.0F, 1.1781F, 0.0F);
        bone106.texOffs(102, 147).addBox(13.6777F, -23.5F, 1.4891F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone106.texOffs(102, 147).addBox(13.6777F, -23.5F, 1.4891F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone107 = new ModelRenderer(this);
        bone107.setPos(0.0F, 0.0F, 0.0F);
        bone105.addChild(bone107);
        setRotationAngle(bone107, 0.0F, 0.7854F, 0.0F);
        bone107.texOffs(102, 147).addBox(14.0515F, -23.5F, -4.6233F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone107.texOffs(102, 147).addBox(14.0515F, -23.5F, -4.6233F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone108 = new ModelRenderer(this);
        bone108.setPos(0.0F, 0.0F, 0.0F);
        bone105.addChild(bone108);
        setRotationAngle(bone108, 0.0F, 0.3927F, 0.0F);
        bone108.texOffs(102, 147).addBox(12.0578F, -23.5F, -10.4135F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone108.texOffs(102, 147).addBox(12.0578F, -23.5F, -10.4135F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone109 = new ModelRenderer(this);
        bone109.setPos(15.75F, -0.5F, 0.0F);
        bone127.addChild(bone109);
        setRotationAngle(bone109, 0.0F, 0.0F, 0.3927F);
        bone109.texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone109.texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone109.texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);
        bone109.texOffs(124, 74).addBox(0.3854F, -23.7674F, -12.0F, 3.0F, 10.0F, 10.0F, -0.25F, false);
        bone109.texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone109.texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone109.texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);

        bone110 = new ModelRenderer(this);
        bone110.setPos(2.5F, 0.0F, 1.5F);
        bone109.addChild(bone110);
        bone110.texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone110.texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone111 = new ModelRenderer(this);
        bone111.setPos(0.0F, 0.0F, 0.0F);
        bone110.addChild(bone111);
        setRotationAngle(bone111, 0.0F, -1.1781F, 0.0F);
        bone111.texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone111.texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone112 = new ModelRenderer(this);
        bone112.setPos(0.0F, 0.0F, 0.0F);
        bone110.addChild(bone112);
        setRotationAngle(bone112, 0.0F, -0.7854F, 0.0F);
        bone112.texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone112.texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone113 = new ModelRenderer(this);
        bone113.setPos(0.0F, 0.0F, 0.0F);
        bone110.addChild(bone113);
        setRotationAngle(bone113, 0.0F, -0.3927F, 0.0F);
        bone113.texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone113.texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone114 = new ModelRenderer(this);
        bone114.setPos(-2.0F, 0.0F, 1.5F);
        bone109.addChild(bone114);
        bone114.texOffs(141, 67).addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone114.texOffs(141, 67).addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone115 = new ModelRenderer(this);
        bone115.setPos(0.0F, 0.0F, 0.0F);
        bone114.addChild(bone115);
        setRotationAngle(bone115, 0.0F, 1.1781F, 0.0F);
        bone115.texOffs(89, 137).addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone115.texOffs(89, 137).addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone116 = new ModelRenderer(this);
        bone116.setPos(0.0F, 0.0F, 0.0F);
        bone114.addChild(bone116);
        setRotationAngle(bone116, 0.0F, 0.7854F, 0.0F);
        bone116.texOffs(89, 137).addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone116.texOffs(89, 137).addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone117 = new ModelRenderer(this);
        bone117.setPos(0.0F, 0.0F, 0.0F);
        bone114.addChild(bone117);
        setRotationAngle(bone117, 0.0F, 0.3927F, 0.0F);
        bone117.texOffs(89, 137).addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone117.texOffs(89, 137).addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone118 = new ModelRenderer(this);
        bone118.setPos(15.75F, -0.5F, 0.0F);
        bone127.addChild(bone118);
        setRotationAngle(bone118, 0.0F, 0.0F, 0.3927F);
        bone118.texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone118.texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone118.texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);
        bone118.texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone118.texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone118.texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);

        bone119 = new ModelRenderer(this);
        bone119.setPos(2.5F, 0.0F, 1.5F);
        bone118.addChild(bone119);
        bone119.texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone119.texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone120 = new ModelRenderer(this);
        bone120.setPos(0.0F, 0.0F, 0.0F);
        bone119.addChild(bone120);
        setRotationAngle(bone120, 0.0F, -1.1781F, 0.0F);
        bone120.texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone120.texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone121 = new ModelRenderer(this);
        bone121.setPos(0.0F, 0.0F, 0.0F);
        bone119.addChild(bone121);
        setRotationAngle(bone121, 0.0F, -0.7854F, 0.0F);
        bone121.texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone121.texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone122 = new ModelRenderer(this);
        bone122.setPos(0.0F, 0.0F, 0.0F);
        bone119.addChild(bone122);
        setRotationAngle(bone122, 0.0F, -0.3927F, 0.0F);
        bone122.texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone122.texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone123 = new ModelRenderer(this);
        bone123.setPos(-2.0F, 0.0F, 1.5F);
        bone118.addChild(bone123);
        bone123.texOffs(141, 67).addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone123.texOffs(141, 67).addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone124 = new ModelRenderer(this);
        bone124.setPos(0.0F, 0.0F, 0.0F);
        bone123.addChild(bone124);
        setRotationAngle(bone124, 0.0F, 1.1781F, 0.0F);
        bone124.texOffs(89, 137).addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone124.texOffs(89, 137).addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone125 = new ModelRenderer(this);
        bone125.setPos(0.0F, 0.0F, 0.0F);
        bone123.addChild(bone125);
        setRotationAngle(bone125, 0.0F, 0.7854F, 0.0F);
        bone125.texOffs(89, 137).addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone125.texOffs(89, 137).addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone126 = new ModelRenderer(this);
        bone126.setPos(0.0F, 0.0F, 0.0F);
        bone123.addChild(bone126);
        setRotationAngle(bone126, 0.0F, 0.3927F, 0.0F);
        bone126.texOffs(89, 137).addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone126.texOffs(89, 137).addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone91 = new ModelRenderer(this);
        bone91.setPos(25.5F, 0.0F, 0.0F);
        bone127.addChild(bone91);
        bone91.texOffs(54, 90).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone91.texOffs(54, 90).addBox(6.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone91.texOffs(59, 134).addBox(7.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);
        bone91.texOffs(124, 74).addBox(7.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, -0.25F, false);
        bone91.texOffs(54, 90).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone91.texOffs(54, 90).addBox(6.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone91.texOffs(59, 134).addBox(7.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);

        bone92 = new ModelRenderer(this);
        bone92.setPos(2.5F, 0.0F, 1.5F);
        bone91.addChild(bone92);
        bone92.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone92.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone93 = new ModelRenderer(this);
        bone93.setPos(0.0F, 0.0F, 0.0F);
        bone92.addChild(bone93);
        setRotationAngle(bone93, 0.0F, -1.1781F, 0.0F);
        bone93.texOffs(89, 137).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone93.texOffs(89, 137).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone94 = new ModelRenderer(this);
        bone94.setPos(0.0F, 0.0F, 0.0F);
        bone92.addChild(bone94);
        setRotationAngle(bone94, 0.0F, -0.7854F, 0.0F);
        bone94.texOffs(89, 137).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone94.texOffs(89, 137).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone95 = new ModelRenderer(this);
        bone95.setPos(0.0F, 0.0F, 0.0F);
        bone92.addChild(bone95);
        setRotationAngle(bone95, 0.0F, -0.3927F, 0.0F);
        bone95.texOffs(89, 137).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone95.texOffs(89, 137).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone96 = new ModelRenderer(this);
        bone96.setPos(-2.0F, 0.0F, 1.5F);
        bone91.addChild(bone96);
        bone96.texOffs(141, 67).addBox(7.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone96.texOffs(141, 67).addBox(7.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone97 = new ModelRenderer(this);
        bone97.setPos(0.0F, 0.0F, 0.0F);
        bone96.addChild(bone97);
        setRotationAngle(bone97, 0.0F, 1.1781F, 0.0F);
        bone97.texOffs(89, 137).addBox(14.0604F, -23.5F, 2.413F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone97.texOffs(89, 137).addBox(14.0604F, -23.5F, 2.413F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone98 = new ModelRenderer(this);
        bone98.setPos(0.0F, 0.0F, 0.0F);
        bone96.addChild(bone98);
        setRotationAngle(bone98, 0.0F, 0.7854F, 0.0F);
        bone98.texOffs(89, 137).addBox(14.7586F, -23.5F, -3.9162F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone98.texOffs(89, 137).addBox(14.7586F, -23.5F, -3.9162F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone99 = new ModelRenderer(this);
        bone99.setPos(0.0F, 0.0F, 0.0F);
        bone96.addChild(bone99);
        setRotationAngle(bone99, 0.0F, 0.3927F, 0.0F);
        bone99.texOffs(89, 137).addBox(12.9817F, -23.5F, -10.0308F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone99.texOffs(89, 137).addBox(12.9817F, -23.5F, -10.0308F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone193 = new ModelRenderer(this);
        bone193.setPos(5.3673F, 3.0761F, -2.5F);
        bone127.addChild(bone193);
        setRotationAngle(bone193, 1.2217F, 0.0F, 1.5708F);
        bone193.texOffs(0, 127).addBox(-17.0F, -15.2537F, 0.2814F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone193.texOffs(54, 90).addBox(-16.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone193.texOffs(54, 90).addBox(-18.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone193.texOffs(141, 95).addBox(-17.5F, -16.7537F, -1.2186F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone193.texOffs(54, 90).addBox(-16.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone193.texOffs(54, 90).addBox(-18.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone194 = new ModelRenderer(this);
        bone194.setPos(-25.9979F, -11.5961F, 11.6066F);
        bone193.addChild(bone194);
        bone194.texOffs(141, 67).addBox(9.4979F, -5.1576F, -11.8252F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone194.texOffs(141, 67).addBox(9.4979F, -5.1576F, -11.8252F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone195 = new ModelRenderer(this);
        bone195.setPos(0.0F, 0.0F, 0.0F);
        bone194.addChild(bone195);
        setRotationAngle(bone195, 0.0F, -1.1781F, 0.0F);
        bone195.texOffs(89, 137).addBox(-7.2158F, -5.1576F, -15.6887F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone195.texOffs(89, 137).addBox(-7.2158F, -5.1576F, -15.6887F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone196 = new ModelRenderer(this);
        bone196.setPos(0.0F, 0.0F, 0.0F);
        bone194.addChild(bone196);
        setRotationAngle(bone196, 0.0F, -0.7854F, 0.0F);
        bone196.texOffs(89, 137).addBox(-1.7363F, -5.1576F, -16.8726F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone196.texOffs(89, 137).addBox(-1.7363F, -5.1576F, -16.8726F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone197 = new ModelRenderer(this);
        bone197.setPos(0.0F, 0.0F, 0.0F);
        bone194.addChild(bone197);
        setRotationAngle(bone197, 0.0F, -0.3927F, 0.0F);
        bone197.texOffs(89, 137).addBox(3.7793F, -5.1576F, -15.8695F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone197.texOffs(89, 137).addBox(3.7793F, -5.1576F, -15.8695F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone198 = new ModelRenderer(this);
        bone198.setPos(-30.4979F, -11.5961F, 11.6066F);
        bone193.addChild(bone198);
        bone198.texOffs(141, 67).addBox(11.9911F, -5.1576F, -11.8184F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone198.texOffs(141, 67).addBox(11.9911F, -5.1576F, -11.8184F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone199 = new ModelRenderer(this);
        bone199.setPos(0.0F, 0.0F, 0.0F);
        bone198.addChild(bone199);
        setRotationAngle(bone199, 0.0F, 1.1781F, 0.0F);
        bone199.texOffs(89, 137).addBox(13.5811F, -5.1576F, 6.9388F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone199.texOffs(89, 137).addBox(13.5811F, -5.1576F, 6.9388F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone200 = new ModelRenderer(this);
        bone200.setPos(0.0F, 0.0F, 0.0F);
        bone198.addChild(bone200);
        setRotationAngle(bone200, 0.0F, 0.7854F, 0.0F);
        bone200.texOffs(89, 137).addBox(16.0478F, -5.1576F, 0.4485F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone200.texOffs(89, 137).addBox(16.0478F, -5.1576F, 0.4485F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone201 = new ModelRenderer(this);
        bone201.setPos(0.0F, 0.0F, 0.0F);
        bone198.addChild(bone201);
        setRotationAngle(bone201, 0.0F, 0.3927F, 0.0F);
        bone201.texOffs(89, 137).addBox(15.843F, -5.1576F, -6.4917F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone201.texOffs(89, 137).addBox(15.843F, -5.1576F, -6.4917F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone73 = new ModelRenderer(this);
        bone73.setPos(5.3673F, 0.5761F, -0.5F);
        bone127.addChild(bone73);
        setRotationAngle(bone73, 1.8326F, 0.0F, 1.5708F);
        bone73.texOffs(0, 127).addBox(-17.0F, -10.6569F, 5.298F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone73.texOffs(54, 90).addBox(-16.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone73.texOffs(54, 90).addBox(-18.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone73.texOffs(141, 95).addBox(-17.5F, -12.1569F, 3.798F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone73.texOffs(54, 90).addBox(-16.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone73.texOffs(54, 90).addBox(-18.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone74 = new ModelRenderer(this);
        bone74.setPos(-25.9979F, -11.5961F, 11.6066F);
        bone73.addChild(bone74);
        bone74.texOffs(141, 67).addBox(9.4979F, -0.5607F, -6.8086F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone74.texOffs(141, 67).addBox(9.4979F, -0.5607F, -6.8086F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone75 = new ModelRenderer(this);
        bone75.setPos(0.0F, 0.0F, 0.0F);
        bone74.addChild(bone75);
        setRotationAngle(bone75, 0.0F, -1.1781F, 0.0F);
        bone75.texOffs(89, 137).addBox(-2.5811F, -0.5607F, -13.7689F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone75.texOffs(89, 137).addBox(-2.5811F, -0.5607F, -13.7689F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone76 = new ModelRenderer(this);
        bone76.setPos(0.0F, 0.0F, 0.0F);
        bone74.addChild(bone76);
        setRotationAngle(bone76, 0.0F, -0.7854F, 0.0F);
        bone76.texOffs(89, 137).addBox(1.811F, -0.5607F, -13.3254F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone76.texOffs(89, 137).addBox(1.811F, -0.5607F, -13.3254F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone77 = new ModelRenderer(this);
        bone77.setPos(0.0F, 0.0F, 0.0F);
        bone74.addChild(bone77);
        setRotationAngle(bone77, 0.0F, -0.3927F, 0.0F);
        bone77.texOffs(89, 137).addBox(5.699F, -0.5607F, -11.2348F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone77.texOffs(89, 137).addBox(5.699F, -0.5607F, -11.2348F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone78 = new ModelRenderer(this);
        bone78.setPos(-30.4979F, -11.5961F, 11.6066F);
        bone73.addChild(bone78);
        bone78.texOffs(141, 67).addBox(11.9911F, -0.5607F, -6.8018F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone78.texOffs(141, 67).addBox(11.9911F, -0.5607F, -6.8018F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone79 = new ModelRenderer(this);
        bone79.setPos(0.0F, 0.0F, 0.0F);
        bone78.addChild(bone79);
        setRotationAngle(bone79, 0.0F, 1.1781F, 0.0F);
        bone79.texOffs(89, 137).addBox(8.9463F, -0.5607F, 8.8586F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone79.texOffs(89, 137).addBox(8.9463F, -0.5607F, 8.8586F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone80 = new ModelRenderer(this);
        bone80.setPos(0.0F, 0.0F, 0.0F);
        bone78.addChild(bone80);
        setRotationAngle(bone80, 0.0F, 0.7854F, 0.0F);
        bone80.texOffs(89, 137).addBox(12.5005F, -0.5607F, 3.9958F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone80.texOffs(89, 137).addBox(12.5005F, -0.5607F, 3.9958F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone81 = new ModelRenderer(this);
        bone81.setPos(0.0F, 0.0F, 0.0F);
        bone78.addChild(bone81);
        setRotationAngle(bone81, 0.0F, 0.3927F, 0.0F);
        bone81.texOffs(89, 137).addBox(13.9232F, -0.5607F, -1.857F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone81.texOffs(89, 137).addBox(13.9232F, -0.5607F, -1.857F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone82 = new ModelRenderer(this);
        bone82.setPos(5.3673F, -1.9239F, -0.5F);
        bone127.addChild(bone82);
        setRotationAngle(bone82, -0.3054F, 0.0F, 1.5708F);
        bone82.texOffs(0, 127).addBox(-17.0F, -10.2241F, -14.5354F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone82.texOffs(54, 90).addBox(-16.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone82.texOffs(54, 90).addBox(-18.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone82.texOffs(141, 95).addBox(-17.5F, -11.7241F, -16.0354F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone82.texOffs(54, 90).addBox(-16.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone82.texOffs(54, 90).addBox(-18.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone83 = new ModelRenderer(this);
        bone83.setPos(-25.9979F, -11.5961F, 11.6066F);
        bone82.addChild(bone83);
        bone83.texOffs(141, 67).addBox(9.4979F, -0.1279F, -26.642F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone83.texOffs(141, 67).addBox(9.4979F, -0.1279F, -26.642F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone84 = new ModelRenderer(this);
        bone84.setPos(0.0F, 0.0F, 0.0F);
        bone83.addChild(bone84);
        setRotationAngle(bone84, 0.0F, -1.1781F, 0.0F);
        bone84.texOffs(89, 137).addBox(-20.9047F, -0.1279F, -21.3588F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone84.texOffs(89, 137).addBox(-20.9047F, -0.1279F, -21.3588F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone85 = new ModelRenderer(this);
        bone85.setPos(0.0F, 0.0F, 0.0F);
        bone83.addChild(bone85);
        setRotationAngle(bone85, 0.0F, -0.7854F, 0.0F);
        bone85.texOffs(89, 137).addBox(-12.2133F, -0.1279F, -27.3497F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone85.texOffs(89, 137).addBox(-12.2133F, -0.1279F, -27.3497F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone86 = new ModelRenderer(this);
        bone86.setPos(0.0F, 0.0F, 0.0F);
        bone83.addChild(bone86);
        setRotationAngle(bone86, 0.0F, -0.3927F, 0.0F);
        bone86.texOffs(89, 137).addBox(-1.8909F, -0.1279F, -29.5584F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone86.texOffs(89, 137).addBox(-1.8909F, -0.1279F, -29.5584F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone87 = new ModelRenderer(this);
        bone87.setPos(-30.4979F, -11.5961F, 11.6066F);
        bone82.addChild(bone87);
        bone87.texOffs(141, 67).addBox(11.9911F, -0.1279F, -26.6352F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone87.texOffs(141, 67).addBox(11.9911F, -0.1279F, -26.6352F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone88 = new ModelRenderer(this);
        bone88.setPos(0.0F, 0.0F, 0.0F);
        bone87.addChild(bone88);
        setRotationAngle(bone88, 0.0F, 1.1781F, 0.0F);
        bone88.texOffs(89, 137).addBox(27.27F, -0.1279F, 1.2687F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone88.texOffs(89, 137).addBox(27.27F, -0.1279F, 1.2687F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone89 = new ModelRenderer(this);
        bone89.setPos(0.0F, 0.0F, 0.0F);
        bone87.addChild(bone89);
        setRotationAngle(bone89, 0.0F, 0.7854F, 0.0F);
        bone89.texOffs(89, 137).addBox(26.5248F, -0.1279F, -10.0285F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone89.texOffs(89, 137).addBox(26.5248F, -0.1279F, -10.0285F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone90 = new ModelRenderer(this);
        bone90.setPos(0.0F, 0.0F, 0.0F);
        bone87.addChild(bone90);
        setRotationAngle(bone90, 0.0F, 0.3927F, 0.0F);
        bone90.texOffs(89, 137).addBox(21.5131F, -0.1279F, -20.1806F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone90.texOffs(89, 137).addBox(21.5131F, -0.1279F, -20.1806F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone128 = new ModelRenderer(this);
        bone128.setPos(0.0F, 0.0F, 0.0F);
        bone.addChild(bone128);


        bone129 = new ModelRenderer(this);
        bone129.setPos(13.0F, -3.0F, 24.5F);
        bone128.addChild(bone129);
        bone129.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone129.texOffs(54, 90).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone129.texOffs(103, 130).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, true);
        bone129.texOffs(74, 116).addBox(5.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, -0.25F, true);
        bone129.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone129.texOffs(54, 90).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone129.texOffs(103, 130).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, true);

        bone130 = new ModelRenderer(this);
        bone130.setPos(-2.5F, 0.0F, 1.5F);
        bone129.addChild(bone130);
        bone130.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone130.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone131 = new ModelRenderer(this);
        bone131.setPos(0.0F, 0.0F, 0.0F);
        bone130.addChild(bone131);
        setRotationAngle(bone131, 0.0F, 1.1781F, 0.0F);
        bone131.texOffs(89, 137).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone131.texOffs(89, 137).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone132 = new ModelRenderer(this);
        bone132.setPos(0.0F, 0.0F, 0.0F);
        bone130.addChild(bone132);
        setRotationAngle(bone132, 0.0F, 0.7854F, 0.0F);
        bone132.texOffs(89, 137).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone132.texOffs(89, 137).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone133 = new ModelRenderer(this);
        bone133.setPos(0.0F, 0.0F, 0.0F);
        bone130.addChild(bone133);
        setRotationAngle(bone133, 0.0F, 0.3927F, 0.0F);
        bone133.texOffs(89, 137).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone133.texOffs(89, 137).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone134 = new ModelRenderer(this);
        bone134.setPos(2.0F, 0.0F, 1.5F);
        bone129.addChild(bone134);
        bone134.texOffs(141, 67).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone134.texOffs(141, 67).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone135 = new ModelRenderer(this);
        bone135.setPos(0.0F, 0.0F, 0.0F);
        bone134.addChild(bone135);
        setRotationAngle(bone135, 0.0F, -1.1781F, 0.0F);
        bone135.texOffs(89, 137).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone135.texOffs(89, 137).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone136 = new ModelRenderer(this);
        bone136.setPos(0.0F, 0.0F, 0.0F);
        bone134.addChild(bone136);
        setRotationAngle(bone136, 0.0F, -0.7854F, 0.0F);
        bone136.texOffs(89, 137).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone136.texOffs(89, 137).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone137 = new ModelRenderer(this);
        bone137.setPos(0.0F, 0.0F, 0.0F);
        bone134.addChild(bone137);
        setRotationAngle(bone137, 0.0F, -0.3927F, 0.0F);
        bone137.texOffs(89, 137).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone137.texOffs(89, 137).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone138 = new ModelRenderer(this);
        bone138.setPos(4.0F, -3.0F, 24.5F);
        bone128.addChild(bone138);
        bone138.texOffs(118, 46).addBox(5.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, -0.25F, true);
        bone138.texOffs(104, 104).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone138.texOffs(104, 104).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone138.texOffs(42, 134).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, false);
        bone138.texOffs(104, 104).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone138.texOffs(104, 104).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone138.texOffs(42, 134).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, false);

        bone139 = new ModelRenderer(this);
        bone139.setPos(-2.5F, 0.0F, 1.5F);
        bone138.addChild(bone139);
        bone139.texOffs(13, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone139.texOffs(13, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone140 = new ModelRenderer(this);
        bone140.setPos(0.0F, 0.0F, 0.0F);
        bone139.addChild(bone140);
        setRotationAngle(bone140, 0.0F, 1.1781F, 0.0F);
        bone140.texOffs(125, 141).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone140.texOffs(125, 141).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone141 = new ModelRenderer(this);
        bone141.setPos(0.0F, 0.0F, 0.0F);
        bone139.addChild(bone141);
        setRotationAngle(bone141, 0.0F, 0.7854F, 0.0F);
        bone141.texOffs(125, 141).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone141.texOffs(125, 141).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone142 = new ModelRenderer(this);
        bone142.setPos(0.0F, 0.0F, 0.0F);
        bone139.addChild(bone142);
        setRotationAngle(bone142, 0.0F, 0.3927F, 0.0F);
        bone142.texOffs(125, 141).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone142.texOffs(125, 141).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone143 = new ModelRenderer(this);
        bone143.setPos(2.0F, 0.0F, 1.5F);
        bone138.addChild(bone143);
        bone143.texOffs(13, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone143.texOffs(13, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone144 = new ModelRenderer(this);
        bone144.setPos(0.0F, 0.0F, 0.0F);
        bone143.addChild(bone144);
        setRotationAngle(bone144, 0.0F, -1.1781F, 0.0F);
        bone144.texOffs(125, 141).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone144.texOffs(125, 141).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone145 = new ModelRenderer(this);
        bone145.setPos(0.0F, 0.0F, 0.0F);
        bone143.addChild(bone145);
        setRotationAngle(bone145, 0.0F, -0.7854F, 0.0F);
        bone145.texOffs(125, 141).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone145.texOffs(125, 141).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone146 = new ModelRenderer(this);
        bone146.setPos(0.0F, 0.0F, 0.0F);
        bone143.addChild(bone146);
        setRotationAngle(bone146, 0.0F, -0.3927F, 0.0F);
        bone146.texOffs(125, 141).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone146.texOffs(125, 141).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone147 = new ModelRenderer(this);
        bone147.setPos(-9.0F, -3.0F, 24.5F);
        bone128.addChild(bone147);
        bone147.texOffs(99, 58).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone147.texOffs(99, 58).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone147.texOffs(25, 131).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, true);
        bone147.texOffs(118, 46).addBox(5.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, -0.25F, true);
        bone147.texOffs(99, 58).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone147.texOffs(99, 58).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone147.texOffs(25, 131).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, true);

        bone148 = new ModelRenderer(this);
        bone148.setPos(-2.5F, 0.0F, 1.5F);
        bone147.addChild(bone148);
        bone148.texOffs(0, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone148.texOffs(0, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone149 = new ModelRenderer(this);
        bone149.setPos(0.0F, 0.0F, 0.0F);
        bone148.addChild(bone149);
        setRotationAngle(bone149, 0.0F, 1.1781F, 0.0F);
        bone149.texOffs(102, 147).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone149.texOffs(102, 147).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone150 = new ModelRenderer(this);
        bone150.setPos(0.0F, 0.0F, 0.0F);
        bone148.addChild(bone150);
        setRotationAngle(bone150, 0.0F, 0.7854F, 0.0F);
        bone150.texOffs(102, 147).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone150.texOffs(102, 147).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone151 = new ModelRenderer(this);
        bone151.setPos(0.0F, 0.0F, 0.0F);
        bone148.addChild(bone151);
        setRotationAngle(bone151, 0.0F, 0.3927F, 0.0F);
        bone151.texOffs(102, 147).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone151.texOffs(102, 147).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone152 = new ModelRenderer(this);
        bone152.setPos(2.0F, 0.0F, 1.5F);
        bone147.addChild(bone152);
        bone152.texOffs(0, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone152.texOffs(0, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone153 = new ModelRenderer(this);
        bone153.setPos(0.0F, 0.0F, 0.0F);
        bone152.addChild(bone153);
        setRotationAngle(bone153, 0.0F, -1.1781F, 0.0F);
        bone153.texOffs(102, 147).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone153.texOffs(102, 147).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone154 = new ModelRenderer(this);
        bone154.setPos(0.0F, 0.0F, 0.0F);
        bone152.addChild(bone154);
        setRotationAngle(bone154, 0.0F, -0.7854F, 0.0F);
        bone154.texOffs(102, 147).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone154.texOffs(102, 147).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone155 = new ModelRenderer(this);
        bone155.setPos(0.0F, 0.0F, 0.0F);
        bone152.addChild(bone155);
        setRotationAngle(bone155, 0.0F, -0.3927F, 0.0F);
        bone155.texOffs(102, 147).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone155.texOffs(102, 147).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone156 = new ModelRenderer(this);
        bone156.setPos(9.0F, -3.0F, 24.5F);
        bone128.addChild(bone156);
        bone156.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone156.texOffs(54, 90).addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone156.texOffs(59, 134).addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);
        bone156.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone156.texOffs(54, 90).addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone156.texOffs(59, 134).addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);
        bone156.texOffs(124, 74).addBox(5.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, -0.25F, true);

        bone157 = new ModelRenderer(this);
        bone157.setPos(-2.5F, 0.0F, 1.5F);
        bone156.addChild(bone157);
        bone157.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone157.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone158 = new ModelRenderer(this);
        bone158.setPos(0.0F, 0.0F, 0.0F);
        bone157.addChild(bone158);
        setRotationAngle(bone158, 0.0F, 1.1781F, 0.0F);
        bone158.texOffs(89, 137).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone158.texOffs(89, 137).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone159 = new ModelRenderer(this);
        bone159.setPos(0.0F, 0.0F, 0.0F);
        bone157.addChild(bone159);
        setRotationAngle(bone159, 0.0F, 0.7854F, 0.0F);
        bone159.texOffs(89, 137).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone159.texOffs(89, 137).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone160 = new ModelRenderer(this);
        bone160.setPos(0.0F, 0.0F, 0.0F);
        bone157.addChild(bone160);
        setRotationAngle(bone160, 0.0F, 0.3927F, 0.0F);
        bone160.texOffs(89, 137).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone160.texOffs(89, 137).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone161 = new ModelRenderer(this);
        bone161.setPos(2.0F, 0.0F, 1.5F);
        bone156.addChild(bone161);
        bone161.texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone161.texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone162 = new ModelRenderer(this);
        bone162.setPos(0.0F, 0.0F, 0.0F);
        bone161.addChild(bone162);
        setRotationAngle(bone162, 0.0F, -1.1781F, 0.0F);
        bone162.texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone162.texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone163 = new ModelRenderer(this);
        bone163.setPos(0.0F, 0.0F, 0.0F);
        bone161.addChild(bone163);
        setRotationAngle(bone163, 0.0F, -0.7854F, 0.0F);
        bone163.texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone163.texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone164 = new ModelRenderer(this);
        bone164.setPos(0.0F, 0.0F, 0.0F);
        bone161.addChild(bone164);
        setRotationAngle(bone164, 0.0F, -0.3927F, 0.0F);
        bone164.texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone164.texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone165 = new ModelRenderer(this);
        bone165.setPos(-2.75F, -3.5F, 24.5F);
        bone128.addChild(bone165);
        setRotationAngle(bone165, 0.0F, 0.0F, -0.3927F);
        bone165.texOffs(54, 90).addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone165.texOffs(54, 90).addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone165.texOffs(59, 134).addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);
        bone165.texOffs(54, 90).addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone165.texOffs(54, 90).addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone165.texOffs(59, 134).addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);

        bone166 = new ModelRenderer(this);
        bone166.setPos(-2.5F, 0.0F, 1.5F);
        bone165.addChild(bone166);
        bone166.texOffs(141, 67).addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone166.texOffs(141, 67).addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone167 = new ModelRenderer(this);
        bone167.setPos(0.0F, 0.0F, 0.0F);
        bone166.addChild(bone167);
        setRotationAngle(bone167, 0.0F, 1.1781F, 0.0F);
        bone167.texOffs(89, 137).addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone167.texOffs(89, 137).addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone168 = new ModelRenderer(this);
        bone168.setPos(0.0F, 0.0F, 0.0F);
        bone166.addChild(bone168);
        setRotationAngle(bone168, 0.0F, 0.7854F, 0.0F);
        bone168.texOffs(89, 137).addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone168.texOffs(89, 137).addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone169 = new ModelRenderer(this);
        bone169.setPos(0.0F, 0.0F, 0.0F);
        bone166.addChild(bone169);
        setRotationAngle(bone169, 0.0F, 0.3927F, 0.0F);
        bone169.texOffs(89, 137).addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone169.texOffs(89, 137).addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone170 = new ModelRenderer(this);
        bone170.setPos(2.0F, 0.0F, 1.5F);
        bone165.addChild(bone170);
        bone170.texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone170.texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone171 = new ModelRenderer(this);
        bone171.setPos(0.0F, 0.0F, 0.0F);
        bone170.addChild(bone171);
        setRotationAngle(bone171, 0.0F, -1.1781F, 0.0F);
        bone171.texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone171.texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone172 = new ModelRenderer(this);
        bone172.setPos(0.0F, 0.0F, 0.0F);
        bone170.addChild(bone172);
        setRotationAngle(bone172, 0.0F, -0.7854F, 0.0F);
        bone172.texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone172.texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone173 = new ModelRenderer(this);
        bone173.setPos(0.0F, 0.0F, 0.0F);
        bone170.addChild(bone173);
        setRotationAngle(bone173, 0.0F, -0.3927F, 0.0F);
        bone173.texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone173.texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone174 = new ModelRenderer(this);
        bone174.setPos(-2.75F, -3.5F, 24.5F);
        bone128.addChild(bone174);
        setRotationAngle(bone174, 0.0F, 0.0F, -0.3927F);
        bone174.texOffs(54, 90).addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone174.texOffs(54, 90).addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone174.texOffs(59, 134).addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);
        bone174.texOffs(124, 74).addBox(11.3967F, -17.6445F, -12.0F, 3.0F, 10.0F, 10.0F, -0.25F, true);
        bone174.texOffs(54, 90).addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone174.texOffs(54, 90).addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone174.texOffs(59, 134).addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);

        bone175 = new ModelRenderer(this);
        bone175.setPos(-2.5F, 0.0F, 1.5F);
        bone174.addChild(bone175);
        bone175.texOffs(141, 67).addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone175.texOffs(141, 67).addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone176 = new ModelRenderer(this);
        bone176.setPos(0.0F, 0.0F, 0.0F);
        bone175.addChild(bone176);
        setRotationAngle(bone176, 0.0F, 1.1781F, 0.0F);
        bone176.texOffs(89, 137).addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone176.texOffs(89, 137).addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone177 = new ModelRenderer(this);
        bone177.setPos(0.0F, 0.0F, 0.0F);
        bone175.addChild(bone177);
        setRotationAngle(bone177, 0.0F, 0.7854F, 0.0F);
        bone177.texOffs(89, 137).addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone177.texOffs(89, 137).addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone178 = new ModelRenderer(this);
        bone178.setPos(0.0F, 0.0F, 0.0F);
        bone175.addChild(bone178);
        setRotationAngle(bone178, 0.0F, 0.3927F, 0.0F);
        bone178.texOffs(89, 137).addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone178.texOffs(89, 137).addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone179 = new ModelRenderer(this);
        bone179.setPos(2.0F, 0.0F, 1.5F);
        bone174.addChild(bone179);
        bone179.texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone179.texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone180 = new ModelRenderer(this);
        bone180.setPos(0.0F, 0.0F, 0.0F);
        bone179.addChild(bone180);
        setRotationAngle(bone180, 0.0F, -1.1781F, 0.0F);
        bone180.texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone180.texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone181 = new ModelRenderer(this);
        bone181.setPos(0.0F, 0.0F, 0.0F);
        bone179.addChild(bone181);
        setRotationAngle(bone181, 0.0F, -0.7854F, 0.0F);
        bone181.texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone181.texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone182 = new ModelRenderer(this);
        bone182.setPos(0.0F, 0.0F, 0.0F);
        bone179.addChild(bone182);
        setRotationAngle(bone182, 0.0F, -0.3927F, 0.0F);
        bone182.texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone182.texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone183 = new ModelRenderer(this);
        bone183.setPos(-12.5F, -3.0F, 24.5F);
        bone128.addChild(bone183);
        bone183.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone183.texOffs(54, 90).addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone183.texOffs(59, 134).addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);
        bone183.texOffs(124, 74).addBox(5.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, -0.25F, true);
        bone183.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone183.texOffs(54, 90).addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, true);
        bone183.texOffs(59, 134).addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, true);

        bone184 = new ModelRenderer(this);
        bone184.setPos(-2.5F, 0.0F, 1.5F);
        bone183.addChild(bone184);
        bone184.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone184.texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone185 = new ModelRenderer(this);
        bone185.setPos(0.0F, 0.0F, 0.0F);
        bone184.addChild(bone185);
        setRotationAngle(bone185, 0.0F, 1.1781F, 0.0F);
        bone185.texOffs(89, 137).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone185.texOffs(89, 137).addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone186 = new ModelRenderer(this);
        bone186.setPos(0.0F, 0.0F, 0.0F);
        bone184.addChild(bone186);
        setRotationAngle(bone186, 0.0F, 0.7854F, 0.0F);
        bone186.texOffs(89, 137).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone186.texOffs(89, 137).addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone187 = new ModelRenderer(this);
        bone187.setPos(0.0F, 0.0F, 0.0F);
        bone184.addChild(bone187);
        setRotationAngle(bone187, 0.0F, 0.3927F, 0.0F);
        bone187.texOffs(89, 137).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone187.texOffs(89, 137).addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone188 = new ModelRenderer(this);
        bone188.setPos(2.0F, 0.0F, 1.5F);
        bone183.addChild(bone188);
        bone188.texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone188.texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone189 = new ModelRenderer(this);
        bone189.setPos(0.0F, 0.0F, 0.0F);
        bone188.addChild(bone189);
        setRotationAngle(bone189, 0.0F, -1.1781F, 0.0F);
        bone189.texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone189.texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone190 = new ModelRenderer(this);
        bone190.setPos(0.0F, 0.0F, 0.0F);
        bone188.addChild(bone190);
        setRotationAngle(bone190, 0.0F, -0.7854F, 0.0F);
        bone190.texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone190.texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone191 = new ModelRenderer(this);
        bone191.setPos(0.0F, 0.0F, 0.0F);
        bone188.addChild(bone191);
        setRotationAngle(bone191, 0.0F, -0.3927F, 0.0F);
        bone191.texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone191.texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone192 = new ModelRenderer(this);
        bone192.setPos(-22.0F, -17.0F, 24.5F);
        bone.addChild(bone192);


        bone211 = new ModelRenderer(this);
        bone211.setPos(20.9F, 0.0F, 0.0F);
        bone192.addChild(bone211);
        bone211.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone211.texOffs(54, 90).addBox(0.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone211.texOffs(103, 130).addBox(1.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, false);
        bone211.texOffs(74, 116).addBox(1.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, -0.25F, false);
        bone211.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone211.texOffs(54, 90).addBox(0.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone211.texOffs(103, 130).addBox(1.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, -1.25F, false);

        bone212 = new ModelRenderer(this);
        bone212.setPos(2.5F, 0.0F, 1.5F);
        bone211.addChild(bone212);
        bone212.texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone212.texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone213 = new ModelRenderer(this);
        bone213.setPos(0.0F, 0.0F, 0.0F);
        bone212.addChild(bone213);
        setRotationAngle(bone213, 0.0F, -1.1781F, 0.0F);
        bone213.texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone213.texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone214 = new ModelRenderer(this);
        bone214.setPos(0.0F, 0.0F, 0.0F);
        bone212.addChild(bone214);
        setRotationAngle(bone214, 0.0F, -0.7854F, 0.0F);
        bone214.texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone214.texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone215 = new ModelRenderer(this);
        bone215.setPos(0.0F, 0.0F, 0.0F);
        bone212.addChild(bone215);
        setRotationAngle(bone215, 0.0F, -0.3927F, 0.0F);
        bone215.texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone215.texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone216 = new ModelRenderer(this);
        bone216.setPos(-2.0F, 0.0F, 1.5F);
        bone211.addChild(bone216);
        bone216.texOffs(141, 67).addBox(1.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone216.texOffs(141, 67).addBox(1.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone217 = new ModelRenderer(this);
        bone217.setPos(0.0F, 0.0F, 0.0F);
        bone216.addChild(bone217);
        setRotationAngle(bone217, 0.0F, 1.1781F, 0.0F);
        bone217.texOffs(89, 137).addBox(11.7643F, -23.5F, -3.1303F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone217.texOffs(89, 137).addBox(11.7643F, -23.5F, -3.1303F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone218 = new ModelRenderer(this);
        bone218.setPos(0.0F, 0.0F, 0.0F);
        bone216.addChild(bone218);
        setRotationAngle(bone218, 0.0F, 0.7854F, 0.0F);
        bone218.texOffs(89, 137).addBox(10.516F, -23.5F, -8.1589F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone218.texOffs(89, 137).addBox(10.516F, -23.5F, -8.1589F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone219 = new ModelRenderer(this);
        bone219.setPos(0.0F, 0.0F, 0.0F);
        bone216.addChild(bone219);
        setRotationAngle(bone219, 0.0F, 0.3927F, 0.0F);
        bone219.texOffs(89, 137).addBox(7.4384F, -23.5F, -12.3269F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone219.texOffs(89, 137).addBox(7.4384F, -23.5F, -12.3269F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone220 = new ModelRenderer(this);
        bone220.setPos(29.0F, 1.25F, -1.75F);
        bone192.addChild(bone220);
        setRotationAngle(bone220, -0.48F, 0.0F, 1.5708F);
        bone220.texOffs(85, 90).addBox(-15.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone220.texOffs(85, 90).addBox(-18.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone220.texOffs(74, 137).addBox(-17.0F, -10.9021F, -16.2901F, 4.0F, 13.0F, 3.0F, -1.25F, false);
        bone220.texOffs(85, 90).addBox(-15.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone220.texOffs(85, 90).addBox(-18.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone220.texOffs(74, 137).addBox(-17.0F, -10.9021F, -16.2901F, 4.0F, 13.0F, 3.0F, -1.25F, false);
        bone220.texOffs(125, 120).addBox(-16.5F, -9.4021F, -14.7901F, 3.0F, 10.0F, 10.0F, -0.25F, false);

        bone221 = new ModelRenderer(this);
        bone221.setPos(2.5F, 0.0F, 1.5F);
        bone220.addChild(bone221);
        bone221.texOffs(143, 37).addBox(-17.5F, -10.9021F, -16.7901F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone221.texOffs(143, 37).addBox(-17.5F, -10.9021F, -16.7901F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone222 = new ModelRenderer(this);
        bone222.setPos(0.0F, 0.0F, 0.0F);
        bone221.addChild(bone222);
        setRotationAngle(bone222, 0.0F, -1.1781F, 0.0F);
        bone222.texOffs(138, 141).addBox(-22.1344F, -10.9021F, 7.3542F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone222.texOffs(138, 141).addBox(-22.1344F, -10.9021F, 7.3542F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone223 = new ModelRenderer(this);
        bone223.setPos(0.0F, 0.0F, 0.0F);
        bone221.addChild(bone223);
        setRotationAngle(bone223, 0.0F, -0.7854F, 0.0F);
        bone223.texOffs(138, 141).addBox(-24.3374F, -10.9021F, -1.2929F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone223.texOffs(138, 141).addBox(-24.3374F, -10.9021F, -1.2929F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone224 = new ModelRenderer(this);
        bone224.setPos(0.0F, 0.0F, 0.0F);
        bone221.addChild(bone224);
        setRotationAngle(bone224, 0.0F, -0.3927F, 0.0F);
        bone224.texOffs(138, 141).addBox(-23.0635F, -10.9021F, -10.1248F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone224.texOffs(138, 141).addBox(-23.0635F, -10.9021F, -10.1248F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone225 = new ModelRenderer(this);
        bone225.setPos(-2.0F, 0.0F, 1.5F);
        bone220.addChild(bone225);
        bone225.texOffs(143, 37).addBox(-16.0068F, -10.9021F, -16.7832F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone225.texOffs(143, 37).addBox(-16.0068F, -10.9021F, -16.7832F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone226 = new ModelRenderer(this);
        bone226.setPos(0.0F, 0.0F, 0.0F);
        bone225.addChild(bone226);
        setRotationAngle(bone226, 0.0F, 1.1781F, 0.0F);
        bone226.texOffs(138, 141).addBox(7.4536F, -10.9021F, -20.8279F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone226.texOffs(138, 141).addBox(7.4536F, -10.9021F, -20.8279F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone227 = new ModelRenderer(this);
        bone227.setPos(0.0F, 0.0F, 0.0F);
        bone225.addChild(bone227);
        setRotationAngle(bone227, 0.0F, 0.7854F, 0.0F);
        bone227.texOffs(138, 141).addBox(-0.2391F, -10.9021F, -22.8597F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone227.texOffs(138, 141).addBox(-0.2391F, -10.9021F, -22.8597F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone228 = new ModelRenderer(this);
        bone228.setPos(0.0F, 0.0F, 0.0F);
        bone225.addChild(bone228);
        setRotationAngle(bone228, 0.0F, 0.3927F, 0.0F);
        bone228.texOffs(138, 141).addBox(-8.1237F, -10.9021F, -21.7929F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone228.texOffs(138, 141).addBox(-8.1237F, -10.9021F, -21.7929F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone238 = new ModelRenderer(this);
        bone238.setPos(15.8673F, -1.4239F, 0.5F);
        bone192.addChild(bone238);
        setRotationAngle(bone238, 0.0F, 0.0F, 0.3927F);
        bone238.texOffs(0, 127).addBox(-4.3513F, -20.9301F, -12.5F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone238.texOffs(54, 90).addBox(-3.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone238.texOffs(54, 90).addBox(-5.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone238.texOffs(141, 95).addBox(-4.8513F, -22.4301F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone238.texOffs(54, 90).addBox(-3.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone238.texOffs(54, 90).addBox(-5.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone239 = new ModelRenderer(this);
        bone239.setPos(1.3827F, 0.9239F, 1.0F);
        bone238.addChild(bone239);
        bone239.texOffs(141, 67).addBox(-5.234F, -23.354F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone239.texOffs(141, 67).addBox(-5.234F, -23.354F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone240 = new ModelRenderer(this);
        bone240.setPos(0.0F, 0.0F, 0.0F);
        bone239.addChild(bone240);
        setRotationAngle(bone240, 0.0F, -1.1781F, 0.0F);
        bone240.texOffs(89, 137).addBox(-14.8627F, -23.354F, -2.9104F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone240.texOffs(89, 137).addBox(-14.8627F, -23.354F, -2.9104F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone241 = new ModelRenderer(this);
        bone241.setPos(0.0F, 0.0F, 0.0F);
        bone239.addChild(bone241);
        setRotationAngle(bone241, 0.0F, -0.7854F, 0.0F);
        bone241.texOffs(89, 137).addBox(-13.6911F, -23.354F, -7.9934F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone241.texOffs(89, 137).addBox(-13.6911F, -23.354F, -7.9934F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone242 = new ModelRenderer(this);
        bone242.setPos(0.0F, 0.0F, 0.0F);
        bone239.addChild(bone242);
        setRotationAngle(bone242, 0.0F, -0.3927F, 0.0F);
        bone242.texOffs(89, 137).addBox(-10.6635F, -23.354F, -12.2411F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone242.texOffs(89, 137).addBox(-10.6635F, -23.354F, -12.2411F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone243 = new ModelRenderer(this);
        bone243.setPos(-3.1173F, 0.9239F, 1.0F);
        bone238.addChild(bone243);
        bone243.texOffs(141, 67).addBox(-2.7408F, -23.354F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone243.texOffs(141, 67).addBox(-2.7408F, -23.354F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone244 = new ModelRenderer(this);
        bone244.setPos(0.0F, 0.0F, 0.0F);
        bone243.addChild(bone244);
        setRotationAngle(bone244, 0.0F, 1.1781F, 0.0F);
        bone244.texOffs(89, 137).addBox(9.9526F, -23.354F, -7.5039F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone244.texOffs(89, 137).addBox(9.9526F, -23.354F, -7.5039F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone245 = new ModelRenderer(this);
        bone245.setPos(0.0F, 0.0F, 0.0F);
        bone243.addChild(bone245);
        setRotationAngle(bone245, 0.0F, 0.7854F, 0.0F);
        bone245.texOffs(89, 137).addBox(7.1686F, -23.354F, -11.5063F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone245.texOffs(89, 137).addBox(7.1686F, -23.354F, -11.5063F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone246 = new ModelRenderer(this);
        bone246.setPos(0.0F, 0.0F, 0.0F);
        bone243.addChild(bone246);
        setRotationAngle(bone246, 0.0F, 0.3927F, 0.0F);
        bone246.texOffs(89, 137).addBox(3.0648F, -23.354F, -14.1386F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone246.texOffs(89, 137).addBox(3.0648F, -23.354F, -14.1386F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone46 = new ModelRenderer(this);
        bone46.setPos(29.3673F, -0.9239F, -2.5F);
        bone192.addChild(bone46);
        setRotationAngle(bone46, 0.7854F, 0.0F, 1.5708F);
        bone46.texOffs(0, 127).addBox(-17.0F, -16.3137F, -4.5F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone46.texOffs(54, 90).addBox(-16.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone46.texOffs(54, 90).addBox(-18.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone46.texOffs(141, 95).addBox(-17.5F, -17.8137F, -6.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone46.texOffs(54, 90).addBox(-16.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone46.texOffs(54, 90).addBox(-18.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone47 = new ModelRenderer(this);
        bone47.setPos(6.0021F, -0.9895F, 1.0F);
        bone46.addChild(bone47);
        bone47.texOffs(141, 67).addBox(-22.5021F, -16.8242F, -6.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone47.texOffs(141, 67).addBox(-22.5021F, -16.8242F, -6.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone48 = new ModelRenderer(this);
        bone48.setPos(0.0F, 0.0F, 0.0F);
        bone47.addChild(bone48);
        setRotationAngle(bone48, 0.0F, -1.1781F, 0.0F);
        bone48.texOffs(89, 137).addBox(-14.0799F, -16.8242F, 16.1047F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone48.texOffs(89, 137).addBox(-14.0799F, -16.8242F, 16.1047F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone49 = new ModelRenderer(this);
        bone49.setPos(0.0F, 0.0F, 0.0F);
        bone47.addChild(bone49);
        setRotationAngle(bone49, 0.0F, -0.7854F, 0.0F);
        bone49.texOffs(89, 137).addBox(-20.2446F, -16.8242F, 9.8738F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone49.texOffs(89, 137).addBox(-20.2446F, -16.8242F, 9.8738F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone50 = new ModelRenderer(this);
        bone50.setPos(0.0F, 0.0F, 0.0F);
        bone47.addChild(bone50);
        setRotationAngle(bone50, 0.0F, -0.3927F, 0.0F);
        bone50.texOffs(89, 137).addBox(-23.5557F, -16.8242F, 1.7582F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone50.texOffs(89, 137).addBox(-23.5557F, -16.8242F, 1.7582F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone51 = new ModelRenderer(this);
        bone51.setPos(1.5021F, -0.9895F, 1.0F);
        bone46.addChild(bone51);
        bone51.texOffs(141, 67).addBox(-20.0089F, -16.8242F, -5.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone51.texOffs(141, 67).addBox(-20.0089F, -16.8242F, -5.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone52 = new ModelRenderer(this);
        bone52.setPos(0.0F, 0.0F, 0.0F);
        bone51.addChild(bone52);
        setRotationAngle(bone52, 0.0F, 1.1781F, 0.0F);
        bone52.texOffs(89, 137).addBox(-4.0466F, -16.8242F, -20.3961F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone52.texOffs(89, 137).addBox(-4.0466F, -16.8242F, -20.3961F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone53 = new ModelRenderer(this);
        bone53.setPos(0.0F, 0.0F, 0.0F);
        bone51.addChild(bone53);
        setRotationAngle(bone53, 0.0F, 0.7854F, 0.0F);
        bone53.texOffs(89, 137).addBox(-10.6987F, -16.8242F, -18.0598F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone53.texOffs(89, 137).addBox(-10.6987F, -16.8242F, -18.0598F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone54 = new ModelRenderer(this);
        bone54.setPos(0.0F, 0.0F, 0.0F);
        bone51.addChild(bone54);
        setRotationAngle(bone54, 0.0F, 0.3927F, 0.0F);
        bone54.texOffs(89, 137).addBox(-15.9504F, -16.8242F, -13.3557F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone54.texOffs(89, 137).addBox(-15.9504F, -16.8242F, -13.3557F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone247 = new ModelRenderer(this);
        bone247.setPos(24.5F, 0.0F, 0.0F);
        bone192.addChild(bone247);
        bone247.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone247.texOffs(54, 90).addBox(1.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone247.texOffs(59, 134).addBox(2.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);
        bone247.texOffs(124, 74).addBox(2.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, -0.25F, false);
        bone247.texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone247.texOffs(54, 90).addBox(1.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone247.texOffs(59, 134).addBox(2.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, -1.25F, false);

        bone248 = new ModelRenderer(this);
        bone248.setPos(2.5F, 0.0F, 1.5F);
        bone247.addChild(bone248);
        bone248.texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone248.texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone249 = new ModelRenderer(this);
        bone249.setPos(0.0F, 0.0F, 0.0F);
        bone248.addChild(bone249);
        setRotationAngle(bone249, 0.0F, -1.1781F, 0.0F);
        bone249.texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone249.texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone250 = new ModelRenderer(this);
        bone250.setPos(0.0F, 0.0F, 0.0F);
        bone248.addChild(bone250);
        setRotationAngle(bone250, 0.0F, -0.7854F, 0.0F);
        bone250.texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone250.texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone251 = new ModelRenderer(this);
        bone251.setPos(0.0F, 0.0F, 0.0F);
        bone248.addChild(bone251);
        setRotationAngle(bone251, 0.0F, -0.3927F, 0.0F);
        bone251.texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone251.texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone252 = new ModelRenderer(this);
        bone252.setPos(-2.0F, 0.0F, 1.5F);
        bone247.addChild(bone252);
        bone252.texOffs(141, 67).addBox(2.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone252.texOffs(141, 67).addBox(2.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone253 = new ModelRenderer(this);
        bone253.setPos(0.0F, 0.0F, 0.0F);
        bone252.addChild(bone253);
        setRotationAngle(bone253, 0.0F, 1.1781F, 0.0F);
        bone253.texOffs(89, 137).addBox(12.1469F, -23.5F, -2.2064F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone253.texOffs(89, 137).addBox(12.1469F, -23.5F, -2.2064F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone254 = new ModelRenderer(this);
        bone254.setPos(0.0F, 0.0F, 0.0F);
        bone252.addChild(bone254);
        setRotationAngle(bone254, 0.0F, 0.7854F, 0.0F);
        bone254.texOffs(89, 137).addBox(11.2231F, -23.5F, -7.4518F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone254.texOffs(89, 137).addBox(11.2231F, -23.5F, -7.4518F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone255 = new ModelRenderer(this);
        bone255.setPos(0.0F, 0.0F, 0.0F);
        bone252.addChild(bone255);
        setRotationAngle(bone255, 0.0F, 0.3927F, 0.0F);
        bone255.texOffs(89, 137).addBox(8.3623F, -23.5F, -11.9443F, 3.0F, 13.0F, 3.0F, -1.25F, true);
        bone255.texOffs(89, 137).addBox(8.3623F, -23.5F, -11.9443F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone55 = new ModelRenderer(this);
        bone55.setPos(-29.25F, 13.5F, 23.0F);
        bone.addChild(bone55);
        setRotationAngle(bone55, 0.8727F, 0.0F, 1.5708F);
        bone55.texOffs(74, 116).addBox(-17.5F, -16.2707F, -3.0139F, 4.0F, 10.0F, 10.0F, -0.25F, false);
        bone55.texOffs(54, 90).addBox(-15.0F, -17.7707F, -3.5139F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone55.texOffs(54, 90).addBox(-19.0F, -17.7707F, -3.5139F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone55.texOffs(103, 130).addBox(-18.0F, -17.7707F, -4.5139F, 5.0F, 13.0F, 3.0F, -1.25F, false);

        bone56 = new ModelRenderer(this);
        bone56.setPos(2.5F, 0.0F, 1.5F);
        bone55.addChild(bone56);
        bone56.texOffs(141, 67).addBox(-17.5F, -17.7707F, -5.0139F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone57 = new ModelRenderer(this);
        bone57.setPos(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone57);
        setRotationAngle(bone57, 0.0F, -1.1781F, 0.0F);
        bone57.texOffs(89, 137).addBox(-11.2547F, -17.7707F, 11.8607F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone58 = new ModelRenderer(this);
        bone58.setPos(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone58);
        setRotationAngle(bone58, 0.0F, -0.7854F, 0.0F);
        bone58.texOffs(89, 137).addBox(-16.0104F, -17.7707F, 7.0341F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone59 = new ModelRenderer(this);
        bone59.setPos(0.0F, 0.0F, 0.0F);
        bone56.addChild(bone59);
        setRotationAngle(bone59, 0.0F, -0.3927F, 0.0F);
        bone59.texOffs(89, 137).addBox(-18.557F, -17.7707F, 0.7549F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone60 = new ModelRenderer(this);
        bone60.setPos(-2.0F, 0.0F, 1.5F);
        bone55.addChild(bone60);
        bone60.texOffs(141, 67).addBox(-17.0068F, -17.7707F, -5.0071F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone61 = new ModelRenderer(this);
        bone61.setPos(0.0F, 0.0F, 0.0F);
        bone60.addChild(bone61);
        setRotationAngle(bone61, 0.0F, 1.1781F, 0.0F);
        bone61.texOffs(89, 137).addBox(-3.8088F, -17.7707F, -17.2452F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone62 = new ModelRenderer(this);
        bone62.setPos(0.0F, 0.0F, 0.0F);
        bone60.addChild(bone62);
        setRotationAngle(bone62, 0.0F, 0.7854F, 0.0F);
        bone62.texOffs(89, 137).addBox(-9.2731F, -17.7707F, -15.2398F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone63 = new ModelRenderer(this);
        bone63.setPos(0.0F, 0.0F, 0.0F);
        bone60.addChild(bone63);
        setRotationAngle(bone63, 0.0F, 0.3927F, 0.0F);
        bone63.texOffs(89, 137).addBox(-13.5541F, -17.7707F, -11.2959F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone64 = new ModelRenderer(this);
        bone64.setPos(-28.25F, 12.0F, 23.0F);
        bone.addChild(bone64);
        setRotationAngle(bone64, -0.5236F, 0.0F, -1.5708F);
        bone64.texOffs(0, 127).addBox(16.5F, 5.9282F, -6.9282F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone64.texOffs(54, 90).addBox(17.0F, 4.4282F, -7.4282F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone64.texOffs(54, 90).addBox(15.0F, 4.4282F, -7.4282F, 3.0F, 13.0F, 12.0F, -1.25F, false);
        bone64.texOffs(141, 95).addBox(16.0F, 4.4282F, -8.4282F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone65 = new ModelRenderer(this);
        bone65.setPos(2.5F, 0.0F, 1.5F);
        bone64.addChild(bone65);
        bone65.texOffs(141, 67).addBox(14.5F, 4.4282F, -8.9282F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone66 = new ModelRenderer(this);
        bone66.setPos(0.0F, 0.0F, 0.0F);
        bone65.addChild(bone66);
        setRotationAngle(bone66, 0.0F, -1.1781F, 0.0F);
        bone66.texOffs(89, 137).addBox(-2.6251F, 4.4282F, -19.2014F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone67 = new ModelRenderer(this);
        bone67.setPos(0.0F, 0.0F, 0.0F);
        bone65.addChild(bone67);
        setRotationAngle(bone67, 0.0F, -0.7854F, 0.0F);
        bone67.texOffs(89, 137).addBox(3.8492F, 4.4282F, -18.3611F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone68 = new ModelRenderer(this);
        bone68.setPos(0.0F, 0.0F, 0.0F);
        bone65.addChild(bone68);
        setRotationAngle(bone68, 0.0F, -0.3927F, 0.0F);
        bone68.texOffs(89, 137).addBox(9.5092F, 4.4282F, -15.1072F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone69 = new ModelRenderer(this);
        bone69.setPos(-2.0F, 0.0F, 1.5F);
        bone64.addChild(bone69);
        bone69.texOffs(141, 67).addBox(16.9932F, 4.4282F, -8.9214F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone70 = new ModelRenderer(this);
        bone70.setPos(0.0F, 0.0F, 0.0F);
        bone69.addChild(bone70);
        setRotationAngle(bone70, 0.0F, 1.1781F, 0.0F);
        bone70.texOffs(89, 137).addBox(12.8188F, 4.4282F, 12.6688F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone71 = new ModelRenderer(this);
        bone71.setPos(0.0F, 0.0F, 0.0F);
        bone69.addChild(bone71);
        setRotationAngle(bone71, 0.0F, 0.7854F, 0.0F);
        bone71.texOffs(89, 137).addBox(17.5363F, 4.4282F, 6.034F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone72 = new ModelRenderer(this);
        bone72.setPos(0.0F, 0.0F, 0.0F);
        bone69.addChild(bone72);
        setRotationAngle(bone72, 0.0F, 0.3927F, 0.0F);
        bone72.texOffs(89, 137).addBox(19.3557F, 4.4282F, -1.901F, 3.0F, 13.0F, 3.0F, -1.25F, true);

        bone36 = new ModelRenderer(this);
        bone36.setPos(-12.5F, 0.0F, 3.75F);
        main.addChild(bone36);
        bone36.texOffs(54, 73).addBox(6.0F, -5.0F, -10.25F, 13.0F, 3.0F, 13.0F, 0.0F, false);
        bone36.texOffs(104, 84).addBox(5.0F, -2.0F, -11.25F, 2.0F, 2.0F, 15.0F, 0.0F, false);
        bone36.texOffs(98, 51).addBox(7.0F, -2.0F, -11.25F, 11.0F, 2.0F, 2.0F, 0.0F, false);
        bone36.texOffs(98, 46).addBox(7.0F, -2.0F, 1.75F, 11.0F, 2.0F, 2.0F, 0.0F, false);
        bone36.texOffs(39, 116).addBox(18.0F, -2.0F, -11.25F, 2.0F, 2.0F, 15.0F, 0.0F, false);

        bone229 = new ModelRenderer(this);
        bone229.setPos(5.4014F, 13.8604F, 3.425F);
        bone36.addChild(bone229);
        setRotationAngle(bone229, 0.0F, 0.0F, 0.3927F);
        bone229.texOffs(78, 45).addBox(3.5016F, -30.8187F, -13.675F, 3.0F, 11.0F, 13.0F, 0.0F, false);

        book = new ModelRenderer(this);
        book.setPos(-13.25F, -13.25F, 2.0F);
        main.addChild(book);
        setRotationAngle(book, 0.2134F, -0.002F, 0.8411F);


        bone230 = new ModelRenderer(this);
        bone230.setPos(-0.6327F, 0.0761F, 2.0F);
        book.addChild(bone230);
        setRotationAngle(bone230, 0.0F, 0.0F, 1.5708F);
        bone230.texOffs(188, 27).addBox(-1.0F, -5.0F, -4.5F, 2.0F, 10.0F, 10.0F, -0.25F, false);
        bone230.texOffs(201, 65).addBox(-1.5F, -6.5F, -6.0F, 3.0F, 13.0F, 3.0F, -1.25F, false);
        bone230.texOffs(221, 0).addBox(-0.5F, -6.5F, -5.0F, 3.0F, 13.0F, 12.0F, -1.25F, false);

        bone231 = new ModelRenderer(this);
        bone231.setPos(-25.9979F, -11.5961F, 11.6066F);
        bone230.addChild(bone231);


        bone232 = new ModelRenderer(this);
        bone232.setPos(0.0F, 0.0F, 0.0F);
        bone231.addChild(bone232);
        setRotationAngle(bone232, 0.0F, -1.1781F, 0.0F);
        bone232.texOffs(231, 48).addBox(-5.5103F, 5.0961F, -32.3005F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone233 = new ModelRenderer(this);
        bone233.setPos(0.0F, 0.0F, 0.0F);
        bone231.addChild(bone233);
        setRotationAngle(bone233, 0.0F, -0.3927F, 0.0F);
        bone233.texOffs(214, 65).addBox(16.7316F, 5.0961F, -26.4099F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone234 = new ModelRenderer(this);
        bone234.setPos(-0.6327F, 0.0761F, 2.0F);
        book.addChild(bone234);
        setRotationAngle(bone234, 0.0F, 0.0F, 1.5708F);


        bone235 = new ModelRenderer(this);
        bone235.setPos(-0.75F, 0.0F, -4.25F);
        bone234.addChild(bone235);
        setRotationAngle(bone235, 0.0F, -0.0654F, 0.0F);
        bone235.texOffs(203, 48).addBox(-0.5F, -5.25F, -1.0F, 1.0F, 10.5F, 6.5F, -0.49F, false);

        bone236 = new ModelRenderer(this);
        bone236.setPos(0.0F, 0.0F, 5.0F);
        bone235.addChild(bone236);
        setRotationAngle(bone236, 0.0F, 0.0873F, 0.0F);
        bone236.texOffs(188, 82).addBox(-0.5F, -5.25F, -0.5F, 1.0F, 10.5F, 5.5F, -0.49F, false);

        bone257 = new ModelRenderer(this);
        bone257.setPos(-0.7F, 0.0F, -4.0F);
        bone234.addChild(bone257);
        setRotationAngle(bone257, -3.1416F, -0.3927F, 3.1416F);
        bone257.texOffs(213, 27).addBox(-0.7995F, -5.0F, 0.0001F, 1.0F, 10.0F, 10.0F, -0.25F, false);

        bone258 = new ModelRenderer(this);
        bone258.setPos(-1.0F, 0.0F, -4.0F);
        bone234.addChild(bone258);
        setRotationAngle(bone258, -3.1416F, -0.3927F, 3.1416F);
        bone258.texOffs(188, 0).addBox(-2.5386F, -6.5F, -1.306F, 3.0F, 13.0F, 13.0F, -1.25F, false);

        bone259 = new ModelRenderer(this);
        bone259.setPos(-25.9979F, -11.5961F, 11.6066F);
        bone234.addChild(bone259);


        bone260 = new ModelRenderer(this);
        bone260.setPos(0.0F, 0.0F, 0.0F);
        bone259.addChild(bone260);
        setRotationAngle(bone260, 0.0F, -0.7854F, 0.0F);
        bone260.texOffs(218, 48).addBox(6.1965F, 5.0961F, -31.5673F, 3.0F, 13.0F, 3.0F, -1.25F, false);

        bone261 = new ModelRenderer(this);
        bone261.setPos(0.0F, 0.0F, 0.0F);
        bone259.addChild(bone261);
        setRotationAngle(bone261, 0.0F, -0.3927F, 0.0F);
        bone261.texOffs(188, 65).addBox(16.7316F, 5.0961F, -26.4099F, 3.0F, 13.0F, 3.0F, -1.25F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}