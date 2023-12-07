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

public class KeyboardModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "keyboard");
    private final ModelPart main;

    public KeyboardModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-29.0F, 1.0F, 12.0F, 42.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-29.0F, -1.0F, 18.0F, 42.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(70, 75).addBox(-12.0F, -1.1F, 22.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 74).addBox(-9.0F, -1.1F, 20.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 73).addBox(-9.0F, -1.1F, 19.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(62, 66).addBox(-6.3F, -1.35F, 19.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(38, 66).addBox(-6.3F, -1.35F, 19.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(40, 73).addBox(-6.3F, -1.35F, 20.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 49).addBox(-4.3F, -1.35F, 20.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(38, 63).addBox(-4.3F, -1.35F, 19.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(62, 63).addBox(-4.3F, -1.35F, 19.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(81, 83).addBox(-3.3F, -1.35F, 23.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(13, 84).addBox(-3.3F, -1.35F, 22.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(20, 84).addBox(-3.3F, -1.35F, 22.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(10, 82).addBox(-2.3F, -1.35F, 23.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(57, 83).addBox(-2.3F, -1.35F, 22.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(73, 83).addBox(-2.3F, -1.35F, 22.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(81, 80).addBox(-2.3F, -1.35F, 20.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 82).addBox(-2.3F, -1.35F, 19.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(4, 82).addBox(-2.3F, -1.35F, 19.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(77, 47).addBox(-1.3F, -1.35F, 20.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(17, 78).addBox(-1.3F, -1.35F, 19.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(34, 80).addBox(-1.3F, -1.35F, 19.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(80, 40).addBox(-1.3F, -1.35F, 22.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(81, 35).addBox(-1.3F, -1.35F, 22.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 81).addBox(-1.3F, -1.35F, 23.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-15.7F, -1.35F, 20.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(44, 46).addBox(-15.7F, -1.35F, 19.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(44, 53).addBox(-15.7F, -1.35F, 19.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(57, 35).addBox(-15.7F, -1.35F, 22.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(44, 58).addBox(-15.7F, -1.35F, 22.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(17, 74).addBox(-15.7F, -1.35F, 23.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(34, 74).addBox(-14.7F, -1.35F, 23.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(20, 76).addBox(-14.7F, -1.35F, 22.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(40, 76).addBox(-13.7F, -1.35F, 23.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(61, 76).addBox(-13.7F, -1.35F, 22.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 77).addBox(-13.7F, -1.35F, 22.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(34, 77).addBox(-14.7F, -1.35F, 22.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(77, 35).addBox(-14.7F, -1.35F, 20.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(77, 38).addBox(-14.7F, -1.35F, 19.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(77, 44).addBox(-14.7F, -1.35F, 19.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(8, 0).addBox(-13.7F, -1.35F, 20.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 11).addBox(-13.7F, -1.35F, 19.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 27).addBox(-13.7F, -1.35F, 19.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(44, 41).addBox(-11.7F, -1.35F, 20.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(53, 38).addBox(-11.7F, -1.35F, 19.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(53, 41).addBox(-11.7F, -1.35F, 19.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(109, 0).addBox(-29.0F, -0.4142F, 13.4142F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(77, 45).addBox(9.0F, -0.4142F, 13.4142F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 58).addBox(-20.0F, 2.0F, 22.0F, 24.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(111, 36).addBox(-1.0F, 2.0F, 17.0F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-19.0F, 2.0F, 17.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(64, 83).addBox(10.0F, 15.0F, 22.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 81).addBox(-29.0F, 15.0F, 23.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(44, 53).addBox(-20.0F, 2.0F, 13.0F, 24.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-29.0F, 15.0F, 13.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 73).addBox(-11.5F, 6.0F, -2.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(8.5F, -5.0F, -15.0F, 7.0F, 21.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(44, 35).addBox(7.5F, -6.0F, -16.0F, 8.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 74).addBox(9.5F, -7.0F, -9.25F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.5F, -14.0F, -7.75F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(9, 30).addBox(10.25F, -11.0F, -6.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(38, 69).addBox(10.75F, -11.0F, -6.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(38, 63).addBox(-12.0F, 7.0F, -3.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(116, 7).addBox(-6.0F, 9.0F, -3.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(26, 74).addBox(-6.0F, 9.0F, 3.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-12.0F, 9.0F, 3.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 54).addBox(-11.5F, 11.0F, -1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 46).addBox(-5.5F, 11.0F, -1.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-12.0F, 9.0F, -3.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 46).addBox(-10.0F, 13.0F, 3.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(89, 75).addBox(-10.0F, 13.0F, -2.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 38).addBox(10.0F, 15.0F, 14.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, 1.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.275F))
                .texOffs(0, 0).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(8, 33).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(11.25F, -10.5F, -5.75F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(44, 38).addBox(-7.0F, 4.5F, -4.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(114, 91).addBox(17.0F, 4.5F, -4.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(29, 29).addBox(-10.0F, 4.5F, -5.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 31).addBox(-10.0F, 4.5F, 2.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 1.5F, 20.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(59, 73).addBox(6.0F, 4.5F, -5.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(15, 74).addBox(-18.0F, 4.5F, -5.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(29, 27).addBox(-24.0F, 4.5F, -6.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 33).addBox(-24.0F, 4.5F, 3.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 1.5F, 20.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(26, 73).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-8.0F, 6.9F, 19.5F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(38, 38).addBox(-2.0F, -0.5F, -1.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 86).addBox(-40.0F, -0.5F, -1.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 0.2929F, 13.4142F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(70, 63).addBox(-7.0F, -10.5F, -1.0F, 14.0F, 11.0F, 1.0F, new CubeDeformation(-0.475F))
                .texOffs(29, 35).addBox(-6.0F, -0.4F, -1.4F, 12.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
                .texOffs(0, 63).addBox(-9.0F, -9.0F, -0.5F, 18.0F, 10.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 26.5F, -0.3927F, 0.0F, 0.0F));

        PartDefinition group = main.addOrReplaceChild("group", CubeListBuilder.create().texOffs(99, 0).addBox(-14.0F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 98).addBox(-10.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(96, 45).addBox(-13.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(30, 96).addBox(-12.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(20, 95).addBox(-4.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(94, 93).addBox(-3.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(94, 84).addBox(-5.9F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(93, 16).addBox(-6.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(10, 93).addBox(-7.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(50, 92).addBox(-9.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(84, 91).addBox(-1.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(91, 75).addBox(0.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(91, 27).addBox(-0.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(0, 91).addBox(2.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(74, 90).addBox(3.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(40, 90).addBox(4.9F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(64, 89).addBox(5.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(87, 36).addBox(6.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(30, 87).addBox(9.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(20, 86).addBox(8.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(10, 84).addBox(11.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(54, 83).addBox(-16.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(0, 82).addBox(-15.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(81, 81).addBox(15.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(44, 81).addBox(14.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(71, 80).addBox(13.0F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(77, 35).addBox(12.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F)), PartPose.offset(0.0F, 1.2F, 13.4F));

        PartDefinition group2 = main.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(40, 99).addBox(16.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(20, 113).addBox(-14.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(111, 18).addBox(-11.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(111, 27).addBox(-10.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(111, 70).addBox(-13.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(90, 111).addBox(-12.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(0, 109).addBox(-4.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(109, 52).addBox(-3.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(80, 109).addBox(-6.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(50, 110).addBox(-5.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(110, 61).addBox(-7.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(110, 100).addBox(-8.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(110, 109).addBox(-9.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(10, 111).addBox(-9.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(101, 25).addBox(-2.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(101, 34).addBox(-1.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(50, 101).addBox(-0.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(101, 73).addBox(-0.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(10, 102).addBox(1.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(90, 102).addBox(0.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(100, 103).addBox(3.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(20, 104).addBox(2.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(104, 82).addBox(4.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(104, 91).addBox(5.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(30, 105).addBox(6.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(106, 9).addBox(7.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(106, 43).addBox(8.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(60, 107).addBox(8.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(40, 108).addBox(10.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 108).addBox(9.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(114, 79).addBox(-17.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(30, 114).addBox(-16.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(100, 112).addBox(-15.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(99, 55).addBox(15.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 99).addBox(14.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(0, 100).addBox(13.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(100, 64).addBox(12.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(80, 100).addBox(11.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F)), PartPose.offset(0.0F, 1.2F, 13.4F));

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