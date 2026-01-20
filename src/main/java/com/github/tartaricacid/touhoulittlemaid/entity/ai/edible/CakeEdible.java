package com.github.tartaricacid.touhoulittlemaid.entity.ai.edible;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CakeEdible implements IMaidEdibleBlock {
    private static final int CAKE_PLACE_DISTANCE = 3;

    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos pos, BlockState state) {
        return state.is(Blocks.CAKE);
    }

    @Override
    public int getFavorabilityPoints(EntityMaid maid, BlockPos pos, BlockState state) {
        return 1;
    }

    @Override
    public boolean consume(EntityMaid maid, BlockPos pos, BlockState state) {
        if (state.is(Blocks.CAKE)) {
            int bites = state.getValue(CakeBlock.BITES);
            // 女仆随机吃一两口
            int eatBites = maid.getRandom().nextInt(2) + 1;
            Level level = maid.level;
            if (bites < CakeBlock.MAX_BITES) {
                int currentBites = Math.min(bites + eatBites, CakeBlock.MAX_BITES);
                level.setBlock(pos, state.setValue(CakeBlock.BITES, currentBites), Block.UPDATE_ALL);
            } else {
                level.removeBlock(pos, false);
            }
            maid.playSound(SoundEvents.GENERIC_EAT);
            return true;
        }
        return false;
    }

    @Override
    public boolean canPlaceAsFood(EntityMaid maid, ItemStack stack, int slotIndex) {
        return stack.is(Items.CAKE);
    }

    @Override
    public boolean shouldPlaceTo(EntityMaid maid, BlockPos pos, BlockState state, ItemStack stack) {
        // 蛋糕随便放，只要下方能放，而且就在女仆身边，但不能放在脚下
        if (pos.equals(maid.blockPosition())) {
            return false;
        }
        double disSqr = maid.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
        if (disSqr > CAKE_PLACE_DISTANCE * CAKE_PLACE_DISTANCE) {
            return false;
        }
        if (!state.canBeReplaced()) {
            return false;
        }
        BlockPos belowPos = pos.below();
        BlockState belowState = maid.level.getBlockState(belowPos);
        return belowState.isSolid();
    }

    @Override
    public boolean placeAsFood(EntityMaid maid, BlockPos pos, ItemStack stack, int slotIndex) {
        ItemStack stackExtra = maid.getAvailableBackpackInv().extractItem(slotIndex, 1, false);
        if (stackExtra.isEmpty()) {
            return false;
        }
        Block cakeBlock = Blocks.CAKE;
        maid.level.setBlock(pos, cakeBlock.defaultBlockState(), Block.UPDATE_ALL);
        maid.playSound(SoundEvents.WOOL_PLACE);
        return true;
    }
}
