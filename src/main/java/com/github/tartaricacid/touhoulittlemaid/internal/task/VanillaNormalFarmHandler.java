package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import com.github.tartaricacid.touhoulittlemaid.util.EmptyBlockReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

/**
 * 用于女仆在农场模式下的判定收割、种植行为的接口
 *
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public class VanillaNormalFarmHandler implements FarmHandler {
    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        // IPlantable 对象判定
        if (item instanceof IPlantable) {
            // 地狱疣认定为种子
            if (item == Items.NETHER_WART) {
                return true;
            }
            IPlantable plantable = (IPlantable) stack.getItem();
            // getPlantType 为 Crop，且 getPlant 不为空的，也认定为种子
            return plantable.getPlantType(EmptyBlockReader.INSTANCE, BlockPos.ORIGIN) == EnumPlantType.Crop
                    && plantable.getPlant(EmptyBlockReader.INSTANCE, BlockPos.ORIGIN).getBlock() != Blocks.AIR;
        }
        return false;
    }

    @Override
    public boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        // 继承自 BlockCrops，且达到了最大生长周期
        if (block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(state)) {
            return true;
        }

        // 地狱疣
        return block == Blocks.NETHER_WART && state.getValue(BlockNetherWart.AGE) >= 3;
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

        // IPlantable 对象的判定种植
        // 此处同时应用到了地狱疣和普通作物
        IPlantable plantable = (IPlantable) seed.getItem();
        IBlockState soil = world.getBlockState(pos.down());
        return soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, plantable);
    }

    @Override
    public ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        // IPlantable 对象种植后应该放置的作物方块
        // 此处同时应用到了地狱疣和普通作物
        IPlantable plantable = (IPlantable) seed.getItem();
        IBlockState seedState = plantable.getPlant(world, pos);

        // 如果作物不为空，而且女仆成功放置了对应的作物方块
        if (seedState.getBlock() != Blocks.AIR && maid.placeBlock(pos, seedState)) {
            // 扣除物品
            seed.shrink(1);
        }

        // 返回扣除后的物品对象
        return seed;
    }
}
