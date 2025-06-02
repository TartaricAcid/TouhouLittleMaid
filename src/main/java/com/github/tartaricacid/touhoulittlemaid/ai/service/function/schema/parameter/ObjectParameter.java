package com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ObjectParameter extends Parameter {
    @SerializedName("type")
    private String type = "object";

    @SerializedName("properties")
    private Map<String, Parameter> properties = Maps.newHashMap();

    @SerializedName("required")
    private List<String> required = Lists.newArrayList();

    private ObjectParameter() {
    }

    public static ObjectParameter create() {
        return new ObjectParameter();
    }

    public ObjectParameter addProperties(String name, Parameter parameter, boolean required) {
        this.properties.put(name, parameter);
        if (required) {
            this.required.add(name);
        }
        return this;
    }

    public ObjectParameter addProperties(String name, Parameter parameter) {
        return addProperties(name, parameter, true);
    }

    public ObjectParameter addRequired(String name) {
        this.required.add(name);
        return this;
    }

    @Override
    public ObjectParameter setTitle(String title) {
        return (ObjectParameter) super.setTitle(title);
    }

    @Override
    public ObjectParameter setDescription(String description) {
        return (ObjectParameter) super.setDescription(description);
    }

    @Override
    public ObjectParameter setDefaultValue(String defaultValue) {
        return (ObjectParameter) super.setDefaultValue(defaultValue);
    }

    @Override
    public ObjectParameter addEnumValues(String... enumValues) {
        return (ObjectParameter) super.addEnumValues(enumValues);
    }
}
