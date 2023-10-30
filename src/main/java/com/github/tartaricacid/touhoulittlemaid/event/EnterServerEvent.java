package com.github.tartaricacid.touhoulittlemaid.event;


import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class EnterServerEvent {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            InitTrigger.GIVE_SMART_SLAB_CONFIG.trigger((ServerPlayer) event.getPlayer());
            InitTrigger.GIVE_PATCHOULI_BOOK_CONFIG.trigger((ServerPlayer) event.getEntity());
        }
    }
}
