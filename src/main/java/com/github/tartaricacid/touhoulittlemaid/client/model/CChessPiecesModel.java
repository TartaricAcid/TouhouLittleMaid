package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CChessPiecesModel {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "models/entity/cchess_pieces.json");
    private static BedrockModel<LivingEntity> bedrockModel;
    private final BedrockPart main;

    public CChessPiecesModel(String name) {
        this.main = bedrockModel.getModelMap().get(name).getModelRenderer();
    }

    public static CChessPiecesModel[] initModel() {
        Minecraft.getInstance().getResourceManager().getResource(MODEL).ifPresent(res -> {
            try (InputStream stream = res.open()) {
                BedrockModelPOJO pojo = CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
                bedrockModel = new BedrockModel<>(pojo, BedrockVersion.NEW);
            } catch (IOException ignore) {
            }
        });

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
