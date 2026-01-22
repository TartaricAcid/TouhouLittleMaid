package com.github.tartaricacid.touhoulittlemaid.compat.farmersdelight;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockManager;
import net.minecraftforge.fml.ModList;

public class FarmersDelightCompat {
    public static final String ID = "farmersdelight";

    public static void addFarmersDelightEdible(MaidEdibleBlockManager manager) {
        if (ModList.get().isLoaded(ID)) {
            manager.add(new FarmersDelightEdible());
        }
    }
}
