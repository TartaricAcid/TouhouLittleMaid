package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class CustomMaidModelResources {
    private String jsonFileName;
    private List<CustomModelPack<MaidModelInfo>> packList;
    private HashMap<String, EntityModelJson> idModelMap;
    private HashMap<String, MaidModelInfo> idInfoMap;
    private HashMap<String, List<Object>> idAnimationMap;

    private HashMap<String, ModelData> easterEggNormalTagModelMap;
    private HashMap<String, ModelData> easterEggEncryptTagModelMap;

    public CustomMaidModelResources(String jsonFileName, List<CustomModelPack<MaidModelInfo>> packList,
                                    HashMap<String, EntityModelJson> idToModel,
                                    HashMap<String, MaidModelInfo> idToInfo,
                                    HashMap<String, List<Object>> idAnimationMap) {
        this.jsonFileName = jsonFileName;
        this.packList = packList;
        this.idModelMap = idToModel;
        this.idInfoMap = idToInfo;
        this.idAnimationMap = idAnimationMap;
        this.easterEggNormalTagModelMap = Maps.newHashMap();
        this.easterEggEncryptTagModelMap = Maps.newHashMap();
    }

    public void clearAll() {
        this.packList.clear();
        this.idModelMap.clear();
        this.idInfoMap.clear();
        this.idAnimationMap.clear();
        this.easterEggNormalTagModelMap.clear();
        this.easterEggEncryptTagModelMap.clear();
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public List<CustomModelPack<MaidModelInfo>> getPackList() {
        return packList;
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public void addPack(CustomModelPack<MaidModelInfo> pack) {
        this.packList.add(pack);
    }

    public void putModel(String modelId, EntityModelJson modelJson) {
        this.idModelMap.put(modelId, modelJson);
    }

    public void putInfo(String modelId, MaidModelInfo maidModelItem) {
        this.idInfoMap.put(modelId, maidModelItem);
    }

    public void putAnimation(String modelId, List<Object> animationJs) {
        this.idAnimationMap.put(modelId, animationJs);
    }

    public Optional<EntityModelJson> getModel(String modelId) {
        return Optional.ofNullable(idModelMap.get(modelId));
    }

    public float getModelRenderItemScale(String modelId) {
        if (idInfoMap.containsKey(modelId)) {
            return idInfoMap.get(modelId).getRenderItemScale();
        }
        return 1.0f;
    }

    public Optional<List<Object>> getAnimation(String modelId) {
        return Optional.ofNullable(idAnimationMap.get(modelId));
    }

    public void removeAnimation(String modelId) {
        idAnimationMap.remove(modelId);
    }

    public Optional<MaidModelInfo> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }

    public boolean containsInfo(String modelId) {
        return idInfoMap.containsKey(modelId);
    }

    public Optional<ModelData> getEasterEggNormalTagModel(String tag) {
        return Optional.ofNullable(this.easterEggNormalTagModelMap.get(tag));
    }

    public Optional<ModelData> getEasterEggEncryptTagModel(String tag) {
        return Optional.ofNullable(this.easterEggEncryptTagModelMap.get(tag));
    }

    public void putEasterEggNormalTagModel(String tag, ModelData data) {
        this.easterEggNormalTagModelMap.put(tag, data);
    }

    public void putEasterEggEncryptTagModel(String tag, ModelData data) {
        this.easterEggEncryptTagModelMap.put(tag, data);
    }
}
