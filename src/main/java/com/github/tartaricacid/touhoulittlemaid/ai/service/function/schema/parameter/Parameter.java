package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Parameter {
    @SerializedName("title")
    private String title = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("default")
    private String defaultValue = null;

    @SerializedName("enum")
    private List<String> enumValues = null;

    public Parameter setTitle(String title) {
        this.title = title;
        return this;
    }

    public Parameter setDescription(String description) {
        this.description = description;
        return this;
    }

    public Parameter setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Parameter addEnumValues(String... enumValues) {
        if (this.enumValues == null) {
            this.enumValues = new ArrayList<>(Arrays.asList(enumValues));
        } else {
            this.enumValues.addAll(Arrays.asList(enumValues));
        }
        return this;
    }
}
