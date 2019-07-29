package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class EntityMaidFollowOwner extends EntityAIFollowOwner {
    private EntityMaid entityMaid;

    public EntityMaidFollowOwner(EntityMaid entityMaid, double followSpeedIn, float minDistIn, float maxDistIn) {
        super(entityMaid, followSpeedIn, minDistIn, maxDistIn);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.isHome() && super.shouldExecute();
    }

    /**
     * 原版 EntityAIFollowOwner 调用了缓存的 Navigator，导致更新后 Navigator 无法应用到此 AI，所以重写了一遍
     */
    @Override
    public void updateTask() {
        this.entityMaid.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.entityMaid.getVerticalFaceSpeed());

        if (!this.entityMaid.isSitting()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.entityMaid.getNavigator().tryMoveToEntityLiving(this.owner, this.followSpeed)) {
                    if (!this.entityMaid.getLeashed() && !this.entityMaid.isRiding()) {
                        if (this.entityMaid.getDistanceSq(this.owner) >= 144.0D) {
                            int i = MathHelper.floor(this.owner.posX) - 2;
                            int j = MathHelper.floor(this.owner.posZ) - 2;
                            int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
                                        this.entityMaid.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.entityMaid.rotationYaw, this.entityMaid.rotationPitch);
                                        this.entityMaid.getNavigator().clearPath();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.isHome() && !this.entityMaid.getNavigator().noPath() && this.entityMaid.getDistanceSq(this.owner) > (double) (this.maxDist * this.maxDist) && !this.entityMaid.isSitting();
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.entityMaid.getNavigator().clearPath();
        this.entityMaid.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
}
