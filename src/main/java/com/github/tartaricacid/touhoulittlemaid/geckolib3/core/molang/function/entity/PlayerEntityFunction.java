package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.ContextFunction;
import net.minecraft.client.player.AbstractClientPlayer;

public abstract class PlayerEntityFunction extends ContextFunction<AbstractClientPlayer> {
    @Override
    protected boolean validateContext(IContext<?> context) {
        return context.entity() instanceof AbstractClientPlayer;
    }
}
