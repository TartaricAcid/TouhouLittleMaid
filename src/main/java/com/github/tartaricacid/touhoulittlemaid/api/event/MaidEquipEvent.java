package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class MaidEquipEvent extends LivingEvent {
    private final EntityMaid maid;
    private final EquipmentSlot slot;
    private final ItemStack stack;

    public MaidEquipEvent(EntityMaid maid, EquipmentSlot slot, ItemStack stack) {
        super(maid);
        this.maid = maid;
        this.slot = slot;
        this.stack = stack;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public ItemStack getStack() {
        return stack;
    }
}
