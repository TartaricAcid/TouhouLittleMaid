package com.github.tartaricacid.touhoulittlemaid.ai.service;

import java.util.Map;

public interface SupportModelSelect {
    Map<String, String> models();

    default void addModel(String id, String name) {
        models().put(id, name);
    }

    default void removeModel(String id) {
        models().remove(id);
    }

    default String getDefaultModel() {
        return models().keySet().iterator().next();
    }

    default String getModel(String id) {
        return models().getOrDefault(id, getDefaultModel());
    }

    default String getModelName(String id) {
        return models().get(id);
    }
}
