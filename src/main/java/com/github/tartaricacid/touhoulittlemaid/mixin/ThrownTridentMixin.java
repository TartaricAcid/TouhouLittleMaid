package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ThrownTrident.class)
public class ThrownTridentMixin {
    @Inject(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At("HEAD"))
    private void onHitEntity(EntityHitResult result, CallbackInfo ci) {
        ThrownTrident trident = (ThrownTrident) (Object) this;
        // 击中实体后，恢复重力
        if (trident.isNoGravity() && trident.getOwner() instanceof EntityMaid) {
            trident.setNoGravity(false);
        }
    }
}
