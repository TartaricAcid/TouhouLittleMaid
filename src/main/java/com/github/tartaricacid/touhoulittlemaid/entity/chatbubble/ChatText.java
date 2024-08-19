package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.isValidResourceLocation;

public final class ChatText {

    public static final ResourceLocation EMPTY_ICON_PATH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "empty");
    public static final ChatText EMPTY_CHAT_TEXT = new ChatText(ChatTextType.EMPTY, EMPTY_ICON_PATH, StringUtils.EMPTY);
    public static final StreamCodec<ByteBuf, ChatText> STREAM_CODEC = StreamCodec.composite(
            ChatTextType.STREAM_CODEC,
            ChatText::getType,
            ResourceLocation.STREAM_CODEC,
            ChatText::getIconPath,
            ByteBufCodecs.STRING_UTF8,
            ChatText::getText,
            ChatText::new
    );
    private static final String ICON_IDENTIFIER_CHAR = "%";

    private final ChatTextType type;
    private final ResourceLocation iconPath;
    private final String text;

    public ChatText(ChatTextType type, ResourceLocation iconPath, String text) {
        this.type = type;
        this.iconPath = iconPath;
        this.text = text;
    }

    public static void toBuff(ChatText chatText, FriendlyByteBuf buf) {
        buf.writeEnum(chatText.type);
        buf.writeResourceLocation(chatText.iconPath);
        buf.writeUtf(chatText.text);
    }

    public static ChatText fromBuff(FriendlyByteBuf buf) {
        ChatTextType type = buf.readEnum(ChatTextType.class);
        ResourceLocation iconPath = buf.readResourceLocation();
        String text = buf.readUtf();
        return new ChatText(type, iconPath, text);
    }

    public boolean isText() {
        return type == ChatTextType.TEXT;
    }

    public boolean isIcon() {
        return type == ChatTextType.ICON;
    }

    public ResourceLocation getIconPath() {
        return iconPath;
    }

    public String getText() {
        return text;
    }

    public ChatTextType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof ChatText chatText)) {
            return false;
        } else {
            return type.equals(chatText.type) && iconPath.equals(chatText.iconPath) && text.equals(chatText.text);
        }
    }

    public static class Serializer implements JsonDeserializer<ChatText> {
        @Override
        public ChatText deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String text = GsonHelper.convertToString(json, "chat_text");
            if (StringUtils.isEmpty(text)) {
                return EMPTY_CHAT_TEXT;
            }

            if (text.startsWith(ICON_IDENTIFIER_CHAR) && text.endsWith(ICON_IDENTIFIER_CHAR)) {
                String substring = text.substring(1, text.length() - 1);
                if (isValidResourceLocation(substring)) {
                    return new ChatText(ChatTextType.ICON, ResourceLocation.parse(substring), StringUtils.EMPTY);
                }
                return EMPTY_CHAT_TEXT;
            }

            return new ChatText(ChatTextType.TEXT, EMPTY_ICON_PATH, text);
        }
    }
}