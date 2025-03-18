package com.github.tartaricacid.touhoulittlemaid.entity.ai.control;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 参考：net.minecraft.world.entity.monster.Drowned.DrownedMoveControl
 */
public class MaidMoveControl extends MoveControl {
    private final EntityMaid maid;

    public MaidMoveControl(EntityMaid maid) {
        super(maid);
        this.maid = maid;
    }

    @Override
    public void tick() {
        //女仆现在想游泳了
        if (this.maid.getSwimManager().wantToSwim()) {
            //TODO:this.operation != MoveControl.Operation.MOVE_TO || 不明用途已删除。暂时未造成影响
            if (this.maid.isUnderWater() && this.maid.getNavigation().isDone()) {
                this.maid.setSpeed(0.0F);
                return;
            }
            double x = this.wantedX - this.maid.getX();
            double y = this.wantedY - this.maid.getY();
            double z = this.wantedZ - this.maid.getZ();
            double sqrt = Math.sqrt(x * x + y * y + z * z);
            float yRot = (float) (Math.toDegrees(Mth.atan2(z, x)) - 90);

            this.maid.setYRot(this.rotlerp(this.maid.getYRot(), yRot, 90.0F));
            this.maid.yBodyRot = this.maid.getYRot();
            float speed = (float) (this.speedModifier * this.maid.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float speedLerp = Mth.lerp(1, this.maid.getSpeed(), speed);
            if (maid.getSwimManager().getSwimTarget() != null) {
                maid.getLookControl().setLookAt(maid.getSwimManager().getSwimTarget().getCenter());
            }
            // 太慢了，3 倍基础速度
            this.maid.setSpeed(speedLerp * 3);
            this.maid.setDeltaMovement(this.maid.getDeltaMovement().add(speedLerp * x * 0.005, speedLerp * y / sqrt * 0.25, speedLerp * z * 0.005));
        } else {
            //部分Climbable会被识别成实体方块并尝试跳一跳，这会导致女仆无法通过这些位置。将其jump去除
            if (this.operation == MoveControl.Operation.MOVE_TO) {

                this.operation = MoveControl.Operation.WAIT;
                double d0 = this.wantedX - this.mob.getX();
                double d1 = this.wantedZ - this.mob.getZ();
                double d2 = this.wantedY - this.mob.getY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                    return;
                }

                float f9 = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                BlockPos blockpos = this.mob.blockPosition();
                BlockState blockstate = this.mob.level().getBlockState(blockpos);
                VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level(), blockpos);
                if (d2 > (double) this.mob.getStepHeight() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.mob.getBbWidth())
                        ||
                        !voxelshape.isEmpty()
                                && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY()
                                && !blockstate.is(BlockTags.DOORS)
                                && !blockstate.is(BlockTags.FENCES)
                                && !blockstate.is(BlockTags.CLIMBABLE)
                ) {
                    this.mob.getJumpControl().jump();
                    this.operation = MoveControl.Operation.JUMPING;
                }
            } else if (this.operation == MoveControl.Operation.JUMPING && this.mob.isInWater())
                this.operation = MoveControl.Operation.WAIT;
            else
                super.tick();
        }
    }
}