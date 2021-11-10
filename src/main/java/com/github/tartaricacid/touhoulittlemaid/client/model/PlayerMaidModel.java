package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PlayerMaidModel extends BedrockModel<EntityMaid> {
    private static final ResourceLocation STEVE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/player_maid.json");
    private static final ResourceLocation ALEX = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/player_maid_slim.json");

    public PlayerMaidModel(boolean smallArms) {
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        if (smallArms) {
            try (InputStream stream = manager.getResource(ALEX).getInputStream()) {
                loadNewModel(CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            try (InputStream stream = manager.getResource(STEVE).getInputStream()) {
                loadLegacyModel(CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
