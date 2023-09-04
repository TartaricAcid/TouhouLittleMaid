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

public class StatueBaseModel extends EntityModel<Entity> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "statue_base");
    private final ModelPart bone;

    public StatueBaseModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -2.0F, -19.0F, 48.0F, 2.0F, 48.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(-22.5F, -10.5F, -17.5F, 45.0F, 3.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -5.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, -0.1F, 5.0F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, -111.0F, 0.0F));

        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone6 = bone4.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone7 = bone2.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -111.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone8 = bone7.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone9 = bone8.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone10 = bone8.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone11 = bone2.addOrReplaceChild("bone11", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -111.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition bone12 = bone11.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone13 = bone12.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone14 = bone12.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone15 = bone2.addOrReplaceChild("bone15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -111.0F, 0.0F, 0.0F, -4.7124F, 0.0F));

        PartDefinition bone16 = bone15.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(0, 104).addBox(-22.5F, 103.8422F, 0.0F, 45.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone17 = bone16.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 105.0F, -0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone18 = bone16.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 105.0F, 0.001F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(0.325f, 0.325f, 0.325f);
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}