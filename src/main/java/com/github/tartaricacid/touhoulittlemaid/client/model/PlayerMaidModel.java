package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Mob;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PlayerMaidModel extends BedrockModel<Mob> {
    private static final ResourceLocation STEVE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/player_maid.json");
    private static final ResourceLocation ALEX = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/player_maid_slim.json");

    public PlayerMaidModel(boolean smallArms) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        if (smallArms) {
            try (InputStream stream = manager.open(ALEX)) {
                loadNewModel(CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            try (InputStream stream = manager.open(STEVE)) {
                loadLegacyModel(CustomPackLoader.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
