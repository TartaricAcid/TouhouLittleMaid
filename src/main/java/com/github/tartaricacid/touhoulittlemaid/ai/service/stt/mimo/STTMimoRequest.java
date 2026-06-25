package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.mimo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.List;

public class STTMimoRequest {
    private static final String WAV_DATA_URL_PREFIX = "data:audio/wav;base64,";

    @SerializedName("model")
    private String model = STTMimoSite.DEFAULT_MODEL;

    @SerializedName("messages")
    private List<Message> messages = Lists.newArrayList();

    @SerializedName("asr_options")
    private AsrOptions asrOptions = new AsrOptions(STTMimoSite.DEFAULT_LANGUAGE);

    public static STTMimoRequest create(String model, String language, byte[] wavData) {
        STTMimoRequest request = new STTMimoRequest();
        request.model = StringUtils.defaultIfBlank(model, STTMimoSite.DEFAULT_MODEL);
        request.asrOptions = new AsrOptions(StringUtils.defaultIfBlank(language, STTMimoSite.DEFAULT_LANGUAGE));

        String data = WAV_DATA_URL_PREFIX + Base64.getEncoder().encodeToString(wavData);
        InputAudio inputAudio = new InputAudio(data);
        request.messages.add(new Message("user", List.of(new InputAudioContent("input_audio", inputAudio))));
        return request;
    }

    private record Message(String role, List<InputAudioContent> content) {
    }

    private record InputAudioContent(String type, @SerializedName("input_audio") InputAudio inputAudio) {
    }

    private record InputAudio(String data) {
    }

    private record AsrOptions(String language) {
    }
}
