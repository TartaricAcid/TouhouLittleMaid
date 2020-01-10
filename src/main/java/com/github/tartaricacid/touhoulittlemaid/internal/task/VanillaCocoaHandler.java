package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/11/9 14:59
 **/
public class VanillaCocoaHandler implements FarmHandler {
    private static final ResourceLocation NAME = new ResourceLocation(TouhouLittleMaid.MOD_ID, "cocoa");

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public double getMinDistanceSq() {
        return 9.0;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        // 棕色染料，就是可可豆，也认定为种子
        return item == Items.DYE && stack.getMetadata() == EnumDyeColor.BROWN.getDyeDamage();
    }

    @Override
    public boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        // 生长成熟的可可豆
        return block == Blocks.COCOA && state.getValue(BlockCocoa.AGE) >= 2;
    }

    @Override
    public void harvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        maid.destroyBlock(pos);
    }

    @Override
    public boolean canPlant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        // 此处方块可替换（比如草之类的方块）
        if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            return false;
        }

        // 可可豆可种植逻辑判断
        if (seed.getItem() == Items.DYE && seed.getMetadata() == EnumDyeColor.BROWN.getDyeDamage()) {
            // 遍历选取方块的四个面，看是不是丛林木
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                IBlockState state = world.getBlockState(pos.offset(facing));
                if (state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        IBlockState seedState = Blocks.AIR.getDefaultState();
        // 获取可可豆种植后应该放置的作物方块
        if (seed.getItem() == Items.DYE && seed.getMetadata() == EnumDyeColor.BROWN.getDyeDamage()) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                IBlockState state = world.getBlockState(pos.offset(facing));
                if (state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                    seedState = Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, facing);
                    break;
                }
            }
        }

        // 如果作物不为空，而且女仆成功放置了对应的作物方块
        if (seedState.getBlock() != Blocks.AIR && maid.placeBlock(pos, seedState)) {
            // 扣除物品
            seed.shrink(1);
        }

        // 返回扣除后的物品对象
        return seed;
    }
}
