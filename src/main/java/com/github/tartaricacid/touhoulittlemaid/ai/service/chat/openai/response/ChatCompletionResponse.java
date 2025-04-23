package com.github.tartaricacid.touhoulittlemaid.ai.service.chat.openai.response;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

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

    public String getFirstChoiceMessage() {
        if (this.choices.length > 0) {
            Choice choice = this.choices[0];
            String content = choice.getMessage().getContent();
            return content == null ? StringUtils.EMPTY : content;
        }
        return StringUtils.EMPTY;
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