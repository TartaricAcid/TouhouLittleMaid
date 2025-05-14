package com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response;

import com.google.gson.annotations.SerializedName;

public class CompletionTokensDetails {
    @SerializedName("reasoning_tokens")
    private int reasoningTokens;

    @SerializedName("accepted_prediction_tokens")
    private int acceptedPredictionTokens;

    @SerializedName("rejected_prediction_tokens")
    private int rejectedPredictionTokens;

    public int getReasoningTokens() {
        return reasoningTokens;
    }

    public int getAcceptedPredictionTokens() {
        return acceptedPredictionTokens;
    }

    public int getRejectedPredictionTokens() {
        return rejectedPredictionTokens;
    }
}