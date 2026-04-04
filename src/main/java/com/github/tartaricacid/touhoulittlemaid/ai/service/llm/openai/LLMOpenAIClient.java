package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ToolRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.FunctionTool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.DefaultLLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ResponseFormat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ChatCompletionResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.Message;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.Usage;
import com.github.tartaricacid.touhoulittlemaid.capability.ChatTokensCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LLMOpenAIClient implements LLMClient {
    protected static final Duration MAX_TIMEOUT = Duration.ofSeconds(60);

    protected final HttpClient httpClient;
    protected final LLMOpenAISite site;

    public LLMOpenAIClient(HttpClient httpClient, LLMOpenAISite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void chat(LLMCallback callback) {
        EntityMaid maid = callback.getMaid();

        // 模型站点信息获取
        URI url = URI.create(this.site.url());
        String apiKey = this.site.secretKey();
        String model = maid.getAiChatManager().getLLMModel();
        boolean isReasoningModel = this.site.isReasoningModel(model);

        // 构建对话
        ChatCompletion chatCompletion = ChatCompletion.create().model(model)
                .setResponseFormat(ResponseFormat.text());

        // 添加额外参数
        chatCompletion = this.extraArgs(chatCompletion);

        // 添加消息
        for (LLMMessage message : callback.getMessages()) {
            if (message.role() == Role.USER) {
                chatCompletion.userChat(message.message());
            } else if (message.role() == Role.ASSISTANT) {
                if (message.toolCalls() == null || message.toolCalls().isEmpty()) {
                    chatCompletion.assistantChat(message.message());
                } else {
                    chatCompletion.assistantChat(message.message(), message.toolCalls());
                }
            } else if (message.role() == Role.SYSTEM) {
                // 如果是新版 open ai reasoning 使用 developer 模式
                // 系统消息需要特殊处理
                if (isReasoningModel) {
                    chatCompletion.developerChat(message.message());
                } else {
                    chatCompletion.systemChat(message.message());
                }
            } else if (message.role() == Role.TOOL) {
                chatCompletion.toolChat(message.message(), message.toolCallId());
            }
        }

        // 添加所有 tools
        if (callback.needAddTools) {
            for (var entry : ToolRegister.getAllTools().entrySet()) {
                String toolId = entry.getKey();
                ITool<?> tool = entry.getValue();

                if (tool == null || !tool.trigger(maid, chatCompletion)) {
                    continue;
                }

                String summary = tool.summary(maid);
                ObjectParameter root = ObjectParameter.create();
                Parameter parameter = tool.parameters(root, maid);
                chatCompletion.addTool(FunctionTool.create()
                        .setName(toolId)
                        .setDescription(summary)
                        .setParameters(parameter)
                        .build()
                );
            }
        }

        // 如果是 minimax 站点，它不支持输入多个 system 消息，所以需要将 system 消息合并到一起
        // https://github.com/MiniMax-AI/MiniMax-M2/issues/51#issuecomment-3570551456
        if (this.site.id().equals(DefaultLLMSite.MINIMAX.id())) {
            chatCompletion.mergeSystemMessages();
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(chatCompletion)))
                .timeout(MAX_TIMEOUT).uri(url);

        if (TouhouLittleMaid.DEBUG) {
            TouhouLittleMaid.LOGGER.info(GSON.toJson(chatCompletion));
        }

        this.site.headers().forEach(builder::header);
        HttpRequest httpRequest = builder.build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .whenComplete((response, throwable) ->
                        handle(callback, response, throwable, httpRequest));
    }

    /**
     * 用于添加额外的参数，主要用于一些非标准 OpenAI API 模型的额外参数添加
     *
     */
    protected ChatCompletion extraArgs(ChatCompletion chatCompletion) {
        // 部分国内模型站点会添加此字段
        if (this.site.hasThinkingField()) {
            return chatCompletion.disableThinking();
        }
        return chatCompletion;
    }

    protected void handle(LLMCallback callback, HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        // 优先检查女仆是否存在
        EntityMaid maid = callback.getMaid();
        if (this.shouldStopChat(maid)) {
            return;
        }

        this.<ChatCompletionResponse>handleResponse(callback, response, throwable, request, chat -> {
            if (TouhouLittleMaid.DEBUG) {
                TouhouLittleMaid.LOGGER.info(GSON.toJson(chat));
            }

            Usage usage = chat.getUsage();
            if (usage != null) {
                // TOKEN 计数
                int totalTokens = usage.getTotalTokens();
                if (totalTokens > 0 && callback.getMaid().getOwner() instanceof ServerPlayer serverPlayer) {
                    int tokenCount = serverPlayer.getCapability(ChatTokensCapabilityProvider.CHAT_TOKENS_CAP).map(tokens -> {
                        tokens.addCount(totalTokens);
                        return tokens.getCount();
                    }).orElse(0);

                    // 如果此时 token 超过配置，那么就触发回调失败
                    int tokenLimit = AIConfig.MAX_TOKENS_PER_PLAYER.get();
                    if (tokenCount > tokenLimit) {
                        String message = "Token Limit Exceeded: %d tokens used, limit is %d".formatted(tokenCount, tokenLimit);
                        callback.onFailure(request, new Throwable(message), ErrorCode.CHAT_TOKEN_LIMIT_EXCEEDED);
                        return;
                    }
                }
            }

            Message firstChoice = chat.getFirstChoice();
            if (firstChoice == null) {
                String message = "No Choice Found: %s".formatted(response);
                callback.onFailure(request, new Throwable(message), ErrorCode.CHAT_CHOICE_IS_EMPTY);
                return;
            }
            if (firstChoice.hasToolCall()) {
                callback.onFunctionCall(firstChoice, this);
            } else {
                this.onTextCall(callback, firstChoice);
            }
        }, ChatCompletionResponse.class);
    }

    protected void onTextCall(ResponseCallback<ResponseChat> callback, Message firstChoice) {
        String content = firstChoice.getContent();
        if (StringUtils.isBlank(content)) {
            callback.onSuccess(new ResponseChat(StringUtils.EMPTY, StringUtils.EMPTY));
            return;
        }
        callback.onSuccess(new ResponseChat(content));
    }
}
