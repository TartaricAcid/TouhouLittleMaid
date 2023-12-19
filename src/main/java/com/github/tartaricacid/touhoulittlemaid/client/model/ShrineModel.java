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

public class ShrineModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "shrine");
    private final ModelPart main;

    public ShrineModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -5.0F, -7.5F, 15.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(60, 26).mirror().addBox(-3.5F, -19.0F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(73, 35).addBox(-3.5F, -17.5F, 2.5F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(31, 39).addBox(-5.0F, -10.5F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(42, 21).addBox(-0.5F, -23.75F, -3.75F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(-0.5F, -23.75F, 2.75F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 0).addBox(-0.5F, -24.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(21, 68).mirror().addBox(2.5F, -17.5F, -3.5F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(21, 68).addBox(-3.5F, -17.5F, -3.5F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(60, 26).addBox(-3.5F, -11.5F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(60, 26).addBox(-3.5F, -18.0F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 8).addBox(-2.5F, -2.5F, -6.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.6005F, 3.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r2 = main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(98, 0).addBox(-0.5F, -5.034F, 9.2477F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.2342F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r3 = main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(98, 0).mirror().addBox(-0.5F, -5.034F, -11.2477F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -16.2342F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r4 = main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(98, 0).mirror().addBox(-6.9399F, -5.7711F, -4.0F, 5.25F, 0.5F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(98, 0).addBox(-6.9399F, -5.7711F, 3.0F, 5.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(35, 21).addBox(-6.4781F, -5.6293F, -7.0F, 5.0F, 0.5F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.2342F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r5 = main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(98, 0).mirror().addBox(-6.0166F, -5.4559F, -4.0F, 0.5F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(98, 0).addBox(-6.0166F, -5.4559F, 3.0F, 0.5F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(33, 51).addBox(-5.8044F, -5.5973F, -7.0F, 0.5F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.2342F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r6 = main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(98, 0).addBox(1.6899F, -5.7711F, 3.0F, 5.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 0).addBox(1.6899F, -5.7711F, -4.0F, 5.25F, 0.5F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(35, 21).mirror().addBox(1.4781F, -5.6293F, -7.0F, 5.0F, 0.5F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -16.2342F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r7 = main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(98, 0).addBox(5.5166F, -5.4559F, 3.0F, 0.5F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 0).addBox(5.5166F, -5.4559F, -4.0F, 0.5F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 13).addBox(0.5771F, -5.5771F, -3.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(61, 13).addBox(0.2235F, -5.2235F, 2.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(33, 51).mirror().addBox(5.3044F, -5.5973F, -7.0F, 0.5F, 5.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -16.2342F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition bone7 = main.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(110, 36).addBox(2.4833F, -1.7325F, -3.7F, 0.55F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).mirror().addBox(-3.5F, 4.2508F, -3.7F, 7.0F, 0.55F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).mirror().addBox(-3.0333F, -1.7325F, -3.7F, 0.55F, 7.0F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).mirror().addBox(-3.5F, -1.2658F, -3.7F, 7.0F, 0.55F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).addBox(3.5F, -0.3158F, -3.5F, 0.2F, 0.2F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(110, 36).mirror().addBox(3.5F, -1.7325F, 2.4833F, 0.2F, 7.0F, 0.55F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).addBox(3.5F, 4.2508F, -3.5F, 0.2F, 0.55F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(3.5F, 3.6508F, -3.5F, 0.2F, 0.2F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(3.5F, -1.7325F, -3.0333F, 0.2F, 7.0F, 0.55F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(3.5F, -1.2658F, -3.5F, 0.2F, 0.55F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(3.5F, -1.7325F, -2.0833F, 0.2F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(3.5F, -1.7325F, 1.8833F, 0.2F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.0333F, -1.7325F, 3.5F, 0.55F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.5F, -1.2658F, 3.5F, 7.0F, 0.55F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(2.4833F, -1.7325F, 3.5F, 0.55F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(1.8833F, -1.7325F, 3.5F, 0.2F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.5F, 4.2508F, 3.5F, 7.0F, 0.55F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.5F, 3.6508F, 3.5F, 7.0F, 0.2F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.5F, -0.3158F, 3.5F, 7.0F, 0.2F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-2.0833F, -1.7325F, 3.5F, 0.2F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.7F, -1.7325F, 2.4833F, 0.2F, 7.0F, 0.55F, new CubeDeformation(0.0F))
                .texOffs(110, 36).mirror().addBox(-3.7F, -1.2658F, -3.5F, 0.2F, 0.55F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).mirror().addBox(-3.7F, -0.3158F, -3.5F, 0.2F, 0.2F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).addBox(-3.7F, -1.7325F, -3.0333F, 0.2F, 7.0F, 0.55F, new CubeDeformation(0.0F))
                .texOffs(110, 36).mirror().addBox(-3.7F, 4.2508F, -3.5F, 0.2F, 0.55F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).mirror().addBox(-3.7F, 3.6508F, -3.5F, 0.2F, 0.2F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 36).addBox(-3.7F, -1.7325F, -2.0833F, 0.2F, 7.0F, 0.2F, new CubeDeformation(0.0F))
                .texOffs(110, 36).addBox(-3.7F, -1.7325F, 1.8833F, 0.2F, 7.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.2342F, 0.0F));

        PartDefinition bone4 = main.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(32, 36).addBox(-6.75F, 7.9842F, 6.5F, 14.0F, 1.0F, 0.5F, new CubeDeformation(-0.1F))
                .texOffs(0, 35).addBox(3.25F, 7.9842F, -6.5F, 4.0F, 1.0F, 0.5F, new CubeDeformation(-0.1F))
                .texOffs(0, 35).addBox(-6.75F, 7.9842F, -6.5F, 4.0F, 1.0F, 0.5F, new CubeDeformation(-0.1F))
                .texOffs(60, 10).addBox(6.5F, 7.9842F, -6.75F, 0.5F, 1.0F, 14.0F, new CubeDeformation(-0.1F))
                .texOffs(58, 37).addBox(-6.5F, 7.9842F, -6.75F, 0.5F, 1.0F, 14.0F, new CubeDeformation(-0.1F)), PartPose.offset(-0.25F, -16.2342F, -0.25F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(62, 0).addBox(-6.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.1F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -5.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -4.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -3.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -2.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -1.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -0.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 0.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 1.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 2.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 3.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 4.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 5.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 5.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 4.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 3.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 2.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 1.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 0.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -0.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -1.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -2.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -3.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -4.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -5.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.1F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.1F))
                .texOffs(62, 0).addBox(-5.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-4.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-3.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-2.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-1.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-0.75F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(0.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(1.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(2.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(3.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(4.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(5.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, 6.25F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.1F))
                .texOffs(62, 0).addBox(-6.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-5.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-4.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-3.75F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(3.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(4.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(5.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(6.25F, 7.4842F, -6.75F, 0.5F, 7.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 0.0F, 0.25F));

        PartDefinition bone3 = main.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(48, 56).addBox(-5.5F, -8.0F, -5.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(33, 51).addBox(2.5F, -8.0F, -5.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-5.5F, -8.0F, -3.5F, 11.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition bone6 = main.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 68).addBox(3.0F, 4.7342F, -4.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-4.5F, 4.7342F, -4.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(37, 21).addBox(3.0F, -4.5158F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-4.5F, 4.7342F, 3.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 68).addBox(-4.0F, 4.7342F, -4.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(37, 21).addBox(-4.0F, -4.5158F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(37, 21).addBox(3.0F, -4.5158F, 3.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(37, 21).addBox(-4.0F, -4.5158F, 3.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 10).addBox(-4.5F, -3.7658F, 3.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(65, 53).addBox(-4.0F, -3.7658F, -6.5F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(65, 53).addBox(3.0F, -3.7658F, -6.5F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(46, 10).addBox(-4.5F, -3.7658F, -4.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 0).addBox(-4.5F, -1.7658F, -4.5F, 9.0F, 0.5F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.2342F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
