package com.github.tartaricacid.touhoulittlemaid.entity.task.meal;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.event.MaidMealRegConfigEvent;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class DefaultMaidHomeMeal implements IMaidMeal {
    private static final int MAX_PROBABILITY = 15;

    @Override
    public boolean canMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand) {
        return stack.isEdible() && !IMaidMeal.isBlockList(stack, MaidConfig.MAID_HOME_MEALS_BLOCK_LIST.get())
                && !IMaidMeal.isBlockList(stack, MaidMealRegConfigEvent.HOME_MEAL_REGEX);
    }

    @Override
    public void onMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand) {
        FoodProperties foodProperties = stack.getFoodProperties(maid);
        if (foodProperties != null) {
            maid.startUsingItem(hand);
            int nutrition = foodProperties.getNutrition();
            float saturationModifier = foodProperties.getSaturationModifier();
            float total = nutrition + nutrition * saturationModifier * 2;
            // 原版的熟牛肉之类的一般在 20 左右（除了迷之炖菜为 34.2）
            int point = Math.round(total) / MAX_PROBABILITY;
            float tailPoint = total - point * MAX_PROBABILITY;
            if (0 < tailPoint && maid.getRandom().nextInt(MAX_PROBABILITY) < tailPoint) {
                point = point + 1;
            }
            maid.getFavorabilityManager().apply(Type.HOME_MEAL, point);
            if (point > 0) {
                NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.HEART, stack.getUseDuration()));
            }
        }
    }
}
