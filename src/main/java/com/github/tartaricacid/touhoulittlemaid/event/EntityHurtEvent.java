package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        if (attacker instanceof EntityMaid && ray instanceof EntityRayTraceResult) {
            EntityMaid maid = (EntityMaid) attacker;
            Entity victim = ((EntityRayTraceResult) ray).getEntity();
            if (victim instanceof TameableEntity) {
                TameableEntity tameable = (TameableEntity) victim;
                if (tameable.getOwnerUUID() != null && tameable.getOwnerUUID().equals(maid.getOwnerUUID())) {
                    event.setCanceled(true);
                }
            }
            if (victim instanceof PlayerEntity) {
                if (maid.isOwnedBy((PlayerEntity) victim)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
