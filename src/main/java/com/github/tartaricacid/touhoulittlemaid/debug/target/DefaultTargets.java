package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.util.VisibleForDebug;

import java.util.List;
import java.util.function.Function;

@VisibleForDebug
public class DefaultTargets {
    private static final int COLOR = 0x9FFFFF00;
    private static final String TEXT = "Target Pos";
    private static final int LIFE_TIME = 2 * 1000;

    public static List<Function<EntityMaid, List<DebugTarget>>> getDefaultTargets() {
        return List.of(DefaultTargets::getDefaultTargets);
    }

    public static List<DebugTarget> getDefaultTargets(EntityMaid maid) {
        return maid.getBrain()
                .getMemory(InitEntities.TARGET_POS.get())
                .map(tracker -> List.of(new DebugTarget(tracker.currentBlockPosition(), COLOR, TEXT, LIFE_TIME)))
                .orElseGet(List::of);
    }
}
