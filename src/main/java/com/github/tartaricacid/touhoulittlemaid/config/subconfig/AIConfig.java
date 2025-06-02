package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.StringUtils;

public class AIConfig {
    private static final String TRANSLATE_KEY = "config.touhou_little_maid.global_ai";

    public static ModConfigSpec.BooleanValue LLM_ENABLED;
    public static ModConfigSpec.DoubleValue LLM_TEMPERATURE;
    public static ModConfigSpec.BooleanValue FUNCTION_CALL_ENABLED;
    public static ModConfigSpec.BooleanValue AUTO_GEN_SETTING_ENABLED;
    public static ModConfigSpec.ConfigValue<String> LLM_PROXY_ADDRESS;
    public static ModConfigSpec.IntValue LLM_MAX_TOKEN;
    public static ModConfigSpec.IntValue MAID_MAX_HISTORY_LLM_SIZE;
    public static ModConfigSpec.IntValue MAX_TOKENS_PER_PLAYER;

    public static ModConfigSpec.BooleanValue TTS_ENABLED;
    public static ModConfigSpec.ConfigValue<String> TTS_LANGUAGE;
    public static ModConfigSpec.ConfigValue<String> TTS_PROXY_ADDRESS;

    public static ModConfigSpec.BooleanValue STT_ENABLED;
    public static ModConfigSpec.EnumValue<STTApiType> STT_TYPE;
    public static ModConfigSpec.ConfigValue<String> STT_MICROPHONE;
    public static ModConfigSpec.IntValue MAID_CAN_CHAT_DISTANCE;
    public static ModConfigSpec.ConfigValue<String> STT_PROXY_ADDRESS;

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("ai");

        builder.comment("Whether or not to enable the AI LLM feature").translation(translateKey("llm_enabled"));
        LLM_ENABLED = builder.define("LLMEnabled", true);

        builder.comment("LLM temperature, the higher this value, the more random the output will be").translation(translateKey("llm_temperature"));
        LLM_TEMPERATURE = builder.defineInRange("LLMTemperature", 0.7, 0, 2);

        builder.comment("Whether to enable the function call function?").translation(translateKey("function_call_enabled.1"));
        builder.comment("The maid will be able to interact with the game after it is enabled, but it will increase the amount of token used").translation(translateKey("function_call_enabled.2"));
        FUNCTION_CALL_ENABLED = builder.define("FunctionCallEnable", false);

        builder.comment("Whether to automatically generate the maid's settings").translation(translateKey("auto_gen_setting_enabled"));
        AUTO_GEN_SETTING_ENABLED = builder.define("AutoGenSettingEnabled", true);

        builder.comment("LLM AI Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported").translation(translateKey("llm_proxy_address"));
        LLM_PROXY_ADDRESS = builder.define("LLMProxyAddress", "");

        builder.comment("The maximum token supported by the LLM AI").translation(translateKey("llm_max_token"));
        LLM_MAX_TOKEN = builder.defineInRange("LLMMaxToken", 4096, 1, Integer.MAX_VALUE);

        builder.comment("The maximum historical conversation length cached by the maid").translation(translateKey("maid_max_history_llm_size"));
        MAID_MAX_HISTORY_LLM_SIZE = builder.defineInRange("MaidMaxHistoryLLMSize", 16, 1, 128);

        builder.comment("The maximum tokens that a player can use").translation(translateKey("max_tokens_per_player"));
        MAX_TOKENS_PER_PLAYER = builder.defineInRange("MaxTokensPerPlayer", Integer.MAX_VALUE, 1, Integer.MAX_VALUE);

        builder.comment("Whether or not to enable the TTS feature").translation(translateKey("tts_enabled"));
        TTS_ENABLED = builder.define("TTSEnabled", true);

        builder.comment("The TTS language you intend to use, will be overridden by the maid's settings").translation(translateKey("tts_language"));
        TTS_LANGUAGE = builder.define("TTSLanguage", "en_us");

        builder.comment("TTS Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported").translation(translateKey("tts_proxy_address"));
        TTS_PROXY_ADDRESS = builder.define("TTSProxyAddress", "");

        builder.comment("Whether or not to enable the STT feature").translation(translateKey("stt_enabled"));
        STT_ENABLED = builder.define("STTEnabled", true);

        builder.comment("STT Type, currently support player2 app or aliyun").translation(translateKey("stt_type"));
        STT_TYPE = builder.defineEnum("STTType", STTApiType.PLAYER2, EnumGetMethod.NAME_IGNORECASE);

        builder.comment("The name of the microphone device, empty is default").translation(translateKey("stt_microphone"));
        STT_MICROPHONE = builder.define("STTMicrophone", StringUtils.EMPTY);

        builder.comment("The range of search when chatting with the maid").translation(translateKey("maid_can_chat_distance"));
        MAID_CAN_CHAT_DISTANCE = builder.defineInRange("MaidCanChatDistance", 12, 1, 256);

        builder.comment("STT Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported").translation(translateKey("stt_proxy_address"));
        STT_PROXY_ADDRESS = builder.define("STTProxyAddress", "");

        builder.pop();
    }

    private static String translateKey(String key) {
        return TRANSLATE_KEY + "." + key;
    }
}
