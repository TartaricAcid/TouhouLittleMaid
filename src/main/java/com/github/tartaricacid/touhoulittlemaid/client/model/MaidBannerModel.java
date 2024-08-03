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

public class MaidBannerModel extends AbstractModel<Entity> {
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

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(34, 49).addBox(-1.0F, -68.0F, 10.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.1F))
                .texOffs(34, 49).addBox(-1.0F, -68.0F, -3.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.1F))
                .texOffs(56, 0).addBox(-1.0F, -35.0F, -1.0F, 2.0F, 35.0F, 2.0F, new CubeDeformation(0.05F))
                .texOffs(56, 0).addBox(-1.0F, -70.0F, -1.0F, 2.0F, 35.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -26.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-1.0F, -46.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-1.0F, -36.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-1.0F, -56.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = main.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -66.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-1.0F, -66.0F, 16.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition banner = partdefinition.addOrReplaceChild("banner", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -65.0F, -0.5F, 20.0F, 40.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    public ModelPart getBanner() {
        return banner;
    }
}
