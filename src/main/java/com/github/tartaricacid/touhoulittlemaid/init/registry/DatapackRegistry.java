package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datapack.resources.BoardStateDataReloadListener;
import com.github.tartaricacid.touhoulittlemaid.datapack.resources.KaomojiDataReloadListener;
import com.github.tartaricacid.touhoulittlemaid.datapack.resources.SkillsDataReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class DatapackRegistry {
    @SubscribeEvent
    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new KaomojiDataReloadListener());
        event.addListener(new BoardStateDataReloadListener());
        event.addListener(new SkillsDataReloadListener());
    }
}
