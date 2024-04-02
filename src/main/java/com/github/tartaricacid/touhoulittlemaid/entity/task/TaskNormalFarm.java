package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

public class TaskNormalFarm implements IFarmTask {
    private static final ResourceLocation NAME = new ResourceLocation(TouhouLittleMaid.MOD_ID, "farm");

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
        if (item == Items.NETHER_WART) {
            return true;
        }
        if (item instanceof ItemNameBlockItem) {
            ItemNameBlockItem blockNamedItem = (ItemNameBlockItem) item;
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                IPlantable plantable = (IPlantable) block;
                return plantable.getPlantType(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) == PlantType.CROP
                        && plantable.getPlant(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).getBlock() != Blocks.AIR;
            }
        }
        return false;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (block instanceof CropBlock && ((CropBlock) block).isMaxAge(cropState)) {
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

            if (cropBlock instanceof CropBlock) {
                CropBlock crop = (CropBlock) cropBlock;
                BlockEntity blockEntity = cropState.hasBlockEntity() ? maid.level.getBlockEntity(cropPos) : null;
                dropResourcesToMaidInv(cropState, maid.level, cropPos, blockEntity, maid, maid.getMainHandItem(), availableInv);
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
        BlockState aboveState = maid.level.getBlockState(basePos.above());
        if (!aboveState.canBeReplaced() || aboveState.liquid()) {
            return false;
        }
        if (seed.getItem() instanceof ItemNameBlockItem) {
            ItemNameBlockItem blockNamedItem = (ItemNameBlockItem) seed.getItem();
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                IPlantable plantable = (IPlantable) block;
                return baseState.canSustainPlant(maid.level, basePos, Direction.UP, plantable);
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() instanceof ItemNameBlockItem) {
            ItemNameBlockItem blockNamedItem = (ItemNameBlockItem) seed.getItem();
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                maid.placeItemBlock(basePos.above(), seed);
            }
        }
        return seed;
    }

    private void dropResourcesToMaidInv(BlockState state, Level level, BlockPos pos, @Nullable BlockEntity blockEntity, EntityMaid maid, ItemStack tool, CombinedInvWrapper invWrapper) {
        if (level instanceof ServerLevel serverLevel) {
            Block.getDrops(state, serverLevel, pos, blockEntity, maid, tool).forEach(stack -> {
                ItemStack remindItemStack = ItemHandlerHelper.insertItemStacked(invWrapper, stack, false);
                if (!remindItemStack.isEmpty()) {
                    Block.popResource(level, pos, remindItemStack);
                }
            });
            state.spawnAfterBreak(serverLevel, pos, tool, true);
        }
    }
}
