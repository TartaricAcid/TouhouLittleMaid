package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.OpenAISite;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class DefaultLLMSite {
    public static OpenAISite PLAYER2 = createSite("player2",
            "http://127.0.0.1:4315/v1/chat/completions", true,
            Map.of("player2-game-key", "TouhouLittleMaid"),
            "default");

    public static OpenAISite ALIYUN = createSite("aliyun",
            "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
            "qwen-plus", "qwen-max", "qwen-turbo", "qwen-long");

    public static OpenAISite DEEPSEEK = createSite("deepseek",
            "https://api.deepseek.com/chat/completions",
            "deepseek-chat");

    public static OpenAISite TENCENT = createSite("tencent",
            "https://api.hunyuan.cloud.tencent.com/v1/chat/completions",
            "hunyuan-lite", "hunyuan-standard", "hunyuan-role", "hunyuan-turbo-latest", "hunyuan-large");

    public static OpenAISite createSite(String name, String url, String... models) {
        return createSite(name, url, false, Map.of(), models);
    }

    public static OpenAISite createSite(String name, String url, boolean enabled, Map<String, String> header, String... models) {
        return new OpenAISite(name, SerializableSite.defaultIcon(name), url, enabled, StringUtils.EMPTY, header, Lists.newArrayList(models));
    }
}
