package com.github.tartaricacid.touhoulittlemaid.datapack.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datapack.BoardStateData;
import com.github.tartaricacid.touhoulittlemaid.datapack.pojo.BoardStateRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

public class BoardStateDataReloadListener implements ResourceManagerReloadListener {
    private static final ResourceLocation CHESS_PATH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "board_states/chess.json");
    private static final ResourceLocation XIANGQI_PATH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "board_states/xiangqi.json");
    private static final ResourceLocation GOMOKU_PATH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "board_states/gomoku.json");

    private static final Gson GSON = new Gson();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        // 先清除旧数据
        BoardStateData.clear();
        // 再逐层读取新数据，进行合并加载
        resourceManager.listPacks().forEach(packResources -> {
            readData(packResources, CHESS_PATH, BoardStateData::addChessRecords);
            readData(packResources, XIANGQI_PATH, BoardStateData::addXiangqiRecords);
            readData(packResources, GOMOKU_PATH, BoardStateData::addGomokuRecords);
        });
    }

    private static void readData(PackResources packResources, ResourceLocation path, Consumer<List<BoardStateRecord>> adder) {
        IoSupplier<InputStream> resource = packResources.getResource(PackType.SERVER_DATA, path);
        if (resource == null) {
            return;
        }
        try (InputStream inputStream = resource.get(); InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            List<BoardStateRecord> records = GSON.fromJson(reader, new TypeToken<List<BoardStateRecord>>() {
            }.getType());
            if (records != null && !records.isEmpty()) {
                adder.accept(records);
            }
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to load board state data from {}", path, e);
        }
    }
}
