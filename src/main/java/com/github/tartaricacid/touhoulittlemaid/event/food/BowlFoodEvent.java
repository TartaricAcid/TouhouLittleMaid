package com.github.tartaricacid.touhoulittlemaid.event.food;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

@EventBusSubscriber
public class BowlFoodEvent {
    @SubscribeEvent
    public static void onAfterMaidEat(MaidAfterEatEvent event) {
        ItemStack foodAfterEat = event.getFoodAfterEat();
        EntityMaid maid = event.getMaid();
        FoodProperties foodProperties = foodAfterEat.getItem().getFoodProperties(foodAfterEat.getItem().getDefaultInstance(), maid);
        if (foodProperties != null) {
            ItemStack itemStack = foodProperties.usingConvertsTo().get();
            if (!itemStack.isEmpty() && !foodAfterEat.isEmpty()) {
                CombinedInvWrapper availableInv = maid.getAvailableInv(false);
                ItemStack result = ItemHandlerHelper.insertItemStacked(availableInv, itemStack, false);
                // 如果女仆背包满了，掉落在地上
                if (!result.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), itemStack);
                    maid.level.addFreshEntity(itemEntity);
                }
            }
        }
    }
}
