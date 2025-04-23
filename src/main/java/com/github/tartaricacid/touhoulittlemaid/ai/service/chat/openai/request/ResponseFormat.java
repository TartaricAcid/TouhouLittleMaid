package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.request;

import com.google.gson.annotations.SerializedName;

public class ResponseFormat {
    @SerializedName("type")
    private String type;

    public static ResponseFormat text() {
        return new ResponseFormat("text");
    }

    public static ResponseFormat json() {
        return new ResponseFormat("json_object");
    }

    private ResponseFormat(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
