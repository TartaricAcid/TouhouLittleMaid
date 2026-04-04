package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import javax.annotation.Nullable;
import java.util.Map;

/**
 *
 * @param name        Skill 的名称；最多 64 个字符，仅限小写字母、数字和连字符，不得以连字符开头或结尾
 * @param description 描述 Skill 的效果以及何时使用；最多 1024 个字符，非空
 * @param metadata    元数据，任意字符串键值映射
 * @param body        Skill 的主体内容，包含技能说明等，格式没有限制
 * @param references  参考资料，键为参考资料名称（带拓展名），值为对应文件的内容
 */
public record SkillInstance(
        String name,
        String description,
        @Nullable Map<String, String> metadata,
        String body,
        Map<String, String> references
) {
    public static final String TLM_TYPE = "tlm-type";
    public static final String KNOWLEDGE = "knowledge";

    /**
     * 是否是特殊的知识库类型 skill，此类型 skill 会专门开一个子对话来提取关键信息，返回给玩家
     */
    public boolean isKnowledgeType() {
        if (this.metadata == null || this.metadata.isEmpty() || !this.metadata.containsKey(TLM_TYPE)) {
            return false;
        }
        String type = this.metadata.get(TLM_TYPE);
        return KNOWLEDGE.equalsIgnoreCase(type);
    }
}
