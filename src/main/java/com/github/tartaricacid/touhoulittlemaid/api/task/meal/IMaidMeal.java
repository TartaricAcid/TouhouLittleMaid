package com.github.tartaricacid.touhoulittlemaid.api.task.meal;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public interface IMaidMeal {
    /**
     * 女仆能否吃下这个物品
     */
    boolean canMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand);

    /**
     * 女仆吃物品时的行为
     */
    void onMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand);
}
