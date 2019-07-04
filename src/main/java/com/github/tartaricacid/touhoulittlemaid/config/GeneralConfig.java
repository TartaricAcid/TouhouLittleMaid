package com.github.tartaricacid.touhoulittlemaid.config;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = TouhouLittleMaid.MOD_ID, name = "TouhouLittleMaid")
public class GeneralConfig {
    @Config.LangKey("config.touhou_little_maid.maid_config")
    @Config.Name("MaidConfig")
    public static MaidConfig MAID_CONFIG = new MaidConfig();

    public static class MaidConfig {
        @Config.Comment("The Item That Can Tamed Maid")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_tamed_item")
        @Config.Name("MaidTamedItem")
        public String maidTamedItem = "minecraft:cake";
    }

    /**
     * 用于 GUI 界面配置调节的保存
     */
    @Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(TouhouLittleMaid.MOD_ID)) {
                ConfigManager.sync(TouhouLittleMaid.MOD_ID, Config.Type.INSTANCE);
                TouhouLittleMaid.LOGGER.info("Touhou Little Mod's Config Already Change.");
            }
        }
    }
}
