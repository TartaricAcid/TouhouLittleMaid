package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class MaidFindBedTask extends MaidMoveToBlockTask {
    public MaidFindBedTask(float movementSpeed, int searchLength) {
        super(movementSpeed, searchLength);
    }

    @Override
    protected boolean shouldMoveTo(ServerWorld worldIn, EntityMaid entityIn, BlockPos pos) {
        BlockState blockstate = worldIn.getBlockState(pos);
        return blockstate.getBlock().is(InitBlocks.MAID_BED.get())
                && blockstate.getValue(BlockMaidBed.PART) == BedPart.HEAD
                && !blockstate.getValue(BedBlock.OCCUPIED);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        if (!maid.isSleeping()) {
            searchForDestination(worldIn, maid);
        }
    }
}
