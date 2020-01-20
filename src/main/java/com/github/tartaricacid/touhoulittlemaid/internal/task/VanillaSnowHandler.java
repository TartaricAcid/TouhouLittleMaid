package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/11/10 13:54
 **/
public class VanillaSnowHandler implements FarmHandler {
    private static final ResourceLocation NAME = new ResourceLocation(TouhouLittleMaid.MOD_ID, "snow");

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public double getMinDistanceSq() {
        return 2.5;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        return state.getBlock() instanceof BlockSnow;
    }

    @Override
    public void harvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state) {
        ItemStack itemStack = maid.getHeldItemMainhand();
        if (itemStack.getItem() instanceof ItemSpade) {
            if (maid.destroyBlock(pos)) {
                itemStack.damageItem(1, maid);
            }
        } else {
            maid.destroyBlock(pos, false);
        }
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
