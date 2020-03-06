package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class EntityMaidCompassSetting extends EntityAIBase {
    private final double movementSpeed;
    private EntityMaid entityMaid;
    private int timeCount;
    private ItemKappaCompass.Mode mode;

    public EntityMaidCompassSetting(EntityMaid entityMaid, double speedIn) {
        this.entityMaid = entityMaid;
        this.movementSpeed = speedIn;
        this.mode = entityMaid.getCompassMode();
        this.timeCount = 200;
        this.setMutexBits(1 | 4);
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.guiOpening && --timeCount < 0 && entityMaid.isHomeModeEnable() && !entityMaid.isSitting()
                && entityMaid.getCompassMode() != ItemKappaCompass.Mode.NONE;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }

    @Override
    public void startExecuting() {
        this.timeCount = 200;
        // 如果女仆正在寻路，等待 1s 后再尝试
        if (entityMaid.hasPath()) {
            this.timeCount = 20;
            return;
        }
        this.mode = entityMaid.getCompassMode();
        List<BlockPos> posList = entityMaid.getCompassPosList(this.mode);
        if (posList.size() <= 0) {
            return;
        }
        switch (mode) {
            case SINGLE_POINT: {
                singlePointAi(posList);
                break;
            }
            case MULTI_POINT_REENTRY: {
                multiPointReentryAi(posList);
                break;
            }
            case MULTI_POINT_CLOSURE: {
                multiPointClosureAi(posList);
                break;
            }
            case SET_RANGE:
                setRangeAi();
                break;
            default:
        }
    }

    private void setRangeAi() {
        if (!entityMaid.isWithinHomeDistanceCurrentPosition()) {
            BlockPos home = entityMaid.getHomePosition();
            if (!home.equals(BlockPos.ORIGIN)) {
                entityMaid.getNavigator().tryMoveToXYZ(home.getX(), home.getY(), home.getZ(), movementSpeed);
            }
        }
    }

    private void multiPointClosureAi(List<BlockPos> posList) {
        int currentIndex = entityMaid.getCurrentIndex();
        BlockPos pos = posList.get(currentIndex);

        if (entityMaid.getPosition().distanceSq(pos) <= 1.2) {
            int nextIndex = (currentIndex + 1) % posList.size();
            BlockPos nextPos = posList.get(nextIndex);
            if (entityMaid.getNavigator().tryMoveToXYZ(nextPos.getX(), nextPos.getY(), nextPos.getZ(), movementSpeed)) {
                entityMaid.setCurrentIndex(nextIndex);
                this.timeCount = 20;
            } else {
                this.timeCount = 200;
            }
        } else {
            if (entityMaid.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), movementSpeed)) {
                this.timeCount = 20;
            } else {
                this.timeCount = 200;
            }
        }
    }

    private void multiPointReentryAi(List<BlockPos> posList) {
        int currentIndex = entityMaid.getCurrentIndex();
        boolean descending = entityMaid.isDescending();
        BlockPos pos = posList.get(currentIndex);
        if (entityMaid.getPosition().distanceSq(pos) <= 1.2) {
            if (currentIndex == 0) {
                descending = false;
            }
            if (currentIndex == posList.size() - 1) {
                descending = true;
            }
            int next = descending ? -1 : 1;
            int nextIndex = (currentIndex + next) % posList.size();
            BlockPos nextPos = posList.get(nextIndex);
            if (entityMaid.getNavigator().tryMoveToXYZ(nextPos.getX(), nextPos.getY(), nextPos.getZ(), movementSpeed)) {
                entityMaid.setCurrentIndex(nextIndex);
                entityMaid.setDescending(descending);
                this.timeCount = 20;
            } else {
                this.timeCount = 200;
            }
        } else {
            if (entityMaid.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), movementSpeed)) {
                this.timeCount = 20;
            } else {
                this.timeCount = 200;
            }
        }
    }

    private void singlePointAi(List<BlockPos> posList) {
        BlockPos pos = posList.get(0);
        double distance = entityMaid.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
        if (8 < distance && distance < 16) {
            entityMaid.getNavigator().clearPath();
            entityMaid.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), movementSpeed);
        } else if (distance >= 16) {
            entityMaid.attemptTeleport(pos.getX(), pos.getY(), pos.getZ());
        }
        this.timeCount = 100;
    }
}
