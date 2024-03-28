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
    }
}
