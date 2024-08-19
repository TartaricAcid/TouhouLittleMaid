package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;

@EventBusSubscriber
public final class MaidMountEvent {
    @SubscribeEvent
    public static void onMaidMount(EntityMountEvent event) {
        if (event.isMounting() && event.getEntityMounting() instanceof EntityMaid && !(event.getEntityBeingMounted() instanceof EntitySit)) {
            EntityMaid maid = (EntityMaid) event.getEntityMounting();
            if (!maid.isRideable()) {
                event.setCanceled(true);
            }
        }
    }
}
