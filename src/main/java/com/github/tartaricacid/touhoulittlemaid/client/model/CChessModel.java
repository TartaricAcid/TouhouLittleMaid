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

public class CChessModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "cchess");
    private final ModelPart main;

    public CChessModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0F, -18.0F, -32.0F, 64.0F, 18.0F, 64.0F, new CubeDeformation(-8.0F)), PartPose.offset(0.0F, 32.0F, 0.0F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 82).addBox(-28.5F, -16.05F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(-14.0F))
                .texOffs(128, 82).addBox(-23.75F, -16.05F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(-14.0F)), PartPose.offset(-2.125F, -8.0F, 0.0F));

        PartDefinition bone2 = main.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 146).mirror().addBox(-3.5F, -16.05F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(-14.0F)).mirror(false)
                .texOffs(128, 146).mirror().addBox(-8.25F, -16.05F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(-14.0F)).mirror(false), PartPose.offset(-2.675F, -8.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
