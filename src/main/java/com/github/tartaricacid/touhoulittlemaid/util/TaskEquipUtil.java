package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import java.util.function.Predicate;

public final class TaskEquipUtil {
    private TaskEquipUtil() {
    }

    /**
     * 尝试将背包中满足条件的物品放入主手。
     * 若当前主手已满足条件，直接返回 true。
     */
    public static boolean tryEquipFromBackpack(EntityMaid maid, Predicate<ItemStack> predicate) {
        if (predicate.test(maid.getMainHandItem())) {
            return true;
        }
        RangedWrapper backpack = maid.getAvailableBackpackInv();
        int slot = ItemsUtil.findStackSlot(backpack, predicate::test);
        if (slot >= 0) {
            int count = backpack.getStackInSlot(slot).getCount();
            ItemStack output = backpack.extractItem(slot, count, false);
            if (!maid.getMainHandItem().isEmpty()) {
                ItemStack mainhand = maid.getMainHandItem();
                backpack.setStackInSlot(slot, mainhand);
            }
            maid.setItemInHand(InteractionHand.MAIN_HAND, output);
            return true;
        }
        return false;
    }

    /**
     * 将主手物品放回背包的第一个空槽位。
     *
     * @return 若成功移动物品进背包返回 true
     */
    public static boolean putMainHandBack(EntityMaid maid) {
        if (maid.getMainHandItem().isEmpty()) {
            return false;
        }
        RangedWrapper backpack = maid.getAvailableBackpackInv();
        ItemStack mainHandItem = maid.getMainHandItem();
        for (int i = 0; i < backpack.getSlots(); i++) {
            ItemStack stackInSlot = backpack.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                backpack.setStackInSlot(i, mainHandItem);
                maid.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                return true;
            }
        }
        return false;
    }
}
