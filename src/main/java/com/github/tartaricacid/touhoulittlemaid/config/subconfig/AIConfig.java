package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.StringUtils;

public class AIConfig {
    private static final int TOKEN_LIMIT_K_UNIT = 1024;

    public static ForgeConfigSpec.BooleanValue LLM_ENABLED;
    public static ForgeConfigSpec.BooleanValue AUTO_GEN_SETTING_ENABLED;
    public static ForgeConfigSpec.ConfigValue<String> LLM_PROXY_ADDRESS;
    public static ForgeConfigSpec.IntValue MAID_HISTORY_COMPRESS_TOKEN_LIMIT;
    public static ForgeConfigSpec.IntValue MAX_TOKENS_PER_PLAYER;

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

        builder.comment("Whether to automatically generate the maid's settings");
        AUTO_GEN_SETTING_ENABLED = builder.define("AutoGenSettingEnabled", true);

        builder.comment("LLM AI Proxy Address, such as 127.0.0.1:1080, empty is no proxy, SOCKS proxies are not supported");
        LLM_PROXY_ADDRESS = builder.define("LLMProxyAddress", "");

        builder.comment("Compress the maid's LLM chat history before the next player message when the previous chat request reaches this token count, in K tokens (1K = 1024 tokens)");
        MAID_HISTORY_COMPRESS_TOKEN_LIMIT = builder.defineInRange("MaidHistoryCompressTokenLimit", 48, 8, 1024);

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

        builder.pop();
    }

    public static int getMaidHistoryCompressTokenLimit() {
        return MAID_HISTORY_COMPRESS_TOKEN_LIMIT.get() * TOKEN_LIMIT_K_UNIT;
    }
}
