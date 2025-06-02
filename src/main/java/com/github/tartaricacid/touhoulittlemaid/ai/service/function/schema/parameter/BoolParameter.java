package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

public class BoolParameter extends Parameter {
    @SerializedName("type")
    private String type = "boolean";

    private BoolParameter() {
    }

    public static BoolParameter create() {
        return new BoolParameter();
    }
}
