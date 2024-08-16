package com.github.tartaricacid.touhoulittlemaid.inventory.chest;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
//import com.github.tartaricacid.touhoulittlemaid.compat.ironchest.IronChestType;
import com.github.tartaricacid.touhoulittlemaid.compat.ironchest.IronChestType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class ChestManager {
    private static List<IChestType> ALL_CHEST_TYPES;

    public ChestManager() {
        ALL_CHEST_TYPES = Lists.newArrayList();
    }

    public static void init() {
        ChestManager manager = new ChestManager();
        manager.add(new VanillaChestType());
        manager.add(new BarrelChestType());
        IronChestType.register(manager);
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addChestType(manager);
        }
        ALL_CHEST_TYPES = ImmutableList.copyOf(ALL_CHEST_TYPES);
    }

    public void add(IChestType chestType) {
        ALL_CHEST_TYPES.add(chestType);
    }

    public static List<IChestType> getAllChestTypes() {
        return ALL_CHEST_TYPES;
    }
}
