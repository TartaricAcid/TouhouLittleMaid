package com.github.tartaricacid.touhoulittlemaid.compat.farmersdelight;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;

import static com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock.belowIsSnackStand;

public class FarmersDelightEdible implements IMaidEdibleBlock {
    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos pos, BlockState state) {
        // 农夫乐事有两种方块类食物，一种继承 PieBlock，一种继承 FeastBlock
        Block block = state.getBlock();
        if (block instanceof PieBlock || block instanceof FeastBlock) {
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
        Block block = state.getBlock();
        Level level = maid.level;
        ItemStack stack = block.getCloneItemStack(level, pos, state);

        if (block instanceof PieBlock pieBlock) {
            // 糕点
            int bites = state.getValue(PieBlock.BITES);
            int maxBites = pieBlock.getMaxBites() - 1;

            if (bites < maxBites) {
                int currentBites = Math.min(bites + 1, maxBites);
                level.setBlock(pos, state.setValue(PieBlock.BITES, currentBites), Block.UPDATE_ALL);
            } else {
                maid.destroyBlock(pos);
            }
        } else if (block instanceof FeastBlock feastBlock) {
            // 盛宴
            IntegerProperty property = feastBlock.getServingsProperty();
            int servings = state.getValue(property);

            if (0 < servings) {
                level.setBlock(pos, state.setValue(property, servings - 1), Block.UPDATE_ALL);
            } else {
                maid.destroyBlock(pos);
            }
        }

        maid.spawnItemParticles(stack, 8);
        maid.playSound(SoundEvents.GENERIC_EAT);
        return true;
    }

    @Override
    public boolean canPlaceAsFood(EntityMaid maid, ItemStack stack, int slotIndex) {
        Item item = stack.getItem();
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return block instanceof PieBlock || block instanceof FeastBlock;
        }
        return false;
    }
}
