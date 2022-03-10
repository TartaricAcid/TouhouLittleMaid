package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class EntityHurtEvent {
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        Entity attacker = event.getArrow().getOwner();
        RayTraceResult ray = event.getRayTraceResult();
        if (attacker instanceof TameableEntity && ray instanceof EntityRayTraceResult) {
            TameableEntity thrower = (TameableEntity) attacker;
            Entity victim = ((EntityRayTraceResult) ray).getEntity();
            if (victim instanceof TameableEntity) {
                TameableEntity tameable = (TameableEntity) victim;
                if (tameable.getOwnerUUID() != null && tameable.getOwnerUUID().equals(thrower.getOwnerUUID())) {
                    event.setCanceled(true);
                }
            }
            if (victim instanceof LivingEntity) {
                if (thrower.isOwnedBy((LivingEntity) victim)) {
                    event.setCanceled(true);
                }
            }
            ResourceLocation registryName = victim.getType().getRegistryName();
            if (registryName != null && MaidConfig.MAID_RANGED_ATTACK_IGNORE.get().contains(registryName.toString())) {
                event.setCanceled(true);
            }
        }
    }
}
