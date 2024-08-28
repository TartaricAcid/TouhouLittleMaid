package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EntityFairyModel extends EntityModel<EntityFairy> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "fairy");
    private final ModelPart head;
    private final ModelPart armRight;
    private final ModelPart armLeft;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart blink;

    public EntityFairyModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.head = root.getChild("head");
        this.armRight = root.getChild("armRight");
        this.armLeft = root.getChild("armLeft");
        this.body = root.getChild("body");
        this.legLeft = root.getChild("legLeft");
        this.legRight = root.getChild("legRight");
        this.wingLeft = root.getChild("wingLeft");
        this.wingRight = root.getChild("wingRight");
        this.blink = this.head.getChild("blink");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(62, 17).addBox(-6.0F, -10.75F, -1.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(30, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(22, 25).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition blink = head.addOrReplaceChild("blink", CubeListBuilder.create().texOffs(20, 0).addBox(-4.0F, -22.0F, -4.001F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition bone40 = head.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, 4.1137F, -0.7853F, 8.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2F, 3.8F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone5 = head.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(25, 45).addBox(-1.5F, 4.1137F, -0.7853F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 11).addBox(-1.25F, 4.1137F, -1.7853F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone6 = head.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 44).addBox(-0.1946F, 3.1892F, -1.75F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(6, 11).addBox(-1.9446F, 3.6892F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -11.0F, 2.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition bone7 = head.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(13, 42).addBox(-2.8054F, 3.1892F, -1.75F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -11.0F, 2.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition bone8 = head.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(0, 11).addBox(-0.0554F, 3.6892F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -11.0F, 2.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition og_hair = head.addOrReplaceChild("og_hair", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.3F, 3.8F, 0.2618F, 0.0F, 0.0F));

        PartDefinition hair9 = og_hair.addOrReplaceChild("hair9", CubeListBuilder.create().texOffs(62, 23).addBox(-0.2F, 0.25F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition hair10 = og_hair.addOrReplaceChild("hair10", CubeListBuilder.create().texOffs(64, 23).addBox(-0.75F, 0.05F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2007F));

        PartDefinition hair11 = og_hair.addOrReplaceChild("hair11", CubeListBuilder.create().texOffs(66, 23).addBox(-0.75F, -0.25F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2356F));

        PartDefinition hair12 = og_hair.addOrReplaceChild("hair12", CubeListBuilder.create().texOffs(68, 23).mirror().addBox(-0.8F, 0.25F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition hair13 = og_hair.addOrReplaceChild("hair13", CubeListBuilder.create().texOffs(70, 23).mirror().addBox(-0.25F, 0.05F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1571F));

        PartDefinition hair14 = og_hair.addOrReplaceChild("hair14", CubeListBuilder.create().texOffs(72, 23).mirror().addBox(-0.25F, -0.25F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2356F));

        PartDefinition hair15 = og_hair.addOrReplaceChild("hair15", CubeListBuilder.create().texOffs(74, 23).addBox(-0.75F, 0.05F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1571F));

        PartDefinition hair16 = og_hair.addOrReplaceChild("hair16", CubeListBuilder.create().texOffs(76, 23).mirror().addBox(-0.25F, 0.05F, 0.25F, 1.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2007F));

        PartDefinition hairbun = head.addOrReplaceChild("hairbun", CubeListBuilder.create().texOffs(85, 0).addBox(-3.0F, -1.75F, 0.25F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(18, 11).addBox(-1.0F, 0.25F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.7F, 4.5F));

        PartDefinition ahoge = head.addOrReplaceChild("ahoge", CubeListBuilder.create().texOffs(62, 34).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(62, 32).addBox(-0.5F, -4.5F, 0.0F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(66, 34).addBox(5.5F, -4.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(63, 37).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -8.0F, -2.5F));

        PartDefinition tamed_bowtie = head.addOrReplaceChild("tamed_bowtie", CubeListBuilder.create().texOffs(102, 51).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 4.5F));

        PartDefinition headdressRightTop_r1 = tamed_bowtie.addOrReplaceChild("headdressRightTop_r1", CubeListBuilder.create().texOffs(100, 58).addBox(0.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, -0.0456F, -0.1685F, -0.607F));

        PartDefinition headdressLeftBottom_r1 = tamed_bowtie.addOrReplaceChild("headdressLeftBottom_r1", CubeListBuilder.create().texOffs(116, 53).addBox(0.0F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, -0.0873F, 0.9599F));

        PartDefinition headdressRightBottom_r1 = tamed_bowtie.addOrReplaceChild("headdressRightBottom_r1", CubeListBuilder.create().texOffs(116, 48).addBox(-5.0F, -2.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0873F, -0.9599F));

        PartDefinition headdressLeftTop_r1 = tamed_bowtie.addOrReplaceChild("headdressLeftTop_r1", CubeListBuilder.create().texOffs(114, 58).addBox(-6.0F, -2.5F, -0.5F, 6.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, -0.0456F, 0.1685F, 0.607F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.5F, -2.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-3.5F, -4.5F, -2.0F, 7.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 13.5F, 0.0F));

        PartDefinition sittingRotationSkirt = body.addOrReplaceChild("sittingRotationSkirt", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition skirt = sittingRotationSkirt.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(30, 16).addBox(-2.5F, -7.0F, -1.5F, 7.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 8.0F, -1.0F));

        PartDefinition bone = skirt.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(1.0F, -5.0F, 1.0F));

        PartDefinition bone4 = bone.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.5F));

        PartDefinition bone49 = bone4.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -1.5186F, -0.7446F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.9705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone50 = bone49.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(32, 56).addBox(-1.326F, -2.111F, -0.7206F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone51 = bone49.addOrReplaceChild("bone51", CubeListBuilder.create().texOffs(56, 24).addBox(-1.674F, -2.111F, -0.7436F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone52 = bone.addOrReplaceChild("bone52", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition skirtpart_left = bone52.addOrReplaceChild("skirtpart_left", CubeListBuilder.create().texOffs(54, 16).addBox(-2.0F, -1.7186F, -0.6946F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone54 = skirtpart_left.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(51, 56).addBox(-0.826F, -2.3188F, -0.6706F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.4422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone55 = skirtpart_left.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(42, 56).addBox(-1.674F, -2.3188F, -0.6936F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition skirtpart_right = bone52.addOrReplaceChild("skirtpart_right", CubeListBuilder.create().texOffs(0, 54).addBox(-2.0F, -1.6186F, -8.2214F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 4.3705F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bone2 = skirtpart_right.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(38, 56).addBox(-0.826F, -2.2188F, -8.2464F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.4422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone10 = skirtpart_right.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(47, 54).addBox(-1.674F, -2.2188F, -8.2464F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition skirt_front = bone.addOrReplaceChild("skirt_front", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -3.1416F, 0.0F));

        PartDefinition skirt1_front = skirt_front.addOrReplaceChild("skirt1_front", CubeListBuilder.create().texOffs(54, 33).addBox(-2.0F, -1.5436F, -0.7196F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.9705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition skirt2_front = skirt1_front.addOrReplaceChild("skirt2_front", CubeListBuilder.create().texOffs(26, 55).addBox(-1.326F, -2.1188F, -0.6956F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition skirt3_front = skirt1_front.addOrReplaceChild("skirt3_front", CubeListBuilder.create().texOffs(16, 33).addBox(-1.624F, -2.1188F, -0.6956F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition lace2 = skirt.addOrReplaceChild("lace2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -18.5F, 1.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition apron = skirt.addOrReplaceChild("apron", CubeListBuilder.create().texOffs(62, 9).mirror().addBox(-3.5F, -0.683F, -0.5335F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -6.4641F, -1.1F, -0.2618F, 0.0F, 0.0F));

        PartDefinition chestbow = body.addOrReplaceChild("chestbow", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5F, 5.0F));

        PartDefinition bone163 = chestbow.addOrReplaceChild("bone163", CubeListBuilder.create().texOffs(24, 26).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2822F, -0.0079F, -7.5966F, -0.3054F, -0.3054F, -2.3126F));

        PartDefinition bone164 = chestbow.addOrReplaceChild("bone164", CubeListBuilder.create().texOffs(24, 24).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -7.51F));

        PartDefinition bone165 = bone164.addOrReplaceChild("bone165", CubeListBuilder.create().texOffs(24, 28).addBox(-2.0F, 3.1411F, -14.4648F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 15.51F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone9 = chestbow.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(24, 26).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.2822F, -0.0079F, -7.5966F, -0.3054F, 0.3054F, 2.3126F));

        PartDefinition chestbow2 = body.addOrReplaceChild("chestbow2", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition bone3 = chestbow2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(76, 11).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2037F, -10.7079F, 2.5984F, 0.2849F, -0.274F, -0.8249F));

        PartDefinition bone11 = chestbow2.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(76, 9).addBox(-1.25F, -1.25F, -16.01F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, -9.95F, 17.8F));

        PartDefinition bone12 = bone11.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(84, 9).addBox(-0.5F, -0.4589F, -0.4648F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -0.75F, -15.0F, 0.3873F, 0.0665F, -0.1615F));

        PartDefinition bone14 = bone11.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(86, 9).addBox(-0.5F, -0.4589F, -0.4648F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -0.75F, -15.0F, 0.3873F, -0.0665F, 0.1615F));

        PartDefinition bone13 = chestbow2.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(76, 11).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.1855F, -10.7079F, 2.6397F, 0.3311F, 0.2895F, 0.8777F));

        PartDefinition legLeft = partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(46, 22).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(53, 42).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(1.5F, 16.0F, 0.0F));

        PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(10, 52).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 53).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(72, 46).addBox(-1.0F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0986F, 9.3589F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition wingLeft = partdefinition.addOrReplaceChild("wingLeft", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 11.0F, 4.0F, 0.0F, 1.2217F, 0.0F));

        PartDefinition wingLeftUp = wingLeft.addOrReplaceChild("wingLeftUp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 9.0F, -2.0F, 0.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -11.0F, -7.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition wingLeftDown = wingLeft.addOrReplaceChild("wingLeftDown", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.0F, 3.0F, -1.5F, 0.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, 6.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition legRight = partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(37, 45).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(52, 51).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(-1.5F, 16.0F, 0.0F));

        PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(47, 45).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(55, 56).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(60, 46).mirror().addBox(-2.0F, -0.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0986F, 9.3589F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition wingRight = partdefinition.addOrReplaceChild("wingRight", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 11.0F, 4.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition wingRightUp = wingRight.addOrReplaceChild("wingRightUp", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 9.0F, -2.0F, 0.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, -7.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition wingRightDown = wingRight.addOrReplaceChild("wingRightDown", CubeListBuilder.create().texOffs(0, 12).addBox(1.0F, 3.0F, -1.5F, 0.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 6.5F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(EntityFairy entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;
        armLeft.zRot = Mth.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.zRot = -Mth.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        if (entityIn.isOnGround()) {
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

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        armRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        armLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        wingLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        wingRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}