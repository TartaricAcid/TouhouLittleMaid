package com.github.tartaricacid.touhoulittlemaid.entity.ai.control;

import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
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
        // 如何女仆想游泳了
        if (this.maid.getSwimManager().wantToSwim()) {
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
            // 部分 Climbable 会被识别成实体方块并尝试跳一跳，这会导致女仆无法通过这些位置。将其 jump 去除
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;

                double x = this.wantedX - this.mob.getX();
                double y = this.wantedY - this.mob.getY();
                double z = this.wantedZ - this.mob.getZ();
                double sqrt = x * x + y * y + z * z;
                if (sqrt < 2.5e-7) {
                    this.mob.setZza(0);
                    return;
                }

                float angle = Mth.RAD_TO_DEG * (float) Mth.atan2(z, x) - 90;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), angle, 90));
                this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

                BlockPos blockPos = this.mob.blockPosition();
                BlockState blockState = this.mob.level.getBlockState(blockPos);
                VoxelShape voxelShape = blockState.getCollisionShape(this.mob.level(), blockPos);

                if (this.mob.getStepHeight() < y && x * x + z * z < Math.max(1, this.mob.getBbWidth())
                    || !voxelShape.isEmpty()
                       && this.mob.getY() < (voxelShape.max(Direction.Axis.Y) + blockPos.getY())
                       && !blockState.is(TagBlock.MAID_JUMP_FORBIDDEN_BLOCK)
                ) {
                    this.mob.getJumpControl().jump();
                    this.operation = MoveControl.Operation.JUMPING;
                }
            } else if (this.operation == MoveControl.Operation.JUMPING && this.mob.isInWater()) {
                this.operation = Operation.WAIT;
            } else {
                super.tick();
            }
        }
    }
}