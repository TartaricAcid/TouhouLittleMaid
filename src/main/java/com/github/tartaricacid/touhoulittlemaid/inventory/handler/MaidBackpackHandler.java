package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MaidBackpackHandler extends ItemStackHandler {
    public MaidBackpackHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return EntityMaid.canInsertItem(stack);
    }
}
