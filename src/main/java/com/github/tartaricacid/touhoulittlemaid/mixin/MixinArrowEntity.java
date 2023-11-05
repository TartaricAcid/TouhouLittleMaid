package com.github.tartaricacid.touhoulittlemaid.mixin;

import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AbstractArrowEntity.class)
public interface MixinArrowEntity {
    @Accessor("inGround")
    boolean tlmInGround();

    @Invoker("getPickupItem")
    ItemStack getTlmPickupItem();
}