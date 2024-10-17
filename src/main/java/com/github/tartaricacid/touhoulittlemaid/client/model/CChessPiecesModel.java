package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CChessPiecesModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "cchess_pieces");
    private final ModelPart main;

    public CChessPiecesModel(ModelPart root, String name) {
        this.main = root.getChild(name);
    }

    public static CChessPiecesModel[] initModel(BlockEntityRendererProvider.Context context) {
        CChessPiecesModel[] models = new CChessPiecesModel[23];
        ModelPart modelPart = context.bakeLayer(CChessPiecesModel.LAYER);

        models[8] = new CChessPiecesModel(modelPart, "ShuaiRed");
        models[9] = new CChessPiecesModel(modelPart, "ShiRed");
        models[10] = new CChessPiecesModel(modelPart, "XiangRed");
        models[11] = new CChessPiecesModel(modelPart, "MaRed");
        models[12] = new CChessPiecesModel(modelPart, "JuRed");
        models[13] = new CChessPiecesModel(modelPart, "PaoRed");
        models[14] = new CChessPiecesModel(modelPart, "BinRed");

        models[16] = new CChessPiecesModel(modelPart, "JiangBlack");
        models[17] = new CChessPiecesModel(modelPart, "ShiBlack");
        models[18] = new CChessPiecesModel(modelPart, "XiangBlack");
        models[19] = new CChessPiecesModel(modelPart, "MaBlack");
        models[20] = new CChessPiecesModel(modelPart, "JuBlack");
        models[21] = new CChessPiecesModel(modelPart, "PaoBlack");
        models[22] = new CChessPiecesModel(modelPart, "ZuBlack");

        return models;
    }

    public static CChessPiecesModel getSelectedModel(BlockEntityRendererProvider.Context context) {
        return new CChessPiecesModel(context.bakeLayer(CChessPiecesModel.LAYER), "Selected");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("JuBlack", CubeListBuilder.create().texOffs(0, 38).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("MaBlack", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("PaoBlack", CubeListBuilder.create().texOffs(0, 76).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("XiangBlack", CubeListBuilder.create().texOffs(80, 0).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("ShiBlack", CubeListBuilder.create().texOffs(80, 38).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("ZuBlack", CubeListBuilder.create().texOffs(80, 76).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("JiangBlack", CubeListBuilder.create().texOffs(0, 114).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("MaRed", CubeListBuilder.create().texOffs(0, 152).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("PaoRed", CubeListBuilder.create().texOffs(80, 152).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("XiangRed", CubeListBuilder.create().texOffs(160, 0).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("ShiRed", CubeListBuilder.create().texOffs(160, 38).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("BinRed", CubeListBuilder.create().texOffs(80, 114).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("ShuaiRed", CubeListBuilder.create().texOffs(160, 76).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("JuRed", CubeListBuilder.create().texOffs(160, 114).addBox(-10.0F, -9.0F, -10.0F, 20.0F, 18.0F, 20.0F, new CubeDeformation(-8.5F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        partdefinition.addOrReplaceChild("Selected", CubeListBuilder.create().texOffs(0, 191).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(-2.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
