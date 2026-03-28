package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatCompletion {
    @SerializedName("model")
    private String model = "";

    @SerializedName("messages")
    private List<ChatMessage> messages = Lists.newArrayList();

    @SerializedName("tools")
    private List<Tool> tools = null;

    @SerializedName("response_format")
    private ResponseFormat responseFormat = ResponseFormat.text();

    /**
     * 部分国内模型会添加此此段，故需要兼容
     */
    @SerializedName("thinking")
    private Thinking thinking = null;

    public static ChatCompletion create() {
        return new ChatCompletion();
    }

    public ChatCompletion model(String model) {
        this.model = model;
        return this;
    }

    public ChatCompletion systemChat(String message) {
        this.messages.add(ChatMessage.systemChat(message));
        return this;
    }

    public ChatCompletion userChat(String message) {
        this.messages.add(ChatMessage.userChat(message));
        return this;
    }

    public ChatCompletion assistantChat(String message) {
        this.messages.add(ChatMessage.assistantChat(message));
        return this;
    }

    public ChatCompletion assistantChat(String message, List<ToolCall> toolCalls) {
        this.messages.add(ChatMessage.assistantChat(message, toolCalls));
        return this;
    }

    public ChatCompletion toolChat(String message, String toolCallId) {
        this.messages.add(ChatMessage.toolChat(message, toolCallId));
        return this;
    }

    public ChatCompletion developerChat(String message) {
        this.messages.add(ChatMessage.developerChat(message));
        return this;
    }

    public ChatCompletion addTool(Tool tool) {
        if (this.tools == null) {
            this.tools = Lists.newArrayList();
        }
        this.tools.add(tool);
        return this;
    }

    public ChatCompletion disableThinking() {
        this.thinking = Thinking.disabled();
        return this;
    }

    /**
     * 部分站点（比如 MiniMax）不支持多个 system 消息，需要将多个 system 消息合并成一个消息发送
     */
    public ChatCompletion mergeSystemMessages() {
        boolean continuous = true;
        List<ChatMessage> copy = Lists.newArrayList();

        // 获取头部的 system 消息
        for (ChatMessage msg : this.messages) {
            if (continuous && msg.getRole().equals(Role.SYSTEM.getId())) {
                // 如果没有消息，那么加入
                if (copy.isEmpty()) {
                    copy.add(msg);
                } else {
                    // 如果有消息了，那么合并到第一个消息中
                    ChatMessage first = copy.get(0);
                    first = ChatMessage.systemChat(first.getContent() + "\n" + msg.getContent());
                    copy.set(0, first);
                }
            } else {
                continuous = false;
                copy.add(msg);
            }
        }

        this.messages = copy;
        return this;
    }

    public ChatCompletion setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
        return this;
    }
}
