package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class EntityHurtEvent {
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        Entity attacker = event.getArrow().shootingEntity;
        RayTraceResult ray = event.getRayTraceResult();
        if (attacker instanceof EntityMaid && ray.typeOfHit == RayTraceResult.Type.ENTITY &&
                ray.entityHit != null && ray.entityHit.isEntityAlive()) {
            EntityMaid maid = (EntityMaid) attacker;
            Entity victim = ray.entityHit;
            if (maid.hasSasimono()) {
                boolean victimIsTameableHasSameOwner = victim instanceof EntityTameable && ((EntityTameable) victim).getOwnerId() != null &&
                        ((EntityTameable) victim).getOwnerId().equals(maid.getOwnerId());
                boolean victimIsPlayerOwner = victim instanceof EntityPlayer && victim.getUniqueID().equals(maid.getOwnerId());
                if (victimIsTameableHasSameOwner || victimIsPlayerOwner) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
