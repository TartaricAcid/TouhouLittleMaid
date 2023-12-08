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

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, -6.0F));

        PartDefinition group = main.addOrReplaceChild("group", CubeListBuilder.create().texOffs(0, 0).addBox(-27.0F, -8.0F, -15.0F, 38.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(9.0F, -19.0F, -13.0F, 1.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(100, 119).addBox(-1.0F, -19.0F, -14.0F, 11.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(92, 6).addBox(-2.0F, -20.0F, -15.0F, 13.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(112, 88).addBox(5.0F, -24.0F, -14.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 63).addBox(5.5F, -30.0F, -13.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(136, 52).addBox(-17.0F, -8.5F, -14.5F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(46, 63).addBox(-15.0F, -9.25F, -13.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 15).addBox(-20.0F, -17.25F, -13.5F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-23.0F, 13.9F, -14.0F, 30.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(46, 65).addBox(-23.0F, -4.0F, -11.0F, 30.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(109, 17).addBox(6.0F, -2.0F, -14.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(16, 63).addBox(-23.0F, -2.0F, -14.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(30, 63).addBox(-24.0F, -7.0F, -14.0F, 1.0F, 23.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(105, 33).addBox(-30.0F, 3.0F, -14.0F, 6.0F, 13.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(7.0F, -7.0F, -14.0F, 1.0F, 23.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-24.0F, -7.0F, -15.0F, 32.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition cube_r1 = group.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(60, 120).addBox(-7.0F, -7.5F, 0.8F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 63).addBox(-2.0F, 0.5F, 0.8F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(102, 135).addBox(-4.0F, 1.25F, -0.2F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.75F, -11.5F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r2 = group.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(112, 68).addBox(-1.3F, -4.0F, -4.3F, 1.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.8915F, -13.25F, -5.8238F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r3 = group.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(37, 67).addBox(0.7F, -0.5F, 6.7F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(128, 34).addBox(-2.3F, 0.25F, 4.7F, 5.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2346F, -8.75F, -8.6522F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r4 = group.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(110, 57).addBox(-3.75F, -0.0333F, -6.0833F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(31, 63).addBox(-2.5F, -0.9833F, -2.8333F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-3.25F, -0.4833F, 1.4167F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.5733F, -8.2667F, -4.3254F, 0.0F, 0.3927F, 0.0F));

        PartDefinition group8 = main.addOrReplaceChild("group8", CubeListBuilder.create().texOffs(0, 17).addBox(-15.2458F, 1.9736F, -14.0516F, 30.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.2458F, -4.9736F, -2.1984F));

        PartDefinition cube_r5 = group8.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.9903F, -0.6223F, -0.9946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(0, 0).addBox(-2.9903F, -0.6223F, -0.5946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(0, 2).addBox(-2.8903F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.7597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.9597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.1597F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 28).addBox(0.3597F, -0.5223F, 1.5554F, 6.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-0.4903F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.2903F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.0653F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.8653F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.3597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.5597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(5.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.9597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.1597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.5597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(1.7597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.9597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(3.3597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.1597F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-0.6403F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.4403F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.3153F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.8903F, -0.5223F, 0.7054F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.7597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.6403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.8403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.0403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-0.2403F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.5597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(1.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.1597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.9597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(3.7597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.5597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(5.3597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.1597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.9597F, -0.5223F, -0.0946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.2403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.4403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-0.6403F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.1597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.9597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(1.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.5597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(3.3597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.1597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.9597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(5.7597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.5597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.3597F, -0.5223F, -0.8946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-9.2403F, -0.5223F, 1.0554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 5).addBox(-9.2403F, -0.5223F, -0.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-7.2403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-9.2403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-6.8403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-7.6403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-8.4403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-9.2403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-6.8403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 7).addBox(-7.6403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 7).addBox(-8.4403F, -0.5223F, -1.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-9.2403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-6.8403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-7.6403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-8.4403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 5).addBox(-9.2403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-6.8403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-7.6403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-8.4403F, -0.5223F, -0.0446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 5).addBox(-9.2403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-6.8403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-7.6403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-8.4403F, -0.5223F, -0.8446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.9403F, -0.5223F, 0.7554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.1403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.9403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-5.7403F, -0.5223F, 1.5554F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.1403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.9403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-5.7403F, -0.5223F, -0.6446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.1403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.9403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-5.7403F, -0.5223F, -1.4446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.9403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.6403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.8403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.0403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-0.2403F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(1.3597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.5597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.1597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.9597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(3.7597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.5597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(5.3597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.1597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.9597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.7597F, -0.5223F, -1.6946F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-5.7403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.9403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-4.1403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.9403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-2.1403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-1.3403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(-0.5403F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(0.5597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(1.3597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.1597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(2.9597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.0597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(4.8597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(5.6597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(6.4597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 2).addBox(7.7597F, -0.5223F, -2.5446F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(126, 6).addBox(-7.2403F, 1.0777F, -1.9446F, 13.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(92, 0).addBox(-9.2403F, 0.0777F, -2.4446F, 18.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition group3 = main.addOrReplaceChild("group3", CubeListBuilder.create(), PartPose.offset(0.0F, -8.72F, 27.3872F));

        PartDefinition cube_r6 = group3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(104, 67).addBox(-1.0F, -24.0F, -1.1F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.72F, 0.8002F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone = group3.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.2885F, 20.72F, 0.0499F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(132, 88).addBox(-11.8876F, -24.5F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(132, 96).addBox(-11.8876F, -63.0F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 100).addBox(-11.8876F, -62.0F, 7.924F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 21.0F, -8.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r8 = bone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(132, 92).addBox(-5.0F, -24.5F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(133, 0).addBox(-5.0F, -63.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(22, 100).addBox(-5.0F, -62.0F, -0.5F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.9957F, 21.0F, 2.4185F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(44, 100).addBox(-1.0F, -24.0F, -1.5F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.8064F, -20.0F, 0.9671F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r10 = bone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(52, 100).addBox(-15.3978F, -24.0F, 2.6955F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -20.0F, -8.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone2 = group3.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.2885F, 20.72F, 0.0499F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r11 = bone2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(132, 88).mirror().addBox(1.8876F, -24.5F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(132, 96).mirror().addBox(1.8876F, -63.0F, 7.424F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(60, 67).addBox(1.8876F, -62.0F, 7.924F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 21.0F, -8.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r12 = bone2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(132, 92).mirror().addBox(-5.0F, -24.5F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(133, 0).mirror().addBox(-5.0F, -63.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(82, 67).addBox(-5.0F, -62.0F, -0.5F, 10.0F, 38.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.9957F, 21.0F, 2.4185F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r13 = bone2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(44, 100).mirror().addBox(-1.0F, -24.0F, -1.5F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(17.8064F, -20.0F, 0.9671F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r14 = bone2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(52, 100).mirror().addBox(13.3978F, -24.0F, 2.6955F, 2.0F, 48.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -20.0F, -8.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition group2 = main.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(137, 114).addBox(6.0F, 11.9F, 12.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(104, 57).addBox(6.0F, 6.9F, 12.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(58, 49).addBox(0.0F, 1.9F, 6.0F, 16.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(74, 17).addBox(1.0F, 3.9F, 9.0F, 14.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(126, 9).addBox(4.0F, 10.363F, 1.3275F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 32).addBox(1.0F, 0.9F, 7.0F, 14.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(84, 119).addBox(16.0F, -0.1F, 8.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(116, 88).addBox(-2.0F, -0.1F, 8.0F, 2.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(115, 122).addBox(-1.5F, -2.2075F, 9.6691F, 1.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(128, 24).addBox(-2.0F, -3.2075F, 9.6691F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(126, 127).addBox(16.0F, -3.2075F, 9.6691F, 2.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(126, 114).addBox(16.5F, -2.2075F, 9.6691F, 1.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(126, 68).addBox(3.0F, -14.1866F, 22.5922F, 10.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(4.0F, -13.1866F, 21.5922F, 8.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 44).addBox(0.0F, 12.9F, 13.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 101).addBox(7.0F, 12.9F, 6.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(66, 37).addBox(7.0F, 8.9F, 13.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 58).addBox(7.5F, 13.9F, 21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 54).addBox(7.5F, 13.9F, 5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(31, 67).addBox(-1.0F, 13.9F, 13.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 63).addBox(15.0F, 13.9F, 13.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 0.0F, -8.0F));

        PartDefinition cube_r15 = group2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(80, 135).addBox(-4.0F, -2.0F, -1.3F, 8.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -14.1537F, 22.771F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r16 = group2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, -3.5F, -2.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(7, 0).addBox(1.0F, -2.5F, -3.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0F, -10.6866F, 24.5922F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r17 = group2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(66, 32).addBox(-4.0F, -2.5F, -3.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(-5.0F, -3.5F, -2.0F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -10.6866F, 24.5922F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r18 = group2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(116, 104).addBox(-7.0F, -11.5F, -3.0F, 14.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(120, 137).addBox(-10.0F, -11.0F, -4.0F, 2.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(132, 137).addBox(8.0F, -11.0F, -4.0F, 2.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 106).addBox(-8.0F, -12.0F, -2.0F, 16.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 3.9F, 20.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r19 = group2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(66, 48).addBox(8.5F, -4.0F, -9.7F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-9.5F, -4.0F, -9.7F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 5.2F, 17.1F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r20 = group2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(60, 129).addBox(-4.0F, 0.0F, 1.0F, 8.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 1.9F, 6.0F, -0.3927F, 0.0F, 0.0F));

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