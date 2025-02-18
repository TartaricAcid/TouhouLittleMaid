package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.InitYsmMaidModelsEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.block.ModelSwitcherGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.block.ModelSwitcherModelGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.AbstractModelGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.ChairModelGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.MaidModelGui;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;
import static net.minecraft.resources.ResourceLocation.isValidNamespace;

@OnlyIn(Dist.CLIENT)
public final class CacheIconManager {
    private static final LinkedList<MaidModelInfo> MAID_CACHE_QUEUE = new LinkedList<>();
    private static final LinkedList<ChairModelInfo> CHAIR_CACHE_QUEUE = new LinkedList<>();
    private static final LinkedList<MaidModelGui.YsmMaidInfo> YSM_MAID_INFOS_QUEUE = new LinkedList<>();

    public static void clearCache() {
        MAID_CACHE_QUEUE.clear();
        CHAIR_CACHE_QUEUE.clear();
        YSM_MAID_INFOS_QUEUE.clear();
    }

    public static void addMaidPack(CustomModelPack<MaidModelInfo> customModelPack) {
        MAID_CACHE_QUEUE.addAll(customModelPack.getModelList());
    }

    public static void addChairPack(CustomModelPack<ChairModelInfo> customModelPack) {
        CHAIR_CACHE_QUEUE.addAll(customModelPack.getModelList());
    }

    public static void openMaidModelGui(EntityMaid maid) {
        MaidModelGui maidModelGui = new MaidModelGui(maid);
        if (MiscConfig.MODEL_ICON_CACHE.get() && !MAID_CACHE_QUEUE.isEmpty()) {
            var maidCacheScreen = getMaidCacheScreen(maidModelGui);
            Minecraft.getInstance().setScreen(maidCacheScreen);
        } else {
            Minecraft.getInstance().setScreen(maidModelGui);
        }
    }

    public static void openChairModelGui(EntityChair chair) {
        ChairModelGui chairModelGui = new ChairModelGui(chair);
        if (MiscConfig.MODEL_ICON_CACHE.get() && !CHAIR_CACHE_QUEUE.isEmpty()) {
            var chairCacheScreen = getChairCacheScreen(chairModelGui);
            Minecraft.getInstance().setScreen(chairCacheScreen);
        } else {
            Minecraft.getInstance().setScreen(chairModelGui);
        }
    }

    public static void openModelSwitcherModelGui(EntityMaid maid, TileEntityModelSwitcher.ModeInfo info, ModelSwitcherGui modelSwitcherGui) {
        ModelSwitcherModelGui switcherModelGui = new ModelSwitcherModelGui(maid, info, modelSwitcherGui);
        if (MiscConfig.MODEL_ICON_CACHE.get() && !MAID_CACHE_QUEUE.isEmpty()) {
            var maidCacheScreen = getMaidCacheScreen(switcherModelGui);
            Minecraft.getInstance().setScreen(maidCacheScreen);
        } else {
            Minecraft.getInstance().setScreen(switcherModelGui);
        }
    }

    private static CacheScreen<EntityMaid, MaidModelInfo> getMaidCacheScreen(AbstractModelGui<EntityMaid, MaidModelInfo> maidModelGui) {
        return new MaidCacheScreen(maidModelGui, InitEntities.MAID.get(), MAID_CACHE_QUEUE, (graphics, posX, posY, modelInfo, scaleModified, maid) -> {
            clearMaidDataResidue(maid, false);
            if (modelInfo.getEasterEgg() != null) {
                maid.setModelId(EASTER_EGG_MODEL);
            } else {
                maid.setModelId(modelInfo.getModelId().toString());
            }
            int scale = scaleModified / 2;
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,
                    posX + scale,
                    posY + scaleModified,
                    (int) (scale * modelInfo.getRenderItemScale()),
                    -25, -20, maid);
        });
    }

    private static CacheScreen<EntityChair, ChairModelInfo> getChairCacheScreen(ChairModelGui chairModelGui) {
        return new CacheScreen<>(chairModelGui, InitEntities.CHAIR.get(), CHAIR_CACHE_QUEUE, (graphics, posX, posY, modelInfo, scaleModified, chair) -> {
            chair.setModelId(modelInfo.getModelId().toString());
            int scale = scaleModified / 2;
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,
                    posX + scale,
                    posY + scaleModified - 5,
                    (int) (scale * modelInfo.getRenderItemScale() * 0.9),
                    -25, -20, chair);
        });
    }

    public static void buildYsmMaidInfos() {
        YSM_MAID_INFOS_QUEUE.clear();

        InitYsmMaidModelsEvent initYsmMaidModelsEvent = new InitYsmMaidModelsEvent();
        MinecraftForge.EVENT_BUS.post(initYsmMaidModelsEvent);

        List<MaidModelGui.YsmMaidInfo> ysmMaidInfos = Lists.newArrayList();
        for (MaidModelGui.YsmMaidBaseInfo model : initYsmMaidModelsEvent.getYsmModels()) {
            String modelIdString = translate(model.modelId());
            String textureNameString = translate(model.textureId());

            ResourceLocation cacheIconId = !isValidNamespace(modelIdString) || !isValidNamespace(textureNameString) ? null : createCacheIconId(modelIdString, textureNameString);

            ysmMaidInfos.add(new MaidModelGui.YsmMaidInfo(modelIdString, textureNameString, model.tooltips(), model.needAuth(), cacheIconId));
        }

        YSM_MAID_INFOS_QUEUE.addAll(ysmMaidInfos);
    }

    public static List<MaidModelGui.YsmMaidInfo> getYsmMaidInfos() {
        return YSM_MAID_INFOS_QUEUE;
    }

    public static String translate(String key) {
        return key;
    }

    static ResourceLocation createCacheIconId(String modeId, String textureName) {
        return new ResourceLocation("yes_steve_model", modeId + "/" + textureName + "/cache");
    }
}
