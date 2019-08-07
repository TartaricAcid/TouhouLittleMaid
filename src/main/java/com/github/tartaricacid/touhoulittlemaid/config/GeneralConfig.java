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
        @Config.Comment("The item that can tamed maid")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_tamed_item")
        @Config.Name("MaidTamedItem")
        public String maidTamedItem = "minecraft:cake";

        @Config.Comment("The item that can temptation maid")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_temptation_item")
        @Config.Name("MaidTemptationItem")
        public String maidTemptationItem = "minecraft:cake";

        @Config.Comment("Get number of ticks, at least during which the maid will be silent.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_talk_interval")
        @Config.Name("MaidTalkInterval")
        @Config.RangeInt(min = 20)
        public int maidTalkInterval = 120;

        @Config.Comment("Get number of count, at least during which the maid will be silent in pickup item.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_pickup_sound_interval")
        @Config.Name("MaidPickupSoundInterval")
        @Config.RangeInt(min = 1)
        public int maidPickupSoundInterval = 5;

        @Config.Comment("Get number of ticks, at least during which the maid will be silent in hurt.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_hurt_sound_interval")
        @Config.Name("MaidHurtSoundInterval")
        @Config.RangeInt(min = 20)
        public int maidHurtSoundInterval = 120;

        @Config.Comment("Should the maid always show the hat? when set to false, the maid will only render the hat when wearing the helmet.")
        @Config.LangKey("config.touhou_little_maid.maid_config.maid_always_show_hat")
        @Config.Name("MaidAlwaysShowHat")
        public boolean maidAlwaysShowHat = true;
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
