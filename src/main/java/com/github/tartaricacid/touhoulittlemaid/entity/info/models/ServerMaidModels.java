package com.github.tartaricacid.touhoulittlemaid.entity.info.models;

import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public final class ServerMaidModels {
    private static final String JSON_FILE_NAME = "maid_model.json";
    private static ServerMaidModels INSTANCE;
    private final HashMap<String, MaidModelInfo> idInfoMap;

    public ServerMaidModels() {
        idInfoMap = Maps.newHashMap();
    }

    public static ServerMaidModels getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerMaidModels();
        }
        return INSTANCE;
    }

    public void clearAll() {
        idInfoMap.clear();
    }

    public void putInfo(String modelId, MaidModelInfo maidModelItem) {
        this.idInfoMap.put(modelId, maidModelItem);
    }

    public Optional<MaidModelInfo> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }

    public boolean containsInfo(String modelId) {
        return idInfoMap.containsKey(modelId);
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public int getModelSize() {
        return idInfoMap.size();
    }

    public String getJsonFileName() {
        return JSON_FILE_NAME;
    }
}
