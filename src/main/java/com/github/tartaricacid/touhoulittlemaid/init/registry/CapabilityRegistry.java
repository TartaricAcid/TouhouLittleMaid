package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class CapabilityRegistry {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MaidNumCapability.class);
        event.register(PowerCapability.class);
    }
}
