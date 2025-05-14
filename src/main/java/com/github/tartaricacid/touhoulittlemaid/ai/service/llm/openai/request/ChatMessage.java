package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.google.gson.annotations.SerializedName;

public class ChatMessage {
    @SerializedName("role")
    private String role;

    @SerializedName("content")
    private String content;

    public static ChatMessage systemChat(String content) {
        return new ChatMessage(Role.SYSTEM.getId(), content);
    }

    public static ChatMessage userChat(String content) {
        return new ChatMessage(Role.USER.getId(), content);
    }

    public static ChatMessage assistantChat(String content) {
        return new ChatMessage(Role.ASSISTANT.getId(), content);
    }

    private ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
