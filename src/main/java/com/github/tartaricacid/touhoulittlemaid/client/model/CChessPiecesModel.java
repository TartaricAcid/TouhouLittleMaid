package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.BedrockModelLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class CChessPiecesModel {
    private final BedrockPart main;

    public CChessPiecesModel(String name) {
        SimpleBedrockModel<Entity> model = BedrockModelLoader.getModel(BedrockModelLoader.CCHESS_PIECES);
        this.main = Objects.requireNonNull(model).getPart(name);
    }

    public static CChessPiecesModel[] initModel() {
        CChessPiecesModel[] models = new CChessPiecesModel[23];

        models[8] = new CChessPiecesModel("ShuaiRed");
        models[9] = new CChessPiecesModel("ShiRed");
        models[10] = new CChessPiecesModel("XiangRed");
        models[11] = new CChessPiecesModel("MaRed");
        models[12] = new CChessPiecesModel("JuRed");
        models[13] = new CChessPiecesModel("PaoRed");
        models[14] = new CChessPiecesModel("BingRed");

        models[16] = new CChessPiecesModel("JiangBlack");
        models[17] = new CChessPiecesModel("ShiBlack");
        models[18] = new CChessPiecesModel("XiangBlack");
        models[19] = new CChessPiecesModel("MaBlack");
        models[20] = new CChessPiecesModel("JuBlack");
        models[21] = new CChessPiecesModel("PaoBlack");
        models[22] = new CChessPiecesModel("ZuBlack");

        return models;
    }

    public static CChessPiecesModel getSelectedModel() {
        return new CChessPiecesModel("Selected");
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
