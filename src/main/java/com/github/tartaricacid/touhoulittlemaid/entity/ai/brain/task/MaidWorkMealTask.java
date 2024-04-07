package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RangedWrapper;

import java.util.List;

public class MaidWorkMealTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 50;

    public MaidWorkMealTask() {
        super(ImmutableMap.of());
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        if (super.checkExtraStartConditions(serverLevel, maid)) {
            String workMealTypeName = Type.WORK_MEAL.getTypeName();
            FavorabilityManager manager = maid.getFavorabilityManager();
            return !maid.isSleeping() && manager.canAdd(workMealTypeName);
        }
        return false;
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        List<IMaidMeal> maidMeals = MaidMealManager.getMaidMeals(MaidMealType.WORK_MEAL);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = maid.getItemInHand(hand);

            emptyItemCheck:
            if (itemInHand.isEmpty()) {
                // 尝试在背包中寻找食物放入
                RangedWrapper backpackInv = maid.getAvailableBackpackInv();
                for (int i = 0; i < backpackInv.getSlots(); i++) {
                    ItemStack stack = backpackInv.getStackInSlot(i);
                    if (stack.isEmpty()) {
                        continue;
                    }
                    for (IMaidMeal maidMeal : maidMeals) {
                        if (maidMeal.canMaidEat(maid, stack, hand)) {
                            maid.setItemInHand(hand, stack.copy());
                            backpackInv.setStackInSlot(i, ItemStack.EMPTY);
                            itemInHand = maid.getItemInHand(hand);
                            break emptyItemCheck;
                        }
                    }
                }
                // 搜索完所有的物品都没有合适的，就不执行后续内容了
                continue;
            }

            for (IMaidMeal maidMeal : maidMeals) {
                if (maidMeal.canMaidEat(maid, itemInHand, hand)) {
                    maidMeal.onMaidEat(maid, itemInHand, hand);
                    return;
                }
            }
        }
    }
}
