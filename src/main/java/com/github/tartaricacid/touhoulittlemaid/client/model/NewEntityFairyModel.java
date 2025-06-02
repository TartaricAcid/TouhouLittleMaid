package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.io.InputStream;

public class NewEntityFairyModel extends SimpleBedrockModel<EntityFairy> {
    // 为了别的模组兼容，暂时保留
    @Deprecated
    public static ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "new_fairy");

    private final BedrockPart head;
    private final BedrockPart blink;
    private final BedrockPart armLeft;
    private final BedrockPart legLeft;
    private final BedrockPart legRight;
    private final BedrockPart wingLeft;
    private final BedrockPart wingRight;
    private final BedrockPart armRight;

    public NewEntityFairyModel(InputStream stream) {
        super(stream);
        this.head = this.getPart("head");
        this.blink = this.getPart("blink");
        this.armLeft = this.getPart("armLeft");
        this.legLeft = this.getPart("legLeft");
        this.legRight = this.getPart("legRight");
        this.wingLeft = this.getPart("wingLeft");
        this.wingRight = this.getPart("wingRight");
        this.armRight = this.getPart("armRight");
    }

    @Override
    public void setupAnim(EntityFairy entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;
        armLeft.zRot = Mth.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.zRot = -Mth.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        if (entityIn.onGround()) {
            legLeft.xRot = Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legRight.xRot = -Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            armLeft.xRot = -Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            armRight.xRot = Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            wingLeft.yRot = -Mth.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
            wingRight.yRot = Mth.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        } else {
            legLeft.xRot = 0f;
            legRight.xRot = 0f;
            armLeft.xRot = -0.17453292F;
            armRight.xRot = -0.17453292F;
            head.xRot = head.xRot - 8 * 0.017453292F;
            wingLeft.yRot = -Mth.cos(ageInTicks * 0.5f) * 0.4f + 1.2f;
            wingRight.yRot = Mth.cos(ageInTicks * 0.5f) * 0.4f - 1.2f;
        }
        float remainder = ageInTicks % 60;
        // 0-10 显示眨眼贴图
        blink.visible = (55 < remainder && remainder < 60);
    }

    // 为了别的模组兼容，暂时保留
    @Deprecated
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(76, 20).addBox(-9.0F, -12.75F, -1.0F, 18.0F, 13.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(76, 33).addBox(-9.0F, -12.75F, -0.9375F, 18.0F, 13.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -7.75F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(83, 10).addBox(-3.0F, -7.95F, 4.75F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(80, 10).addBox(-1.0F, -5.95F, 3.9375F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition og_hair_r1 = head.addOrReplaceChild("og_hair_r1", CubeListBuilder.create().texOffs(70, 10).addBox(-0.1799F, -0.2112F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2616F, 0.0113F, -0.0421F));

        PartDefinition og_hair_r2 = head.addOrReplaceChild("og_hair_r2", CubeListBuilder.create().texOffs(66, 10).addBox(1.2039F, -0.0236F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2568F, 0.0516F, -0.194F));

        PartDefinition og_hair_r3 = head.addOrReplaceChild("og_hair_r3", CubeListBuilder.create().texOffs(64, 10).addBox(2.1776F, -0.0219F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2549F, 0.0605F, -0.2279F));

        PartDefinition og_hair_r4 = head.addOrReplaceChild("og_hair_r4", CubeListBuilder.create().texOffs(72, 10).addBox(-0.8201F, -0.2112F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2616F, -0.0113F, 0.0421F));

        PartDefinition og_hair_r5 = head.addOrReplaceChild("og_hair_r5", CubeListBuilder.create().texOffs(74, 10).addBox(-1.3099F, -0.2495F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2587F, -0.0405F, 0.1518F));

        PartDefinition og_hair_r6 = head.addOrReplaceChild("og_hair_r6", CubeListBuilder.create().texOffs(78, 10).addBox(-3.1776F, -0.0219F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2549F, -0.0605F, 0.2279F));

        PartDefinition og_hair_r7 = head.addOrReplaceChild("og_hair_r7", CubeListBuilder.create().texOffs(68, 10).addBox(0.3099F, -0.2495F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2587F, 0.0405F, -0.1518F));

        PartDefinition og_hair_r8 = head.addOrReplaceChild("og_hair_r8", CubeListBuilder.create().texOffs(76, 10).addBox(-2.2039F, -0.0236F, 0.0F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0812F, 4.161F, 0.2568F, -0.0516F, 0.194F));

        PartDefinition bone40_r1 = head.addOrReplaceChild("bone40_r1", CubeListBuilder.create().texOffs(101, 9).addBox(-3.75F, 0.25F, 0.25F, 8.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, -0.2F, 3.8F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rear_ponytail_r1 = head.addOrReplaceChild("rear_ponytail_r1", CubeListBuilder.create().texOffs(100, 0).addBox(-1.75F, -1.5F, 1.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(120, 0).addBox(-1.2F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, -1.75F, 4.05F, 0.0F, 0.0F, 0.1745F));

        PartDefinition rear_ponytail_r2 = head.addOrReplaceChild("rear_ponytail_r2", CubeListBuilder.create().texOffs(110, 0).addBox(-1.25F, -1.5F, 1.0F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(120, 3).addBox(-0.8F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.25F, -1.75F, 4.05F, 0.0F, 0.0F, -0.1745F));

        PartDefinition pigtails_r1 = head.addOrReplaceChild("pigtails_r1", CubeListBuilder.create().texOffs(64, 0).addBox(-3.5F, -0.75F, -1.75F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(61, 0).addBox(-0.75F, -0.25F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -7.0F, 2.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition pigtails_r2 = head.addOrReplaceChild("pigtails_r2", CubeListBuilder.create().texOffs(73, 0).addBox(-1.25F, -0.25F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(76, 0).addBox(0.5F, -0.75F, -1.75F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 2.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition bone42_r1 = head.addOrReplaceChild("bone42_r1", CubeListBuilder.create().texOffs(85, 0).addBox(-1.0F, -0.25F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(88, 0).addBox(-1.5F, -0.25F, 0.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, 4.25F, 0.1745F, 0.0F, 0.0F));

        PartDefinition blink = head.addOrReplaceChild("blink", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -26.0F, -4.001F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(32, 40).addBox(-0.2113F, -0.0469F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 33).addBox(-0.7113F, -1.0469F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(60, 35).addBox(-0.7113F, -1.0469F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(2.5341F, 7.7588F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -7.5F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 20).addBox(-4.0F, -7.5F, -3.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(0, 33).addBox(-3.5F, 0.5F, -2.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(-0.0625F))
                .texOffs(18, 16).addBox(-0.5F, -7.0F, -4.01F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 28).addBox(-0.5F, -3.5F, 2.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));

        PartDefinition bone168_r1 = body.addOrReplaceChild("bone168_r1", CubeListBuilder.create().texOffs(0, 31).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6875F, -7.25F, -2.75F, 0.0F, 0.0F, 0.3491F));

        PartDefinition backbow_r1 = body.addOrReplaceChild("backbow_r1", CubeListBuilder.create().texOffs(64, 20).addBox(-0.2056F, -0.208F, -1.4023F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.0F, -0.2618F, -3.1241F));

        PartDefinition backbow_r2 = body.addOrReplaceChild("backbow_r2", CubeListBuilder.create().texOffs(64, 23).addBox(-4.7441F, -0.7487F, -1.3889F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.0F, 0.2618F, -0.1745F));

        PartDefinition backbow_r3 = body.addOrReplaceChild("backbow_r3", CubeListBuilder.create().texOffs(64, 25).addBox(-4.6903F, -0.7817F, -1.3744F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.0F, 0.2618F, -0.384F));

        PartDefinition backbow_r4 = body.addOrReplaceChild("backbow_r4", CubeListBuilder.create().texOffs(62, 28).addBox(-0.4847F, 0.1799F, -1.1529F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.3927F, 0.0F, 0.48F));

        PartDefinition backbow_r5 = body.addOrReplaceChild("backbow_r5", CubeListBuilder.create().texOffs(52, 23).addBox(-0.2559F, -0.7487F, -1.3889F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.0F, -0.2618F, 0.1745F));

        PartDefinition backbow_r6 = body.addOrReplaceChild("backbow_r6", CubeListBuilder.create().texOffs(52, 25).addBox(-0.3097F, -0.7817F, -1.3744F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.0F, -0.2618F, 0.384F));

        PartDefinition backbow_r7 = body.addOrReplaceChild("backbow_r7", CubeListBuilder.create().texOffs(58, 28).addBox(-0.5153F, 0.1799F, -1.1529F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.3927F, 0.0F, -0.48F));

        PartDefinition backbow_r8 = body.addOrReplaceChild("backbow_r8", CubeListBuilder.create().texOffs(52, 20).addBox(-4.7944F, -0.208F, -1.4023F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5962F, 4.1137F, 0.0F, 0.2618F, 3.1241F));

        PartDefinition bowtie_r1 = body.addOrReplaceChild("bowtie_r1", CubeListBuilder.create().texOffs(18, 20).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -6.5F, -3.75F, -0.3185F, -0.3035F, 0.8345F));

        PartDefinition bowtie_r2 = body.addOrReplaceChild("bowtie_r2", CubeListBuilder.create().texOffs(22, 16).addBox(-1.5F, -2.0F, 0.5F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -4.01F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bowtie_r3 = body.addOrReplaceChild("bowtie_r3", CubeListBuilder.create().texOffs(18, 18).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -6.5F, -3.75F, -0.3185F, 0.3035F, -0.8345F));

        PartDefinition bone168_r2 = body.addOrReplaceChild("bone168_r2", CubeListBuilder.create().texOffs(8, 31).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6875F, -7.25F, -2.75F, 0.0F, 0.0F, -0.3491F));

        PartDefinition skirt = body.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(24, 50).addBox(-2.5F, -11.0F, -2.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 10.5F, -1.0F));

        PartDefinition apron2_r1 = skirt.addOrReplaceChild("apron2_r1", CubeListBuilder.create().texOffs(45, 48).addBox(-4.0F, -0.5F, 0.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -11.183F, -2.5335F, -0.3054F, 0.0F, 0.0F));

        PartDefinition bone59_r1 = skirt.addOrReplaceChild("bone59_r1", CubeListBuilder.create().texOffs(16, 59).addBox(1.3605F, -3.9541F, 0.0311F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.1385F, -3.238F, -0.1601F, -0.0388F, -0.2135F));

        PartDefinition bone58_r1 = skirt.addOrReplaceChild("bone58_r1", CubeListBuilder.create().texOffs(60, 59).addBox(-4.3605F, -3.9541F, 0.0311F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.201F, 5.262F, 0.1765F, -0.036F, 0.214F));

        PartDefinition bone59_r2 = skirt.addOrReplaceChild("bone59_r2", CubeListBuilder.create().texOffs(0, 59).addBox(-4.3605F, -3.9541F, 0.0311F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.1385F, -3.238F, -0.1601F, 0.0388F, 0.2135F));

        PartDefinition bone58_r2 = skirt.addOrReplaceChild("bone58_r2", CubeListBuilder.create().texOffs(6, 59).addBox(-2.5F, -4.4173F, 0.0108F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.1385F, -3.2224F, -0.1644F, 0.0F, 0.0F));

        PartDefinition bone59_r3 = skirt.addOrReplaceChild("bone59_r3", CubeListBuilder.create().texOffs(22, 56).addBox(0.0F, 0.2989F, -3.8614F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4375F, -10.5578F, 1.408F, -0.1745F, 0.0F, -0.2182F));

        PartDefinition bone57_r1 = skirt.addOrReplaceChild("bone57_r1", CubeListBuilder.create().texOffs(28, 54).addBox(0.0F, 0.0F, -2.5F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4531F, -10.5578F, 0.9705F, 0.0F, 0.0F, -0.2182F));

        PartDefinition bone58_r3 = skirt.addOrReplaceChild("bone58_r3", CubeListBuilder.create().texOffs(38, 56).addBox(0.0F, 0.2716F, 0.8809F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4375F, -10.5578F, 0.5955F, 0.1745F, 0.0F, -0.2182F));

        PartDefinition bone58_r4 = skirt.addOrReplaceChild("bone58_r4", CubeListBuilder.create().texOffs(50, 59).addBox(-2.5F, -4.4173F, 0.0108F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.201F, 5.3245F, 0.1847F, 0.0F, 0.0F));

        PartDefinition bone58_r5 = skirt.addOrReplaceChild("bone58_r5", CubeListBuilder.create().texOffs(44, 59).addBox(1.3605F, -3.9541F, 0.0311F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.201F, 5.262F, 0.1765F, 0.036F, -0.214F));

        PartDefinition bone59_r4 = skirt.addOrReplaceChild("bone59_r4", CubeListBuilder.create().texOffs(82, 56).addBox(0.0F, 0.2989F, -3.8614F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4375F, -10.5578F, 1.408F, -0.1745F, 0.0F, 0.2182F));

        PartDefinition bone58_r6 = skirt.addOrReplaceChild("bone58_r6", CubeListBuilder.create().texOffs(66, 56).addBox(0.0F, 0.2716F, 0.8809F, 0.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4375F, -10.5578F, 0.5955F, 0.1745F, 0.0F, 0.2182F));

        PartDefinition bone57_r2 = skirt.addOrReplaceChild("bone57_r2", CubeListBuilder.create().texOffs(72, 54).addBox(0.0F, 0.0F, -2.5F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4531F, -10.5578F, 0.9705F, 0.0F, 0.0F, 0.2182F));

        PartDefinition apron2 = skirt.addOrReplaceChild("apron2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -11.0F, -2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legLeft = partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(12, 46).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 39).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 15.0F, 0.0F));

        PartDefinition legRight = partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(0, 46).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 39).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 15.0F, 0.0F));

        PartDefinition wingLeft = partdefinition.addOrReplaceChild("wingLeft", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 10.0F, 5.0F, 0.0F, 1.2217F, 0.0F));

        PartDefinition wingLeftUp_r1 = wingLeft.addOrReplaceChild("wingLeftUp_r1", CubeListBuilder.create().texOffs(0, 77).addBox(0.0F, -3.5858F, -1.4142F, 0.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0607F, 1.7678F, 0.7854F, 0.0F, 0.0F));

        PartDefinition wingLeftDown_r1 = wingLeft.addOrReplaceChild("wingLeftDown_r1", CubeListBuilder.create().texOffs(16, 69).addBox(0.0F, 1.4142F, -7.4142F, 0.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.4142F, 1.4142F, 0.7854F, 0.0F, 0.0F));

        PartDefinition wingRight = partdefinition.addOrReplaceChild("wingRight", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 10.0F, 5.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition wingRightDown_r1 = wingRight.addOrReplaceChild("wingRightDown_r1", CubeListBuilder.create().texOffs(0, 69).addBox(0.0F, 1.4142F, -7.4142F, 0.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.4142F, 1.4142F, 0.7854F, 0.0F, 0.0F));

        PartDefinition wingRightUp_r1 = wingRight.addOrReplaceChild("wingRightUp_r1", CubeListBuilder.create().texOffs(0, 53).addBox(0.0F, -3.5858F, -1.4142F, 0.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0607F, 1.7678F, 0.7854F, 0.0F, 0.0F));

        PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(48, 35).addBox(-2.2887F, -1.0469F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(-0.3F))
                .texOffs(24, 40).addBox(-1.7887F, -0.0469F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 33).addBox(-2.2887F, -1.0469F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5341F, 7.7588F, 0.0F, 0.0F, 0.0F, 0.2618F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}