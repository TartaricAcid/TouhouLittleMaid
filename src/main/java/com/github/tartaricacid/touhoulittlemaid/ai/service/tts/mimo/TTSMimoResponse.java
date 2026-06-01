package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.mimo;

import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.List;

public class TTSMimoResponse {
    private List<Choice> choices;

    public String audioData() {
        if (choices == null || choices.isEmpty()) {
            return StringUtils.EMPTY;
        }
        Choice choice = choices.get(0);
        if (choice == null || choice.message == null || choice.message.audio == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.defaultString(choice.message.audio.data);
    }

    public byte[] decodeAudioData() {
        String data = audioData();
        if (StringUtils.isBlank(data)) {
            throw new IllegalStateException("Mimo response audio data is empty");
        }
        return Base64.getDecoder().decode(data);
    }

    private static class Choice {
        private Message message;
    }

    private static class Message {
        private Audio audio;
    }

    private static class Audio {
        private String data;
    }
}
