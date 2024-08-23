package com.github.tartaricacid.touhoulittlemaid.entity.task.meal;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.event.MaidMealRegConfigEvent;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticlePackage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class DefaultMaidHealSelfMeal implements IMaidMeal {
    private static final int MAX_PROBABILITY = 5;

    @Override
    public boolean canMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand) {
        return stack.getFoodProperties(null) != null && !IMaidMeal.isBlockList(stack, MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST.getPath())
                && !IMaidMeal.isBlockList(stack, MaidMealRegConfigEvent.HEAL_MEAL_REGEX);
    }

    @Override
    public void onMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand) {
        FoodProperties foodProperties = stack.getFoodProperties(maid);
        if (foodProperties != null) {
            maid.startUsingItem(hand);
            int nutrition = foodProperties.nutrition();
            float saturation = foodProperties.saturation();
            // 1.21 修改了营养值数值大小和相关计算，删去了 saturationModifier，改为直接加和
            float total = nutrition + saturation;
            // 原版的熟牛肉之类的一般在 20 左右（除了迷之炖菜为 34.2）
            if (maid.getRandom().nextInt(MAX_PROBABILITY) < total) {
                float healCount = Math.max(total / MAX_PROBABILITY, 1);
                maid.heal(healCount);
                NetworkHandler.sendToNearby(maid, new SpawnParticlePackage(maid.getId(), SpawnParticlePackage.Type.HEAL, stack.getUseDuration(maid)));
            }
        }
    }
}
