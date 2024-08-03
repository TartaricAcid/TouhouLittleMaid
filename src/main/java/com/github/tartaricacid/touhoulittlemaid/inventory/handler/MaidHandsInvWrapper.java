package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.EntityHandsInvWrapper;

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
