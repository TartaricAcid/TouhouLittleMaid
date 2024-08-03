package com.github.tartaricacid.touhoulittlemaid.client.resource.models;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class ChairModels {
    private static final String JSON_FILE_NAME = "maid_chair.json";
    private static ChairModels INSTANCE;
    private final List<CustomModelPack<ChairModelInfo>> packList;
    private final HashMap<String, BedrockModel<EntityChair>> idModelMap;
    private final HashMap<String, ChairModelInfo> idInfoMap;
    private final HashMap<String, List<Object>> idAnimationMap;

    private ChairModels() {
        this.packList = Lists.newArrayList();
        this.idModelMap = Maps.newHashMap();
        this.idInfoMap = Maps.newHashMap();
        this.idAnimationMap = Maps.newHashMap();
    }

    public static ChairModels getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChairModels();
        }
        return INSTANCE;
    }

    public void clearAll() {
        this.packList.clear();
        this.idModelMap.clear();
        this.idInfoMap.clear();
        this.idAnimationMap.clear();
    }

    public String getJsonFileName() {
        return JSON_FILE_NAME;
    }

    public List<CustomModelPack<ChairModelInfo>> getPackList() {
        return packList;
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public void addPack(CustomModelPack<ChairModelInfo> pack) {
        this.packList.add(pack);
    }

    public void putModel(String modelId, BedrockModel<EntityChair> modelJson) {
        this.idModelMap.put(modelId, modelJson);
    }

    public void putInfo(String modelId, ChairModelInfo chairModelItem) {
        this.idInfoMap.put(modelId, chairModelItem);
    }

    public void putAnimation(String modelId, List<Object> animationJs) {
        this.idAnimationMap.put(modelId, animationJs);
    }

    public Optional<BedrockModel<EntityChair>> getModel(String modelId) {
        return Optional.ofNullable(idModelMap.get(modelId));
    }

    public float getModelRenderItemScale(String modelId) {
        if (idInfoMap.containsKey(modelId)) {
            return idInfoMap.get(modelId).getRenderItemScale();
        }
        return 1.0f;
    }

    public float getModelMountedYOffset(String modelId) {
        if (idInfoMap.containsKey(modelId)) {
            return idInfoMap.get(modelId).getMountedYOffset();
        }
        return 0.0f;
    }

    public boolean getModelTameableCanRide(String modelId) {
        if (idInfoMap.containsKey(modelId)) {
            return idInfoMap.get(modelId).isTameableCanRide();
        }
        return true;
    }

    public boolean getModelNoGravity(String modelId) {
        if (idInfoMap.containsKey(modelId)) {
            return idInfoMap.get(modelId).isNoGravity();
        }
        return false;
    }

    public Optional<List<Object>> getAnimation(String modelId) {
        return Optional.ofNullable(idAnimationMap.get(modelId));
    }

    public void removeAnimation(String modelId) {
        idAnimationMap.remove(modelId);
    }

    public Optional<ChairModelInfo> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }
}
