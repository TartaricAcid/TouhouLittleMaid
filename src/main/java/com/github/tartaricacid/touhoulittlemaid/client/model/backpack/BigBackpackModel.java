package com.github.tartaricacid.touhoulittlemaid.client.model.backpack;


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


public class BigBackpackModel extends EntityModel<EntityMaid> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "backpack_big");
    private final ModelPart bone;

    public BigBackpackModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, -1.75F));

        PartDefinition armorBody_r1 = bone.addOrReplaceChild("armorBody_r1", CubeListBuilder.create().texOffs(42, 8).addBox(-1.0F, -2.25F, -1.5F, 2.0F, 4.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6665F, 9.3426F, 3.9862F, -0.0873F, -0.2618F, 0.1745F));

        PartDefinition armorBody_r2 = bone.addOrReplaceChild("armorBody_r2", CubeListBuilder.create().texOffs(20, 0).addBox(-3.5F, -1.5F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 2.0F, 3.4F, 0.1309F, 0.0F, 0.0F));

        PartDefinition armorBody_r3 = bone.addOrReplaceChild("armorBody_r3", CubeListBuilder.create().texOffs(42, 0).addBox(-1.0F, -2.25F, -1.5F, 2.0F, 4.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6665F, 9.3426F, 3.9862F, -0.0873F, 0.2618F, -0.1745F));

        PartDefinition armorBody_r4 = bone.addOrReplaceChild("armorBody_r4", CubeListBuilder.create().texOffs(18, 39).addBox(-2.5F, -3.5F, -1.0F, 5.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0948F, 6.4705F, 0.0873F, 0.0F, 0.0F));

        PartDefinition armorBody_r5 = bone.addOrReplaceChild("armorBody_r5", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -3.0F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 3.9F, -0.0436F, 0.0F, 0.0F));

        PartDefinition armorBody_r6 = bone.addOrReplaceChild("armorBody_r6", CubeListBuilder.create().texOffs(38, 27).addBox(-2.5F, -1.5F, -1.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-3.5F, -2.5F, -1.025F, 7.0F, 4.0F, 2.0F, new CubeDeformation(-0.95F)), PartPose.offsetAndRotation(0.0F, 2.5F, 6.15F, -0.0873F, 0.0F, 0.0F));

        PartDefinition band3 = bone.addOrReplaceChild("band3", CubeListBuilder.create(), PartPose.offset(1.5F, 2.65F, 0.75F));

        PartDefinition bone2 = band3.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(5, 0).addBox(-0.5F, 0.3331F, -1.0007F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.749F, -1.0F));

        PartDefinition bone3 = band3.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(6, 1).addBox(-1.0F, -0.0007F, -3.4311F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.65F, -2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone4 = band3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(2, 5).addBox(-1.0F, -0.251F, -11.433F, 1.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.75F, -4.5F, 2.0944F, 0.0F, 0.0F));

        PartDefinition band2 = bone.addOrReplaceChild("band2", CubeListBuilder.create(), PartPose.offset(-2.5F, 2.65F, 0.75F));

        PartDefinition bone5 = band2.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(5, 0).addBox(-0.5F, 0.3331F, -1.0007F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.749F, -1.0F));

        PartDefinition bone6 = band2.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(6, 1).addBox(-1.0F, -0.0007F, -3.4311F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.65F, -2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone7 = band2.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(2, 5).addBox(-1.0F, -0.251F, -11.433F, 1.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.75F, -4.5F, 2.0944F, 0.0F, 0.0F));

        PartDefinition straps = bone.addOrReplaceChild("straps", CubeListBuilder.create(), PartPose.offset(-10.0F, 9.0F, 3.9F));

        PartDefinition armorBody_r7 = straps.addOrReplaceChild("armorBody_r7", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -3.5F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(10.0F, -0.9052F, 2.5705F, 0.0873F, 0.0F, 0.0F));

        PartDefinition armorBody_r8 = straps.addOrReplaceChild("armorBody_r8", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition armorBody_r9 = straps.addOrReplaceChild("armorBody_r9", CubeListBuilder.create().texOffs(32, 39).addBox(-1.5F, -1.2F, -1.3F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.6F))
                .texOffs(44, 16).addBox(-1.5F, -1.2F, -1.7F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.6001F)), PartPose.offsetAndRotation(10.0F, -7.0F, -0.5F, 0.1309F, 0.0F, 0.0F));

        PartDefinition armorBody_r10 = straps.addOrReplaceChild("armorBody_r10", CubeListBuilder.create().texOffs(41, 37).addBox(-2.0F, -1.5F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(10.0F, -6.5F, 2.25F, -0.0873F, 0.0F, 0.0F));

        PartDefinition zippers = bone.addOrReplaceChild("zippers", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0948F, -0.5295F));

        PartDefinition armorBody_r11 = zippers.addOrReplaceChild("armorBody_r11", CubeListBuilder.create().texOffs(22, 19).addBox(-4.5F, -2.5F, -0.75F, 9.0F, 6.0F, 2.0F, new CubeDeformation(-0.75F))
                .texOffs(20, 10).addBox(-4.5F, -2.5F, 0.5F, 9.0F, 7.0F, 2.0F, new CubeDeformation(-0.75F)), PartPose.offsetAndRotation(0.0F, -6.0948F, 3.9295F, 0.1309F, 0.0F, 0.0F));

        PartDefinition armorBody_r12 = zippers.addOrReplaceChild("armorBody_r12", CubeListBuilder.create().texOffs(29, 29).mirror().addBox(-1.525F, -3.25F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.95F)).mirror(false)
                .texOffs(29, 29).mirror().addBox(-1.45F, -3.25F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.95F)).mirror(false)
                .texOffs(29, 29).mirror().addBox(-1.375F, -3.25F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.95F)).mirror(false), PartPose.offsetAndRotation(-3.6665F, 1.2478F, 4.5157F, -0.0873F, 0.2618F, -0.1745F));

        PartDefinition armorBody_r13 = zippers.addOrReplaceChild("armorBody_r13", CubeListBuilder.create().texOffs(29, 29).addBox(-0.625F, -3.25F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.95F))
                .texOffs(29, 29).addBox(-0.55F, -3.25F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.95F))
                .texOffs(29, 29).addBox(-0.475F, -3.25F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(-0.95F)), PartPose.offsetAndRotation(3.6665F, 1.2478F, 4.5157F, -0.0873F, -0.2618F, 0.1745F));

        PartDefinition armorBody_r14 = zippers.addOrReplaceChild("armorBody_r14", CubeListBuilder.create().texOffs(16, 27).addBox(-3.5F, -4.5F, -0.6F, 7.0F, 6.0F, 2.0F, new CubeDeformation(-0.95F))
                .texOffs(16, 27).addBox(-3.5F, -4.5F, -0.525F, 7.0F, 6.0F, 2.0F, new CubeDeformation(-0.95F))
                .texOffs(16, 27).addBox(-3.5F, -4.5F, -0.675F, 7.0F, 6.0F, 2.0F, new CubeDeformation(-0.95F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition armorBody_r15 = zippers.addOrReplaceChild("armorBody_r15", CubeListBuilder.create().texOffs(0, 36).addBox(-3.5F, -2.5F, -0.875F, 7.0F, 4.0F, 2.0F, new CubeDeformation(-0.95F))
                .texOffs(0, 36).addBox(-3.5F, -2.5F, -0.95F, 7.0F, 4.0F, 2.0F, new CubeDeformation(-0.95F)), PartPose.offsetAndRotation(0.0F, -5.5948F, 6.6795F, -0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EntityMaid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.translate(0, -0.46, -0.04);
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}