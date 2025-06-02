package com.github.tartaricacid.touhoulittlemaid.ai.manager.response;

import org.apache.commons.lang3.StringUtils;

public class ResponseChat {
    public String chatText;
    public String ttsText;

    public ResponseChat(String input) {
        String[] split = input.trim().split("---", 2);
        this.chatText = split[0].trim();
        this.ttsText = split.length > 1 ? split[1].trim() : this.chatText;
        if (StringUtils.isBlank(ttsText)) {
            this.ttsText = this.chatText;
        }
    }

    public ResponseChat(String chatText, String ttsText) {
        this.chatText = chatText;
        this.ttsText = ttsText;
    }

    public String getChatText() {
        return chatText;
    }

    public String getTtsText() {
        return ttsText;
    }

    @Override
    public String toString() {
        return "%s---%s".formatted(chatText, ttsText);
    }
}
