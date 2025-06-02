package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

public class ArrayParameter extends Parameter {
    @SerializedName("type")
    private String type = "array";

    @SerializedName("items")
    private Parameter items = null;

    @SerializedName("minItems")
    private Integer minItems = null;

    @SerializedName("maxItems")
    private Integer maxItems = null;

    @SerializedName("uniqueItems")
    private boolean uniqueItems = false;

    private ArrayParameter() {
    }

    public static ArrayParameter create() {
        return new ArrayParameter();
    }

    public ArrayParameter setItems(Parameter items) {
        this.items = items;
        return this;
    }

    public ArrayParameter setRange(int minItems, int maxItems) {
        this.minItems = minItems;
        this.maxItems = maxItems;
        return this;
    }

    public ArrayParameter setMinItems(int minItems) {
        this.minItems = minItems;
        return this;
    }

    public ArrayParameter setMaxItems(int maxItems) {
        this.maxItems = maxItems;
        return this;
    }

    public ArrayParameter setUniqueItems(boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
        return this;
    }

    @Override
    public ArrayParameter setTitle(String title) {
        return (ArrayParameter) super.setTitle(title);
    }

    @Override
    public ArrayParameter setDescription(String description) {
        return (ArrayParameter) super.setDescription(description);
    }

    @Override
    public ArrayParameter setDefaultValue(String defaultValue) {
        return (ArrayParameter) super.setDefaultValue(defaultValue);
    }

    @Override
    public ArrayParameter addEnumValues(String... enumValues) {
        return (ArrayParameter) super.addEnumValues(enumValues);
    }
}
