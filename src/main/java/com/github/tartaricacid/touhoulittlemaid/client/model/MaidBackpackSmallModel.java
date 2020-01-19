package com.github.tartaricacid.touhoulittlemaid.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MaidBackpackSmallModel extends ModelBase {
    private final ModelRenderer all;
    private final ModelRenderer bandRight;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;
    private final ModelRenderer bone36;
    private final ModelRenderer bone37;
    private final ModelRenderer bone46;
    private final ModelRenderer bone47;
    private final ModelRenderer pedobear;
    private final ModelRenderer bone45;
    private final ModelRenderer bone43;
    private final ModelRenderer handle;
    private final ModelRenderer bone38;
    private final ModelRenderer bone39;
    private final ModelRenderer bone41;
    private final ModelRenderer bone40;
    private final ModelRenderer bone42;
    private final ModelRenderer bandLeft;
    private final ModelRenderer bone15;
    private final ModelRenderer bone11;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone16;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer cap;
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
    private final ModelRenderer bag;
    private final ModelRenderer bone12;
    private final ModelRenderer bone44;

    public MaidBackpackSmallModel() {
        textureWidth = 64;
        textureHeight = 64;

        all = new ModelRenderer(this);
        all.setRotationPoint(0.0F, 24.0F, 1.0F);

        bandRight = new ModelRenderer(this);
        bandRight.setRotationPoint(-2.8F, -8.0F, -2.0F);
        setRotationAngle(bandRight, 0.0F, 0.0873F, 0.0F);
        all.addChild(bandRight);

        bone26 = new ModelRenderer(this);
        bone26.setRotationPoint(3.0F, 8.0F, 2.0F);
        setRotationAngle(bone26, -2.8798F, 0.0F, 0.0F);
        bandRight.addChild(bone26);
        bone26.cubeList.add(new ModelBox(bone26, 21, 6, -6.6F, 5.5119F, -2.6664F, 6, 6, 5, -2.4F, true));

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(3.0F, 8.0F, 2.0F);
        setRotationAngle(bone27, -2.4435F, 0.0F, 0.0F);
        bandRight.addChild(bone27);
        bone27.cubeList.add(new ModelBox(bone27, 21, 6, -6.6F, 5.7456F, -6.4923F, 6, 6, 5, -2.4F, true));

        bone28 = new ModelRenderer(this);
        bone28.setRotationPoint(3.0F, 7.0F, 3.0F);
        setRotationAngle(bone28, -1.9199F, 0.0F, 0.0F);
        bandRight.addChild(bone28);
        bone28.cubeList.add(new ModelBox(bone28, 21, 6, -6.6F, 4.245F, -9.3351F, 6, 6, 5, -2.4F, true));

        bone29 = new ModelRenderer(this);
        bone29.setRotationPoint(3.0F, 8.0F, 2.0F);
        setRotationAngle(bone29, -1.5708F, 0.0F, 0.0F);
        bandRight.addChild(bone29);
        bone29.cubeList.add(new ModelBox(bone29, 21, 6, -6.6F, 1.6F, -12.6F, 6, 6, 5, -2.4F, true));
        bone29.cubeList.add(new ModelBox(bone29, 21, 6, -6.6F, 2.8F, -12.6F, 6, 6, 5, -2.4F, true));

        bone30 = new ModelRenderer(this);
        bone30.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone30, -0.7854F, 0.0F, 0.0F);
        bandRight.addChild(bone30);
        bone30.cubeList.add(new ModelBox(bone30, 21, 6, -6.6F, -6.5012F, -12.7238F, 6, 6, 5, -2.4F, true));

        bone31 = new ModelRenderer(this);
        bone31.setRotationPoint(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone31);
        bone31.cubeList.add(new ModelBox(bone31, 21, 6, -6.6F, -11.7515F, -7.6485F, 6, 6, 5, -2.4F, true));

        bone32 = new ModelRenderer(this);
        bone32.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone32, 0.2618F, 0.0F, 0.0F);
        bandRight.addChild(bone32);
        bone32.cubeList.add(new ModelBox(bone32, 21, 6, -6.6F, -11.6321F, -5.3599F, 6, 6, 5, -2.4F, true));
        bone32.cubeList.add(new ModelBox(bone32, 21, 6, -6.6F, -10.4321F, -5.3599F, 6, 6, 5, -2.4F, true));

        bone33 = new ModelRenderer(this);
        bone33.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone33, 0.4363F, 0.0F, 0.0F);
        bandRight.addChild(bone33);
        bone33.cubeList.add(new ModelBox(bone33, 21, 6, -6.6F, -9.6423F, -4.1286F, 6, 6, 5, -2.4F, true));
        bone33.cubeList.add(new ModelBox(bone33, 21, 6, -6.6F, -8.4423F, -4.1286F, 6, 6, 5, -2.4F, true));

        bone34 = new ModelRenderer(this);
        bone34.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone34, 0.6109F, 0.0F, 0.0F);
        bandRight.addChild(bone34);
        bone34.cubeList.add(new ModelBox(bone34, 21, 6, -6.6F, -7.4689F, -3.2615F, 6, 6, 5, -2.4F, true));
        bone34.cubeList.add(new ModelBox(bone34, 21, 6, -6.6F, -6.2689F, -3.2615F, 6, 6, 5, -2.4F, true));

        bone35 = new ModelRenderer(this);
        bone35.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone35, 0.6981F, 0.0F, 0.0F);
        bandRight.addChild(bone35);
        bone35.cubeList.add(new ModelBox(bone35, 21, 6, -6.6F, -5.1339F, -3.0256F, 6, 6, 5, -2.4F, true));

        bone36 = new ModelRenderer(this);
        bone36.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone36, 0.7854F, 0.0F, 0.0F);
        bandRight.addChild(bone36);
        bone36.cubeList.add(new ModelBox(bone36, 21, 6, -6.6F, -3.9825F, -2.8895F, 6, 6, 5, -2.4F, true));

        bone37 = new ModelRenderer(this);
        bone37.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone37, 1.3963F, 0.0F, 0.0F);
        bandRight.addChild(bone37);
        bone37.cubeList.add(new ModelBox(bone37, 21, 6, -6.6F, -2.9941F, -2.5816F, 6, 6, 5, -2.4F, true));

        bone46 = new ModelRenderer(this);
        bone46.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone46, 1.5708F, 0.0F, 0.0F);
        bandRight.addChild(bone46);
        bone46.cubeList.add(new ModelBox(bone46, 21, 6, -6.6F, -1.8349F, -2.684F, 6, 6, 5, -2.4F, true));

        bone47 = new ModelRenderer(this);
        bone47.setRotationPoint(3.0F, 8.0F, 0.0F);
        setRotationAngle(bone47, 1.7453F, 0.0F, 0.0F);
        bandRight.addChild(bone47);
        bone47.cubeList.add(new ModelBox(bone47, 21, 6, -6.6F, -0.711F, -2.9862F, 6, 6, 5, -2.4F, true));

        pedobear = new ModelRenderer(this);
        pedobear.setRotationPoint(0.0F, 0.0F, 0.0F);
        all.addChild(pedobear);
        pedobear.cubeList.add(new ModelBox(pedobear, 29, 39, -4.6F, -6.7F, -1.3F, 1, 3, 3, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 0, 32, -4.5F, -6.9F, 0.9F, 1, 1, 1, -0.1F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 0, 32, -4.5F, -6.9F, -1.5F, 1, 1, 1, -0.1F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 12, 32, -4.7F, -6.1F, 0.5F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 12, 32, -4.7F, -6.1F, -1.1F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 12, 32, -4.7F, -5.3F, -0.3F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 23, 0, -4.7F, -5.3F, 0.1F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 23, 0, -4.8F, -6.9F, 0.9F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 23, 0, -4.8F, -6.9F, -1.5F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 23, 0, -4.7F, -5.3F, -0.7F, 1, 1, 1, -0.3F, false));
        pedobear.cubeList.add(new ModelBox(pedobear, 16, 32, -4.7F, -4.9F, -0.3F, 1, 1, 1, -0.3F, false));

        bone45 = new ModelRenderer(this);
        bone45.setRotationPoint(-0.85F, -1.35F, -1.8F);
        setRotationAngle(bone45, 0.0F, 0.0F, 0.5236F);
        pedobear.addChild(bone45);
        bone45.cubeList.add(new ModelBox(bone45, 0, 14, -6.5602F, -5.2816F, 1.5F, 1, 2, 1, -0.35F, false));

        bone43 = new ModelRenderer(this);
        bone43.setRotationPoint(1.6F, -4.1F, -1.8F);
        setRotationAngle(bone43, 0.0F, 0.0F, 0.1745F);
        pedobear.addChild(bone43);
        bone43.cubeList.add(new ModelBox(bone43, 0, 32, -7.9098F, -4.5405F, 0.0F, 4, 5, 4, -1.7F, false));

        handle = new ModelRenderer(this);
        handle.setRotationPoint(0.0F, 0.4F, 0.0F);
        all.addChild(handle);

        bone38 = new ModelRenderer(this);
        bone38.setRotationPoint(1.0F, 0.0F, 0.0F);
        handle.addChild(bone38);
        bone38.cubeList.add(new ModelBox(bone38, 0, 12, -2.0F, -10.7F, -2.299F, 2, 1, 1, -0.3F, true));

        bone39 = new ModelRenderer(this);
        bone39.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone39, 0.0F, 0.0F, 0.7854F);
        handle.addChild(bone39);
        bone39.cubeList.add(new ModelBox(bone39, 0, 12, -7.866F, -7.4418F, -2.299F, 2, 1, 1, -0.3F, true));

        bone41 = new ModelRenderer(this);
        bone41.setRotationPoint(-1.0F, 0.0F, 0.0F);
        setRotationAngle(bone41, 0.0F, 0.0F, -0.7854F);
        handle.addChild(bone41);
        bone41.cubeList.add(new ModelBox(bone41, 0, 12, 5.866F, -7.4418F, -2.299F, 2, 1, 1, -0.3F, false));

        bone40 = new ModelRenderer(this);
        bone40.setRotationPoint(1.0F, 0.0F, 0.0F);
        setRotationAngle(bone40, 0.0F, 0.0F, 1.5708F);
        handle.addChild(bone40);
        bone40.cubeList.add(new ModelBox(bone40, 0, 12, -9.7101F, -0.9899F, -2.299F, 2, 1, 1, -0.3F, true));

        bone42 = new ModelRenderer(this);
        bone42.setRotationPoint(-1.0F, 0.0F, 0.0F);
        setRotationAngle(bone42, 0.0F, 0.0F, -1.5708F);
        handle.addChild(bone42);
        bone42.cubeList.add(new ModelBox(bone42, 0, 12, 7.7101F, -0.9899F, -2.299F, 2, 1, 1, -0.3F, false));

        bandLeft = new ModelRenderer(this);
        bandLeft.setRotationPoint(2.8F, -8.0F, -2.0F);
        setRotationAngle(bandLeft, 0.0F, -0.0873F, 0.0F);
        all.addChild(bandLeft);

        bone15 = new ModelRenderer(this);
        bone15.setRotationPoint(-3.0F, 8.0F, 2.0F);
        setRotationAngle(bone15, -2.8798F, 0.0F, 0.0F);
        bandLeft.addChild(bone15);
        bone15.cubeList.add(new ModelBox(bone15, 21, 6, 0.6F, 5.5119F, -2.6664F, 6, 6, 5, -2.4F, false));

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(-3.0F, 8.0F, 2.0F);
        setRotationAngle(bone11, -2.4435F, 0.0F, 0.0F);
        bandLeft.addChild(bone11);
        bone11.cubeList.add(new ModelBox(bone11, 21, 6, 0.6F, 5.7456F, -6.4923F, 6, 6, 5, -2.4F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(-3.0F, 7.0F, 3.0F);
        setRotationAngle(bone13, -1.9199F, 0.0F, 0.0F);
        bandLeft.addChild(bone13);
        bone13.cubeList.add(new ModelBox(bone13, 21, 6, 0.6F, 4.245F, -9.3351F, 6, 6, 5, -2.4F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(-3.0F, 8.0F, 2.0F);
        setRotationAngle(bone14, -1.5708F, 0.0F, 0.0F);
        bandLeft.addChild(bone14);
        bone14.cubeList.add(new ModelBox(bone14, 21, 6, 0.6F, 1.6F, -12.6F, 6, 6, 5, -2.4F, false));
        bone14.cubeList.add(new ModelBox(bone14, 21, 6, 0.6F, 2.8F, -12.6F, 6, 6, 5, -2.4F, false));

        bone17 = new ModelRenderer(this);
        bone17.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone17, -0.7854F, 0.0F, 0.0F);
        bandLeft.addChild(bone17);
        bone17.cubeList.add(new ModelBox(bone17, 21, 6, 0.6F, -6.5012F, -12.7238F, 6, 6, 5, -2.4F, false));

        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone18);
        bone18.cubeList.add(new ModelBox(bone18, 21, 6, 0.6F, -11.7515F, -7.6485F, 6, 6, 5, -2.4F, false));

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone16, 0.2618F, 0.0F, 0.0F);
        bandLeft.addChild(bone16);
        bone16.cubeList.add(new ModelBox(bone16, 21, 6, 0.6F, -11.6321F, -5.3599F, 6, 6, 5, -2.4F, false));
        bone16.cubeList.add(new ModelBox(bone16, 21, 6, 0.6F, -10.4321F, -5.3599F, 6, 6, 5, -2.4F, false));

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone19, 0.4363F, 0.0F, 0.0F);
        bandLeft.addChild(bone19);
        bone19.cubeList.add(new ModelBox(bone19, 21, 6, 0.6F, -9.6423F, -4.1286F, 6, 6, 5, -2.4F, false));
        bone19.cubeList.add(new ModelBox(bone19, 21, 6, 0.6F, -8.4423F, -4.1286F, 6, 6, 5, -2.4F, false));

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone20, 0.6109F, 0.0F, 0.0F);
        bandLeft.addChild(bone20);
        bone20.cubeList.add(new ModelBox(bone20, 21, 6, 0.6F, -7.4689F, -3.2615F, 6, 6, 5, -2.4F, false));
        bone20.cubeList.add(new ModelBox(bone20, 21, 6, 0.6F, -6.2689F, -3.2615F, 6, 6, 5, -2.4F, false));

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone21, 0.6981F, 0.0F, 0.0F);
        bandLeft.addChild(bone21);
        bone21.cubeList.add(new ModelBox(bone21, 21, 6, 0.6F, -5.1339F, -3.0256F, 6, 6, 5, -2.4F, false));

        bone22 = new ModelRenderer(this);
        bone22.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone22, 0.7854F, 0.0F, 0.0F);
        bandLeft.addChild(bone22);
        bone22.cubeList.add(new ModelBox(bone22, 21, 6, 0.6F, -3.9825F, -2.8895F, 6, 6, 5, -2.4F, false));

        bone23 = new ModelRenderer(this);
        bone23.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone23, 1.3963F, 0.0F, 0.0F);
        bandLeft.addChild(bone23);
        bone23.cubeList.add(new ModelBox(bone23, 21, 6, 0.6F, -2.9941F, -2.5816F, 6, 6, 5, -2.4F, false));

        bone24 = new ModelRenderer(this);
        bone24.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone24, 1.5708F, 0.0F, 0.0F);
        bandLeft.addChild(bone24);
        bone24.cubeList.add(new ModelBox(bone24, 21, 6, 0.6F, -1.8349F, -2.684F, 6, 6, 5, -2.4F, false));

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(-3.0F, 8.0F, 0.0F);
        setRotationAngle(bone25, 1.7453F, 0.0F, 0.0F);
        bandLeft.addChild(bone25);
        bone25.cubeList.add(new ModelBox(bone25, 21, 6, 0.6F, -0.711F, -2.9862F, 6, 6, 5, -2.4F, false));

        cap = new ModelRenderer(this);
        cap.setRotationPoint(0.0F, 0.0F, 1.0F);
        all.addChild(cap);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone, -0.4363F, 0.0F, 0.0F);
        cap.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 38, 7, -4.0F, -6.9826F, -6.1009F, 8, 1, 0, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, 4.001F, -6.9451F, -6.0367F, 0, 1, 1, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 0, -3.999F, -6.9451F, -6.0367F, 0, 1, 1, 0.0F, false));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone2, -0.8727F, 0.0F, 0.0F);
        cap.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 38, 6, -4.0F, -4.7505F, -8.4803F, 8, 1, 0, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, 4.0F, -4.7432F, -8.4062F, 0, 1, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 0, -3.999F, -4.7432F, -8.4062F, 0, 1, 1, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone3, -1.309F, 0.0F, 0.0F);
        cap.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 32, 38, -4.0F, -1.7219F, -9.6935F, 8, 1, 0, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, 4.0F, -1.7462F, -9.6232F, 0, 1, 1, 0.0F, false));
        bone3.cubeList.add(new ModelBox(bone3, 0, 0, -3.999F, -1.7462F, -9.6232F, 0, 1, 1, 0.0F, false));

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone4, -1.7453F, 0.0F, 0.0F);
        cap.addChild(bone4);
        bone4.cubeList.add(new ModelBox(bone4, 32, 37, -4.0F, 1.5357F, -9.5131F, 8, 1, 0, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, 4.0F, 1.4843F, -9.4595F, 0, 1, 1, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 0, 0, -3.999F, 1.4843F, -9.4595F, 0, 1, 1, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone5, -2.1817F, 0.0F, 0.0F);
        cap.addChild(bone5);
        bone5.cubeList.add(new ModelBox(bone5, 32, 36, -4.0F, 4.4118F, -7.9719F, 8, 1, 0, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, 4.0F, 4.343F, -7.9459F, 0, 1, 1, 0.0F, false));
        bone5.cubeList.add(new ModelBox(bone5, 0, 0, -3.999F, 4.343F, -7.9459F, 0, 1, 1, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone6, -2.618F, 0.0F, 0.0F);
        cap.addChild(bone6);
        bone6.cubeList.add(new ModelBox(bone6, 28, 34, -4.0F, 5.3671F, -5.3606F, 8, 2, 0, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone7, -2.9671F, 0.0F, 0.0F);
        cap.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 28, 32, -4.0F, 4.8765F, -3.2017F, 8, 2, 0, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone8, -3.0543F, 0.0F, 0.0F);
        cap.addChild(bone8);
        bone8.cubeList.add(new ModelBox(bone8, 26, 4, -4.0F, 3.1369F, -2.7635F, 8, 2, 0, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone9, 3.1416F, 0.0F, 0.0F);
        cap.addChild(bone9);
        bone9.cubeList.add(new ModelBox(bone9, 24, 17, -4.0F, 1.3658F, -2.4806F, 8, 2, 0, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone10, 3.0543F, 0.0F, 0.0F);
        cap.addChild(bone10);
        bone10.cubeList.add(new ModelBox(bone10, 24, 19, -4.0F, 0.5767F, -2.3521F, 8, 1, 0, 0.0F, false));

        bag = new ModelRenderer(this);
        bag.setRotationPoint(0.0F, 0.0F, 1.0F);
        all.addChild(bag);
        bag.cubeList.add(new ModelBox(bag, 20, 20, -4.0F, -8.0F, -3.0F, 8, 8, 4, 0.0F, false));
        bag.cubeList.add(new ModelBox(bag, 24, 19, -4.0F, -8.98F, 1.0F, 8, 1, 0, 0.0F, false));
        bag.cubeList.add(new ModelBox(bag, 0, 8, 4.0F, -9.0F, -2.0F, 0, 1, 3, 0.0F, false));
        bag.cubeList.add(new ModelBox(bag, 0, 8, -4.0F, -9.0F, -2.0F, 0, 1, 3, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(-0.1F, 0.0F, 0.0F);
        setRotationAngle(bone12, 0.0349F, 0.0F, 0.0F);
        bag.addChild(bone12);
        bone12.cubeList.add(new ModelBox(bone12, 0, 23, -3.9F, -7.8651F, 0.8994F, 8, 8, 1, -0.1F, false));
        bone12.cubeList.add(new ModelBox(bone12, 0, 0, -4.9F, -6.7651F, 0.5994F, 10, 8, 3, -1.2F, false));

        bone44 = new ModelRenderer(this);
        bone44.setRotationPoint(0.1F, 0.0F, 0.0F);
        setRotationAngle(bone44, 0.0F, 0.0F, -0.1745F);
        bone12.addChild(bone44);
        bone44.cubeList.add(new ModelBox(bone44, 16, 32, 1.8009F, -3.0739F, 0.0994F, 4, 5, 4, -1.7F, false));
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