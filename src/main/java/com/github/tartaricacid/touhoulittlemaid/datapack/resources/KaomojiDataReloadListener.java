package com.github.tartaricacid.touhoulittlemaid.datapack.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datapack.KaomojiData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class KaomojiDataReloadListener implements ResourceManagerReloadListener {
    private static final ResourceLocation FILE_PATH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "chat_bubble/kaomoji.json");
    private static final Gson GSON = new Gson();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        // 先清除旧数据
        KaomojiData.clear();
        // 再逐层读取新数据，进行合并加载
        resourceManager.listPacks().forEach(packResources -> {
            IoSupplier<InputStream> resource = packResources.getResource(PackType.SERVER_DATA, FILE_PATH);
            if (resource == null) {
                return;
            }
            try (InputStream inputStream = resource.get(); InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Map<String, List<String>> data = GSON.fromJson(reader, new TypeToken<Map<String, List<String>>>() {
                }.getType());
                if (data != null && !data.isEmpty()) {
                    KaomojiData.merge(data);
                }
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error("Failed to load kaomoji data from {}", FILE_PATH, e);
            }
        });
    }
}
