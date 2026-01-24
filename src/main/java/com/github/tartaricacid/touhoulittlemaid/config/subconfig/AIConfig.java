package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.StringUtils;

public class AIConfig {
    public static ForgeConfigSpec.BooleanValue LLM_ENABLED;
    public static ForgeConfigSpec.DoubleValue LLM_TEMPERATURE;
    public static ForgeConfigSpec.BooleanValue FUNCTION_CALL_ENABLED;
    public static ForgeConfigSpec.BooleanValue AUTO_GEN_SETTING_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> LLM_PROXY_ADDRESS;
    public static ForgeConfigSpec.IntValue LLM_MAX_TOKEN;
    public static ForgeConfigSpec.IntValue MAID_MAX_HISTORY_LLM_SIZE;
    public static ForgeConfigSpec.IntValue MAX_TOKENS_PER_PLAYER;
    public static ForgeConfigSpec.IntValue MAX_AI_FUNCTION_CALL;

    public static ForgeConfigSpec.BooleanValue TTS_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> TTS_LANGUAGE;
    public static ForgeConfigSpec.ConfigValue<String> TTS_PROXY_ADDRESS;

    public static ForgeConfigSpec.BooleanValue STT_ENABLED;
    public static ForgeConfigSpec.EnumValue<STTApiType> STT_TYPE;
    public static ForgeConfigSpec.ConfigValue<String> STT_MICROPHONE;
    public static ForgeConfigSpec.IntValue MAID_CAN_CHAT_DISTANCE;
    public static ForgeConfigSpec.ConfigValue<String> STT_PROXY_ADDRESS;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("ai");

        builder.comment("Whether or not to enable the AI LLM feature");
        LLM_ENABLED = builder.define("LLMEnabled", true);

        builder.comment("LLM temperature, the higher this value, the more random the output will be");
        LLM_TEMPERATURE = builder.defineInRange("LLMTemperature", 0.7, 0, 2);

        builder.comment("Whether to enable the function call function?");
        builder.comment("The maid will be able to interact with the game after it is enabled, but it will increase the amount of token used");
        FUNCTION_CALL_ENABLED = builder.define("FunctionCallEnable", false);

        builder.comment("Whether to automatically generate the maid's settings");
        AUTO_GEN_SETTING_ENABLED = builder.define("AutoGenSettingEnabled", true);

        builder.comment("LLM AI Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        LLM_PROXY_ADDRESS = builder.define("LLMProxyAddress", "");

        builder.comment("The maximum token supported by the LLM AI");
        LLM_MAX_TOKEN = builder.defineInRange("LLMMaxToken", 4096, 1, Integer.MAX_VALUE);

        builder.comment("The maximum historical conversation length cached by the maid");
        MAID_MAX_HISTORY_LLM_SIZE = builder.defineInRange("MaidMaxHistoryLLMSize", 16, 1, 128);

        builder.comment("The maximum tokens that a player can use");
        MAX_TOKENS_PER_PLAYER = builder.defineInRange("MaxTokensPerPlayer", Integer.MAX_VALUE, 1, Integer.MAX_VALUE);

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

        builder.comment("The name of the microphone device, empty is default");
        STT_MICROPHONE = builder.define("STTMicrophone", StringUtils.EMPTY);

        builder.comment("The range of search when chatting with the maid");
        MAID_CAN_CHAT_DISTANCE = builder.defineInRange("MaidCanChatDistance", 12, 1, 256);

        builder.comment("STT Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        STT_PROXY_ADDRESS = builder.define("STTProxyAddress", "");

        builder.comment("Maximum number of function call for llm in single chat");
        MAX_AI_FUNCTION_CALL = builder.defineInRange("MaidMaxAIFunctionCall", 3, 0, 32);

        builder.pop();
    }
}
