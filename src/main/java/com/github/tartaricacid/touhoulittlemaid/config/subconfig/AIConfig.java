package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import net.minecraftforge.common.ForgeConfigSpec;

public class AIConfig {
    public static ForgeConfigSpec.BooleanValue LLM_ENABLED;
    public static ForgeConfigSpec.DoubleValue LLM_TEMPERATURE;
    public static ForgeConfigSpec.ConfigValue<String> LLM_PROXY_ADDRESS;
    public static ForgeConfigSpec.IntValue LLM_MAX_TOKEN;
    public static ForgeConfigSpec.IntValue MAID_MAX_HISTORY_LLM_SIZE;

    public static ForgeConfigSpec.BooleanValue TTS_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> TTS_LANGUAGE;
    public static ForgeConfigSpec.ConfigValue<String> TTS_PROXY_ADDRESS;

    public static ForgeConfigSpec.BooleanValue STT_ENABLED;
    public static ForgeConfigSpec.EnumValue<STTApiType> STT_TYPE;
    public static ForgeConfigSpec.ConfigValue<String> STT_PROXY_ADDRESS;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("ai");

        builder.comment("Whether or not to enable the AI LLM feature");
        LLM_ENABLED = builder.define("LLMEnabled", true);

        builder.comment("LLM temperature, the higher this value, the more random the output will be");
        LLM_TEMPERATURE = builder.defineInRange("LLMTemperature", 0.5, 0, 2);

        builder.comment("LLM AI Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        LLM_PROXY_ADDRESS = builder.define("LLMProxyAddress", "");

        builder.comment("The maximum token supported by the LLM AI");
        LLM_MAX_TOKEN = builder.defineInRange("LLMMaxToken", 256, 1, Integer.MAX_VALUE);

        builder.comment("The maximum historical conversation length cached by the maid");
        MAID_MAX_HISTORY_LLM_SIZE = builder.defineInRange("MaidMaxHistoryLLMSize", 16, 1, 128);

        builder.comment("Whether or not to enable the TTS feature");
        TTS_ENABLED = builder.define("TTSEnabled", true);

        builder.comment("The TTS language you intend to use, will be overridden by the maid's settings");
        TTS_LANGUAGE = builder.define("TTSLanguage", "en_us");

        builder.comment("TTS Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        TTS_PROXY_ADDRESS = builder.define("TTSProxyAddress", "");

        builder.comment("Whether or not to enable the STT feature");
        STT_ENABLED = builder.define("STTEnabled", true);

        builder.comment("STT Type, currently support player2 app or aliyun");
        STT_TYPE = builder.defineEnum("STTType", STTApiType.PLAYER2, EnumGetMethod.NAME_IGNORECASE);

        builder.comment("STT Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        STT_PROXY_ADDRESS = builder.define("STTProxyAddress", "");

        builder.pop();
    }
}
