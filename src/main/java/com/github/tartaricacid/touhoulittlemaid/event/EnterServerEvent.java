package com.github.tartaricacid.touhoulittlemaid.event;


import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class EnterServerEvent {
    @SubscribeEvent
    public static void onAttachCapabilityEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            InitTrigger.GIVE_SMART_SLAB_CONFIG.trigger((ServerPlayerEntity) event.getPlayer());
        }
    }
}
