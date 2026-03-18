package com.github.tartaricacid.touhoulittlemaid.ai.agent.context;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

/**
 * 女仆 AI 上下文项。
 * <p>
 * 用于描述一个可被设定模板替换、也可被 `maid_context` skill 展示的上下文值。
 */
public interface IMaidContext {
    /**
     * 上下文唯一键。
     */
    String key();

    /**
     * 上下文展示标签。
     */
    String label();

    /**
     * 获取当前上下文值。
     *
     * @param maid 当前女仆
     */
    String getValue(EntityMaid maid);
}
