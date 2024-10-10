package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class MaidMountEvent {
    @SubscribeEvent
    public static void onMaidMount(EntityMountEvent event) {
        Entity entityMounting = event.getEntityMounting();
        Entity entityBeingMounted = event.getEntityBeingMounted();
        if (event.isMounting() && entityMounting instanceof EntityMaid maid && !(entityBeingMounted instanceof EntitySit)) {
            if (!maid.isRideable()) {
                event.setCanceled(true);
                return;
            }

            // 当船上有坐垫，坐垫是空的时，那么优先坐到坐垫上！所以这块为空
            if (entityBeingMounted instanceof Boat boat && boat.getControllingPassenger() instanceof EntityChair chair && chair.getPassengers().isEmpty()) {
                event.setCanceled(true);
            }
        }
    }
}
