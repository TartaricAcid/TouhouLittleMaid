package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Pattern;

public class StringParameter extends Parameter {
    @SerializedName("type")
    private String type = "string";

    @SerializedName("minLength")
    private Integer minLength = null;

    @SerializedName("maxLength")
    private Integer maxLength = null;

    @SerializedName("pattern")
    private String pattern = null;

    private StringParameter() {
    }

    public static StringParameter create() {
        return new StringParameter();
    }

    public StringParameter setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public StringParameter setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public StringParameter setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public StringParameter setPattern(Pattern pattern) {
        this.pattern = pattern.pattern();
        return this;
    }

    public StringParameter setRange(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public StringParameter setTitle(String title) {
        return (StringParameter) super.setTitle(title);
    }

    @Override
    public StringParameter setDescription(String description) {
        return (StringParameter) super.setDescription(description);
    }

    @Override
    public StringParameter setDefaultValue(String defaultValue) {
        return (StringParameter) super.setDefaultValue(defaultValue);
    }

    @Override
    public StringParameter addEnumValues(String... enumValues) {
        return (StringParameter) super.addEnumValues(enumValues);
    }
}
