package com.github.tartaricacid.touhoulittlemaid.ai.service.function.response;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.ChatType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMConfig.GroundedAnswerContext;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMConfig.SkillContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * 当本地代码执行完 Function Call 时，返回的对象
 */
public record ToolResponse(String message, @Nullable Continuation continuation) {
    /**
     * 普通的对话
     */
    public ToolResponse(String message) {
        this(message, null);
    }

    /**
     * 正常返回，并使用指定的 skillId 添加 tool 继续后续对话
     * <p>
     * 目前仅用于 use_skill 主路由，后续如果有其他需要继续调用 skill 的场景也可以使用这个方法
     */
    public static ToolResponse continueWithSkill(String message, String skillId) {
        return new ToolResponse(message, new Continuation(ChatType.MULTI_FUNCTION_CALL,
                new SkillContext(skillId), null));
    }

    /**
     * 返回特殊的知识库研读结果，让 LLM 构建一个单独的、完全洁净的上下文进行继续后续对话
     * <p>
     * 目前仅用于知识库对话
     */
    public static ToolResponse groundedAnswer(String message, String activeSkillId, String question, String resolvedKnowledge) {
        return new ToolResponse(message, new Continuation(ChatType.GROUNDED_ANSWER_PASS,
                new SkillContext(activeSkillId), new GroundedAnswerContext(question, resolvedKnowledge)));
    }

    /**
     * 告诉 LLM 本次 Function Call 的参数无效，并给出正确的参数选项，要求 LLM 重新生成调用
     *
     * @param reason  无效的原因描述
     * @param skillId LLM 应当重新调用的 skill ID，通常是当前 skill 的 ID
     */
    public static ToolResponse invalidParam(String parameterName, Collection<String> values,
                                            String reason, String skillId) {
        String joined = String.join(", ", values);
        String correctUsage = "%s: choose one of [%s]".formatted(parameterName, joined);
        String text = "Invalid tool parameters: %s, Correct parameters: %s".formatted(reason, correctUsage);
        return new ToolResponse(text, new Continuation(ChatType.MULTI_FUNCTION_CALL,
                new SkillContext(skillId), null));
    }

    public record Continuation(ChatType chatType, @Nullable SkillContext skillContext,
                               @Nullable GroundedAnswerContext groundedAnswerContext
    ) {
    }
}
