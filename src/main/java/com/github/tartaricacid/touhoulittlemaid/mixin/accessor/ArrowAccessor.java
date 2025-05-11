package com.github.tartaricacid.touhoulittlemaid.mixin.accessor;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AbstractArrow.class)
public interface ArrowAccessor {
    @Accessor("inGround")
    boolean tlmInGround();

    @Invoker("getPickupItem")
    ItemStack getTlmPickupItem();
}
