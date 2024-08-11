package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RangedWrapper;

import java.util.List;

public class MaidHealSelfTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 50;
    // TODO 配置文件管控
    private static final int MAX_CHECK_MISSING_HEATH = 2;

    public MaidHealSelfTask() {
        super(ImmutableMap.of());
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        if (super.checkExtraStartConditions(serverLevel, maid)) {
            float missingHealth = maid.getMaxHealth() - maid.getHealth();
            return !maid.isSleeping() && missingHealth >= MAX_CHECK_MISSING_HEATH;
        }
        return false;
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        List<IMaidMeal> maidMeals = MaidMealManager.getMaidMeals(MaidMealType.HEAL_MEAL);

        // 先查询手部的物品能否吃：能就直接开吃，否就进行后续工作
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = maid.getItemInHand(hand);

            if (itemInHand.isEmpty()) {
                continue;
            }

            for (IMaidMeal maidMeal : maidMeals) {
                if (maidMeal.canMaidEat(maid, itemInHand, hand)) {
                    maidMeal.onMaidEat(maid, itemInHand, hand);
                    return;
                }
            }
        }

        // 对手部进行处理：如果没有空的手部，那就取副手
        InteractionHand eanHand = InteractionHand.OFF_HAND;
        for (InteractionHand hand : InteractionHand.values()) {
            if (maid.getItemInHand(hand).isEmpty()) {
                eanHand = hand;
                break;
            }
        }
        ItemStack itemInHand = maid.getItemInHand(eanHand);

        // 尝试在背包中寻找食物放入
        boolean hasFood = false;
        RangedWrapper backpackInv = maid.getAvailableBackpackInv();
        swapItemCheck:
        for (int i = 0; i < backpackInv.getSlots(); i++) {
            ItemStack stack = backpackInv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            for (IMaidMeal maidMeal : maidMeals) {
                if (maidMeal.canMaidEat(maid, stack, eanHand)) {
                    ItemStack foodStack = stack.copy();
                    ItemStack handStack = itemInHand.copy();
                    maid.setItemInHand(eanHand, foodStack);
                    backpackInv.setStackInSlot(i, ItemStack.EMPTY);
                    ItemHandlerHelper.insertItemStacked(backpackInv, handStack, false);
                    itemInHand = maid.getItemInHand(eanHand);
                    hasFood = true;
                    break swapItemCheck;
                }
            }
        }

        // 开吃
        if (hasFood) {
            for (IMaidMeal maidMeal : maidMeals) {
                if (maidMeal.canMaidEat(maid, itemInHand, eanHand)) {
                    maidMeal.onMaidEat(maid, itemInHand, eanHand);
                    return;
                }
            }
        }
    }
}
