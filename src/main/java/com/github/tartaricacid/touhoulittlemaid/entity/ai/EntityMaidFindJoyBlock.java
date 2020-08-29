package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidJoy;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidJoy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.JoyType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidJoy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityMaidFindJoyBlock extends EntityAIMoveToBlock {
    private final EntityMaid maid;

    public EntityMaidFindJoyBlock(EntityMaid maid, double speedIn) {
        super(maid, speedIn, 8);
        this.maid = maid;
    }

    @Override
    public boolean shouldExecute() {
        return this.maid.isTamed() && !this.maid.isSitting() && !this.maid.isSleep() && super.shouldExecute();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.getIsAboveDestination()) {
            IBlockState state = maid.world.getBlockState(destinationBlock);
            if (state.getBlock() != MaidBlocks.MAID_JOY || !state.getValue(BlockMaidJoy.CORE)) {
                return;
            }

            TileEntity te = maid.world.getTileEntity(destinationBlock);
            if (!(te instanceof TileEntityMaidJoy) || !((TileEntityMaidJoy) te).isCoreBlock()) {
                return;
            }

            List<EntityMaidJoy> joyList = maid.world.getEntitiesWithinAABB(EntityMaidJoy.class, new AxisAlignedBB(destinationBlock));
            if (!joyList.isEmpty()) {
                return;
            }

            // 取消骑乘状态
            if (maid.getRidingEntity() != null) {
                maid.dismountRidingEntity();
            }
            // 取消被骑乘状态
            if (maid.getControllingPassenger() != null) {
                maid.getControllingPassenger().dismountRidingEntity();
            }

            String type = ((TileEntityMaidJoy) te).getType();
            EntityMaidJoy entityMaidJoy = new EntityMaidJoy(maid.world, type,
                    destinationBlock.getX() + 0.5,
                    destinationBlock.getY() + 0.5625,
                    destinationBlock.getZ() + 0.5);
            float yaw = state.getValue(BlockHorizontal.FACING).getHorizontalAngle();
            entityMaidJoy.rotationYaw = yaw;
            entityMaidJoy.prevRotationYaw = yaw;
            maid.world.spawnEntity(entityMaidJoy);
            maid.startRiding(entityMaidJoy);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.maid.isTamed() && !this.maid.isSitting() && !this.maid.isSleep() && super.shouldContinueExecuting();
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        if (worldIn.isAirBlock(pos.up())) {
            IBlockState state = worldIn.getBlockState(pos);
            Block block = state.getBlock();
            if (block == MaidBlocks.MAID_JOY && state.getValue(BlockMaidJoy.CORE)) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te instanceof TileEntityMaidJoy && ((TileEntityMaidJoy) te).isCoreBlock()) {
                    List<EntityMaidJoy> joyList = maid.world.getEntitiesWithinAABB(EntityMaidJoy.class, new AxisAlignedBB(pos));
                    if (joyList.isEmpty()) {
                        return JoyType.canUseJoyType(maid, ((TileEntityMaidJoy) te).getType());
                    }
                }
            }
        }
        return false;
    }
}
