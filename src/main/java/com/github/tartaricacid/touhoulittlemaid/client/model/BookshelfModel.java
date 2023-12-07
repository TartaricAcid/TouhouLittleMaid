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

public class BookshelfModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "bookshelf");
    private final ModelPart main;

    public BookshelfModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -26.0F, 31.0F, 32.0F, 41.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-16.0F, 15.0F, 19.0F, 32.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(66, 26).addBox(-2.0F, 11.0F, -2.25F, 13.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(77, 64).addBox(-1.0F, 14.0F, 9.75F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(105, 26).addBox(-1.0F, 14.0F, -3.25F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(71, 70).addBox(-3.0F, 15.0F, -15.25F, 15.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(77, 59).addBox(2.0F, 15.0F, -7.75F, 11.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(135, 150).addBox(9.0F, -11.0F, -6.25F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 161).addBox(-5.0137F, 12.9863F, -15.25F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(144, 122).addBox(8.0137F, 8.959F, -15.25F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(153, 74).addBox(-3.0F, 6.9453F, -15.25F, 10.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(8, 58).addBox(6.9863F, -13.0137F, -6.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 49).addBox(5.3F, 3.5453F, -14.25F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 56).addBox(3.3F, 3.5453F, -14.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 46).addBox(3.3F, 3.5453F, -14.25F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 42).addBox(3.3F, 6.1453F, -14.25F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 60).addBox(3.3F, 3.5453F, -12.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(147, 42).addBox(-3.0F, 10.9727F, -15.25F, 10.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(69, 79).addBox(10.0F, 14.0F, -3.25F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(77, 42).addBox(-3.0F, 14.0F, -3.25F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(66, 13).addBox(-15.0F, -13.0F, 19.0F, 30.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(66, 0).addBox(-15.0F, 1.0F, 19.0F, 30.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(26, 70).addBox(15.0F, -26.0F, 19.0F, 1.0F, 41.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 70).addBox(-16.0F, -26.0F, 19.0F, 1.0F, 41.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(81, 102).addBox(-19.0F, 6.0F, 15.75F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(-16.0F, -27.0F, 19.0F, 32.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(88, 78).addBox(-4.35F, -6.0F, -3.8F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(94, 90).addBox(-5.35F, -3.0F, -4.4F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(96, 42).addBox(-4.95F, 0.0F, -5.4F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.65F, 10.0F, 26.4F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 96).addBox(-4.35F, 3.0F, -4.4F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-26.55F, 10.0F, 25.4F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(52, 70).addBox(2.2335F, -12.9752F, -5.675F, 3.0F, 11.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4014F, 13.8604F, 3.425F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(8, 56).addBox(0.4911F, -1.5226F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9325F, -10.5F, -2.75F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 52).addBox(-1.8361F, -1.9602F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9325F, -10.5F, -2.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(9, 46).addBox(0.1776F, -1.0534F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9325F, -10.5F, -2.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(5, 46).addBox(0.8361F, -1.9602F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0401F, -10.5F, -2.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r8 = bone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(9, 42).addBox(-1.4911F, -1.5226F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0401F, -10.5F, -2.75F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(14, 70).addBox(0.0F, 0.0F, -4.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(-0.2F))
                .texOffs(40, 70).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(-0.2F))
                .texOffs(156, 0).addBox(-1.5F, -0.5F, -4.5F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.6259F, -11.2706F, -5.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r10 = bone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(82, 121).addBox(-1.1776F, -1.0534F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9462F, 9.459F, -11.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r11 = bone.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(105, 30).addBox(-1.4911F, -1.5226F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9462F, 9.459F, -11.75F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r12 = bone.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(96, 102).addBox(0.8361F, -1.9602F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9462F, 9.459F, -11.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r13 = bone.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(139, 160).addBox(-1.8361F, -1.9602F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(39, 143).addBox(0.1776F, 0.0534F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.9462F, 9.459F, -11.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r14 = bone.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(159, 103).addBox(0.4911F, -1.5226F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(15, 142).addBox(-1.5226F, 0.4911F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.9462F, 9.459F, -11.75F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r15 = bone.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(102, 150).addBox(0.1776F, -1.0534F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(87, 140).addBox(-1.8361F, 0.9602F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.9462F, 9.459F, -11.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r16 = bone.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(160, 141).addBox(0.0F, 0.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9239F, 11.3553F, -11.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r17 = bone.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(155, 160).addBox(0.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.631F, 12.0625F, -11.75F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r18 = bone.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 161).addBox(0.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0137F, 12.9863F, -11.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r19 = bone.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(121, 131).addBox(0.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.9239F, 15.6173F, -11.75F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r20 = bone.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(39, 123).addBox(0.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 16.0F, -11.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r21 = bone.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(15, 123).addBox(-1.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.631F, 14.9102F, -11.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r22 = bone.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(32, 161).addBox(0.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.631F, 14.9102F, -11.75F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r23 = bone.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(162, 50).addBox(-1.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9239F, 15.6173F, -11.75F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r24 = bone.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(164, 16).addBox(-1.0F, -1.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 16.0F, -11.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition group = main.addOrReplaceChild("group", CubeListBuilder.create().texOffs(63, 160).addBox(-2.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(154, 122).addBox(-5.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(153, 84).addBox(-8.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(111, 150).addBox(-11.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(87, 150).addBox(-14.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(48, 150).addBox(-17.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(149, 23).addBox(-20.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(147, 55).addBox(-23.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(145, 141).addBox(-26.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(144, 103).addBox(-29.5F, -5.0F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(24, 142).addBox(-2.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 142).addBox(-5.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(141, 4).addBox(-8.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(72, 140).addBox(-11.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(138, 74).addBox(-14.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(132, 45).addBox(-17.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(130, 131).addBox(-20.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(106, 131).addBox(-23.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(48, 131).addBox(-26.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(129, 93).addBox(-29.5F, -18.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(125, 26).addBox(-2.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(123, 64).addBox(-5.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(24, 123).addBox(-8.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 123).addBox(-11.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(91, 121).addBox(-14.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(67, 121).addBox(-17.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(120, 112).addBox(-20.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(108, 54).addBox(-23.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(52, 108).addBox(-26.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(105, 102).addBox(-29.5F, -32.9F, -4.5F, 3.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(22.5F, 10.0F, 18.5F));

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