package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.mux;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMultiSelectTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;

import java.util.ArrayList;
import java.util.List;

public class TaskSwitchBehavior extends Behavior<EntityMaid> {

    public TaskSwitchBehavior() {
        super(ImmutableMap.of());
    }

    @Override
    protected void tick(ServerLevel level, EntityMaid owner, long gameTime) {
        if (gameTime % 10 != 3) return;
        List<IMultiSelectTask> groups = new ArrayList<>();
        var current = owner.getTask();
        boolean idle = current instanceof IMultiSelectTask task && task.isIdling(owner);
        int max = 0;
        for (var e : TaskManager.getTaskIndex()) {
            if (current == e) continue;
            if (!(e instanceof IMultiSelectTask sel)) continue;
            int priority = e.getTaskPriority();
            if (priority <= current.getTaskPriority() && !idle) continue;
            if (!e.isEnable(owner)) continue;
            if (!sel.mayActivate(owner)) continue;
            groups.add(sel);
            max = Math.max(max, priority);
        }
        int maxFinal = max;
        groups.removeIf(e -> e.getTaskPriority() < maxFinal);
        if (groups.isEmpty()) return;
        var sel = groups.get(owner.getRandom().nextInt(groups.size()));
        for (var e : owner.getBrain().getRunningBehaviors()) {
            if (e instanceof BehaviorWrapper wrapper) {
                wrapper.doStop(level, owner, gameTime);
            }
        }
        if (current instanceof IMultiSelectTask prev)
            prev.onStop(owner);
        sel.activate(owner);
        owner.setTask(sel);
    }

}
