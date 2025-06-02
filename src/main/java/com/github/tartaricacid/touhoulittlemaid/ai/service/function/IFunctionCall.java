package com.github.tartaricacid.touhoulittlemaid.ai.service.function;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;

/**
 * 女仆能够执行的 Function Call 对象
 *
 * @param <T>
 */
public interface IFunctionCall<T> {
    /**
     * function 名称，建议小写英文、下划线
     */
    String getId();

    /**
     * 告诉 AI，什么时候需要调用这个 function
     *
     * @param maid 正在对话的女仆，可以依据女仆来动态生成一些提示词
     */
    String getDescription(EntityMaid maid);

    /**
     * 为 function call 添加参数，告诉 AI 需要返回什么样子的参数
     *
     * @param root 空的对象，往里面存入你的参数，然后返回它就行
     * @param maid 正在对话的女仆，可以依据女仆来动态生成一些提示词
     * @return 一般还是传入的 root
     */
    Parameter addParameters(ObjectParameter root, EntityMaid maid);

    /**
     * 当 AI 返回 function call 参数时，你解码这个 json 字符串的解码器
     */
    Codec<T> codec();

    /**
     * 是否添加进 AI 对话中，建议只在特定情况下添加，可以节省 token 数量
     *
     * @param maid           正在对话的女仆
     * @param chatCompletion AI 对话对象
     * @return 是否添加进 AI 对话中
     */
    default boolean addToChatCompletion(EntityMaid maid, ChatCompletion chatCompletion) {
        return true;
    }

    /**
     * 最终 function call 执行的游戏逻辑播放。
     * <p>
     * 还是要注意，AI 并没有那么智能，它有可能给你传入完全超出预期的参数，一定要注意
     *
     * @param result 解码后的对象
     * @param maid   正在对话的女仆
     * @return 该函数执行完毕后，返回的工具结果
     */
    ToolResponse onToolCall(T result, EntityMaid maid);
}
