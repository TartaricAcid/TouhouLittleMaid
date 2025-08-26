package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.ISpecialCropHandler;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.crop.SpecialCropManager;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.CropBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TaskNormalFarm implements IFarmTask {
    private static final ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "farm");

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return Items.IRON_HOE.getDefaultInstance();
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        // 先判断特殊作物
        ISpecialCropHandler handler = SpecialCropManager.getItemSeedHandlers().get(item);
        if (handler != null) {
            return handler.isSeed(stack);
        }
        // 然后是默认情况
        // 自己新建一个 tag 用了存储可种的种子
        return stack.is(TagItem.MAID_PLANTABLE_SEEDS) && stack.getItem() instanceof ItemNameBlockItem;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        // 先判断特殊情况
        ISpecialCropHandler handler = SpecialCropManager.getBlockCropHandlers().get(block);
        if (handler != null) {
            return handler.canHarvest(maid, cropPos, cropState);
        }
        // 其他情况
        return block instanceof CropBlock crop && crop.isMaxAge(cropState);
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        boolean isDestroyMode = maid.getMainHandItem().getItem() instanceof HoeItem;
        Block cropBlock = cropState.getBlock();

        // 先判断特殊情况
        ISpecialCropHandler handler = SpecialCropManager.getBlockCropHandlers().get(cropBlock);
        if (handler != null) {
            handler.harvest(maid, cropPos, cropState, isDestroyMode);
            return;
        }

        // 其他情况
        if (isDestroyMode) {
            maid.destroyBlock(cropPos);
        } else if (cropBlock instanceof CropBlockAccessor crop) {
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

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        // 先判断特殊情况
        ISpecialCropHandler handler = SpecialCropManager.getBlockCropHandlers().get(baseState.getBlock());
        if (handler != null) {
            return handler.canPlant(maid, basePos, baseState, seed);
        }

        // 其他情况
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

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        // 先判断特殊作物
        Item item = seed.getItem();
        ISpecialCropHandler handler = SpecialCropManager.getItemSeedHandlers().get(item);
        if (handler != null) {
            return handler.plant(maid, basePos, baseState, seed);
        }
        // 然后是默认情况
        if (item instanceof ItemNameBlockItem) {
            maid.placeItemBlock(basePos.above(), seed);
        }
        return seed;
    }
}
