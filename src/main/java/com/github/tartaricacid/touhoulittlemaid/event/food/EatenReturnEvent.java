package com.github.tartaricacid.touhoulittlemaid.event.food;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.event.MaidEatenRetConEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.Map;

@Mod.EventBusSubscriber
public class EatenReturnEvent {
    @SubscribeEvent
    public static void onAfterMaidEat(MaidAfterEatEvent event) {
        ItemStack foodAfterEat = event.getFoodAfterEat();
        EntityMaid maid = event.getMaid();

        Class<? extends Item> aClass = foodAfterEat.getItem().getClass();
        Map<Class<?>, ItemStack> classItemStackHashMap = MaidEatenRetConEvent.EATEN_RETURN_MAP;
        for (Class<?> aClass1 : classItemStackHashMap.keySet()) {
            if (aClass1.isAssignableFrom(aClass) && !foodAfterEat.isEmpty()) {
                CombinedInvWrapper availableInv = maid.getAvailableInv(false);
                ItemStack itemStack = classItemStackHashMap.get(aClass1);
                ItemStack result = ItemHandlerHelper.insertItemStacked(availableInv, itemStack, false);
                // 如果女仆背包满了，掉落在地上
                if (!result.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), itemStack);
                    maid.level.addFreshEntity(itemEntity);
                }
                return;
            }
        }
    }
}