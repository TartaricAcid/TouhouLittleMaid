package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;

public class BackpackManager {
    private static Map<ResourceLocation, IMaidBackpack> BACKPACK_ID_MAP;
    private static Map<Item, IMaidBackpack> BACKPACK_ITEM_MAP;

    private BackpackManager() {
        BACKPACK_ID_MAP = Maps.newHashMap();
        BACKPACK_ITEM_MAP = Maps.newHashMap();
    }

    public static void init() {
        BackpackManager manager = new BackpackManager();
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addMaidBackpack(manager);
        }
        BACKPACK_ID_MAP = ImmutableMap.copyOf(BACKPACK_ID_MAP);
        BACKPACK_ITEM_MAP = ImmutableMap.copyOf(BACKPACK_ITEM_MAP);
    }

    public boolean isBackpackItem(Item item) {
        return BACKPACK_ITEM_MAP.containsKey(item);
    }

    public void add(IMaidBackpack backpack) {
        BACKPACK_ID_MAP.put(backpack.getId(), backpack);
        BACKPACK_ITEM_MAP.put(backpack.getItem(), backpack);
    }
}
