package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.player2;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TTSPlayer2Request {
    @SerializedName("play_in_app")
    private boolean playInApp = true;

    @SerializedName("speed")
    private float speed = 1.0f;

    @SerializedName("text")
    private String text;

    @SerializedName("voice_ids")
    private List<String> voiceIds = Lists.newArrayList();

    public static TTSPlayer2Request create() {
        return new TTSPlayer2Request();
    }

    private TTSPlayer2Request() {
    }

    public TTSPlayer2Request setText(String text) {
        this.text = text;
        return this;
    }

    public TTSPlayer2Request setVoiceId(String id) {
        voiceIds.add(id);
        return this;
    }
}
