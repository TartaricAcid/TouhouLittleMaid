package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import net.neoforged.neoforge.items.ItemStackHandler;

public class AltarItemHandler extends ItemStackHandler {
    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
