package com.github.tartaricacid.touhoulittlemaid.api.task.meal;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public interface IMaidMeal {
    static boolean isBlockList(ItemStack food, List<String> blockList) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(food.getItem());
        return key == null || blockList.contains(key.toString());
    }

    /**
     * 女仆能否吃下这个物品
     */
    boolean canMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand);

    /**
     * 女仆吃物品时的行为
     */
    void onMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand);
}
