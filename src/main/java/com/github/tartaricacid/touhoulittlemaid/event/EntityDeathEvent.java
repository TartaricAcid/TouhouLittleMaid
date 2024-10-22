package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class EntityDeathEvent {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity causingEntity = event.getSource().getEntity();
        if (causingEntity instanceof EntityMaid maid) {
            maid.getKillRecordManager().onTargetDeath(maid, event.getEntity());
        }
    }
}
