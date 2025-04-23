package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class Message {
    @SerializedName("role")
    private String role;

    @SerializedName("content")
    private String content;

    public String getRole() {
        return role;
    }

    @Nullable
    public String getContent() {
        return content;
    }
}