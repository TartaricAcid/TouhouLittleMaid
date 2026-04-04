package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.tencent;

import com.google.gson.annotations.SerializedName;

public record ResponseMessage(@SerializedName("Response") Response response) {
    public record Response(
            @SerializedName("Result") String result,
            @SerializedName("Error") Error error
    ) {
    }

    public record Error(
            @SerializedName("Code") String code,
            @SerializedName("Message") String message
    ) {
    }
}
