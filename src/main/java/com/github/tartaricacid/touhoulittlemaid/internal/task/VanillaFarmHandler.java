package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import com.github.tartaricacid.touhoulittlemaid.util.EmptyBlockReader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class VanillaFarmHandler implements FarmHandler {

    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof IPlantable) {
            if (item == Items.NETHER_WART) {
                return true;
            }
            IPlantable plantable = (IPlantable) stack.getItem();
            if (plantable.getPlantType(EmptyBlockReader.INSTANCE, BlockPos.ORIGIN) == EnumPlantType.Crop && plantable.getPlant(EmptyBlockReader.INSTANCE, BlockPos.ORIGIN).getBlock() != Blocks.AIR) {
                return true;
            }
        }
        if (item == Items.DYE && stack.getMetadata() == EnumDyeColor.BROWN.getDyeDamage()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        if (block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(state)) {
            return true;
        }
        if (block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN) {
            Block stem = block == Blocks.MELON_BLOCK ? Blocks.MELON_STEM : Blocks.PUMPKIN_STEM;
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (world.getBlockState(pos.offset(facing)).getBlock() == stem) {
                    return true;
                }
            }
        }
        if (block == Blocks.COCOA && state.getValue(BlockCocoa.AGE) >= 2) {
            return true;
        }
        if (block == Blocks.NETHER_WART && state.getValue(BlockNetherWart.AGE) >= 3) {
            return true;
        }
        return false;
    }

    @Override
    public void harvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        maid.destroyBlock(pos);
    }

    @Override
    public boolean canPlant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
            return false;
        if (seed.getItem() == Items.DYE && seed.getMetadata() == EnumDyeColor.BROWN.getDyeDamage()) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                IBlockState state = world.getBlockState(pos.offset(facing));
                if (state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                    return true;
                }
            }
            return false;
        }
        IPlantable plantable = (IPlantable) seed.getItem();
        IBlockState soil = world.getBlockState(pos.down());
        return soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, plantable);
    }

    @Override
    public ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        IBlockState seedState = Blocks.AIR.getDefaultState();
        if (seed.getItem() == Items.DYE && seed.getMetadata() == EnumDyeColor.BROWN.getDyeDamage()) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                IBlockState state = world.getBlockState(pos.offset(facing));
                if (state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                    seedState = Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.FACING, facing);
                    break;
                }
            }
        }
        else {
            IPlantable plantable = (IPlantable) seed.getItem();
            seedState = plantable.getPlant(world, pos);
        }
        if (seedState.getBlock() != Blocks.AIR && maid.placeBlock(pos, seedState)) {
            seed.shrink(1);
        }
        return seed;
    }

}
