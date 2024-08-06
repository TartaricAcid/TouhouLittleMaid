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
import net.minecraft.world.entity.monster.MagmaCube;

public class EntityMarisaYukkuriModel extends AbstractModel<MagmaCube> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "marisa_yukkuri");
    private final ModelPart bone;

    public EntityMarisaYukkuriModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone8 = bone.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(46, 14).addBox(-4.0F, -1.5F, -0.5F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 5.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition bone9 = bone8.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(46, 19).addBox(-4.0F, -1.5026F, -0.4026F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.6F, 0.6F, 0.5236F, 0.0F, 0.0F));

        PartDefinition bone1 = bone.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(48, 0).addBox(0.1F, -3.5F, -3.3F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -3.0F, 1.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-2.1F, -3.5F, -3.3F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -3.0F, 1.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition bone3 = bone.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 49).addBox(-7.0F, -0.5F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(8, 51).addBox(-5.0F, -3.5F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 35).addBox(-5.5F, -1.5F, -5.5F, 11.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(0, 35).addBox(-5.5F, -0.1F, -5.5F, 11.0F, 1.0F, 11.0F, new CubeDeformation(-0.3F)).texOffs(34, 0).addBox(4.5F, -2.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, -0.2175F, 0.0074F, -0.0164F));

        PartDefinition bone10 = bone3.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.1F, -1.5F, 1.5F, 0.3927F, 0.3927F, 0.0F));

        PartDefinition bone11 = bone3.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.1F, -1.5F, -1.5F, -2.6268F, 0.5336F, -2.8956F));

        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(36, 31).addBox(-3.5F, -2.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.4F, 1.8F, -0.829F, 0.0F, 0.0F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(44, 50).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.6F, 2.0F, -0.829F, 0.0F, 0.0F));

        PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 54).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2926F, 0.6986F, -0.6981F, 0.0F, 0.0F));

        PartDefinition bone7 = bone6.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(2, 48).addBox(-1.0F, -1.2F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -0.6F, 0.7418F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(MagmaCube slime, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
