package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request;

import com.google.gson.annotations.SerializedName;

public class References {
    @SerializedName("audio")
    private byte[] audio;

    @SerializedName("text")
    private String text;

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    public void setText(String text) {
        this.text = text;
    }
}
