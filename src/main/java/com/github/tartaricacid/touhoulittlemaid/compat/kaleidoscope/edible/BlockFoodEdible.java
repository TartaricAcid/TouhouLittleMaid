package com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.edible;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock.belowIsSnackStand;

public class BlockFoodEdible implements IMaidEdibleBlock {
    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof FoodBiteBlock) {
            // 检查下方是否是 MAID_SNACK_STAND_BLOCK
            return belowIsSnackStand(maid, pos);
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
        Level level = maid.level;
        if (bites < biteBlock.getMaxBites()) {
            int currentBites = Math.min(bites + 1, biteBlock.getMaxBites());
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
}
