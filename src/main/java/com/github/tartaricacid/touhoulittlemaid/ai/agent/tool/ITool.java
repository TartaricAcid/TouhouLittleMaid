package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * 女仆 AI 的 Tool 抽象。
 * <p>
 * Tool 用于向模型暴露一个可执行的原子操作，包含：
 * 工具标识、用途摘要、参数定义、参数编解码器，以及调用后的实际执行逻辑。
 * Tool 一般直接长期挂在根上下文中，由具体的 Skill 里写提示词让 LLM 主动触发调用。
 *
 * @param <T> Tool 调用参数的解码结果类型
 */
public interface ITool<T> {
    /**
     * 工具方法，告诉 LLM 本次 Function Call 的参数无效，并给出正确的参数选项，要求 LLM 重新生成调用
     *
     * @param reason 无效的原因描述
     */
    static String invalidParam(String parameterName, Collection<String> values, String reason) {
        String joined = String.join(", ", values);
        String correctUsage = "%s: choose one of [%s]".formatted(parameterName, joined);
        return "Invalid parameter: %s. Correct usage: %s".formatted(reason, correctUsage);
    }

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
     * 执行 Tool 调用。请记得在传入的 callback 里添加工具的返回消息，从而继续让对话进行下去
     * <p>
     * 此方法依赖于 onCallAsync 的默认实现，因此默认情况下会被 onCallAsync 调用。
     * <p>
     * 需要同步执行的 Tool 可以直接重写此方法，而需要异步执行的 Tool 则需要重写 onCallAsync 方法。
     *
     * @param toolCallId LLM 发回的参数，需要带上这个 ID 以让 LLM 知道这是哪个 Tool 的返回结果
     * @param result     解码后的参数对象
     * @param callback   当前执行逻辑的回调
     * @return 返回回调，这个回调会进行下一轮 LLM 通信。这个回调可以是原样传入的，也可以是新建的
     */
    LLMCallback onCall(String toolCallId, T result, LLMCallback callback);

    /**
     * 异步执行 Tool 调用。
     * <p>
     * 默认直接复用同步 {@link #onCall(String, Object, LLMCallback)}，以保证已有 Tool 无需修改。
     * 需要执行耗时异步任务的 Tool 可以重写此方法，并在完成后返回用于下一轮通信的回调。
     *
     * @param toolCallId LLM 发回的参数，需要带上这个 ID 以让 LLM 知道这是哪个 Tool 的返回结果
     * @param result     解码后的参数对象
     * @param callback   当前执行逻辑的回调
     * @param client     当前调用的 LLM 客户端实例，必要时可以通过它发起新的对话请求
     * @return 异步回调结果
     */
    @ApiStatus.AvailableSince("1.5.2")
    default CompletableFuture<LLMCallback> onCallAsync(
            String toolCallId, T result, LLMCallback callback, LLMClient client
    ) {
        LLMCallback onCall = onCall(toolCallId, result, callback);
        return CompletableFuture.completedFuture(onCall);
    }

    /**
     * 生成此次工具调用的摘要信息，用于女仆聊天气泡提示
     *
     * @param result 解码后的参数对象
     * @return 摘要信息
     */
    default String invocationSummary(T result) {
        return this.id();
    }

    /**
     * 可翻译的工具调用的摘要信息，用于女仆聊天气泡提示
     * <p>
     * 此方法和上面的 {@link #invocationSummary(Object)} 功能存在重复，
     * 返回值是一个 Component，可以包含翻译文本和样式信息，而不仅仅是纯字符串
     * <p>
     * 如果此方法返回非 EMPTY，将覆盖 {@link #invocationSummary(Object)}
     *
     * @param result 解码后的参数对象
     * @return Component 形式的摘要信息
     */
    @ApiStatus.AvailableSince("1.5.2")
    default Component invocationSummaryComponent(T result) {
        return Component.empty();
    }

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
