package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.LLMOpenAISite;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public final class DefaultLLMSite {
    public static LLMOpenAISite PLAYER2 = createSite("player2",
            "http://127.0.0.1:4315/v1/chat/completions", true,
            Map.of("player2-game-key", "TouhouLittleMaid"),
            "default");

    public static LLMOpenAISite ALIYUN = createSite("aliyun",
            "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
            "qwen3-max", "qwen3.5-plus", "qwen3.5-flash");

    public static LLMOpenAISite DEEPSEEK = createSite("deepseek",
            "https://api.deepseek.com/chat/completions",
            "deepseek-chat");

    public static LLMOpenAISite SILICONFLOW = createSite("siliconflow",
            "https://api.siliconflow.cn/v1/chat/completions",
            "Pro/MiniMaxAI/MiniMax-M2.5",
            "Pro/zai-org/GLM-5",
            "Pro/moonshotai/Kimi-K2.5",
            "deepseek-ai/DeepSeek-V3.2",
            "Qwen/Qwen3.5-397B-A17B"
    );

    public static LLMOpenAISite createSite(String name, String url, String... models) {
        return createSite(name, url, false, Map.of(), models);
    }

    public static LLMOpenAISite createSite(String name, String url, boolean enabled, Map<String, String> header, String... models) {
        List<LLMOpenAISite.ModelEntry> modelEntries = Lists.newArrayList();
        for (String modelName : models) {
            modelEntries.add(new LLMOpenAISite.ModelEntry(modelName));
        }
        return new LLMOpenAISite(name, SerializableSite.defaultIcon(name), url, enabled, StringUtils.EMPTY, header, modelEntries);
    }

    public static void addDefaultSites() {
        AvailableSites.LLM_SITES.put(PLAYER2.id(), PLAYER2);
        AvailableSites.LLM_SITES.put(ALIYUN.id(), ALIYUN);
        AvailableSites.LLM_SITES.put(DEEPSEEK.id(), DEEPSEEK);
        AvailableSites.LLM_SITES.put(SILICONFLOW.id(), SILICONFLOW);
    }
}
