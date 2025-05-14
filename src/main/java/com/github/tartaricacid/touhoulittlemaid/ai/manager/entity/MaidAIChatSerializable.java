package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class MaidAIChatSerializable {
    public String llmSite = "";
    public String llmModel = "";

    public String ttsSite = "";
    public String ttsModel = "";
    public String ttsLanguage = "";

    public String ownerName = "";
    public String customSetting = "";

    public void decode(FriendlyByteBuf buf) {
        llmSite = buf.readUtf();
        llmModel = buf.readUtf();
        ttsSite = buf.readUtf();
        ttsModel = buf.readUtf();
        ttsLanguage = buf.readUtf();
        ownerName = buf.readUtf();
        customSetting = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(llmSite);
        buf.writeUtf(llmModel);
        buf.writeUtf(ttsSite);
        buf.writeUtf(ttsModel);
        buf.writeUtf(ttsLanguage);
        buf.writeUtf(ownerName);
        buf.writeUtf(customSetting);
    }

    public void copyFrom(MaidAIChatSerializable data) {
        llmSite = data.llmSite;
        llmModel = data.llmModel;
        ttsSite = data.ttsSite;
        ttsModel = data.ttsModel;
        ttsLanguage = data.ttsLanguage;
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
            data.putString("OwnerName", ownerName);
            data.putString("CustomSetting", customSetting);
        }
        tag.put("MaidAIChat", data);
        return tag;
    }
}
