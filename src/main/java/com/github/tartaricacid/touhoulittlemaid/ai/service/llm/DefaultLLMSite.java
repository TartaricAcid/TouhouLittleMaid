package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializableSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.LLMOpenAISite;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class DefaultLLMSite {
    public static LLMOpenAISite DEEPSEEK = createSite("deepseek",
            "https://api.deepseek.com/chat/completions",
            true, true, Map.of(),
            "deepseek-v4-flash", "deepseek-v4-pro");

    public static LLMOpenAISite PLAYER2 = createSite("player2",
            "http://127.0.0.1:4315/v1/chat/completions", false,
            Map.of("player2-game-key", "TouhouLittleMaid"),
            "default");

    public static LLMOpenAISite ALIYUN = createSite("aliyun",
            "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
            "qwen3-max", "qwen3.5-plus", "qwen3.5-flash");

    public static LLMOpenAISite SILICONFLOW = createSite("siliconflow",
            "https://api.siliconflow.cn/v1/chat/completions",
            "Pro/MiniMaxAI/MiniMax-M2.5",
            "Pro/zai-org/GLM-5",
            "Pro/moonshotai/Kimi-K2.5",
            "Pro/deepseek-ai/DeepSeek-V3.2",
            "Qwen/Qwen3.5-397B-A17B"
    );

    public static LLMOpenAISite DOUBAO = createSite("doubao",
            "https://ark.cn-beijing.volces.com/api/v3/chat/completions", true,
            "doubao-seed-2-0-pro-260215",
            "doubao-seed-2-0-lite-260215",
            "doubao-seed-2-0-mini-260215"
    );

    public static LLMOpenAISite ZHIPU = createSite("zhipu",
            "https://open.bigmodel.cn/api/paas/v4/chat/completions", true,
            "glm-5", "glm-5-turbo", "glm-4.7", "glm-4.6"
    );

    public static LLMOpenAISite MINIMAX = createSite("minimax",
            "https://api.minimaxi.com/v1/chat/completions",
            "MiniMax-M2.7", "MiniMax-M2.7-highspeed", "MiniMax-M2.5", "MiniMax-M2.5-highspeed"
    );

    public static LLMOpenAISite KIMI = createSite("kimi",
            "https://api.moonshot.cn/v1/chat/completions", true,
            "kimi-k2.5", "kimi-k2-turbo-preview"
    );

    public static LLMOpenAISite GEMINI = createSite("gemini",
            "https://generativelanguage.googleapis.com/v1beta/openai/chat/completions",
            "gemini-3.1-pro-preview", "gemini-3-flash-preview", "gemini-3.1-flash-lite-preview"
    );

    public static LLMOpenAISite GROK = createSite("grok",
            "https://api.x.ai/v1/chat/completions",
            "grok-4.20-0309-non-reasoning", "grok-4-1-fast-non-reasoning"
    );

    public static LLMOpenAISite OPEN_ROUTER = createSite("openrouter",
            "https://openrouter.ai/api/v1/chat/completions", false,
            Map.of("HTTP-Referer", "https://github.com/TartaricAcid/TouhouLittleMaid",
                    "X-OpenRouter-Title", "Touhou Little Maid Mod (Minecraft)"
            ), "xiaomi/mimo-v2-flash", "google/gemini-3-flash-preview", "x-ai/grok-4.1-fast"
    );

    public static Consumer<LLMSite> FIXED_DEEPSEEK = site -> {
        if (site instanceof LLMOpenAISite openAISite) {
            Map<String, String> models = openAISite.models();
            // DeepSeek 将于 2026/07/24 弃用这些模型名，需要修正
            openAISite.removeModel("deepseek-chat");
            openAISite.removeModel("deepseek-reasoner");
            if (!models.containsKey("deepseek-v4-flash")) {
                openAISite.addModel("deepseek-v4-flash");
            }
            if (!models.containsKey("deepseek-v4-pro")) {
                openAISite.addModel("deepseek-v4-pro");
            }
            openAISite.setHasThinkingField(true);
        }
    };

    public static Consumer<LLMSite> FIXED_THINKING = site -> {
        if (site instanceof LLMOpenAISite openAISite) {
            openAISite.setHasThinkingField(true);
        }
    };

    public static LLMOpenAISite createSite(String name, String url, String... models) {
        return createSite(name, url, false, Map.of(), models);
    }

    public static LLMOpenAISite createSite(String name, String url, boolean hasThinkingField, String... models) {
        return createSite(name, url, false, hasThinkingField, Map.of(), models);
    }

    public static LLMOpenAISite createSite(String name, String url, boolean enabled, Map<String, String> header, String... models) {
        return createSite(name, url, enabled, false, header, models);
    }

    public static LLMOpenAISite createSite(String name, String url, boolean enabled, boolean hasThinkingField, Map<String, String> header, String... models) {
        List<LLMOpenAISite.ModelEntry> modelEntries = Lists.newArrayList();
        for (String modelName : models) {
            modelEntries.add(new LLMOpenAISite.ModelEntry(modelName));
        }
        return new LLMOpenAISite(name, SerializableSite.defaultIcon(name), url, enabled, StringUtils.EMPTY, hasThinkingField, header, modelEntries);
    }

    public static void addDefaultSites() {
        AvailableSites.LLM_SITES.put(DEEPSEEK.id(), DEEPSEEK);
        AvailableSites.LLM_SITES.put(PLAYER2.id(), PLAYER2);
        AvailableSites.LLM_SITES.put(ALIYUN.id(), ALIYUN);
        AvailableSites.LLM_SITES.put(SILICONFLOW.id(), SILICONFLOW);
        AvailableSites.LLM_SITES.put(DOUBAO.id(), DOUBAO);
        AvailableSites.LLM_SITES.put(ZHIPU.id(), ZHIPU);
        AvailableSites.LLM_SITES.put(MINIMAX.id(), MINIMAX);
        AvailableSites.LLM_SITES.put(KIMI.id(), KIMI);
        AvailableSites.LLM_SITES.put(GEMINI.id(), GEMINI);
        AvailableSites.LLM_SITES.put(GROK.id(), GROK);
        AvailableSites.LLM_SITES.put(OPEN_ROUTER.id(), OPEN_ROUTER);

        AvailableSites.FIXED_LLM_SITES.put(DEEPSEEK.id(), FIXED_DEEPSEEK);
        AvailableSites.FIXED_LLM_SITES.put(DOUBAO.id(), FIXED_THINKING);
        AvailableSites.FIXED_LLM_SITES.put(ZHIPU.id(), FIXED_THINKING);
        AvailableSites.FIXED_LLM_SITES.put(KIMI.id(), FIXED_THINKING);
    }
}
