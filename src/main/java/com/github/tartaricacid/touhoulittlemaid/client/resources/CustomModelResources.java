package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.client.animation.EnumAnimationType;
import com.github.tartaricacid.touhoulittlemaid.client.animation.pojo.CustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author TartaricAcid
 * @date 2019/8/28 13:56
 **/
@SideOnly(Side.CLIENT)
public class CustomModelResources {
    private String jsonFileName;
    private List<CustomModelPackPOJO> packList;
    private HashMap<String, EntityModelJson> idModelMap;
    private HashMap<String, ModelItem> idInfoMap;
    private HashMap<String, HashMap<EnumAnimationType, CustomAnimation>> idAnimationMap;

    public CustomModelResources(String jsonFileName, List<CustomModelPackPOJO> packList,
                                HashMap<String, EntityModelJson> idToModel,
                                HashMap<String, ModelItem> idToInfo,
                                HashMap<String, HashMap<EnumAnimationType, CustomAnimation>> idAnimationMap) {
        this.jsonFileName = jsonFileName;
        this.packList = packList;
        this.idModelMap = idToModel;
        this.idInfoMap = idToInfo;
        this.idAnimationMap = idAnimationMap;
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

    public List<CustomModelPackPOJO> getPackList() {
        return packList;
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public void addPack(CustomModelPackPOJO pack) {
        this.packList.add(pack);
    }

    public void putModel(String modelId, EntityModelJson modelJson) {
        this.idModelMap.put(modelId, modelJson);
    }

    public void putInfo(String modelId, ModelItem modelItem) {
        this.idInfoMap.put(modelId, modelItem);
    }

    public void putAnimation(String modelId, HashMap<EnumAnimationType, CustomAnimation> animationMap) {
        this.idAnimationMap.put(modelId, animationMap);
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

    public Optional<CustomAnimation> getAnimation(String modelId, EnumAnimationType animationType) {
        return Optional.ofNullable(idAnimationMap.get(modelId).get(animationType));
    }

    public Optional<ModelItem> getInfo(String modelId) {
        return Optional.ofNullable(idInfoMap.get(modelId));
    }
}
