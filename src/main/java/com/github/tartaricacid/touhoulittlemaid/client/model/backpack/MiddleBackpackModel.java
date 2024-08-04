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

public class MiddleBackpackModel extends EntityModel<EntityMaid> {
    public static ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "backpack_middle");
    private final ModelPart all;

    public MiddleBackpackModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, 3.0F));

        PartDefinition band2 = all.addOrReplaceChild("band2", CubeListBuilder.create(), PartPose.offset(3.0F, -10.25F, -4.0F));

        PartDefinition bone73 = band2.addOrReplaceChild("bone73", CubeListBuilder.create().texOffs(92, 106).addBox(-4.0F, -15.501F, -8.5F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 12.25F, 4.0F));

        PartDefinition bone74 = band2.addOrReplaceChild("bone74", CubeListBuilder.create().texOffs(92, 106).addBox(-1.0F, -0.001F, -3.5F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -3.75F, -4.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone75 = band2.addOrReplaceChild("bone75", CubeListBuilder.create().texOffs(84, 106).addBox(-1.0F, -0.251F, -11.433F, 1.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.75F, -4.5F, 2.0944F, 0.0F, 0.0F));

        PartDefinition bone76 = band2.addOrReplaceChild("bone76", CubeListBuilder.create().texOffs(92, 106).addBox(-1.0F, 0.265F, 0.424F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -3.75F, -1.5F, -1.0123F, 0.0F, 0.0F));

        PartDefinition band = all.addOrReplaceChild("band", CubeListBuilder.create(), PartPose.offset(-3.0F, -10.25F, -4.0F));

        PartDefinition bone69 = band.addOrReplaceChild("bone69", CubeListBuilder.create().texOffs(92, 106).addBox(-4.0F, -15.501F, -8.5F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 12.25F, 4.0F));

        PartDefinition bone71 = band.addOrReplaceChild("bone71", CubeListBuilder.create().texOffs(92, 106).addBox(-1.0F, -0.001F, -3.5F, 1.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.75F, -4.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition bone72 = band.addOrReplaceChild("bone72", CubeListBuilder.create().texOffs(84, 106).addBox(-1.0F, -0.251F, -11.433F, 1.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.75F, -4.5F, 2.0944F, 0.0F, 0.0F));

        PartDefinition bone70 = band.addOrReplaceChild("bone70", CubeListBuilder.create().texOffs(92, 106).addBox(-1.0F, 0.265F, 0.424F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.75F, -1.5F, -1.0123F, 0.0F, 0.0F));

        PartDefinition decoration = all.addOrReplaceChild("decoration", CubeListBuilder.create().texOffs(90, 56).addBox(-0.75F, -4.5019F, -1.2936F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.4F))
                .texOffs(64, 0).addBox(-0.85F, -3.5019F, -0.9436F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(64, 0).addBox(-0.85F, -3.5019F, 0.3564F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(8.2F, -1.4F, -1.5F, 0.0873F, 0.0F, 0.0F));

        PartDefinition bone49 = decoration.addOrReplaceChild("bone49", CubeListBuilder.create().texOffs(88, 57).addBox(-1.55F, -6.5019F, -1.3936F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(88, 57).addBox(-1.55F, -6.5019F, -0.1936F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone51 = decoration.addOrReplaceChild("bone51", CubeListBuilder.create().texOffs(64, 0).addBox(-0.7F, -1.7786F, 1.517F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(64, 0).addBox(-0.7F, -2.1786F, 1.117F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(64, 0).addBox(-0.7F, -1.9786F, 1.117F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(64, 0).addBox(-0.7F, -1.9786F, 1.317F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(64, 0).addBox(-0.7F, -1.7786F, 1.317F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cone = decoration.addOrReplaceChild("cone", CubeListBuilder.create().texOffs(96, 0).addBox(-1.0F, -2.2019F, -0.2436F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.1F, -0.1F, 0.0F));

        PartDefinition bone8 = cone.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(96, 0).addBox(-1.0F, -2.2794F, -0.1713F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1571F, 0.0F, 0.0F));

        PartDefinition bone52 = cone.addOrReplaceChild("bone52", CubeListBuilder.create().texOffs(96, 0).addBox(-1.0F, -2.1992F, -0.3222F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1571F, 0.0F, 0.0F));

        PartDefinition bone53 = cone.addOrReplaceChild("bone53", CubeListBuilder.create().texOffs(96, 0).addBox(-1.0F, -2.2088F, -0.4003F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3142F, 0.0F, 0.0F));

        PartDefinition bone50 = cone.addOrReplaceChild("bone50", CubeListBuilder.create().texOffs(96, 0).addBox(-1.0F, -2.3673F, -0.112F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3142F, 0.0F, 0.0F));

        PartDefinition web = decoration.addOrReplaceChild("web", CubeListBuilder.create().texOffs(64, 0).addBox(-0.04F, -2.2468F, -1.2887F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F))
                .texOffs(64, 0).addBox(-0.04F, -1.5468F, -0.5887F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F))
                .texOffs(64, 0).addBox(-0.04F, -2.6468F, -0.5887F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition web2 = decoration.addOrReplaceChild("web2", CubeListBuilder.create().texOffs(64, 0).addBox(-0.04F, -1.9904F, 0.7132F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F))
                .texOffs(64, 0).addBox(-0.04F, -1.2904F, 0.0132F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F))
                .texOffs(64, 0).addBox(-0.04F, -2.3904F, 0.0132F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition paw = all.addOrReplaceChild("paw", CubeListBuilder.create().texOffs(96, 64).addBox(9.4822F, -14.3F, -1.922F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.25F))
                .texOffs(96, 64).addBox(9.4822F, -14.3F, 1.578F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone = paw.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(96, 64).addBox(6.3458F, -13.8F, 0.25F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 64).addBox(6.3458F, -14.3F, -1.25F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 64).addBox(6.0958F, -14.3F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(8.0F, 0.0F, -8.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone77 = bone.addOrReplaceChild("bone77", CubeListBuilder.create().texOffs(96, 64).addBox(6.3458F, -11.3749F, -7.9129F, 5.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition bone39 = paw.addOrReplaceChild("bone39", CubeListBuilder.create().texOffs(96, 0).addBox(-0.5F, -0.25F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(96, 0).addBox(-1.5607F, -0.25F, -0.9393F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(96, 0).addBox(-0.5607F, 0.0F, -0.9393F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-0.5F, -0.25F, 0.1213F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(96, 0).addBox(0.5607F, -0.25F, -0.9393F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(12.0F, -11.55F, 0.8458F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone37 = paw.addOrReplaceChild("bone37", CubeListBuilder.create().texOffs(96, 0).addBox(-0.1893F, -0.25F, -1.8713F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(96, 0).addBox(-1.25F, -0.25F, -0.8107F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(96, 0).addBox(-0.1893F, -0.25F, 0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F))
                .texOffs(96, 0).addBox(0.8713F, -0.25F, -0.8107F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(12.0F, -11.55F, 0.8458F, 0.0F, -0.7854F, 0.0F));

        PartDefinition toe = paw.addOrReplaceChild("toe", CubeListBuilder.create().texOffs(96, 64).addBox(-5.0F, -1.05F, -0.9042F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(18.25F, -13.25F, 0.75F));

        PartDefinition bone14 = toe.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(96, 32).addBox(-3.867F, 0.1661F, -0.2456F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.1745F));

        PartDefinition bone21 = toe.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(96, 32).addBox(-3.9017F, 0.1661F, -0.5575F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, 0.1745F));

        PartDefinition bone28 = toe.addOrReplaceChild("bone28", CubeListBuilder.create().texOffs(96, 32).addBox(-4.038F, -0.1812F, -0.2154F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, -0.1745F));

        PartDefinition bone27 = toe.addOrReplaceChild("bone27", CubeListBuilder.create().texOffs(96, 32).addBox(-4.0727F, -0.1812F, -0.5876F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, -0.1745F));

        PartDefinition toe2 = paw.addOrReplaceChild("toe2", CubeListBuilder.create().texOffs(96, 64).addBox(-3.243F, -1.05F, 3.1461F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(18.25F, -13.25F, 0.75F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bone29 = toe2.addOrReplaceChild("bone29", CubeListBuilder.create().texOffs(96, 32).addBox(-1.4596F, -0.1391F, 3.4428F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.1745F));

        PartDefinition bone30 = toe2.addOrReplaceChild("bone30", CubeListBuilder.create().texOffs(96, 32).addBox(-2.901F, -0.1391F, 3.7319F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, 0.1745F));

        PartDefinition bone31 = toe2.addOrReplaceChild("bone31", CubeListBuilder.create().texOffs(96, 32).addBox(-1.6306F, 0.1239F, 3.4729F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, -0.1745F));

        PartDefinition bone32 = toe2.addOrReplaceChild("bone32", CubeListBuilder.create().texOffs(96, 32).addBox(-3.072F, 0.1239F, 3.7017F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, -0.1745F));

        PartDefinition toe3 = paw.addOrReplaceChild("toe3", CubeListBuilder.create().texOffs(96, 64).addBox(-3.3784F, -1.05F, -5.0107F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(18.25F, -13.25F, 0.75F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bone33 = toe3.addOrReplaceChild("bone33", CubeListBuilder.create().texOffs(96, 32).addBox(-3.0073F, -0.1155F, -4.567F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.1745F));

        PartDefinition bone34 = toe3.addOrReplaceChild("bone34", CubeListBuilder.create().texOffs(96, 32).addBox(-1.6159F, -0.1155F, -4.3242F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, 0.1745F));

        PartDefinition bone35 = toe3.addOrReplaceChild("bone35", CubeListBuilder.create().texOffs(96, 32).addBox(-3.1783F, 0.1003F, -4.5368F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, -0.1745F));

        PartDefinition bone36 = toe3.addOrReplaceChild("bone36", CubeListBuilder.create().texOffs(96, 32).addBox(-1.7869F, 0.1003F, -4.3544F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1745F, -0.1745F));

        PartDefinition bag = all.addOrReplaceChild("bag", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -11.8F, -4.0F, 16.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper = bag.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = upper.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 3.0569F, 1.087F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -4.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone3 = upper.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 1.6714F, 1.8412F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -4.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition bone4 = upper.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 0.1379F, 2.211F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone5 = upper.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -1.4391F, 2.1714F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -4.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition bone6 = upper.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -2.952F, 1.7249F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -4.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition bone7 = upper.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -6.2979F, 0.9021F, 16.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition lower = bag.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 0.0F));

        PartDefinition bone15 = lower.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -2.7046F, 0.7247F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone16 = lower.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -1.4589F, 1.1412F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition bone17 = lower.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -0.1479F, 1.2211F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone18 = lower.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 1.1391F, 0.9589F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition bone19 = lower.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 2.3144F, 0.3726F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition bone20 = lower.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 3.2979F, -0.4979F, 16.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, -4.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition upper2 = bag.addOrReplaceChild("upper2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone9 = upper2.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 3.4699F, -0.5457F, 16.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition bone10 = upper2.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 2.4693F, -1.4592F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 25).addBox(2.25F, 0.9693F, -2.4092F, 4.0F, 4.0F, 3.0F, new CubeDeformation(-1.0F))
                .texOffs(64, 25).addBox(-6.25F, 0.9693F, -2.4092F, 4.0F, 4.0F, 3.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 4.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition bone11 = upper2.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 1.2663F, -2.0827F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 4.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition bone12 = upper2.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -0.0571F, -2.3735F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 4.0F, 1.0472F, 0.0F, 0.0F));

        PartDefinition bone13 = upper2.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -1.4106F, -2.3119F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 4.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition lower2 = bag.addOrReplaceChild("lower2", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 2.1F));

        PartDefinition bone22 = lower2.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -2.8329F, -1.2458F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 4.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone40 = lower2.addOrReplaceChild("bone40", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -3.8F, -0.5042F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 4.0F));

        PartDefinition bone23 = lower2.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -1.7068F, -1.7118F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 4.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition bone24 = lower2.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -0.4985F, -1.8705F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 4.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone25 = lower2.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 0.7097F, -1.7111F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 4.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition bone26 = lower2.addOrReplaceChild("bone26", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, 1.8355F, -1.2443F, 16.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 4.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition bone44 = lower2.addOrReplaceChild("bone44", CubeListBuilder.create().texOffs(94, 96).addBox(-8.0F, -11.5069F, 4.3261F, 16.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition feet = lower2.addOrReplaceChild("feet", CubeListBuilder.create().texOffs(64, 0).addBox(-0.5F, -4.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offset(4.0F, 16.0F, 4.0F));

        PartDefinition bone65 = feet.addOrReplaceChild("bone65", CubeListBuilder.create().texOffs(64, 0).addBox(-0.5F, -3.1858F, -0.7485F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone63 = feet.addOrReplaceChild("bone63", CubeListBuilder.create().texOffs(64, 0).addBox(2.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition bone64 = feet.addOrReplaceChild("bone64", CubeListBuilder.create().texOffs(64, 0).addBox(-3.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition feet2 = lower2.addOrReplaceChild("feet2", CubeListBuilder.create().texOffs(64, 0).addBox(-0.5F, -4.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offset(-4.0F, 16.0F, 4.0F));

        PartDefinition bone66 = feet2.addOrReplaceChild("bone66", CubeListBuilder.create().texOffs(64, 0).addBox(-0.5F, -3.1858F, -0.7485F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone67 = feet2.addOrReplaceChild("bone67", CubeListBuilder.create().texOffs(64, 0).addBox(2.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition bone68 = feet2.addOrReplaceChild("bone68", CubeListBuilder.create().texOffs(64, 0).addBox(-3.0249F, -3.2732F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition bagLeft = bag.addOrReplaceChild("bagLeft", CubeListBuilder.create().texOffs(89, 106).addBox(-3.0F, -11.8F, -8.0F, 7.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(88, 116).addBox(-2.5F, -1.8F, -8.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(90, 117).addBox(-1.5F, -0.8F, -8.0F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 0.0F, -0.25F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone45 = bagLeft.addOrReplaceChild("bone45", CubeListBuilder.create().texOffs(94, 106).addBox(3.6296F, -10.3034F, -8.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition bagRight = bag.addOrReplaceChild("bagRight", CubeListBuilder.create().texOffs(89, 106).mirror().addBox(-4.0F, -11.8F, -8.0F, 7.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(88, 116).mirror().addBox(-5.5F, -1.8F, -8.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(90, 117).mirror().addBox(-4.5F, -0.8F, -8.0F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1F, 0.0F, -0.25F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bone46 = bagRight.addOrReplaceChild("bone46", CubeListBuilder.create().texOffs(94, 106).mirror().addBox(-6.6296F, -10.3034F, -8.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition pocketLeft = bag.addOrReplaceChild("pocketLeft", CubeListBuilder.create().texOffs(96, 120).addBox(7.9F, -8.5F, -1.45F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 32).addBox(8.9F, -8.6F, -1.45F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(7.7F, -8.6F, -1.45F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -1.5F, 0.9F));

        PartDefinition bone38 = pocketLeft.addOrReplaceChild("bone38", CubeListBuilder.create().texOffs(124, 126).addBox(10.0F, -1.005F, -0.9879F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(124, 126).addBox(10.0F, -1.005F, -0.5879F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(124, 126).addBox(10.0F, -1.405F, -0.5879F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-1.6F, -7.5F, 0.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition pocketRight = bag.addOrReplaceChild("pocketRight", CubeListBuilder.create().texOffs(124, 123).addBox(-10.0F, -8.5F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 32).addBox(-10.0F, -5.25F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-10.0F, -7.75F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-9.1515F, -5.25F, -0.8485F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-9.1515F, -7.75F, -0.8485F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-9.1515F, -5.25F, 0.8485F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-9.1515F, -7.75F, 0.8485F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(124, 123).addBox(-9.2929F, -8.5F, -0.7071F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 123).addBox(-9.2929F, -8.5F, 0.7071F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 123).addBox(-9.2929F, -3.7929F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.4F, 0.0F, 0.3F));

        PartDefinition bone56 = pocketRight.addOrReplaceChild("bone56", CubeListBuilder.create().texOffs(124, 123).addBox(-9.5459F, 3.5962F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone55 = pocketRight.addOrReplaceChild("bone55", CubeListBuilder.create().texOffs(124, 123).addBox(-9.2929F, -2.9749F, -2.9749F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 123).addBox(-9.2929F, -3.682F, -2.2678F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition bone54 = pocketRight.addOrReplaceChild("bone54", CubeListBuilder.create().texOffs(124, 123).addBox(-6.364F, -8.5F, 6.7782F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 123).addBox(-7.0711F, -8.5F, 6.0711F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 32).addBox(-7.1125F, -5.25F, 5.9711F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-7.1125F, -7.75F, 5.9711F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-6.264F, -5.25F, 6.8196F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(96, 32).addBox(-6.264F, -7.75F, 6.8196F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition bear = pocketRight.addOrReplaceChild("bear", CubeListBuilder.create().texOffs(64, 46).addBox(-13.5F, -14.0F, -3.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(-3.5F))
                .texOffs(94, 62).addBox(-9.5F, -10.0F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.4F))
                .texOffs(90, 58).addBox(-10.0F, -11.35F, -0.85F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F))
                .texOffs(90, 58).addBox(-10.0F, -11.35F, 0.85F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)), PartPose.offset(-0.3F, 0.0F, -0.55F));

        PartDefinition beak = bag.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -11.2F, 7.2F, -0.2618F, 0.0F, 0.0F));

        PartDefinition bone57 = beak.addOrReplaceChild("bone57", CubeListBuilder.create().texOffs(96, 0).addBox(-0.5F, -0.0302F, -1.829F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition bone58 = beak.addOrReplaceChild("bone58", CubeListBuilder.create().texOffs(96, 0).addBox(-0.5F, -0.0019F, -1.9564F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition bone61 = beak.addOrReplaceChild("bone61", CubeListBuilder.create().texOffs(96, 0).addBox(-0.5195F, -0.0238F, -3.0985F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.5236F, 0.0F));

        PartDefinition bone62 = beak.addOrReplaceChild("bone62", CubeListBuilder.create().texOffs(96, 0).addBox(-0.4805F, -0.0238F, -3.0985F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, -0.5236F, 0.0F));

        PartDefinition bone59 = beak.addOrReplaceChild("bone59", CubeListBuilder.create().texOffs(102, 0).addBox(-0.4723F, -0.0151F, -1.9623F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition bone60 = beak.addOrReplaceChild("bone60", CubeListBuilder.create().texOffs(102, 0).addBox(-0.5277F, -0.0151F, -1.9623F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition sign = all.addOrReplaceChild("sign", CubeListBuilder.create(), PartPose.offset(7.0F, 1.2F, 3.6F));

        PartDefinition bone47 = sign.addOrReplaceChild("bone47", CubeListBuilder.create().texOffs(0, 64).addBox(-12.001F, -17.7F, -12.0F, 24.0F, 24.0F, 24.0F, new CubeDeformation(-11.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition bone43 = sign.addOrReplaceChild("bone43", CubeListBuilder.create().texOffs(0, 64).addBox(-12.0F, -15.9F, -12.001F, 24.0F, 24.0F, 24.0F, new CubeDeformation(-11.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone48 = sign.addOrReplaceChild("bone48", CubeListBuilder.create().texOffs(0, 112).addBox(-3.399F, -6.7F, -3.4F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-3.7F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone41 = sign.addOrReplaceChild("bone41", CubeListBuilder.create().texOffs(0, 112).addBox(-4.0F, -6.7F, -4.601F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-3.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition bone42 = sign.addOrReplaceChild("bone42", CubeListBuilder.create().texOffs(0, 112).addBox(-4.601F, -6.7F, -3.4F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-3.7F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityMaid entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}