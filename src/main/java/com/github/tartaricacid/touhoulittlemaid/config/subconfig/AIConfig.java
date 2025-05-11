package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import net.minecraftforge.common.ForgeConfigSpec;

public class AIConfig {
    public static ForgeConfigSpec.BooleanValue CHAT_ENABLED;
    public static ForgeConfigSpec.DoubleValue CHAT_TEMPERATURE;
    public static ForgeConfigSpec.ConfigValue<String> CHAT_PROXY_ADDRESS;
    public static ForgeConfigSpec.BooleanValue TTS_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> TTS_LANGUAGE;
    public static ForgeConfigSpec.ConfigValue<String> TTS_PROXY_ADDRESS;
    public static ForgeConfigSpec.EnumValue<STTApiType> STT_TYPE;
    public static ForgeConfigSpec.ConfigValue<String> STT_URL;
    public static ForgeConfigSpec.ConfigValue<String> STT_TOKEN;
    public static ForgeConfigSpec.ConfigValue<String> STT_PROXY_ADDRESS;
    public static ForgeConfigSpec.IntValue MAID_MAX_HISTORY_CHAT_SIZE;

    public static void init(ForgeConfigSpec.Builder builder) {
        // 读取网站列表
        AvailableSites.readSites();

        builder.push("ai");

        builder.comment("Whether or not to enable the AI Chat feature");
        CHAT_ENABLED = builder.define("ChatEnabled", true);

        builder.comment("Chat temperature, the higher this value, the more random the output will be");
        CHAT_TEMPERATURE = builder.defineInRange("ChatTemperature", 0.5, 0, 2);

        builder.comment("Chat AI Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        CHAT_PROXY_ADDRESS = builder.define("ChatProxyAddress", "");

        builder.comment("Whether or not to enable the TTS feature");
        TTS_ENABLED = builder.define("TTSEnabled", true);

        builder.comment("The TTS language you intend to use");
        TTS_LANGUAGE = builder.define("TTSLanguage", "en_us");

        builder.comment("TTS Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        TTS_PROXY_ADDRESS = builder.define("TTSProxyAddress", "");

        builder.comment("STT Type, currently support player2 app or aliyun");
        STT_TYPE = builder.defineEnum("STTType", STTApiType.PLAYER2, EnumGetMethod.NAME_IGNORECASE);

        builder.comment("STT Url address, currently support player2 app or aliyun");
        STT_URL = builder.define("STTUrl", "http://127.0.0.1:4315/v1/stt");

        builder.comment("STT Token, use for aliyun");
        STT_TOKEN = builder.define("STTToken", "");

        builder.comment("STT Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        STT_PROXY_ADDRESS = builder.define("STTProxyAddress", "");

        builder.comment("The maximum historical conversation length cached by the maid");
        MAID_MAX_HISTORY_CHAT_SIZE = builder.defineInRange("MaidMaxHistoryChatSize", 16, 1, 128);

        builder.pop();
    }
}
