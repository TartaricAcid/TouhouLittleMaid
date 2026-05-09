package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.record.WikiRecord;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.grounded.GroundedAnswerCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.http.UrlTool;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite.LLM_HTTP_CLIENT;

public class QueryMinecraftWikiTool implements ITool<String> {
    public static final String TOOL_ID = "query_minecraft_wiki";

    private static final String QUERY_PARAMETER_ID = "query";
    private static final Codec<String> CODEC = Codec.STRING.fieldOf(QUERY_PARAMETER_ID).codec();

    private static final String USER_AGENT = "Minecraft TouhouLittleMaid Mod Search (baka943@qq.com)";
    private static final Map<String, String> WIKI_API = Map.of(
            "en_us", "https://minecraft.wiki/api.php",
            "zh_cn", "https://zh.minecraft.wiki/api.php"
    );

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);
    private static final long TOTAL_TIMEOUT_SECONDS = 40;

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return """
                LOW PRIORITY knowledge lookup for authoritative vanilla Minecraft Wiki information.
                Use only when the user explicitly asks to look up/wiki/search Minecraft knowledge, or when no more specific game, crafting, item, action, or mod-provided tool can answer or execute the request.
                Do NOT use this for crafting/recipe/item operations when a dedicated crafting or item tool is available.
                MAX 2 attempts; stop immediately if no results found.
                """.trim();
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter query = StringParameter.create().setDescription(
                "The search keyword or phrase. Use specific nouns (e.g., 'Creeper', 'Redstone', 'Enderman') " +
                "instead of complex natural language sentences to ensure accurate wiki indexing"
        ).setRange(1, 80);
        root.addProperties(QUERY_PARAMETER_ID, query);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public String invocationSummary(String result) {
        return "%s { %s }".formatted(TOOL_ID, StringUtils.abbreviate(result, 40));
    }

    @Override
    public Component invocationSummaryComponent(String result) {
        return Component.translatable("ai.touhou_little_maid.chat.tool_call.query_minecraft_wiki", result)
                .withStyle(ChatFormatting.GRAY);
    }

    @Override
    public LLMCallback onCall(String toolCallId, String result, LLMCallback callback) {
        // 这个工具是异步触发的，不会调用此方法
        return callback;
    }

    @Override
    public CompletableFuture<LLMCallback> onCallAsync(String toolCallId, String result, LLMCallback callback, LLMClient client) {
        String query = StringUtils.trimToEmpty(result);
        if (StringUtils.isBlank(query)) {
            LLMCallback toolResult = callback.addToolResult("Error: query is blank", toolCallId);
            return CompletableFuture.completedFuture(toolResult);
        }

        String language = callback.getChatManager().getChatLanguage();
        return this.fetchWikiSearch(query, language)
                // 查询完毕后，交给子 Agent 异步进行数据清洗
                .thenCompose(content -> {
                    if (StringUtils.isBlank(content)) {
                        return CompletableFuture.completedFuture("Cannot find relevant information");
                    }
                    // 手动刷新下气泡状态
                    callback.runOnServerThread(() -> {
                        Component tip = Component.translatable("ai.touhou_little_maid.chat.tool_call.query_minecraft_wiki.formulating.1")
                                .withStyle(ChatFormatting.GRAY);
                        callback.refreshWaitingChatBubble(tip);
                    });
                    return summarizeWithChildCallback(content, callback, client);
                })
                // 前三步总耗时不得超过 TOTAL_TIMEOUT_SECONDS 秒
                .orTimeout(TOTAL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                // 使用 handleAsync 确保无论成功还是失败
                // 最终都能在主线程上回调结果给主 Agent
                .handleAsync((summary, throwable) -> {
                    CompletableFuture<LLMCallback> finalResult = new CompletableFuture<>();
                    // 最终返回工具结果，必须在主线程上
                    callback.runOnServerThread(() -> {
                        // 再刷新下气泡状态
                        Component tip = Component.translatable("ai.touhou_little_maid.chat.tool_call.query_minecraft_wiki.formulating.2")
                                .withStyle(ChatFormatting.GRAY);
                        callback.refreshWaitingChatBubble(tip);

                        if (throwable != null) {
                            String error = "Error: " + throwable.getMessage();
                            finalResult.complete(callback.addToolResult(error, toolCallId));
                        } else {
                            // 将子 Agent 提取的知识回填给主 Agent
                            String handleSummary = "Source: Minecraft Wiki\nSummary: " + summary;
                            finalResult.complete(callback.addToolResult(handleSummary, toolCallId));
                        }
                    });
                    return finalResult;
                }, Runnable::run).thenCompose(f -> f);
    }

    private CompletableFuture<String> fetchWikiSearch(String query, String language) {
        String baseUrl = WIKI_API.getOrDefault(language, WIKI_API.get("en_us"));

        String url = UrlTool.buildQueryString(baseUrl, map -> {
            // 动作类型：查询
            map.put("action", "query");
            // 查询模块：全文搜索，同时搜索标题和正文
            map.put("list", "search");
            // 搜索关键词
            map.put("srsearch", query);
            // 返回数量：1，即仅返回最相关的结果
            map.put("srlimit", "1");
            // 返回格式 json
            map.put("format", "json");
        });

        HttpRequest searchRequest = createRequest(url);
        return LLM_HTTP_CLIENT.sendAsync(searchRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::requireSuccessfulResponse)
                .thenCompose(searchBody -> fetchWikiPage(searchBody, baseUrl));
    }

    private @NotNull CompletableFuture<String> fetchWikiPage(String searchBody, String baseUrl) {
        String title = LLMClient.GSON
                .fromJson(searchBody, WikiRecord.QueryTitleResult.class)
                .getFirstTitle();

        if (StringUtils.isBlank(title)) {
            return CompletableFuture.completedFuture(null);
        }

        String fetchUrl = UrlTool.buildQueryString(baseUrl, map -> {
            // 动作类型：查询
            map.put("action", "query");
            // 提取页面的摘要
            map.put("prop", "extracts");
            // 页面标题
            map.put("titles", title);
            // 别名自动重定向
            map.put("redirects", "1");
            // 纯文本模式
            map.put("explaintext", "1");
            // 返回格式 json
            map.put("format", "json");
        });

        HttpRequest pageRequest = createRequest(fetchUrl);
        return LLM_HTTP_CLIENT.sendAsync(pageRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::requireSuccessfulResponse)
                .thenApply(fetchBody -> LLMClient.GSON
                        .fromJson(fetchBody, WikiRecord.PageExtractResult.class)
                        .getFirstExtract()
                );
    }

    private CompletableFuture<String> summarizeWithChildCallback(String content, LLMCallback parentCallback, LLMClient client) {
        CompletableFuture<String> summaryFuture = new CompletableFuture<>();
        MaidAIChatManager chatManager = parentCallback.getChatManager();
        long bubbleId = parentCallback.getWaitingChatBubbleId();
        GroundedAnswerCallback callback = new GroundedAnswerCallback(chatManager, content, bubbleId, summaryFuture);
        client.chat(callback);
        return summaryFuture;
    }

    private static HttpRequest createRequest(String fetchUrl) {
        return HttpRequest.newBuilder()
                .uri(URI.create(fetchUrl))
                .header(HttpHeaders.ACCEPT, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .timeout(REQUEST_TIMEOUT)
                .GET().build();
    }

    private String requireSuccessfulResponse(HttpResponse<String> response) {
        if (200 <= response.statusCode() && response.statusCode() < 300) {
            return response.body();
        }
        throw new IllegalStateException("HTTP %s: %s".formatted(response.statusCode(), response.body()));
    }
}
