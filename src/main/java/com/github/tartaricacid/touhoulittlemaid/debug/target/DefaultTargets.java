package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultTargets {
    public static List<Function<EntityMaid, List<DebugTarget>>> getDefaultTargets() {
        return List.of(
                DefaultTargets::getDefaultTargets
        );
    }

    public static List<DebugTarget> getDefaultTargets(EntityMaid maid) {
        if (maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get())) {
            return List.of(
                    new DebugTarget(maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).get().currentBlockPosition(), 0xFFFF0000, "Target Pos")
            );
        }
        return List.of();
    }
}
