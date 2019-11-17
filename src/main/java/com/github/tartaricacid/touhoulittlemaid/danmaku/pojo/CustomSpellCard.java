package com.github.tartaricacid.touhoulittlemaid.danmaku.pojo;

import com.google.gson.annotations.SerializedName;


public class CustomSpellCard {
    @SerializedName("danmaku")
    private Danmaku danmaku;

    @SerializedName("info")
    private Info info;

    public Danmaku getDanmaku() {
        return danmaku;
    }

    public Info getInfo() {
        return info;
    }
}