package com.github.tartaricacid.touhoulittlemaid.entity.info.models;

import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public final class ServerChairModels {
    private static final String JSON_FILE_NAME = "maid_chair.json";
    private static ServerChairModels INSTANCE;
    private final HashMap<String, ChairModelInfo> idInfoMap;

    public ServerChairModels() {
        idInfoMap = Maps.newHashMap();
    }

    public static ServerChairModels getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerChairModels();
        }
        return INSTANCE;
    }

    public void clearAll() {
        idInfoMap.clear();
    }

    public void putInfo(String modelId, ChairModelInfo maidModelItem) {
        this.idInfoMap.put(modelId, maidModelItem);
    }

    public Optional<ChairModelInfo> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }

    public boolean containsInfo(String modelId) {
        return idInfoMap.containsKey(modelId);
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public String getJsonFileName() {
        return JSON_FILE_NAME;
    }
}
