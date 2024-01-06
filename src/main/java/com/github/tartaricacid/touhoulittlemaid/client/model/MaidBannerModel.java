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

public class MaidBannerModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "maid_banner");
    private final ModelPart main;
    private final ModelPart banner;

    public MaidBannerModel(ModelPart root) {
        this.main = root.getChild("main");
        this.banner = root.getChild("banner");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(34, 49).addBox(-1.0F, -68.0F, 10.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.05F))
                .texOffs(34, 49).addBox(-1.0F, -68.0F, -3.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.05F))
                .texOffs(56, 0).addBox(-1.0F, -35.0F, -1.0F, 2.0F, 35.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-1.0F, -70.0F, -1.0F, 2.0F, 35.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition banner = partdefinition.addOrReplaceChild("banner", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -66.0F, -1.0F, 20.0F, 40.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelPart getBanner() {
        return banner;
    }
}
