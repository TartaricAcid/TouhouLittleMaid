package com.github.tartaricacid.touhoulittlemaid.client.resource.models;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

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
        CacheIconManager.addChairPack(pack);
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

    public void sortPackList() {
        List<CustomModelPack<ChairModelInfo>> defaultPackList = Lists.newArrayList();
        List<CustomModelPack<ChairModelInfo>> sortPackList = Lists.newArrayList();

        // 先把默认模型查到，按顺序放进去
        for (String id : DefaultPackConstant.CHAIR_SORT) {
            this.packList.stream().filter(info -> info.getId().equals(id)).findFirst().ifPresent(defaultPackList::add);
        }
        // 剩余模型放进另一个里，进行字典排序
        this.packList.stream().filter(info -> !DefaultPackConstant.CHAIR_SORT.contains(info.getId())).forEach(sortPackList::add);
        sortPackList.sort(Comparator.comparing(CustomModelPack::getId));

        // 最后顺次放入
        this.packList.clear();
        this.packList.addAll(defaultPackList);
        this.packList.addAll(sortPackList);
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
