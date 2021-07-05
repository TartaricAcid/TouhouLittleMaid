package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CommonSetupEvent {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(ServerCustomPackLoader::reloadPacks);
        event.enqueueWork(CommonSetupEvent::registerCapability);
    }

    private static void registerCapability() {
        CapabilityManager.INSTANCE.register(PowerCapability.class, new PowerCapability.Storage(), PowerCapability::new);
    }
}