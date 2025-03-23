package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraftforge.items.wrapper.RangedWrapper;

import java.util.List;
import java.util.Optional;

/**
 * 女仆在水下，空气值不足时，会尝试吃任何可以补充空气的东西
 * 如果没有找到，则会尝试寻找可以呼吸的位置
 */
public class MaidBreathAirTask extends Behavior<EntityMaid> {
    private static final int MAX_PROBABILITY = 5;
    /**
     * 数值过大可能性能不流畅
     */
    private static final int AIR_SEARCH_RANGE = 16;

    public MaidBreathAirTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        // 正在食用可呼吸的食物
        if (maid.getSwimManager().isEatBreatheItem()) {
            return false;
        }
        // 如果正在上浮但是失去目标也是需要重新计算的。目标存在时可以不需要再次计算
        if (maid.getSwimManager().isGoingToBreath() && maid.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            BlockPos target = maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get().getTarget().currentBlockPosition();
            // 有可能在途中被其他任务覆盖呼吸目标点，还是吸口气比较要紧
            if (givesAir(maid, target)) {
                // 不明原因出现的莫名其妙重寻路导致生成一条新的不可达路线
                if (!maid.getNavigation().isDone() || maid.blockPosition().distManhattan(target) <= 1) {
                    return false;
                }
            }
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
        if (this.eatBreatheItem(maid)) {
            return;
        }
        this.findAirPosition(level, maid);
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

    private boolean eatBreatheItem(EntityMaid maid) {
        // 先查询手部的物品能否吃：能就直接开吃，否就进行后续工作
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = maid.getItemInHand(hand);
            if (itemInHand.isEmpty()) {
                continue;
            }
            if (this.isBreatheFood(maid, itemInHand)) {
                this.startEatBreatheItem(maid, itemInHand, hand);
                return true;
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
                ItemStack foodStack = backpackInv.extractItem(i, backpackInv.getStackInSlot(i).getCount(), false);
                ItemStack handStack = itemInHand.copy();
                maid.setItemInHand(eanHand, foodStack);
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
        return hasFood;
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


    // 寻找可呼吸新鲜空气的地方
    private void findAirPosition(ServerLevel level, EntityMaid maid) {
        if (!maid.canBrainMoving()) {
            return;
        }

        NodeEvaluator nodeEvaluator = maid.getNavigation().getNodeEvaluator();
        var pathFinding = new MaidPathFindingBFS(nodeEvaluator, level, maid, AIR_SEARCH_RANGE, AIR_SEARCH_RANGE);
        Optional<BlockPos> match = pathFinding.find(blockPos -> this.givesAir(maid, blockPos));
        pathFinding.finish();

        // FIXME: BFS 算法找到的目标点在 A* 算法中可能会需要更多步骤才能走到，当超过了寻路长度后可能会被截断导致无法找到路径
        if (match.isPresent() && maid.canPathReach(match.get())) {
            maid.getSwimManager().setGoingToBreath(true);
            BehaviorUtils.setWalkAndLookTargetMemories(maid, match.get(), 0.5f, 1);
            return;
        }

        // 当前女仆坐标的海平面位置
        BlockPos.MutableBlockPos seaLevelPos = maid.blockPosition().mutable().setY(level.getSeaLevel() + 1);
        if (this.givesAir(maid, seaLevelPos) && maid.canPathReach(seaLevelPos)) {
            maid.getSwimManager().setGoingToBreath(true);
            BehaviorUtils.setWalkAndLookTargetMemories(maid, seaLevelPos, 0.5f, 1);
            return;
        }

        // 当前女仆坐标的海平面 5x5x1 区域
        final int seaLevelOffset = 2;
        Iterable<BlockPos> canBreathPos = BlockPos.betweenClosed(
                seaLevelPos.getX() - seaLevelOffset,
                seaLevelPos.getY(),
                seaLevelPos.getZ() - seaLevelOffset,
                seaLevelPos.getX() + seaLevelOffset,
                seaLevelPos.getY(),
                seaLevelPos.getZ() + seaLevelOffset);
        for (BlockPos canBreathPo : canBreathPos) {
            if (this.givesAir(maid, canBreathPo) && maid.canPathReach(canBreathPo)) {
                maid.getSwimManager().setGoingToBreath(true);
                BehaviorUtils.setWalkAndLookTargetMemories(maid, canBreathPo, 0.5f, 1);
                return;
            }
        }
    }

    // 提供空气的判断
    // 反正女仆也钻不进一格的高度（寻路困难），直接判断两格的空气，避免寻路判断发生的故障
    private boolean givesAir(EntityMaid maid, BlockPos pos) {
        Level level = maid.level;
        BlockState blockState = level.getBlockState(pos);
        boolean noFluid = level.getFluidState(pos).isEmpty() || blockState.is(Blocks.BUBBLE_COLUMN);
        return noFluid && blockState.getBlock().isPathfindable(blockState, level, pos, PathComputationType.LAND);
    }
}
