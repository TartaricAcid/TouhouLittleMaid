package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.UpdateMaidSleepYawMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed.PART;

public class EntityMaidFindBed extends EntityAIMoveToBlock {
    private final EntityMaid maid;

    public EntityMaidFindBed(EntityMaid maid, double speedIn) {
        super(maid, speedIn, 8);
        this.maid = maid;
    }

    @Override
    public boolean shouldExecute() {
        return this.maid.isTamed() && !this.maid.isSitting() && !this.maid.isSleep() &&
                (!maid.world.isDaytime() || maid.world.isThundering()) && super.shouldExecute();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        boolean canSleep = !maid.world.isDaytime() || maid.world.isThundering();
        if (this.getIsAboveDestination() && maid.world.getEntitiesWithinAABB(EntityMaid.class,
                new AxisAlignedBB(destinationBlock), EntityMaid::isSleep).isEmpty() && canSleep) {
            // 取消骑乘状态
            if (maid.getRidingEntity() != null) {
                maid.dismountRidingEntity();
            }
            // 取消被骑乘状态
            if (maid.getControllingPassenger() != null) {
                maid.getControllingPassenger().dismountRidingEntity();
            }
            this.maid.setSleep(true);
            IBlockState state = maid.world.getBlockState(destinationBlock);
            if (state.getBlock() == MaidBlocks.MAID_BED && state.getValue(BlockMaidBed.PART) == BlockMaidBed.EnumPartType.FOOT) {
                CommonProxy.INSTANCE.sendToAllAround(new UpdateMaidSleepYawMessage(maid.getEntityId(), state.getValue(BlockHorizontal.FACING).getHorizontalAngle()),
                        new NetworkRegistry.TargetPoint(maid.dimension, maid.posX, maid.posY, maid.posZ, 128));
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.maid.isTamed() && !this.maid.isSitting() && !this.maid.isSleep() &&
                (!maid.world.isDaytime() || maid.world.isThundering()) && super.shouldContinueExecuting();
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        if (worldIn.isAirBlock(pos.up())) {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if (block == MaidBlocks.MAID_BED && iblockstate.getValue(PART) != BlockMaidBed.EnumPartType.HEAD) {
                List<EntityMaid> maidList = worldIn.getEntitiesWithinAABB(EntityMaid.class, new AxisAlignedBB(pos), EntityMaid::isSleep);
                return maidList.isEmpty();
            }
        }
        return false;
    }
}
