package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ComputerModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "computer");
    private final ModelPart main;

    public ComputerModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, -1.0F));

        PartDefinition group = main.addOrReplaceChild("group", CubeListBuilder.create().texOffs(0, 0).addBox(-27.0F, -8.0F, -15.0F, 38.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(9.0F, -19.0F, -13.0F, 1.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(144, 96).addBox(-1.0F, -19.0F, -14.0F, 11.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(112, 73).addBox(-2.0F, -20.0F, -15.0F, 13.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(66, 81).addBox(-3.0F, -20.5F, -14.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 90).addBox(-8.4501F, -23.2712F, -13.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(158, 126).addBox(5.0F, -24.0F, -14.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(74, 17).addBox(5.5F, -30.0F, -13.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(158, 84).addBox(-17.0F, -8.5F, -14.5F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(90, 29).addBox(-15.0F, -9.25F, -13.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(146, 117).addBox(-20.0F, -17.25F, -13.5F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-23.0F, -3.0F, -11.0F, 30.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(66, 66).addBox(-23.0F, 13.9F, -14.0F, 30.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(74, 54).addBox(-23.0F, -4.0F, -11.0F, 30.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 140).addBox(6.0F, -2.0F, -14.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(132, 58).addBox(-23.0F, -2.0F, -14.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 90).addBox(-24.0F, -7.0F, -14.0F, 1.0F, 23.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(104, 29).addBox(-30.0F, 3.0F, -14.0F, 6.0F, 13.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(74, 17).addBox(7.0F, -7.0F, -14.0F, 1.0F, 23.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 66).addBox(-24.0F, -7.0F, -15.0F, 32.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition cube_r1 = group.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(146, 108).addBox(-7.0F, -7.5F, 0.8F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(92, 8).addBox(-2.0F, 0.5F, 0.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(158, 71).addBox(-4.0F, 1.25F, -0.2F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.75F, -11.5F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r2 = group.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(132, 116).addBox(-1.3F, -4.0F, -4.3F, 1.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.8915F, -13.25F, -5.8238F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r3 = group.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 93).addBox(0.7F, -0.5F, 6.7F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(132, 152).addBox(-2.3F, 0.25F, 4.7F, 5.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2346F, -8.75F, -8.6522F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r4 = group.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(127, 29).addBox(-3.75F, -0.0333F, -6.0833F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 90).addBox(-0.5F, -0.9833F, -2.8333F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(-3.25F, -0.4833F, 1.4167F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.5733F, -8.2667F, -4.3254F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r5 = group.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-3.2385F, -21.2934F, -12.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition group10 = main.addOrReplaceChild("group10", CubeListBuilder.create().texOffs(247, 3).addBox(1.0171F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F))
                .texOffs(247, 3).addBox(3.0308F, 0.5137F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F))
                .texOffs(247, 3).addBox(1.0171F, 2.5274F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F))
                .texOffs(247, 3).addBox(-0.9965F, 0.5137F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offset(-1.6671F, -23.7774F, -20.05F));

        PartDefinition cube_r6 = group10.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(247, 3).addBox(0.5137F, -0.9965F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r7 = group10.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(247, 3).addBox(-0.7241F, -0.856F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r8 = group10.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(247, 3).addBox(-0.7241F, -0.144F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r9 = group10.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(247, 3).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(0.0933F, 2.4376F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r10 = group10.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(247, 3).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(-0.3433F, 1.7843F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r11 = group10.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(247, 3).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(0.7465F, 2.8741F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r12 = group10.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(247, 3).addBox(-1.7654F, 1.262F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F))
                .texOffs(247, 3).addBox(0.2482F, -0.7517F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(2.7826F, 0.762F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r13 = group10.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(247, 3).addBox(-1.5728F, 0.7969F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F))
                .texOffs(247, 3).addBox(0.4409F, -1.2168F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(2.7826F, 0.762F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r14 = group10.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(247, 3).addBox(0.4409F, 0.2168F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F))
                .texOffs(247, 3).addBox(-1.5728F, -1.7969F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.375F)), PartPose.offsetAndRotation(2.7826F, 0.762F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition group9 = main.addOrReplaceChild("group9", CubeListBuilder.create().texOffs(142, 44).addBox(-4.0999F, 1.5212F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(76, 141).addBox(-6.1135F, -0.4924F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(70, 141).addBox(-2.0862F, -0.4924F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(140, 27).addBox(-4.0999F, -2.5061F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(140, 10).addBox(-4.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(72, 95).addBox(-4.0F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offset(3.4499F, -22.7712F, -20.5F));

        PartDefinition cube_r15 = group9.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(140, 66).addBox(-1.5728F, -1.7969F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(141, 69).addBox(0.4409F, 0.2168F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3344F, -0.2442F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r16 = group9.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(132, 140).addBox(0.2482F, -0.7517F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 141).addBox(-1.7654F, 1.262F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3344F, -0.2442F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r17 = group9.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(136, 140).addBox(0.4409F, -1.2168F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(62, 141).addBox(-1.5728F, 0.7969F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3344F, -0.2442F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r18 = group9.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(80, 141).addBox(-0.7241F, -0.144F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.117F, -1.0062F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r19 = group9.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(90, 141).addBox(0.5137F, -0.9965F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.117F, -1.0062F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r20 = group9.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(94, 141).addBox(-0.7241F, -0.856F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.117F, -1.0062F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r21 = group9.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(139, 141).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0237F, 1.4314F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r22 = group9.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(142, 40).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4602F, 0.7782F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r23 = group9.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(142, 42).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3705F, 1.8679F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition group8 = main.addOrReplaceChild("group8", CubeListBuilder.create(), PartPose.offset(0.2458F, -4.9736F, -4.9484F));

        PartDefinition cube_r24 = group8.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(248, 27).addBox(-2.9903F, -0.6223F, -0.9946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(248, 27).addBox(-2.9903F, -0.6223F, -0.5946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(248, 27).addBox(-2.8903F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.7597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.9597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.1597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(238, 27).addBox(0.3597F, -0.5223F, 1.5554F, 6.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-0.4903F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.2903F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.0653F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.8653F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.3597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.5597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(5.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.9597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.1597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.5597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(1.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.9597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(3.3597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.1597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-0.6403F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.4403F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.3153F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.8903F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.7597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.6403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.8403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.0403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-0.2403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.5597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(1.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.1597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.9597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(3.7597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.5597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(5.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.1597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.9597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.2403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.4403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-0.6403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.1597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.9597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(1.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.5597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(3.3597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.1597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.9597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(5.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.5597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.3597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-9.2403F, -0.5223F, 1.0554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(247, 27).addBox(-9.2403F, -0.5223F, -0.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-7.2403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-9.2403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-6.8403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-7.6403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-8.4403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-9.2403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-6.8403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-7.6403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-8.4403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-9.2403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-6.8403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-7.6403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-8.4403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(247, 27).addBox(-9.2403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-6.8403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-7.6403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-8.4403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(247, 27).addBox(-9.2403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-6.8403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-7.6403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-8.4403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.9403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.1403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.9403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-5.7403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.1403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.9403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-5.7403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.1403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.9403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-5.7403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.9403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.6403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.8403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.0403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-0.2403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(1.3597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.5597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.1597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.9597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(3.7597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.5597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(5.3597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.1597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.9597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.7597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-5.7403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.9403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-4.1403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.9403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-2.1403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-1.3403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(-0.5403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(0.5597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(1.3597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.1597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(2.9597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.0597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(4.8597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(5.6597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(6.4597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(248, 27).addBox(7.7597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(92, 13).addBox(-7.2403F, 1.0777F, -1.9446F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(74, 56).addBox(-9.2403F, 0.0777F, -2.4446F, 18.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition group6 = main.addOrReplaceChild("group6", CubeListBuilder.create(), PartPose.offset(0.8087F, -19.6381F, -14.0F));

        PartDefinition group7 = group6.addOrReplaceChild("group7", CubeListBuilder.create().texOffs(74, 26).addBox(-3.5137F, -0.8038F, -21.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 5).addBox(-2.5137F, -6.4038F, -20.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(-0.15F))
                .texOffs(78, 137).addBox(-2.5137F, -6.4038F, -20.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(88, 62).addBox(-3.0137F, -7.4038F, -20.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(62, 137).addBox(-5.0273F, -5.3901F, -20.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(136, 62).addBox(0.0F, -5.3901F, -20.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(90, 89).addBox(-5.5273F, -4.0F, -21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(83, 26).addBox(-5.0273F, -3.8659F, -21.7071F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(83, 17).addBox(-5.0273F, -3.8659F, -19.2929F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 92).addBox(-5.0273F, -2.1588F, -21.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 90).addBox(-5.0273F, -4.573F, -21.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, 90).addBox(0.5F, -4.0F, -21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(74, 84).addBox(0.0F, -4.573F, -21.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(74, 56).addBox(0.0F, -3.8659F, -21.7071F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(73, 81).addBox(0.0F, -2.1588F, -21.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 12).addBox(0.0F, -3.8659F, -19.2929F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.795F, 11.6381F, 14.0F));

        PartDefinition cube_r25 = group7.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(133, 29).addBox(-0.5F, -0.4052F, 0.8194F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(133, 31).addBox(-0.5F, 1.0091F, -0.5948F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(133, 33).addBox(-0.5F, -0.4052F, -2.0091F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(133, 35).addBox(-0.5F, -1.8194F, -0.5948F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(133, 37).addBox(-5.5273F, -0.4052F, 0.8194F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 133).addBox(-5.5273F, 1.0091F, -0.5948F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(134, 64).addBox(-5.5273F, -1.8194F, -0.5948F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(132, 136).addBox(-5.5273F, -0.4052F, -2.0091F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -3.0F, -20.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r26 = group7.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(46, 135).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.205F, -6.9418F, -20.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r27 = group7.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(135, 67).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4433F, -6.6675F, -20.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r28 = group7.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(135, 69).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1554F, -6.1227F, -20.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r29 = group7.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(136, 136).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1827F, -6.1227F, -20.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r30 = group7.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(137, 65).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.584F, -6.6675F, -20.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r31 = group7.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(66, 137).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8223F, -6.9418F, -20.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r32 = group7.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(70, 137).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0137F, -5.4038F, -19.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r33 = group7.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(74, 137).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0137F, -5.4038F, -20.5F, -0.3927F, 0.0F, 0.0F));

        PartDefinition group3 = main.addOrReplaceChild("group3", CubeListBuilder.create().texOffs(38, 129).addBox(33.3404F, -24.0F, 9.9671F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(108, 128).addBox(-2.8494F, -24.0F, 9.9671F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.2455F, -8.0F, 10.5371F));

        PartDefinition cube_r34 = group3.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(142, 40).addBox(-5.4755F, -23.0F, -2.355F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(144, 84).addBox(-5.4755F, -63.0F, -2.355F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(56, 89).addBox(-4.9755F, -61.0F, -2.355F, 1.0F, 38.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1783F, 41.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r35 = group3.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(154, 155).addBox(-5.0F, -23.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(158, 56).addBox(-5.0F, -63.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(100, 89).addBox(-5.0F, -61.0F, -0.5F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9613F, 41.0F, 12.4185F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r36 = group3.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(46, 154).addBox(-11.8876F, -23.0F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(46, 158).addBox(-11.8876F, -63.0F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 90).addBox(-11.8876F, -61.0F, 7.924F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.957F, 41.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r37 = group3.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(142, 52).addBox(-5.0F, -23.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(156, 44).addBox(-5.0F, -63.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(122, 89).addBox(-5.0F, -61.0F, -0.5F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.6736F, 41.0F, 12.4185F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r38 = group3.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(120, 58).addBox(-11.8876F, -23.0F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(156, 40).addBox(-11.8876F, -63.0F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 127).addBox(-11.8876F, -61.0F, 7.924F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.6693F, 41.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r39 = group3.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(76, 137).addBox(3.4755F, -23.0F, -2.355F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(100, 128).addBox(3.4755F, -65.0F, -4.355F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(144, 72).addBox(3.4755F, -63.0F, -2.355F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(78, 89).addBox(3.9755F, -61.0F, -2.355F, 1.0F, 38.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.6693F, 41.0F, 2.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r40 = group3.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(22, 127).addBox(-5.4755F, -24.0F, -4.355F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1783F, 0.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r41 = group3.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(116, 128).addBox(-15.3978F, -24.0F, 2.6955F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.957F, 0.0F, 2.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r42 = group3.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(124, 128).addBox(-0.5F, -24.0F, -1.5F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.2455F, 0.0F, 10.7503F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r43 = group3.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(30, 129).addBox(-15.3978F, -24.0F, 2.6955F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.6693F, 0.0F, 2.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition group4 = group3.addOrReplaceChild("group4", CubeListBuilder.create().texOffs(68, 95).addBox(-7.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(24, 95).addBox(-4.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(20, 95).addBox(-1.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(16, 95).addBox(-16.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(94, 89).addBox(-13.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(62, 94).addBox(-10.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(58, 94).addBox(16.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(4, 94).addBox(13.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(0, 94).addBox(10.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(62, 90).addBox(1.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(56, 90).addBox(4.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(74, 59).addBox(7.5F, -23.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(0, 17).addBox(-17.5F, -22.5F, -3.5F, 36.0F, 31.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(0, 64).addBox(-22.5F, -23.0F, -3.5F, 46.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(15.7455F, 0.0F, 10.7503F));

        PartDefinition group5 = group3.addOrReplaceChild("group5", CubeListBuilder.create(), PartPose.offset(15.7455F, 0.0F, 10.7503F));

        PartDefinition group2 = main.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(132, 159).addBox(6.0F, 11.9F, 12.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(132, 83).addBox(6.0F, 6.9F, 12.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 73).addBox(0.0F, 1.9F, 6.0F, 16.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(92, 0).addBox(1.0F, 3.9F, 9.0F, 14.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(112, 82).addBox(4.0F, 10.363F, 1.3275F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(90, 17).addBox(1.0F, 0.9F, 7.0F, 14.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(46, 137).addBox(16.0F, -0.1F, 8.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(132, 136).addBox(-2.0F, -0.1F, 8.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(147, 58).addBox(-1.5F, -2.2075F, 9.6691F, 1.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(151, 145).addBox(-2.0F, -3.2075F, 9.6691F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(151, 1).addBox(16.0F, -3.2075F, 9.6691F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(80, 149).addBox(16.5F, -2.2075F, 9.6691F, 1.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(148, 136).addBox(3.0F, -14.1866F, 22.5922F, 10.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 89).addBox(4.0F, -13.1866F, 21.5922F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(94, 62).addBox(0.0F, 12.9F, 13.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(122, 40).addBox(7.0F, 12.9F, 6.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(90, 22).addBox(7.0F, 8.9F, 13.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(92, 4).addBox(7.5F, 13.9F, 21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(92, 0).addBox(7.5F, 13.9F, 5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(90, 93).addBox(-1.0F, 13.9F, 13.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 92).addBox(15.0F, 13.9F, 13.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, -8.0F));

        PartDefinition cube_r44 = group2.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(158, 60).addBox(-4.0F, -2.0F, -1.3F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -14.1537F, 22.771F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r45 = group2.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -3.5F, -2.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 0).addBox(1.0F, -2.5F, -3.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, -10.6866F, 24.5922F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r46 = group2.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(90, 17).addBox(-4.0F, -2.5F, -3.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 73).addBox(-5.0F, -3.5F, -2.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -10.6866F, 24.5922F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r47 = group2.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(130, 0).addBox(-7.0F, -11.5F, -3.0F, 14.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 133).addBox(-10.0F, -11.0F, -4.0F, 2.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 158).addBox(8.0F, -11.0F, -4.0F, 2.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(129, 13).addBox(-8.0F, -12.0F, -2.0F, 16.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 3.9F, 20.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r48 = group2.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(52, 90).addBox(8.5F, -4.0F, -9.7F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 66).addBox(-9.5F, -4.0F, -9.7F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 5.2F, 17.1F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r49 = group2.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(153, 27).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 1.9F, 6.0F, -0.3927F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}