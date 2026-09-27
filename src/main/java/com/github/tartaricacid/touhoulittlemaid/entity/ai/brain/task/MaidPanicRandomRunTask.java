package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.RandomStroll;

public class MaidPanicRandomRunTask extends MaidRunOne {
    public MaidPanicRandomRunTask(float speedModifier) {
        super(ImmutableList.of(
                Pair.of(RandomStroll.stroll(speedModifier, 5, 3), 1)
        ));
    }

    @Override
    public boolean tryStart(ServerLevel level, EntityMaid maid, long gameTime) {
        if (!MaidPanicTask.isHurt(maid) || MaidPanicTask.hasHostile(maid)) {
            return false;
        }

        MaidRunAwayTask.leaveMountOrSitForDanger(maid);
        return super.tryStart(level, maid, gameTime);
    }
}