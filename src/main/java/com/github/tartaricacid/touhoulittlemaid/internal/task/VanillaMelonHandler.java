package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/11/9 14:59
 **/
public class VanillaMelonHandler implements FarmHandler {
    private static final ResourceLocation NAME = new ResourceLocation(TouhouLittleMaid.MOD_ID, "melon");

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public double getMinDistanceSq() {
        return 4.0;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        // 只管收，不管种
        return false;
    }

    @Override
    public boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        // 西瓜和南瓜
        if (block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN) {
            Block stem = block == Blocks.MELON_BLOCK ? Blocks.MELON_STEM : Blocks.PUMPKIN_STEM;
            // 如果检索的西瓜、南瓜四个方向是有瓜藤的，就认为是长出来的瓜
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (world.getBlockState(pos.offset(facing)).getBlock() == stem) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void harvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        maid.destroyBlock(pos);
    }

    @Override
    public boolean canPlant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        // 原样返回
        return seed;
    }
}
