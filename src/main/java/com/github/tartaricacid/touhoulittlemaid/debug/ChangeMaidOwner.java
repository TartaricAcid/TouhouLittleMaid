package com.github.tartaricacid.touhoulittlemaid.debug;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
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
