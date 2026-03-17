package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ToolRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.FunctionTool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.*;
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
import java.util.List;

public class LLMOpenAIClient implements LLMClient {
    protected static final Duration MAX_TIMEOUT = Duration.ofSeconds(60);

    protected final HttpClient httpClient;
    protected final LLMOpenAISite site;

    public LLMOpenAIClient(HttpClient httpClient, LLMOpenAISite site) {
        this.httpClient = httpClient;
        this.site = site;
    }

    @Override
    public void chat(List<LLMMessage> messages, LLMConfig config, ResponseCallback<ResponseChat> callback) {
        URI url = URI.create(this.site.url());
        String apiKey = this.site.secretKey();
        String model = config.model();
        boolean isReasoningModel = this.site.isReasoningModel(model);
        double temperature = config.temperature();
        int maxTokens = config.maxTokens();
        EntityMaid maid = config.maid();

        // 构建对话
        ChatCompletion chatCompletion;

        // 如果是新版 open ai reasoning 模型
        // 没有 temperature 和 maxTokens 参数
        if (isReasoningModel) {
            chatCompletion = ChatCompletion.create().model(model)
                    .maxCompletionTokens(maxTokens)
                    .setResponseFormat(ResponseFormat.text());
        } else {
            chatCompletion = ChatCompletion.create().model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .setResponseFormat(ResponseFormat.text());
        }

        // 添加额外参数
        chatCompletion = this.extraArgs(chatCompletion);

        // 添加消息
        for (LLMMessage message : messages) {
            if (message.role() == Role.USER) {
                chatCompletion.userChat(message.message());
            } else if (message.role() == Role.ASSISTANT) {
                if (message.toolCalls() == null || message.toolCalls().isEmpty()) {
                    chatCompletion.assistantChat(message.message());
                } else {
                    chatCompletion.assistantChat(message.message(), message.toolCalls());
                }
            } else if (message.role() == Role.SYSTEM) {
                // reasoning 使用 developer 模式，系统消息需要特殊处理
                if (isReasoningModel) {
                    chatCompletion.developerChat(message.message());
                } else {
                    chatCompletion.systemChat(message.message());
                }
            } else if (message.role() == Role.TOOL) {
                chatCompletion.toolChat(message.message(), message.toolCallId());
            }
        }

        // 添加 skill
        // FIXME 修改配置名称
        if (AIConfig.FUNCTION_CALL_ENABLED.get()) {
            this.addRootSkills(maid, config, chatCompletion);
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
                        handle(messages, config, callback, response, throwable, httpRequest));
    }

    /**
     * 用于添加额外的参数，主要用于一些非标准 OpenAI API 模型的额外参数添加
     *
     */
    protected ChatCompletion extraArgs(ChatCompletion chatCompletion) {
        return chatCompletion;
    }

    protected void addRootSkills(EntityMaid maid, LLMConfig config, ChatCompletion chatCompletion) {
        ChatType chatType = config.chatType();

        // 首次生成角色设定时不需要添加
        if (chatType == ChatType.AUTO_GEN_SETTING) {
            return;
        }

        // 首次对话只需要添加基本上 use_skill 的 skill
        if (chatType == ChatType.NORMAL_CHAT) {
            LLMConfig.SkillContext context = new LLMConfig.SkillContext(SkillRegister.USE_SKILL);
            this.addSkillFromContext(maid, chatCompletion, context);
            return;
        }

        // 多轮 Function call 需要按需加载
        if (chatType == ChatType.MULTI_FUNCTION_CALL && config.skillContext() != null) {
            LLMConfig.SkillContext context = config.skillContext();
            this.addSkillFromContext(maid, chatCompletion, context);
        }
    }

    protected void addSkillFromContext(EntityMaid maid, ChatCompletion chatCompletion, LLMConfig.SkillContext context) {
        ISkill skill = SkillRegister.getSkill(context.skillId());
        if (skill == null || !skill.trigger(maid)) {
            return;
        }

        // 依据 skill 的 tool 按需加载
        skill.tools(maid).forEach(toolId -> {
            ITool<?> tool = ToolRegister.getTool(toolId);
            if (tool == null || !tool.trigger(maid, chatCompletion)) {
                return;
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
        });
    }

    protected void handle(List<LLMMessage> messages, LLMConfig config, ResponseCallback<ResponseChat> callback,
                          HttpResponse<String> response, Throwable throwable, HttpRequest request) {
        this.<ChatCompletionResponse>handleResponse(callback, response, throwable, request, chat -> {
            if (TouhouLittleMaid.DEBUG) {
                TouhouLittleMaid.LOGGER.info(GSON.toJson(chat));
            }

            Usage usage = chat.getUsage();
            if (usage != null) {
                // TOKEN 计数
                int totalTokens = usage.getTotalTokens();
                if (totalTokens > 0 && config.maid().getOwner() instanceof ServerPlayer serverPlayer) {
                    serverPlayer.getCapability(ChatTokensCapabilityProvider.CHAT_TOKENS_CAP)
                            .ifPresent(tokens -> tokens.addCount(totalTokens));
                }
            }

            Message firstChoice = chat.getFirstChoice();
            if (firstChoice == null) {
                String message = "No Choice Found: %s".formatted(response);
                callback.onFailure(request, new Throwable(message), ErrorCode.CHAT_CHOICE_IS_EMPTY);
                return;
            }
            if (firstChoice.hasToolCall()) {
                ((LLMCallback) callback).onFunctionCall(firstChoice, messages, config, this);
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
