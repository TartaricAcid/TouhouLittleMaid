package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.request;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSRequest;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TTSGptSovitsRequest implements TTSRequest {
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

    public TTSGptSovitsRequest setSiteExtraArgs(Site site) {
        Map<String, Object> extraArgs = site.getExtraArgs();
        if (extraArgs.containsKey("ref_audio_path")) {
            this.refAudioPath = (String) extraArgs.get("ref_audio_path");
        }
        if (extraArgs.containsKey("prompt_lang")) {
            this.promptLang = (String) extraArgs.get("prompt_lang");
        }
        if (extraArgs.containsKey("prompt_text")) {
            this.promptText = (String) extraArgs.get("prompt_text");
        }
        if (extraArgs.containsKey("text_split_method")) {
            this.textSplitMethod = (String) extraArgs.get("text_split_method");
        }
        if (extraArgs.containsKey("aux_ref_audio_paths")) {
            this.auxRefAudioPaths = Objects.requireNonNullElse((List<String>) site.getExtraArgs().get("aux_ref_audio_paths"), Lists.newArrayList());
        }
        return this;
    }

    public TTSGptSovitsRequest setText(String text) {
        this.text = text;
        return this;
    }

    public TTSGptSovitsRequest setTextLang(String textLang) {
        this.textLang = textLang;
        return this;
    }
}
