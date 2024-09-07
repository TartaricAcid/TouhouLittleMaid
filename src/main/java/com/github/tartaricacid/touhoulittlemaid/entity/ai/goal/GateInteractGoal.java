package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import com.github.tartaricacid.touhoulittlemaid.util.FenceGateBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;


import static net.minecraft.world.level.block.FenceGateBlock.OPEN;

public abstract class GateInteractGoal extends Goal {

    protected Mob mob;
    protected BlockPos gatePos = BlockPos.ZERO;
    protected boolean hasGate;
    private boolean passed;
    private float gateOpenDirX;
    private float gateOpenDirZ;

    public GateInteractGoal(Mob pMob) {
        this.mob = pMob;
        if (!GoalUtils.hasGroundPathNavigation(pMob)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    protected boolean isOpen() {
        if (!this.hasGate) {
            return false;
        } else {
            BlockState blockstate = this.mob.level.getBlockState(this.gatePos);
            if (!(blockstate.getBlock() instanceof FenceGateBlock)) {
                this.hasGate = false;
                return false;
            } else {
                return blockstate.getValue(OPEN);
            }
        }
    }

    protected void setOpen(boolean pOpen) {
        if (this.hasGate) {
            BlockState blockstate = this.mob.level.getBlockState(this.gatePos);
            if (blockstate.getBlock() instanceof FenceGateBlock) {
                FenceGateBlockUtil.setOpen(this.mob, this.mob.level, blockstate, this.gatePos, pOpen);
            }
        }
    }

    @Override
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        } else if (!this.mob.horizontalCollision) {
            return false;
        } else {
            GroundPathNavigation groundpathnavigation = (GroundPathNavigation) this.mob.getNavigation();
            Path path = groundpathnavigation.getPath();
            if (path != null && !path.isDone() && groundpathnavigation.canOpenDoors()) {
                for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                    Node node = path.getNode(i);
                    this.gatePos = new BlockPos(node.x, node.y, node.z);
                    if (!(this.mob.distanceToSqr((double) this.gatePos.getX(), this.mob.getY(), (double) this.gatePos.getZ()) > 2.25D)) {
                        this.hasGate = isFenceGate(this.mob.level(), this.gatePos);
                        if (this.hasGate) {
                            return true;
                        }
                    }
                }

                this.gatePos = this.mob.blockPosition().above();
                this.hasGate = isFenceGate(this.mob.level(), this.gatePos);
                return this.hasGate;
            } else {
                return false;
            }
        }
    }

    public static boolean isFenceGate(Level pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos).getBlock() instanceof FenceGateBlock;
    }

    public boolean canContinueToUse() {
        return !this.passed;
    }

    public void start() {
        this.passed = false;
        this.gateOpenDirX = (float) ((double) this.gatePos.getX() + 0.5D - this.mob.getX());
        this.gateOpenDirZ = (float) ((double) this.gatePos.getZ() + 0.5D - this.mob.getZ());
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        float f = (float) ((double) this.gatePos.getX() + 0.5D - this.mob.getX());
        float f1 = (float) ((double) this.gatePos.getZ() + 0.5D - this.mob.getZ());
        float f2 = this.gateOpenDirX * f + this.gateOpenDirZ * f1;
        if (f2 < 0.0F) {
            this.passed = true;
        }
    }
}
