package com.github.tartaricacid.touhoulittlemaid.config.pojo;

import com.google.gson.annotations.SerializedName;

public class Out {
    @SerializedName("nbt")
    private String nbt;

    @SerializedName("meta")
    private int meta;

    @SerializedName("count")
    private Count count;

    @SerializedName("id")
    private String id;

    public String getNbt() {
        return nbt;
    }

    public int getMeta() {
        return meta;
    }

    public Count getCount() {
        return count;
    }

    public String getId() {
        return id;
    }
}