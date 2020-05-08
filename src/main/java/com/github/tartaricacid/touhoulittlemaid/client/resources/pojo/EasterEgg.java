package com.github.tartaricacid.touhoulittlemaid.client.resources.pojo;

import com.google.gson.annotations.SerializedName;

public class EasterEgg {
    @SerializedName("encrypt")
    private boolean encrypt = false;

    @SerializedName("tag")
    private String tag = "";

    public boolean isEncrypt() {
        return encrypt;
    }

    public String getTag() {
        return tag;
    }
}
