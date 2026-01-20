package com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.edible;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFoodEdible implements IMaidEdibleBlock {
    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof FoodBiteBlock) {
            // 检查下方是否是 MAID_SNACK_STAND_BLOCK
            BlockPos belowPos = pos.below();
            BlockState belowState = maid.level.getBlockState(belowPos);
            return belowState.is(TagBlock.MAID_SNACK_STAND_BLOCK);
        }
        return false;
    }

    @Override
    public int getFavorabilityPoints(EntityMaid maid, BlockPos pos, BlockState state) {
        return 1;
    }

    @Override
    public boolean consume(EntityMaid maid, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof FoodBiteBlock biteBlock)) {
            return false;
        }
        int bites = state.getValue(biteBlock.getBites());
        // 女仆随机吃一两口
        int eatBites = maid.getRandom().nextInt(2) + 1;
        Level level = maid.level;
        if (bites < biteBlock.getMaxBites()) {
            int currentBites = Math.min(bites + eatBites, biteBlock.getMaxBites());
            level.setBlock(pos, state.setValue(biteBlock.getBites(), currentBites), Block.UPDATE_ALL);
        } else {
            maid.destroyBlock(pos);
        }
        maid.playSound(SoundEvents.GENERIC_EAT);
        return true;
    }

    @Override
    public boolean canPlaceAsFood(EntityMaid maid, ItemStack stack, int slotIndex) {
        return stack.getItem() instanceof BowlFoodBlockItem;
    }

    @Override
    public boolean shouldPlaceTo(EntityMaid maid, BlockPos pos, BlockState state, ItemStack stack) {
        // 菜不能放在脚下
        if (pos.equals(maid.blockPosition())) {
            return false;
        }
        // 目标位置能放东西
        if (!state.canBeReplaced()) {
            return false;
        }
        // 必须放在 MAID_SNACK_STAND_BLOCK 上
        BlockState belowState = maid.level.getBlockState(pos.below());
        return belowState.is(TagBlock.MAID_SNACK_STAND_BLOCK);
    }

    @Override
    public boolean placeAsFood(EntityMaid maid, BlockPos pos, ItemStack stack, int slotIndex) {
        ItemStack stackExtra = maid.getAvailableInv(true).extractItem(slotIndex, 1, false);
        if (stackExtra.isEmpty()) {
            return false;
        }
        Direction facing = maid.getDirection();
        return maid.placeItemBlock(pos, facing, stackExtra);
    }
}
