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
import net.minecraft.world.entity.monster.Slime;

public class EntityYukkuriModel extends AbstractModel<Slime> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "yukkuri");
    private final ModelPart bone;

    public EntityYukkuriModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(38, 0).mirror().addBox(3.0F, -3.75F, -5.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.35F)).mirror(false)
                .texOffs(38, 0).addBox(-5.0F, -3.75F, -5.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.35F))
                .texOffs(0, 0).addBox(-1.5F, -11.0F, -2.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(48, 14).addBox(-0.8195F, -2.0F, 0.1061F, 7.0F, 4.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -6.5F, 4.0F, 0.0F, -0.1745F, -0.4363F));
        PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(48, 14).mirror().addBox(-6.1805F, -2.0F, 0.1061F, 7.0F, 4.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, -6.5F, 4.0F, 0.0F, 0.1745F, 0.4363F));
        PartDefinition bone4 = bone.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(50, 20).addBox(-0.3195F, -2.0F, 0.1061F, 6.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -6.5F, 4.0F, 0.0F, -0.1745F, 2.7053F));
        PartDefinition bone5 = bone.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(50, 20).mirror().addBox(-5.6805F, -2.0F, 0.1061F, 6.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(0.0F, -6.5F, 4.0F, 0.0F, 0.1745F, -2.7053F));
        PartDefinition bone6 = bone.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(48, 0).addBox(0.1F, -3.5F, -3.3F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, 1.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition bone7 = bone.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-2.1F, -3.5F, -3.3F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -3.0F, 1.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition bone8 = bone.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(54, 35).addBox(-2.0F, -0.6042F, -1.0909F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 6.0F, 0.1745F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Slime slime, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}