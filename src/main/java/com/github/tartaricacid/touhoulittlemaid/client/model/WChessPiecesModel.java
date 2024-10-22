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

public class WChessPiecesModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(TouhouLittleMaid.MOD_ID, "main"), "wchess_pieces");
    private final ModelPart main;

    public WChessPiecesModel(ModelPart root, String name) {
        this.main = root.getChild(name);
    }

    public static WChessPiecesModel[] initModel(ModelPart root) {
        WChessPiecesModel[] models = new WChessPiecesModel[23];

        models[8] = new WChessPiecesModel(root, "KING_W");
        models[9] = new WChessPiecesModel(root, "QUEEN_W");
        models[10] = new WChessPiecesModel(root, "ROOK_W");
        models[11] = new WChessPiecesModel(root, "BISHOP_W");
        models[12] = new WChessPiecesModel(root, "KNIGHT_W");
        models[13] = new WChessPiecesModel(root, "PAWN_W");

        models[16] = new WChessPiecesModel(root, "KING_B");
        models[17] = new WChessPiecesModel(root, "QUEEN_B");
        models[18] = new WChessPiecesModel(root, "ROOK_B");
        models[19] = new WChessPiecesModel(root, "BISHOP_B");
        models[20] = new WChessPiecesModel(root, "KNIGHT_B");
        models[21] = new WChessPiecesModel(root, "PAWN_B");

        return models;
    }

    public static WChessPiecesModel getSelectedModel(ModelPart root) {
        return new WChessPiecesModel(root, "SLECT");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition KING_W = partdefinition.addOrReplaceChild("KING_W", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition QUEEN_W = partdefinition.addOrReplaceChild("QUEEN_W", CubeListBuilder.create().texOffs(0, 5).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition ROOK_W = partdefinition.addOrReplaceChild("ROOK_W", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition BISHOP_W = partdefinition.addOrReplaceChild("BISHOP_W", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition KNIGHT_W = partdefinition.addOrReplaceChild("KNIGHT_W", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition PAWN_W = partdefinition.addOrReplaceChild("PAWN_W", CubeListBuilder.create().texOffs(16, 5).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition KING_B = partdefinition.addOrReplaceChild("KING_B", CubeListBuilder.create().texOffs(16, 10).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition QUEEN_B = partdefinition.addOrReplaceChild("QUEEN_B", CubeListBuilder.create().texOffs(16, 15).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition ROOK_B = partdefinition.addOrReplaceChild("ROOK_B", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition BISHOP_B = partdefinition.addOrReplaceChild("BISHOP_B", CubeListBuilder.create().texOffs(16, 20).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition KNIGHT_B = partdefinition.addOrReplaceChild("KNIGHT_B", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition PAWN_B = partdefinition.addOrReplaceChild("PAWN_B", CubeListBuilder.create().texOffs(16, 25).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition SLECT = partdefinition.addOrReplaceChild("SLECT", CubeListBuilder.create().texOffs(8, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }
}
