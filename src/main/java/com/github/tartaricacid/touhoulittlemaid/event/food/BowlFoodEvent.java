package com.github.tartaricacid.touhoulittlemaid.event.food;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

@Mod.EventBusSubscriber
public class BowlFoodEvent {
    @SubscribeEvent
    public static void onAfterMaidEat(MaidAfterEatEvent event) {
        ItemStack foodAfterEat = event.getFoodAfterEat();
        EntityMaid maid = event.getMaid();
        if (foodAfterEat.getItem() instanceof BowlFoodItem && !foodAfterEat.isEmpty()) {
            CombinedInvWrapper availableInv = maid.getAvailableInv(false);
            ItemStack bowl = new ItemStack(Items.BOWL);
            ItemStack result = ItemHandlerHelper.insertItemStacked(availableInv, bowl, false);
            // 如果女仆背包满了，掉落在地上
            if (!result.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), bowl);
                maid.level.addFreshEntity(itemEntity);
            }
        }
    }
}
