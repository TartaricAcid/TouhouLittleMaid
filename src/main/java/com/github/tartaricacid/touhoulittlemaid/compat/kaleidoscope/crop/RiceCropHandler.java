package com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.crop;

import com.github.tartaricacid.touhoulittlemaid.api.task.ISpecialCropHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.crop.SpecialCropManager;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class RiceCropHandler implements ISpecialCropHandler {
    public static void addCropHandlers(SpecialCropManager manager) {
        RiceCropHandler handler = new RiceCropHandler();
        manager.addSeed(ModItems.RICE_SEED.get(), handler);
        manager.addSeed(ModItems.WILD_RICE_SEED.get(), handler);
        manager.addCrop(ModBlocks.RICE_CROP.get(), handler);
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        // 由于种植特殊，暂时无法种植，只能玩家种植
        return false;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        int location = cropState.getValue(RiceCropBlock.LOCATION);
        return location == RiceCropBlock.DOWN && cropState.getValue(RiceCropBlock.AGE) >= RiceCropBlock.MAX_AGE;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, boolean isDestroyMode) {
        // 无视 isDestroyMode，直接收获
        maid.dropResourcesToMaidInv(cropState, maid.level, cropPos, null, maid, maid.getMainHandItem());
        maid.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, cropPos, Block.getId(cropState));
        // 直接设置 Age 为 0
        cropState = cropState.setValue(RiceCropBlock.AGE, 0);
        maid.level.setBlock(cropPos, cropState, Block.UPDATE_ALL);
        maid.level.gameEvent(maid, GameEvent.BLOCK_CHANGE, cropPos);
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        // 由于种植特殊，暂时无法种植，只能玩家种植后，女仆右键收获
        return false;
    }
}
