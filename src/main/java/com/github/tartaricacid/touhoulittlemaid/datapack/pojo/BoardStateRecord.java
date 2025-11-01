package com.github.tartaricacid.touhoulittlemaid.datapack.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record BoardStateRecord(
        @SerializedName("tags") List<String> tags,
        @SerializedName("display") Display display,
        @SerializedName("data") String data,
        @SerializedName("weight") int weight) {

    public record Display(
            @SerializedName("description") String description,
            @SerializedName("author") String author) {
    }
}
