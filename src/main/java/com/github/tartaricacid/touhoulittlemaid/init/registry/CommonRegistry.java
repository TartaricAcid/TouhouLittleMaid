package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class CommonRegistry {
    @SubscribeEvent
    public static void onSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(ServerCustomPackLoader::reloadPacks);
        event.enqueueWork(NetworkHandler::init);
        event.enqueueWork(BackpackManager::initItemIndex);
    }
}