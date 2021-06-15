package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MaidBackpackSmallModel extends EntityModel<EntityMaid> {
    private final ModelRenderer all;

    public MaidBackpackSmallModel() {
        texWidth = 64;
        texHeight = 64;

        all = new ModelRenderer(this);
        all.setPos(0.0F, 24.0F, 1.0F);

        ModelRenderer bandRight = new ModelRenderer(this);
        bandRight.setPos(-2.8F, -8.0F, -2.0F);
        all.addChild(bandRight);
        setRotationAngle(bandRight, 0.0F, 0.0873F, 0.0F);

        ModelRenderer bone26 = new ModelRenderer(this);
        bone26.setPos(3.0F, 8.0F, 2.0F);
        bandRight.addChild(bone26);
        setRotationAngle(bone26, -2.8798F, 0.0F, 0.0F);
        bone26.texOffs(21, 6).addBox(-6.6F, 5.5119F, -2.6664F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone27 = new ModelRenderer(this);
        bone27.setPos(3.0F, 8.0F, 2.0F);
        bandRight.addChild(bone27);
        setRotationAngle(bone27, -2.4435F, 0.0F, 0.0F);
        bone27.texOffs(21, 6).addBox(-6.6F, 5.7456F, -6.4923F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone28 = new ModelRenderer(this);
        bone28.setPos(3.0F, 7.0F, 3.0F);
        bandRight.addChild(bone28);
        setRotationAngle(bone28, -1.9199F, 0.0F, 0.0F);
        bone28.texOffs(21, 6).addBox(-6.6F, 4.245F, -9.3351F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone29 = new ModelRenderer(this);
        bone29.setPos(3.0F, 8.0F, 2.0F);
        bandRight.addChild(bone29);
        setRotationAngle(bone29, -1.5708F, 0.0F, 0.0F);
        bone29.texOffs(21, 6).addBox(-6.6F, 1.6F, -12.6F, 6.0F, 6.0F, 5.0F, -2.4F, true);
        bone29.texOffs(21, 6).addBox(-6.6F, 2.8F, -12.6F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone30 = new ModelRenderer(this);
        bone30.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone30);
        setRotationAngle(bone30, -0.7854F, 0.0F, 0.0F);
        bone30.texOffs(21, 6).addBox(-6.6F, -6.5012F, -12.7238F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone31 = new ModelRenderer(this);
        bone31.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone31);
        bone31.texOffs(21, 6).addBox(-6.6F, -11.7515F, -7.6485F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone32 = new ModelRenderer(this);
        bone32.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone32);
        setRotationAngle(bone32, 0.2618F, 0.0F, 0.0F);
        bone32.texOffs(21, 6).addBox(-6.6F, -11.6321F, -5.3599F, 6.0F, 6.0F, 5.0F, -2.4F, true);
        bone32.texOffs(21, 6).addBox(-6.6F, -10.4321F, -5.3599F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone33 = new ModelRenderer(this);
        bone33.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone33);
        setRotationAngle(bone33, 0.4363F, 0.0F, 0.0F);
        bone33.texOffs(21, 6).addBox(-6.6F, -9.6423F, -4.1286F, 6.0F, 6.0F, 5.0F, -2.4F, true);
        bone33.texOffs(21, 6).addBox(-6.6F, -8.4423F, -4.1286F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone34 = new ModelRenderer(this);
        bone34.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone34);
        setRotationAngle(bone34, 0.6109F, 0.0F, 0.0F);
        bone34.texOffs(21, 6).addBox(-6.6F, -7.4689F, -3.2615F, 6.0F, 6.0F, 5.0F, -2.4F, true);
        bone34.texOffs(21, 6).addBox(-6.6F, -6.2689F, -3.2615F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone35 = new ModelRenderer(this);
        bone35.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone35);
        setRotationAngle(bone35, 0.6981F, 0.0F, 0.0F);
        bone35.texOffs(21, 6).addBox(-6.6F, -5.1339F, -3.0256F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone36 = new ModelRenderer(this);
        bone36.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone36);
        setRotationAngle(bone36, 0.7854F, 0.0F, 0.0F);
        bone36.texOffs(21, 6).addBox(-6.6F, -3.9825F, -2.8895F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone37 = new ModelRenderer(this);
        bone37.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone37);
        setRotationAngle(bone37, 1.3963F, 0.0F, 0.0F);
        bone37.texOffs(21, 6).addBox(-6.6F, -2.9941F, -2.5816F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone46 = new ModelRenderer(this);
        bone46.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone46);
        setRotationAngle(bone46, 1.5708F, 0.0F, 0.0F);
        bone46.texOffs(21, 6).addBox(-6.6F, -1.8349F, -2.684F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer bone47 = new ModelRenderer(this);
        bone47.setPos(3.0F, 8.0F, 0.0F);
        bandRight.addChild(bone47);
        setRotationAngle(bone47, 1.7453F, 0.0F, 0.0F);
        bone47.texOffs(21, 6).addBox(-6.6F, -0.711F, -2.9862F, 6.0F, 6.0F, 5.0F, -2.4F, true);

        ModelRenderer pedoBear = new ModelRenderer(this);
        pedoBear.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(pedoBear);
        pedoBear.texOffs(29, 39).addBox(-4.6F, -6.7F, -1.3F, 1.0F, 3.0F, 3.0F, -0.3F, false);
        pedoBear.texOffs(0, 32).addBox(-4.5F, -6.9F, 0.9F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        pedoBear.texOffs(0, 32).addBox(-4.5F, -6.9F, -1.5F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        pedoBear.texOffs(12, 32).addBox(-4.7F, -6.1F, 0.5F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(12, 32).addBox(-4.7F, -6.1F, -1.1F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(12, 32).addBox(-4.7F, -5.3F, -0.3F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(23, 0).addBox(-4.7F, -5.3F, 0.1F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(23, 0).addBox(-4.8F, -6.9F, 0.9F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(23, 0).addBox(-4.8F, -6.9F, -1.5F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(23, 0).addBox(-4.7F, -5.3F, -0.7F, 1.0F, 1.0F, 1.0F, -0.3F, false);
        pedoBear.texOffs(16, 32).addBox(-4.7F, -4.9F, -0.3F, 1.0F, 1.0F, 1.0F, -0.3F, false);

        ModelRenderer bone45 = new ModelRenderer(this);
        bone45.setPos(-0.85F, -1.35F, -1.8F);
        pedoBear.addChild(bone45);
        setRotationAngle(bone45, 0.0F, 0.0F, 0.5236F);
        bone45.texOffs(0, 14).addBox(-6.5602F, -5.2816F, 1.5F, 1.0F, 2.0F, 1.0F, -0.35F, false);

        ModelRenderer bone43 = new ModelRenderer(this);
        bone43.setPos(1.6F, -4.1F, -1.8F);
        pedoBear.addChild(bone43);
        setRotationAngle(bone43, 0.0F, 0.0F, 0.1745F);
        bone43.texOffs(0, 32).addBox(-7.9098F, -4.5405F, 0.0F, 4.0F, 5.0F, 4.0F, -1.7F, false);

        ModelRenderer handle = new ModelRenderer(this);
        handle.setPos(0.0F, 0.4F, 0.0F);
        all.addChild(handle);

        ModelRenderer bone38 = new ModelRenderer(this);
        bone38.setPos(1.0F, 0.0F, 0.0F);
        handle.addChild(bone38);
        bone38.texOffs(0, 12).addBox(-2.0F, -10.7F, -2.299F, 2.0F, 1.0F, 1.0F, -0.3F, true);

        ModelRenderer bone39 = new ModelRenderer(this);
        bone39.setPos(1.0F, 0.0F, 0.0F);
        handle.addChild(bone39);
        setRotationAngle(bone39, 0.0F, 0.0F, 0.7854F);
        bone39.texOffs(0, 12).addBox(-7.866F, -7.4418F, -2.299F, 2.0F, 1.0F, 1.0F, -0.3F, true);

        ModelRenderer bone41 = new ModelRenderer(this);
        bone41.setPos(-1.0F, 0.0F, 0.0F);
        handle.addChild(bone41);
        setRotationAngle(bone41, 0.0F, 0.0F, -0.7854F);
        bone41.texOffs(0, 12).addBox(5.866F, -7.4418F, -2.299F, 2.0F, 1.0F, 1.0F, -0.3F, false);

        ModelRenderer bone40 = new ModelRenderer(this);
        bone40.setPos(1.0F, 0.0F, 0.0F);
        handle.addChild(bone40);
        setRotationAngle(bone40, 0.0F, 0.0F, 1.5708F);
        bone40.texOffs(0, 12).addBox(-9.7101F, -0.9899F, -2.299F, 2.0F, 1.0F, 1.0F, -0.3F, true);

        ModelRenderer bone42 = new ModelRenderer(this);
        bone42.setPos(-1.0F, 0.0F, 0.0F);
        handle.addChild(bone42);
        setRotationAngle(bone42, 0.0F, 0.0F, -1.5708F);
        bone42.texOffs(0, 12).addBox(7.7101F, -0.9899F, -2.299F, 2.0F, 1.0F, 1.0F, -0.3F, false);

        ModelRenderer bandLeft = new ModelRenderer(this);
        bandLeft.setPos(2.8F, -8.0F, -2.0F);
        all.addChild(bandLeft);
        setRotationAngle(bandLeft, 0.0F, -0.0873F, 0.0F);

        ModelRenderer bone15 = new ModelRenderer(this);
        bone15.setPos(-3.0F, 8.0F, 2.0F);
        bandLeft.addChild(bone15);
        setRotationAngle(bone15, -2.8798F, 0.0F, 0.0F);
        bone15.texOffs(21, 6).addBox(0.6F, 5.5119F, -2.6664F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone11 = new ModelRenderer(this);
        bone11.setPos(-3.0F, 8.0F, 2.0F);
        bandLeft.addChild(bone11);
        setRotationAngle(bone11, -2.4435F, 0.0F, 0.0F);
        bone11.texOffs(21, 6).addBox(0.6F, 5.7456F, -6.4923F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone13 = new ModelRenderer(this);
        bone13.setPos(-3.0F, 7.0F, 3.0F);
        bandLeft.addChild(bone13);
        setRotationAngle(bone13, -1.9199F, 0.0F, 0.0F);
        bone13.texOffs(21, 6).addBox(0.6F, 4.245F, -9.3351F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone14 = new ModelRenderer(this);
        bone14.setPos(-3.0F, 8.0F, 2.0F);
        bandLeft.addChild(bone14);
        setRotationAngle(bone14, -1.5708F, 0.0F, 0.0F);
        bone14.texOffs(21, 6).addBox(0.6F, 1.6F, -12.6F, 6.0F, 6.0F, 5.0F, -2.4F, false);
        bone14.texOffs(21, 6).addBox(0.6F, 2.8F, -12.6F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone17 = new ModelRenderer(this);
        bone17.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone17);
        setRotationAngle(bone17, -0.7854F, 0.0F, 0.0F);
        bone17.texOffs(21, 6).addBox(0.6F, -6.5012F, -12.7238F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone18 = new ModelRenderer(this);
        bone18.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone18);
        bone18.texOffs(21, 6).addBox(0.6F, -11.7515F, -7.6485F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone16 = new ModelRenderer(this);
        bone16.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone16);
        setRotationAngle(bone16, 0.2618F, 0.0F, 0.0F);
        bone16.texOffs(21, 6).addBox(0.6F, -11.6321F, -5.3599F, 6.0F, 6.0F, 5.0F, -2.4F, false);
        bone16.texOffs(21, 6).addBox(0.6F, -10.4321F, -5.3599F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone19 = new ModelRenderer(this);
        bone19.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone19);
        setRotationAngle(bone19, 0.4363F, 0.0F, 0.0F);
        bone19.texOffs(21, 6).addBox(0.6F, -9.6423F, -4.1286F, 6.0F, 6.0F, 5.0F, -2.4F, false);
        bone19.texOffs(21, 6).addBox(0.6F, -8.4423F, -4.1286F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone20 = new ModelRenderer(this);
        bone20.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone20);
        setRotationAngle(bone20, 0.6109F, 0.0F, 0.0F);
        bone20.texOffs(21, 6).addBox(0.6F, -7.4689F, -3.2615F, 6.0F, 6.0F, 5.0F, -2.4F, false);
        bone20.texOffs(21, 6).addBox(0.6F, -6.2689F, -3.2615F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone21 = new ModelRenderer(this);
        bone21.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone21);
        setRotationAngle(bone21, 0.6981F, 0.0F, 0.0F);
        bone21.texOffs(21, 6).addBox(0.6F, -5.1339F, -3.0256F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone22 = new ModelRenderer(this);
        bone22.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone22);
        setRotationAngle(bone22, 0.7854F, 0.0F, 0.0F);
        bone22.texOffs(21, 6).addBox(0.6F, -3.9825F, -2.8895F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone23 = new ModelRenderer(this);
        bone23.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone23);
        setRotationAngle(bone23, 1.3963F, 0.0F, 0.0F);
        bone23.texOffs(21, 6).addBox(0.6F, -2.9941F, -2.5816F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone24 = new ModelRenderer(this);
        bone24.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone24);
        setRotationAngle(bone24, 1.5708F, 0.0F, 0.0F);
        bone24.texOffs(21, 6).addBox(0.6F, -1.8349F, -2.684F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer bone25 = new ModelRenderer(this);
        bone25.setPos(-3.0F, 8.0F, 0.0F);
        bandLeft.addChild(bone25);
        setRotationAngle(bone25, 1.7453F, 0.0F, 0.0F);
        bone25.texOffs(21, 6).addBox(0.6F, -0.711F, -2.9862F, 6.0F, 6.0F, 5.0F, -2.4F, false);

        ModelRenderer cap = new ModelRenderer(this);
        cap.setPos(0.0F, 0.0F, 1.0F);
        all.addChild(cap);

        ModelRenderer bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone);
        setRotationAngle(bone, -0.4363F, 0.0F, 0.0F);
        bone.texOffs(38, 7).addBox(-4.0F, -6.9826F, -6.1009F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bone.texOffs(0, 0).addBox(4.001F, -6.9451F, -6.0367F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(0, 0).addBox(-3.999F, -6.9451F, -6.0367F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone2);
        setRotationAngle(bone2, -0.8727F, 0.0F, 0.0F);
        bone2.texOffs(38, 6).addBox(-4.0F, -4.7505F, -8.4803F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bone2.texOffs(0, 0).addBox(4.0F, -4.7432F, -8.4062F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone2.texOffs(0, 0).addBox(-3.999F, -4.7432F, -8.4062F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone3);
        setRotationAngle(bone3, -1.309F, 0.0F, 0.0F);
        bone3.texOffs(32, 38).addBox(-4.0F, -1.7219F, -9.6935F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bone3.texOffs(0, 0).addBox(4.0F, -1.7462F, -9.6232F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone3.texOffs(0, 0).addBox(-3.999F, -1.7462F, -9.6232F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone4);
        setRotationAngle(bone4, -1.7453F, 0.0F, 0.0F);
        bone4.texOffs(32, 37).addBox(-4.0F, 1.5357F, -9.5131F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bone4.texOffs(0, 0).addBox(4.0F, 1.4843F, -9.4595F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(0, 0).addBox(-3.999F, 1.4843F, -9.4595F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone5);
        setRotationAngle(bone5, -2.1817F, 0.0F, 0.0F);
        bone5.texOffs(32, 36).addBox(-4.0F, 4.4118F, -7.9719F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bone5.texOffs(0, 0).addBox(4.0F, 4.343F, -7.9459F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(0, 0).addBox(-3.999F, 4.343F, -7.9459F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        ModelRenderer bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone6);
        setRotationAngle(bone6, -2.618F, 0.0F, 0.0F);
        bone6.texOffs(28, 34).addBox(-4.0F, 5.3671F, -5.3606F, 8.0F, 2.0F, 0.0F, 0.0F, false);

        ModelRenderer bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone7);
        setRotationAngle(bone7, -2.9671F, 0.0F, 0.0F);
        bone7.texOffs(28, 32).addBox(-4.0F, 4.8765F, -3.2017F, 8.0F, 2.0F, 0.0F, 0.0F, false);

        ModelRenderer bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone8);
        setRotationAngle(bone8, -3.0543F, 0.0F, 0.0F);
        bone8.texOffs(26, 4).addBox(-4.0F, 3.1369F, -2.7635F, 8.0F, 2.0F, 0.0F, 0.0F, false);

        ModelRenderer bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone9);
        setRotationAngle(bone9, 3.1416F, 0.0F, 0.0F);
        bone9.texOffs(24, 17).addBox(-4.0F, 1.3658F, -2.4806F, 8.0F, 2.0F, 0.0F, 0.0F, false);

        ModelRenderer bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, 0.0F, 0.0F);
        cap.addChild(bone10);
        setRotationAngle(bone10, 3.0543F, 0.0F, 0.0F);
        bone10.texOffs(24, 19).addBox(-4.0F, 0.5767F, -2.3521F, 8.0F, 1.0F, 0.0F, 0.0F, false);

        ModelRenderer bag = new ModelRenderer(this);
        bag.setPos(0.0F, 0.0F, 1.0F);
        all.addChild(bag);
        bag.texOffs(20, 20).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
        bag.texOffs(24, 19).addBox(-4.0F, -8.98F, 1.0F, 8.0F, 1.0F, 0.0F, 0.0F, false);
        bag.texOffs(0, 8).addBox(4.0F, -9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        bag.texOffs(0, 8).addBox(-4.0F, -9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);

        ModelRenderer bone12 = new ModelRenderer(this);
        bone12.setPos(-0.1F, 0.0F, 0.0F);
        bag.addChild(bone12);
        setRotationAngle(bone12, 0.0349F, 0.0F, 0.0F);
        bone12.texOffs(0, 23).addBox(-3.9F, -7.8651F, 0.8994F, 8.0F, 8.0F, 1.0F, -0.1F, false);
        bone12.texOffs(0, 0).addBox(-4.9F, -6.7651F, 0.5994F, 10.0F, 8.0F, 3.0F, -1.2F, false);

        ModelRenderer bone44 = new ModelRenderer(this);
        bone44.setPos(0.1F, 0.0F, 0.0F);
        bone12.addChild(bone44);
        setRotationAngle(bone44, 0.0F, 0.0F, -0.1745F);
        bone44.texOffs(16, 32).addBox(1.8009F, -3.0739F, 0.0994F, 4.0F, 5.0F, 4.0F, -1.7F, false);
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