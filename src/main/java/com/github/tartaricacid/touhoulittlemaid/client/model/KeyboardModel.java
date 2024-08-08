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

public class KeyboardModel extends AbstractModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "keyboard");
    private final ModelPart main;

    public KeyboardModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 7.0F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-29.0F, 1.0F, 12.0F, 42.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-29.0F, -1.0F, 18.0F, 42.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(46, 71).addBox(-12.0F, -1.1F, 22.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(29, 32).addBox(-9.0F, -1.1F, 20.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 29).addBox(-9.0F, -1.1F, 19.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 76).addBox(-29.0F, -0.4142F, 13.4142F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 75).addBox(9.0F, -0.4142F, 13.4142F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(-20.0F, 2.0F, 22.0F, 24.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-19.0F, 2.0F, 17.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).mirror().addBox(1.0F, 2.0F, 17.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 11).addBox(10.0F, 15.0F, 22.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-29.0F, 15.0F, 23.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(-20.0F, 2.0F, 13.0F, 24.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-29.0F, 15.0F, 13.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(8.5F, -5.0F, -15.0F, 7.0F, 21.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(44, 29).addBox(7.5F, -6.0F, -16.0F, 8.0F, 1.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(60, 63).addBox(9.5F, -7.0F, -9.25F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.5F, -14.0F, -7.75F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(9, 30).addBox(10.25F, -11.0F, -6.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(29, 35).addBox(10.75F, -11.0F, -6.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 11).addBox(10.0F, 15.0F, 14.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -11.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, 1.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.275F))
                .texOffs(0, 0).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(8, 33).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(11.25F, -10.5F, -5.75F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 76).addBox(-7.0F, 4.5F, -4.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(48, 76).addBox(17.0F, 4.5F, -4.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(29, 27).addBox(-10.0F, 4.5F, -5.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 27).addBox(-10.0F, 4.5F, 2.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 1.5F, 20.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(35, 71).addBox(6.0F, 4.5F, -5.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(35, 71).addBox(-18.0F, 4.5F, -5.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(29, 27).addBox(-24.0F, 4.5F, -6.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(29, 27).addBox(-24.0F, 4.5F, 3.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 1.5F, 20.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 63).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-8.0F, 6.9F, 19.5F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(44, 42).addBox(-2.0F, -0.5F, -1.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 71).addBox(-40.0F, -0.5F, -1.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 0.2929F, 13.4142F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 63).addBox(-7.0F, -10.5F, -1.0F, 14.0F, 11.0F, 1.0F, new CubeDeformation(-0.475F))
                .texOffs(29, 39).addBox(-6.0F, -0.4F, -1.4F, 12.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
                .texOffs(44, 52).addBox(-9.0F, -9.0F, -0.5F, 18.0F, 10.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 26.5F, -0.3927F, 0.0F, 0.0F));

        PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(8, 0).addBox(-5.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(8, 0).addBox(-3.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(8, 0).addBox(1.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(8, 0).addBox(3.7F, -2.85F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(-8.0F, 1.5F, 19.5F));

        PartDefinition bone4 = bone.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(9, 11).addBox(-5.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 11).addBox(-3.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 11).addBox(1.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 11).addBox(3.7F, -2.85F, 0.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(-8.0F, 1.5F, 19.5F));

        PartDefinition bone5 = bone.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(9, 27).addBox(-5.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 27).addBox(-3.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 27).addBox(1.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(9, 27).addBox(3.7F, -2.85F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(-8.0F, 1.5F, 19.5F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 27).addBox(-7.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-7.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-7.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-6.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-6.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-6.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-5.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-5.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-5.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(4.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-7.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-7.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-7.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-6.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-6.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(-6.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(4.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(4.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(5.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(5.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(5.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(6.7F, -2.85F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(6.7F, -2.85F, 3.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(6.7F, -2.85F, 2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(5.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(5.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(5.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(6.7F, -2.85F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(6.7F, -2.85F, 0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 27).addBox(6.7F, -2.85F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(-8.0F, 1.5F, 19.5F));

        PartDefinition group = bone.addOrReplaceChild("group", CubeListBuilder.create().texOffs(60, 71).addBox(-14.0F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-10.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-13.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-12.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-4.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-3.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-5.9F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-6.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-7.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-9.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-1.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(0.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-0.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(2.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(3.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(4.9F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(5.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(6.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(9.4F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(8.5F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(11.2F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-16.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(-15.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(15.7F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(14.8F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(13.0F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F))
                .texOffs(60, 71).addBox(12.1F, -1.1F, -6.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.175F)), PartPose.offset(-8.0F, 1.2F, 21.4F));

        PartDefinition group2 = bone.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(70, 72).addBox(16.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-14.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-11.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-10.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-13.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-12.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-4.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-3.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-6.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-5.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-7.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-8.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-9.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-9.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-2.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-1.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-0.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-0.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(1.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(0.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(3.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(2.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(4.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(5.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(6.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(7.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(8.95F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(8.05F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(10.75F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(9.85F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-17.15F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-16.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(-15.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(15.25F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(14.35F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(13.45F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(12.55F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F))
                .texOffs(70, 72).addBox(11.65F, -0.725F, -8.85F, 1.0F, 1.0F, 8.0F, new CubeDeformation(-0.075F)), PartPose.offset(-8.0F, 1.2F, 21.4F));

        PartDefinition bone6 = main.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(32, 63).addBox(-3.5F, 6.0F, -10.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(29, 29).addBox(-4.0F, 7.0F, -11.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-4.0F, 9.0F, -11.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, 9.0F, -5.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(77, 38).addBox(-3.5F, 11.0F, -9.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(31, 76).addBox(-2.0F, 13.0F, -10.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(80, 61).addBox(2.0F, 9.0F, -11.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(13, 76).addBox(-2.0F, 13.0F, -4.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(77, 29).addBox(2.0F, 9.0F, -5.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(53, 63).addBox(2.5F, 11.0F, -9.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}