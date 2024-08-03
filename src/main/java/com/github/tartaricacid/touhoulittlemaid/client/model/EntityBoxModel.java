package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EntityBoxModel extends AbstractModel<EntityBox> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "box");
    private final ModelPart bottom;
    private final ModelPart x1;
    private final ModelPart x2;
    private final ModelPart z1;
    private final ModelPart z2;
    private final ModelPart top;

    public EntityBoxModel(ModelPart root) {
        this.bottom = root.getChild("bottom");
        this.x1 = root.getChild("x1");
        this.x2 = root.getChild("x2");
        this.z1 = root.getChild("z1");
        this.z2 = root.getChild("z2");
        this.top = root.getChild("top");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -1.0F, -15.0F, 30.0F, 1.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition x1 = partdefinition.addOrReplaceChild("x1", CubeListBuilder.create().texOffs(64, 31).addBox(-14.0F, -30.5F, -0.5F, 28.0F, 30.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.5F, -14.5F));

        PartDefinition x2 = partdefinition.addOrReplaceChild("x2", CubeListBuilder.create().texOffs(64, 31).addBox(-14.0F, -30.5F, -0.5F, 28.0F, 30.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.5F, 14.5F));

        PartDefinition z1 = partdefinition.addOrReplaceChild("z1", CubeListBuilder.create().texOffs(0, 31).addBox(-0.5F, -30.5F, -15.0F, 1.0F, 30.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(14.5F, 23.5F, 0.0F));

        PartDefinition z2 = partdefinition.addOrReplaceChild("z2", CubeListBuilder.create().texOffs(0, 31).addBox(-0.5F, -30.5F, -15.0F, 1.0F, 30.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.5F, 23.5F, 0.0F));

        PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -32.0F, -15.0F, 30.0F, 1.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(64, 64).addBox(-16.0F, -32.0F, -16.0F, 32.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 64).addBox(-16.0F, -32.0F, 15.0F, 32.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 61).addBox(-16.0F, -32.0F, -15.0F, 1.0F, 6.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(32, 61).addBox(15.0F, -32.0F, -15.0F, 1.0F, 6.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityBox entityIn = (EntityBox) entity;
        int stage = entityIn.getOpenStage();
        if (stage == EntityBox.FIRST_STAGE) {
            top.visible = true;
            x1.xRot = 0;
            x2.xRot = 0;
            z1.zRot = 0;
            z2.zRot = 0;
        } else if (stage > EntityBox.SECOND_STAGE) {
            top.visible = false;
            x1.xRot = 0;
            x2.xRot = 0;
            z1.zRot = 0;
            z2.zRot = 0;
        } else {
            top.visible = false;
            x1.xRot = 0.023998277f * (EntityBox.SECOND_STAGE - stage);
            x2.xRot = -0.023998277f * (EntityBox.SECOND_STAGE - stage);
            z1.zRot = 0.023998277f * (EntityBox.SECOND_STAGE - stage);
            z2.zRot = -0.023998277f * (EntityBox.SECOND_STAGE - stage);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        x1.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        x2.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        z1.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        z2.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        top.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}