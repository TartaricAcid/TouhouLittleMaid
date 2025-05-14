package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatCompletion {
    @SerializedName("model")
    private String model = "";

    @SerializedName("messages")
    private List<ChatMessage> messages = Lists.newArrayList();

    @SerializedName("response_format")
    private ResponseFormat responseFormat = ResponseFormat.text();

    @SerializedName("max_tokens")
    private int maxTokens = 256;

    @SerializedName("temperature")
    private double temperature = 0.5;

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

    public ChatCompletion maxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
        return this;
    }

    public ChatCompletion temperature(double temperature) {
        // 温度的范围是 [0,2)
        this.temperature = Math.min(temperature, 1.99);
        return this;
    }

    public ChatCompletion setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
        return this;
    }
}
