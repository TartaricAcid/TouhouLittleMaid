package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TTSGptSovitsRequest {
    @SerializedName("text")
    private String text;

    @SerializedName("text_lang")
    private String textLang;

    @SerializedName("ref_audio_path")
    private String refAudioPath;

    @SerializedName("prompt_lang")
    private String promptLang;

    @SerializedName("prompt_text")
    private String promptText;

    @SerializedName("aux_ref_audio_paths")
    private List<String> auxRefAudioPaths = Lists.newArrayList();

    @SerializedName("text_split_method")
    private String textSplitMethod;

    @SerializedName("media_type")
    private String mediaType = "ogg";

    @SerializedName("streaming_mode")
    private boolean streamingMode = true;

    public static TTSGptSovitsRequest create() {
        return new TTSGptSovitsRequest();
    }

    private TTSGptSovitsRequest() {
    }

    public TTSGptSovitsRequest setText(String text) {
        this.text = text;
        return this;
    }

    public TTSGptSovitsRequest setTextLang(String textLang) {
        this.textLang = textLang;
        return this;
    }

    public TTSGptSovitsRequest setRefAudioPath(String refAudioPath) {
        this.refAudioPath = refAudioPath;
        return this;
    }

    public TTSGptSovitsRequest setPromptLang(String promptLang) {
        this.promptLang = promptLang;
        return this;
    }

    public TTSGptSovitsRequest setPromptText(String promptText) {
        this.promptText = promptText;
        return this;
    }

    public TTSGptSovitsRequest setAuxRefAudioPaths(List<String> auxRefAudioPaths) {
        this.auxRefAudioPaths = auxRefAudioPaths;
        return this;
    }

    public TTSGptSovitsRequest setTextSplitMethod(String textSplitMethod) {
        this.textSplitMethod = textSplitMethod;
        return this;
    }
}
