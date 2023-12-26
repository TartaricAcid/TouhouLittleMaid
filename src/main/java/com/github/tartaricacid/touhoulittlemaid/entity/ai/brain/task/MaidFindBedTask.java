package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class MaidFindBedTask extends MaidMoveToBlockTask {
    public MaidFindBedTask(float movementSpeed) {
        super(movementSpeed);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid entityIn, BlockPos pos) {
        BlockState blockstate = worldIn.getBlockState(pos);
        return blockstate.is(InitBlocks.MAID_BED.get())
                && blockstate.getValue(BlockMaidBed.PART) == BedPart.HEAD
                && !blockstate.getValue(BedBlock.OCCUPIED);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        if (!maid.isSleeping()) {
            searchForDestination(worldIn, maid);
        }
    }
}
