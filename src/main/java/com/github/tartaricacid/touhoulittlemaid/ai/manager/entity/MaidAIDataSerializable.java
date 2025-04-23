package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class MaidAIDataSerializable {
    protected String chatSiteName = "";
    protected String chatModel = "";
    protected double chatTemperature = -1;
    protected String ttsSiteName = "";
    protected String ttsModel = "";
    protected String ttsLanguage = "";
    protected String ownerName = "";
    protected String customSetting = "";

    public void decode(FriendlyByteBuf buf) {
        chatSiteName = buf.readUtf();
        chatModel = buf.readUtf();
        chatTemperature = buf.readDouble();
        ttsSiteName = buf.readUtf();
        ttsModel = buf.readUtf();
        ttsLanguage = buf.readUtf();
        ownerName = buf.readUtf();
        customSetting = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(chatSiteName);
        buf.writeUtf(chatModel);
        buf.writeDouble(chatTemperature);
        buf.writeUtf(ttsSiteName);
        buf.writeUtf(ttsModel);
        buf.writeUtf(ttsLanguage);
        buf.writeUtf(ownerName);
        buf.writeUtf(customSetting);
    }

    public void copyFrom(MaidAIDataSerializable data) {
        chatSiteName = data.chatSiteName;
        chatModel = data.chatModel;
        chatTemperature = data.chatTemperature;
        ttsSiteName = data.ttsSiteName;
        ttsModel = data.ttsModel;
        ttsLanguage = data.ttsLanguage;
        ownerName = data.ownerName;
        customSetting = data.customSetting;
    }

    public void readFromTag(CompoundTag tag) {
        if (tag.contains("MaidAIChatData")) {
            CompoundTag data = tag.getCompound("MaidAIChatData");
            chatSiteName = data.getString("ChatSiteName");
            chatModel = data.getString("ChatModel");
            chatTemperature = data.getDouble("ChatTemperature");
            ttsSiteName = data.getString("TtsSiteName");
            ttsModel = data.getString("TtsModel");
            ttsLanguage = data.getString("TtsLanguage");
            ownerName = data.getString("OwnerName");
            customSetting = data.getString("CustomSetting");
        }
    }

    public void writeToTag(CompoundTag tag) {
        CompoundTag data = new CompoundTag();
        {
            data.putString("ChatSiteName", chatSiteName);
            data.putString("ChatModel", chatModel);
            data.putDouble("ChatTemperature", chatTemperature);
            data.putString("TtsSiteName", ttsSiteName);
            data.putString("TtsModel", ttsModel);
            data.putString("TtsLanguage", ttsLanguage);
            data.putString("OwnerName", ownerName);
            data.putString("CustomSetting", customSetting);
        }
        tag.put("MaidAIChatData", data);
    }

    public String getChatSiteName() {
        return chatSiteName;
    }

    public void setChatSiteName(String chatSiteName) {
        this.chatSiteName = chatSiteName;
    }

    public String getChatModel() {
        return chatModel;
    }

    public void setChatModel(String chatModel) {
        this.chatModel = chatModel;
    }

    public double getChatTemperature() {
        return chatTemperature;
    }

    public void setChatTemperature(double chatTemperature) {
        this.chatTemperature = chatTemperature;
    }

    public String getTtsSiteName() {
        return ttsSiteName;
    }

    public void setTtsSiteName(String ttsSiteName) {
        this.ttsSiteName = ttsSiteName;
    }

    public String getTtsModel() {
        return ttsModel;
    }

    public void setTtsModel(String ttsModel) {
        this.ttsModel = ttsModel;
    }

    public String getTtsLanguage() {
        return ttsLanguage;
    }

    public void setTtsLanguage(String ttsLanguage) {
        this.ttsLanguage = ttsLanguage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCustomSetting() {
        return customSetting;
    }

    public void setCustomSetting(String customSetting) {
        this.customSetting = customSetting;
    }
}
