package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.ToolCall;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HistoryMessagesCheck {
    /**
     * 在发送给 LLM 之前，清理消息列表中不合法的 tool 相关消息。
     * <p>
     * 处理两种情况：
     * <ol>
     *   <li>对话区开头出现孤儿 TOOL 消息（缺少前置的 ASSISTANT tool_call）</li>
     *   <li>ASSISTANT 消息携带了 tool_calls，但后续缺少全部对应的 TOOL 结果消息</li>
     * </ol>
     * 历史压缩、网络异常等都可能产生上述不配对的情况，大多数 LLM API 遇到后会报错。
     */
    public static void checkMessages(List<LLMMessage> messages) {
        if (messages.size() <= 1) {
            return;
        }

        // 先跳过开头连续的 SYSTEM 消息，定位到"对话区"的起始位置
        int systemCount = 0;
        while (systemCount < messages.size() && messages.get(systemCount).role() == Role.SYSTEM) {
            systemCount++;
        }
        // 全都是 SYSTEM 消息，没有需要过滤的对话内容
        if (systemCount >= messages.size()) {
            return;
        }

        // 保留 SYSTEM 前缀，对话区部分丢弃开头连续的孤儿 TOOL 消息后重新拼接
        List<LLMMessage> systemMessages = Lists.newArrayList(messages.subList(0, systemCount));
        List<LLMMessage> conversation = messages.stream()
                .skip(systemCount)
                // 丢弃开头连续的 tool 消息
                .dropWhile(msg -> Role.TOOL.equals(msg.role()))
                .collect(Collectors.toCollection(ArrayList::new));

        // 清理不完整的 tool call 配对
        removeUnpairedToolCalls(conversation);

        messages.clear();
        messages.addAll(systemMessages);
        messages.addAll(conversation);
    }

    /**
     * 扫描对话列表，移除不完整的 tool call 配对。
     * <p>
     * 如果某条 ASSISTANT 消息携带了 tool_calls，但紧随其后的 TOOL 消息
     * 未能覆盖所有 tool_call id，则将该 ASSISTANT 的 tool_calls 剥离
     * （保留其文本内容），并删除其后所有孤儿 TOOL 消息。
     */
    private static void removeUnpairedToolCalls(List<LLMMessage> conversation) {
        int i = 0;
        while (i < conversation.size()) {
            LLMMessage msg = conversation.get(i);
            if (msg.role() != Role.ASSISTANT || msg.toolCalls() == null || msg.toolCalls().isEmpty()) {
                i++;
                continue;
            }

            // 收集该 ASSISTANT 期望的所有 tool_call id
            Set<String> expectedIds = msg.toolCalls().stream()
                    .map(ToolCall::getId)
                    .collect(Collectors.toSet());

            // 收集紧随其后连续 TOOL 消息的 toolCallId
            int toolStart = i + 1;
            int toolEnd = toolStart;
            Set<String> actualIds = Sets.newHashSet();
            while (toolEnd < conversation.size() && conversation.get(toolEnd).role() == Role.TOOL) {
                String toolCallId = conversation.get(toolEnd).toolCallId();
                if (toolCallId != null) {
                    actualIds.add(toolCallId);
                }
                toolEnd++;
            }

            // 如果 TOOL 结果不能完整覆盖所有 tool_call，视为不完整配对
            if (!actualIds.containsAll(expectedIds)) {
                // 用不带 toolCalls 的副本替换原 ASSISTANT 消息
                conversation.set(i, new LLMMessage(msg.role(), msg.message(), msg.gameTime()));
                // 删除其后所有孤儿 TOOL 消息
                if (toolEnd > toolStart) {
                    conversation.subList(toolStart, toolEnd).clear();
                }
                // 不递增 i，删除后当前位置可能又是新的 ASSISTANT+toolCalls
                continue;
            }

            i++;
        }
    }
}
