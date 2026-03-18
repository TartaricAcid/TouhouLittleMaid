package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;

/**
 * 女仆 AI 的 Tool 抽象。
 * <p>
 * Tool 用于向模型暴露一个可执行的原子操作，包含：
 * 工具标识、用途摘要、参数定义、参数编解码器，以及调用后的实际执行逻辑。
 * Tool 一般由某个 Skill 暴露给模型，而不是直接长期挂在根上下文中。
 *
 * @param <T> Tool 调用参数的解码结果类型
 */
public interface ITool<T> {
    /**
     * Tool 的唯一标识符，不能和其他 Tool 重复。
     * <p>
     * ID 建议仅使用小写英文字母、数字和下划线，且尽量语义化，便于模型调用和调试。
     */
    String id();

    /**
     * 返回对该 Tool 的简短摘要，帮助模型判断是否应调用此工具。
     *
     * @param maid 当前女仆实例
     */
    String summary(EntityMaid maid);

    /**
     * 构建该 Tool 的参数定义。
     *
     * @param root 参数根对象
     * @param maid 当前女仆实例
     */
    Parameter parameters(ObjectParameter root, EntityMaid maid);

    /**
     * 返回该 Tool 的参数编解码器，用于解析模型传入的结构化参数。
     */
    Codec<T> codec();

    /**
     * 执行 Tool 调用。
     *
     * @param result 解码后的参数对象
     * @param maid   当前女仆实例
     */
    ToolResponse onCall(T result, EntityMaid maid);

    /**
     * 程序侧再次判断当前 Tool 是否允许在当前上下文下暴露给模型。
     * 一般不需要重写此方法。
     *
     * @param maid           当前女仆实例
     * @param chatCompletion 当前对话请求对象
     */
    default boolean trigger(EntityMaid maid, ChatCompletion chatCompletion) {
        return true;
    }
}
