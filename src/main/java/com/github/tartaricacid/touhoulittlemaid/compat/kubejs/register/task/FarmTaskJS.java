package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmPlantTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmSurroundingMoveTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.tartaricacid.touhoulittlemaid.util.functional.QuadFunction;
import com.github.tartaricacid.touhoulittlemaid.util.functional.QuadPredicate;
import com.github.tartaricacid.touhoulittlemaid.util.functional.TriConsumer;
import com.github.tartaricacid.touhoulittlemaid.util.functional.TriPredicate;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FarmTaskJS implements IFarmTask {
    private final Builder builder;

    public FarmTaskJS(Builder builder) {
        this.builder = builder;
    }

    @Override
    public ResourceLocation getUid() {
        return builder.id;
    }

    @Override
    public ItemStack getIcon() {
        return builder.icon;
    }

    @Override
    @Nullable
    public SoundEvent getAmbientSound(EntityMaid maid) {
        if (this.builder.sound == null) {
            return SoundUtil.attackSound(maid, InitSounds.MAID_FARM.get(), 0.5f);
        }
        return this.builder.sound;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        // 使用带 3x3x1 的范围的寻路任务
        MaidFarmSurroundingMoveTask maidFarmSurroundingMoveTask = new MaidFarmSurroundingMoveTask(this, 0.6f);
        MaidFarmPlantTask maidFarmPlantTask = new MaidFarmPlantTask(this);
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(Pair.of(5, maidFarmSurroundingMoveTask), Pair.of(6, maidFarmPlantTask));
        for (var pair : this.builder.brains) {
            tasks.add(Pair.of(pair.getFirst(), pair.getSecond().apply(this, maid)));
        }
        return tasks;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        if (this.builder.enable == null) {
            return true;
        }
        return this.builder.enable.test(maid);
    }

    @Override
    public boolean isHidden(EntityMaid maid) {
        if (this.builder.isHidden == null) {
            return false;
        }
        return this.builder.isHidden.test(maid);
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        if (this.builder.enableLookAndRandomWalk == null) {
            return true;
        }
        return this.builder.enableLookAndRandomWalk.test(maid);
    }

    @Override
    public boolean enableEating(EntityMaid maid) {
        if (this.builder.enableEating == null) {
            return true;
        }
        return this.builder.enableEating.test(maid);
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return this.builder.enableConditionDesc;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return this.builder.conditionDesc;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        if (this.builder.isSeed == null) {
            return false;
        }
        return this.builder.isSeed.test(stack);
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.builder.canHarvest == null) {
            return false;
        }
        return this.builder.canHarvest.test(maid, cropPos, cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.builder.harvest == null) {
            return;
        }
        this.builder.harvest.accept(maid, cropPos, cropState);
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (this.builder.canPlant == null) {
            return false;
        }
        return this.builder.canPlant.test(maid, basePos, baseState, seed);
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (this.builder.plant == null) {
            return seed;
        }
        return this.builder.plant.apply(maid, basePos, baseState, seed);
    }

    @Override
    public double getCloseEnoughDist() {
        return this.builder.closeEnoughDist;
    }

    @Override
    public boolean checkCropPosAbove() {
        return this.builder.checkCropPosAbove;
    }

    public static class Builder extends TaskBuilder<Builder, FarmTaskJS> {
        private @Nullable Predicate<ItemStack> isSeed = null;
        private @Nullable TriPredicate<EntityMaid, BlockPos, BlockState> canHarvest = null;
        private @Nullable TriConsumer<EntityMaid, BlockPos, BlockState> harvest = null;
        private @Nullable QuadPredicate<EntityMaid, BlockPos, BlockState, ItemStack> canPlant = null;
        private @Nullable QuadFunction<EntityMaid, BlockPos, BlockState, ItemStack, ItemStack> plant = null;
        private double closeEnoughDist = 2;
        private boolean checkCropPosAbove = true;

        public Builder(ResourceLocation id, ItemStack icon) {
            super(id, icon);
        }

        @Info("""
                Check if the item stack is a seed. Used for the canPlant and plant methods. Mandatory. <br>
                判断是否是种子，用于后续的 canPlant 和 plant 方法传参。必填项。
                """)
        public FarmTaskJS.Builder isSeed(Predicate<ItemStack> isSeed) {
            this.isSeed = isSeed;
            return this;
        }

        @Info("""
                Check if the maid can harvest the crop at the given position. Mandatory. <br>
                判断女仆是否可以在指定位置收割作物。必填项。
                """)
        public FarmTaskJS.Builder canHarvest(TriPredicate<EntityMaid, BlockPos, BlockState> canHarvest) {
            this.canHarvest = canHarvest;
            return this;
        }

        @Info("""
                Harvest the crop at the given position. Mandatory. <br>
                收割指定位置的作物。必填项。
                """)
        public FarmTaskJS.Builder harvest(TriConsumer<EntityMaid, BlockPos, BlockState> harvest) {
            this.harvest = harvest;
            return this;
        }

        @Info("""
                Check if the maid can plant a seed at the given position. Mandatory. <br>
                判断女仆是否可以在指定位置种植种子。必填项。
                """)
        public FarmTaskJS.Builder canPlant(QuadPredicate<EntityMaid, BlockPos, BlockState, ItemStack> canPlant) {
            this.canPlant = canPlant;
            return this;
        }

        @Info("""
                Plant a seed at the given position and return the remaining seed stack. Mandatory. <br>
                在指定位置种植种子，并返回剩余的种子物品。必填项。
                """)
        public FarmTaskJS.Builder plant(QuadFunction<EntityMaid, BlockPos, BlockState, ItemStack, ItemStack> plant) {
            this.plant = plant;
            return this;
        }

        @Info("""
                The distance at which the maid considers herself close enough to the crop. <br>
                Only when the distance to the target block is less than or equal to this value will the planting/harvesting logic be executed. <br>
                Default is 2 blocks. <br>
                女仆认为自己离作物足够近的距离，当距离目标方块小于等于此值时才会执行种植/收割逻辑，默认为 2 格。
                """)
        public FarmTaskJS.Builder closeEnoughDist(double closeEnoughDist) {
            this.closeEnoughDist = closeEnoughDist;
            return this;
        }

        @Info("""
                Check if there are two blocks of space above the target position for the maid to reach. Default is true. <br>
                检查目标上面是否有两格空间能容纳女仆到达，默认为 true。
                """)
        public FarmTaskJS.Builder checkCropPosAbove(boolean checkCropPosAbove) {
            this.checkCropPosAbove = checkCropPosAbove;
            return this;
        }
    }
}
