package com.github.tartaricacid.touhoulittlemaid.inventory;

import net.minecraftforge.items.ItemStackHandler;

public class AltarItemHandler extends ItemStackHandler {
    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
