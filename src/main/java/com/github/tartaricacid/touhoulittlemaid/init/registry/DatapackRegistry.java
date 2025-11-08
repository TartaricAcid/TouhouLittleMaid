package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datapack.resources.BoardStateDataReloadListener;
import com.github.tartaricacid.touhoulittlemaid.datapack.resources.KaomojiDataReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class DatapackRegistry {
    @SubscribeEvent
    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new KaomojiDataReloadListener());
        event.addListener(new BoardStateDataReloadListener());
    }
}
