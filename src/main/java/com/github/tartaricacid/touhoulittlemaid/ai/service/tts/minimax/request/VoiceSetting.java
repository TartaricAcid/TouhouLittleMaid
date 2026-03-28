package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax.request;

import com.google.gson.annotations.SerializedName;

public class VoiceSetting {
    @SerializedName("voice_id")
    private String voiceId = "";

    public VoiceSetting(String voiceId) {
        this.voiceId = voiceId;
    }
}
