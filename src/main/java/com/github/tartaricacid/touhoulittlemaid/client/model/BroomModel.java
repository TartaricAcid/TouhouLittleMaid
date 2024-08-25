package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BroomModel extends AbstractModel<EntityBroom> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "broom");
    private final ModelPart all;

    public BroomModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 10.0F));

        PartDefinition middle = head.addOrReplaceChild("middle", CubeListBuilder.create(), PartPose.offset(0.0F, -0.75F, 0.0F));

        PartDefinition sz = middle.addOrReplaceChild("sz", CubeListBuilder.create().texOffs(18, 2).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, -1.0F, 5.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition sz2 = middle.addOrReplaceChild("sz2", CubeListBuilder.create().texOffs(17, 34).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, 1.0F, 5.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition sz4 = middle.addOrReplaceChild("sz4", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-1.1F, -0.7F, 5.0F, -0.1314F, -0.0865F, 1.5822F));

        PartDefinition sz19 = middle.addOrReplaceChild("sz19", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.1F, -0.7F, 5.0F, -0.1314F, 0.0865F, -1.5822F));

        PartDefinition sz27 = middle.addOrReplaceChild("sz27", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.6F, 0.3F, 5.0F, -0.2199F, -0.1376F, -1.5479F));

        PartDefinition sz28 = middle.addOrReplaceChild("sz28", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-0.6F, 0.3F, 5.0F, -0.2199F, 0.1376F, 1.5479F));

        PartDefinition sz3 = middle.addOrReplaceChild("sz3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.1F, -0.7F, 5.0F, -0.1314F, -0.0865F, 1.5822F));

        PartDefinition hdj = middle.addOrReplaceChild("hdj", CubeListBuilder.create().texOffs(24, 4).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, -0.75F, 0.2182F, 0.0F, 0.0F));

        PartDefinition sz11 = hdj.addOrReplaceChild("sz11", CubeListBuilder.create().texOffs(6, 16).addBox(-0.0577F, -1.1297F, -1.0431F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-1.5063F, -0.0578F, 1.4249F, 0.9079F, -0.0985F, 1.6184F));

        PartDefinition sz13 = hdj.addOrReplaceChild("sz13", CubeListBuilder.create().texOffs(0, 16).addBox(-0.9423F, -1.1297F, -1.0431F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.5063F, -0.0578F, 1.4249F, 0.9079F, 0.0985F, -1.6184F));

        PartDefinition sz12 = hdj.addOrReplaceChild("sz12", CubeListBuilder.create().texOffs(26, 18).addBox(0.1615F, -1.0285F, -0.0319F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-1.5063F, -0.0578F, 1.4249F, 1.1238F, -0.1378F, 1.6374F));

        PartDefinition sz14 = hdj.addOrReplaceChild("sz14", CubeListBuilder.create().texOffs(10, 6).addBox(-1.1615F, -1.0285F, -0.0319F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.5063F, -0.0578F, 1.4249F, 1.1238F, 0.1378F, -1.6374F));

        PartDefinition sz10 = hdj.addOrReplaceChild("sz10", CubeListBuilder.create().texOffs(18, 0).addBox(-0.9423F, -1.1297F, -1.0431F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.5063F, 0.4422F, 0.4249F, 0.0257F, 0.0931F, -1.8371F));

        PartDefinition sz7 = hdj.addOrReplaceChild("sz7", CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-1.0577F, -1.1297F, -1.0431F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-1.5063F, 0.4422F, 0.4249F, 0.0257F, -0.0931F, 1.8371F));

        PartDefinition sz9 = hdj.addOrReplaceChild("sz9", CubeListBuilder.create().texOffs(18, 6).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.5063F, 0.4422F, 0.4249F, -0.4982F, 0.0497F, -1.842F));

        PartDefinition sz8 = hdj.addOrReplaceChild("sz8", CubeListBuilder.create().texOffs(18, 6).mirror().addBox(-1.0F, -1.0F, -1.5F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-1.5063F, 0.4422F, 0.4249F, -0.4982F, -0.0497F, 1.842F));

        PartDefinition sz15 = hdj.addOrReplaceChild("sz15", CubeListBuilder.create().texOffs(51, 36).addBox(-3.7003F, -1.5093F, -5.3478F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(5.9437F, 1.1091F, 3.8955F, -0.1769F, 0.2978F, -0.459F));

        PartDefinition sz23 = hdj.addOrReplaceChild("sz23", CubeListBuilder.create().texOffs(51, 36).mirror().addBox(2.7003F, -1.5093F, -5.3478F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-5.9437F, 1.1091F, 3.8955F, -0.1769F, -0.2978F, 0.459F));

        PartDefinition sz24 = hdj.addOrReplaceChild("sz24", CubeListBuilder.create().texOffs(26, 0).addBox(-2.0337F, -1.5048F, -0.5382F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.5374F, 1.8739F, -2.1712F, -0.2187F, 0.6831F, -0.5459F));

        PartDefinition sz25 = hdj.addOrReplaceChild("sz25", CubeListBuilder.create().texOffs(8, 24).addBox(1.0337F, -1.5048F, -0.5382F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-1.5374F, 1.8739F, -2.1712F, -0.2187F, -0.6831F, 0.5459F));

        PartDefinition sz17 = hdj.addOrReplaceChild("sz17", CubeListBuilder.create().texOffs(52, 14).addBox(0.0814F, 0.4036F, -3.5F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(2.5483F, 1.9633F, 3.652F, 0.0938F, 0.3326F, 0.3688F));

        PartDefinition sz26 = hdj.addOrReplaceChild("sz26", CubeListBuilder.create().texOffs(52, 14).mirror().addBox(-1.0814F, 0.4036F, -3.5F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-2.5483F, 1.9633F, 3.652F, 0.0938F, -0.3326F, -0.3688F));

        PartDefinition sz18 = hdj.addOrReplaceChild("sz18", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0488F, -0.0089F, -1.3887F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.7836F, 2.5258F, -0.5131F, 0.1183F, 0.7231F, 0.4166F));

        PartDefinition sz22 = hdj.addOrReplaceChild("sz22", CubeListBuilder.create().texOffs(26, 25).addBox(0.0488F, -0.0089F, -1.3887F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-1.7836F, 2.5258F, -0.5131F, 0.1183F, -0.7231F, -0.4166F));

        PartDefinition sz16 = hdj.addOrReplaceChild("sz16", CubeListBuilder.create().texOffs(10, 49).addBox(-0.5F, -1.5F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.9437F, 1.1091F, 3.8955F, -0.3339F, 0.0886F, -1.2327F));

        PartDefinition sz21 = hdj.addOrReplaceChild("sz21", CubeListBuilder.create().texOffs(10, 49).mirror().addBox(-0.5F, -1.5F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-1.9437F, 1.1091F, 3.8955F, -0.3339F, -0.0886F, 1.2327F));

        PartDefinition sz5 = middle.addOrReplaceChild("sz5", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(2.0F, 0.0F, 5.0F, -0.2618F, 0.0F, -1.5708F));

        PartDefinition sz20 = middle.addOrReplaceChild("sz20", CubeListBuilder.create().texOffs(19, 19).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 5.0F, -0.2618F, 0.0F, 1.5708F));

        PartDefinition sz6 = middle.addOrReplaceChild("sz6", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 0.0F, 5.0F, -0.2618F, 0.0F, 1.5708F));

        PartDefinition left = head.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5742F, 0.0F, 4.9848F, 0.0F, 0.2181F, 0.0F));

        PartDefinition handle = all.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(39, 44).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition handle2 = all.addOrReplaceChild("handle2", CubeListBuilder.create().texOffs(36, 18).addBox(-1.0F, -0.0606F, -9.3274F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition handle3 = all.addOrReplaceChild("handle3", CubeListBuilder.create().texOffs(36, 0).addBox(-0.4378F, -1.5375F, -6.0808F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5622F, -0.3907F, -16.1535F, -0.0418F, 0.0F, 0.0F));

        PartDefinition handle4 = all.addOrReplaceChild("handle4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.4378F, -0.8646F, -6.0368F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(18, 24).addBox(-0.9378F, -2.6146F, -3.0368F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5622F, -0.3907F, -16.1535F, 0.1764F, 0.0F, 0.0F));

        PartDefinition handle5 = all.addOrReplaceChild("handle5", CubeListBuilder.create().texOffs(23, 9).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.3791F, -20.6994F, 1.2236F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityBroom broom, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        all.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (broom.isVehicle()) {
            all.xRot = headPitch * ((float) Math.PI / 180F) / 10;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
