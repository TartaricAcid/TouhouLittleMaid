package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import net.minecraft.core.BlockPos;

public interface ICachedEvaluator {
    void init(int x, int y, int z, int cx, int cy, int cz);

    void markVis(BlockPos pos);
}
