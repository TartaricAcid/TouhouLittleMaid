package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;

public class ChatMessage {
    @SerializedName("role")
    private String role;

    @SerializedName("content")
    private String content;

    @SerializedName("tool_calls")
    @Nullable
    private List<ToolCall> toolCalls = null;

    @SerializedName("tool_call_id")
    @Nullable
    private String toolCallId = null;

    public static ChatMessage systemChat(String content) {
        return new ChatMessage(Role.SYSTEM.getId(), content);
    }

    public static ChatMessage userChat(String content) {
        return new ChatMessage(Role.USER.getId(), content);
    }

    public static ChatMessage assistantChat(String content) {
        return new ChatMessage(Role.ASSISTANT.getId(), content);
    }

    public static ChatMessage assistantChat(String content, List<ToolCall> toolCalls) {
        ChatMessage chatMessage = new ChatMessage(Role.ASSISTANT.getId(), content);
        chatMessage.toolCalls = toolCalls;
        return chatMessage;
    }

    public static ChatMessage toolChat(String content, String toolCallId) {
        ChatMessage chatMessage = new ChatMessage(Role.TOOL.getId(), content);
        chatMessage.toolCallId = toolCallId;
        return chatMessage;
    }

    private ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
