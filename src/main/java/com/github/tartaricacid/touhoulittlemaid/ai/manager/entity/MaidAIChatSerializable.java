package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class MaidAIChatSerializable {
    public static final String NO_TTS_SITE = "__none__";

    public String llmSite = "";
    public String llmModel = "";

    public String ttsSite = "";
    public String ttsModel = "";
    public String ttsLanguage = "";
    public String chatLanguage = "";

    public String ownerName = "";
    public String customSetting = "";

    /**
     * 哨兵值，如果为此值，说明此时对当前女仆禁用 TTS 功能
     */
    public static boolean isNoTTSSite(String siteId) {
        return NO_TTS_SITE.equals(siteId);
    }

    public void decode(FriendlyByteBuf buf) {
        llmSite = buf.readUtf();
        llmModel = buf.readUtf();
        ttsSite = buf.readUtf();
        ttsModel = buf.readUtf();
        ttsLanguage = buf.readUtf();
        chatLanguage = buf.readUtf();
        ownerName = buf.readUtf();
        customSetting = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(llmSite);
        buf.writeUtf(llmModel);
        buf.writeUtf(ttsSite);
        buf.writeUtf(ttsModel);
        buf.writeUtf(ttsLanguage);
        buf.writeUtf(chatLanguage);
        buf.writeUtf(ownerName);
        buf.writeUtf(customSetting);
    }

    public void copyFrom(MaidAIChatSerializable data) {
        llmSite = data.llmSite;
        llmModel = data.llmModel;
        ttsSite = data.ttsSite;
        ttsModel = data.ttsModel;
        ttsLanguage = data.ttsLanguage;
        chatLanguage = data.chatLanguage;
        ownerName = data.ownerName;
        customSetting = data.customSetting;
    }

    public CompoundTag readFromTag(CompoundTag tag) {
        if (tag.contains("MaidAIChat")) {
            CompoundTag data = tag.getCompound("MaidAIChat");
            llmSite = data.getString("LLMSite");
            llmModel = data.getString("LLMModel");
            ttsSite = data.getString("TTSSiteName");
            ttsModel = data.getString("TTSModel");
            ttsLanguage = data.getString("TTSLanguage");
            chatLanguage = data.getString("ChatLanguage");
            ownerName = data.getString("OwnerName");
            customSetting = data.getString("CustomSetting");
        }
        return tag;
    }

    public CompoundTag writeToTag(CompoundTag tag) {
        CompoundTag data = new CompoundTag();
        {
            data.putString("LLMSite", llmSite);
            data.putString("LLMModel", llmModel);
            data.putString("TTSSiteName", ttsSite);
            data.putString("TTSModel", ttsModel);
            data.putString("TTSLanguage", ttsLanguage);
            data.putString("ChatLanguage", chatLanguage);
            data.putString("OwnerName", ownerName);
            data.putString("CustomSetting", customSetting);
        }
        tag.put("MaidAIChat", data);
        return tag;
    }
}
