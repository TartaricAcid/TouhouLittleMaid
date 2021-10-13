package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;

import javax.annotation.Nonnull;

public class MaidHandsInvWrapper extends EntityHandsInvWrapper {
    public MaidHandsInvWrapper(LivingEntity entity) {
        super(entity);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return EntityMaid.canInsertItem(stack);
    }
}
