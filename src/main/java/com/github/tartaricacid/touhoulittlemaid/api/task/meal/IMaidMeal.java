package com.github.tartaricacid.touhoulittlemaid.api.task.meal;

import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IMaidMeal {
    static boolean isBlockList(ItemStack food, List<String> blockList) {
        // 先过 tag 黑名单
        if (food.is(TagItem.MAID_EAT_BLOCKLIST_ITEM)) {
            return true;
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(food.getItem());
        return blockList.contains(key.toString());
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
