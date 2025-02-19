package com.github.tartaricacid.touhoulittlemaid.compat.ysm.data;

import com.github.tartaricacid.touhoulittlemaid.compat.ysm.client.event.InitYsmMaidModelsEvent;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.resources.ResourceLocation.isValidNamespace;

public final class YsmModelData {
    private static final List<YsmMaidInfo> YSM_MAID_INFOS = new ArrayList<>();

    public static void buildYsmMaidInfos() {
        YSM_MAID_INFOS.clear();

        InitYsmMaidModelsEvent initYsmMaidModelsEvent = new InitYsmMaidModelsEvent();
        MinecraftForge.EVENT_BUS.post(initYsmMaidModelsEvent);

        List<YsmMaidInfo> ysmMaidInfos = Lists.newArrayList();
        for (YsmMaidBaseInfo model : initYsmMaidModelsEvent.getYsmModels()) {
            String modelIdString = model.modelId();
            String textureNameString = model.textureId();

            ResourceLocation cacheIconId = !isValidNamespace(modelIdString) || !isValidNamespace(textureNameString) ? null : createCacheIconId(modelIdString, textureNameString);

            ysmMaidInfos.add(new YsmMaidInfo(modelIdString, textureNameString, model.tooltips(), model.needAuth(), cacheIconId));
        }

        YSM_MAID_INFOS.addAll(ysmMaidInfos);
    }

    public static void clear() {
        YSM_MAID_INFOS.clear();
    }

    public static List<YsmMaidInfo> getYsmMaidInfos() {
        return YSM_MAID_INFOS;
    }

    static ResourceLocation createCacheIconId(String modeId, String textureName) {
        return new ResourceLocation("yes_steve_model", modeId + "/" + textureName + "/cache");
    }
}
