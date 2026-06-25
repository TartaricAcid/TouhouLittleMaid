package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.mimo;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.Format;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class TTSMimoRequest {
    @SerializedName("model")
    private String model = TTSMimoSite.MODEL_TTS;

    @SerializedName("messages")
    private List<Message> messages = Lists.newArrayList();

    @SerializedName("audio")
    private Audio audio = new Audio(Format.WAV.getId(), null);

    public static TTSMimoRequest create(String siteModel, String voicePrompt, @Nullable String voiceCloneAudio,
                                        String message, String voice) {
        TTSMimoRequest request = new TTSMimoRequest();
        request.model = siteModel;
        boolean voiceClone = TTSMimoSite.isVoiceCloneModel(siteModel);
        if (StringUtils.isNotBlank(voicePrompt) || voiceClone) {
            request.messages.add(new Message("user", StringUtils.defaultString(voicePrompt)));
        }
        request.messages.add(new Message("assistant", message));
        if (TTSMimoSite.isVoiceDesignModel(siteModel)) {
            request.audio = new Audio(Format.WAV.getId(), null);
        } else if (voiceClone) {
            request.audio = new Audio(Format.WAV.getId(), voiceCloneAudio);
        } else {
            request.audio = new Audio(Format.WAV.getId(), voice);
        }
        return request;
    }

    private record Message(String role, Object content) {
    }

    private record Audio(String format, @Nullable String voice) {
    }
}
