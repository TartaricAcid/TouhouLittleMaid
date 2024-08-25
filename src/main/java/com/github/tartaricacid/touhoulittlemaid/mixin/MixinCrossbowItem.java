package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class MixinCrossbowItem {
    @Inject(method = "shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"))
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, LivingEntity target, CallbackInfo ci) {
        if (shooter instanceof EntityMaid maid && projectile instanceof AbstractArrow arrow) {
            // 弩箭伤害也和好感度挂钩
            // 但是烟花火箭的伤害是很特殊的，就不应用了
            AttributeInstance attackDamage = maid.getAttribute(Attributes.ATTACK_DAMAGE);
            double attackValue = 2.0;
            if (attackDamage != null) {
                attackValue = attackDamage.getBaseValue();
            }
            float multiplier = (float) (attackValue / 2.0f);
            arrow.setBaseDamage(arrow.getBaseDamage() * multiplier);
        }
    }
}
