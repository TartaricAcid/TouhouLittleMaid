package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MaidBackpackMiddleModel extends EntityModel<EntityMaid> {
    private final ModelRenderer all;
    private final ModelRenderer band2;
    private final ModelRenderer bone73;
    private final ModelRenderer bone74;
    private final ModelRenderer bone75;
    private final ModelRenderer bone76;
    private final ModelRenderer band;
    private final ModelRenderer bone69;
    private final ModelRenderer bone71;
    private final ModelRenderer bone72;
    private final ModelRenderer bone70;
    private final ModelRenderer decoration;
    private final ModelRenderer bone49;
    private final ModelRenderer bone51;
    private final ModelRenderer cone;
    private final ModelRenderer bone8;
    private final ModelRenderer bone52;
    private final ModelRenderer bone53;
    private final ModelRenderer bone50;
    private final ModelRenderer web;
    private final ModelRenderer web2;
    private final ModelRenderer paw;
    private final ModelRenderer bone;
    private final ModelRenderer bone77;
    private final ModelRenderer bone39;
    private final ModelRenderer bone37;
    private final ModelRenderer toe;
    private final ModelRenderer bone14;
    private final ModelRenderer bone21;
    private final ModelRenderer bone28;
    private final ModelRenderer bone27;
    private final ModelRenderer toe2;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer toe3;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;
    private final ModelRenderer bone36;
    private final ModelRenderer bag;
    private final ModelRenderer upper;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer lower;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer upper2;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer lower2;
    private final ModelRenderer bone22;
    private final ModelRenderer bone40;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone44;
    private final ModelRenderer feet;
    private final ModelRenderer bone65;
    private final ModelRenderer bone63;
    private final ModelRenderer bone64;
    private final ModelRenderer feet2;
    private final ModelRenderer bone66;
    private final ModelRenderer bone67;
    private final ModelRenderer bone68;
    private final ModelRenderer bagLeft;
    private final ModelRenderer bone45;
    private final ModelRenderer bagRight;
    private final ModelRenderer bone46;
    private final ModelRenderer pocketLeft;
    private final ModelRenderer bone38;
    private final ModelRenderer pocketRight;
    private final ModelRenderer bone56;
    private final ModelRenderer bone55;
    private final ModelRenderer bone54;
    private final ModelRenderer bear;
    private final ModelRenderer beak;
    private final ModelRenderer bone57;
    private final ModelRenderer bone58;
    private final ModelRenderer bone61;
    private final ModelRenderer bone62;
    private final ModelRenderer bone59;
    private final ModelRenderer bone60;
    private final ModelRenderer sign;
    private final ModelRenderer bone47;
    private final ModelRenderer bone43;
    private final ModelRenderer bone48;
    private final ModelRenderer bone41;
    private final ModelRenderer bone42;

    public MaidBackpackMiddleModel() {
        texWidth = 128;
        texHeight = 128;

        all = new ModelRenderer(this);
        all.setPos(0.0F, 27.0F, 3.0F);

        band2 = new ModelRenderer(this);
        band2.setPos(3.0F, -10.25F, -4.0F);
        all.addChild(band2);

        bone73 = new ModelRenderer(this);
        bone73.setPos(4.0F, 12.25F, 4.0F);
        band2.addChild(bone73);
        bone73.texOffs(92, 106).addBox(-4.0F, -15.501F, -8.5F, 1.0F, 0.0F, 3.0F, 0.0F, false);

        bone74 = new ModelRenderer(this);
        bone74.setPos(1.0F, -3.75F, -4.5F);
        band2.addChild(bone74);
        setRotationAngle(bone74, 1.5708F, 0.0F, 0.0F);
        bone74.texOffs(92, 106).addBox(-1.0F, -0.001F, -3.5F, 1.0F, 0.0F, 3.0F, 0.0F, false);

        bone75 = new ModelRenderer(this);
        bone75.setPos(1.0F, -0.75F, -4.5F);
        band2.addChild(bone75);
        setRotationAngle(bone75, 2.0944F, 0.0F, 0.0F);
        bone75.texOffs(84, 106).addBox(-1.0F, -0.251F, -11.433F, 1.0F, 0.0F, 11.0F, 0.0F, false);

        bone76 = new ModelRenderer(this);
        bone76.setPos(1.0F, -3.75F, -1.5F);
        band2.addChild(bone76);
        setRotationAngle(bone76, -1.0123F, 0.0F, 0.0F);
        bone76.texOffs(92, 106).addBox(-1.0F, 0.265F, 0.424F, 1.0F, 0.0F, 4.0F, 0.0F, false);

        band = new ModelRenderer(this);
        band.setPos(-3.0F, -10.25F, -4.0F);
        all.addChild(band);

        bone69 = new ModelRenderer(this);
        bone69.setPos(3.0F, 12.25F, 4.0F);
        band.addChild(bone69);
        bone69.texOffs(92, 106).addBox(-4.0F, -15.501F, -8.5F, 1.0F, 0.0F, 3.0F, 0.0F, false);

        bone71 = new ModelRenderer(this);
        bone71.setPos(0.0F, -3.75F, -4.5F);
        band.addChild(bone71);
        setRotationAngle(bone71, 1.5708F, 0.0F, 0.0F);
        bone71.texOffs(92, 106).addBox(-1.0F, -0.001F, -3.5F, 1.0F, 0.0F, 3.0F, 0.0F, false);

        bone72 = new ModelRenderer(this);
        bone72.setPos(0.0F, -0.75F, -4.5F);
        band.addChild(bone72);
        setRotationAngle(bone72, 2.0944F, 0.0F, 0.0F);
        bone72.texOffs(84, 106).addBox(-1.0F, -0.251F, -11.433F, 1.0F, 0.0F, 11.0F, 0.0F, false);

        bone70 = new ModelRenderer(this);
        bone70.setPos(0.0F, -3.75F, -1.5F);
        band.addChild(bone70);
        setRotationAngle(bone70, -1.0123F, 0.0F, 0.0F);
        bone70.texOffs(92, 106).addBox(-1.0F, 0.265F, 0.424F, 1.0F, 0.0F, 4.0F, 0.0F, false);

        decoration = new ModelRenderer(this);
        decoration.setPos(8.2F, -1.4F, -1.5F);
        all.addChild(decoration);
        setRotationAngle(decoration, 0.0873F, 0.0F, 0.0F);
        decoration.texOffs(90, 56).addBox(-0.75F, -4.5019F, -1.2936F, 1.0F, 3.0F, 3.0F, -0.4F, false);
        decoration.texOffs(64, 0).addBox(-0.85F, -3.5019F, -0.9436F, 1.0F, 1.0F, 1.0F, -0.25F, false);
        decoration.texOffs(64, 0).addBox(-0.85F, -3.5019F, 0.3564F, 1.0F, 1.0F, 1.0F, -0.25F, false);

        bone49 = new ModelRenderer(this);
        bone49.setPos(0.0F, 0.0F, 0.0F);
        decoration.addChild(bone49);
        bone49.texOffs(88, 57).addBox(-1.55F, -6.5019F, -1.3936F, 2.0F, 3.0F, 2.0F, -0.6F, false);
        bone49.texOffs(88, 57).addBox(-1.55F, -6.5019F, -0.1936F, 2.0F, 3.0F, 2.0F, -0.6F, false);

        bone51 = new ModelRenderer(this);
        bone51.setPos(0.0F, 0.0F, 0.0F);
        decoration.addChild(bone51);
        setRotationAngle(bone51, 0.7854F, 0.0F, 0.0F);
        bone51.texOffs(64, 0).addBox(-0.7F, -1.7786F, 1.517F, 1.0F, 1.0F, 1.0F, -0.4F, false);
        bone51.texOffs(64, 0).addBox(-0.7F, -2.1786F, 1.117F, 1.0F, 1.0F, 1.0F, -0.4F, false);
        bone51.texOffs(64, 0).addBox(-0.7F, -1.9786F, 1.117F, 1.0F, 1.0F, 1.0F, -0.4F, false);
        bone51.texOffs(64, 0).addBox(-0.7F, -1.9786F, 1.317F, 1.0F, 1.0F, 1.0F, -0.4F, false);
        bone51.texOffs(64, 0).addBox(-0.7F, -1.7786F, 1.317F, 1.0F, 1.0F, 1.0F, -0.4F, false);

        cone = new ModelRenderer(this);
        cone.setPos(0.1F, -0.1F, 0.0F);
        decoration.addChild(cone);
        cone.texOffs(96, 0).addBox(-1.0F, -2.2019F, -0.2436F, 1.0F, 3.0F, 1.0F, -0.3F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        cone.addChild(bone8);
        setRotationAngle(bone8, -0.1571F, 0.0F, 0.0F);
        bone8.texOffs(96, 0).addBox(-1.0F, -2.2794F, -0.1713F, 1.0F, 3.0F, 1.0F, -0.3F, false);

        bone52 = new ModelRenderer(this);
        bone52.setPos(0.0F, 0.0F, 0.0F);
        cone.addChild(bone52);
        setRotationAngle(bone52, 0.1571F, 0.0F, 0.0F);
        bone52.texOffs(96, 0).addBox(-1.0F, -2.1992F, -0.3222F, 1.0F, 3.0F, 1.0F, -0.3F, false);

        bone53 = new ModelRenderer(this);
        bone53.setPos(0.0F, 0.0F, 0.0F);
        cone.addChild(bone53);
        setRotationAngle(bone53, 0.3142F, 0.0F, 0.0F);
        bone53.texOffs(96, 0).addBox(-1.0F, -2.2088F, -0.4003F, 1.0F, 3.0F, 1.0F, -0.3F, false);

        bone50 = new ModelRenderer(this);
        bone50.setPos(0.0F, 0.0F, 0.0F);
        cone.addChild(bone50);
        setRotationAngle(bone50, -0.3142F, 0.0F, 0.0F);
        bone50.texOffs(96, 0).addBox(-1.0F, -2.3673F, -0.112F, 1.0F, 3.0F, 1.0F, -0.3F, false);

        web = new ModelRenderer(this);
        web.setPos(-0.7F, 0.0F, 0.0F);
        decoration.addChild(web);
        setRotationAngle(web, -0.5236F, 0.0F, 0.0F);
        web.texOffs(64, 0).addBox(-0.04F, -2.2468F, -1.2887F, 1.0F, 2.0F, 1.0F, -0.45F, false);
        web.texOffs(64, 0).addBox(-0.04F, -1.5468F, -0.5887F, 1.0F, 2.0F, 1.0F, -0.45F, false);
        web.texOffs(64, 0).addBox(-0.04F, -2.6468F, -0.5887F, 1.0F, 2.0F, 1.0F, -0.45F, false);

        web2 = new ModelRenderer(this);
        web2.setPos(-0.7F, 0.0F, 0.0F);
        decoration.addChild(web2);
        setRotationAngle(web2, 0.5236F, 0.0F, 0.0F);
        web2.texOffs(64, 0).addBox(-0.04F, -1.9904F, 0.7132F, 1.0F, 2.0F, 1.0F, -0.45F, false);
        web2.texOffs(64, 0).addBox(-0.04F, -1.2904F, 0.0132F, 1.0F, 2.0F, 1.0F, -0.45F, false);
        web2.texOffs(64, 0).addBox(-0.04F, -2.3904F, 0.0132F, 1.0F, 2.0F, 1.0F, -0.45F, false);

        paw = new ModelRenderer(this);
        paw.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(paw);
        paw.texOffs(96, 64).addBox(9.4822F, -14.3F, -1.922F, 4.0F, 3.0F, 3.0F, 0.25F, false);
        paw.texOffs(96, 64).addBox(9.4822F, -14.3F, 1.578F, 4.0F, 3.0F, 2.0F, 0.25F, false);

        bone = new ModelRenderer(this);
        bone.setPos(8.0F, 0.0F, -8.0F);
        paw.addChild(bone);
        setRotationAngle(bone, 0.0F, -1.5708F, 0.0F);
        bone.texOffs(96, 64).addBox(6.3458F, -13.8F, 0.25F, 5.0F, 2.0F, 1.0F, 0.0F, false);
        bone.texOffs(96, 64).addBox(6.3458F, -14.3F, -1.25F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone.texOffs(96, 64).addBox(6.0958F, -14.3F, -0.5F, 5.0F, 3.0F, 1.0F, -0.25F, false);

        bone77 = new ModelRenderer(this);
        bone77.setPos(0.0F, 0.0F, 0.0F);
        bone.addChild(bone77);
        setRotationAngle(bone77, -0.6981F, 0.0F, 0.0F);
        bone77.texOffs(96, 64).addBox(6.3458F, -11.3749F, -7.9129F, 5.0F, 2.0F, 11.0F, 0.0F, false);

        bone39 = new ModelRenderer(this);
        bone39.setPos(12.0F, -11.55F, 0.8458F);
        paw.addChild(bone39);
        setRotationAngle(bone39, 0.0F, -1.5708F, 0.0F);
        bone39.texOffs(96, 0).addBox(-0.5F, -0.25F, -2.0F, 1.0F, 1.0F, 1.0F, 0.25F, false);
        bone39.texOffs(96, 0).addBox(-1.5607F, -0.25F, -0.9393F, 1.0F, 1.0F, 1.0F, 0.25F, false);
        bone39.texOffs(96, 0).addBox(-0.5607F, 0.0F, -0.9393F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone39.texOffs(96, 0).addBox(-0.5F, -0.25F, 0.1213F, 1.0F, 1.0F, 1.0F, 0.25F, false);
        bone39.texOffs(96, 0).addBox(0.5607F, -0.25F, -0.9393F, 1.0F, 1.0F, 1.0F, 0.25F, false);

        bone37 = new ModelRenderer(this);
        bone37.setPos(12.0F, -11.55F, 0.8458F);
        paw.addChild(bone37);
        setRotationAngle(bone37, 0.0F, -0.7854F, 0.0F);
        bone37.texOffs(96, 0).addBox(-0.1893F, -0.25F, -1.8713F, 1.0F, 1.0F, 1.0F, 0.25F, false);
        bone37.texOffs(96, 0).addBox(-1.25F, -0.25F, -0.8107F, 1.0F, 1.0F, 1.0F, 0.25F, false);
        bone37.texOffs(96, 0).addBox(-0.1893F, -0.25F, 0.25F, 1.0F, 1.0F, 1.0F, 0.25F, false);
        bone37.texOffs(96, 0).addBox(0.8713F, -0.25F, -0.8107F, 1.0F, 1.0F, 1.0F, 0.25F, false);

        toe = new ModelRenderer(this);
        toe.setPos(18.25F, -13.25F, 0.75F);
        paw.addChild(toe);
        toe.texOffs(96, 64).addBox(-5.0F, -1.05F, -0.9042F, 2.0F, 3.0F, 2.0F, 0.25F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, 0.0F, 0.0F);
        toe.addChild(bone14);
        setRotationAngle(bone14, 0.0F, -0.1745F, 0.1745F);
        bone14.texOffs(96, 32).addBox(-3.867F, 0.1661F, -0.2456F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setPos(0.0F, 0.0F, 0.0F);
        toe.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.1745F, 0.1745F);
        bone21.texOffs(96, 32).addBox(-3.9017F, 0.1661F, -0.5575F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone28 = new ModelRenderer(this);
        bone28.setPos(0.0F, 0.0F, 0.0F);
        toe.addChild(bone28);
        setRotationAngle(bone28, 0.0F, -0.1745F, -0.1745F);
        bone28.texOffs(96, 32).addBox(-4.038F, -0.1812F, -0.2154F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(0.0F, 0.0F, 0.0F);
        toe.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.1745F, -0.1745F);
        bone27.texOffs(96, 32).addBox(-4.0727F, -0.1812F, -0.5876F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        toe2 = new ModelRenderer(this);
        toe2.setPos(18.25F, -13.25F, 0.75F);
        paw.addChild(toe2);
        setRotationAngle(toe2, 0.0F, -0.7854F, 0.0F);
        toe2.texOffs(96, 64).addBox(-3.243F, -1.05F, 3.1461F, 2.0F, 3.0F, 2.0F, 0.25F, false);

        bone29 = new ModelRenderer(this);
        bone29.setPos(0.0F, 0.0F, 0.0F);
        toe2.addChild(bone29);
        setRotationAngle(bone29, 0.0F, -0.1745F, 0.1745F);
        bone29.texOffs(96, 32).addBox(-1.4596F, -0.1391F, 3.4428F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(0.0F, 0.0F, 0.0F);
        toe2.addChild(bone30);
        setRotationAngle(bone30, 0.0F, 0.1745F, 0.1745F);
        bone30.texOffs(96, 32).addBox(-2.901F, -0.1391F, 3.7319F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(0.0F, 0.0F, 0.0F);
        toe2.addChild(bone31);
        setRotationAngle(bone31, 0.0F, -0.1745F, -0.1745F);
        bone31.texOffs(96, 32).addBox(-1.6306F, 0.1239F, 3.4729F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone32 = new ModelRenderer(this);
        bone32.setPos(0.0F, 0.0F, 0.0F);
        toe2.addChild(bone32);
        setRotationAngle(bone32, 0.0F, 0.1745F, -0.1745F);
        bone32.texOffs(96, 32).addBox(-3.072F, 0.1239F, 3.7017F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        toe3 = new ModelRenderer(this);
        toe3.setPos(18.25F, -13.25F, 0.75F);
        paw.addChild(toe3);
        setRotationAngle(toe3, 0.0F, 0.7854F, 0.0F);
        toe3.texOffs(96, 64).addBox(-3.3784F, -1.05F, -5.0107F, 2.0F, 3.0F, 2.0F, 0.25F, false);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.0F, 0.0F, 0.0F);
        toe3.addChild(bone33);
        setRotationAngle(bone33, 0.0F, -0.1745F, 0.1745F);
        bone33.texOffs(96, 32).addBox(-3.0073F, -0.1155F, -4.567F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone34 = new ModelRenderer(this);
        bone34.setPos(0.0F, 0.0F, 0.0F);
        toe3.addChild(bone34);
        setRotationAngle(bone34, 0.0F, 0.1745F, 0.1745F);
        bone34.texOffs(96, 32).addBox(-1.6159F, -0.1155F, -4.3242F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone35 = new ModelRenderer(this);
        bone35.setPos(0.0F, 0.0F, 0.0F);
        toe3.addChild(bone35);
        setRotationAngle(bone35, 0.0F, -0.1745F, -0.1745F);
        bone35.texOffs(96, 32).addBox(-3.1783F, 0.1003F, -4.5368F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone36 = new ModelRenderer(this);
        bone36.setPos(0.0F, 0.0F, 0.0F);
        toe3.addChild(bone36);
        setRotationAngle(bone36, 0.0F, 0.1745F, -0.1745F);
        bone36.texOffs(96, 32).addBox(-1.7869F, 0.1003F, -4.3544F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bag = new ModelRenderer(this);
        bag.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(bag);
        bag.texOffs(94, 96).addBox(-8.0F, -11.8F, -4.0F, 16.0F, 9.0F, 1.0F, 0.0F, false);

        upper = new ModelRenderer(this);
        upper.setPos(0.0F, 0.0F, 0.0F);
        bag.addChild(upper);
        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, -16.0F, -4.0F);
        upper.addChild(bone2);
        setRotationAngle(bone2, -0.2618F, 0.0F, 0.0F);
        bone2.texOffs(94, 96).addBox(-8.0F, 3.0569F, 1.087F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, -16.0F, -4.0F);
        upper.addChild(bone3);
        setRotationAngle(bone3, -0.5236F, 0.0F, 0.0F);
        bone3.texOffs(94, 96).addBox(-8.0F, 1.6714F, 1.8412F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -16.0F, -4.0F);
        upper.addChild(bone4);
        setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);
        bone4.texOffs(94, 96).addBox(-8.0F, 0.1379F, 2.211F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -16.0F, -4.0F);
        upper.addChild(bone5);
        setRotationAngle(bone5, -1.0472F, 0.0F, 0.0F);
        bone5.texOffs(94, 96).addBox(-8.0F, -1.4391F, 2.1714F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, -16.0F, -4.0F);
        upper.addChild(bone6);
        setRotationAngle(bone6, -1.309F, 0.0F, 0.0F);
        bone6.texOffs(94, 96).addBox(-8.0F, -2.952F, 1.7249F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, -16.0F, -4.0F);
        upper.addChild(bone7);
        setRotationAngle(bone7, -1.5708F, 0.0F, 0.0F);
        bone7.texOffs(94, 96).addBox(-8.0F, -6.2979F, 0.9021F, 16.0F, 3.0F, 1.0F, 0.0F, false);

        lower = new ModelRenderer(this);
        lower.setPos(0.0F, -16.0F, 0.0F);
        bag.addChild(lower);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, 16.0F, -4.0F);
        lower.addChild(bone15);
        setRotationAngle(bone15, 0.2618F, 0.0F, 0.0F);
        bone15.texOffs(94, 96).addBox(-8.0F, -2.7046F, 0.7247F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, 16.0F, -4.0F);
        lower.addChild(bone16);
        setRotationAngle(bone16, 0.5236F, 0.0F, 0.0F);
        bone16.texOffs(94, 96).addBox(-8.0F, -1.4589F, 1.1412F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, 16.0F, -4.0F);
        lower.addChild(bone17);
        setRotationAngle(bone17, 0.7854F, 0.0F, 0.0F);
        bone17.texOffs(94, 96).addBox(-8.0F, -0.1479F, 1.2211F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, 16.0F, -4.0F);
        lower.addChild(bone18);
        setRotationAngle(bone18, 1.0472F, 0.0F, 0.0F);
        bone18.texOffs(94, 96).addBox(-8.0F, 1.1391F, 0.9589F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, 16.0F, -4.0F);
        lower.addChild(bone19);
        setRotationAngle(bone19, 1.309F, 0.0F, 0.0F);
        bone19.texOffs(94, 96).addBox(-8.0F, 2.3144F, 0.3726F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, 16.0F, -4.0F);
        lower.addChild(bone20);
        setRotationAngle(bone20, 1.5708F, 0.0F, 0.0F);
        bone20.texOffs(94, 96).addBox(-8.0F, 3.2979F, -0.4979F, 16.0F, 4.0F, 1.0F, 0.0F, false);

        upper2 = new ModelRenderer(this);
        upper2.setPos(0.0F, 0.0F, 0.0F);
        bag.addChild(upper2);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, -16.0F, 4.0F);
        upper2.addChild(bone9);
        setRotationAngle(bone9, 0.2618F, 0.0F, 0.0F);
        bone9.texOffs(94, 96).addBox(-8.0F, 3.4699F, -0.5457F, 16.0F, 4.0F, 1.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, -16.0F, 4.0F);
        upper2.addChild(bone10);
        setRotationAngle(bone10, 0.5236F, 0.0F, 0.0F);
        bone10.texOffs(94, 96).addBox(-8.0F, 2.4693F, -1.4592F, 16.0F, 1.0F, 1.0F, 0.0F, false);
        bone10.texOffs(64, 25).addBox(2.25F, 0.9693F, -2.4092F, 4.0F, 4.0F, 3.0F, -1.0F, false);
        bone10.texOffs(64, 25).addBox(-6.25F, 0.9693F, -2.4092F, 4.0F, 4.0F, 3.0F, -1.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, -16.0F, 4.0F);
        upper2.addChild(bone11);
        setRotationAngle(bone11, 0.7854F, 0.0F, 0.0F);
        bone11.texOffs(94, 96).addBox(-8.0F, 1.2663F, -2.0827F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, -16.0F, 4.0F);
        upper2.addChild(bone12);
        setRotationAngle(bone12, 1.0472F, 0.0F, 0.0F);
        bone12.texOffs(94, 96).addBox(-8.0F, -0.0571F, -2.3735F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, -16.0F, 4.0F);
        upper2.addChild(bone13);
        setRotationAngle(bone13, 1.309F, 0.0F, 0.0F);
        bone13.texOffs(94, 96).addBox(-8.0F, -1.4106F, -2.3119F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        lower2 = new ModelRenderer(this);
        lower2.setPos(0.0F, -16.0F, 2.1F);
        bag.addChild(lower2);

        bone22 = new ModelRenderer(this);
        bone22.setPos(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone22);
        setRotationAngle(bone22, -0.2618F, 0.0F, 0.0F);
        bone22.texOffs(94, 96).addBox(-8.0F, -2.8329F, -1.2458F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone40 = new ModelRenderer(this);
        bone40.setPos(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone40);
        bone40.texOffs(94, 96).addBox(-8.0F, -3.8F, -0.5042F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone23 = new ModelRenderer(this);
        bone23.setPos(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone23);
        setRotationAngle(bone23, -0.5236F, 0.0F, 0.0F);
        bone23.texOffs(94, 96).addBox(-8.0F, -1.7068F, -1.7118F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone24);
        setRotationAngle(bone24, -0.7854F, 0.0F, 0.0F);
        bone24.texOffs(94, 96).addBox(-8.0F, -0.4985F, -1.8705F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone25);
        setRotationAngle(bone25, -1.0472F, 0.0F, 0.0F);
        bone25.texOffs(94, 96).addBox(-8.0F, 0.7097F, -1.7111F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone26);
        setRotationAngle(bone26, -1.309F, 0.0F, 0.0F);
        bone26.texOffs(94, 96).addBox(-8.0F, 1.8355F, -1.2443F, 16.0F, 1.0F, 1.0F, 0.0F, false);

        bone44 = new ModelRenderer(this);
        bone44.setPos(0.0F, 16.0F, 0.0F);
        lower2.addChild(bone44);
        setRotationAngle(bone44, 0.2618F, 0.0F, 0.0F);
        bone44.texOffs(94, 96).addBox(-8.0F, -11.5069F, 4.3261F, 16.0F, 9.0F, 1.0F, 0.0F, false);

        feet = new ModelRenderer(this);
        feet.setPos(4.0F, 16.0F, 4.0F);
        lower2.addChild(feet);
        feet.texOffs(64, 0).addBox(-0.5F, -4.5F, 0.0F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bone65 = new ModelRenderer(this);
        bone65.setPos(0.0F, 0.0F, 0.0F);
        feet.addChild(bone65);
        setRotationAngle(bone65, -0.2618F, 0.0F, 0.0F);
        bone65.texOffs(64, 0).addBox(-0.5F, -3.1858F, -0.7485F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bone63 = new ModelRenderer(this);
        bone63.setPos(0.0F, 0.0F, 0.0F);
        feet.addChild(bone63);
        setRotationAngle(bone63, 0.0F, 0.0F, -1.0472F);
        bone63.texOffs(64, 0).addBox(2.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bone64 = new ModelRenderer(this);
        bone64.setPos(0.0F, 0.0F, 0.0F);
        feet.addChild(bone64);
        setRotationAngle(bone64, 0.0F, 0.0F, 1.0472F);
        bone64.texOffs(64, 0).addBox(-3.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        feet2 = new ModelRenderer(this);
        feet2.setPos(-4.0F, 16.0F, 4.0F);
        lower2.addChild(feet2);
        feet2.texOffs(64, 0).addBox(-0.5F, -4.5F, 0.0F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bone66 = new ModelRenderer(this);
        bone66.setPos(0.0F, 0.0F, 0.0F);
        feet2.addChild(bone66);
        setRotationAngle(bone66, -0.2618F, 0.0F, 0.0F);
        bone66.texOffs(64, 0).addBox(-0.5F, -3.1858F, -0.7485F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bone67 = new ModelRenderer(this);
        bone67.setPos(0.0F, 0.0F, 0.0F);
        feet2.addChild(bone67);
        setRotationAngle(bone67, 0.0F, 0.0F, -1.0472F);
        bone67.texOffs(64, 0).addBox(2.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bone68 = new ModelRenderer(this);
        bone68.setPos(0.0F, 0.0F, 0.0F);
        feet2.addChild(bone68);
        setRotationAngle(bone68, 0.0F, 0.0F, 1.0472F);
        bone68.texOffs(64, 0).addBox(-3.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, -0.3F, false);

        bagLeft = new ModelRenderer(this);
        bagLeft.setPos(-0.1F, 0.0F, -0.25F);
        bag.addChild(bagLeft);
        setRotationAngle(bagLeft, 0.0F, -1.5708F, 0.0F);
        bagLeft.texOffs(89, 106).addBox(-3.0F, -11.8F, -8.0F, 7.0F, 10.0F, 0.0F, 0.0F, false);
        bagLeft.texOffs(88, 116).addBox(-2.5F, -1.8F, -8.0F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bagLeft.texOffs(90, 117).addBox(-1.5F, -0.8F, -8.0F, 6.0F, 1.0F, 0.0F, 0.0F, false);

        bone45 = new ModelRenderer(this);
        bone45.setPos(0.0F, 0.0F, 0.0F);
        bagLeft.addChild(bone45);
        setRotationAngle(bone45, 0.0F, 0.0F, -0.2618F);
        bone45.texOffs(94, 106).addBox(3.6296F, -10.3034F, -8.0F, 3.0F, 10.0F, 0.0F, 0.0F, false);

        bagRight = new ModelRenderer(this);
        bagRight.setPos(0.1F, 0.0F, -0.25F);
        bag.addChild(bagRight);
        setRotationAngle(bagRight, 0.0F, 1.5708F, 0.0F);
        bagRight.texOffs(89, 106).addBox(-4.0F, -11.8F, -8.0F, 7.0F, 10.0F, 0.0F, 0.0F, true);
        bagRight.texOffs(88, 116).addBox(-5.5F, -1.8F, -8.0F, 8.0F, 1.0F, 0.0F, 0.0F, true);
        bagRight.texOffs(90, 117).addBox(-4.5F, -0.8F, -8.0F, 6.0F, 1.0F, 0.0F, 0.0F, true);

        bone46 = new ModelRenderer(this);
        bone46.setPos(0.0F, 0.0F, 0.0F);
        bagRight.addChild(bone46);
        setRotationAngle(bone46, 0.0F, 0.0F, 0.2618F);
        bone46.texOffs(94, 106).addBox(-6.6296F, -10.3034F, -8.0F, 3.0F, 10.0F, 0.0F, 0.0F, true);

        pocketLeft = new ModelRenderer(this);
        pocketLeft.setPos(0.0F, -1.5F, 0.9F);
        bag.addChild(pocketLeft);
        pocketLeft.texOffs(96, 120).addBox(7.9F, -8.5F, -1.45F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        pocketLeft.texOffs(96, 32).addBox(8.9F, -8.6F, -1.45F, 0.0F, 1.0F, 4.0F, 0.1F, false);
        pocketLeft.texOffs(96, 32).addBox(7.7F, -8.6F, -1.45F, 1.0F, 0.0F, 4.0F, 0.1F, false);

        bone38 = new ModelRenderer(this);
        bone38.setPos(-1.6F, -7.5F, 0.25F);
        pocketLeft.addChild(bone38);
        setRotationAngle(bone38, -0.7854F, 0.0F, 0.0F);
        bone38.texOffs(124, 126).addBox(10.0F, -1.005F, -0.9879F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        bone38.texOffs(124, 126).addBox(10.0F, -1.005F, -0.5879F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        bone38.texOffs(124, 126).addBox(10.0F, -1.405F, -0.5879F, 1.0F, 1.0F, 1.0F, -0.3F, false);

        pocketRight = new ModelRenderer(this);
        pocketRight.setPos(0.4F, 0.0F, 0.3F);
        bag.addChild(pocketRight);
        pocketRight.texOffs(124, 123).addBox(-10.0F, -8.5F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        pocketRight.texOffs(96, 32).addBox(-10.0F, -5.25F, 0.0F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        pocketRight.texOffs(96, 32).addBox(-10.0F, -7.75F, 0.0F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        pocketRight.texOffs(96, 32).addBox(-9.1515F, -5.25F, -0.8485F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        pocketRight.texOffs(96, 32).addBox(-9.1515F, -7.75F, -0.8485F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        pocketRight.texOffs(96, 32).addBox(-9.1515F, -5.25F, 0.8485F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        pocketRight.texOffs(96, 32).addBox(-9.1515F, -7.75F, 0.8485F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        pocketRight.texOffs(124, 123).addBox(-9.2929F, -8.5F, -0.7071F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        pocketRight.texOffs(124, 123).addBox(-9.2929F, -8.5F, 0.7071F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        pocketRight.texOffs(124, 123).addBox(-9.2929F, -3.7929F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone56 = new ModelRenderer(this);
        bone56.setPos(0.0F, 0.0F, 0.0F);
        pocketRight.addChild(bone56);
        setRotationAngle(bone56, 0.0F, 0.0F, 0.7854F);
        bone56.texOffs(124, 123).addBox(-9.5459F, 3.5962F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone55 = new ModelRenderer(this);
        bone55.setPos(0.0F, 0.0F, 0.0F);
        pocketRight.addChild(bone55);
        setRotationAngle(bone55, -0.7854F, 0.0F, 0.0F);
        bone55.texOffs(124, 123).addBox(-9.2929F, -2.9749F, -2.9749F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone55.texOffs(124, 123).addBox(-9.2929F, -3.682F, -2.2678F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone54 = new ModelRenderer(this);
        bone54.setPos(0.0F, 0.0F, 0.0F);
        pocketRight.addChild(bone54);
        setRotationAngle(bone54, 0.0F, -0.7854F, 0.0F);
        bone54.texOffs(124, 123).addBox(-6.364F, -8.5F, 6.7782F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        bone54.texOffs(124, 123).addBox(-7.0711F, -8.5F, 6.0711F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        bone54.texOffs(96, 32).addBox(-7.1125F, -5.25F, 5.9711F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        bone54.texOffs(96, 32).addBox(-7.1125F, -7.75F, 5.9711F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        bone54.texOffs(96, 32).addBox(-6.264F, -5.25F, 6.8196F, 1.0F, 1.0F, 1.0F, 0.1F, false);
        bone54.texOffs(96, 32).addBox(-6.264F, -7.75F, 6.8196F, 1.0F, 1.0F, 1.0F, 0.1F, false);

        bear = new ModelRenderer(this);
        bear.setPos(-0.3F, 0.0F, -0.55F);
        pocketRight.addChild(bear);
        bear.texOffs(64, 46).addBox(-13.5F, -14.0F, -3.5F, 9.0F, 9.0F, 9.0F, -3.5F, false);
        bear.texOffs(94, 62).addBox(-9.5F, -10.0F, 0.5F, 1.0F, 1.0F, 1.0F, 0.4F, false);
        bear.texOffs(90, 58).addBox(-10.0F, -11.35F, -0.85F, 1.0F, 2.0F, 2.0F, -0.45F, false);
        bear.texOffs(90, 58).addBox(-10.0F, -11.35F, 0.85F, 1.0F, 2.0F, 2.0F, -0.45F, false);

        beak = new ModelRenderer(this);
        beak.setPos(0.0F, -11.2F, 7.2F);
        bag.addChild(beak);
        setRotationAngle(beak, -0.2618F, 0.0F, 0.0F);
        bone57 = new ModelRenderer(this);
        bone57.setPos(0.0F, 0.0F, 0.0F);
        beak.addChild(bone57);
        setRotationAngle(bone57, -0.0873F, 0.0F, 0.0F);
        bone57.texOffs(96, 0).addBox(-0.5F, -0.0302F, -1.829F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone58 = new ModelRenderer(this);
        bone58.setPos(0.0F, 0.0F, 0.0F);
        beak.addChild(bone58);
        setRotationAngle(bone58, 0.1745F, 0.0F, 0.0F);
        bone58.texOffs(96, 0).addBox(-0.5F, -0.0019F, -1.9564F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone61 = new ModelRenderer(this);
        bone61.setPos(0.0F, 0.0F, 0.0F);
        beak.addChild(bone61);
        setRotationAngle(bone61, 0.0873F, 0.5236F, 0.0F);
        bone61.texOffs(96, 0).addBox(-0.5195F, -0.0238F, -3.0985F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        bone62 = new ModelRenderer(this);
        bone62.setPos(0.0F, 0.0F, 0.0F);
        beak.addChild(bone62);
        setRotationAngle(bone62, 0.0873F, -0.5236F, 0.0F);
        bone62.texOffs(96, 0).addBox(-0.4805F, -0.0238F, -3.0985F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        bone59 = new ModelRenderer(this);
        bone59.setPos(0.0F, 0.0F, 0.0F);
        beak.addChild(bone59);
        setRotationAngle(bone59, 0.0F, -0.2618F, 0.0F);
        bone59.texOffs(102, 0).addBox(-0.4723F, -0.0151F, -1.9623F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone60 = new ModelRenderer(this);
        bone60.setPos(0.0F, 0.0F, 0.0F);
        beak.addChild(bone60);
        setRotationAngle(bone60, 0.0F, 0.2618F, 0.0F);
        bone60.texOffs(102, 0).addBox(-0.5277F, -0.0151F, -1.9623F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        sign = new ModelRenderer(this);
        sign.setPos(7.0F, 1.2F, 3.6F);
        all.addChild(sign);
        bone47 = new ModelRenderer(this);
        bone47.setPos(0.0F, 0.0F, 0.0F);
        sign.addChild(bone47);
        setRotationAngle(bone47, 0.0F, 3.1416F, 0.0F);
        bone47.texOffs(0, 64).addBox(-12.001F, -17.7F, -12.0F, 24.0F, 24.0F, 24.0F, -11.1F, false);

        bone43 = new ModelRenderer(this);
        bone43.setPos(0.0F, 0.0F, 0.0F);
        sign.addChild(bone43);
        setRotationAngle(bone43, 0.0F, -1.5708F, 0.0F);
        bone43.texOffs(0, 64).addBox(-12.0F, -15.9F, -12.001F, 24.0F, 24.0F, 24.0F, -11.1F, false);

        bone48 = new ModelRenderer(this);
        bone48.setPos(0.0F, 0.0F, 0.0F);
        sign.addChild(bone48);
        bone48.texOffs(0, 112).addBox(-3.399F, -6.7F, -3.4F, 8.0F, 8.0F, 8.0F, -3.7F, false);

        bone41 = new ModelRenderer(this);
        bone41.setPos(0.0F, 0.0F, 0.0F);
        sign.addChild(bone41);
        setRotationAngle(bone41, 0.0F, -1.5708F, 0.0F);
        bone41.texOffs(0, 112).addBox(-4.0F, -6.7F, -4.601F, 8.0F, 8.0F, 8.0F, -3.7F, false);

        bone42 = new ModelRenderer(this);
        bone42.setPos(0.0F, 0.0F, 0.0F);
        sign.addChild(bone42);
        setRotationAngle(bone42, 0.0F, 3.1416F, 0.0F);
        bone42.texOffs(0, 112).addBox(-4.601F, -6.7F, -3.4F, 8.0F, 8.0F, 8.0F, -3.7F, false);
    }

    @Override
    public void setupAnim(EntityMaid entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        all.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}