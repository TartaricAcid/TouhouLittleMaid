package com.github.tartaricacid.touhoulittlemaid.debug;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.UUID;

@EventBusSubscriber
public class ChangeMaidOwner {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        if (player.getMainHandItem().is(Items.DEBUG_STICK)) {
            maid.setOwnerUUID(UUID.randomUUID());
            maid.level.broadcastEntityEvent(maid, EntityEvent.TAMING_SUCCEEDED);
            event.setCanceled(true);
        }
    }
}
