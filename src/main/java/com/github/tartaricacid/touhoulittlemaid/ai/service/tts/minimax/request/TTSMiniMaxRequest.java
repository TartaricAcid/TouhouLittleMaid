package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.minimax.request;

import com.google.gson.annotations.SerializedName;

public class TTSMiniMaxRequest {
    @SerializedName("model")
    private String siteModel = "";

    @SerializedName("text")
    private String text = "";

    @SerializedName("voice_setting")
    private VoiceSetting voiceSetting = null;

    public static TTSMiniMaxRequest create() {
        return new TTSMiniMaxRequest();
    }

    private TTSMiniMaxRequest() {
    }

    public TTSMiniMaxRequest setText(String text) {
        this.text = text;
        return this;
    }

    public TTSMiniMaxRequest setSiteModel(String siteModel) {
        this.siteModel = siteModel;
        return this;
    }

    public TTSMiniMaxRequest setVoiceId(String voiceId) {
        this.voiceSetting = new VoiceSetting(voiceId);
        return this;
    }
}
