package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.FunctionCallRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
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
    protected final EntityMaid maid;
    protected final MaidAIChatManager chatManager;
    /**
     * 函数调用计数器，防止无限循环调用
     */
    protected int callCount;
    /**
     * 等待气泡的 ID，在获取到 LLM 传递的信息后，需要移除它
     */
    protected long waitingChatBubbleId;
    protected String message;

    public LLMCallback(MaidAIChatManager chatManager, String message, long waitingChatBubbleId) {
        this(chatManager, message, waitingChatBubbleId, 0);
    }

    public LLMCallback(MaidAIChatManager chatManager, String message, long waitingChatBubbleId, int callCount) {
        this.maid = chatManager.getMaid();
        this.chatManager = chatManager;
        this.message = message;
        this.waitingChatBubbleId = waitingChatBubbleId;
        this.callCount = callCount;
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
        IFunctionCall functionCall = FunctionCallRegister.getFunctionCall(name);
        if (functionCall == null) {
            return;
        }
        Object result = null;
        try {
            JsonObject parse = GsonHelper.parse(arguments);
            Optional optional = functionCall.codec().parse(JsonOps.INSTANCE, parse).resultOrPartial(TouhouLittleMaid.LOGGER::error);
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
        TouhouLittleMaid.LOGGER.debug("Use function call: {}, arguments is {}", functionCall.getId(), arguments);
        // 因为获取网络流是在独立的线程上，所以需要推送到主线程执行
        EntityMaid maid = config.maid();
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }
        Object finalResult = result;
        serverLevel.getServer().submit(() -> {
            // 工具调用必须在主线程，否则可能会出奇怪的问题
            // 计数增加，避免循环触发
            this.callCount = this.callCount + 1;
            // 告诉manager，正在处理这些tool call
            PendingToolCall pending = new PendingToolCall(toolCall.getId(), this.callCount, this.waitingChatBubbleId);
            chatManager.addPendingCall(pending);
            if (this.callCount >= AIConfig.MAX_AI_FUNCTION_CALL.get()) {
                // 超出调用数量
                String message = "Exceeded maximun tool call count.";
                TouhouLittleMaid.LOGGER.warn(message);
                chatManager.onPendingComplete(toolCall.getId(), "Error: " + message);
                return;
            }

            ToolResponse toolResponse = functionCall.onToolCall(finalResult, maid, toolCall.getId());
            // 处理延迟完成的工具调用
            if (toolResponse.isPending()) {
                TouhouLittleMaid.LOGGER.debug("Tool call {} is pending async completion", toolCall.getId());
                return;
            } else {
                // 立即完成的tool call, 继续进行下一轮 AI 对话
                String response = toolResponse.message();
                chatManager.onPendingComplete(toolCall.getId(), response);
            }
        });
    }
}
