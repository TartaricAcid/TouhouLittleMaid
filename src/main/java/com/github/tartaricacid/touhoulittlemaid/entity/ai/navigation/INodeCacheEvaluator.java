package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import net.minecraft.world.level.pathfinder.Node;

public interface INodeCacheEvaluator {
    Node createNode(int x, int y, int z);
}
