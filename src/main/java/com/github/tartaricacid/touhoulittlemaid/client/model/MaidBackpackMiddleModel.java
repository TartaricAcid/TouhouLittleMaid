package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MaidBackpackMiddleModel extends ModelBase {
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
        textureWidth = 128;
        textureHeight = 128;

        all = new ModelRenderer(this);
        all.setRotationPoint(0.0F, 27.0F, 3.0F);

        band2 = new ModelRenderer(this);
        band2.setRotationPoint(3.0F, -10.25F, -4.0F);
        all.addChild(band2);

        bone73 = new ModelRenderer(this);
        bone73.setRotationPoint(4.0F, 12.25F, 4.0F);
        band2.addChild(bone73);
        bone73.cubeList.add(new ModelBox(bone73, 92, 106, -4.0F, -15.501F, -8.5F, 1, 0, 3, 0.0F, false));

        bone74 = new ModelRenderer(this);
        bone74.setRotationPoint(1.0F, -3.75F, -4.5F);
        setRotationAngle(bone74, 1.5708F, 0.0F, 0.0F);
        band2.addChild(bone74);
        bone74.cubeList.add(new ModelBox(bone74, 92, 106, -1.0F, -0.001F, -3.5F, 1, 0, 3, 0.0F, false));

        bone75 = new ModelRenderer(this);
        bone75.setRotationPoint(1.0F, -0.75F, -4.5F);
        setRotationAngle(bone75, 2.0944F, 0.0F, 0.0F);
        band2.addChild(bone75);
        bone75.cubeList.add(new ModelBox(bone75, 84, 106, -1.0F, -0.251F, -11.433F, 1, 0, 11, 0.0F, false));

        bone76 = new ModelRenderer(this);
        bone76.setRotationPoint(1.0F, -3.75F, -1.5F);
        setRotationAngle(bone76, -1.0123F, 0.0F, 0.0F);
        band2.addChild(bone76);
        bone76.cubeList.add(new ModelBox(bone76, 92, 106, -1.0F, 0.265F, 0.424F, 1, 0, 4, 0.0F, false));

        band = new ModelRenderer(this);
        band.setRotationPoint(-3.0F, -10.25F, -4.0F);
        all.addChild(band);

        bone69 = new ModelRenderer(this);
        bone69.setRotationPoint(3.0F, 12.25F, 4.0F);
        band.addChild(bone69);
        bone69.cubeList.add(new ModelBox(bone69, 92, 106, -4.0F, -15.501F, -8.5F, 1, 0, 3, 0.0F, false));

        bone71 = new ModelRenderer(this);
        bone71.setRotationPoint(0.0F, -3.75F, -4.5F);
        setRotationAngle(bone71, 1.5708F, 0.0F, 0.0F);
        band.addChild(bone71);
        bone71.cubeList.add(new ModelBox(bone71, 92, 106, -1.0F, -0.001F, -3.5F, 1, 0, 3, 0.0F, false));

        bone72 = new ModelRenderer(this);
        bone72.setRotationPoint(0.0F, -0.75F, -4.5F);
        setRotationAngle(bone72, 2.0944F, 0.0F, 0.0F);
        band.addChild(bone72);
        bone72.cubeList.add(new ModelBox(bone72, 84, 106, -1.0F, -0.251F, -11.433F, 1, 0, 11, 0.0F, false));

        bone70 = new ModelRenderer(this);
        bone70.setRotationPoint(0.0F, -3.75F, -1.5F);
        setRotationAngle(bone70, -1.0123F, 0.0F, 0.0F);
        band.addChild(bone70);
        bone70.cubeList.add(new ModelBox(bone70, 92, 106, -1.0F, 0.265F, 0.424F, 1, 0, 4, 0.0F, false));

        decoration = new ModelRenderer(this);
        decoration.setRotationPoint(8.2F, -1.4F, -1.5F);
        setRotationAngle(decoration, 0.0873F, 0.0F, 0.0F);
        all.addChild(decoration);
        decoration.cubeList.add(new ModelBox(decoration, 90, 56, -0.75F, -4.5019F, -1.2936F, 1, 3, 3, -0.4F, false));
        decoration.cubeList.add(new ModelBox(decoration, 64, 0, -0.85F, -3.5019F, -0.9436F, 1, 1, 1, -0.25F, false));
        decoration.cubeList.add(new ModelBox(decoration, 64, 0, -0.85F, -3.5019F, 0.3564F, 1, 1, 1, -0.25F, false));

        bone49 = new ModelRenderer(this);
        bone49.setRotationPoint(0.0F, 0.0F, 0.0F);
        decoration.addChild(bone49);
        bone49.cubeList.add(new ModelBox(bone49, 88, 57, -1.55F, -6.5019F, -1.3936F, 2, 3, 2, -0.6F, false));
        bone49.cubeList.add(new ModelBox(bone49, 88, 57, -1.55F, -6.5019F, -0.1936F, 2, 3, 2, -0.6F, false));

        bone51 = new ModelRenderer(this);
        bone51.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone51, 0.7854F, 0.0F, 0.0F);
        decoration.addChild(bone51);
        bone51.cubeList.add(new ModelBox(bone51, 64, 0, -0.7F, -1.7786F, 1.517F, 1, 1, 1, -0.4F, false));
        bone51.cubeList.add(new ModelBox(bone51, 64, 0, -0.7F, -2.1786F, 1.117F, 1, 1, 1, -0.4F, false));
        bone51.cubeList.add(new ModelBox(bone51, 64, 0, -0.7F, -1.9786F, 1.117F, 1, 1, 1, -0.4F, false));
        bone51.cubeList.add(new ModelBox(bone51, 64, 0, -0.7F, -1.9786F, 1.317F, 1, 1, 1, -0.4F, false));
        bone51.cubeList.add(new ModelBox(bone51, 64, 0, -0.7F, -1.7786F, 1.317F, 1, 1, 1, -0.4F, false));

        cone = new ModelRenderer(this);
        cone.setRotationPoint(0.1F, -0.1F, 0.0F);
        decoration.addChild(cone);
        cone.cubeList.add(new ModelBox(cone, 96, 0, -1.0F, -2.2019F, -0.2436F, 1, 3, 1, -0.3F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, -0.1571F, 0.0F, 0.0F);
        cone.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 96, 0, -1.0F, -2.2794F, -0.1713F, 1, 3, 1, -0.3F, false));

        bone52 = new ModelRenderer(this);
        bone52.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone52, 0.1571F, 0.0F, 0.0F);
        cone.addChild(bone52);
        bone52.cubeList.add(new ModelBox(bone52, 96, 0, -1.0F, -2.1992F, -0.3222F, 1, 3, 1, -0.3F, false));

        bone53 = new ModelRenderer(this);
        bone53.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone53, 0.3142F, 0.0F, 0.0F);
        cone.addChild(bone53);
        bone53.cubeList.add(new ModelBox(bone53, 96, 0, -1.0F, -2.2088F, -0.4003F, 1, 3, 1, -0.3F, false));

        bone50 = new ModelRenderer(this);
        bone50.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone50, -0.3142F, 0.0F, 0.0F);
        cone.addChild(bone50);
        bone50.cubeList.add(new ModelBox(bone50, 96, 0, -1.0F, -2.3673F, -0.112F, 1, 3, 1, -0.3F, false));

        web = new ModelRenderer(this);
        web.setRotationPoint(-0.7F, 0.0F, 0.0F);
        setRotationAngle(web, -0.5236F, 0.0F, 0.0F);
        decoration.addChild(web);
        web.cubeList.add(new ModelBox(web, 64, 0, -0.04F, -2.2468F, -1.2887F, 1, 2, 1, -0.45F, false));
        web.cubeList.add(new ModelBox(web, 64, 0, -0.04F, -1.5468F, -0.5887F, 1, 2, 1, -0.45F, false));
        web.cubeList.add(new ModelBox(web, 64, 0, -0.04F, -2.6468F, -0.5887F, 1, 2, 1, -0.45F, false));

        web2 = new ModelRenderer(this);
        web2.setRotationPoint(-0.7F, 0.0F, 0.0F);
        setRotationAngle(web2, 0.5236F, 0.0F, 0.0F);
        decoration.addChild(web2);
        web2.cubeList.add(new ModelBox(web2, 64, 0, -0.04F, -1.9904F, 0.7132F, 1, 2, 1, -0.45F, false));
        web2.cubeList.add(new ModelBox(web2, 64, 0, -0.04F, -1.2904F, 0.0132F, 1, 2, 1, -0.45F, false));
        web2.cubeList.add(new ModelBox(web2, 64, 0, -0.04F, -2.3904F, 0.0132F, 1, 2, 1, -0.45F, false));

        paw = new ModelRenderer(this);
        paw.setRotationPoint(0.0F, 0.0F, 0.0F);
        all.addChild(paw);
        paw.cubeList.add(new ModelBox(paw, 96, 64, 9.4822F, -14.3F, -1.922F, 4, 3, 3, 0.25F, false));
        paw.cubeList.add(new ModelBox(paw, 96, 64, 9.4822F, -14.3F, 1.578F, 4, 3, 2, 0.25F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(8.0F, 0.0F, -8.0F);
        setRotationAngle(bone, 0.0F, -1.5708F, 0.0F);
        paw.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 96, 64, 6.3458F, -13.8F, 0.25F, 5, 2, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 64, 6.3458F, -14.3F, -1.25F, 5, 3, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 96, 64, 6.0958F, -14.3F, -0.5F, 5, 3, 1, -0.25F, false));

        bone77 = new ModelRenderer(this);
        bone77.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone77, -0.6981F, 0.0F, 0.0F);
        bone.addChild(bone77);
        bone77.cubeList.add(new ModelBox(bone77, 96, 64, 6.3458F, -11.3749F, -7.9129F, 5, 2, 11, 0.0F, false));

        bone39 = new ModelRenderer(this);
        bone39.setRotationPoint(12.0F, -11.55F, 0.8458F);
        setRotationAngle(bone39, 0.0F, -1.5708F, 0.0F);
        paw.addChild(bone39);
        bone39.cubeList.add(new ModelBox(bone39, 96, 0, -0.5F, -0.25F, -2.0F, 1, 1, 1, 0.25F, false));
        bone39.cubeList.add(new ModelBox(bone39, 96, 0, -1.5607F, -0.25F, -0.9393F, 1, 1, 1, 0.25F, false));
        bone39.cubeList.add(new ModelBox(bone39, 96, 0, -0.5607F, 0.0F, -0.9393F, 1, 1, 1, 0.0F, false));
        bone39.cubeList.add(new ModelBox(bone39, 96, 0, -0.5F, -0.25F, 0.1213F, 1, 1, 1, 0.25F, false));
        bone39.cubeList.add(new ModelBox(bone39, 96, 0, 0.5607F, -0.25F, -0.9393F, 1, 1, 1, 0.25F, false));

        bone37 = new ModelRenderer(this);
        bone37.setRotationPoint(12.0F, -11.55F, 0.8458F);
        setRotationAngle(bone37, 0.0F, -0.7854F, 0.0F);
        paw.addChild(bone37);
        bone37.cubeList.add(new ModelBox(bone37, 96, 0, -0.1893F, -0.25F, -1.8713F, 1, 1, 1, 0.25F, false));
        bone37.cubeList.add(new ModelBox(bone37, 96, 0, -1.25F, -0.25F, -0.8107F, 1, 1, 1, 0.25F, false));
        bone37.cubeList.add(new ModelBox(bone37, 96, 0, -0.1893F, -0.25F, 0.25F, 1, 1, 1, 0.25F, false));
        bone37.cubeList.add(new ModelBox(bone37, 96, 0, 0.8713F, -0.25F, -0.8107F, 1, 1, 1, 0.25F, false));

        toe = new ModelRenderer(this);
        toe.setRotationPoint(18.25F, -13.25F, 0.75F);
        paw.addChild(toe);
        toe.cubeList.add(new ModelBox(toe, 96, 64, -5.0F, -1.05F, -0.9042F, 2, 3, 2, 0.25F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone14, 0.0F, -0.1745F, 0.1745F);
        toe.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 96, 32, -3.867F, 0.1661F, -0.2456F, 3, 1, 1, 0.0F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone21, 0.0F, 0.1745F, 0.1745F);
        toe.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 96, 32, -3.9017F, 0.1661F, -0.5575F, 3, 1, 1, 0.0F, false));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone28, 0.0F, -0.1745F, -0.1745F);
        toe.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 96, 32, -4.038F, -0.1812F, -0.2154F, 3, 1, 1, 0.0F, false));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone27, 0.0F, 0.1745F, -0.1745F);
        toe.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 96, 32, -4.0727F, -0.1812F, -0.5876F, 3, 1, 1, 0.0F, false));

        toe2 = new ModelRenderer(this);
        toe2.setRotationPoint(18.25F, -13.25F, 0.75F);
        setRotationAngle(toe2, 0.0F, -0.7854F, 0.0F);
        paw.addChild(toe2);
        toe2.cubeList.add(new ModelBox(toe2, 96, 64, -3.243F, -1.05F, 3.1461F, 2, 3, 2, 0.25F, false));

        bone29 = new ModelRenderer(this);
        bone29.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone29, 0.0F, -0.1745F, 0.1745F);
        toe2.addChild(bone29);
        bone29.cubeList.add(new ModelBox(bone29, 96, 32, -1.4596F, -0.1391F, 3.4428F, 3, 1, 1, 0.0F, false));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone30, 0.0F, 0.1745F, 0.1745F);
        toe2.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 96, 32, -2.901F, -0.1391F, 3.7319F, 3, 1, 1, 0.0F, false));

        bone31 = new ModelRenderer(this);
        bone31.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone31, 0.0F, -0.1745F, -0.1745F);
        toe2.addChild(bone31);
        bone31.cubeList.add(new ModelBox(bone31, 96, 32, -1.6306F, 0.1239F, 3.4729F, 3, 1, 1, 0.0F, false));

        bone32 = new ModelRenderer(this);
        bone32.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone32, 0.0F, 0.1745F, -0.1745F);
        toe2.addChild(bone32);
        bone32.cubeList.add(new ModelBox(bone32, 96, 32, -3.072F, 0.1239F, 3.7017F, 3, 1, 1, 0.0F, false));

        toe3 = new ModelRenderer(this);
        toe3.setRotationPoint(18.25F, -13.25F, 0.75F);
        setRotationAngle(toe3, 0.0F, 0.7854F, 0.0F);
        paw.addChild(toe3);
        toe3.cubeList.add(new ModelBox(toe3, 96, 64, -3.3784F, -1.05F, -5.0107F, 2, 3, 2, 0.25F, false));

        bone33 = new ModelRenderer(this);
        bone33.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone33, 0.0F, -0.1745F, 0.1745F);
        toe3.addChild(bone33);
        bone33.cubeList.add(new ModelBox(bone33, 96, 32, -3.0073F, -0.1155F, -4.567F, 3, 1, 1, 0.0F, false));

        bone34 = new ModelRenderer(this);
        bone34.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone34, 0.0F, 0.1745F, 0.1745F);
        toe3.addChild(bone34);
        bone34.cubeList.add(new ModelBox(bone34, 96, 32, -1.6159F, -0.1155F, -4.3242F, 3, 1, 1, 0.0F, false));

        bone35 = new ModelRenderer(this);
        bone35.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone35, 0.0F, -0.1745F, -0.1745F);
        toe3.addChild(bone35);
        bone35.cubeList.add(new ModelBox(bone35, 96, 32, -3.1783F, 0.1003F, -4.5368F, 3, 1, 1, 0.0F, false));

        bone36 = new ModelRenderer(this);
        bone36.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone36, 0.0F, 0.1745F, -0.1745F);
        toe3.addChild(bone36);
        bone36.cubeList.add(new ModelBox(bone36, 96, 32, -1.7869F, 0.1003F, -4.3544F, 3, 1, 1, 0.0F, false));

        bag = new ModelRenderer(this);
        bag.setRotationPoint(0.0F, 0.0F, 0.0F);
        all.addChild(bag);
        bag.cubeList.add(new ModelBox(bag, 94, 96, -8.0F, -11.8F, -4.0F, 16, 9, 1, 0.0F, false));

        upper = new ModelRenderer(this);
        upper.setRotationPoint(0.0F, 0.0F, 0.0F);
        bag.addChild(upper);

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, -16.0F, -4.0F);
        setRotationAngle(bone2, -0.2618F, 0.0F, 0.0F);
        upper.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 94, 96, -8.0F, 3.0569F, 1.087F, 16, 1, 1, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, -16.0F, -4.0F);
        setRotationAngle(bone3, -0.5236F, 0.0F, 0.0F);
        upper.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 94, 96, -8.0F, 1.6714F, 1.8412F, 16, 1, 1, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, -16.0F, -4.0F);
        setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);
        upper.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 94, 96, -8.0F, 0.1379F, 2.211F, 16, 1, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, -16.0F, -4.0F);
        setRotationAngle(bone5, -1.0472F, 0.0F, 0.0F);
        upper.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 94, 96, -8.0F, -1.4391F, 2.1714F, 16, 1, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, -16.0F, -4.0F);
        setRotationAngle(bone6, -1.309F, 0.0F, 0.0F);
        upper.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 94, 96, -8.0F, -2.952F, 1.7249F, 16, 1, 1, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, -16.0F, -4.0F);
        setRotationAngle(bone7, -1.5708F, 0.0F, 0.0F);
        upper.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 94, 96, -8.0F, -6.2979F, 0.9021F, 16, 3, 1, 0.0F, false));

        lower = new ModelRenderer(this);
        lower.setRotationPoint(0.0F, -16.0F, 0.0F);
        bag.addChild(lower);

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(0.0F, 16.0F, -4.0F);
        setRotationAngle(bone15, 0.2618F, 0.0F, 0.0F);
        lower.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 94, 96, -8.0F, -2.7046F, 0.7247F, 16, 1, 1, 0.0F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, 16.0F, -4.0F);
        setRotationAngle(bone16, 0.5236F, 0.0F, 0.0F);
        lower.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 94, 96, -8.0F, -1.4589F, 1.1412F, 16, 1, 1, 0.0F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(0.0F, 16.0F, -4.0F);
        setRotationAngle(bone17, 0.7854F, 0.0F, 0.0F);
        lower.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 94, 96, -8.0F, -0.1479F, 1.2211F, 16, 1, 1, 0.0F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, 16.0F, -4.0F);
        setRotationAngle(bone18, 1.0472F, 0.0F, 0.0F);
        lower.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 94, 96, -8.0F, 1.1391F, 0.9589F, 16, 1, 1, 0.0F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(0.0F, 16.0F, -4.0F);
        setRotationAngle(bone19, 1.309F, 0.0F, 0.0F);
        lower.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 94, 96, -8.0F, 2.3144F, 0.3726F, 16, 1, 1, 0.0F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 16.0F, -4.0F);
        setRotationAngle(bone20, 1.5708F, 0.0F, 0.0F);
        lower.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 94, 96, -8.0F, 3.2979F, -0.4979F, 16, 4, 1, 0.0F, false));

        upper2 = new ModelRenderer(this);
        upper2.setRotationPoint(0.0F, 0.0F, 0.0F);
        bag.addChild(upper2);

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, -16.0F, 4.0F);
        setRotationAngle(bone9, 0.2618F, 0.0F, 0.0F);
        upper2.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 94, 96, -8.0F, 3.4699F, -0.5457F, 16, 4, 1, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, -16.0F, 4.0F);
        setRotationAngle(bone10, 0.5236F, 0.0F, 0.0F);
        upper2.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 94, 96, -8.0F, 2.4693F, -1.4592F, 16, 1, 1, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 64, 25, 2.25F, 0.9693F, -2.4092F, 4, 4, 3, -1.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 64, 25, -6.25F, 0.9693F, -2.4092F, 4, 4, 3, -1.0F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, -16.0F, 4.0F);
        setRotationAngle(bone11, 0.7854F, 0.0F, 0.0F);
        upper2.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 94, 96, -8.0F, 1.2663F, -2.0827F, 16, 1, 1, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, -16.0F, 4.0F);
        setRotationAngle(bone12, 1.0472F, 0.0F, 0.0F);
        upper2.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 94, 96, -8.0F, -0.0571F, -2.3735F, 16, 1, 1, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, -16.0F, 4.0F);
        setRotationAngle(bone13, 1.309F, 0.0F, 0.0F);
        upper2.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 94, 96, -8.0F, -1.4106F, -2.3119F, 16, 1, 1, 0.0F, false));

        lower2 = new ModelRenderer(this);
        lower2.setRotationPoint(0.0F, -16.0F, 2.1F);
        bag.addChild(lower2);

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(0.0F, 16.0F, 4.0F);
        setRotationAngle(bone22, -0.2618F, 0.0F, 0.0F);
        lower2.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 94, 96, -8.0F, -2.8329F, -1.2458F, 16, 1, 1, 0.0F, false));

        bone40 = new ModelRenderer(this);
        bone40.setRotationPoint(0.0F, 16.0F, 4.0F);
        lower2.addChild(bone40);
        bone40.cubeList.add(new ModelBox(bone40, 94, 96, -8.0F, -3.8F, -0.5042F, 16, 1, 1, 0.0F, false));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(0.0F, 16.0F, 4.0F);
        setRotationAngle(bone23, -0.5236F, 0.0F, 0.0F);
        lower2.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 94, 96, -8.0F, -1.7068F, -1.7118F, 16, 1, 1, 0.0F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(0.0F, 16.0F, 4.0F);
        setRotationAngle(bone24, -0.7854F, 0.0F, 0.0F);
        lower2.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 94, 96, -8.0F, -0.4985F, -1.8705F, 16, 1, 1, 0.0F, false));

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(0.0F, 16.0F, 4.0F);
        setRotationAngle(bone25, -1.0472F, 0.0F, 0.0F);
        lower2.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 94, 96, -8.0F, 0.7097F, -1.7111F, 16, 1, 1, 0.0F, false));

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(0.0F, 16.0F, 4.0F);
        setRotationAngle(bone26, -1.309F, 0.0F, 0.0F);
        lower2.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 94, 96, -8.0F, 1.8355F, -1.2443F, 16, 1, 1, 0.0F, false));

        bone44 = new ModelRenderer(this);
        bone44.setRotationPoint(0.0F, 16.0F, 0.0F);
        setRotationAngle(bone44, 0.2618F, 0.0F, 0.0F);
        lower2.addChild(bone44);
        bone44.cubeList.add(new ModelBox(bone44, 94, 96, -8.0F, -11.5069F, 4.3261F, 16, 9, 1, 0.0F, false));

        feet = new ModelRenderer(this);
        feet.setRotationPoint(4.0F, 16.0F, 4.0F);
        lower2.addChild(feet);
        feet.cubeList.add(new ModelBox(feet, 64, 0, -0.5F, -4.5F, 0.0F, 1, 2, 1, -0.3F, false));

        bone65 = new ModelRenderer(this);
        bone65.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone65, -0.2618F, 0.0F, 0.0F);
        feet.addChild(bone65);
        bone65.cubeList.add(new ModelBox(bone65, 64, 0, -0.5F, -3.1858F, -0.7485F, 1, 2, 1, -0.3F, false));

        bone63 = new ModelRenderer(this);
        bone63.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone63, 0.0F, 0.0F, -1.0472F);
        feet.addChild(bone63);
        bone63.cubeList.add(new ModelBox(bone63, 64, 0, 2.0249F, -3.2732F, 0.0F, 1, 2, 1, -0.3F, false));

        bone64 = new ModelRenderer(this);
        bone64.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone64, 0.0F, 0.0F, 1.0472F);
        feet.addChild(bone64);
        bone64.cubeList.add(new ModelBox(bone64, 64, 0, -3.0249F, -3.2732F, 0.0F, 1, 2, 1, -0.3F, false));

        feet2 = new ModelRenderer(this);
        feet2.setRotationPoint(-4.0F, 16.0F, 4.0F);
        lower2.addChild(feet2);
        feet2.cubeList.add(new ModelBox(feet2, 64, 0, -0.5F, -4.5F, 0.0F, 1, 2, 1, -0.3F, false));

        bone66 = new ModelRenderer(this);
        bone66.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone66, -0.2618F, 0.0F, 0.0F);
        feet2.addChild(bone66);
        bone66.cubeList.add(new ModelBox(bone66, 64, 0, -0.5F, -3.1858F, -0.7485F, 1, 2, 1, -0.3F, false));

        bone67 = new ModelRenderer(this);
        bone67.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone67, 0.0F, 0.0F, -1.0472F);
        feet2.addChild(bone67);
        bone67.cubeList.add(new ModelBox(bone67, 64, 0, 2.0249F, -3.2732F, 0.0F, 1, 2, 1, -0.3F, false));

        bone68 = new ModelRenderer(this);
        bone68.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone68, 0.0F, 0.0F, 1.0472F);
        feet2.addChild(bone68);
        bone68.cubeList.add(new ModelBox(bone68, 64, 0, -3.0249F, -3.2732F, 0.0F, 1, 2, 1, -0.3F, false));

        bagLeft = new ModelRenderer(this);
        bagLeft.setRotationPoint(-0.1F, 0.0F, -0.25F);
        setRotationAngle(bagLeft, 0.0F, -1.5708F, 0.0F);
        bag.addChild(bagLeft);
        bagLeft.cubeList.add(new ModelBox(bagLeft, 89, 106, -3.0F, -11.8F, -8.0F, 7, 10, 0, 0.0F, false));
        bagLeft.cubeList.add(new ModelBox(bagLeft, 88, 116, -2.5F, -1.8F, -8.0F, 8, 1, 0, 0.0F, false));
        bagLeft.cubeList.add(new ModelBox(bagLeft, 90, 117, -1.5F, -0.8F, -8.0F, 6, 1, 0, 0.0F, false));

        bone45 = new ModelRenderer(this);
        bone45.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone45, 0.0F, 0.0F, -0.2618F);
        bagLeft.addChild(bone45);
        bone45.cubeList.add(new ModelBox(bone45, 94, 106, 3.6296F, -10.3034F, -8.0F, 3, 10, 0, 0.0F, false));

        bagRight = new ModelRenderer(this);
        bagRight.setRotationPoint(0.1F, 0.0F, -0.25F);
        setRotationAngle(bagRight, 0.0F, 1.5708F, 0.0F);
        bag.addChild(bagRight);
        bagRight.cubeList.add(new ModelBox(bagRight, 89, 106, -4.0F, -11.8F, -8.0F, 7, 10, 0, 0.0F, true));
        bagRight.cubeList.add(new ModelBox(bagRight, 88, 116, -5.5F, -1.8F, -8.0F, 8, 1, 0, 0.0F, true));
        bagRight.cubeList.add(new ModelBox(bagRight, 90, 117, -4.5F, -0.8F, -8.0F, 6, 1, 0, 0.0F, true));

        bone46 = new ModelRenderer(this);
        bone46.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone46, 0.0F, 0.0F, 0.2618F);
        bagRight.addChild(bone46);
        bone46.cubeList.add(new ModelBox(bone46, 94, 106, -6.6296F, -10.3034F, -8.0F, 3, 10, 0, 0.0F, true));

        pocketLeft = new ModelRenderer(this);
        pocketLeft.setRotationPoint(0.0F, -1.5F, 0.9F);
        bag.addChild(pocketLeft);
        pocketLeft.cubeList.add(new ModelBox(pocketLeft, 96, 120, 7.9F, -8.5F, -1.45F, 1, 4, 4, 0.0F, false));
        pocketLeft.cubeList.add(new ModelBox(pocketLeft, 96, 32, 8.9F, -8.6F, -1.45F, 0, 1, 4, 0.1F, false));
        pocketLeft.cubeList.add(new ModelBox(pocketLeft, 96, 32, 7.7F, -8.6F, -1.45F, 1, 0, 4, 0.1F, false));

        bone38 = new ModelRenderer(this);
        bone38.setRotationPoint(-1.6F, -7.5F, 0.25F);
        setRotationAngle(bone38, -0.7854F, 0.0F, 0.0F);
        pocketLeft.addChild(bone38);
        bone38.cubeList.add(new ModelBox(bone38, 124, 126, 10.0F, -1.005F, -0.9879F, 1, 1, 1, -0.3F, false));
        bone38.cubeList.add(new ModelBox(bone38, 124, 126, 10.0F, -1.005F, -0.5879F, 1, 1, 1, -0.3F, false));
        bone38.cubeList.add(new ModelBox(bone38, 124, 126, 10.0F, -1.405F, -0.5879F, 1, 1, 1, -0.3F, false));

        pocketRight = new ModelRenderer(this);
        pocketRight.setRotationPoint(0.4F, 0.0F, 0.3F);
        bag.addChild(pocketRight);
        pocketRight.cubeList.add(new ModelBox(pocketRight, 124, 123, -10.0F, -8.5F, 0.0F, 1, 5, 1, 0.0F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 96, 32, -10.0F, -5.25F, 0.0F, 1, 1, 1, 0.1F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 96, 32, -10.0F, -7.75F, 0.0F, 1, 1, 1, 0.1F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 96, 32, -9.1515F, -5.25F, -0.8485F, 1, 1, 1, 0.1F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 96, 32, -9.1515F, -7.75F, -0.8485F, 1, 1, 1, 0.1F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 96, 32, -9.1515F, -5.25F, 0.8485F, 1, 1, 1, 0.1F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 96, 32, -9.1515F, -7.75F, 0.8485F, 1, 1, 1, 0.1F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 124, 123, -9.2929F, -8.5F, -0.7071F, 1, 5, 1, 0.0F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 124, 123, -9.2929F, -8.5F, 0.7071F, 1, 5, 1, 0.0F, false));
        pocketRight.cubeList.add(new ModelBox(pocketRight, 124, 123, -9.2929F, -3.7929F, 0.0F, 1, 1, 1, 0.0F, false));

        bone56 = new ModelRenderer(this);
        bone56.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone56, 0.0F, 0.0F, 0.7854F);
        pocketRight.addChild(bone56);
        bone56.cubeList.add(new ModelBox(bone56, 124, 123, -9.5459F, 3.5962F, 0.0F, 1, 1, 1, 0.0F, false));

        bone55 = new ModelRenderer(this);
        bone55.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone55, -0.7854F, 0.0F, 0.0F);
        pocketRight.addChild(bone55);
        bone55.cubeList.add(new ModelBox(bone55, 124, 123, -9.2929F, -2.9749F, -2.9749F, 1, 1, 1, 0.0F, false));
        bone55.cubeList.add(new ModelBox(bone55, 124, 123, -9.2929F, -3.682F, -2.2678F, 1, 1, 1, 0.0F, false));

        bone54 = new ModelRenderer(this);
        bone54.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone54, 0.0F, -0.7854F, 0.0F);
        pocketRight.addChild(bone54);
        bone54.cubeList.add(new ModelBox(bone54, 124, 123, -6.364F, -8.5F, 6.7782F, 1, 5, 1, 0.0F, false));
        bone54.cubeList.add(new ModelBox(bone54, 124, 123, -7.0711F, -8.5F, 6.0711F, 1, 5, 1, 0.0F, false));
        bone54.cubeList.add(new ModelBox(bone54, 96, 32, -7.1125F, -5.25F, 5.9711F, 1, 1, 1, 0.1F, false));
        bone54.cubeList.add(new ModelBox(bone54, 96, 32, -7.1125F, -7.75F, 5.9711F, 1, 1, 1, 0.1F, false));
        bone54.cubeList.add(new ModelBox(bone54, 96, 32, -6.264F, -5.25F, 6.8196F, 1, 1, 1, 0.1F, false));
        bone54.cubeList.add(new ModelBox(bone54, 96, 32, -6.264F, -7.75F, 6.8196F, 1, 1, 1, 0.1F, false));

        bear = new ModelRenderer(this);
        bear.setRotationPoint(-0.3F, 0.0F, -0.55F);
        pocketRight.addChild(bear);
        bear.cubeList.add(new ModelBox(bear, 64, 46, -13.5F, -14.0F, -3.5F, 9, 9, 9, -3.5F, false));
        bear.cubeList.add(new ModelBox(bear, 94, 62, -9.5F, -10.0F, 0.5F, 1, 1, 1, 0.4F, false));
        bear.cubeList.add(new ModelBox(bear, 90, 58, -10.0F, -11.35F, -0.85F, 1, 2, 2, -0.45F, false));
        bear.cubeList.add(new ModelBox(bear, 90, 58, -10.0F, -11.35F, 0.85F, 1, 2, 2, -0.45F, false));

        beak = new ModelRenderer(this);
        beak.setRotationPoint(0.0F, -11.2F, 7.2F);
        setRotationAngle(beak, -0.2618F, 0.0F, 0.0F);
        bag.addChild(beak);

        bone57 = new ModelRenderer(this);
        bone57.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone57, -0.0873F, 0.0F, 0.0F);
        beak.addChild(bone57);
        bone57.cubeList.add(new ModelBox(bone57, 96, 0, -0.5F, -0.0302F, -1.829F, 1, 1, 2, 0.0F, false));

        bone58 = new ModelRenderer(this);
        bone58.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone58, 0.1745F, 0.0F, 0.0F);
        beak.addChild(bone58);
        bone58.cubeList.add(new ModelBox(bone58, 96, 0, -0.5F, -0.0019F, -1.9564F, 1, 1, 2, 0.0F, false));

        bone61 = new ModelRenderer(this);
        bone61.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone61, 0.0873F, 0.5236F, 0.0F);
        beak.addChild(bone61);
        bone61.cubeList.add(new ModelBox(bone61, 96, 0, -0.5195F, -0.0238F, -3.0985F, 1, 1, 3, 0.0F, false));

        bone62 = new ModelRenderer(this);
        bone62.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone62, 0.0873F, -0.5236F, 0.0F);
        beak.addChild(bone62);
        bone62.cubeList.add(new ModelBox(bone62, 96, 0, -0.4805F, -0.0238F, -3.0985F, 1, 1, 3, 0.0F, false));

        bone59 = new ModelRenderer(this);
        bone59.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone59, 0.0F, -0.2618F, 0.0F);
        beak.addChild(bone59);
        bone59.cubeList.add(new ModelBox(bone59, 102, 0, -0.4723F, -0.0151F, -1.9623F, 1, 1, 2, 0.0F, false));

        bone60 = new ModelRenderer(this);
        bone60.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone60, 0.0F, 0.2618F, 0.0F);
        beak.addChild(bone60);
        bone60.cubeList.add(new ModelBox(bone60, 102, 0, -0.5277F, -0.0151F, -1.9623F, 1, 1, 2, 0.0F, false));

        sign = new ModelRenderer(this);
        sign.setRotationPoint(7.0F, 1.2F, 3.6F);
        all.addChild(sign);

        bone47 = new ModelRenderer(this);
        bone47.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone47, 0.0F, 3.1416F, 0.0F);
        sign.addChild(bone47);
        bone47.cubeList.add(new ModelBox(bone47, 0, 64, -12.001F, -17.7F, -12.0F, 24, 24, 24, -11.1F, false));

        bone43 = new ModelRenderer(this);
        bone43.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone43, 0.0F, -1.5708F, 0.0F);
        sign.addChild(bone43);
        bone43.cubeList.add(new ModelBox(bone43, 0, 64, -12.0F, -15.9F, -12.001F, 24, 24, 24, -11.1F, false));

        bone48 = new ModelRenderer(this);
        bone48.setRotationPoint(0.0F, 0.0F, 0.0F);
        sign.addChild(bone48);
        bone48.cubeList.add(new ModelBox(bone48, 0, 112, -3.399F, -6.7F, -3.4F, 8, 8, 8, -3.7F, false));

        bone41 = new ModelRenderer(this);
        bone41.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone41, 0.0F, -1.5708F, 0.0F);
        sign.addChild(bone41);
        bone41.cubeList.add(new ModelBox(bone41, 0, 112, -4.0F, -6.7F, -4.601F, 8, 8, 8, -3.7F, false));

        bone42 = new ModelRenderer(this);
        bone42.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone42, 0.0F, 3.1416F, 0.0F);
        sign.addChild(bone42);
        bone42.cubeList.add(new ModelBox(bone42, 0, 112, -4.601F, -6.7F, -3.4F, 8, 8, 8, -3.7F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        all.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}