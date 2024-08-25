package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PicnicMatModel extends AbstractModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "picnic_mat");
    private final ModelPart all;
    private final ModelPart hide;

    public PicnicMatModel(ModelPart root) {
        this.all = root.getChild("all");
        this.hide = root.getChild("hide");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition mat = all.addOrReplaceChild("mat", CubeListBuilder.create().texOffs(94, 96).addBox(-0.25F, -40.0F, -40.0F, 1.0F, 80.0F, 80.0F, new CubeDeformation(-0.25F)).texOffs(0, 0).addBox(-0.25F, -40.0F, -40.0F, 1.0F, 80.0F, 80.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition basket = all.addOrReplaceChild("basket", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.9F, 1.75F, 0.0F, -2.6616F, 0.0F));

        PartDefinition bone6 = basket.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(157, 62).mirror().addBox(-7.5F, -0.5F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(157, 58).mirror().addBox(-7.5F, -0.5F, -7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(156, 40).addBox(-8.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2153F, -6.5F, -3.0594F, 0.3198F, -0.422F, -0.6799F));

        PartDefinition bone5 = basket.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(157, 62).addBox(-0.5F, -0.5F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(157, 58).addBox(-0.5F, -0.5F, -7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(156, 40).addBox(7.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5005F, -6.5F, 0.2406F, -0.3198F, -0.422F, 0.6799F));

        PartDefinition box = basket.addOrReplaceChild("box", CubeListBuilder.create().texOffs(196, 58).addBox(-9.0F, -1.0F, -6.0F, 18.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(230, 38).addBox(-9.0F, -7.0F, -6.0F, 1.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(230, 18).addBox(8.0F, -7.0F, -6.0F, 1.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(222, 9).addBox(-8.0F, -7.0F, 5.0F, 16.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(222, 0).addBox(-8.0F, -7.0F, -6.0F, 16.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6426F, -1.0F, -1.4094F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone4 = basket.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(196, 74).addBox(-9.0F, -3.0F, -12.0F, 18.0F, 1.0F, 12.0F, new CubeDeformation(0.01F)).texOffs(186, 0).addBox(-8.0F, -2.0F, -12.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(186, 13).addBox(-8.0F, -2.0F, -1.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(202, 21).addBox(8.01F, -2.0F, -12.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(202, 40).addBox(-9.01F, -2.0F, -12.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3574F, -8.0F, 3.7868F, 2.7925F, -0.5236F, 0.0F));

        PartDefinition bone8 = bone4.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(160, 0).mirror().addBox(1.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(160, 0).mirror().addBox(-7.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(142, 0).addBox(-7.5F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(142, 0).addBox(0.5F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(142, 5).addBox(3.5F, -1.0F, -3.5F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(142, 5).addBox(-4.5F, -1.0F, -3.5F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.95F, -6.0F));

        PartDefinition bread = all.addOrReplaceChild("bread", CubeListBuilder.create().texOffs(0, 168).addBox(-8.0F, -0.875F, -8.9219F, 15.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(25.0F, -0.625F, 1.8594F, 0.0F, -0.5672F, 0.0F));

        PartDefinition cake = all.addOrReplaceChild("cake", CubeListBuilder.create().texOffs(0, 224).addBox(-7.0F, -1.5F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.0F, 0.1F, 1.0F, 0.0F, 0.6109F, 0.0F));

        PartDefinition drinks = all.addOrReplaceChild("drinks", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition drink1 = drinks.addOrReplaceChild("drink1", CubeListBuilder.create().texOffs(21, 247).addBox(-1.6667F, -0.75F, -1.6667F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(-2.1667F, -1.25F, -2.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(-0.1667F, -4.25F, -0.1667F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(14.1667F, -5.75F, -10.8333F, 0.0F, -0.5236F, 0.0F));

        PartDefinition drink2 = drinks.addOrReplaceChild("drink2", CubeListBuilder.create().texOffs(50, 247).addBox(-1.6667F, -0.75F, -1.6667F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(-2.1667F, -1.25F, -2.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(-0.1667F, -4.25F, -0.1667F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-18.8333F, -5.75F, -10.8333F, 0.0F, -0.5236F, 0.0F));

        PartDefinition drink3 = drinks.addOrReplaceChild("drink3", CubeListBuilder.create().texOffs(35, 247).addBox(7.3333F, -0.75F, 0.3333F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(6.8333F, -1.25F, -0.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(8.8333F, -4.25F, 1.8333F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(14.1667F, -5.75F, 8.1667F, 0.0F, -1.2217F, 0.0F));

        PartDefinition drink4 = drinks.addOrReplaceChild("drink4", CubeListBuilder.create().texOffs(21, 247).addBox(-1.6667F, -0.75F, -1.6667F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(-2.1667F, -1.25F, -2.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(-0.1667F, -4.25F, -0.1667F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-11.8333F, -5.75F, 15.1667F, 0.0F, -0.5236F, 0.0F));

        PartDefinition plates = all.addOrReplaceChild("plates", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition plate1 = plates.addOrReplaceChild("plate1", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, -1.0F, 23.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone69 = plate1.addOrReplaceChild("bone69", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, -100.0F));

        PartDefinition bone70 = bone69.addOrReplaceChild("bone70", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 49.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone71 = bone69.addOrReplaceChild("bone71", CubeListBuilder.create().texOffs(3, 7).addBox(31.734F, -0.5F, 35.8553F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone72 = bone69.addOrReplaceChild("bone72", CubeListBuilder.create().texOffs(3, 7).addBox(45.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone73 = bone69.addOrReplaceChild("bone73", CubeListBuilder.create().texOffs(3, 7).addBox(31.734F, -0.5F, -30.6127F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone74 = bone69.addOrReplaceChild("bone74", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, -44.3787F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone75 = bone69.addOrReplaceChild("bone75", CubeListBuilder.create().texOffs(3, 7).addBox(-34.734F, -0.5F, -30.6127F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone76 = bone69.addOrReplaceChild("bone76", CubeListBuilder.create().texOffs(3, 7).addBox(-48.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone77 = bone69.addOrReplaceChild("bone77", CubeListBuilder.create().texOffs(3, 7).addBox(-34.734F, -0.5F, 35.8553F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone60 = plate1.addOrReplaceChild("bone60", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, -74.0F));

        PartDefinition bone61 = bone60.addOrReplaceChild("bone61", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, 46.6142F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone62 = bone60.addOrReplaceChild("bone62", CubeListBuilder.create().texOffs(2, 1).addBox(32.234F, -0.5F, 32.7482F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone63 = bone60.addOrReplaceChild("bone63", CubeListBuilder.create().texOffs(2, 1).addBox(46.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone64 = bone60.addOrReplaceChild("bone64", CubeListBuilder.create().texOffs(2, 1).addBox(32.234F, -0.5F, -33.7198F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone65 = bone60.addOrReplaceChild("bone65", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -47.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone66 = bone60.addOrReplaceChild("bone66", CubeListBuilder.create().texOffs(2, 1).addBox(-34.234F, -0.5F, -33.7198F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone67 = bone60.addOrReplaceChild("bone67", CubeListBuilder.create().texOffs(2, 1).addBox(-48.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone68 = bone60.addOrReplaceChild("bone68", CubeListBuilder.create().texOffs(2, 1).addBox(-34.234F, -0.5F, 32.7482F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition plate2 = plates.addOrReplaceChild("plate2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 0.0F, 19.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone174 = plate2.addOrReplaceChild("bone174", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -54.0F));

        PartDefinition bone175 = bone174.addOrReplaceChild("bone175", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone176 = bone174.addOrReplaceChild("bone176", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone177 = bone174.addOrReplaceChild("bone177", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone178 = bone174.addOrReplaceChild("bone178", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone179 = bone174.addOrReplaceChild("bone179", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone180 = bone174.addOrReplaceChild("bone180", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone181 = bone174.addOrReplaceChild("bone181", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone182 = bone174.addOrReplaceChild("bone182", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone183 = plate2.addOrReplaceChild("bone183", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -28.0F));

        PartDefinition bone184 = bone183.addOrReplaceChild("bone184", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone185 = bone183.addOrReplaceChild("bone185", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone186 = bone183.addOrReplaceChild("bone186", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone187 = bone183.addOrReplaceChild("bone187", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone188 = bone183.addOrReplaceChild("bone188", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone189 = bone183.addOrReplaceChild("bone189", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone190 = bone183.addOrReplaceChild("bone190", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone191 = bone183.addOrReplaceChild("bone191", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone195 = plate2.addOrReplaceChild("bone195", CubeListBuilder.create().texOffs(33, 230).addBox(-0.5F, -0.45F, -0.975F, 1.0F, 0.9F, 2.5F, new CubeDeformation(-0.275F)).texOffs(5, 1).addBox(-0.5F, -0.45F, -1.375F, 1.0F, 0.9F, 1.7F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(-1.0F, -0.75F, 4.9337F, -0.2443F, 0.0F, 0.0F));

        PartDefinition bone196 = bone195.addOrReplaceChild("bone196", CubeListBuilder.create().texOffs(5, 1).addBox(-0.5F, -0.4425F, -0.5193F, 1.0F, 0.9F, 0.9F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.975F, -0.7418F, 0.0F, 0.0F));

        PartDefinition bone198 = bone196.addOrReplaceChild("bone198", CubeListBuilder.create().texOffs(5, 1).addBox(-0.5F, -0.381F, -0.658F, 1.0F, 0.9F, 0.9F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition bone199 = bone198.addOrReplaceChild("bone199", CubeListBuilder.create().texOffs(5, 1).addBox(-0.5F, -0.4909F, -0.3764F, 1.0F, 0.9F, 0.9F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.1811F, -0.3787F, 0.5672F, 0.0F, 0.0F));

        PartDefinition bone204 = bone198.addOrReplaceChild("bone204", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1811F, -0.3787F));

        PartDefinition bone197 = bone204.addOrReplaceChild("bone197", CubeListBuilder.create().texOffs(5, 1).addBox(-0.5F, -0.4603F, -0.412F, 1.0F, 0.9F, 1.1F, new CubeDeformation(-0.35F)).texOffs(5, 1).addBox(-0.45F, -0.4603F, -1.412F, 0.9F, 0.9F, 1.7F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.2503F, -0.0789F, 1.309F, 0.0F, 0.0F));

        PartDefinition bone200 = bone204.addOrReplaceChild("bone200", CubeListBuilder.create().texOffs(5, 1).addBox(-0.2629F, -0.3149F, -0.675F, 1.0F, 0.9F, 1.2F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.2702F, -0.2348F, 1.3835F, -0.1841F, -0.7681F));

        PartDefinition bone203 = bone200.addOrReplaceChild("bone203", CubeListBuilder.create().texOffs(5, 1).addBox(-0.3812F, -0.3149F, -1.0111F, 0.9F, 0.9F, 1.7F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0155F, 0.0F, -0.3974F, 0.0F, 0.9163F, 0.0F));

        PartDefinition bone201 = bone204.addOrReplaceChild("bone201", CubeListBuilder.create().texOffs(5, 1).mirror().addBox(-0.7371F, -0.3149F, -0.675F, 1.0F, 0.9F, 1.2F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.2702F, -0.2348F, 1.3835F, 0.1841F, 0.7681F));

        PartDefinition bone202 = bone201.addOrReplaceChild("bone202", CubeListBuilder.create().texOffs(5, 1).mirror().addBox(-0.5188F, -0.3149F, -1.0111F, 0.9F, 0.9F, 1.7F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offsetAndRotation(-0.0155F, 0.0F, -0.3974F, 0.0F, -0.9163F, 0.0F));

        PartDefinition plate3 = plates.addOrReplaceChild("plate3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.0F, -19.0F, 0.0F, 0.48F, 0.0F));

        PartDefinition bone97 = plate3.addOrReplaceChild("bone97", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -31.0F));

        PartDefinition bone98 = bone97.addOrReplaceChild("bone98", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -23.0F));

        PartDefinition bone99 = bone98.addOrReplaceChild("bone99", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone100 = bone98.addOrReplaceChild("bone100", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone101 = bone98.addOrReplaceChild("bone101", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone102 = bone98.addOrReplaceChild("bone102", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone103 = bone98.addOrReplaceChild("bone103", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone104 = bone98.addOrReplaceChild("bone104", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone105 = bone98.addOrReplaceChild("bone105", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone106 = bone98.addOrReplaceChild("bone106", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone107 = bone97.addOrReplaceChild("bone107", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 3.0F));

        PartDefinition bone108 = bone107.addOrReplaceChild("bone108", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone109 = bone107.addOrReplaceChild("bone109", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone110 = bone107.addOrReplaceChild("bone110", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone111 = bone107.addOrReplaceChild("bone111", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone112 = bone107.addOrReplaceChild("bone112", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone113 = bone107.addOrReplaceChild("bone113", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone114 = bone107.addOrReplaceChild("bone114", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone115 = bone107.addOrReplaceChild("bone115", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone116 = plate3.addOrReplaceChild("bone116", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, -31.0F));

        PartDefinition bone117 = bone116.addOrReplaceChild("bone117", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -23.0F));

        PartDefinition bone118 = bone117.addOrReplaceChild("bone118", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone119 = bone117.addOrReplaceChild("bone119", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone120 = bone117.addOrReplaceChild("bone120", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone121 = bone117.addOrReplaceChild("bone121", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone122 = bone117.addOrReplaceChild("bone122", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone123 = bone117.addOrReplaceChild("bone123", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone124 = bone117.addOrReplaceChild("bone124", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone125 = bone117.addOrReplaceChild("bone125", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone126 = bone116.addOrReplaceChild("bone126", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 3.0F));

        PartDefinition bone127 = bone126.addOrReplaceChild("bone127", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone128 = bone126.addOrReplaceChild("bone128", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone129 = bone126.addOrReplaceChild("bone129", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone130 = bone126.addOrReplaceChild("bone130", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone131 = bone126.addOrReplaceChild("bone131", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone132 = bone126.addOrReplaceChild("bone132", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone133 = bone126.addOrReplaceChild("bone133", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone134 = bone126.addOrReplaceChild("bone134", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone135 = bone116.addOrReplaceChild("bone135", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 0.0F));

        PartDefinition bone136 = bone135.addOrReplaceChild("bone136", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -23.0F));

        PartDefinition bone137 = bone136.addOrReplaceChild("bone137", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone138 = bone136.addOrReplaceChild("bone138", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone139 = bone136.addOrReplaceChild("bone139", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone140 = bone136.addOrReplaceChild("bone140", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone141 = bone136.addOrReplaceChild("bone141", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone142 = bone136.addOrReplaceChild("bone142", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone143 = bone136.addOrReplaceChild("bone143", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone144 = bone136.addOrReplaceChild("bone144", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone145 = bone135.addOrReplaceChild("bone145", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 3.0F));

        PartDefinition bone146 = bone145.addOrReplaceChild("bone146", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone147 = bone145.addOrReplaceChild("bone147", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone148 = bone145.addOrReplaceChild("bone148", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone149 = bone145.addOrReplaceChild("bone149", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone150 = bone145.addOrReplaceChild("bone150", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone151 = bone145.addOrReplaceChild("bone151", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone152 = bone145.addOrReplaceChild("bone152", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone153 = bone145.addOrReplaceChild("bone153", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone205 = plate3.addOrReplaceChild("bone205", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -31.0F));

        PartDefinition bone79 = bone205.addOrReplaceChild("bone79", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -23.0F));

        PartDefinition bone80 = bone79.addOrReplaceChild("bone80", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone81 = bone79.addOrReplaceChild("bone81", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone82 = bone79.addOrReplaceChild("bone82", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone83 = bone79.addOrReplaceChild("bone83", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone84 = bone79.addOrReplaceChild("bone84", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone85 = bone79.addOrReplaceChild("bone85", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone86 = bone79.addOrReplaceChild("bone86", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone87 = bone79.addOrReplaceChild("bone87", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone88 = bone205.addOrReplaceChild("bone88", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 3.0F));

        PartDefinition bone89 = bone88.addOrReplaceChild("bone89", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone90 = bone88.addOrReplaceChild("bone90", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone91 = bone88.addOrReplaceChild("bone91", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone92 = bone88.addOrReplaceChild("bone92", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone93 = bone88.addOrReplaceChild("bone93", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone94 = bone88.addOrReplaceChild("bone94", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone95 = bone88.addOrReplaceChild("bone95", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone96 = bone88.addOrReplaceChild("bone96", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition plate4 = plates.addOrReplaceChild("plate4", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, 0.0F, 26.0F, 0.0F, 1.0036F, 0.0F));

        PartDefinition bone155 = plate4.addOrReplaceChild("bone155", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -54.0F));

        PartDefinition bone156 = bone155.addOrReplaceChild("bone156", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone157 = bone155.addOrReplaceChild("bone157", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone158 = bone155.addOrReplaceChild("bone158", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone159 = bone155.addOrReplaceChild("bone159", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone160 = bone155.addOrReplaceChild("bone160", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone161 = bone155.addOrReplaceChild("bone161", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone162 = bone155.addOrReplaceChild("bone162", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone163 = bone155.addOrReplaceChild("bone163", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone164 = plate4.addOrReplaceChild("bone164", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -28.0F));

        PartDefinition bone165 = bone164.addOrReplaceChild("bone165", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone166 = bone164.addOrReplaceChild("bone166", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone167 = bone164.addOrReplaceChild("bone167", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone168 = bone164.addOrReplaceChild("bone168", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone169 = bone164.addOrReplaceChild("bone169", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone170 = bone164.addOrReplaceChild("bone170", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone171 = bone164.addOrReplaceChild("bone171", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone172 = bone164.addOrReplaceChild("bone172", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone194 = plate4.addOrReplaceChild("bone194", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.1F, -1.1F, 4.7F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone193 = bone194.addOrReplaceChild("bone193", CubeListBuilder.create().texOffs(25, 172).mirror().addBox(-0.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(-0.4F)).mirror(false), PartPose.offsetAndRotation(-0.15F, 0.0F, 0.0F, 0.0F, -0.0175F, 0.0F));

        PartDefinition bone192 = bone194.addOrReplaceChild("bone192", CubeListBuilder.create().texOffs(25, 172).addBox(-0.425F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.15F, 0.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        PartDefinition plate5 = plates.addOrReplaceChild("plate5", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 0.0F, -25.0F, 0.0F, 1.309F, 0.0F));

        PartDefinition bone16 = plate5.addOrReplaceChild("bone16", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -54.0F));

        PartDefinition bone17 = bone16.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone18 = bone16.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone19 = bone16.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone20 = bone16.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone21 = bone16.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone22 = bone16.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone23 = bone16.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone24 = bone16.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(3, 7).addBox(-1.5F, -0.5F, 2.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone25 = plate5.addOrReplaceChild("bone25", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, -28.0F));

        PartDefinition bone26 = bone25.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone27 = bone25.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone28 = bone25.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone29 = bone25.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone30 = bone25.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone31 = bone25.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone32 = bone25.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone33 = bone25.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(2, 1).addBox(-1.0F, -0.5F, -0.4858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone34 = plate5.addOrReplaceChild("bone34", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.1F, -1.1F, 4.7F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone35 = bone34.addOrReplaceChild("bone35", CubeListBuilder.create().texOffs(25, 172).mirror().addBox(-0.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(-0.4F)).mirror(false), PartPose.offsetAndRotation(-0.15F, 0.0F, 0.0F, 0.0F, -0.0175F, 0.0F));

        PartDefinition bone36 = bone34.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(25, 172).addBox(-0.425F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.15F, 0.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        PartDefinition plate6 = plates.addOrReplaceChild("plate6", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 0.0F, -26.0F, 0.0F, -2.4435F, 0.0F));

        PartDefinition bone207 = plate6.addOrReplaceChild("bone207", CubeListBuilder.create(), PartPose.offset(13.0F, -0.75F, -8.0F));

        PartDefinition bone208 = bone207.addOrReplaceChild("bone208", CubeListBuilder.create().texOffs(3, 7).addBox(-14.5F, -0.5F, -43.3787F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 54.0F));

        PartDefinition bone209 = bone207.addOrReplaceChild("bone209", CubeListBuilder.create().texOffs(3, 7).addBox(-43.2193F, -0.5F, -20.7132F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone210 = bone207.addOrReplaceChild("bone210", CubeListBuilder.create().texOffs(3, 7).addBox(-47.5F, -0.5F, 15.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone211 = bone207.addOrReplaceChild("bone211", CubeListBuilder.create().texOffs(3, 7).addBox(-24.8345F, -0.5F, 44.3406F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone212 = bone207.addOrReplaceChild("bone212", CubeListBuilder.create().texOffs(3, 7).addBox(11.5F, -0.5F, 48.6213F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone213 = bone207.addOrReplaceChild("bone213", CubeListBuilder.create().texOffs(3, 7).addBox(40.2193F, -0.5F, 25.9558F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone214 = bone207.addOrReplaceChild("bone214", CubeListBuilder.create().texOffs(3, 7).addBox(44.5F, -0.5F, -10.3787F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone215 = bone207.addOrReplaceChild("bone215", CubeListBuilder.create().texOffs(3, 7).addBox(21.8345F, -0.5F, -39.098F, 3.0F, 0.5F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 54.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone216 = plate6.addOrReplaceChild("bone216", CubeListBuilder.create(), PartPose.offset(13.0F, -0.75F, 18.0F));

        PartDefinition bone217 = bone216.addOrReplaceChild("bone217", CubeListBuilder.create().texOffs(2, 1).addBox(-14.0F, -0.5F, -46.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.5F, 28.0F));

        PartDefinition bone218 = bone216.addOrReplaceChild("bone218", CubeListBuilder.create().texOffs(2, 1).addBox(-42.7193F, -0.5F, -23.8203F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone219 = bone216.addOrReplaceChild("bone219", CubeListBuilder.create().texOffs(2, 1).addBox(-47.0F, -0.5F, 12.6142F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone220 = bone216.addOrReplaceChild("bone220", CubeListBuilder.create().texOffs(2, 1).addBox(-24.3345F, -0.5F, 41.2335F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition bone221 = bone216.addOrReplaceChild("bone221", CubeListBuilder.create().texOffs(2, 1).addBox(12.0F, -0.5F, 45.6142F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone222 = bone216.addOrReplaceChild("bone222", CubeListBuilder.create().texOffs(2, 1).addBox(40.7193F, -0.5F, 22.8487F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -3.927F, 0.0F));

        PartDefinition bone223 = bone216.addOrReplaceChild("bone223", CubeListBuilder.create().texOffs(2, 1).addBox(45.0F, -0.5F, -13.3858F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone224 = bone216.addOrReplaceChild("bone224", CubeListBuilder.create().texOffs(2, 1).addBox(22.3345F, -0.5F, -42.2051F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 28.0F, 0.0F, -5.4978F, 0.0F));

        PartDefinition bone225 = plate6.addOrReplaceChild("bone225", CubeListBuilder.create().texOffs(33, 230).addBox(-13.5F, 10.6784F, -45.6086F, 1.0F, 0.9F, 2.5F, new CubeDeformation(-0.275F)).texOffs(5, 1).addBox(-13.5F, 10.6784F, -46.0086F, 1.0F, 0.9F, 1.7F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(12.0F, -0.75F, 50.9337F, -0.2443F, 0.0F, 0.0F));

        PartDefinition bone226 = bone225.addOrReplaceChild("bone226", CubeListBuilder.create().texOffs(5, 1).addBox(-13.5F, 37.9163F, -25.9084F, 1.0F, 0.9F, 0.9F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.975F, -0.7418F, 0.0F, 0.0F));

        PartDefinition bone227 = bone226.addOrReplaceChild("bone227", CubeListBuilder.create().texOffs(5, 1).addBox(-13.5F, 45.1137F, -7.4572F, 1.0F, 0.9F, 0.9F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition bone228 = bone227.addOrReplaceChild("bone228", CubeListBuilder.create().texOffs(5, 1).addBox(-13.5F, 34.2257F, -30.5551F, 1.0F, 0.9F, 0.9F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.1811F, -0.3787F, 0.5672F, 0.0F, 0.0F));

        PartDefinition bone229 = bone227.addOrReplaceChild("bone229", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1811F, -0.3787F));

        PartDefinition bone230 = bone229.addOrReplaceChild("bone230", CubeListBuilder.create().texOffs(5, 1).addBox(-13.5F, 4.7471F, -46.1164F, 1.0F, 0.9F, 1.1F, new CubeDeformation(-0.35F)).texOffs(5, 1).addBox(-13.45F, 4.7471F, -47.1164F, 0.9F, 0.9F, 1.7F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.2503F, -0.0789F, 1.309F, 0.0F, 0.0F));

        PartDefinition bone231 = bone229.addOrReplaceChild("bone231", CubeListBuilder.create().texOffs(5, 1).addBox(-41.7731F, 4.8925F, -23.8005F, 1.0F, 0.9F, 1.2F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0F, 0.2702F, -0.2348F, 1.3835F, -0.1841F, -0.7681F));

        PartDefinition bone232 = bone231.addOrReplaceChild("bone232", CubeListBuilder.create().texOffs(5, 1).addBox(-7.3044F, 4.8925F, -48.0212F, 0.9F, 0.9F, 1.7F, new CubeDeformation(-0.35F)), PartPose.offsetAndRotation(0.0155F, 0.0F, -0.3974F, 0.0F, 0.9163F, 0.0F));

        PartDefinition bone233 = bone229.addOrReplaceChild("bone233", CubeListBuilder.create().texOffs(5, 1).mirror().addBox(22.3883F, 4.8925F, -42.1852F, 1.0F, 0.9F, 1.2F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.2702F, -0.2348F, 1.3835F, 0.1841F, 0.7681F));

        PartDefinition bone234 = bone233.addOrReplaceChild("bone234", CubeListBuilder.create().texOffs(5, 1).mirror().addBox(-19.3732F, 4.8925F, -44.6276F, 0.9F, 0.9F, 1.7F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offsetAndRotation(-0.0155F, 0.0F, -0.3974F, 0.0F, -0.9163F, 0.0F));

        PartDefinition sit = partdefinition.addOrReplaceChild("sit", CubeListBuilder.create().texOffs(94, 177).addBox(20.0F, -2.0F, -28.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(94, 177).mirror().addBox(-28.0F, -2.0F, -28.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(94, 177).addBox(20.0F, -2.0F, 20.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(94, 177).mirror().addBox(-28.0F, -2.0F, 20.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition hide = partdefinition.addOrReplaceChild("hide", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition breadHide = hide.addOrReplaceChild("breadHide", CubeListBuilder.create(), PartPose.offsetAndRotation(25.0F, -0.625F, 1.8594F, 0.0F, -0.5672F, 0.0F));

        PartDefinition bone51 = breadHide.addOrReplaceChild("bone51", CubeListBuilder.create().texOffs(72, 170).addBox(-2.5833F, -3.1667F, -2.375F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4167F, -0.7083F, 4.9531F, 0.0F, -0.1745F, 0.0F));

        PartDefinition bone52 = bone51.addOrReplaceChild("bone52", CubeListBuilder.create().texOffs(76, 179).addBox(-2.116F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4167F, -0.6667F, 0.125F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone53 = bone51.addOrReplaceChild("bone53", CubeListBuilder.create().texOffs(78, 186).addBox(-0.9679F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4167F, -0.6667F, 0.625F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone54 = bone51.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(76, 179).addBox(-0.884F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5833F, -0.6667F, 0.125F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone55 = bone51.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(78, 186).addBox(-1.032F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5833F, -0.6667F, 0.625F, 0.0F, 1.0472F, 0.0F));

        PartDefinition bone44 = breadHide.addOrReplaceChild("bone44", CubeListBuilder.create().texOffs(72, 170).addBox(9.1667F, -3.1667F, -20.375F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.1667F, -0.7083F, 17.9531F));

        PartDefinition bone47 = bone44.addOrReplaceChild("bone47", CubeListBuilder.create().texOffs(76, 179).addBox(-2.116F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.1667F, -0.6667F, -17.875F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone48 = bone44.addOrReplaceChild("bone48", CubeListBuilder.create().texOffs(78, 186).addBox(-0.9679F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.1667F, -0.6667F, -17.375F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone49 = bone44.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(76, 179).addBox(-0.884F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1667F, -0.6667F, -17.875F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone50 = bone44.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(78, 186).addBox(-1.032F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.1667F, -0.6667F, -17.375F, 0.0F, 1.0472F, 0.0F));

        PartDefinition bone41 = breadHide.addOrReplaceChild("bone41", CubeListBuilder.create().texOffs(72, 170).addBox(-2.5833F, -3.1667F, -1.625F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4167F, -0.7083F, -5.7969F, 0.0F, 0.1745F, 0.0F));

        PartDefinition bone42 = bone41.addOrReplaceChild("bone42", CubeListBuilder.create().texOffs(76, 179).addBox(-2.116F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4167F, -0.6667F, 0.875F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone43 = bone41.addOrReplaceChild("bone43", CubeListBuilder.create().texOffs(78, 186).addBox(-0.968F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4167F, -0.6667F, 1.375F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone45 = bone41.addOrReplaceChild("bone45", CubeListBuilder.create().texOffs(76, 179).addBox(-0.884F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5833F, -0.6667F, 0.875F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone46 = bone41.addOrReplaceChild("bone46", CubeListBuilder.create().texOffs(78, 186).addBox(-1.032F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5833F, -0.6667F, 1.375F, 0.0F, 1.0472F, 0.0F));

        PartDefinition cakeHide = hide.addOrReplaceChild("cakeHide", CubeListBuilder.create().texOffs(24, 190).addBox(-4.4456F, -6.5F, -4.05F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(24, 206).addBox(0.0294F, -6.5F, -4.05F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 190).addBox(-2.1956F, -6.5F, -4.05F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.0F, 0.1F, 1.0F, 0.0F, 0.6109F, 0.0F));

        PartDefinition bone57 = cakeHide.addOrReplaceChild("bone57", CubeListBuilder.create().texOffs(0, 206).addBox(1.8457F, -6.5F, -9.6922F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 5.5F, 0.0F, -0.1745F, 0.0F));

        PartDefinition basketHide = hide.addOrReplaceChild("basketHide", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.9F, 1.75F, 0.0F, -2.6616F, 0.0F));

        PartDefinition bone2 = basketHide.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.6426F, -1.0F, -1.4094F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone = bone2.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(177, 65).addBox(-7.0F, -4.75F, -10.5F, 1.0F, 4.0F, 11.0F, new CubeDeformation(-0.25F)).texOffs(177, 32).addBox(-12.875F, -4.0F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(186, 19).addBox(-9.875F, -6.0F, -8.25F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.1F)).texOffs(198, 19).addBox(-12.875F, -6.0F, -8.45F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.1F)).texOffs(134, 0).addBox(-14.875F, -6.5F, -10.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(134, 0).addBox(-9.875F, -6.5F, -10.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(160, 9).addBox(-16.875F, -6.4F, -10.4F, 10.0F, 4.0F, 2.0F, new CubeDeformation(-0.4F)).texOffs(177, 42).addBox(-12.875F, -7.125F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.0F, 5.0F));

        PartDefinition bone7 = bone.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(168, 19).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(139, 17).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, -5.0F, -2.0F));

        PartDefinition bone9 = bone.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(139, 17).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(151, 19).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, -5.0F, -6.25F));

        PartDefinition bone15 = bone.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(160, 32).addBox(-6.125F, -6.0F, -6.625F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -1.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -3.25F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -5.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -2.125F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -4.375F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone37 = bone15.addOrReplaceChild("bone37", CubeListBuilder.create().texOffs(160, 32).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.625F, -3.5F, -7.55F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bone38 = bone15.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(160, 32).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.625F, -3.5F, -8.875F, -0.2967F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        hide.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    public ModelPart getHideModel() {
        return hide;
    }
}
