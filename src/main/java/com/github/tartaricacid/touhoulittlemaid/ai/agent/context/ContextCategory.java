package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 女仆上下文分类。
 */
public final class ContextCategory {
    private final String id;
    private final String summary;
    private final Set<String> contextKeys = Sets.newLinkedHashSet();

    public ContextCategory(String id, String summary) {
        this.id = id;
        this.summary = summary;
    }

    public String id() {
        return id;
    }

    public String summary() {
        return summary;
    }

    boolean hasContexts() {
        return !contextKeys.isEmpty();
    }

    void addContextKey(String key) {
        contextKeys.add(key);
    }

    Set<String> contextKeys() {
        return contextKeys;
    }
}
