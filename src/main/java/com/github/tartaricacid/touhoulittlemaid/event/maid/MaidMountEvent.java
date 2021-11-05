package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class MaidMountEvent {
    @SubscribeEvent
    public static void onMaidMount(EntityMountEvent event) {
        if (event.isMounting() && event.getEntityMounting() instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) event.getEntityMounting();
            if (!maid.isRideable()) {
                event.setCanceled(true);
            }
        }
    }
}
