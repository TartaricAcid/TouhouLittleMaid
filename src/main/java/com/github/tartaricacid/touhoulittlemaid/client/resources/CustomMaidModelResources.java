package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelItem;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class CustomMaidModelResources {
    private String jsonFileName;
    private List<CustomModelPack<MaidModelItem>> packList;
    private HashMap<String, EntityModelJson> idModelMap;
    private HashMap<String, MaidModelItem> idInfoMap;
    private HashMap<String, List<Object>> idAnimationMap;
    private HashMap<String, String> idDebugAnimationFile;

    public CustomMaidModelResources(String jsonFileName, List<CustomModelPack<MaidModelItem>> packList,
                                    HashMap<String, EntityModelJson> idToModel,
                                    HashMap<String, MaidModelItem> idToInfo,
                                    HashMap<String, List<Object>> idAnimationMap) {
        this.jsonFileName = jsonFileName;
        this.packList = packList;
        this.idModelMap = idToModel;
        this.idInfoMap = idToInfo;
        this.idAnimationMap = idAnimationMap;
        this.idDebugAnimationFile = Maps.newHashMap();
    }

    public void clearAll() {
        this.packList.clear();
        this.idModelMap.clear();
        this.idInfoMap.clear();
        this.idAnimationMap.clear();
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public List<CustomModelPack<MaidModelItem>> getPackList() {
        return packList;
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public void addPack(CustomModelPack<MaidModelItem> pack) {
        this.packList.add(pack);
    }

    public void putModel(String modelId, EntityModelJson modelJson) {
        this.idModelMap.put(modelId, modelJson);
    }

    public void putInfo(String modelId, MaidModelItem maidModelItem) {
        this.idInfoMap.put(modelId, maidModelItem);
    }

    public void putAnimation(String modelId, List<Object> animationJs) {
        this.idAnimationMap.put(modelId, animationJs);
    }

    public void putDebugAnimation(String modelId, String debugAnimationFilePath) {
        this.idDebugAnimationFile.put(modelId, debugAnimationFilePath);
    }

    public void removeDebugAnimation(String modelId) {
        this.idDebugAnimationFile.remove(modelId);
    }

    @Nullable
    public String getDebugAnimationFilePath(String modelId) {
        return this.idDebugAnimationFile.get(modelId);
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

    public Optional<MaidModelItem> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }
}
