package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.query;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity.EntityFunction;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class Position extends EntityFunction {
    @Override
    protected Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        int axis = arguments.getAsInt(context, 0);
        float partialTicks = context.entity().animationEvent().getPartialTick();
        Entity entity = context.entity().entity();
        switch (axis) {
            case 0: return Mth.lerp(partialTicks, entity.xo, entity.getX());
            case 1: return Mth.lerp(partialTicks, entity.yo, entity.getY());
            case 2: return Mth.lerp(partialTicks, entity.zo, entity.getZ());
            default: return null;
        }
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}
