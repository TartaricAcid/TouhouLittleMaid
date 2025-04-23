package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request;

import com.google.gson.annotations.SerializedName;

public class Prosody {
    @SerializedName("number")
    private double number = 1;

    @SerializedName("volume")
    private double volume = 0;

    public Prosody setNumber(double number) {
        this.number = number;
        return this;
    }

    public Prosody setVolume(double volume) {
        this.volume = volume;
        return this;
    }
}
