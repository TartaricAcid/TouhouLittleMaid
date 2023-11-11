package com.github.tartaricacid.touhoulittlemaid.client.resource.pojo;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatText;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger.DEFAULT_CHAT_BUBBLE;

public class ChatBubbleInfo {
    @SerializedName("bg")
    private ResourceLocation bg = null;

    @SerializedName("text")
    private Text text = null;

    public ResourceLocation getBg() {
        return bg;
    }

    public Text getText() {
        return text;
    }

    public ChatBubbleInfo decorate() {
        if (bg == null) {
            bg = DEFAULT_CHAT_BUBBLE.bg;
        }
        if (text == null) {
            text = DEFAULT_CHAT_BUBBLE.text;
        } else if (!text.isReplace()) {
            if (text.main.isEmpty()) {
                text.main = DEFAULT_CHAT_BUBBLE.text.main;
            } else {
                mergeChatText(text.main, DEFAULT_CHAT_BUBBLE.text.main);
            }
            if (text.special.isEmpty()) {
                text.special = DEFAULT_CHAT_BUBBLE.text.special;
            } else {
                mergeChatText(text.special, DEFAULT_CHAT_BUBBLE.text.special);
            }
            if (text.other.isEmpty()) {
                text.other = DEFAULT_CHAT_BUBBLE.text.other;
            } else {
                mergeChatText(text.other, DEFAULT_CHAT_BUBBLE.text.other);
            }
        }
        return this;
    }

    private void mergeChatText(HashMap<String, List<ChatText>> textTo, HashMap<String, List<ChatText>> textFrom) {
        for (String key : textFrom.keySet()) {
            List<ChatText> chatTexts = textFrom.get(key);
            if (textTo.containsKey(key)) {
                textTo.get(key).addAll(chatTexts);
            } else {
                textTo.put(key, chatTexts);
            }
        }
    }

    public static class Text {
        @SerializedName("replace")
        private boolean replace = false;

        @SerializedName("main")
        private HashMap<String, List<ChatText>> main = Maps.newHashMap();

        @SerializedName("special")
        private HashMap<String, List<ChatText>> special = Maps.newHashMap();

        @SerializedName("other")
        private HashMap<String, List<ChatText>> other = Maps.newHashMap();

        public boolean isReplace() {
            return replace;
        }

        public HashMap<String, List<ChatText>> getMain() {
            return main;
        }

        public HashMap<String, List<ChatText>> getSpecial() {
            return special;
        }

        public HashMap<String, List<ChatText>> getOther() {
            return other;
        }
    }
}
