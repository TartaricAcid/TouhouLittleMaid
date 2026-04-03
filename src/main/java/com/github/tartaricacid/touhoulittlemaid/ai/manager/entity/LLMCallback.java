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
import java.util.StringJoiner;

public class LLMCallback implements ResponseCallback<ResponseChat> {
    /**
     * 最大对话轮次：16 次
     */
    private static final int MAX_TOOL_TURN_COUNT = 16;
    /**
     * 每个工具最大只允许重复调用两次，避免疯狂循环调用
     */
    private static final int MAX_REPEAT_TOOL_BATCH_COUNT = 2;
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
     * Tool 回合计数器，防止无限循环调用
     */
    protected int toolTurnCount = 0;
    /**
     * 连续重复的 Tool 批次计数器，用于拦截无进展的重复循环
     */
    protected int repeatedToolBatchCount = 0;
    /**
     * 上一次调用的 Tool 批次的签名，用于判断是否连续重复调用
     */
    protected String lastToolBatchSignature = StringUtils.EMPTY;

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
            String key = "ai.touhou_little_maid.chat.chat_bubble_waiting";
            this.waitingChatBubbleId = this.maid.getChatBubbleManager().addThinkingText(key);
        }
    }

    public LLMCallback(MaidAIChatManager chatManager, List<LLMMessage> messages) {
        this(chatManager, messages, false);
    }

    public LLMCallback addToolResult(String result, String toolId) {
        this.messages.add(LLMMessage.toolChat(maid, result, toolId));
        // 工具调用结果也如实记录进历史里，优化后续缓存命中
        this.chatManager.addToolHistory(result, toolId);
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

        TTSSite site = chatManager.getTTSSite();
        if (AIConfig.TTS_ENABLED.get() && site != null && site.enabled()) {
            // TODO 部分多模态模型，是直接在 LLM 回应的 JSON 里添加 TTS 信息
            // TODO 故需要考虑这种情况
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
        List<ToolCall> deduped = this.dedupToolCalls(choice.getToolCalls());
        if (deduped.isEmpty()) {
            String message = "No valid tool calls returned by LLM";
            this.onFailure(null, new Throwable(message), ErrorCode.CHAT_CHOICE_IS_EMPTY);
            return;
        }

        if (!this.beginToolBatch(deduped)) {
            return;
        }

        if (!(maid.level instanceof ServerLevel serverLevel)) {
            return;
        }

        boolean hasMultipleToolCalls = deduped.size() > 1;
        serverLevel.getServer().submit(() -> {
            // 旁路回调，记录并单独触发
            List<LLMCallback> sideCallbacks = Lists.newArrayList();
            // 主路回调，在本流程内依次执行工具调用，同时提取出所有的旁路回调
            LLMCallback nextCallback = this.executeToolBatch(deduped, hasMultipleToolCalls, sideCallbacks);
            // 先发送旁路子流程（如知识库查询），它们独立运行不影响主对话流
            for (LLMCallback side : sideCallbacks) {
                client.chat(side);
            }
            // 然后执行主路回调
            if (nextCallback != null) {
                client.chat(nextCallback);
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private LLMCallback onSingleCall(ToolCall toolCall, LLMCallback callback) throws JsonSyntaxException {
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
            return this.onToolErrorCall(toolCall, invalidMsg, callback);
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
                return this.onToolErrorCall(toolCall, invalidMsg, callback);
            }
            result = optional.get();
        } catch (Exception exception) {
            String invalidMsg = """
                    Invalid arguments for tool '%s': %s (raw JSON: %s).
                    Fix the arguments according to the schema and retry.
                    """.formatted(name, exception.getLocalizedMessage(), arguments);
            return this.onToolErrorCall(toolCall, invalidMsg, callback);
        }

        // 需要记录下工具调用，方便 debug
        TouhouLittleMaid.LOGGER.debug("Use function call: {}, arguments is {}", name, arguments);

        Object finalResult = result;
        // 工具调用的信息摘要
        String summary = tool.invocationSummary(finalResult);
        // 向玩家更新气泡，并提示当前正在调用工具
        callback.refreshWaitingChatBubble(summary);
        // 执行 tool，获得返回结果
        return tool.onCall(toolCall.getId(), finalResult, callback);
    }

    /**
     * 刷新等待气泡，在主思考文本下方追加一行灰色副文本，提示当前正在调用的工具名称。
     *
     * @param tool 当前正在执行的工具调用信息摘要
     */
    private void refreshWaitingChatBubble(String tool) {
        String key = "ai.touhou_little_maid.chat.chat_bubble_waiting_calling";
        Component secondaryText = Component.translatable(key, tool).withStyle(ChatFormatting.GRAY);
        this.waitingChatBubbleId = maid.getChatBubbleManager().refreshThinkingText(
                "ai.touhou_little_maid.chat.chat_bubble_waiting",
                waitingChatBubbleId, secondaryText
        );
    }

    /**
     * 如果大模型出现了幻觉，此时需要 tool result 里需要严肃指出，
     * 让大模型自己意识到这一点，并且在下一轮对话里进行纠正。
     */
    private LLMCallback onToolErrorCall(ToolCall toolCall, String invalidMsg, LLMCallback callback) {
        // 日志记录一下
        FunctionToolCall function = toolCall.getFunction();
        TouhouLittleMaid.LOGGER.warn("Tool call error: tool call is {}, arguments is {}",
                function.getName(), function.getArguments());

        // 记录发送的 tool result 消息，供后续对话使用
        return callback.addToolResult(invalidMsg, toolCall.getId());
    }

    /**
     * 对同一批次中的 tool_call 列表按签名去重，保留首次出现的调用。
     * <p>
     * LLM 偶尔会在同一轮返回完全相同的 tool_call（名称 + 参数一致），
     * 重复执行没有意义且会浪费 token，因此在这里统一过滤。
     */
    private List<ToolCall> dedupToolCalls(List<ToolCall> toolCalls) {
        List<ToolCall> safeToolCalls = toolCalls == null ? List.of() : toolCalls;
        Set<String> seen = Sets.newHashSet();
        List<ToolCall> deduped = Lists.newArrayList();
        for (ToolCall tc : safeToolCalls) {
            if (seen.add(getToolCallSignature(tc))) {
                deduped.add(tc);
            }
        }
        return deduped;
    }

    /**
     * 在执行一批 tool_call 之前进行前置检查，包括：
     * <ul>
     *   <li>累加工具调用轮次计数器，超过 {@link #MAX_TOOL_TURN_COUNT} 时中断；</li>
     *   <li>比较本轮批次签名与上一轮签名，检测连续重复调用并在超过
     *       {@link #MAX_REPEAT_TOOL_BATCH_COUNT} 时中断。</li>
     * </ul>
     *
     * @return {@code true} 表示可以继续执行，{@code false} 表示应中断当前调用链
     */
    private boolean beginToolBatch(List<ToolCall> toolCalls) {
        // 增加工具调用轮次，避免疯狂调用
        this.toolTurnCount = this.toolTurnCount + 1;

        // 超出，直接中断
        if (this.toolTurnCount > MAX_TOOL_TURN_COUNT) {
            String message = "Tool turn count exceed max count: %d".formatted(MAX_TOOL_TURN_COUNT);
            this.onFailure(null, new Throwable(message), ErrorCode.REQUEST_RECEIVED_ERROR);
            TouhouLittleMaid.LOGGER.error(message);
            return false;
        }

        // 检查是否连续重复调用同一批工具，如果是，增加计数器；如果不是，重置计数器并更新签名
        String signature = this.createToolBatchSignature(toolCalls);
        if (signature.equals(this.lastToolBatchSignature)) {
            this.repeatedToolBatchCount = this.repeatedToolBatchCount + 1;
        } else {
            this.lastToolBatchSignature = signature;
            this.repeatedToolBatchCount = 1;
        }

        // 如果连续重复调用同一批工具超过阈值，直接中断，避免无进展的循环调用
        if (this.repeatedToolBatchCount > MAX_REPEAT_TOOL_BATCH_COUNT) {
            String message = "Repeated identical tool batch exceed max count: %s".formatted(signature);
            TouhouLittleMaid.LOGGER.error(message);
            this.onFailure(null, new Throwable(message), ErrorCode.REQUEST_RECEIVED_ERROR);
            return false;
        }
        return true;
    }

    /**
     * 依次执行一批去重后的 tool_call，逐个调用 {@link #onSingleCall}。
     * <p>
     * 若某个工具返回了与当前主回调不同的子流程回调（如知识库查询产生的 {@code GroundedAnswerCallback}），
     * 不会中断主流程，而是将其收集到 {@code sideCallbacks} 中由调用方独立发送，
     * 同时向主回调补充一条占位 tool result，以满足 LLM 协议对每个 tool_call 都需要响应的要求。
     * <p>
     * 若某个工具调用抛出 {@link JsonSyntaxException}，会通过 {@link #onToolErrorCall}
     * 将错误信息写入 tool result，让 LLM 在下一轮自行纠正，而不会中断整批后续调用。
     *
     * @param toolCalls            去重后的工具调用列表
     * @param hasMultipleToolCalls 本批是否包含多个工具调用，用于决定子流程回调的处理策略
     * @param sideCallbacks        输出参数，收集需要独立发送的旁路子流程回调
     * @return 主流程的 {@link LLMCallback}，始终为当前会话的主回调
     */
    private LLMCallback executeToolBatch(List<ToolCall> toolCalls, boolean hasMultipleToolCalls, List<LLMCallback> sideCallbacks) {
        LLMCallback nextCallback = this;
        for (ToolCall toolCall : toolCalls) {
            try {
                LLMCallback returned = this.onSingleCall(toolCall, nextCallback);
                // 如果是子 agent
                if (returned != nextCallback) {
                    String placeholder = "Tool has been dispatched to a dedicated sub-agent; answer will follow separately.";
                    if (hasMultipleToolCalls) {
                        // 多个 tool 调用，那么把子 agent 单独剥离
                        sideCallbacks.add(returned);
                        // 同时向主回调补充一条占位 tool result，以免 LLM 缺少响应触发 400 error
                        nextCallback.addToolResult(placeholder, toolCall.getId());
                    } else {
                        // 单个子 agent，那么只需要替换返回值即可
                        nextCallback = returned;
                        // 此时不需要在回调里塞入 tool result，只需要在历史对话里塞入即可
                        this.chatManager.addToolHistory(placeholder, toolCall.getId());
                    }
                }
            } catch (JsonSyntaxException exception) {
                String message = "Exception %s, JSON is: %s".formatted(exception.getLocalizedMessage(), toolCall.getFunction().getArguments());
                this.onToolErrorCall(toolCall, message, nextCallback);
            }
        }
        return nextCallback;
    }

    /**
     * 将一批 tool_call 列表的各个签名用 {@code "||"} 连接，生成批次级别的签名字符串。
     * <p>
     * 该签名用于与上一轮批次签名对比，判断 LLM 是否在连续重复调用同一组工具。
     */
    private String createToolBatchSignature(List<ToolCall> toolCalls) {
        StringJoiner joiner = new StringJoiner("||");
        for (ToolCall toolCall : toolCalls) {
            joiner.add(getToolCallSignature(toolCall));
        }
        return joiner.toString();
    }

    /**
     * 为单个 {@link ToolCall} 生成归一化签名，格式为 {@code "name|arguments"}。
     * <p>
     * 签名会移除 arguments 中的所有空白字符，使得仅因 JSON 格式化差异
     * （如换行、缩进）不同的两次调用被视为相同调用。
     * <p>
     * 同时用于 {@link #dedupToolCalls} 的去重 key
     * 和 {@link #createToolBatchSignature} 的批次签名拼接。
     */
    private String getToolCallSignature(ToolCall toolCall) {
        FunctionToolCall function = toolCall.getFunction();
        String name = function != null ? function.getName() : "unknown";
        String arguments = function != null ? function.getArguments() : StringUtils.EMPTY;
        return name + "|" + StringUtils.deleteWhitespace(arguments);
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
