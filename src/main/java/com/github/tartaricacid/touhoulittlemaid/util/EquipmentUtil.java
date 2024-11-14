package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EquipmentUtil {
    public static ItemStack getEquippedItem(LivingEntity entity, EquipmentSlot slot) {
        return entity.getItemBySlot(slot);
    }

    public static ItemStack getEquippedElytraItem(LivingEntity entity) {
        // 原版鞘翅
        var stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.getItem() == Items.ELYTRA) {
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
