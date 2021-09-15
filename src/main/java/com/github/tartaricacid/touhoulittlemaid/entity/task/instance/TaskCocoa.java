package com.github.tartaricacid.touhoulittlemaid.entity.task.instance;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IFarmTask;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;

public class TaskCocoa implements IFarmTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "cocoa");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.COCOA_BEANS.getDefaultInstance();
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Items.COCOA_BEANS;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.is(Blocks.COCOA) && cropState.getValue(CocoaBlock.AGE) >= 2;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        maid.destroyBlock(cropPos);
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        basePos = basePos.above();
        baseState = maid.level.getBlockState(basePos);
        if (baseState.is(Blocks.JUNGLE_LOG) && seed.getItem() == Items.COCOA_BEANS) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState state = maid.level.getBlockState(basePos.relative(direction));
                if (state.getMaterial().isReplaceable()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() == Items.COCOA_BEANS) {
            basePos = basePos.above();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos directionPos = basePos.relative(direction);
                if (!seed.isEmpty() && maid.canPlaceBlock(directionPos)) {
                    World world = maid.level;
                    BlockState cocoaState = Blocks.COCOA.defaultBlockState().setValue(HorizontalBlock.FACING, direction.getOpposite());
                    world.setBlock(directionPos, cocoaState, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    SoundType soundType = cocoaState.getSoundType(world, directionPos, maid);
                    world.playSound(null, directionPos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    seed.shrink(1);
                }
            }
        }
        return seed;
    }

    @Override
    public double getCloseEnoughDist() {
        return 2.5;
    }

    @Override
    public boolean checkCropPosAbove() {
        return false;
    }

    @Override
    public List<ITextComponent> getDescription(EntityMaid maid) {
        return Collections.emptyList();
    }
}
