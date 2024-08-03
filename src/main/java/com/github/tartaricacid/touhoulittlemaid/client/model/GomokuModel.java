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

public class GomokuModel extends AbstractModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "gomoku");
    private final ModelPart main;
    private final ModelPart blackBox;
    private final ModelPart whiteBox;

    public GomokuModel(ModelPart root) {
        this.main = root.getChild("main");
        this.blackBox = root.getChild("blackBox");
        this.whiteBox = root.getChild("whiteBox");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-75.5F, -61.51F, -75.5F, 151.0F, 151.0F, 151.0F, new CubeDeformation(-59.5F))
                .texOffs(0, 0).addBox(-16.0F, -2.0F, -16.0F, 32.0F, 2.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition blackBox = partdefinition.addOrReplaceChild("blackBox", CubeListBuilder.create().texOffs(69, 11).mirror().addBox(17.0F, -1.0F, 8.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 99).mirror().addBox(17.5F, -2.95F, 8.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 99).mirror().addBox(18.25F, -3.85F, 8.9F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(12, 99).mirror().addBox(19.0F, -3.85F, 10.4F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(69, 11).mirror().addBox(17.0F, -4.0F, 8.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(69, 11).mirror().addBox(21.0F, -4.0F, 9.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(69, 11).mirror().addBox(17.0F, -4.0F, 12.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(69, 11).mirror().addBox(18.0F, -4.0F, 8.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone2 = blackBox.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(12, 99).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(18.5F, -4.15F, 9.9F, 0.0F, 0.0F, 0.3927F));

        PartDefinition whiteBox = partdefinition.addOrReplaceChild("whiteBox", CubeListBuilder.create().texOffs(69, 11).addBox(-22.0F, -1.0F, -13.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 96).addBox(-21.5F, -2.95F, -12.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 96).addBox(-20.25F, -3.85F, -10.9F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(44, 96).addBox(-21.0F, -3.85F, -12.4F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(69, 11).addBox(-18.0F, -4.0F, -12.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(69, 11).addBox(-22.0F, -4.0F, -13.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(69, 11).addBox(-21.0F, -4.0F, -13.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(69, 11).addBox(-22.0F, -4.0F, -9.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone4 = whiteBox.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(44, 96).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-18.5F, -4.15F, -9.9F, 0.0F, 0.0F, -0.3927F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        blackBox.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        whiteBox.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
