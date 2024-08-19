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

public class PicnicBasketModel extends AbstractModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "picnic_basket");
    private final ModelPart all;

    public PicnicBasketModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 1.0F));

        PartDefinition upperCover = all.addOrReplaceChild("upperCover", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -3.0F, -12.0F, 18.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(46, 36).addBox(-8.0F, -2.0F, -12.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 17).addBox(-8.0F, -2.0F, -1.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 43).addBox(8.0F, -2.0F, -12.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(49, 2).addBox(-9.0F, -2.0F, -12.0F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 5.0F));

        PartDefinition bone7 = upperCover.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, -0.8F, -2.6F, -0.6109F, 0.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -1.0F));

        PartDefinition box = body.addOrReplaceChild("box", CubeListBuilder.create().texOffs(46, 28).addBox(-8.0F, -7.0F, -6.0F, 16.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 47).addBox(-8.0F, -7.0F, 5.0F, 16.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(31, 28).addBox(8.0F, -7.0F, -6.0F, 1.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 44).addBox(-9.0F, -7.0F, -6.0F, 1.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 14).addBox(-9.0F, -1.0F, -6.0F, 18.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightHandle = all.addOrReplaceChild("rightHandle", CubeListBuilder.create().texOffs(49, 21).addBox(-0.5F, -0.5F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(17, 36).addBox(-0.5F, -0.5F, -7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 28).addBox(7.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3F, -6.5F, -1.0F, 0.0F, 0.0F, -1.9199F));

        PartDefinition leftHandle = all.addOrReplaceChild("leftHandle", CubeListBuilder.create().texOffs(49, 21).mirror().addBox(-7.5F, -0.5F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(17, 36).mirror().addBox(-7.5F, -0.5F, -7.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 28).addBox(-8.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3F, -6.5F, -1.0F, 0.0F, 0.0F, 1.9199F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay,color);
    }
}
