package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.mux;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;

import java.util.ArrayList;
import java.util.List;

public class TaskBehaviorGroup {

    private final EntityMaid maid;
    private final IMaidTask task;
    private final List<BehaviorWrapper> behaviors = new ArrayList<>();

    public TaskBehaviorGroup(EntityMaid maid, IMaidTask task, List<Pair<Integer, BehaviorControl<? super EntityMaid>>> behaviors) {
        this.maid = maid;
        this.task = task;
        var copy = new ArrayList<>(behaviors);
        behaviors.clear();
        for (var e : copy) {
            var wrapped = new BehaviorWrapper(e.getSecond(), this);
            behaviors.add(Pair.of(e.getFirst(), wrapped));
            this.behaviors.add(wrapped);
        }
    }

    public String getDebugInfo() {
        return task.getUid().getPath();
    }

}
