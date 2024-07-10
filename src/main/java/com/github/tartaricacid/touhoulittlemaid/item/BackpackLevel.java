package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackLevel;

public enum BackpackLevel implements IBackpackLevel {
    EMPTY_CAPACITY(6),
    SMALL_CAPACITY(12),
    MIDDLE_CAPACITY(24),
    BIG_CAPACITY(36),
    CRAFTING_TABLE_CAPACITY(18),
    FURNACE_CAPACITY(18),
    TANK_CAPACITY(18);

    private final int slotSize;

    BackpackLevel(int slotSize) {
        this.slotSize = slotSize;
    }

    @Override
    public int getSlotSize() {
        return slotSize;
    }
}
