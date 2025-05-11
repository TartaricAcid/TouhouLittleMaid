package com.github.tartaricacid.touhoulittlemaid.mixin.accessor;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("canAddPassenger")
    boolean tlmCanAddPassenger(Entity pPassenger);
}
