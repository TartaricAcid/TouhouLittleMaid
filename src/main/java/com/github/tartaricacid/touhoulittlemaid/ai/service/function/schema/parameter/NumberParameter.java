package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

public class NumberParameter extends Parameter {
    @SerializedName("type")
    private String type = "number";

    @SerializedName("minimum")
    private Double minimum = null;

    @SerializedName("exclusiveMinimum")
    private Double exclusiveMinimum = null;

    @SerializedName("maximum")
    private Double maximum = null;

    @SerializedName("exclusiveMaximum")
    private Double exclusiveMaximum = null;

    private NumberParameter() {
    }

    public static NumberParameter create() {
        return new NumberParameter();
    }

    public NumberParameter setMinimum(double minimum) {
        this.minimum = minimum;
        return this;
    }

    public NumberParameter setExclusiveMinimum(double exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }

    public NumberParameter setMaximum(double maximum) {
        this.maximum = maximum;
        return this;
    }

    public NumberParameter setExclusiveMaximum(double exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    @Override
    public NumberParameter setTitle(String title) {
        return (NumberParameter) super.setTitle(title);
    }

    @Override
    public NumberParameter setDescription(String description) {
        return (NumberParameter) super.setDescription(description);
    }

    @Override
    public NumberParameter setDefaultValue(String defaultValue) {
        return (NumberParameter) super.setDefaultValue(defaultValue);
    }

    @Override
    public NumberParameter addEnumValues(String... enumValues) {
        return (NumberParameter) super.addEnumValues(enumValues);
    }
}
