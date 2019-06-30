package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityMaidReturnHome extends EntityAIBase {
    private final double movementSpeed;
    private EntityMaid entityMaid;
    private int attemptsCount;
    private int timeCount;

    public EntityMaidReturnHome(EntityMaid entityMaid, double speedIn, int attemptsCount) {
        this.entityMaid = entityMaid;
        this.movementSpeed = speedIn;
        this.attemptsCount = attemptsCount;
        this.timeCount = attemptsCount;
    }

    @Override
    public boolean shouldExecute() {
        // 尝试 attemptsCount 次后进行执行
        timeCount--;

        return timeCount < 0 && entityMaid.isHome() && !entityMaid.isSitting() && !entityMaid.getHomePos().equals(BlockPos.ORIGIN);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return timeCount < 0 && entityMaid.isHome() && !entityMaid.isSitting() && !entityMaid.getHomePos().equals(BlockPos.ORIGIN);
    }

    @Override
    public void startExecuting() {
        BlockPos pos = entityMaid.getHomePos();
        double distance = entityMaid.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
        if (8 < distance && distance < 16) {
            entityMaid.getNavigator().clearPath();
            entityMaid.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), movementSpeed);
        } else if (distance >= 16) {
            entityMaid.attemptTeleport(pos.getX(), pos.getY(), pos.getZ());
        }
        timeCount = attemptsCount;
    }
}
