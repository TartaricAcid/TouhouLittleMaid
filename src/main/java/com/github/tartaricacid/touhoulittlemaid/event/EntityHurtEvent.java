package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber
public final class EntityHurtEvent {
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        Entity attacker = event.getProjectile().getOwner();
        HitResult ray = event.getRayTraceResult();
        if (attacker instanceof TamableAnimal thrower && ray instanceof EntityHitResult) {
            Entity victim = ((EntityHitResult) ray).getEntity();
            if (victim instanceof TamableAnimal tameable) {
                if (tameable.getOwnerUUID() != null && tameable.getOwnerUUID().equals(thrower.getOwnerUUID())) {
                    event.setCanceled(true);
                }
            }
            if (victim instanceof LivingEntity) {
                if (thrower.isOwnedBy((LivingEntity) victim)) {
                    event.setCanceled(true);
                }
            }
            ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType());
            if (MaidConfig.MAID_RANGED_ATTACK_IGNORE.get().contains(registryName.toString())) {
                event.setCanceled(true);
            }
        }
    }
}
