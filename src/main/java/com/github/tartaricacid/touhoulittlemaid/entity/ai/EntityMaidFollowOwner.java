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
        if (entityMaid.isSitting()) {
            return;
        }
        if (--timeToRecalcPath > 0) {
            return;
        }
        timeToRecalcPath = 10;
        if (entityMaid.getNavigator().tryMoveToEntityLiving(this.owner, this.followSpeed)) {
            return;
        }
        if (entityMaid.getLeashed() || entityMaid.isRiding()) {
            return;
        }
        if (this.entityMaid.getDistanceSq(this.owner) >= 144.0D) {
            int x = MathHelper.floor(this.owner.posX) - 2;
            int y = MathHelper.floor(this.owner.posZ) - 2;
            int z = MathHelper.floor(this.owner.getEntityBoundingBox().minY);
            tryToTeleport(x, y, z);
        }
    }

    private void tryToTeleport(int x, int y, int z) {
        for (int xOffset = 0; xOffset <= 4; ++xOffset) {
            for (int zOffset = 0; zOffset <= 4; ++zOffset) {
                boolean offsetIsOkay = xOffset < 1 || zOffset < 1 || xOffset > 3 || zOffset > 3;
                if (offsetIsOkay && this.isTeleportFriendlyBlock(x, y, z, xOffset, zOffset)) {
                    this.entityMaid.setLocationAndAngles(((float) (x + xOffset) + 0.5F), z, ((float) (y + zOffset) + 0.5F),
                            this.entityMaid.rotationYaw, this.entityMaid.rotationPitch);
                    this.entityMaid.getNavigator().clearPath();
                    return;
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.isHome() && !this.entityMaid.getNavigator().noPath()
                && this.entityMaid.getDistanceSq(this.owner) > (double) (this.maxDist * this.maxDist)
                && !this.entityMaid.isSitting();
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.entityMaid.getNavigator().clearPath();
        this.entityMaid.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
}
