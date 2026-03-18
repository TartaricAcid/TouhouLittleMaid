package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.List;

/**
 * 女仆 AI 的 Skill 抽象。
 * <p>
 * Skill 用于向模型暴露一组较高层级的能力包，通常包含：
 * 技能标识、触发描述、技能正文，以及该技能加载后可用的 Tool 列表。
 * Root skill 只负责路由到其它 skill；普通 skill 则负责提供上下文、能力说明或工具集合。
 */
public interface ISkill {
    /**
     * Skill 的唯一标识符，不能和其他 Skill 重复。
     * <p>
     * ID 建议仅使用小写英文字母、数字和下划线，且尽量语义化，便于模型调用和调试。
     */
    String id();

    /**
     * 返回对该 Skill 的简短摘要，用于模型在选择 skill 时理解用途。
     *
     * @param maid 当前女仆实例
     */
    String summary(EntityMaid maid);

    /**
     * 返回该 Skill 的正文内容。
     * <p>
     * 正文会在 skill 被加载后提供给模型，用于补充上下文、规则或操作说明。
     *
     * @param maid 当前女仆实例
     */
    String body(EntityMaid maid);

    /**
     * 返回该 Skill 加载后允许使用的 Tool 标识列表。
     *
     * @param maid 当前女仆实例
     */
    List<String> tools(EntityMaid maid);

    /**
     * 程序侧再次判断当前 Skill 是否允许在当前女仆状态下暴露给模型。
     * 一般不需要重写此方法。
     *
     * @param maid 当前女仆实例
     */
    default boolean trigger(EntityMaid maid) {
        return true;
    }
}
