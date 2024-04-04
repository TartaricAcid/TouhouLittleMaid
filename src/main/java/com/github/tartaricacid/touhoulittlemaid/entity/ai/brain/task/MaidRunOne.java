package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.RunOne;

import java.util.List;

public class MaidRunOne extends RunOne<EntityMaid> {
    public MaidRunOne(List<Pair<? extends BehaviorControl<? super EntityMaid>, Integer>> pEntryCondition) {
        super(pEntryCondition);
    }

    @Override
    public boolean tryStart(ServerLevel pLevel, EntityMaid maid, long pGameTime) {
        return !maid.isBegging() && !maid.isSleeping() && !(maid.getVehicle() instanceof EntitySit) && super.tryStart(pLevel, maid, pGameTime);
    }
}
