package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.google.common.collect.Lists;

import java.util.List;

public final class ExtraMaidBrainManager {
    static List<IExtraMaidBrain> EXTRA_MAID_BRAINS = Lists.newArrayList();

    public static void init() {
        ExtraMaidBrainManager manager = new ExtraMaidBrainManager();
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addExtraMaidBrain(manager);
        }
    }

    public void addExtraMaidBrain(IExtraMaidBrain extraMaidBrain) {
        EXTRA_MAID_BRAINS.add(extraMaidBrain);
    }
}