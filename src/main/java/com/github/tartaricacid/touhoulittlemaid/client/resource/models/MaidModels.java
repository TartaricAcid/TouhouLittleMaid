package com.github.tartaricacid.touhoulittlemaid.client.resource.models;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public final class MaidModels {
    private static final String JSON_FILE_NAME = "maid_model.json";
    private static MaidModels INSTANCE;
    private final List<CustomModelPack<MaidModelInfo>> packList;
    private final HashMap<String, BedrockModel<Mob>> idModelMap;
    private final HashMap<String, MaidModelInfo> idInfoMap;
    private final HashMap<String, List<Object>> idAnimationMap;
    private final HashMap<String, ModelData> easterEggNormalTagModelMap;
    private final HashMap<String, ModelData> easterEggEncryptTagModelMap;

    private MaidModels() {
        this.packList = Lists.newArrayList();
        this.idModelMap = Maps.newHashMap();
        this.idInfoMap = Maps.newHashMap();
        this.idAnimationMap = Maps.newHashMap();
        this.easterEggNormalTagModelMap = Maps.newHashMap();
        this.easterEggEncryptTagModelMap = Maps.newHashMap();
    }

    public static MaidModels getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MaidModels();
        }
        return INSTANCE;
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
        return JSON_FILE_NAME;
    }

    public List<CustomModelPack<MaidModelInfo>> getPackList() {
        return packList;
    }

    public Set<String> getModelIdSet() {
        return idInfoMap.keySet();
    }

    public void addPack(CustomModelPack<MaidModelInfo> pack) {
        this.packList.add(pack);
        CacheIconManager.addMaidPack(pack);
    }

    public void putModel(String modelId, BedrockModel<Mob> modelJson) {
        this.idModelMap.put(modelId, modelJson);
    }

    public void putInfo(String modelId, MaidModelInfo maidModelItem) {
        this.idInfoMap.put(modelId, maidModelItem);
    }

    public void putAnimation(String modelId, List<Object> animationJs) {
        this.idAnimationMap.put(modelId, animationJs);
    }

    public Optional<BedrockModel<Mob>> getModel(String modelId) {
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

    public void sortPackList() {
        List<CustomModelPack<MaidModelInfo>> defaultPackList = Lists.newArrayList();
        List<CustomModelPack<MaidModelInfo>> sortPackList = Lists.newArrayList();

        // 先把默认模型查到，按顺序放进去
        for (String id : DefaultPackConstant.MAID_SORT) {
            this.packList.stream().filter(info -> info.getId().equals(id)).findFirst().ifPresent(defaultPackList::add);
        }
        // 剩余模型放进另一个里，进行字典排序
        this.packList.stream().filter(info -> !DefaultPackConstant.MAID_SORT.contains(info.getId())).forEach(sortPackList::add);
        sortPackList.sort(Comparator.comparing(CustomModelPack::getId));

        // 最后顺次放入
        this.packList.clear();
        this.packList.addAll(defaultPackList);
        this.packList.addAll(sortPackList);
    }

    public static class ModelData {
        private BedrockModel<Mob> model;
        private MaidModelInfo info;
        private List<Object> animations;

        public ModelData(@Nullable BedrockModel<Mob> model, MaidModelInfo info, @Nullable List<Object> animations) {
            this.model = model;
            this.info = info;
            this.animations = animations;
        }

        @Nullable
        public BedrockModel<Mob> getModel() {
            return model;
        }

        public void setModel(@Nullable BedrockModel<Mob> model) {
            this.model = model;
        }

        public MaidModelInfo getInfo() {
            return info;
        }

        public void setInfo(MaidModelInfo info) {
            this.info = info;
        }

        @Nullable
        public List<Object> getAnimations() {
            return animations;
        }

        public void setAnimations(List<Object> animations) {
            this.animations = animations;
        }
    }
}
