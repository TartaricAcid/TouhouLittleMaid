package com.github.tartaricacid.touhoulittlemaid.event.food;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAfterEatEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

@Mod.EventBusSubscriber
public class DefaultEatenEvent {
    @SubscribeEvent
    public static void onAfterMaidEat(MaidAfterEatEvent event) {
        ItemStack foodAfterEat = event.getFoodAfterEat();
        if (!foodAfterEat.isEmpty()) {
            ItemStack craftingRemainingItem = foodAfterEat.getCraftingRemainingItem();
            if (!craftingRemainingItem.isEmpty()) {
                EntityMaid maid = event.getMaid();
                CombinedInvWrapper availableInv = maid.getAvailableInv(false);
                ItemStack result = ItemHandlerHelper.insertItemStacked(availableInv, craftingRemainingItem, false);
                // 如果女仆背包满了，掉落在地上
                if (!result.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), craftingRemainingItem);
                    maid.level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void maidEatFinish(LivingEntityUseItemEvent.Finish event) {

        if (!(event.getEntity() instanceof EntityMaid)) return;
        // 防止一些模组食物食用不按照官方写法：
        // 数量为1直接返回容器，反之给予容器
        // let'do 系列就是直接给予容器的...
        if (!event.getItem().isEdible() && !event.getResultStack().isEmpty()) return;

        ItemStack craftingRemainingItem = event.getItem().getCraftingRemainingItem();
        if (craftingRemainingItem.isEmpty()) return;

        event.setResultStack(craftingRemainingItem);
    }
}