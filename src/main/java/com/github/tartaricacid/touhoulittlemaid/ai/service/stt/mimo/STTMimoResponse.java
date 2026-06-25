package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.mimo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class STTMimoResponse {
    private List<Choice> choices;

    public String text() {
        if (choices == null || choices.isEmpty()) {
            return StringUtils.EMPTY;
        }
        Choice choice = choices.get(0);
        if (choice == null || choice.message == null || choice.message.content == null) {
            return StringUtils.EMPTY;
        }
        return extractText(choice.message.content);
    }

    private static String extractText(JsonElement content) {
        if (content == null || content.isJsonNull()) {
            return StringUtils.EMPTY;
        }
        if (content.isJsonPrimitive()) {
            return content.getAsString();
        }
        if (content.isJsonArray()) {
            StringBuilder builder = new StringBuilder();
            for (JsonElement element : content.getAsJsonArray()) {
                String text = extractContentItemText(element);
                if (StringUtils.isNotBlank(text)) {
                    builder.append(text);
                }
            }
            return builder.toString();
        }
        return extractContentItemText(content);
    }

    private static String extractContentItemText(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return StringUtils.EMPTY;
        }
        JsonObject object = element.getAsJsonObject();
        JsonElement text = object.get("text");
        if (text != null && text.isJsonPrimitive()) {
            return text.getAsString();
        }
        JsonElement content = object.get("content");
        if (content != null && content.isJsonPrimitive()) {
            return content.getAsString();
        }
        return StringUtils.EMPTY;
    }

    private static class Choice {
        private Message message;
    }

    private static class Message {
        private JsonElement content;
    }
}
