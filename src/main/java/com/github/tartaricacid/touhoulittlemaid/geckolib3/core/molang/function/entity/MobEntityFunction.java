package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.ContextFunction;
import net.minecraft.world.entity.Mob;

public abstract class MobEntityFunction extends ContextFunction<Mob> {
    @Override
    protected boolean validateContext(IContext<?> context) {
        return context.entity() instanceof Mob;
    }
}
