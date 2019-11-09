package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/11/9 14:59
 **/
public class VanillaSugarCaneFarmHandler implements FarmHandler {
    @Override
    public Mode getMode() {
        return Mode.SUGAR_CANE;
    }

    @Override
    public double getMinDistanceSq() {
        return 9.0;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.REEDS;
    }

    @Override
    public boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        // 甘蔗：下方还是甘蔗，再下方能够维持甘蔗
        return block == Blocks.REEDS && canReedHarvest(world.getBlockState(pos.down()).getBlock(), world.getBlockState(pos.down(2)).getBlock());
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

        // 判断下方方块是否合适
        BlockPos downPos = pos.down();
        Block downBlock = world.getBlockState(downPos).getBlock();
        if (downBlock != Blocks.GRASS && downBlock != Blocks.DIRT && downBlock != Blocks.SAND) {
            return false;
        }

        // 判断其周围是否有水或者冰
        for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
            IBlockState state = world.getBlockState(downPos.offset(facing));
            if (state.getMaterial() == Material.WATER || state.getBlock() == Blocks.FROSTED_ICE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed) {
        IBlockState seedState = Blocks.AIR.getDefaultState();

        if (seed.getItem() == Items.REEDS) {
            seedState = Blocks.REEDS.getPlant(world, pos);
        }

        // 如果作物不为空，而且女仆成功放置了对应的作物方块
        if (seedState.getBlock() != Blocks.AIR && maid.placeBlock(pos, seedState)) {
            // 扣除物品
            seed.shrink(1);
        }

        // 返回扣除后的物品对象
        return seed;
    }

    /**
     * 收获条件：下方还是甘蔗，再下方是甘蔗生长的方块。
     * 不用原版的判定方法，因为原版判定条件多了下方是甘蔗这种情况，而收获时不需要这种判断。
     * 当然也勿需判定其周围是否有水，反正我只管收获，不管种植的
     *
     * @param blockDown  下方的方块
     * @param blockDown2 下方两格的方块
     */
    private boolean canReedHarvest(Block blockDown, Block blockDown2) {
        boolean blockDownIsOkay = blockDown == Blocks.REEDS;
        boolean blockDown2IsOkay = blockDown2 == Blocks.GRASS || blockDown2 == Blocks.DIRT || blockDown2 == Blocks.SAND;
        return blockDownIsOkay && blockDown2IsOkay;
    }
}
