package com.github.tartaricacid.touhoulittlemaid.entity.ai.edible;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.compat.farmersdelight.FarmersDelightCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.jmc.JmcCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.KaleidoscopeCompat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class MaidEdibleBlockManager {
    private static List<IMaidEdibleBlock> EDIBLE_BLOCKS = Lists.newArrayList();

    private MaidEdibleBlockManager() {
    }

    public static void init() {
        MaidEdibleBlockManager manager = new MaidEdibleBlockManager();

        manager.add(new CakeEdible());

        KaleidoscopeCompat.addBlockFoods(manager);
        JmcCompat.addJmcEdible(manager);
        FarmersDelightCompat.addFarmersDelightEdible(manager);

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerMaidEdibleBlock(manager);
        }
        EDIBLE_BLOCKS = ImmutableList.copyOf(EDIBLE_BLOCKS);
    }

    public void add(IMaidEdibleBlock edibleBlock) {
        EDIBLE_BLOCKS.add(edibleBlock);
    }

    public static List<IMaidEdibleBlock> getEdibleBlocks() {
        return EDIBLE_BLOCKS;
    }
}
