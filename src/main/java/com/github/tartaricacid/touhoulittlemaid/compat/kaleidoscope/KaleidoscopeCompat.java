package com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope;

import com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.crop.RiceCropHandler;
import com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.edible.BlockFoodEdible;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.crop.SpecialCropManager;
import net.minecraftforge.fml.ModList;

public class KaleidoscopeCompat {
    public static final String COOKERY_ID = "kaleidoscope_cookery";

    public static void addCropHandlers(SpecialCropManager manager) {
        if (ModList.get().isLoaded(COOKERY_ID)) {
            RiceCropHandler.addCropHandlers(manager);
        }
    }

    public static void addBlockFoods(MaidEdibleBlockManager manager) {
        if (ModList.get().isLoaded(COOKERY_ID)) {
            manager.add(new BlockFoodEdible());
        }
    }
}
