package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 女仆上下文分类。
 */
public final class ContextCategory {
    private final String id;
    private final String summary;
    private final boolean promptContext;
    private final Set<String> contextKeys = Sets.newLinkedHashSet();

    public ContextCategory(String id, String summary, boolean promptContext) {
        this.id = id;
        this.summary = summary;
        this.promptContext = promptContext;
    }

    public String id() {
        return id;
    }

    public String summary() {
        return summary;
    }

    /**
     * 如果此属性为 true，则此上下文会在 user 每次对话时，注入 user 消息的开头，作为对话上下文的一部分提供给模型。
     * 适用于对话中经常需要但又不太占 token 的上下文。
     * <p>
     * 注意：如果一个分类被标记为 prompt context，那么它将不存在于 query_game_context 工具中
     */
    public boolean isPromptContext() {
        return promptContext;
    }

    void addContextKey(String key) {
        contextKeys.add(key);
    }

    Set<String> contextKeys() {
        return contextKeys;
    }
}
