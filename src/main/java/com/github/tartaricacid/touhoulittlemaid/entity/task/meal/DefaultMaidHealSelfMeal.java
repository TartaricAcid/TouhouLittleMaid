package com.github.tartaricacid.touhoulittlemaid.entity.task.meal;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class DefaultMaidHealSelfMeal implements IMaidMeal {
    private static final int MAX_PROBABILITY = 5;

    @Override
    public boolean canMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand) {
        return stack.isEdible() && !IMaidMeal.isBlockList(stack, MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST.get()) && !IMaidMeal.isBlockListMatch(stack);
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
            if (maid.getRandom().nextInt(MAX_PROBABILITY) < total) {
                float healCount = Math.max(total / MAX_PROBABILITY, 1);
                maid.heal(healCount);
                NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.HEAL, stack.getUseDuration()));
            }
        }
    }
}
