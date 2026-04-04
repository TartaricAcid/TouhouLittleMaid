package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.ISpecialCropHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.crop.SpecialCropManager;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.CropBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

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
        // 先判断特殊作物
        ISpecialCropHandler handler = SpecialCropManager.getItemSeedHandlers().get(item);
        if (handler != null) {
            return handler.isSeed(stack);
        }
        // 然后是默认情况
        if (item instanceof ItemNameBlockItem blockNamedItem) {
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable plant) {
                return plant.getPlantType(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) == PlantType.CROP
                        && plant.getPlant(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).getBlock() != Blocks.AIR;
            }
        }
        return false;
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
        BlockState aboveState = maid.level.getBlockState(basePos.above());
        if (!aboveState.canBeReplaced() || aboveState.liquid()) {
            return false;
        }
        if (seed.getItem() instanceof ItemNameBlockItem blockNamedItem) {
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable plant) {
                return baseState.canSustainPlant(maid.level, basePos, Direction.UP, plant);
            }
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
        if (item instanceof ItemNameBlockItem blockNamedItem) {
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                maid.placeItemBlock(basePos.above(), seed);
            }
        }
        return seed;
    }

    @Override
    public String getMaidActionSummary() {
        return "Plant and harvest crops on nearby farmland.";
    }
}
