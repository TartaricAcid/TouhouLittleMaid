package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.ContextFunction;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.MolangUtils;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectLevel extends ContextFunction<Entity> {
    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }

    @Override
    protected Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        ResourceLocation effectId = MolangUtils.parseResourceLocation(context.entity(), arguments.getAsString(context, 0));
        if (effectId == null) {
            return null;
        }

        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);
        if (effect == null) {
            return 0;
        }

        if (context.entity().entity() instanceof LivingEntity) {
            MobEffectInstance instance = ((LivingEntity) context.entity().entity()).getEffect(effect);
            if (instance != null) {
                return instance.getAmplifier() + 1;
            }
        } else {
            return null;
        }

        return 0;
    }
}
