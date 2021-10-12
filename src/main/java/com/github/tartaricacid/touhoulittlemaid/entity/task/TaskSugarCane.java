package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaskSugarCane implements IFarmTask {
    private static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "sugar_cane");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SUGAR_CANE.getDefaultInstance();
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Items.SUGAR_CANE;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        BlockState blockDownState = maid.level.getBlockState(cropPos.below());
        BlockState blockDown2State = maid.level.getBlockState(cropPos.below(2));
        return cropState.is(Blocks.SUGAR_CANE) && blockDownState.is(Blocks.SUGAR_CANE) && canSustainSugarCane(blockDown2State);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        maid.destroyBlock(cropPos);
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        World world = maid.level;
        BlockPos cropPos = basePos.above();
        if (!world.getBlockState(cropPos).getMaterial().isReplaceable() || world.getBlockState(cropPos).getMaterial().isLiquid()) {
            return false;
        }
        return canSustainSugarCane(baseState) && hasWaterSourceBlock(maid.level, basePos);
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() == Items.SUGAR_CANE) {
            maid.placeItemBlock(basePos.above(), seed);
        }
        return seed;
    }

    @Override
    public double getCloseEnoughDist() {
        return 2.0;
    }

    private boolean canSustainSugarCane(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL) || state.is(Blocks.SAND) || state.is(Blocks.RED_SAND);
    }

    private boolean hasWaterSourceBlock(World world, BlockPos basePos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState offsetState = world.getBlockState(basePos.relative(direction));
            FluidState fluidState = world.getFluidState(basePos.relative(direction));
            if (fluidState.is(FluidTags.WATER) || offsetState.is(Blocks.FROSTED_ICE)) {
                return true;
            }
        }
        return false;
    }
}
