package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ToolRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
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
import net.minecraft.network.chat.Component;
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
    private static final String WAITING_KEY = "ai.touhou_little_maid.chat.chat_bubble_waiting";

    /**
     * 当前正在对话的女仆对象
     */
    protected final EntityMaid maid;
    /**
     * 聊天管理器，提供一些需要持久化的数据，比如缓存历史记录等
     */
    protected final MaidAIChatManager chatManager;
    /**
     * 当前准备向 LLM 发送的消息内容，包含需要的历史对话上下文
     */
    protected List<LLMMessage> messages;
    /**
     * 等待气泡的 ID，在获取到 LLM 传递的信息后，需要移除它
     */
    protected long waitingChatBubbleId;
    /**
     * 函数调用计数器，防止无限循环调用
     */
    protected int callCount = 0;

    /**
     * 此次回调是否需要添加 tools，比如压缩上下文，回答知识库问题的就不需要加入 tools
     */
    public boolean needAddTools = true;

    public LLMCallback(MaidAIChatManager chatManager, List<LLMMessage> messages, boolean subagents) {
        this.maid = chatManager.getMaid();
        this.chatManager = chatManager;
        this.messages = messages;
        // 如果是子 agent，那么无需添加聊天气泡
        if (!subagents) {
            this.waitingChatBubbleId = this.maid.getChatBubbleManager().addThinkingText(WAITING_KEY);
        }
    }

    public LLMCallback(MaidAIChatManager chatManager, List<LLMMessage> messages) {
        this(chatManager, messages, false);
    }

    public LLMCallback addToolResult(String result, String toolId) {
        this.messages.add(LLMMessage.toolChat(maid, result, toolId));
        return this;
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

    /**
     * 普通文字回答时，会调用此方法
     *
     * @param responseChat 响应数据
     */
    @Override
    public void onSuccess(ResponseChat responseChat) {
        String chatText = responseChat.getChatText();
        String ttsText = responseChat.getTtsText();

        if (chatText.isBlank() || ttsText.isBlank()) {
            String message = "Error in Response Chat: %s".formatted(responseChat);
            this.onFailure(null, new Throwable(message), ErrorCode.CHAT_TEXT_IS_EMPTY);
            return;
        }

        // 记录 LLM 的回答到历史中，供后续对话使用
        chatManager.addAssistantHistory(responseChat.toString());
        messages.add(LLMMessage.assistantChat(maid, chatText));

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

    /**
     * 函数调用时，会调用此方法
     */
    public void onFunctionCall(Message choice, LLMClient client) {
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

        // FIXME 如果多个工具触发了，怎么计数？
        deduped.forEach(toolCall -> {
            try {
                this.onSingleCall(toolCall, client);
            } catch (JsonSyntaxException exception) {
                String message = "Exception %s, JSON is: %s".formatted(exception.getLocalizedMessage(), toolCall.getFunction().getArguments());
                this.onFailure(null, new Throwable(message), ErrorCode.JSON_DECODE_ERROR);
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void onSingleCall(ToolCall toolCall, LLMClient client) throws JsonSyntaxException {
        // 先检查调用次数，防止无限循环调用
        this.callCount = this.callCount + 1;
        if (this.callCount > MAX_CALL_COUNT) {
            TouhouLittleMaid.LOGGER.error("Function call count exceed max count: {}", MAX_CALL_COUNT);
        }

        // 检查 LLM 调用的工具和参数是否正确
        FunctionToolCall function = toolCall.getFunction();
        String name = function.getName();
        String arguments = function.getArguments();

        // 工具是否存在
        ITool tool = ToolRegister.getTool(name);
        if (tool == null) {
            String invalidMsg = """
                    Unknown tool '%s'. It is not registered.
                    Use only tool ids from the provided schema and retry.
                    """.formatted(name);
            this.onToolErrorCall(toolCall, invalidMsg, client);
            return;
        }

        // JSON 语法和参数是否正确
        Object result;
        try {
            JsonObject parse = GsonHelper.parse(arguments);
            Optional optional = tool.codec().parse(JsonOps.INSTANCE, parse).resultOrPartial(TouhouLittleMaid.LOGGER::error);
            if (optional.isEmpty()) {
                String invalidMsg = """
                        Failed to parse arguments for tool '%s': '%s'.
                        Check the parameter schema and retry with valid JSON.
                        """.formatted(name, arguments);
                this.onToolErrorCall(toolCall, invalidMsg, client);
                return;
            }
            result = optional.get();
        } catch (Exception exception) {
            String invalidMsg = """
                    Invalid arguments for tool '%s': %s (raw JSON: %s).
                    Fix the arguments according to the schema and retry.
                    """.formatted(name, exception.getLocalizedMessage(), arguments);
            this.onToolErrorCall(toolCall, invalidMsg, client);
            return;
        }

        // 需要记录下工具调用，方便 debug
        TouhouLittleMaid.LOGGER.debug("Use function call: {}, arguments is {}", name, arguments);

        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }

        Object finalResult = result;
        // 工具调用必须在主线程，否则可能会出奇怪的问题
        serverLevel.getServer().submit(() -> {
            // 向玩家更新气泡，并提示当前正在调用工具
            this.refreshWaitingChatBubble(name);
            // 历史记录缓存，注意这里并不原样记录工具调用的参数
            // 而是记录一个简单的字符串，避免历史记录过于冗长，同时污染上下文窗口
            chatManager.addToolHistory("Use Tool: %s".formatted(name), toolCall.getId());
            // 执行 tool，获得返回结果
            LLMCallback callback = tool.onCall(toolCall.getId(), finalResult, this);
            // 再次和 LLM 通信，注意此时用的是 Function Call 传递回的回调，可能已经被修改了
            client.chat(callback);
        });
    }

    private void refreshWaitingChatBubble(String tool) {
        Component secondaryText = Component
                .translatable("ai.touhou_little_maid.chat.chat_bubble_waiting_calling", tool)
                .withStyle(ChatFormatting.GRAY);
        this.waitingChatBubbleId = maid.getChatBubbleManager().refreshThinkingText(
                "ai.touhou_little_maid.chat.chat_bubble_waiting",
                waitingChatBubbleId,
                secondaryText
        );
    }

    /**
     * 如果大模型出现了幻觉，此时需要 tool result 里需要严肃指出，
     * 让大模型自己意识到这一点，并且在下一轮对话里进行纠正。
     */
    private void onToolErrorCall(ToolCall toolCall, String invalidMsg, LLMClient client) {
        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }

        // 日志记录一下
        FunctionToolCall function = toolCall.getFunction();
        TouhouLittleMaid.LOGGER.warn("Tool call error: tool call is {}, arguments is {}",
                function.getName(), function.getArguments());

        // 必须在主线程，否则可能会出奇怪的问题
        serverLevel.getServer().submit(() -> {
            // 记录发送的 tool result 消息，供后续对话使用
            chatManager.addToolHistory(invalidMsg, toolCall.getId());
            messages.add(LLMMessage.toolChat(maid, invalidMsg, toolCall.getId()));
            // 继续对话
            client.chat(this);
        });
    }

    public EntityMaid getMaid() {
        return this.maid;
    }

    public List<LLMMessage> getMessages() {
        return this.messages;
    }

    public MaidAIChatManager getChatManager() {
        return chatManager;
    }

    public long getWaitingChatBubbleId() {
        return waitingChatBubbleId;
    }
}
