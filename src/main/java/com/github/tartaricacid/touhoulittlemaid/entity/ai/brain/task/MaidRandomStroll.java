package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class MaidRandomStroll extends MaidCheckRateTask {
    private final float speedModifier;
    protected final int maxHorizontalDistance;
    protected final int maxVerticalDistance;
    private final boolean mayStrollFromWater;

    public MaidRandomStroll(float pSpeedModifier, int pMaxHorizontalDistance, int pMaxVerticalDistance) {
        this(pSpeedModifier, pMaxHorizontalDistance, pMaxVerticalDistance, true);
    }

    public MaidRandomStroll(float pSpeedModifier, int pMaxHorizontalDistance, int pMaxVerticalDistance, boolean pMayStrollFromWater) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.speedModifier = pSpeedModifier;
        this.maxHorizontalDistance = pMaxHorizontalDistance;
        this.maxVerticalDistance = pMaxVerticalDistance;
        this.mayStrollFromWater = pMayStrollFromWater;
        this.setMaxCheckRate(50);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid)) {
            return this.mayStrollFromWater || !maid.isInWaterOrBubble();
        }
        return false;
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid maid, long pGameTime) {
        Optional<Vec3> optional = Optional.ofNullable(this.getTargetPos(maid));
        maid.getBrain().setMemory(MemoryModuleType.WALK_TARGET, optional.map(pos -> new WalkTarget(pos, this.speedModifier, 0)));
    }

    @Nullable
    protected Vec3 getTargetPos(EntityMaid maid) {
        return LandRandomPos.getPos(maid, this.maxHorizontalDistance, this.maxVerticalDistance);
    }
}
