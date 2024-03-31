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

public class PicnicMatModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "picnic_mat");
    private final ModelPart all;

    public PicnicMatModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone = all.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(94, 96).addBox(-0.25F, -40.0F, -40.0F, 1.0F, 80.0F, 80.0F, new CubeDeformation(-0.25F)).texOffs(0, 0).addBox(-0.25F, -40.0F, -40.0F, 1.0F, 80.0F, 80.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition bone2 = all.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(-16.0F, -1.0F, 22.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone4 = bone2.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(196, 74).addBox(-9.0F, -3.0F, -12.0F, 18.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(186, 0).addBox(-8.0F, -2.0F, -12.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(186, 13).addBox(-8.0F, -2.0F, -1.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(202, 21).addBox(8.0F, -2.0F, -12.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(202, 40).addBox(-9.0F, -2.0F, -12.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 6.0F, -1.8326F, 0.0F, 0.0F));

        PartDefinition bone7 = bone4.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(222, 19).addBox(8.75F, 0.2465F, -0.3823F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(222, 19).addBox(-7.75F, 0.2465F, -0.3823F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.8F, -2.6F, -0.6109F, 0.0F, 0.0F));

        PartDefinition bone8 = bone4.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(160, 0).mirror().addBox(1.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(160, 0).mirror().addBox(-7.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(142, 0).addBox(-7.5F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(142, 0).addBox(0.5F, -1.0F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(142, 5).addBox(3.5F, -1.0F, -3.5F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(142, 5).addBox(-4.5F, -1.0F, -3.5F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.95F, -6.0F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(196, 58).addBox(-9.0F, -1.0F, -6.0F, 18.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(230, 38).addBox(-9.0F, -7.0F, -6.0F, 1.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(230, 18).addBox(8.0F, -7.0F, -6.0F, 1.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(222, 9).addBox(-8.0F, -7.0F, 5.0F, 16.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(222, 0).addBox(-8.0F, -7.0F, -6.0F, 16.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone9 = bone3.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(177, 65).addBox(-7.0F, -4.75F, -10.5F, 1.0F, 4.0F, 11.0F, new CubeDeformation(-0.25F)).texOffs(177, 32).addBox(-12.875F, -4.0F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(186, 19).addBox(-9.875F, -6.0F, -8.25F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.1F)).texOffs(198, 19).addBox(-12.875F, -6.0F, -8.45F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.1F)).texOffs(134, 0).addBox(-14.875F, -6.5F, -10.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(134, 0).addBox(-9.875F, -6.5F, -10.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(160, 9).addBox(-16.875F, -6.4F, -10.4F, 10.0F, 4.0F, 2.0F, new CubeDeformation(-0.4F)).texOffs(177, 42).addBox(-12.875F, -7.125F, -5.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.0F, 5.0F));

        PartDefinition bone11 = bone9.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(168, 19).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(139, 17).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, -5.0F, -2.0F));

        PartDefinition bone10 = bone9.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(139, 17).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(151, 19).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, -5.0F, -6.25F));

        PartDefinition bone14 = bone9.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(160, 32).addBox(-6.125F, -6.0F, -6.625F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -1.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -3.25F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -5.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -2.125F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(160, 32).addBox(-6.125F, -6.0F, -4.375F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone13 = bone14.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(160, 32).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.625F, -3.5F, -7.55F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bone12 = bone14.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(160, 32).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.625F, -3.5F, -8.875F, -0.2967F, 0.0F, 0.0F));

        PartDefinition bone5 = bone2.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(157, 62).addBox(-0.5F, -0.5F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(157, 58).addBox(-0.5F, -0.5F, -7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(156, 40).addBox(7.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3F, -5.5F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition bone6 = bone2.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(157, 62).mirror().addBox(-7.5F, -0.5F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(157, 58).mirror().addBox(-7.5F, -0.5F, -7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(156, 40).addBox(-8.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3F, -5.5F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition bone15 = all.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(228, 94).addBox(-3.5F, -5.2F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-21.0F, -6.05F, 10.7F, 1.0472F, -0.8727F, -1.1694F));

        PartDefinition bone16 = bone15.addOrReplaceChild("bone16", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone17 = bone16.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(244, 119).addBox(-2.5F, -4.6578F, -1.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone18 = bone17.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(250, 127).addBox(-2.0F, -5.0F, -1.001F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone19 = bone17.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(242, 127).addBox(0.0F, -5.0F, -0.999F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone20 = bone15.addOrReplaceChild("bone20", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone21 = bone20.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(244, 119).addBox(-2.5F, -4.6578F, -1.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone22 = bone21.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(250, 127).addBox(-2.0F, -5.0F, -1.001F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone23 = bone21.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(242, 127).addBox(0.0F, -5.0F, -0.999F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone24 = bone15.addOrReplaceChild("bone24", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone25 = bone24.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(244, 119).addBox(-2.5F, -4.6578F, -1.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone26 = bone25.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(250, 127).addBox(-2.0F, -5.0F, -1.001F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone27 = bone25.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(242, 127).addBox(0.0F, -5.0F, -0.999F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone28 = bone15.addOrReplaceChild("bone28", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone29 = bone28.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(244, 119).addBox(-2.5F, -4.6578F, -1.0F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone30 = bone29.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(250, 127).addBox(-2.0F, -5.0F, -1.001F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone31 = bone29.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(242, 127).addBox(0.0F, -5.0F, -0.999F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone36 = bone15.addOrReplaceChild("bone36", CubeListBuilder.create(), PartPose.offset(0.0F, 0.9F, 0.0F));

        PartDefinition bone32 = bone36.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(226, 104).addBox(-7.5F, -4.25F, -3.5F, 4.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.25F, 0.0F));

        PartDefinition bone33 = bone36.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(226, 104).addBox(-7.5F, -4.25F, -3.5F, 4.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.25F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone34 = bone36.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(226, 104).addBox(-7.5F, -4.25F, -3.5F, 4.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.25F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bone35 = bone36.addOrReplaceChild("bone35", CubeListBuilder.create().texOffs(226, 104).addBox(-7.5F, -4.25F, -3.5F, 4.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.25F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bone38 = all.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(21, 247).addBox(-1.6667F, -0.75F, -1.6667F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(-2.1667F, -1.25F, -2.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(-0.1667F, -4.25F, -0.1667F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(30.1667F, -5.75F, -0.8333F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone58 = all.addOrReplaceChild("bone58", CubeListBuilder.create().texOffs(50, 247).addBox(-1.6667F, -0.75F, -1.6667F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(-2.1667F, -1.25F, -2.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(-0.1667F, -4.25F, -0.1667F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-21.8333F, -5.75F, -10.8333F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone39 = all.addOrReplaceChild("bone39", CubeListBuilder.create().texOffs(35, 247).addBox(7.3333F, -0.75F, 0.3333F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 251).addBox(6.8333F, -1.25F, -0.1667F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)).texOffs(0, 243).addBox(8.8333F, -4.25F, 1.8333F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(31.1667F, -5.75F, -2.8333F, 0.0F, -1.2217F, 0.0F));

        PartDefinition bone40 = all.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(0, 168).addBox(-8.0F, -0.875F, -8.9219F, 15.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, -1.125F, -27.1406F, 0.0F, 1.0472F, 0.0F));

        PartDefinition bone41 = bone40.addOrReplaceChild("bone41", CubeListBuilder.create().texOffs(72, 170).addBox(-2.5833F, -3.1667F, -1.625F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4167F, -0.7083F, -5.7969F, 0.0F, 0.1745F, 0.0F));

        PartDefinition bone42 = bone41.addOrReplaceChild("bone42", CubeListBuilder.create().texOffs(76, 179).addBox(-2.116F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4167F, -0.6667F, 0.875F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone43 = bone41.addOrReplaceChild("bone43", CubeListBuilder.create().texOffs(78, 186).addBox(-0.968F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4167F, -0.6667F, 1.375F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone45 = bone41.addOrReplaceChild("bone45", CubeListBuilder.create().texOffs(76, 179).addBox(-0.884F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5833F, -0.6667F, 0.875F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone46 = bone41.addOrReplaceChild("bone46", CubeListBuilder.create().texOffs(78, 186).addBox(-1.032F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5833F, -0.6667F, 1.375F, 0.0F, 1.0472F, 0.0F));

        PartDefinition bone44 = bone40.addOrReplaceChild("bone44", CubeListBuilder.create().texOffs(72, 170).addBox(9.1667F, -3.1667F, -20.375F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.1667F, -0.7083F, 17.9531F));

        PartDefinition bone47 = bone44.addOrReplaceChild("bone47", CubeListBuilder.create().texOffs(76, 179).addBox(-2.116F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.1667F, -0.6667F, -17.875F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone48 = bone44.addOrReplaceChild("bone48", CubeListBuilder.create().texOffs(78, 186).addBox(-0.9679F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.1667F, -0.6667F, -17.375F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone49 = bone44.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(76, 179).addBox(-0.884F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1667F, -0.6667F, -17.875F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone50 = bone44.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(78, 186).addBox(-1.032F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.1667F, -0.6667F, -17.375F, 0.0F, 1.0472F, 0.0F));

        PartDefinition bone51 = bone40.addOrReplaceChild("bone51", CubeListBuilder.create().texOffs(72, 170).addBox(-2.5833F, -3.1667F, -2.375F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4167F, -0.7083F, 4.9531F, 0.0F, -0.1745F, 0.0F));

        PartDefinition bone52 = bone51.addOrReplaceChild("bone52", CubeListBuilder.create().texOffs(76, 179).addBox(-2.116F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4167F, -0.6667F, 0.125F, 0.0F, -0.5236F, 0.0F));

        PartDefinition bone53 = bone51.addOrReplaceChild("bone53", CubeListBuilder.create().texOffs(78, 186).addBox(-0.9679F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4167F, -0.6667F, 0.625F, 0.0F, -1.0472F, 0.0F));

        PartDefinition bone54 = bone51.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(76, 179).addBox(-0.884F, -1.5F, -1.201F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5833F, -0.6667F, 0.125F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone55 = bone51.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(78, 186).addBox(-1.032F, -0.5F, 0.5981F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5833F, -0.6667F, 0.625F, 0.0F, 1.0472F, 0.0F));

        PartDefinition bone56 = all.addOrReplaceChild("bone56", CubeListBuilder.create().texOffs(0, 224).addBox(-7.0F, -1.5F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 190).addBox(-2.1956F, -6.5F, -4.05F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(24, 206).addBox(0.0294F, -6.5F, -4.05F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(24, 190).addBox(-4.4456F, -6.5F, -4.05F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(26.0F, -0.5F, -12.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition bone57 = bone56.addOrReplaceChild("bone57", CubeListBuilder.create().texOffs(0, 206).addBox(1.8457F, -6.5F, -9.6922F, 2.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 5.5F, 0.0F, -0.1745F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
