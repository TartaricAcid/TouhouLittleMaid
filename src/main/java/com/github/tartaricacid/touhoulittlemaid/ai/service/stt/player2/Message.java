package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }
}
