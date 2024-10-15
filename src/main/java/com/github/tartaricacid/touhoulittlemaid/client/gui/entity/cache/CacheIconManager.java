package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.LinkedList;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

@OnlyIn(Dist.CLIENT)
public final class CacheIconManager {
    private static final LinkedList<MaidModelInfo> MAID_CACHE_QUEUE = new LinkedList<>();
    private static final LinkedList<ChairModelInfo> CHAIR_CACHE_QUEUE = new LinkedList<>();

    public static void clearCache() {
        MAID_CACHE_QUEUE.clear();
        CHAIR_CACHE_QUEUE.clear();
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
        return new CacheScreen<>(maidModelGui, InitEntities.MAID.get(), MAID_CACHE_QUEUE, (graphics, posX, posY, modelInfo, scaleModified, maid) -> {
            clearMaidDataResidue(maid, false);
            if (modelInfo.getEasterEgg() != null) {
                maid.setModelId(EASTER_EGG_MODEL);
            } else {
                maid.setModelId(modelInfo.getModelId().toString());
            }

            int half = scaleModified / 2;
            int yOffset = scaleModified * 6 / 5;
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    posX,
                    posY - 3,
                    posX + scaleModified,
                    posY + yOffset - 3,
                    (int) (half * modelInfo.getRenderItemScale()),
                    0.1F,
                    posX + half + 25,
                    posY + yOffset / 2f + 15,
                    maid);
        });
    }

    private static CacheScreen<EntityChair, ChairModelInfo> getChairCacheScreen(ChairModelGui chairModelGui) {
        return new CacheScreen<>(chairModelGui, InitEntities.CHAIR.get(), CHAIR_CACHE_QUEUE, (graphics, posX, posY, modelInfo, scaleModified, chair) -> {
            chair.setModelId(modelInfo.getModelId().toString());

            int half = scaleModified / 2;
            int yOffset = scaleModified * 3 / 2;
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    posX,
                    posY,
                    posX + scaleModified,
                    posY + yOffset,
                    (int) (half * modelInfo.getRenderItemScale() * 0.9),
                    0.1F,
                    posX + half + 20,
                    posY + yOffset / 2f + 10,
                    chair);
        });
    }
}
