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

public class TombstoneModel extends EntityModel<Entity> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "tombstone");
    private final ModelPart main;

    public TombstoneModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 29).addBox(-6.0F, -2.0F, -3.0F, 11.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, 0.0F, -6.0F, 13.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 15).addBox(-6.0F, 1.0F, -5.0F, 11.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(34, 15).addBox(-4.0F, 4.3F, -3.0F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(33, 33).addBox(-4.5F, -12.0F, -1.5F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 48).addBox(-8.5F, -16.0F, -7.025F, 16.0F, 16.0F, 0.0F, new CubeDeformation(-5.0F))
                .texOffs(40, 0).addBox(-3.0F, -14.0F, -2.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 17.1F, -0.5F));

        PartDefinition bone8 = main.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offset(-0.5F, 4.5F, 0.5F));

        PartDefinition bone6 = bone8.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(38, 23).addBox(2.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bone7 = bone8.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(38, 23).addBox(-3.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bone9 = main.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 4.5F, 0.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone10 = bone9.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(38, 23).addBox(2.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bone11 = bone9.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(38, 23).addBox(-3.5397F, -3.6003F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 38).addBox(-0.5642F, -4.9083F, -2.5F, 2.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.75F, 0.5F, 0.0F, 0.0F, 0.1309F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(15, 38).addBox(-0.872F, -1.5934F, -2.5F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2F, -5.25F, 0.0F, 0.0F, 0.0F, -1.0036F));

        PartDefinition bone3 = main.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 38).mirror().addBox(-1.4358F, -4.9083F, -2.5F, 2.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -6.75F, 0.5F, 0.0F, 0.0F, -0.1309F));

        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(15, 38).mirror().addBox(-1.128F, -1.5934F, -2.5F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.2F, -5.25F, 0.0F, 0.0F, 0.0F, 1.0036F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
