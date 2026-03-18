package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ToolRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.UseSkillTool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.ChatType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.FunctionToolCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.Message;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LLMCallback implements ResponseCallback<ResponseChat> {
    private static final int MAX_CALL_COUNT = 16;
    protected final EntityMaid maid;
    protected final MaidAIChatManager chatManager;
    /**
     * 函数调用计数器，防止无限循环调用
     */
    protected int callCount = 0;
    /**
     * 等待气泡的 ID，在获取到 LLM 传递的信息后，需要移除它
     */
    protected long waitingChatBubbleId;
    protected String message;

    public LLMCallback(MaidAIChatManager chatManager, String message, long waitingChatBubbleId) {
        this.maid = chatManager.getMaid();
        this.chatManager = chatManager;
        this.message = message;
        this.waitingChatBubbleId = waitingChatBubbleId;
    }

    @Override
    public void onFailure(HttpRequest request, Throwable throwable, int errorCode) {
        if (maid.level instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            server.submit(() -> {
                if (maid.getOwner() instanceof ServerPlayer player) {
                    String cause = throwable.getLocalizedMessage();
                    MutableComponent errorMessage = ErrorCode.getErrorMessage(ServiceType.LLM, errorCode, cause);
                    player.sendSystemMessage(errorMessage.withStyle(ChatFormatting.RED));
                }
                maid.getChatBubbleManager().removeChatBubble(waitingChatBubbleId);
            });
        }
        if (errorCode == ErrorCode.CHAT_TEXT_IS_EMPTY) {
            TouhouLittleMaid.LOGGER.error("LLM return field is empty, error is {}", throwable.getMessage());
        } else if (errorCode == ErrorCode.JSON_DECODE_ERROR) {
            TouhouLittleMaid.LOGGER.error("Error in parsing LLM return JSON string, error is {}", throwable.getMessage());
        } else {
            TouhouLittleMaid.LOGGER.error("LLM request failed: {}, error is {}", request, throwable.getMessage());
        }
    }

    @Override
    public void onSuccess(ResponseChat responseChat) {
        String chatText = responseChat.getChatText();
        String ttsText = responseChat.getTtsText();

        if (chatText.isBlank() || ttsText.isBlank()) {
            String message = "Error in Response Chat: %s".formatted(responseChat);
            this.onFailure(null, new Throwable(message), ErrorCode.CHAT_TEXT_IS_EMPTY);
        } else {
            if (this.callCount == 0) {
                // 缓存历史聊天记录，当 callCount > 0 时
                // 说明是 Function Call 触发的调用，此时不需要重复缓存用户输入部分
                chatManager.addUserHistory(message);
            }
            chatManager.addAssistantHistory(responseChat.toString());

            TTSSite site = chatManager.getTTSSite();
            if (AIConfig.TTS_ENABLED.get() && site != null && site.enabled()) {
                chatManager.tts(site, chatText, ttsText, waitingChatBubbleId);
            } else {
                if (StringUtils.isNotBlank(chatText) && maid.level instanceof ServerLevel serverLevel) {
                    MinecraftServer server = serverLevel.getServer();
                    server.submit(() -> maid.getChatBubbleManager().addLLMChatText(chatText, waitingChatBubbleId));
                }
            }
        }
    }

    public void onFunctionCall(Message choice, List<LLMMessage> messages, LLMConfig config, LLMClient client) {
        if (this.callCount == 0) {
            // 缓存历史聊天记录，当 callCount > 0 时
            // 说明是 Function Call 触发的调用，此时不需要重复缓存用户输入部分
            chatManager.addUserHistory(message);
        }

        // 缓存 Function Call 的调用记录
        chatManager.addAssistantHistory(StringUtils.EMPTY, choice.getToolCalls());
        messages.add(LLMMessage.assistantChat(maid, choice.getContent(), choice.getToolCalls()));

        // 开始 Function Call：对重复的 tool_call 进行去重后再执行
        List<ToolCall> toolCalls = choice.getToolCalls() == null ? List.of() : choice.getToolCalls();
        Set<String> seen = Sets.newHashSet();
        List<ToolCall> deduped = Lists.newArrayList();
        for (ToolCall tc : toolCalls) {
            FunctionToolCall f = tc.getFunction();
            String name = f != null ? f.getName() : "unknown";
            String arguments = f != null ? f.getArguments() : "";
            String key = name + "|" + arguments;
            if (seen.add(key)) {
                deduped.add(tc);
            }
        }

        deduped.forEach(toolCall -> {
            try {
                this.onSingleCall(messages, config, client, toolCall);
            } catch (JsonSyntaxException exception) {
                String message = "Exception %s, JSON is: %s".formatted(exception.getLocalizedMessage(), toolCall.getFunction().getArguments());
                this.onFailure(null, new Throwable(message), ErrorCode.JSON_DECODE_ERROR);
            }
        });
    }

    @SuppressWarnings("all")
    private void onSingleCall(List<LLMMessage> messages, LLMConfig config, LLMClient client, ToolCall toolCall) throws JsonSyntaxException {
        FunctionToolCall function = toolCall.getFunction();
        String name = function.getName();
        String arguments = function.getArguments();

        ITool tool = ToolRegister.getTool(name);
        if (tool == null) {
            return;
        }

        Object result = null;
        try {
            JsonObject parse = GsonHelper.parse(arguments);
            Optional optional = tool.codec().parse(JsonOps.INSTANCE, parse).resultOrPartial(TouhouLittleMaid.LOGGER::error);
            if (optional.isEmpty()) {
                return;
            }
            result = optional.get();
        } catch (Exception exception) {
            String message = "Exception %s, JSON is: %s".formatted(exception.getLocalizedMessage(), arguments);
            this.onFailure(null, new Throwable(message), ErrorCode.JSON_DECODE_ERROR);
            return;
        }

        // 需要记录下工具调用，方便 debug
        TouhouLittleMaid.LOGGER.debug("Use function call: {}, arguments is {}", tool.id(), arguments);

        // 因为获取网络流是在独立的线程上，所以需要推送到主线程执行
        EntityMaid maid = config.maid();
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }

        Object finalResult = result;
        // 工具调用必须在主线程，否则可能会出奇怪的问题
        serverLevel.getServer().submit(() -> {
            // 计数增加，避免循环触发
            this.callCount = this.callCount + 1;
            if (this.callCount > MAX_CALL_COUNT) {
                TouhouLittleMaid.LOGGER.error("Function call count exceed max count: {}", MAX_CALL_COUNT);
            }

            // 历史记录缓存
            chatManager.addToolHistory("use tool: %s".format(toolCall.getId()), toolCall.getId());

            // 继续进行下一轮 AI 对话
            if (UseSkillTool.TOOL_ID.equals(name)) {
                // USE_SKILL 比较特殊
                if (!(finalResult instanceof String skillId)) {
                    // 此时 finalResult 必须是 string
                    String message = "Invalid tool call arguments for use_skill, expected string but got %s".formatted(finalResult);
                    this.onFailure(null, new Throwable(message), ErrorCode.JSON_DECODE_ERROR);
                    return;
                }

                ISkill skill = SkillRegister.getSkill(skillId);
                if (skill == null) {
                    String message = "Skill %s is not available for use".formatted(skillId);
                    this.onFailure(null, new Throwable(message), ErrorCode.JSON_DECODE_ERROR);
                    return;
                }

                if (!skill.trigger(maid)) {
                    return;
                }

                // 将 skill 的本体部分塞入 tool_result 里，而非系统提示词，这样可以让缓存系统缓存起作用
                messages.add(LLMMessage.toolChat(maid, skill.body(maid), toolCall.getId()));
                LLMConfig.SkillContext context = new LLMConfig.SkillContext(skillId);
                LLMConfig keepConfig = new LLMConfig(config.model(), config.maid(), ChatType.MULTI_FUNCTION_CALL, context);
                client.chat(messages, keepConfig, this);
            } else {
                ToolResponse toolResponse = tool.onCall(finalResult, maid);
                messages.add(LLMMessage.toolChat(maid, toolResponse.message(), toolCall.getId()));
                LLMConfig keepConfig = new LLMConfig(config.model(), config.maid(), ChatType.MULTI_FUNCTION_CALL);
                client.chat(messages, keepConfig, this);
            }
        });
    }
}
