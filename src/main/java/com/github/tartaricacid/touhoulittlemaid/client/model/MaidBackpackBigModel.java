package com.github.tartaricacid.touhoulittlemaid.client.model;


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


public class MaidBackpackBigModel extends EntityModel<EntityMaid> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "backpack_big");
    private final ModelPart bone;

    public MaidBackpackBigModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(88, 45).mirror().addBox(-4.95F, -15.0F, 0.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 62).addBox(-5.95F, -14.0F, 3.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(110, 94).addBox(-7.0F, -4.5F, 2.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(110, 94).mirror().addBox(5.0F, -4.5F, 2.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 62).mirror().addBox(5.05F, -14.0F, 3.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(88, 15).addBox(-5.0F, -23.0F, 0.05F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(88, 30).mirror().addBox(-5.05F, -19.0F, 0.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(88, 0).addBox(-5.0F, -26.0F, 0.0F, 10.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(57, 0).mirror().addBox(-5.0F, -10.0F, 0.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).addBox(-3.5F, -4.0F, -5.25F, 7.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(31, 0).addBox(2.5F, -10.0F, -5.0F, 1.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).mirror().addBox(-3.5F, -10.0F, -5.0F, 1.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(76, 31).addBox(-2.0F, -9.5F, 10.0F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 32).addBox(-2.0F, -17.5F, 9.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(76, 39).addBox(-1.5F, -2.25F, 10.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(57, 15).addBox(-5.0F, -10.0F, 5.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 63).addBox(2.5F, -26.0F, 0.0F, 1.0F, 26.0F, 10.0F, new CubeDeformation(0.1F))
                .texOffs(0, 63).addBox(-3.5F, -26.0F, 0.0F, 1.0F, 26.0F, 10.0F, new CubeDeformation(0.1F))
                .texOffs(51, 121).addBox(-3.5F, -25.2F, 7.2F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(51, 121).mirror().addBox(2.5F, -25.2F, 7.2F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(23, 104).addBox(-5.0F, -23.0F, 7.5F, 10.0F, 23.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(0, 101).addBox(-5.0F, -26.0F, 1.5F, 10.0F, 26.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(88, 113).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityMaid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}