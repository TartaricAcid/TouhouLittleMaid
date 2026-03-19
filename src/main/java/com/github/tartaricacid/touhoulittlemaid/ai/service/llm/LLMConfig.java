package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import org.jetbrains.annotations.Nullable;

/**
 * 大语言模型配置类
 * 用于存储大语言模型的配置参数
 *
 * @param model       模型名称
 * @param temperature 温度，控制生成文本的随机性
 */
public record LLMConfig(String model, double temperature, EntityMaid maid,
                        ChatType chatType, @Nullable SkillContext skillContext) {
    public LLMConfig(String model, double temperature, EntityMaid maid, ChatType chatType) {
        this(model, temperature, maid, chatType, null);
    }

    public LLMConfig(String model, EntityMaid maid, ChatType chatType, SkillContext context) {
        this(model, AIConfig.LLM_TEMPERATURE.get(), maid, chatType, context);
    }

    public static LLMConfig normalChat(String model, EntityMaid maid) {
        return new LLMConfig(model, AIConfig.LLM_TEMPERATURE.get(), maid, ChatType.NORMAL_CHAT);
    }

    public LLMConfig(String model, EntityMaid maid, ChatType chatType) {
        this(model, AIConfig.LLM_TEMPERATURE.get(), maid, chatType);
    }

    /**
     * Skill 的上下文，主要记录了本次对话将要使用的 Skill ID
     */
    public record SkillContext(String skillId) {
    }

    /**
     * 仅用于知识库的参数
     *
     * @param question          玩家聊天记录
     * @param resolvedKnowledge 知识库的内容
     */
    public record GroundedAnswerContext(String question, String resolvedKnowledge) {
    }
}
