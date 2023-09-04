package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EntityFairyModel extends EntityModel<EntityFairy> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "fairy");
    private final ModelPart head;
    private final ModelPart armRight;
    private final ModelPart armLeft;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart blink;

    public EntityFairyModel(ModelPart root) {
        this.head = root.getChild("head");
        this.armRight = root.getChild("armRight");
        this.armLeft = root.getChild("armLeft");
        this.body = root.getChild("body");
        this.legLeft = root.getChild("legLeft");
        this.legRight = root.getChild("legRight");
        this.wingLeft = root.getChild("wingLeft");
        this.wingRight = root.getChild("wingRight");
        this.blink = this.head.getChild("blink");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 24).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(3.5F, -0.5F, 3.75F));

        PartDefinition hair1 = hair.addOrReplaceChild("hair1", CubeListBuilder.create().texOffs(30, 23).addBox(-0.75F, 0.0F, -0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.3491F, -0.0873F));

        PartDefinition hair2 = hair.addOrReplaceChild("hair2", CubeListBuilder.create().texOffs(28, 23).addBox(-0.6F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.2618F, 0.1745F, -0.0524F));

        PartDefinition hair3 = hair.addOrReplaceChild("hair3", CubeListBuilder.create().texOffs(26, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.2967F, 0.0873F, -0.0175F));

        PartDefinition hair4 = hair.addOrReplaceChild("hair4", CubeListBuilder.create().texOffs(24, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.3316F, 0.0F, 0.0F));

        PartDefinition hair5 = hair.addOrReplaceChild("hair5", CubeListBuilder.create().texOffs(6, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.3316F, 0.0F, 0.0175F));

        PartDefinition hair6 = hair.addOrReplaceChild("hair6", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.2967F, -0.0873F, 0.0349F));

        PartDefinition hair7 = hair.addOrReplaceChild("hair7", CubeListBuilder.create().texOffs(2, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.2793F, -0.1745F, 0.0524F));

        PartDefinition hair8 = hair.addOrReplaceChild("hair8", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4F, 0.0F, -0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, 0.2443F, -0.3491F, 0.0698F));

        PartDefinition blink = head.addOrReplaceChild("blink", CubeListBuilder.create().texOffs(48, 10).addBox(-4.0F, -26.0F, -4.001F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition les = head.addOrReplaceChild("les", CubeListBuilder.create().texOffs(48, 18).addBox(-4.5F, -8.5F, -1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 23).addBox(-2.5F, -9.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(62, 20).addBox(2.5F, -8.75F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(59, 62).addBox(0.5F, -9.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 62).addBox(-1.5F, -9.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 62).addBox(-3.5F, -8.75F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 19).addBox(-5.0F, -6.5F, -0.001F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(61, 49).addBox(-4.75F, -7.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 19).addBox(4.0F, -6.5F, -0.001F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(61, 47).addBox(3.75F, -7.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(8, 60).addBox(-2.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 56).addBox(-2.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 6.5F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(36, 56).addBox(-0.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 60).addBox(0.0F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 6.5F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(43, 46).addBox(-2.5F, -7.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 40).addBox(-3.0F, -7.499F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-3.5F, -2.5F, -3.5F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-4.0F, 0.5F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, 3.5F, -4.5F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(42, 40).addBox(-3.5F, 0.5F, -2.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));

        PartDefinition les2 = body.addOrReplaceChild("les2", CubeListBuilder.create().texOffs(60, 58).addBox(-5.0F, 13.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 56).addBox(4.0F, 13.0F, 2.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 42).addBox(-5.0F, 13.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 40).addBox(4.0F, 13.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 5).addBox(-5.0F, 13.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 3).addBox(4.0F, 13.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 1).addBox(-5.0F, 13.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 60).addBox(4.0F, 13.0F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 60).addBox(-3.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 60).addBox(-3.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 60).addBox(-1.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(59, 38).addBox(-1.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(59, 36).addBox(0.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 48).addBox(0.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 46).addBox(2.5F, 13.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(57, 4).addBox(2.5F, 13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, 0.0F));

        PartDefinition les3 = body.addOrReplaceChild("les3", CubeListBuilder.create().texOffs(57, 2).addBox(-3.5F, -7.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(57, 0).addBox(-3.5F, -5.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(57, 57).addBox(-3.5F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 63).addBox(-3.5F, -6.5F, -2.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 63).addBox(-3.5F, -6.5F, 3.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 37).addBox(-3.5F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 35).addBox(-3.5F, -5.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(45, 56).addBox(-3.5F, -7.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 63).addBox(2.5F, -6.5F, -2.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 54).addBox(2.5F, -3.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 29).addBox(2.5F, -5.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 27).addBox(2.5F, -7.5F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 63).addBox(2.5F, -6.5F, 3.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(48, 25).addBox(2.5F, -3.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 23).addBox(2.5F, -5.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 10).addBox(2.5F, -7.5F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition apron = body.addOrReplaceChild("apron", CubeListBuilder.create().texOffs(27, 0).addBox(-3.5F, 0.0F, -0.001F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(27, 7).addBox(-3.5F, 1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(31, 7).addBox(-3.5F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 10).addBox(-3.5F, 5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 40).addBox(-2.5F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 10).addBox(2.5F, 5.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 42).addBox(1.5F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 42).addBox(-0.5F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 40).addBox(2.5F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 44).addBox(2.5F, 1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -3.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition decoration3 = body.addOrReplaceChild("decoration3", CubeListBuilder.create(), PartPose.offset(0.0F, -7.5F, 0.0F));

        PartDefinition bone7 = decoration3.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(24, 17).addBox(-2.25F, 0.5F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.1745F));

        PartDefinition bone8 = decoration3.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(24, 15).addBox(0.25F, 0.5F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, -0.1745F));

        PartDefinition bone9 = decoration3.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(36, 63).addBox(-0.5F, 1.25F, -2.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, -0.2618F));

        PartDefinition bone10 = decoration3.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(62, 35).addBox(-0.5F, 1.25F, -2.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.2618F));

        PartDefinition decoration4 = body.addOrReplaceChild("decoration4", CubeListBuilder.create().texOffs(24, 12).addBox(-1.0F, 0.0F, 0.7F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.75F, 3.0F));

        PartDefinition bone11 = decoration4.addOrReplaceChild("bone11", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition bone12 = bone11.addOrReplaceChild("bone12", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition bone13 = bone11.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(56, 32).addBox(-4.0F, 0.25F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.384F));

        PartDefinition bone14 = decoration4.addOrReplaceChild("bone14", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition bone16 = bone14.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(33, 51).addBox(0.0F, 0.25F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.384F));

        PartDefinition line = decoration4.addOrReplaceChild("line", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, -0.5236F));

        PartDefinition bone17 = line.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(26, 62).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 1.0F));

        PartDefinition bone18 = line.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(24, 62).addBox(0.2588F, -1.0F, 0.9659F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition bone19 = line.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(22, 59).addBox(0.7588F, -1.0F, 1.8319F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone20 = line.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(0, 59).addBox(1.4659F, -1.0F, 2.5391F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone21 = line.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(49, 55).addBox(2.3329F, -1.0F, 3.0391F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone22 = line.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(36, 55).addBox(3.2998F, -1.0F, 3.2976F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -1.309F, 0.0F));

        PartDefinition bone23 = line.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(40, 53).addBox(4.3007F, -1.0F, 3.2971F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone24 = line.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(24, 50).addBox(5.2663F, -1.0F, 3.0376F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -1.8326F, 0.0F));

        PartDefinition bone25 = line.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(28, 43).addBox(6.1329F, -1.0F, 2.537F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, 0.0F, -2.0944F, 0.0F));

        PartDefinition line2 = decoration4.addOrReplaceChild("line2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, -2.618F));

        PartDefinition bone26 = line2.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(41, 4).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.0F));

        PartDefinition bone27 = line2.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(41, 2).addBox(0.2588F, -1.0F, 0.9659F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition bone28 = line2.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(41, 0).addBox(0.7588F, -1.0F, 1.8319F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone29 = line2.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(28, 41).addBox(1.4659F, -1.0F, 2.5391F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone30 = line2.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(28, 39).addBox(2.3329F, -1.0F, 3.0391F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone31 = line2.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(30, 17).addBox(3.2998F, -1.0F, 3.2976F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -1.309F, 0.0F));

        PartDefinition bone32 = line2.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(30, 15).addBox(4.3007F, -1.0F, 3.2971F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone33 = line2.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(30, 13).addBox(5.2663F, -1.0F, 3.0376F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -1.8326F, 0.0F));

        PartDefinition bone34 = line2.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(30, 11).addBox(6.1329F, -1.0F, 2.537F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, -2.0944F, 0.0F));

        PartDefinition legLeft = partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(53, 20).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 15.0F, 0.0F));

        PartDefinition legRight = partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(24, 51).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 15.0F, 0.0F));

        PartDefinition wingLeft = partdefinition.addOrReplaceChild("wingLeft", CubeListBuilder.create().texOffs(0, 24).addBox(-0.3951F, -13.0F, -0.0012F, 0.0F, 24.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 11.0F, 4.0F, -0.0873F, 1.309F, 0.0F));

        PartDefinition wingRight = partdefinition.addOrReplaceChild("wingRight", CubeListBuilder.create().texOffs(0, 0).addBox(-0.6985F, -13.0F, 0.2899F, 0.0F, 24.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 5.0F, -0.0873F, -1.2217F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityFairy entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;
        armLeft.zRot = Mth.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.zRot = -Mth.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        if (entityIn.onGround()) {
            legLeft.xRot = Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legRight.xRot = -Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            armLeft.xRot = -Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            armRight.xRot = Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            wingLeft.yRot = -Mth.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
            wingRight.yRot = Mth.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        } else {
            legLeft.xRot = 0f;
            legRight.xRot = 0f;
            armLeft.xRot = -0.17453292F;
            armRight.xRot = -0.17453292F;
            head.xRot = head.xRot - 8 * 0.017453292F;
            wingLeft.yRot = -Mth.cos(ageInTicks * 0.5f) * 0.4f + 1.2f;
            wingRight.yRot = Mth.cos(ageInTicks * 0.5f) * 0.4f - 1.2f;
        }
        float remainder = ageInTicks % 60;
        // 0-10 显示眨眼贴图
        blink.visible = (55 < remainder && remainder < 60);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        armRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        armLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        wingLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        wingRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}