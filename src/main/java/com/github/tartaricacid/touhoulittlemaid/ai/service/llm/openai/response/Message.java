package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Message {
    @SerializedName("role")
    private String role;

    @SerializedName("content")
    private String content;

    @SerializedName("tool_calls")
    private List<ToolCall> toolCalls;

    public String getRole() {
        return role;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public boolean hasToolCall() {
        return this.toolCalls != null && !this.toolCalls.isEmpty();
    }

    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }
}