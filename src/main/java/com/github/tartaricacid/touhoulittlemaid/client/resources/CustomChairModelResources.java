package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ChairModelItem;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class CustomChairModelResources {
    private String jsonFileName;
    private List<CustomModelPack<ChairModelItem>> packList;
    private HashMap<String, EntityModelJson> idModelMap;
    private HashMap<String, ChairModelItem> idInfoMap;
    private HashMap<String, List<Object>> idAnimationMap;
    private HashMap<String, String> idDebugAnimationFile;

    public CustomChairModelResources(String jsonFileName, List<CustomModelPack<ChairModelItem>> packList,
                                     HashMap<String, EntityModelJson> idToModel,
                                     HashMap<String, ChairModelItem> idToInfo,
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

    public List<CustomModelPack<ChairModelItem>> getPackList() {
        return packList;
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public void addPack(CustomModelPack<ChairModelItem> pack) {
        this.packList.add(pack);
    }

    public void putModel(String modelId, EntityModelJson modelJson) {
        this.idModelMap.put(modelId, modelJson);
    }

    public void putInfo(String modelId, ChairModelItem chairModelItem) {
        this.idInfoMap.put(modelId, chairModelItem);
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

    public Optional<ChairModelItem> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }
}
