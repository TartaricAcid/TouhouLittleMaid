package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BroomModel extends EntityModel<EntityBroom> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "broom");
    private final ModelPart all;

    public BroomModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 10.0F));

        PartDefinition middle = head.addOrReplaceChild("middle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone = middle.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.5F, -1.0F, 6.0F, 3.0F, 12.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left = head.addOrReplaceChild("left", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition right = head.addOrReplaceChild("right", CubeListBuilder.create().texOffs(37, 0).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition handle = all.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -1.0F, -28.0F, 2.0F, 2.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 9.0F));

        PartDefinition chest = all.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(71, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(71, 16).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -0.75F, 12.25F));

        PartDefinition bone6 = chest.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.5F, -9.5F, 0.0F));

        PartDefinition bone2 = bone6.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(104, 0).addBox(-0.5F, -3.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone4 = bone6.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(104, 2).addBox(-1.5F, -2.5F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone5 = bone6.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(104, 2).addBox(-1.5F, -2.5F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone3 = bone6.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(104, 0).mirror().addBox(-0.5F, -3.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(EntityBroom broom, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        all.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (broom.isVehicle()) {
            all.xRot = headPitch * ((float) Math.PI / 180F) / 10;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
