package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EasterEggModel extends BedrockModel<EntityMaid> {
    private static final ResourceLocation MODEL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/easter_egg_model.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/easter_egg_model.png");
    private static EasterEggModel INSTANCE;
    private static MaidModelInfo INFO;

    public EasterEggModel() {
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        try (InputStream stream = manager.getResource(MODEL).getInputStream()) {
            loadLegacyModel(CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static EasterEggModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EasterEggModel();
        }
        return INSTANCE;
    }

    public static MaidModelInfo getInfo() {
        if (INFO == null) {
            INFO = new MaidModelInfo() {
                @Override
                public ResourceLocation getTexture() {
                    return TEXTURE;
                }
            };
        }
        return INFO;
    }
}
