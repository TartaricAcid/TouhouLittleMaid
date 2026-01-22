package com.github.tartaricacid.touhoulittlemaid.compat.jmc;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockManager;
import net.minecraftforge.fml.ModList;

public class JmcCompat {
    public static final String ID = "jmc";

    public static void addJmcEdible(MaidEdibleBlockManager manager) {
        if (ModList.get().isLoaded(ID)) {
            manager.add(new JmcEdible());
        }
    }
}
