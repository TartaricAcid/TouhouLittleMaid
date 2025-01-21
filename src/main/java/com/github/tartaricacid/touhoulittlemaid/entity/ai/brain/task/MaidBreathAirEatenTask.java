package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RangedWrapper;

import java.util.List;

/**
 * 女仆在水下，空气值不足时，会尝试吃任何可以补充空气的东西
 */
public class MaidBreathAirEatenTask extends Behavior<EntityMaid> {
    private static final int MAX_PROBABILITY = 5;

    public MaidBreathAirEatenTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        // 正在食用可呼吸的食物
        if (maid.getSwimManager().isEatBreatheItem()) {
            return false;
        }
        // 氧气值，默认最大是 300
        // 100 则意味着还有 5 秒呼吸时间
        if (maid.getAirSupply() >= 100) {
            return false;
        }
        // 拥有水下呼吸等效果
        if (MobEffectUtil.hasWaterBreathing(maid)) {
            return false;
        }
        // 溺水保护饰品
        if (hasDrownBauble(maid)) {
            return false;
        }
        // 没有在正在食用物品
        return !maid.isUsingItem();
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        this.eatBreatheItem(maid);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, EntityMaid maid, long gameTime) {
        return this.checkExtraStartConditions(level, maid);
    }

    private boolean hasDrownBauble(EntityMaid maid) {
        BaubleItemHandler maidBauble = maid.getMaidBauble();
        for (int i = 0; i < maidBauble.getSlots(); i++) {
            if (maidBauble.getStackInSlot(i).is(InitItems.DROWN_PROTECT_BAUBLE.get())) {
                return true;
            }
        }
        return false;
    }

    private void eatBreatheItem(EntityMaid maid) {
        // 先查询手部的物品能否吃：能就直接开吃，否就进行后续工作
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = maid.getItemInHand(hand);
            if (itemInHand.isEmpty()) {
                continue;
            }
            if (this.isBreatheFood(maid, itemInHand)) {
                this.startEatBreatheItem(maid, itemInHand, hand);
                return;
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
        for (int i = 0; i < backpackInv.getSlots(); i++) {
            ItemStack stack = backpackInv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (this.isBreatheFood(maid, stack)) {
                ItemStack foodStack = stack.copy();
                ItemStack handStack = itemInHand.copy();
                maid.setItemInHand(eanHand, foodStack);
                backpackInv.setStackInSlot(i, ItemStack.EMPTY);
                ItemHandlerHelper.insertItemStacked(backpackInv, handStack, false);
                maid.memoryHandItemStack(handStack);
                itemInHand = maid.getItemInHand(eanHand);
                hasFood = true;
                break;
            }
        }

        // 开吃
        if (hasFood) {
            this.startEatBreatheItem(maid, itemInHand, eanHand);
        }
    }

    private void startEatBreatheItem(EntityMaid maid, ItemStack stack, InteractionHand hand) {
        maid.getSwimManager().setEatBreatheItem(true);

        FoodProperties foodProperties = stack.getFoodProperties(maid);
        float total = 0;
        if (foodProperties != null) {
            int nutrition = foodProperties.getNutrition();
            float saturationModifier = foodProperties.getSaturationModifier();
            total = nutrition + nutrition * saturationModifier * 2;
        }
        maid.startUsingItem(hand);

        // 原版的熟牛肉之类的一般在 20 左右（除了迷之炖菜为 34.2）
        if (maid.getRandom().nextInt(MAX_PROBABILITY) < total) {
            float healCount = Math.max(total / MAX_PROBABILITY, 1);
            maid.heal(healCount);
            NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.HEAL, stack.getUseDuration()));
        }
    }

    private boolean isBreatheFood(EntityMaid maid, ItemStack stack) {
        // 寻找药水
        if (stack.getItem() instanceof PotionItem) {
            List<MobEffectInstance> mobEffects = PotionUtils.getMobEffects(stack);
            if (mobEffects.isEmpty()) {
                return false;
            }
            for (MobEffectInstance effect : mobEffects) {
                if (effect.getEffect() == MobEffects.WATER_BREATHING) {
                    return true;
                }
            }
            return false;
        }

        // 或者能提供水下呼吸的食物
        FoodProperties foodProperties = stack.getFoodProperties(maid);
        if (foodProperties == null) {
            return false;
        }
        List<Pair<MobEffectInstance, Float>> effects = foodProperties.getEffects();
        if (effects.isEmpty()) {
            return false;
        }
        for (Pair<MobEffectInstance, Float> effect : effects) {
            if (effect.getFirst().getEffect() == MobEffects.WATER_BREATHING) {
                return true;
            }
        }
        return false;
    }
}
