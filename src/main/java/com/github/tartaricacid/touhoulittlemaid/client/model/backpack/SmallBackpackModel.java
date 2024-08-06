package com.github.tartaricacid.touhoulittlemaid.client.model.backpack;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SmallBackpackModel extends EntityModel<EntityMaid> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "backpack_small");
    private final ModelPart all;

    public SmallBackpackModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 1.0F));

        PartDefinition bandRight = all.addOrReplaceChild("bandRight", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.8F, -8.0F, -2.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition bone26 = bandRight.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, 5.5119F, -2.6664F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 2.0F, -2.8798F, 0.0F, 0.0F));

        PartDefinition bone27 = bandRight.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, 5.7456F, -6.4923F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 2.0F, -2.4435F, 0.0F, 0.0F));

        PartDefinition bone28 = bandRight.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, 4.245F, -9.3351F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 7.0F, 3.0F, -1.9199F, 0.0F, 0.0F));

        PartDefinition bone29 = bandRight.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, 1.6F, -12.6F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false)
                .texOffs(21, 6).mirror().addBox(-6.6F, 2.8F, -12.6F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone30 = bandRight.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -6.5012F, -12.7238F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone31 = bandRight.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -11.7515F, -7.6485F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offset(3.0F, 8.0F, 0.0F));

        PartDefinition bone32 = bandRight.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -11.6321F, -5.3599F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false)
                .texOffs(21, 6).mirror().addBox(-6.6F, -10.4321F, -5.3599F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone33 = bandRight.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -9.6423F, -4.1286F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false)
                .texOffs(21, 6).mirror().addBox(-6.6F, -8.4423F, -4.1286F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition bone34 = bandRight.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -7.4689F, -3.2615F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false)
                .texOffs(21, 6).mirror().addBox(-6.6F, -6.2689F, -3.2615F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition bone35 = bandRight.addOrReplaceChild("bone35", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -5.1339F, -3.0256F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition bone36 = bandRight.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -3.9825F, -2.8895F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone37 = bandRight.addOrReplaceChild("bone37", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -2.9941F, -2.5816F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

        PartDefinition bone46 = bandRight.addOrReplaceChild("bone46", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -1.8349F, -2.684F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone47 = bandRight.addOrReplaceChild("bone47", CubeListBuilder.create().texOffs(21, 6).mirror().addBox(-6.6F, -0.711F, -2.9862F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)).mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, 0.0F, 1.7453F, 0.0F, 0.0F));

        PartDefinition pedobear = all.addOrReplaceChild("pedobear", CubeListBuilder.create().texOffs(29, 39).addBox(-4.6F, -6.7F, -1.3F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.3F))
                .texOffs(0, 32).addBox(-4.5F, -6.9F, 0.9F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 32).addBox(-4.5F, -6.9F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(12, 32).addBox(-4.7F, -6.1F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(12, 32).addBox(-4.7F, -6.1F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(12, 32).addBox(-4.7F, -5.3F, -0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(23, 0).addBox(-4.7F, -5.3F, 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(23, 0).addBox(-4.8F, -6.9F, 0.9F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(23, 0).addBox(-4.8F, -6.9F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(23, 0).addBox(-4.7F, -5.3F, -0.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(16, 32).addBox(-4.7F, -4.9F, -0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone45 = pedobear.addOrReplaceChild("bone45", CubeListBuilder.create().texOffs(0, 14).addBox(-6.5602F, -5.2816F, 1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(-0.85F, -1.35F, -1.8F, 0.0F, 0.0F, 0.5236F));

        PartDefinition bone43 = pedobear.addOrReplaceChild("bone43", CubeListBuilder.create().texOffs(0, 32).addBox(-7.9098F, -4.5405F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-1.7F)), PartPose.offsetAndRotation(1.6F, -4.1F, -1.8F, 0.0F, 0.0F, 0.1745F));

        PartDefinition handle = all.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.4F, 0.0F));

        PartDefinition bone38 = handle.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-2.0F, -10.7F, -2.299F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition bone39 = handle.addOrReplaceChild("bone39", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-7.866F, -7.4418F, -2.299F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone41 = handle.addOrReplaceChild("bone41", CubeListBuilder.create().texOffs(0, 12).addBox(5.866F, -7.4418F, -2.299F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition bone40 = handle.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-9.7101F, -0.9899F, -2.299F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition bone42 = handle.addOrReplaceChild("bone42", CubeListBuilder.create().texOffs(0, 12).addBox(7.7101F, -0.9899F, -2.299F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition bandLeft = all.addOrReplaceChild("bandLeft", CubeListBuilder.create(), PartPose.offsetAndRotation(2.8F, -8.0F, -2.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition bone15 = bandLeft.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, 5.5119F, -2.6664F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 2.0F, -2.8798F, 0.0F, 0.0F));

        PartDefinition bone11 = bandLeft.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, 5.7456F, -6.4923F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 2.0F, -2.4435F, 0.0F, 0.0F));

        PartDefinition bone13 = bandLeft.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, 4.245F, -9.3351F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 7.0F, 3.0F, -1.9199F, 0.0F, 0.0F));

        PartDefinition bone14 = bandLeft.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, 1.6F, -12.6F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F))
                .texOffs(21, 6).addBox(0.6F, 2.8F, -12.6F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 2.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition bone17 = bandLeft.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -6.5012F, -12.7238F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone18 = bandLeft.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -11.7515F, -7.6485F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offset(-3.0F, 8.0F, 0.0F));

        PartDefinition bone16 = bandLeft.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -11.6321F, -5.3599F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F))
                .texOffs(21, 6).addBox(0.6F, -10.4321F, -5.3599F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone19 = bandLeft.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -9.6423F, -4.1286F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F))
                .texOffs(21, 6).addBox(0.6F, -8.4423F, -4.1286F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition bone20 = bandLeft.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -7.4689F, -3.2615F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F))
                .texOffs(21, 6).addBox(0.6F, -6.2689F, -3.2615F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition bone21 = bandLeft.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -5.1339F, -3.0256F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition bone22 = bandLeft.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -3.9825F, -2.8895F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone23 = bandLeft.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -2.9941F, -2.5816F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 1.3963F, 0.0F, 0.0F));

        PartDefinition bone24 = bandLeft.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -1.8349F, -2.684F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone25 = bandLeft.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(21, 6).addBox(0.6F, -0.711F, -2.9862F, 6.0F, 6.0F, 5.0F, new CubeDeformation(-2.4F)), PartPose.offsetAndRotation(-3.0F, 8.0F, 0.0F, 1.7453F, 0.0F, 0.0F));

        PartDefinition cap = all.addOrReplaceChild("cap", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition bone = cap.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(38, 7).addBox(-4.0F, -6.9826F, -6.1009F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.001F, -6.9451F, -6.0367F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.999F, -6.9451F, -6.0367F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition bone2 = cap.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(38, 6).addBox(-4.0F, -4.7505F, -8.4803F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.0F, -4.7432F, -8.4062F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.999F, -4.7432F, -8.4062F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

        PartDefinition bone3 = cap.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(32, 38).addBox(-4.0F, -1.7219F, -9.6935F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.0F, -1.7462F, -9.6232F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.999F, -1.7462F, -9.6232F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition bone4 = cap.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(32, 37).addBox(-4.0F, 1.5357F, -9.5131F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.0F, 1.4843F, -9.4595F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.999F, 1.4843F, -9.4595F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.7453F, 0.0F, 0.0F));

        PartDefinition bone5 = cap.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(32, 36).addBox(-4.0F, 4.4118F, -7.9719F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(4.0F, 4.343F, -7.9459F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.999F, 4.343F, -7.9459F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.1817F, 0.0F, 0.0F));

        PartDefinition bone6 = cap.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(28, 34).addBox(-4.0F, 5.3671F, -5.3606F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.618F, 0.0F, 0.0F));

        PartDefinition bone7 = cap.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(28, 32).addBox(-4.0F, 4.8765F, -3.2017F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.9671F, 0.0F, 0.0F));

        PartDefinition bone8 = cap.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(26, 4).addBox(-4.0F, 3.1369F, -2.7635F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.0543F, 0.0F, 0.0F));

        PartDefinition bone9 = cap.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(24, 17).addBox(-4.0F, 1.3658F, -2.4806F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition bone10 = cap.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(24, 19).addBox(-4.0F, 0.5767F, -2.3521F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.0543F, 0.0F, 0.0F));

        PartDefinition bag = all.addOrReplaceChild("bag", CubeListBuilder.create().texOffs(20, 20).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 19).addBox(-4.0F, -8.98F, 1.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(4.0F, -9.0F, -2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-4.0F, -9.0F, -2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition bone12 = bag.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(0, 23).addBox(-3.9F, -7.8651F, 0.8994F, 8.0F, 8.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 0).addBox(-4.9F, -6.7651F, 0.5994F, 10.0F, 8.0F, 3.0F, new CubeDeformation(-1.2F)), PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, 0.0349F, 0.0F, 0.0F));

        PartDefinition bone44 = bone12.addOrReplaceChild("bone44", CubeListBuilder.create().texOffs(16, 32).addBox(1.8009F, -3.0739F, 0.0994F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-1.7F)), PartPose.offsetAndRotation(0.1F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EntityMaid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}