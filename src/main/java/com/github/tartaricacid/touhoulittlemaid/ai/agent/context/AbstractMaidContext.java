package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

/**
 * 提供 key 与 label 的基础上下文实现。
 */
public abstract class AbstractMaidContext implements IMaidContext {
    private final String key;
    private final String label;

    protected AbstractMaidContext(String key, String label) {
        this.key = key;
        this.label = label;
    }

    @Override
    public final String key() {
        return key;
    }

    @Override
    public final String label() {
        return label;
    }
}
