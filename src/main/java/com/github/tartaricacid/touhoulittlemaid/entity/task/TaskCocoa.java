package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmPlantTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmSurroundingMoveTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TaskCocoa implements IFarmTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "cocoa");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.COCOA_BEANS.getDefaultInstance();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidFarmSurroundingMoveTask maidFarmSurroundingMoveTask = new MaidFarmSurroundingMoveTask(this, 0.6f);
        MaidFarmPlantTask maidFarmPlantTask = new MaidFarmPlantTask(this);
        return Lists.newArrayList(Pair.of(5, maidFarmSurroundingMoveTask), Pair.of(6, maidFarmPlantTask));
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Items.COCOA_BEANS;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.is(Blocks.COCOA) && cropState.getValue(CocoaBlock.AGE) >= 2;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        maid.destroyBlock(cropPos);
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        basePos = basePos.above();
        baseState = maid.level.getBlockState(basePos);
        if (baseState.is(BlockTags.JUNGLE_LOGS) && seed.getItem() == Items.COCOA_BEANS) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState state = maid.level.getBlockState(basePos.relative(direction));
                if (state.canBeReplaced()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() == Items.COCOA_BEANS) {
            basePos = basePos.above();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos directionPos = basePos.relative(direction);
                if (!seed.isEmpty() && maid.canPlaceBlock(directionPos)) {
                    Level world = maid.level();
                    BlockState cocoaState = Blocks.COCOA.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
                    world.setBlock(directionPos, cocoaState, Block.UPDATE_ALL_IMMEDIATE);
                    SoundType soundType = cocoaState.getSoundType(world, directionPos, maid);
                    world.playSound(null, directionPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    seed.shrink(1);
                }
            }
        }
        return seed;
    }

    @Override
    public double getCloseEnoughDist() {
        return 2.5;
    }

    @Override
    public boolean checkCropPosAbove() {
        return false;
    }
}
