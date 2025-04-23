package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response;

import com.google.gson.annotations.SerializedName;

public class Choice {
    @SerializedName("index")
    private int index;

    @SerializedName("message")
    private Message message;

    @SerializedName("finish_reason")
    private String finishReason;

    public int getIndex() {
        return index;
    }

    public Message getMessage() {
        return message;
    }

    public String getFinishReason() {
        return finishReason;
    }
}