package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.LLMOpenAISite;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public final class DefaultLLMSite {
    public static LLMOpenAISite PLAYER2 = createSite("player2",
            "http://127.0.0.1:4315/v1/chat/completions", true,
            Map.of("player2-game-key", "TouhouLittleMaid"),
            "default");

    public static LLMOpenAISite ALIYUN = createSite("aliyun",
            "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
            "qwen-plus", "qwen-max", "qwen-turbo", "qwen-long");

    public static LLMOpenAISite DEEPSEEK = createSite("deepseek",
            "https://api.deepseek.com/chat/completions",
            "deepseek-chat");

    public static LLMOpenAISite TENCENT = createSite("tencent",
            "https://api.hunyuan.cloud.tencent.com/v1/chat/completions",
            "hunyuan-lite", "hunyuan-standard", "hunyuan-role", "hunyuan-turbo-latest", "hunyuan-large");

    public static LLMOpenAISite SILICONFLOW = createSite("siliconflow",
            "https://api.siliconflow.cn/v1/chat/completions",
            "Qwen/Qwen3-8B", "THUDM/GLM-Z1-9B-0414", "deepseek-ai/DeepSeek-V3");

    public static LLMOpenAISite DOUBAO = createSite("doubao",
            "https://ark.cn-beijing.volces.com/api/v3/chat/completions",
            "doubao-1.5-pro-32k-250115", "doubao-1.5-pro-256k-250115",
            "doubao-1.5-lite-32k-250115", "deepseek-v3-250324");

    public static LLMOpenAISite createSite(String name, String url, String... models) {
        return createSite(name, url, false, Map.of(), models);
    }

    public static LLMOpenAISite createSite(String name, String url, boolean enabled, Map<String, String> header, String... models) {
        return new LLMOpenAISite(name, SerializableSite.defaultIcon(name), url, enabled, StringUtils.EMPTY, header, Lists.newArrayList(models));
    }

    public static void addDefaultSites() {
        AvailableSites.LLM_SITES.put(PLAYER2.id(), PLAYER2);
        AvailableSites.LLM_SITES.put(ALIYUN.id(), ALIYUN);
        AvailableSites.LLM_SITES.put(DEEPSEEK.id(), DEEPSEEK);
        AvailableSites.LLM_SITES.put(TENCENT.id(), TENCENT);
        AvailableSites.LLM_SITES.put(SILICONFLOW.id(), SILICONFLOW);
        AvailableSites.LLM_SITES.put(DOUBAO.id(), DOUBAO);
    }
}
