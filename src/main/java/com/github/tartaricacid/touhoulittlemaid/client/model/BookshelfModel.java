package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BookshelfModel extends AbstractModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "bookshelf");
    private final ModelPart main;

    public BookshelfModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone = main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -42.0F, 23.0F, 32.0F, 41.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 58).addBox(-8.0F, -1.0F, 11.0F, 32.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(67, 28).addBox(0.0F, -1.0F, -23.25F, 20.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(67, 37).addBox(10.0F, -1.0F, -15.75F, 11.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(120, 130).addBox(17.0F, -27.0F, -14.25F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(122, 28).addBox(3.0F, -9.0F, -23.25F, 12.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(98, 37).addBox(0.0F, -5.0F, -23.25F, 15.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(67, 14).addBox(-7.0F, -29.0F, 11.0F, 30.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(67, 0).addBox(-7.0F, -15.0F, 11.0F, 30.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(27, 73).addBox(23.0F, -42.0F, 11.0F, 1.0F, 41.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 73).addBox(-8.0F, -42.0F, 11.0F, 1.0F, 41.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(-8.0F, -43.0F, 11.0F, 32.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 3.0F));

        PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(42, 73).addBox(-12.2744F, -13.2846F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.9651F, 15.1173F, -11.75F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(42, 73).addBox(-17.5879F, 5.3324F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(42, 73).addBox(-1.8041F, 18.2897F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone5 = bone4.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(42, 73).addBox(15.2832F, 7.2832F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone9 = bone.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.0F, 0.0F));

        PartDefinition bone6 = bone9.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(17.3974F, 1.4975F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.9651F, 15.1173F, -11.75F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone7 = bone6.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(5.2742F, 16.6461F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone8 = bone7.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-13.978F, 12.1668F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone10 = bone9.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-12.1668F, -14.7434F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.9651F, 11.8827F, -11.75F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone11 = bone10.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-17.3532F, 4.9813F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone12 = bone11.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-1.7321F, 16.3974F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone13 = bone12.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(15.7168F, 7.7168F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone14 = bone.addOrReplaceChild("bone14", CubeListBuilder.create(), PartPose.offset(-1.6259F, -15.2706F, -5.75F));

        PartDefinition bone15 = bone14.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(42, 73).addBox(-12.2744F, -13.2846F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.591F, 26.3879F, -6.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone16 = bone15.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(42, 73).addBox(-17.5879F, 5.3324F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone17 = bone16.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(42, 73).addBox(-1.8041F, 18.2897F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone18 = bone14.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(42, 73).addBox(17.2897F, 0.0387F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.591F, 23.1533F, -6.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone19 = bone18.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(42, 73).addBox(5.0395F, 16.295F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone20 = bone19.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(42, 73).addBox(-14.0499F, 10.2744F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone21 = bone20.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(42, 73).addBox(-16.7168F, -8.2832F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone22 = bone.addOrReplaceChild("bone22", CubeListBuilder.create(), PartPose.offset(5.6259F, -15.2706F, -5.75F));

        PartDefinition bone26 = bone22.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-12.1668F, -14.7434F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.591F, 23.1533F, -6.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone27 = bone26.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-17.3532F, 4.9813F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone28 = bone27.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(-1.7321F, 16.3974F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone29 = bone28.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(42, 73).mirror().addBox(15.7168F, 7.7168F, -11.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition bone23 = bone22.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(0, 43).addBox(18.2136F, -0.344F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3391F, 4.1533F, -3.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone24 = bone23.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(0, 43).addBox(5.7466F, 17.0021F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone25 = bone24.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(0, 43).addBox(-14.4326F, 11.1983F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone30 = bone25.addOrReplaceChild("bone30", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone31 = bone30.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(0, 43).addBox(0.9241F, 18.3795F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 1.9635F));

        PartDefinition bone32 = bone31.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(0, 43).addBox(-18.168F, 8.1939F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone33 = bone32.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(0, 43).addBox(-15.4466F, -14.2667F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone34 = bone33.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(0, 43).addBox(5.4218F, -18.9651F, -5.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition bone35 = bone34.addOrReplaceChild("bone35", CubeListBuilder.create().texOffs(123, 102).addBox(-0.5768F, -19.7702F, -13.53F, 3.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(15, 73).addBox(1.0181F, -19.4269F, -13.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(-0.2F))
                .texOffs(15, 73).addBox(-0.2319F, -19.4269F, -13.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-4.3391F, 0.9301F, 3.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bone202 = bone22.addOrReplaceChild("bone202", CubeListBuilder.create().texOffs(0, 127).addBox(15.0F, -11.4893F, -13.2676F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(141, 95).addBox(14.5F, -12.9893F, -14.7676F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(15.5F, -12.9893F, -13.7676F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(13.5F, -12.9893F, -13.7676F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(-4.1083F, 20.9945F, -6.7269F, 1.3963F, 0.0F, -1.5708F));

        PartDefinition bone203 = bone202.addOrReplaceChild("bone203", CubeListBuilder.create().texOffs(141, 67).addBox(38.2479F, -23.9662F, -63.8165F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(38.2479F, -23.9662F, -63.8165F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(-22.7479F, 10.9769F, 50.0489F));

        PartDefinition bone204 = bone203.addOrReplaceChild("bone204", CubeListBuilder.create().texOffs(89, 137).addBox(-44.2474F, -23.9662F, -62.1464F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-44.2474F, -23.9662F, -62.1464F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone205 = bone203.addOrReplaceChild("bone205", CubeListBuilder.create().texOffs(89, 137).addBox(-18.1704F, -23.9662F, -73.9654F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-18.1704F, -23.9662F, -73.9654F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone206 = bone203.addOrReplaceChild("bone206", CubeListBuilder.create().texOffs(89, 137).addBox(10.4446F, -23.9662F, -74.9054F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(10.4446F, -23.9662F, -74.9054F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone207 = bone202.addOrReplaceChild("bone207", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(40.7411F, -23.9662F, -63.8097F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(40.7411F, -23.9662F, -63.8097F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-27.2479F, 10.9769F, 50.0489F));

        PartDefinition bone208 = bone207.addOrReplaceChild("bone208", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(72.6169F, -23.9662F, 13.6041F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(72.6169F, -23.9662F, 13.6041F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone209 = bone207.addOrReplaceChild("bone209", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(73.1405F, -23.9662F, -15.9855F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(73.1405F, -23.9662F, -15.9855F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone210 = bone207.addOrReplaceChild("bone210", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(62.3008F, -23.9662F, -43.5232F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(62.3008F, -23.9662F, -43.5232F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone37 = bone.addOrReplaceChild("bone37", CubeListBuilder.create().texOffs(118, 46).addBox(8.9525F, -21.0F, -8.3296F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(99, 58).addBox(11.4525F, -22.5F, -8.8296F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(99, 58).addBox(7.4525F, -22.5F, -8.8296F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(25, 131).addBox(8.4525F, -22.5F, -9.8296F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(-21.25F, 11.0F, 15.75F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone38 = bone37.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(0, 148).addBox(8.9525F, -22.5F, -10.3296F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone41 = bone38.addOrReplaceChild("bone41", CubeListBuilder.create().texOffs(102, 147).addBox(-6.0428F, -22.5F, -14.6124F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone40 = bone38.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(102, 147).addBox(-1.0644F, -22.5F, -15.4294F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone39 = bone38.addOrReplaceChild("bone39", CubeListBuilder.create().texOffs(102, 147).addBox(3.8477F, -22.5F, -14.279F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone42 = bone37.addOrReplaceChild("bone42", CubeListBuilder.create().texOffs(0, 148).mirror().addBox(9.4457F, -22.5F, -10.3227F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone43 = bone42.addOrReplaceChild("bone43", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(11.2252F, -22.5F, 5.1595F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone44 = bone42.addOrReplaceChild("bone44", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(13.1903F, -22.5F, -0.2938F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone45 = bone42.addOrReplaceChild("bone45", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(12.919F, -22.5F, -6.084F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone127 = bone.addOrReplaceChild("bone127", CubeListBuilder.create(), PartPose.offset(-13.0F, 11.0F, 24.5F));

        PartDefinition bone100 = bone127.addOrReplaceChild("bone100", CubeListBuilder.create().texOffs(99, 58).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(99, 58).addBox(5.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(25, 131).addBox(6.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(118, 46).addBox(6.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(99, 58).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(99, 58).addBox(5.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(25, 131).addBox(6.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(22.0F, 0.0F, 0.0F));

        PartDefinition bone101 = bone100.addOrReplaceChild("bone101", CubeListBuilder.create().texOffs(0, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(0, 148).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone102 = bone101.addOrReplaceChild("bone102", CubeListBuilder.create().texOffs(102, 147).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(102, 147).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone103 = bone101.addOrReplaceChild("bone103", CubeListBuilder.create().texOffs(102, 147).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(102, 147).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone104 = bone101.addOrReplaceChild("bone104", CubeListBuilder.create().texOffs(102, 147).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(102, 147).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone105 = bone100.addOrReplaceChild("bone105", CubeListBuilder.create().texOffs(0, 148).mirror().addBox(6.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(0, 148).mirror().addBox(6.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone106 = bone105.addOrReplaceChild("bone106", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(13.6777F, -23.5F, 1.4891F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(102, 147).mirror().addBox(13.6777F, -23.5F, 1.4891F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone107 = bone105.addOrReplaceChild("bone107", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(14.0515F, -23.5F, -4.6233F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(102, 147).mirror().addBox(14.0515F, -23.5F, -4.6233F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone108 = bone105.addOrReplaceChild("bone108", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(12.0578F, -23.5F, -10.4135F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(102, 147).mirror().addBox(12.0578F, -23.5F, -10.4135F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone109 = bone127.addOrReplaceChild("bone109", CubeListBuilder.create().texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(124, 74).addBox(0.3854F, -23.7674F, -12.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(15.75F, -0.5F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bone110 = bone109.addOrReplaceChild("bone110", CubeListBuilder.create().texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone111 = bone110.addOrReplaceChild("bone111", CubeListBuilder.create().texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone112 = bone110.addOrReplaceChild("bone112", CubeListBuilder.create().texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone113 = bone110.addOrReplaceChild("bone113", CubeListBuilder.create().texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone114 = bone109.addOrReplaceChild("bone114", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone115 = bone114.addOrReplaceChild("bone115", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone116 = bone114.addOrReplaceChild("bone116", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone117 = bone114.addOrReplaceChild("bone117", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone118 = bone127.addOrReplaceChild("bone118", CubeListBuilder.create().texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(1.8854F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-1.1146F, -25.2674F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(-0.1146F, -25.2674F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(15.75F, -0.5F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bone119 = bone118.addOrReplaceChild("bone119", CubeListBuilder.create().texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(-0.6146F, -25.2674F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone120 = bone119.addOrReplaceChild("bone120", CubeListBuilder.create().texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-13.095F, -25.2674F, -7.1782F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone121 = bone119.addOrReplaceChild("bone121", CubeListBuilder.create().texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.4247F, -25.2674F, -11.2598F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone122 = bone119.addOrReplaceChild("bone122", CubeListBuilder.create().texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-6.3957F, -25.2674F, -14.0089F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone123 = bone118.addOrReplaceChild("bone123", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(0.8786F, -25.2674F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone124 = bone123.addOrReplaceChild("bone124", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.3377F, -25.2674F, -4.16F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone125 = bone123.addOrReplaceChild("bone125", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(9.7279F, -25.2674F, -8.947F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone126 = bone123.addOrReplaceChild("bone126", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(6.4086F, -25.2674F, -12.7535F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone91 = bone127.addOrReplaceChild("bone91", CubeListBuilder.create().texOffs(54, 90).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(6.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(7.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(124, 74).addBox(7.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(9.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(6.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(7.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(25.5F, 0.0F, 0.0F));

        PartDefinition bone92 = bone91.addOrReplaceChild("bone92", CubeListBuilder.create().texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone93 = bone92.addOrReplaceChild("bone93", CubeListBuilder.create().texOffs(89, 137).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.3723F, -23.5F, -13.7512F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone94 = bone92.addOrReplaceChild("bone94", CubeListBuilder.create().texOffs(89, 137).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-5.3939F, -23.5F, -16.2906F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone95 = bone92.addOrReplaceChild("bone95", CubeListBuilder.create().texOffs(89, 137).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(0.1773F, -23.5F, -16.7315F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone96 = bone91.addOrReplaceChild("bone96", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(7.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(7.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone97 = bone96.addOrReplaceChild("bone97", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(14.0604F, -23.5F, 2.413F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(14.0604F, -23.5F, 2.413F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone98 = bone96.addOrReplaceChild("bone98", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(14.7586F, -23.5F, -3.9162F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(14.7586F, -23.5F, -3.9162F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone99 = bone96.addOrReplaceChild("bone99", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(12.9817F, -23.5F, -10.0308F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(12.9817F, -23.5F, -10.0308F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone193 = bone127.addOrReplaceChild("bone193", CubeListBuilder.create().texOffs(0, 127).addBox(-17.0F, -15.2537F, 0.2814F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(-16.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 95).addBox(-17.5F, -16.7537F, -1.2186F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-16.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -16.7537F, -0.2186F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(5.3673F, 3.0761F, -2.5F, 1.2217F, 0.0F, 1.5708F));

        PartDefinition bone194 = bone193.addOrReplaceChild("bone194", CubeListBuilder.create().texOffs(141, 67).addBox(9.4979F, -5.1576F, -11.8252F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(9.4979F, -5.1576F, -11.8252F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(-25.9979F, -11.5961F, 11.6066F));

        PartDefinition bone195 = bone194.addOrReplaceChild("bone195", CubeListBuilder.create().texOffs(89, 137).addBox(-7.2158F, -5.1576F, -15.6887F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-7.2158F, -5.1576F, -15.6887F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone196 = bone194.addOrReplaceChild("bone196", CubeListBuilder.create().texOffs(89, 137).addBox(-1.7363F, -5.1576F, -16.8726F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-1.7363F, -5.1576F, -16.8726F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone197 = bone194.addOrReplaceChild("bone197", CubeListBuilder.create().texOffs(89, 137).addBox(3.7793F, -5.1576F, -15.8695F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(3.7793F, -5.1576F, -15.8695F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone198 = bone193.addOrReplaceChild("bone198", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(11.9911F, -5.1576F, -11.8184F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(11.9911F, -5.1576F, -11.8184F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-30.4979F, -11.5961F, 11.6066F));

        PartDefinition bone199 = bone198.addOrReplaceChild("bone199", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.5811F, -5.1576F, 6.9388F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.5811F, -5.1576F, 6.9388F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone200 = bone198.addOrReplaceChild("bone200", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(16.0478F, -5.1576F, 0.4485F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(16.0478F, -5.1576F, 0.4485F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone201 = bone198.addOrReplaceChild("bone201", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(15.843F, -5.1576F, -6.4917F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(15.843F, -5.1576F, -6.4917F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone73 = bone127.addOrReplaceChild("bone73", CubeListBuilder.create().texOffs(0, 127).addBox(-17.0F, -10.6569F, 5.298F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(-16.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 95).addBox(-17.5F, -12.1569F, 3.798F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-16.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -12.1569F, 4.798F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(5.3673F, 0.5761F, -0.5F, 1.8326F, 0.0F, 1.5708F));

        PartDefinition bone74 = bone73.addOrReplaceChild("bone74", CubeListBuilder.create().texOffs(141, 67).addBox(9.4979F, -0.5607F, -6.8086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(9.4979F, -0.5607F, -6.8086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(-25.9979F, -11.5961F, 11.6066F));

        PartDefinition bone75 = bone74.addOrReplaceChild("bone75", CubeListBuilder.create().texOffs(89, 137).addBox(-2.5811F, -0.5607F, -13.7689F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-2.5811F, -0.5607F, -13.7689F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone76 = bone74.addOrReplaceChild("bone76", CubeListBuilder.create().texOffs(89, 137).addBox(1.811F, -0.5607F, -13.3254F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(1.811F, -0.5607F, -13.3254F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone77 = bone74.addOrReplaceChild("bone77", CubeListBuilder.create().texOffs(89, 137).addBox(5.699F, -0.5607F, -11.2348F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(5.699F, -0.5607F, -11.2348F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone78 = bone73.addOrReplaceChild("bone78", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(11.9911F, -0.5607F, -6.8018F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(11.9911F, -0.5607F, -6.8018F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-30.4979F, -11.5961F, 11.6066F));

        PartDefinition bone79 = bone78.addOrReplaceChild("bone79", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(8.9463F, -0.5607F, 8.8586F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(8.9463F, -0.5607F, 8.8586F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone80 = bone78.addOrReplaceChild("bone80", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(12.5005F, -0.5607F, 3.9958F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(12.5005F, -0.5607F, 3.9958F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone81 = bone78.addOrReplaceChild("bone81", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.9232F, -0.5607F, -1.857F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.9232F, -0.5607F, -1.857F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone82 = bone127.addOrReplaceChild("bone82", CubeListBuilder.create().texOffs(0, 127).addBox(-17.0F, -10.2241F, -14.5354F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(-16.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 95).addBox(-17.5F, -11.7241F, -16.0354F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-16.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -11.7241F, -15.0354F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(5.3673F, -1.9239F, -0.5F, -0.3054F, 0.0F, 1.5708F));

        PartDefinition bone83 = bone82.addOrReplaceChild("bone83", CubeListBuilder.create().texOffs(141, 67).addBox(9.4979F, -0.1279F, -26.642F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(9.4979F, -0.1279F, -26.642F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(-25.9979F, -11.5961F, 11.6066F));

        PartDefinition bone84 = bone83.addOrReplaceChild("bone84", CubeListBuilder.create().texOffs(89, 137).addBox(-20.9047F, -0.1279F, -21.3588F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-20.9047F, -0.1279F, -21.3588F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone85 = bone83.addOrReplaceChild("bone85", CubeListBuilder.create().texOffs(89, 137).addBox(-12.2133F, -0.1279F, -27.3497F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-12.2133F, -0.1279F, -27.3497F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone86 = bone83.addOrReplaceChild("bone86", CubeListBuilder.create().texOffs(89, 137).addBox(-1.8909F, -0.1279F, -29.5584F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-1.8909F, -0.1279F, -29.5584F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone87 = bone82.addOrReplaceChild("bone87", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(11.9911F, -0.1279F, -26.6352F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(11.9911F, -0.1279F, -26.6352F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-30.4979F, -11.5961F, 11.6066F));

        PartDefinition bone88 = bone87.addOrReplaceChild("bone88", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(27.27F, -0.1279F, 1.2687F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(27.27F, -0.1279F, 1.2687F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone89 = bone87.addOrReplaceChild("bone89", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(26.5248F, -0.1279F, -10.0285F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(26.5248F, -0.1279F, -10.0285F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone90 = bone87.addOrReplaceChild("bone90", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(21.5131F, -0.1279F, -20.1806F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(21.5131F, -0.1279F, -20.1806F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone128 = bone.addOrReplaceChild("bone128", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone129 = bone128.addOrReplaceChild("bone129", CubeListBuilder.create().texOffs(54, 90).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(103, 130).mirror().addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(74, 116).mirror().addBox(5.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(103, 130).mirror().addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(13.0F, -3.0F, 24.5F));

        PartDefinition bone130 = bone129.addOrReplaceChild("bone130", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone131 = bone130.addOrReplaceChild("bone131", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone132 = bone130.addOrReplaceChild("bone132", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone133 = bone130.addOrReplaceChild("bone133", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone134 = bone129.addOrReplaceChild("bone134", CubeListBuilder.create().texOffs(141, 67).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone135 = bone134.addOrReplaceChild("bone135", CubeListBuilder.create().texOffs(89, 137).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone136 = bone134.addOrReplaceChild("bone136", CubeListBuilder.create().texOffs(89, 137).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone137 = bone134.addOrReplaceChild("bone137", CubeListBuilder.create().texOffs(89, 137).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone138 = bone128.addOrReplaceChild("bone138", CubeListBuilder.create().texOffs(118, 46).mirror().addBox(5.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(104, 104).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(104, 104).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(42, 134).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(104, 104).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(104, 104).addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(42, 134).addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(4.0F, -3.0F, 24.5F));

        PartDefinition bone139 = bone138.addOrReplaceChild("bone139", CubeListBuilder.create().texOffs(13, 148).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(13, 148).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone140 = bone139.addOrReplaceChild("bone140", CubeListBuilder.create().texOffs(125, 141).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(125, 141).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone141 = bone139.addOrReplaceChild("bone141", CubeListBuilder.create().texOffs(125, 141).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(125, 141).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone142 = bone139.addOrReplaceChild("bone142", CubeListBuilder.create().texOffs(125, 141).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(125, 141).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone143 = bone138.addOrReplaceChild("bone143", CubeListBuilder.create().texOffs(13, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(13, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone144 = bone143.addOrReplaceChild("bone144", CubeListBuilder.create().texOffs(125, 141).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(125, 141).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone145 = bone143.addOrReplaceChild("bone145", CubeListBuilder.create().texOffs(125, 141).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(125, 141).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone146 = bone143.addOrReplaceChild("bone146", CubeListBuilder.create().texOffs(125, 141).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(125, 141).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone147 = bone128.addOrReplaceChild("bone147", CubeListBuilder.create().texOffs(99, 58).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(99, 58).mirror().addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(25, 131).mirror().addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(118, 46).mirror().addBox(5.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(99, 58).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(99, 58).mirror().addBox(8.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(25, 131).mirror().addBox(5.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-9.0F, -3.0F, 24.5F));

        PartDefinition bone148 = bone147.addOrReplaceChild("bone148", CubeListBuilder.create().texOffs(0, 148).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(0, 148).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone149 = bone148.addOrReplaceChild("bone149", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(102, 147).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone150 = bone148.addOrReplaceChild("bone150", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(102, 147).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone151 = bone148.addOrReplaceChild("bone151", CubeListBuilder.create().texOffs(102, 147).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(102, 147).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone152 = bone147.addOrReplaceChild("bone152", CubeListBuilder.create().texOffs(0, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(0, 148).addBox(6.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone153 = bone152.addOrReplaceChild("bone153", CubeListBuilder.create().texOffs(102, 147).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(102, 147).addBox(-10.5547F, -23.5F, -13.293F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone154 = bone152.addOrReplaceChild("bone154", CubeListBuilder.create().texOffs(102, 147).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(102, 147).addBox(-5.7378F, -23.5F, -15.937F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone155 = bone152.addOrReplaceChild("bone155", CubeListBuilder.create().texOffs(102, 147).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(102, 147).addBox(-0.2757F, -23.5F, -16.5365F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone156 = bone128.addOrReplaceChild("bone156", CubeListBuilder.create().texOffs(54, 90).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(124, 74).mirror().addBox(5.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offset(9.0F, -3.0F, 24.5F));

        PartDefinition bone157 = bone156.addOrReplaceChild("bone157", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone158 = bone157.addOrReplaceChild("bone158", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone159 = bone157.addOrReplaceChild("bone159", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone160 = bone157.addOrReplaceChild("bone160", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone161 = bone156.addOrReplaceChild("bone161", CubeListBuilder.create().texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone162 = bone161.addOrReplaceChild("bone162", CubeListBuilder.create().texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone163 = bone161.addOrReplaceChild("bone163", CubeListBuilder.create().texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone164 = bone161.addOrReplaceChild("bone164", CubeListBuilder.create().texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone165 = bone128.addOrReplaceChild("bone165", CubeListBuilder.create().texOffs(54, 90).mirror().addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(-2.75F, -3.5F, 24.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bone166 = bone165.addOrReplaceChild("bone166", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone167 = bone166.addOrReplaceChild("bone167", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone168 = bone166.addOrReplaceChild("bone168", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone169 = bone166.addOrReplaceChild("bone169", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone170 = bone165.addOrReplaceChild("bone170", CubeListBuilder.create().texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone171 = bone170.addOrReplaceChild("bone171", CubeListBuilder.create().texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone172 = bone170.addOrReplaceChild("bone172", CubeListBuilder.create().texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone173 = bone170.addOrReplaceChild("bone173", CubeListBuilder.create().texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone174 = bone128.addOrReplaceChild("bone174", CubeListBuilder.create().texOffs(54, 90).mirror().addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(124, 74).mirror().addBox(11.3967F, -17.6445F, -12.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(9.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(12.8967F, -19.1445F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(10.8967F, -19.1445F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(-2.75F, -3.5F, 24.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition bone175 = bone174.addOrReplaceChild("bone175", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(12.3967F, -19.1445F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone176 = bone175.addOrReplaceChild("bone176", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(15.7518F, -19.1445F, 6.4787F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone177 = bone175.addOrReplaceChild("bone177", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(17.8772F, -19.1445F, -0.8073F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone178 = bone175.addOrReplaceChild("bone178", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(17.0526F, -19.1445F, -8.352F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone179 = bone174.addOrReplaceChild("bone179", CubeListBuilder.create().texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(10.9035F, -19.1445F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone180 = bone179.addOrReplaceChild("bone180", CubeListBuilder.create().texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-8.6809F, -19.1445F, -17.8169F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone181 = bone179.addOrReplaceChild("bone181", CubeListBuilder.create().texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-2.2754F, -19.1445F, -19.3995F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone182 = bone179.addOrReplaceChild("bone182", CubeListBuilder.create().texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(4.2482F, -19.1445F, -18.4103F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone183 = bone128.addOrReplaceChild("bone183", CubeListBuilder.create().texOffs(54, 90).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(124, 74).mirror().addBox(5.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(54, 90).mirror().addBox(7.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(59, 134).mirror().addBox(5.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-12.5F, -3.0F, 24.5F));

        PartDefinition bone184 = bone183.addOrReplaceChild("bone184", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(6.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.5F, 0.0F, 1.5F));

        PartDefinition bone185 = bone184.addOrReplaceChild("bone185", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.4953F, -23.5F, 1.0309F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone186 = bone184.addOrReplaceChild("bone186", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(13.7076F, -23.5F, -4.9769F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone187 = bone184.addOrReplaceChild("bone187", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.6048F, -23.5F, -10.6086F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone188 = bone183.addOrReplaceChild("bone188", CubeListBuilder.create().texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(5.0068F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.0F, 0.0F, 1.5F));

        PartDefinition bone189 = bone188.addOrReplaceChild("bone189", CubeListBuilder.create().texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.9374F, -23.5F, -12.3691F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone190 = bone188.addOrReplaceChild("bone190", CubeListBuilder.create().texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-6.4449F, -23.5F, -15.2299F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone191 = bone188.addOrReplaceChild("bone191", CubeListBuilder.create().texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-1.1996F, -23.5F, -16.1538F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone192 = bone.addOrReplaceChild("bone192", CubeListBuilder.create(), PartPose.offset(-22.0F, -17.0F, 24.5F));

        PartDefinition bone211 = bone192.addOrReplaceChild("bone211", CubeListBuilder.create().texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(0.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(103, 130).addBox(1.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(74, 116).addBox(1.5F, -22.0F, -12.0F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(0.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(103, 130).addBox(1.0F, -23.5F, -13.5F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(20.9F, 0.0F, 0.0F));

        PartDefinition bone212 = bone211.addOrReplaceChild("bone212", CubeListBuilder.create().texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone213 = bone212.addOrReplaceChild("bone213", CubeListBuilder.create().texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone214 = bone212.addOrReplaceChild("bone214", CubeListBuilder.create().texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone215 = bone212.addOrReplaceChild("bone215", CubeListBuilder.create().texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone216 = bone211.addOrReplaceChild("bone216", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(1.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(1.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone217 = bone216.addOrReplaceChild("bone217", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.7643F, -23.5F, -3.1303F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.7643F, -23.5F, -3.1303F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone218 = bone216.addOrReplaceChild("bone218", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(10.516F, -23.5F, -8.1589F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(10.516F, -23.5F, -8.1589F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone219 = bone216.addOrReplaceChild("bone219", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(7.4384F, -23.5F, -12.3269F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(7.4384F, -23.5F, -12.3269F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone220 = bone192.addOrReplaceChild("bone220", CubeListBuilder.create().texOffs(85, 90).addBox(-15.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(85, 90).addBox(-18.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(74, 137).addBox(-17.0F, -10.9021F, -16.2901F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(85, 90).addBox(-15.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(85, 90).addBox(-18.0F, -10.9021F, -15.2901F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(74, 137).addBox(-17.0F, -10.9021F, -16.2901F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(125, 120).addBox(-16.5F, -9.4021F, -14.7901F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(29.0F, 1.25F, -1.75F, -0.48F, 0.0F, 1.5708F));

        PartDefinition bone221 = bone220.addOrReplaceChild("bone221", CubeListBuilder.create().texOffs(143, 37).addBox(-17.5F, -10.9021F, -16.7901F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(143, 37).addBox(-17.5F, -10.9021F, -16.7901F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone222 = bone221.addOrReplaceChild("bone222", CubeListBuilder.create().texOffs(138, 141).addBox(-22.1344F, -10.9021F, 7.3542F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(138, 141).addBox(-22.1344F, -10.9021F, 7.3542F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone223 = bone221.addOrReplaceChild("bone223", CubeListBuilder.create().texOffs(138, 141).addBox(-24.3374F, -10.9021F, -1.2929F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(138, 141).addBox(-24.3374F, -10.9021F, -1.2929F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone224 = bone221.addOrReplaceChild("bone224", CubeListBuilder.create().texOffs(138, 141).addBox(-23.0635F, -10.9021F, -10.1248F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(138, 141).addBox(-23.0635F, -10.9021F, -10.1248F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone225 = bone220.addOrReplaceChild("bone225", CubeListBuilder.create().texOffs(143, 37).mirror().addBox(-16.0068F, -10.9021F, -16.7832F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(143, 37).mirror().addBox(-16.0068F, -10.9021F, -16.7832F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone226 = bone225.addOrReplaceChild("bone226", CubeListBuilder.create().texOffs(138, 141).mirror().addBox(7.4536F, -10.9021F, -20.8279F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(138, 141).mirror().addBox(7.4536F, -10.9021F, -20.8279F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone227 = bone225.addOrReplaceChild("bone227", CubeListBuilder.create().texOffs(138, 141).mirror().addBox(-0.2391F, -10.9021F, -22.8597F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(138, 141).mirror().addBox(-0.2391F, -10.9021F, -22.8597F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone228 = bone225.addOrReplaceChild("bone228", CubeListBuilder.create().texOffs(138, 141).mirror().addBox(-8.1237F, -10.9021F, -21.7929F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(138, 141).mirror().addBox(-8.1237F, -10.9021F, -21.7929F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone238 = bone192.addOrReplaceChild("bone238", CubeListBuilder.create().texOffs(0, 127).addBox(-4.3513F, -20.9301F, -12.5F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(-3.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-5.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 95).addBox(-4.8513F, -22.4301F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-3.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-5.8513F, -22.4301F, -13.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(15.8673F, -1.4239F, 0.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition bone239 = bone238.addOrReplaceChild("bone239", CubeListBuilder.create().texOffs(141, 67).addBox(-5.234F, -23.354F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(-5.234F, -23.354F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(1.3827F, 0.9239F, 1.0F));

        PartDefinition bone240 = bone239.addOrReplaceChild("bone240", CubeListBuilder.create().texOffs(89, 137).addBox(-14.8627F, -23.354F, -2.9104F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-14.8627F, -23.354F, -2.9104F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone241 = bone239.addOrReplaceChild("bone241", CubeListBuilder.create().texOffs(89, 137).addBox(-13.6911F, -23.354F, -7.9934F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-13.6911F, -23.354F, -7.9934F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone242 = bone239.addOrReplaceChild("bone242", CubeListBuilder.create().texOffs(89, 137).addBox(-10.6635F, -23.354F, -12.2411F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-10.6635F, -23.354F, -12.2411F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone243 = bone238.addOrReplaceChild("bone243", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(-2.7408F, -23.354F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(-2.7408F, -23.354F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-3.1173F, 0.9239F, 1.0F));

        PartDefinition bone244 = bone243.addOrReplaceChild("bone244", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(9.9526F, -23.354F, -7.5039F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(9.9526F, -23.354F, -7.5039F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone245 = bone243.addOrReplaceChild("bone245", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(7.1686F, -23.354F, -11.5063F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(7.1686F, -23.354F, -11.5063F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone246 = bone243.addOrReplaceChild("bone246", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(3.0648F, -23.354F, -14.1386F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(3.0648F, -23.354F, -14.1386F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone46 = bone192.addOrReplaceChild("bone46", CubeListBuilder.create().texOffs(0, 127).addBox(-17.0F, -16.3137F, -4.5F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(-16.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 95).addBox(-17.5F, -17.8137F, -6.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-16.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-18.5F, -17.8137F, -5.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(29.3673F, -0.9239F, -2.5F, 0.7854F, 0.0F, 1.5708F));

        PartDefinition bone47 = bone46.addOrReplaceChild("bone47", CubeListBuilder.create().texOffs(141, 67).addBox(-22.5021F, -16.8242F, -6.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(-22.5021F, -16.8242F, -6.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(6.0021F, -0.9895F, 1.0F));

        PartDefinition bone48 = bone47.addOrReplaceChild("bone48", CubeListBuilder.create().texOffs(89, 137).addBox(-14.0799F, -16.8242F, 16.1047F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-14.0799F, -16.8242F, 16.1047F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone49 = bone47.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(89, 137).addBox(-20.2446F, -16.8242F, 9.8738F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-20.2446F, -16.8242F, 9.8738F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone50 = bone47.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(89, 137).addBox(-23.5557F, -16.8242F, 1.7582F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-23.5557F, -16.8242F, 1.7582F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone51 = bone46.addOrReplaceChild("bone51", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(-20.0089F, -16.8242F, -5.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(-20.0089F, -16.8242F, -5.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(1.5021F, -0.9895F, 1.0F));

        PartDefinition bone52 = bone51.addOrReplaceChild("bone52", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(-4.0466F, -16.8242F, -20.3961F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(-4.0466F, -16.8242F, -20.3961F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone53 = bone51.addOrReplaceChild("bone53", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(-10.6987F, -16.8242F, -18.0598F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(-10.6987F, -16.8242F, -18.0598F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone54 = bone51.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(-15.9504F, -16.8242F, -13.3557F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(-15.9504F, -16.8242F, -13.3557F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone247 = bone192.addOrReplaceChild("bone247", CubeListBuilder.create().texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(1.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(2.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(124, 74).addBox(2.5F, -22.0F, -12.0F, 3.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(4.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(1.0F, -23.5F, -12.5F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(59, 134).addBox(2.0F, -23.5F, -13.5F, 4.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(24.5F, 0.0F, 0.0F));

        PartDefinition bone248 = bone247.addOrReplaceChild("bone248", CubeListBuilder.create().texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 67).addBox(1.5F, -23.5F, -14.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone249 = bone248.addOrReplaceChild("bone249", CubeListBuilder.create().texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-12.2857F, -23.5F, -9.1318F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone250 = bone248.addOrReplaceChild("bone250", CubeListBuilder.create().texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-8.9295F, -23.5F, -12.7551F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone251 = bone248.addOrReplaceChild("bone251", CubeListBuilder.create().texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(89, 137).addBox(-4.4421F, -23.5F, -14.8181F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone252 = bone247.addOrReplaceChild("bone252", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(2.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(141, 67).mirror().addBox(2.9932F, -23.5F, -13.9932F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone253 = bone252.addOrReplaceChild("bone253", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(12.1469F, -23.5F, -2.2064F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(12.1469F, -23.5F, -2.2064F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone254 = bone252.addOrReplaceChild("bone254", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(11.2231F, -23.5F, -7.4518F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(11.2231F, -23.5F, -7.4518F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone255 = bone252.addOrReplaceChild("bone255", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(8.3623F, -23.5F, -11.9443F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false)
                .texOffs(89, 137).mirror().addBox(8.3623F, -23.5F, -11.9443F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone55 = bone.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(74, 116).addBox(-17.5F, -16.2707F, -3.0139F, 4.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(-15.0F, -17.7707F, -3.5139F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(-19.0F, -17.7707F, -3.5139F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(103, 130).addBox(-18.0F, -17.7707F, -4.5139F, 5.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(-29.25F, 13.5F, 23.0F, 0.8727F, 0.0F, 1.5708F));

        PartDefinition bone56 = bone55.addOrReplaceChild("bone56", CubeListBuilder.create().texOffs(141, 67).addBox(-17.5F, -17.7707F, -5.0139F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone57 = bone56.addOrReplaceChild("bone57", CubeListBuilder.create().texOffs(89, 137).addBox(-11.2547F, -17.7707F, 11.8607F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone58 = bone56.addOrReplaceChild("bone58", CubeListBuilder.create().texOffs(89, 137).addBox(-16.0104F, -17.7707F, 7.0341F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone59 = bone56.addOrReplaceChild("bone59", CubeListBuilder.create().texOffs(89, 137).addBox(-18.557F, -17.7707F, 0.7549F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone60 = bone55.addOrReplaceChild("bone60", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(-17.0068F, -17.7707F, -5.0071F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone61 = bone60.addOrReplaceChild("bone61", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(-3.8088F, -17.7707F, -17.2452F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone62 = bone60.addOrReplaceChild("bone62", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(-9.2731F, -17.7707F, -15.2398F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone63 = bone60.addOrReplaceChild("bone63", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(-13.5541F, -17.7707F, -11.2959F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone64 = bone.addOrReplaceChild("bone64", CubeListBuilder.create().texOffs(0, 127).addBox(16.5F, 5.9282F, -6.9282F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(54, 90).addBox(17.0F, 4.4282F, -7.4282F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(54, 90).addBox(15.0F, 4.4282F, -7.4282F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F))
                .texOffs(141, 95).addBox(16.0F, 4.4282F, -8.4282F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(-28.25F, 12.0F, 23.0F, -0.5236F, 0.0F, -1.5708F));

        PartDefinition bone65 = bone64.addOrReplaceChild("bone65", CubeListBuilder.create().texOffs(141, 67).addBox(14.5F, 4.4282F, -8.9282F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offset(2.5F, 0.0F, 1.5F));

        PartDefinition bone66 = bone65.addOrReplaceChild("bone66", CubeListBuilder.create().texOffs(89, 137).addBox(-2.6251F, 4.4282F, -19.2014F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone67 = bone65.addOrReplaceChild("bone67", CubeListBuilder.create().texOffs(89, 137).addBox(3.8492F, 4.4282F, -18.3611F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone68 = bone65.addOrReplaceChild("bone68", CubeListBuilder.create().texOffs(89, 137).addBox(9.5092F, 4.4282F, -15.1072F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone69 = bone64.addOrReplaceChild("bone69", CubeListBuilder.create().texOffs(141, 67).mirror().addBox(16.9932F, 4.4282F, -8.9214F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offset(-2.0F, 0.0F, 1.5F));

        PartDefinition bone70 = bone69.addOrReplaceChild("bone70", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(12.8188F, 4.4282F, 12.6688F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.1781F, 0.0F));

        PartDefinition bone71 = bone69.addOrReplaceChild("bone71", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(17.5363F, 4.4282F, 6.034F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone72 = bone69.addOrReplaceChild("bone72", CubeListBuilder.create().texOffs(89, 137).mirror().addBox(19.3557F, 4.4282F, -1.901F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition bone36 = main.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(54, 73).addBox(6.0F, -5.0F, -10.25F, 13.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(104, 84).addBox(5.0F, -2.0F, -11.25F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(98, 51).addBox(7.0F, -2.0F, -11.25F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(98, 46).addBox(7.0F, -2.0F, 1.75F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(39, 116).addBox(18.0F, -2.0F, -11.25F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.5F, 0.0F, 3.75F));

        PartDefinition bone229 = bone36.addOrReplaceChild("bone229", CubeListBuilder.create().texOffs(78, 45).addBox(3.5016F, -30.8187F, -13.675F, 3.0F, 11.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4014F, 13.8604F, 3.425F, 0.0F, 0.0F, 0.3927F));

        PartDefinition book = main.addOrReplaceChild("book", CubeListBuilder.create(), PartPose.offsetAndRotation(-13.25F, -13.25F, 2.0F, 0.2134F, -0.002F, 0.8411F));

        PartDefinition bone230 = book.addOrReplaceChild("bone230", CubeListBuilder.create().texOffs(188, 27).addBox(-1.0F, -5.0F, -4.5F, 2.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F))
                .texOffs(201, 65).addBox(-1.5F, -6.5F, -6.0F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F))
                .texOffs(221, 0).addBox(-0.5F, -6.5F, -5.0F, 3.0F, 13.0F, 12.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(-0.6327F, 0.0761F, 2.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition bone231 = bone230.addOrReplaceChild("bone231", CubeListBuilder.create(), PartPose.offset(-25.9979F, -11.5961F, 11.6066F));

        PartDefinition bone232 = bone231.addOrReplaceChild("bone232", CubeListBuilder.create().texOffs(231, 48).addBox(-5.5103F, 5.0961F, -32.3005F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        PartDefinition bone233 = bone231.addOrReplaceChild("bone233", CubeListBuilder.create().texOffs(214, 65).addBox(16.7316F, 5.0961F, -26.4099F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition bone234 = book.addOrReplaceChild("bone234", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.6327F, 0.0761F, 2.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition bone235 = bone234.addOrReplaceChild("bone235", CubeListBuilder.create().texOffs(203, 48).addBox(-0.5F, -5.25F, -1.0F, 1.0F, 10.5F, 6.5F, new CubeDeformation(-0.49F)), PartPose.offsetAndRotation(-0.75F, 0.0F, -4.25F, 0.0F, -0.0654F, 0.0F));

        PartDefinition bone236 = bone235.addOrReplaceChild("bone236", CubeListBuilder.create().texOffs(188, 82).addBox(-0.5F, -5.25F, -0.5F, 1.0F, 10.5F, 5.5F, new CubeDeformation(-0.49F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition bone257 = bone234.addOrReplaceChild("bone257", CubeListBuilder.create().texOffs(213, 27).addBox(-0.7995F, -5.0F, 0.0001F, 1.0F, 10.0F, 10.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.7F, 0.0F, -4.0F, -3.1416F, -0.3927F, 3.1416F));

        PartDefinition bone258 = bone234.addOrReplaceChild("bone258", CubeListBuilder.create().texOffs(188, 0).addBox(-2.5386F, -6.5F, -1.306F, 3.0F, 13.0F, 13.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -4.0F, -3.1416F, -0.3927F, 3.1416F));

        PartDefinition bone259 = bone234.addOrReplaceChild("bone259", CubeListBuilder.create(), PartPose.offset(-25.9979F, -11.5961F, 11.6066F));

        PartDefinition bone260 = bone259.addOrReplaceChild("bone260", CubeListBuilder.create().texOffs(218, 48).addBox(6.1965F, 5.0961F, -31.5673F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone261 = bone259.addOrReplaceChild("bone261", CubeListBuilder.create().texOffs(188, 65).addBox(16.7316F, 5.0961F, -26.4099F, 3.0F, 13.0F, 3.0F, new CubeDeformation(-1.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

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