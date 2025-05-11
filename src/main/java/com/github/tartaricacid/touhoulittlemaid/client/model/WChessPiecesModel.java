package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class WChessPiecesModel {
    private final BedrockPart main;

    public WChessPiecesModel(String name) {
        SimpleBedrockModel<Entity> model = BedrockModelLoader.getModel(BedrockModelLoader.WCHESS_PIECES);
        this.main = Objects.requireNonNull(model).getPart(name);
    }

    public static WChessPiecesModel[] initModel() {
        WChessPiecesModel[] models = new WChessPiecesModel[23];

        models[8] = new WChessPiecesModel("KING_W");
        models[9] = new WChessPiecesModel("QUEEN_W");
        models[10] = new WChessPiecesModel("ROOK_W");
        models[11] = new WChessPiecesModel("BISHOP_W");
        models[12] = new WChessPiecesModel("KNIGHT_W");
        models[13] = new WChessPiecesModel("PAWN_W");

        models[16] = new WChessPiecesModel("KING_B");
        models[17] = new WChessPiecesModel("QUEEN_B");
        models[18] = new WChessPiecesModel("ROOK_B");
        models[19] = new WChessPiecesModel("BISHOP_B");
        models[20] = new WChessPiecesModel("KNIGHT_B");
        models[21] = new WChessPiecesModel("PAWN_B");

        return models;
    }

    public static WChessPiecesModel getSelectedModel() {
        return new WChessPiecesModel("SELECT");
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(0.9f, 0.9f, 0.9f);
        poseStack.translate(0, 0.175, 0);
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}
