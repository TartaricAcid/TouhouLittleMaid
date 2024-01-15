package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;

public class MaidFarmMoveTask extends MaidMoveToBlockTask {
    private final NonNullList<ItemStack> seeds = NonNullList.create();
    private final IFarmTask task;

    public MaidFarmMoveTask(IFarmTask task, float movementSpeed) {
        super(movementSpeed);
        this.task = task;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        seeds.clear();
        IItemHandler inv = entityIn.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (task.isSeed(stack)) {
                seeds.add(stack);
            }
        }
        this.searchForDestination(worldIn, entityIn);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos basePos) {
        if (task.checkCropPosAbove()) {
            BlockPos above2Pos = basePos.above(2);
            BlockState stateUp2 = worldIn.getBlockState(above2Pos);
            if (!stateUp2.getCollisionShape(worldIn, above2Pos).isEmpty()) {
                return false;
            }
        }

        BlockPos cropPos = basePos.above();
        BlockState cropState = worldIn.getBlockState(cropPos);
        if (task.canHarvest(maid, cropPos, cropState)) {
            return true;
        }

        BlockState baseState = worldIn.getBlockState(basePos);
        return seeds.stream().anyMatch(seed -> task.canPlant(maid, basePos, baseState, seed));
    }
}
