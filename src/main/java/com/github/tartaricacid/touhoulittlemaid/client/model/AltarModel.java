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

public class AltarModel extends EntityModel<Entity> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "main"), "altar");
    private final ModelPart pillar;
    private final ModelPart pillar2;
    private final ModelPart pillar3;
    private final ModelPart pillar4;
    private final ModelPart pillar5;
    private final ModelPart pillar6;
    private final ModelPart bone;
    private final ModelPart bone56;

    public AltarModel(ModelPart root) {
        this.pillar = root.getChild("pillar");
        this.pillar2 = root.getChild("pillar2");
        this.pillar3 = root.getChild("pillar3");
        this.pillar4 = root.getChild("pillar4");
        this.pillar5 = root.getChild("pillar5");
        this.pillar6 = root.getChild("pillar6");
        this.bone = root.getChild("bone");
        this.bone56 = root.getChild("bone56");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition pillar = partdefinition.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(0, 44).addBox(48.0F, -48.0F, 16.0F, 16.0F, 48.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(47.5F, -48.5F, 15.5F, 17.0F, 6.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(47.5F, -33.5F, 15.5F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone2 = pillar.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(36.9617F, -42.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(40.9617F, -38.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(44.9617F, -34.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 14.8F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone3 = pillar.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(37.0311F, 38.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(33.0311F, 42.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(29.0311F, 46.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 14.8F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone4 = pillar.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(0, 0).addBox(36.9964F, -42.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(40.9964F, -38.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(44.9964F, -34.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 33.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone5 = pillar.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(36.9964F, 38.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(32.9964F, 42.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(28.9964F, 46.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 33.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone10 = pillar.addOrReplaceChild("bone10", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 24.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bone6 = bone10.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 0).addBox(-11.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.7243F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -9.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone7 = bone10.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(7.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(3.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-0.2757F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -9.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone8 = bone10.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(0, 0).addBox(7.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(15.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 9.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone9 = bone10.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-11.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-15.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-19.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 9.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition pillar2 = partdefinition.addOrReplaceChild("pillar2", CubeListBuilder.create().texOffs(0, 44).addBox(48.0F, -48.0F, -32.0F, 16.0F, 48.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(47.5F, -48.5F, -32.5F, 17.0F, 6.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(47.5F, -33.5F, -32.5F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone11 = pillar2.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(0, 0).addBox(36.9617F, -42.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(40.9617F, -38.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(44.9617F, -34.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -14.8F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone12 = pillar2.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(37.0311F, 38.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(33.0311F, 42.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(29.0311F, 46.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -14.8F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone13 = pillar2.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(0, 0).addBox(36.9964F, -42.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(40.9964F, -38.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(44.9964F, -34.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -33.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone14 = pillar2.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(36.9964F, 38.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(32.9964F, 42.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(28.9964F, 46.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -33.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone15 = pillar2.addOrReplaceChild("bone15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -24.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone16 = bone15.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(0, 0).addBox(-11.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.7243F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 9.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone17 = bone15.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(7.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(3.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-0.2757F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 9.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone18 = bone15.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(0, 0).addBox(7.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(15.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -9.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone19 = bone15.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-11.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-15.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-19.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -9.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition pillar3 = partdefinition.addOrReplaceChild("pillar3", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-64.0F, -48.0F, 16.0F, 16.0F, 48.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 21).mirror().addBox(-64.5F, -48.5F, 15.5F, 17.0F, 6.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-64.5F, -33.5F, 15.5F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone20 = pillar3.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-40.9617F, -42.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-44.9617F, -38.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-48.9617F, -34.2246F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 14.8F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone21 = pillar3.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(0, 0).addBox(-41.0311F, 38.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-37.0311F, 42.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-33.0311F, 46.1562F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 14.8F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone22 = pillar3.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-40.9964F, -42.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-44.9964F, -38.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-48.9964F, -34.1904F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 33.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone23 = pillar3.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(0, 0).addBox(-40.9964F, 38.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-36.9964F, 42.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-32.9964F, 46.1904F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 33.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone24 = pillar3.addOrReplaceChild("bone24", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 24.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone25 = bone24.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(7.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(3.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-0.2757F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -9.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone26 = bone24.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(0, 0).addBox(-11.7243F, -11.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.7243F, -7.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.7243F, -3.5766F, 54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -9.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone27 = bone24.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-11.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-15.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-19.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 9.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone28 = bone24.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(0, 0).addBox(7.7243F, 7.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.7243F, 11.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(15.7243F, 15.5766F, 54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 9.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition pillar4 = partdefinition.addOrReplaceChild("pillar4", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-64.0F, -48.0F, -32.0F, 16.0F, 48.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 21).mirror().addBox(-64.5F, -48.5F, -32.5F, 17.0F, 6.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-64.5F, -33.5F, -32.5F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone29 = pillar4.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-40.9617F, -42.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-44.9617F, -38.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-48.9617F, -34.2246F, -0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -14.8F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone30 = pillar4.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(0, 0).addBox(-41.0311F, 38.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-37.0311F, 42.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-33.0311F, 46.1562F, -0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -14.8F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone31 = pillar4.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-40.9964F, -42.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-44.9964F, -38.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-48.9964F, -34.1904F, 0.0955F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -33.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone32 = pillar4.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(0, 0).addBox(-40.9964F, 38.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-36.9964F, 42.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-32.9964F, 46.1904F, 0.3045F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -33.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone33 = pillar4.addOrReplaceChild("bone33", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -24.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bone34 = bone33.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(7.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(3.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-0.2757F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 9.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone35 = bone33.addOrReplaceChild("bone35", CubeListBuilder.create().texOffs(0, 0).addBox(-11.7243F, -11.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.7243F, -7.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.7243F, -3.5766F, -54.5114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 9.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone36 = bone33.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-11.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-15.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-19.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -9.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone37 = bone33.addOrReplaceChild("bone37", CubeListBuilder.create().texOffs(0, 0).addBox(7.7243F, 7.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(11.7243F, 11.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(15.7243F, 15.5766F, -54.1114F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -9.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition pillar5 = partdefinition.addOrReplaceChild("pillar5", CubeListBuilder.create().texOffs(0, 44).mirror().addBox(-32.0F, -48.0F, 48.0F, 16.0F, 48.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 21).mirror().addBox(-32.5F, -48.5F, 47.5F, 17.0F, 6.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-32.5F, -33.5F, 47.5F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone38 = pillar5.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-13.1213F, -24.731F, 31.1903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-17.1213F, -20.731F, 31.1903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-21.1213F, -16.731F, 31.1903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 14.8F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone39 = pillar5.addOrReplaceChild("bone39", CubeListBuilder.create().texOffs(0, 0).addBox(-24.3042F, 9.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-20.3042F, 13.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-16.3042F, 17.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 14.8F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone40 = pillar5.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-24.2695F, -13.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-28.2695F, -9.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-32.2695F, -5.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 33.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone41 = pillar5.addOrReplaceChild("bone41", CubeListBuilder.create().texOffs(0, 0).addBox(-13.156F, 20.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.156F, 24.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.156F, 28.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 33.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone42 = pillar5.addOrReplaceChild("bone42", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 24.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone43 = bone42.addOrReplaceChild("bone43", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(24.4512F, 16.8617F, 23.536F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(20.4512F, 20.8617F, 23.536F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(16.4512F, 24.8617F, 23.536F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -9.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone44 = bone42.addOrReplaceChild("bone44", CubeListBuilder.create().texOffs(0, 0).addBox(16.1161F, -29.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(20.1161F, -25.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(24.1161F, -21.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -9.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone45 = bone42.addOrReplaceChild("bone45", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(16.1161F, 25.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(12.1161F, 29.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(8.1161F, 33.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 9.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone46 = bone42.addOrReplaceChild("bone46", CubeListBuilder.create().texOffs(0, 0).addBox(24.4512F, -20.8617F, 23.136F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(28.4512F, -16.8617F, 23.136F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(32.4512F, -12.8617F, 23.136F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 9.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition pillar6 = partdefinition.addOrReplaceChild("pillar6", CubeListBuilder.create().texOffs(0, 44).addBox(16.0F, -48.0F, 48.0F, 16.0F, 48.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(15.5F, -48.5F, 47.5F, 17.0F, 6.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(15.5F, -33.5F, 47.5F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone47 = pillar6.addOrReplaceChild("bone47", CubeListBuilder.create().texOffs(0, 0).addBox(9.1213F, -24.731F, 31.1903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(13.1213F, -20.731F, 31.1903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(17.1213F, -16.731F, 31.1903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 14.8F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone48 = pillar6.addOrReplaceChild("bone48", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(20.3042F, 9.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(16.3042F, 13.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(12.3042F, 17.7179F, 31.2799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 14.8F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone49 = pillar6.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(0, 0).addBox(20.2695F, -13.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(24.2695F, -9.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(28.2695F, -5.7521F, 30.8799F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 33.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone50 = pillar6.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(9.156F, 20.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(5.156F, 24.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(1.156F, 28.6968F, 30.7903F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 33.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone51 = pillar6.addOrReplaceChild("bone51", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 24.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bone52 = bone51.addOrReplaceChild("bone52", CubeListBuilder.create().texOffs(0, 0).addBox(-28.4512F, 16.8617F, 23.536F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-24.4512F, 20.8617F, 23.536F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-20.4512F, 24.8617F, 23.536F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, -9.2F, -0.1745F, 0.1745F, 0.7854F));

        PartDefinition bone53 = bone51.addOrReplaceChild("bone53", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-20.1161F, -29.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-24.1161F, -25.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-28.1161F, -21.0702F, 23.4166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, -9.2F, -0.1745F, -0.1745F, -0.7854F));

        PartDefinition bone54 = bone51.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(0, 0).addBox(-20.1161F, 25.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-16.1161F, 29.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-12.1161F, 33.0702F, 23.0166F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -27.5F, 9.2F, 0.1745F, -0.1745F, 0.7854F));

        PartDefinition bone55 = bone51.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-28.4512F, -20.8617F, 23.136F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-32.4512F, -16.8617F, 23.136F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-36.4512F, -12.8617F, 23.136F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.25F, -27.5F, 9.2F, 0.1745F, 0.1745F, -0.7854F));

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(64, 39).addBox(-24.0F, -1.0F, -24.0F, 48.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone56 = partdefinition.addOrReplaceChild("bone56", CubeListBuilder.create().texOffs(150, 227).addBox(-32.0F, -12.0F, -64.0F, 16.0F, 12.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(150, 227).mirror().addBox(16.0F, -12.0F, -64.0F, 16.0F, 12.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 236).addBox(-60.0F, -101.0F, -58.0F, 120.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(2, 176).addBox(-54.5F, -95.0F, -64.0F, 109.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 186).addBox(-45.0F, -66.3F, -57.0F, 90.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 171).addBox(-32.0F, -42.0F, -64.0F, 16.0F, 30.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(192, 171).mirror().addBox(16.0F, -42.0F, -64.0F, 16.0F, 30.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(153, 188).mirror().addBox(16.5F, -55.0F, -63.5F, 15.0F, 13.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(153, 188).addBox(-31.5F, -55.0F, -63.5F, 15.0F, 13.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(49, 184).mirror().addBox(17.0F, -73.0F, -63.0F, 14.0F, 18.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(49, 184).addBox(-31.0F, -73.0F, -63.0F, 14.0F, 18.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(30, 173).mirror().addBox(17.5F, -85.0F, -62.5F, 13.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(30, 173).addBox(-30.5F, -85.0F, -62.5F, 13.0F, 12.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(0, 192).mirror().addBox(17.0F, -92.0F, -63.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 192).addBox(-31.0F, -92.0F, -63.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(28, 208).addBox(-47.0F, -68.3F, -59.0F, 94.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 180).addBox(-50.5F, -92.0F, -60.0F, 101.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone57 = bone56.addOrReplaceChild("bone57", CubeListBuilder.create().texOffs(0, 228).addBox(-55.5F, -1.0F, -14.5F, 111.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -98.0F, -56.5F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone58 = bone56.addOrReplaceChild("bone58", CubeListBuilder.create().texOffs(0, 222).addBox(-55.5F, -1.0F, 0.5F, 111.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -98.0F, -55.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone59 = bone56.addOrReplaceChild("bone59", CubeListBuilder.create().texOffs(224, 234).addBox(-6.0F, -20.0F, -1.0F, 12.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(230, 7).addBox(-4.5F, -18.5F, -1.7F, 9.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -67.3F, -59.25F, 0.0873F, 0.0F, 0.0F));

        PartDefinition bone60 = bone56.addOrReplaceChild("bone60", CubeListBuilder.create().texOffs(224, 234).addBox(-6.0F, -20.0F, -1.0F, 12.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(230, 7).addBox(-4.5F, -18.5F, -0.25F, 9.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -67.3F, -52.75F, -0.0873F, 0.0F, 0.0F));

        PartDefinition bone61 = bone56.addOrReplaceChild("bone61", CubeListBuilder.create().texOffs(16, 243).mirror().addBox(35.25F, -8.0F, -58.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(5, 178).mirror().addBox(35.25F, -27.0F, -58.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(5, 180).mirror().addBox(32.0F, -24.0F, -57.0F, 9.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(217, 178).mirror().addBox(32.0F, -31.0F, -58.5F, 12.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone63 = bone61.addOrReplaceChild("bone63", CubeListBuilder.create().texOffs(204, 236).mirror().addBox(-6.0F, -0.5F, -3.0F, 14.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(38.0F, -31.75F, -56.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition bone62 = bone56.addOrReplaceChild("bone62", CubeListBuilder.create().texOffs(16, 243).addBox(-39.25F, -8.0F, -58.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(5, 178).addBox(-39.25F, -27.0F, -58.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(5, 180).addBox(-41.0F, -24.0F, -57.0F, 9.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(217, 178).addBox(-44.0F, -31.0F, -58.5F, 12.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone64 = bone62.addOrReplaceChild("bone64", CubeListBuilder.create().texOffs(204, 236).addBox(-8.0F, -0.5F, -3.0F, 14.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-38.0F, -31.75F, -56.0F, 0.0F, 0.0F, -0.0873F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        pillar.render(poseStack, buffer, packedLight, packedOverlay);
        pillar2.render(poseStack, buffer, packedLight, packedOverlay);
        pillar3.render(poseStack, buffer, packedLight, packedOverlay);
        pillar4.render(poseStack, buffer, packedLight, packedOverlay);
        pillar5.render(poseStack, buffer, packedLight, packedOverlay);
        pillar6.render(poseStack, buffer, packedLight, packedOverlay);
        bone.render(poseStack, buffer, packedLight, packedOverlay);
        bone56.render(poseStack, buffer, packedLight, packedOverlay);
    }
}