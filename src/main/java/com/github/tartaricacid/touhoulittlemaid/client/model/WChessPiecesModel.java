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

public class WChessPiecesModel {
    private static final ResourceLocation MODEL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/wchess_pieces.json");
    private static BedrockModel<LivingEntity> bedrockModel;
    private final BedrockPart main;

    public WChessPiecesModel(String name) {
        this.main = bedrockModel.getModelMap().get(name).getModelRenderer();
    }

    public static WChessPiecesModel[] initModel() {
        Minecraft.getInstance().getResourceManager().getResource(MODEL).ifPresent(res -> {
            try (InputStream stream = res.open()) {
                BedrockModelPOJO pojo = CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
                bedrockModel = new BedrockModel<>(pojo, BedrockVersion.NEW);
            } catch (IOException ignore) {
            }
        });

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
