package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;

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
        Activity activity = maid.getScheduleDetail();
        // 工作餐只能工作时间吃
        if (activity != Activity.WORK) {
            return;
        }
        EntityHandsInvWrapper availableInv = maid.getHandsInvWrapper();
        int slotIndex = -1;
        float maxValue = 0;
        for (int i = 0; i < availableInv.getSlots(); i++) {
            ItemStack stack = availableInv.getStackInSlot(i);
            FoodProperties foodProperties = stack.getFoodProperties(maid);
            if (foodProperties == null) {
                continue;
            }
            int nutrition = foodProperties.getNutrition();
            float saturationModifier = foodProperties.getSaturationModifier();
            float tmpValue = nutrition + nutrition * saturationModifier * 2;
            if (tmpValue > maxValue) {
                maxValue = tmpValue;
                slotIndex = i;
            }
        }
        if (slotIndex < 0 || slotIndex >= availableInv.getSlots()) {
            return;
        }
        ItemStack food = availableInv.getStackInSlot(slotIndex);
        InteractionHand hand = InteractionHand.values()[slotIndex];
        if (food.isEdible()) {
            maid.startUsingItem(hand);
            maid.eat(serverLevel, food);
            // 原版的熟牛肉之类的一般在 20 左右（除了迷之炖菜为 34.2）
            // 所以就以 24 作为 100% 考虑
            if (maid.getRandom().nextInt(24) < maxValue) {
                maid.getFavorabilityManager().apply(Type.WORK_MEAL);
            }
        }
    }
}
