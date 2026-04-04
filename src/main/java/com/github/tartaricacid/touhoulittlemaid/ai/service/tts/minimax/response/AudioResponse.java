package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax.response;

import com.google.gson.annotations.SerializedName;

public record AudioResponse(
        @SerializedName("data") Data data,
        @SerializedName("base_resp") BaseResponse response
) {
    public record Data(
            @SerializedName("audio") String audio
    ) {
    }

    public record BaseResponse(
            @SerializedName("status_code") int statusCode,
            @SerializedName("status_msg") String statusMsg
    ) {
    }
}
