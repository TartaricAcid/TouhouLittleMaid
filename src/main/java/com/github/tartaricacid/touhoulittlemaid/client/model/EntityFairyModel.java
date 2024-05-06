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

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-6.0F, -10.75F, -1.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(28, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(22, 23).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition blink = head.addOrReplaceChild("blink", CubeListBuilder.create().texOffs(52, 0).addBox(-4.0F, -26.0F, -4.001F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition bone40 = head.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(0, 43).addBox(-3.75F, 0.25F, 0.25F, 8.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2F, 3.8F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone5 = head.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(53, 41).addBox(-1.5F, 0.25F, 0.25F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(12, 12).addBox(-1.25F, 0.25F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone6 = head.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(54, 31).addBox(0.5F, -0.75F, -1.75F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 12).addBox(-1.25F, -0.25F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 2.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition bone7 = head.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(52, 51).addBox(-3.5F, -0.75F, -1.75F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -7.0F, 2.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition bone8 = head.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(6, 12).addBox(-0.75F, -0.25F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -7.0F, 2.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition armLeft = partdefinition.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(24, 76).addBox(-0.2113F, 1.4532F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(40, 50).addBox(-0.7113F, 0.4532F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 56).addBox(-0.7113F, 0.4532F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(3.0F, 6.5F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition armRight = partdefinition.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(32, 76).addBox(-1.7887F, 1.4532F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(41, 43).addBox(-2.2887F, 0.4532F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(12, 56).addBox(-2.2887F, 0.4532F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-3.0F, 6.5F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 44).addBox(-3.0F, -7.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -7.5F, -3.1F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));

        PartDefinition bone167 = body.addOrReplaceChild("bone167", CubeListBuilder.create(), PartPose.offset(0.0F, -7.5F, -4.0F));

        PartDefinition bone168 = bone167.addOrReplaceChild("bone168", CubeListBuilder.create().texOffs(12, 37).addBox(0.2891F, 0.0629F, 0.75F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4014F));

        PartDefinition bone169 = bone167.addOrReplaceChild("bone169", CubeListBuilder.create().texOffs(12, 39).addBox(-3.2891F, 0.0629F, 0.75F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4014F));

        PartDefinition bone166 = body.addOrReplaceChild("bone166", CubeListBuilder.create().texOffs(48, 16).addBox(-3.5F, 6.5F, 2.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -4.0F));

        PartDefinition bone163 = bone166.addOrReplaceChild("bone163", CubeListBuilder.create().texOffs(22, 12).addBox(-2.0F, 0.0F, -0.01F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -0.3054F, -0.3054F, -2.3126F));

        PartDefinition bone164 = bone166.addOrReplaceChild("bone164", CubeListBuilder.create().texOffs(18, 12).addBox(-0.75F, -1.25F, -0.01F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, -0.25F, 0.0F));

        PartDefinition bone165 = bone164.addOrReplaceChild("bone165", CubeListBuilder.create().texOffs(28, 4).addBox(-2.0F, -1.0F, 0.99F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -0.75F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone9 = bone166.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(22, 12).mirror().addBox(0.0F, 0.0F, -0.01F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -0.3054F, 0.3054F, 2.3126F));

        PartDefinition decoration3 = body.addOrReplaceChild("decoration3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, -4.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 7.0F));

        PartDefinition bone11 = decoration3.addOrReplaceChild("bone11", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.384F));

        PartDefinition bone15 = bone11.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(24, 19).addBox(-3.5466F, -1.7981F, -4.0561F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone21 = decoration3.addOrReplaceChild("bone21", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition bone22 = bone21.addOrReplaceChild("bone22", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone23 = bone21.addOrReplaceChild("bone23", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition bone24 = bone21.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(22, 2).addBox(-3.8132F, -1.8283F, -4.1276F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone29 = decoration3.addOrReplaceChild("bone29", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0175F));

        PartDefinition bone32 = bone29.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(24, 16).addBox(-4.0659F, -2.888F, -4.1953F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone33 = decoration3.addOrReplaceChild("bone33", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.1241F));

        PartDefinition bone34 = bone33.addOrReplaceChild("bone34", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone35 = bone33.addOrReplaceChild("bone35", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition bone36 = bone33.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(0, 40).addBox(-4.0659F, 0.888F, -4.1953F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone25 = decoration3.addOrReplaceChild("bone25", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.9671F));

        PartDefinition bone26 = bone25.addOrReplaceChild("bone26", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone27 = bone25.addOrReplaceChild("bone27", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition bone28 = bone25.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(22, 0).addBox(-3.8132F, 0.8283F, -4.1276F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone17 = decoration3.addOrReplaceChild("bone17", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7576F));

        PartDefinition bone18 = bone17.addOrReplaceChild("bone18", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone19 = bone17.addOrReplaceChild("bone19", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition bone20 = bone17.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(0, 37).addBox(-3.5466F, -0.2019F, -4.0561F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition bone38 = decoration3.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(32, 65).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 0.5F, -3.5F, 0.3927F, 0.0F, 0.48F));

        PartDefinition bone3 = decoration3.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(36, 65).addBox(0.0F, -1.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 0.5F, -3.5F, 0.3927F, 0.0F, -0.48F));

        PartDefinition sittingRotationSkirt = body.addOrReplaceChild("sittingRotationSkirt", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition skirt = sittingRotationSkirt.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(47, 22).addBox(-2.5F, -11.0F, -2.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(30, 16).addBox(-2.0F, -12.0F, -2.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 11.0F, -1.0F));

        PartDefinition bone = skirt.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(1.0F, -5.0F, 1.0F));

        PartDefinition bone4 = bone.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone49 = bone4.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(0, 67).addBox(-2.5F, -5.6578F, 0.0F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone50 = bone49.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(20, 67).addBox(-2.0F, -5.9922F, -0.101F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone51 = bone49.addOrReplaceChild("bone51", CubeListBuilder.create().texOffs(20, 67).addBox(-1.0F, -6.0922F, 0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition bone52 = bone.addOrReplaceChild("bone52", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition skirtpart_left = bone52.addOrReplaceChild("skirtpart_left", CubeListBuilder.create().texOffs(0, 67).addBox(-2.5F, -5.5578F, 0.0F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone54 = skirtpart_left.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(20, 67).addBox(-2.5F, -6.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.4422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone55 = skirtpart_left.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(20, 67).addBox(-1.0F, -6.0F, 0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition skirtpart_right = bone52.addOrReplaceChild("skirtpart_right", CubeListBuilder.create().texOffs(10, 67).addBox(-2.5F, -5.5578F, -8.941F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(10, 67).addBox(-2.5F, -5.5578F, -8.941F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 4.3705F, -0.1745F, 0.0F, 0.0F));

        PartDefinition bone2 = skirtpart_right.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(26, 67).addBox(-2.5F, -6.0F, -8.941F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.4422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition bone10 = skirtpart_right.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(26, 67).addBox(-1.0F, -6.0F, -8.941F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition skirt_front = bone.addOrReplaceChild("skirt_front", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition skirt1_front = skirt_front.addOrReplaceChild("skirt1_front", CubeListBuilder.create().texOffs(0, 67).addBox(-2.5F, -5.5578F, 0.0F, 5.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.4705F, 0.1745F, 0.0F, 0.0F));

        PartDefinition skirt2_front = skirt1_front.addOrReplaceChild("skirt2_front", CubeListBuilder.create().texOffs(20, 67).addBox(-2.0F, -6.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, 0.1719F));

        PartDefinition skirt3_front = skirt1_front.addOrReplaceChild("skirt3_front", CubeListBuilder.create().texOffs(20, 67).mirror().addBox(-0.95F, -6.0F, -0.001F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 0.3422F, 0.0F, 0.0F, 0.0F, -0.1719F));

        PartDefinition lace2 = skirt.addOrReplaceChild("lace2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -18.5F, 1.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition apron = skirt.addOrReplaceChild("apron", CubeListBuilder.create().texOffs(24, 56).addBox(-4.0F, -0.683F, -0.5335F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -11.0F, -2.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition legLeft = partdefinition.addOrReplaceChild("legLeft", CubeListBuilder.create().texOffs(12, 76).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(40, 57).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 15.0F, 0.0F));

        PartDefinition legRight = partdefinition.addOrReplaceChild("legRight", CubeListBuilder.create().texOffs(0, 76).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(40, 64).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 15.0F, 0.0F));

        PartDefinition wingRight = partdefinition.addOrReplaceChild("wingRight", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 7.0F, 3.5F, 0.0F, -1.2217F, 0.0F));

        PartDefinition wingRightUp = wingRight.addOrReplaceChild("wingRightUp", CubeListBuilder.create().texOffs(0, 7).addBox(0.9531F, -2.9277F, 0.1986F, 0.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.6109F, 0.0873F, 0.0F));

        PartDefinition wingRightDown = wingRight.addOrReplaceChild("wingRightDown", CubeListBuilder.create().texOffs(0, 20).addBox(0.0469F, -5.4633F, 0.7919F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.513F, 0.3563F, -0.1487F));

        PartDefinition wingLeft = partdefinition.addOrReplaceChild("wingLeft", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 7.0F, 3.5F, 0.0F, 1.2217F, 0.0F));

        PartDefinition wingLeftUp = wingLeft.addOrReplaceChild("wingLeftUp", CubeListBuilder.create().texOffs(0, 7).mirror().addBox(-0.9531F, -2.9277F, 0.1986F, 0.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.6109F, -0.0873F, 0.0F));

        PartDefinition wingLeftDown = wingLeft.addOrReplaceChild("wingLeftDown", CubeListBuilder.create().texOffs(0, 20).addBox(-0.0469F, -5.4633F, 0.7919F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, -0.513F, -0.3563F, 0.1487F));

        return LayerDefinition.create(meshdefinition, 128, 128);
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