package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

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
     * 旧版 OpenAI API 参数，大部分国产模型仍在使用此标准
     */
    @SerializedName("temperature")
    private Double temperature = null;

    /**
     * 仅适用于 doubao 模型的字段
     */
    @SerializedName("thinking")
    private DoubaoThinking thinking = null;

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

    public ChatCompletion temperature(double temperature) {
        // 温度的范围是 [0,2)
        this.temperature = Math.min(temperature, 1.99);
        return this;
    }

    /**
     * 仅适用于 doubao 模型的字段
     */
    public ChatCompletion thinking(DoubaoThinking thinking) {
        this.thinking = thinking;
        return this;
    }

    public ChatCompletion setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
        return this;
    }
}
