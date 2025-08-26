package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.CropBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public interface ISpecialCropHandler {
    /**
     * 判断是否是种子，在前面 Item 基础上增加额外判断
     */
    default boolean isSeed(ItemStack stack) {
        return true;
    }

    /**
     * 判断是否可以收获
     */
    default boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return false;
    }

    /**
     * 执行收获逻辑
     *
     * @param isDestroyMode 当女仆持有锄头时，此值为 true，表示直接破坏
     *                      否则是类似于右键收获
     */
    default void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, boolean isDestroyMode) {
        if (isDestroyMode) {
            maid.destroyBlock(cropPos);
        } else if (cropState.getBlock() instanceof CropBlockAccessor crop) {
            BlockEntity blockEntity = cropState.hasBlockEntity() ? maid.level.getBlockEntity(cropPos) : null;
            maid.dropResourcesToMaidInv(cropState, maid.level, cropPos, blockEntity, maid, maid.getMainHandItem());
            maid.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, cropPos, Block.getId(cropState));
            // 直接设置 Age 为 0
            if (cropState.hasProperty(crop.tlmAgeProperty())) {
                try {
                    cropState = cropState.trySetValue(crop.tlmAgeProperty(), 0);
                } catch (IllegalArgumentException ignore) {
                }
            }
            maid.level.setBlock(cropPos, cropState, Block.UPDATE_ALL);
            maid.level.gameEvent(maid, GameEvent.BLOCK_CHANGE, cropPos);
        }
    }

    /**
     * 判断是否可以种植
     */
    default boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        BlockPos abovePos = basePos.above();
        BlockState aboveState = maid.level.getBlockState(abovePos);
        if (!aboveState.canBeReplaced() || aboveState.liquid()) {
            return false;
        }
        if (seed.getItem() instanceof ItemNameBlockItem blockNamedItem) {
            BlockState plantBlockState = blockNamedItem.getBlock().defaultBlockState();
            return plantBlockState.canSurvive(maid.level, abovePos);
        }
        return false;
    }

    /**
     * 执行种植逻辑
     */
    default ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() instanceof ItemNameBlockItem) {
            maid.placeItemBlock(basePos.above(), seed);
        }
        return seed;
    }
}
