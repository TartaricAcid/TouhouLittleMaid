package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface FarmHandler extends TaskHandler {
    boolean isSeed(ItemStack stack);

    boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state);

    void harvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state);

    boolean canPlant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed);

    ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed);

    @Override
    default boolean canExecute(AbstractEntityMaid maid) {
        return true;
    }
}
