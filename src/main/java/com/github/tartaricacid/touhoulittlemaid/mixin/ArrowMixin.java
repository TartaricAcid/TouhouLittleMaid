package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractArrow.class)
public abstract class ArrowMixin extends Projectile {
    protected ArrowMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        // 女仆射出的箭会在 3 秒后恢复重力，不能用 life，因为它在飞行时会被设置为 0
        if (this.tickCount > 60 && this.isNoGravity() && this.getOwner() instanceof EntityMaid) {
            this.setNoGravity(false);
        }
    }
}
