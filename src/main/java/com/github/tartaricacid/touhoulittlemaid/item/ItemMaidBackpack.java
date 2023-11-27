package com.github.tartaricacid.touhoulittlemaid.item;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.Optional;

public class ItemMaidBackpack extends Item {
    private static final Map<Integer, ItemMaidBackpack> ITEM_BY_LEVEL = Maps.newHashMap();
    private final int level;

    public ItemMaidBackpack(int level) {
        super((new Properties()).stacksTo(1));
        this.level = level;
        ITEM_BY_LEVEL.put(level, this);
    }

    public static Optional<ItemMaidBackpack> getInstance(int level) {
        return Optional.ofNullable(ITEM_BY_LEVEL.get(level));
    }

    public int getLevel() {
        return level;
    }
}
