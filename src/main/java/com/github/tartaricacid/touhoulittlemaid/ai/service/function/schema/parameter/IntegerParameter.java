package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

public class IntegerParameter extends Parameter {
    @SerializedName("type")
    private String type = "integer";

    @SerializedName("minimum")
    private Integer minimum = null;

    @SerializedName("exclusiveMinimum")
    private Integer exclusiveMinimum = null;

    @SerializedName("maximum")
    private Integer maximum = null;

    @SerializedName("exclusiveMaximum")
    private Integer exclusiveMaximum = null;

    private IntegerParameter() {
    }

    public static IntegerParameter create() {
        return new IntegerParameter();
    }

    public IntegerParameter setMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public IntegerParameter setExclusiveMinimum(int exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }

    public IntegerParameter setMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }

    public IntegerParameter setExclusiveMaximum(int exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    @Override
    public IntegerParameter setTitle(String title) {
        return (IntegerParameter) super.setTitle(title);
    }

    @Override
    public IntegerParameter setDescription(String description) {
        return (IntegerParameter) super.setDescription(description);
    }

    @Override
    public IntegerParameter setDefaultValue(String defaultValue) {
        return (IntegerParameter) super.setDefaultValue(defaultValue);
    }

    @Override
    public IntegerParameter addEnumValues(String... enumValues) {
        return (IntegerParameter) super.addEnumValues(enumValues);
    }
}
