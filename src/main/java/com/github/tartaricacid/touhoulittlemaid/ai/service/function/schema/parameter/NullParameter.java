package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

public class NullParameter extends Parameter {
    @SerializedName("type")
    private String type = "null";

    private NullParameter() {
    }

    public static NullParameter create() {
        return new NullParameter();
    }
}