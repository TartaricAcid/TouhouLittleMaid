package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ChatCompletionResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("object")
    private String object;

    @SerializedName("created")
    private long created;

    @SerializedName("model")
    private String model;

    @SerializedName("system_fingerprint")
    private String systemFingerprint;

    @SerializedName("choices")
    private Choice[] choices;

    @SerializedName("service_tier")
    private String serviceTier;

    @SerializedName("usage")
    private Usage usage;

    @Nullable
    public Message getFirstChoice() {
        if (this.choices.length > 0) {
            Choice choice = this.choices[0];
            return choice.getMessage();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    public Choice[] getChoices() {
        return choices;
    }

    public String getServiceTier() {
        return serviceTier;
    }

    public Usage getUsage() {
        return usage;
    }
}