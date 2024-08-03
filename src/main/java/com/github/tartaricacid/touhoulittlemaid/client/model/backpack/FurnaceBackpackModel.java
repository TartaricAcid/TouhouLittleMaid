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

public class FurnaceBackpackModel extends EntityModel<EntityMaid> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "furnace_backpack");
    private final ModelPart main;

    public FurnaceBackpackModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 33.5F, 1.0F));

        PartDefinition bone466 = main.addOrReplaceChild("bone466", CubeListBuilder.create().texOffs(0, 80).addBox(-5.0F, -25.0F, -2.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(75, 65).addBox(4.0F, -25.0F, -2.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(68, 41).addBox(-6.0F, -22.0F, -2.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone36 = bone466.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(68, 35).addBox(-6.0F, -8.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(68, 32).addBox(-6.0F, -6.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(72, 0).addBox(-5.0F, -10.5F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(70, 65).addBox(4.0F, -10.5F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -28.5F, -1.5F));

        PartDefinition bone10 = bone36.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(68, 47).addBox(-5.5F, -6.9672F, 12.3212F, 11.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(35, 33).addBox(-5.0F, -6.9672F, -16.1788F, 1.0F, 1.0F, 30.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 0).addBox(-4.5F, -6.9672F, -15.1788F, 9.0F, 1.0F, 28.0F, new CubeDeformation(-0.3F))
                .texOffs(49, 0).addBox(4.0F, -6.9672F, -16.1788F, 1.0F, 1.0F, 30.0F, new CubeDeformation(-0.1F))
                .texOffs(68, 50).addBox(-5.5F, -6.9672F, -15.6788F, 11.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition bone26 = bone466.addOrReplaceChild("bone26", CubeListBuilder.create(), PartPose.offset(-4.0F, -19.1517F, -3.8731F));

        PartDefinition bone6 = bone26.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(86, 15).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone8 = bone26.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(89, 18).addBox(0.0F, -1.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone7 = bone26.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(85, 14).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.5F, 5.5F, 2.0F, -0.2443F, 0.0F, 0.0F));

        PartDefinition bone9 = bone466.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offset(4.0F, -19.1517F, -3.8731F));

        PartDefinition bone11 = bone9.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(86, 15).mirror().addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.025F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone12 = bone9.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(89, 18).mirror().addBox(-1.0F, -1.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone13 = bone9.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(85, 14).mirror().addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.025F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 5.5F, 2.0F, -0.2443F, 0.0F, 0.0F));

        PartDefinition bone = bone466.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(88, 15).addBox(6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(88, 15).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(-3.5F, -19.5F, -1.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone2 = bone466.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(88, 15).addBox(6.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(88, 15).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(-3.5F, -13.5F, -1.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone3 = bone466.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(3, 68).addBox(-5.5F, -2.3361F, -2.487F, 11.0F, 1.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(68, 44).addBox(-6.0F, -1.6861F, -3.237F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(68, 38).addBox(-6.0F, -1.6861F, 5.263F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(37, 68).addBox(-5.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, new CubeDeformation(-0.1F))
                .texOffs(55, 70).addBox(4.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -12.3139F, 1.237F));

        PartDefinition bone467 = bone3.addOrReplaceChild("bone467", CubeListBuilder.create().texOffs(0, 30).addBox(4.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 14).addBox(-5.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bone4 = bone466.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(3, 68).addBox(-5.5F, -2.3361F, -2.487F, 11.0F, 1.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(68, 44).addBox(-6.0F, -1.6861F, -3.237F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(68, 38).addBox(-6.0F, -1.6861F, 5.263F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(37, 68).addBox(-5.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, new CubeDeformation(-0.1F))
                .texOffs(55, 70).addBox(4.0F, -1.6861F, -3.737F, 1.0F, 1.0F, 12.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 30).addBox(-8.0F, -14.0861F, -5.737F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-4.0F)), PartPose.offset(0.0F, -18.3139F, 1.237F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 30).addBox(4.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.2F))
                .texOffs(12, 14).addBox(-5.0F, -0.5F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition bench = main.addOrReplaceChild("bench", CubeListBuilder.create().texOffs(0, 96).addBox(-10.0F, -8.0F, -10.05F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-6.0F))
                .texOffs(64, 96).addBox(-6.0F, -8.0F, -10.05F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-6.0F)), PartPose.offsetAndRotation(0.0F, -16.4F, 3.75F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bench2 = bench.addOrReplaceChild("bench2", CubeListBuilder.create().texOffs(0, 96).addBox(-10.0F, -8.0F, -8.05F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-6.0F))
                .texOffs(64, 96).addBox(-6.0F, -8.0F, -8.05F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-6.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -3.1416F, 0.0F, 3.1416F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityMaid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
