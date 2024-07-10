package com.github.tartaricacid.touhoulittlemaid.item;

public enum BackpackLevel {
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

    public int getSlotSize() {
        return slotSize;
    }
}
