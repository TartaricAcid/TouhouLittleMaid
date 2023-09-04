package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public final class EntityHurtEvent {
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        Entity attacker = event.getProjectile().getOwner();
        HitResult ray = event.getRayTraceResult();
        if (attacker instanceof TamableAnimal && ray instanceof EntityHitResult) {
            TamableAnimal thrower = (TamableAnimal) attacker;
            Entity victim = ((EntityHitResult) ray).getEntity();
            if (victim instanceof TamableAnimal) {
                TamableAnimal tameable = (TamableAnimal) victim;
                if (tameable.getOwnerUUID() != null && tameable.getOwnerUUID().equals(thrower.getOwnerUUID())) {
                    event.setCanceled(true);
                }
            }
            if (victim instanceof LivingEntity) {
                if (thrower.isOwnedBy((LivingEntity) victim)) {
                    event.setCanceled(true);
                }
            }
            ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(victim.getType());
            if (registryName != null && MaidConfig.MAID_RANGED_ATTACK_IGNORE.get().contains(registryName.toString())) {
                event.setCanceled(true);
            }
        }
    }
}
