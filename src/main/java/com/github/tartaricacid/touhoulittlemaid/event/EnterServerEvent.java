package com.github.tartaricacid.touhoulittlemaid.event;


import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class EnterServerEvent {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            InitTrigger.GIVE_SMART_SLAB_CONFIG.trigger((ServerPlayer) event.getEntity());
            InitTrigger.GIVE_PATCHOULI_BOOK_CONFIG.trigger((ServerPlayer) event.getEntity());
        }
    }
}
