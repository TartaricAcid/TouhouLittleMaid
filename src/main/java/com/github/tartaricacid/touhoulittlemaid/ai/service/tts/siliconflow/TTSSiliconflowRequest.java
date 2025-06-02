package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.siliconflow;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.Format;
import com.google.gson.annotations.SerializedName;

public class TTSSiliconflowRequest {
    @SerializedName("model")
    private String model;

    @SerializedName("input")
    private String input;

    @SerializedName("voice")
    private String voice;

    @SerializedName("response_format")
    private String responseFormat = Format.OPUS.getId();

    @SerializedName("sample_rate")
    private int sampleRate = 48000;

    @SerializedName("stream")
    private boolean stream = false;

    @SerializedName("speed")
    private int speed = 1;

    @SerializedName("gain")
    private int gain = 0;

    public static TTSSiliconflowRequest create() {
        return new TTSSiliconflowRequest();
    }

    private TTSSiliconflowRequest() {
    }

    public TTSSiliconflowRequest setModel(String model) {
        this.model = model;
        return this;
    }

    public TTSSiliconflowRequest setInput(String input) {
        this.input = input;
        return this;
    }

    public TTSSiliconflowRequest setVoice(String voice) {
        this.voice = voice;
        return this;
    }
}
