package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.datagen.TagItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

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
        // 自己新建一个 tag 用了存储可种的种子
        return stack.is(TagItem.MAID_PLANTABLE_SEEDS) && stack.getItem() instanceof ItemNameBlockItem;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (block instanceof CropBlock cropBlock && cropBlock.isMaxAge(cropState)) {
            return true;
        }
        return block == Blocks.NETHER_WART && cropState.getValue(NetherWartBlock.AGE) >= 3;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (maid.getMainHandItem().getItem() instanceof HoeItem) {
            maid.destroyBlock(cropPos);
        } else {
            CombinedInvWrapper availableInv = maid.getAvailableInv(false);
            Block cropBlock = cropState.getBlock();
            maid.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, cropPos, Block.getId(cropState));

            if (cropBlock instanceof CropBlock crop) {
                BlockEntity blockEntity = cropState.hasBlockEntity() ? maid.level.getBlockEntity(cropPos) : null;
                maid.dropResourcesToMaidInv(cropState, maid.level, cropPos, blockEntity, maid, maid.getMainHandItem());
                maid.level.setBlock(cropPos, crop.defaultBlockState(), Block.UPDATE_ALL);
                maid.level.gameEvent(maid, GameEvent.BLOCK_CHANGE, cropPos);
                return;
            }

            if (cropBlock == Blocks.NETHER_WART) {
                ItemStack dropItemStack = new ItemStack(Items.NETHER_WART);
                ItemStack remindItemStack = ItemHandlerHelper.insertItemStacked(availableInv, dropItemStack, false);
                if (!remindItemStack.isEmpty()) {
                    Block.popResource(maid.level, cropPos, remindItemStack);
                }
                maid.level.setBlock(cropPos, Blocks.NETHER_WART.defaultBlockState(), Block.UPDATE_ALL);
                maid.level.gameEvent(maid, GameEvent.BLOCK_CHANGE, cropPos);
            }
        }
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
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
        maid.placeItemBlock(basePos.above(), seed);
        return seed;
    }
}
