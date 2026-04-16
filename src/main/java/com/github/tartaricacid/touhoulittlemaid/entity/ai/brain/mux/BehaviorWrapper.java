package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.mux;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;

public class BehaviorWrapper implements BehaviorControl<EntityMaid> {

    private final BehaviorControl<? super EntityMaid> inner;
    private final TaskBehaviorGroup group;

    public BehaviorWrapper(BehaviorControl<? super EntityMaid> inner, TaskBehaviorGroup group) {
        this.inner = inner;
        this.group = group;
    }

    @Override
    public Behavior.Status getStatus() {
        return inner.getStatus();
    }

    @Override
    public boolean tryStart(ServerLevel level, EntityMaid entity, long gameTime) {
        return inner.tryStart(level, entity, gameTime);
    }

    @Override
    public void tickOrStop(ServerLevel level, EntityMaid entity, long gameTime) {
        inner.tickOrStop(level, entity, gameTime);
    }

    @Override
    public void doStop(ServerLevel level, EntityMaid entity, long gameTime) {
        inner.doStop(level, entity, gameTime);
    }

    @Override
    public String debugString() {
        return group.getDebugInfo() + " - " + inner.getClass().getSimpleName();
    }

}
