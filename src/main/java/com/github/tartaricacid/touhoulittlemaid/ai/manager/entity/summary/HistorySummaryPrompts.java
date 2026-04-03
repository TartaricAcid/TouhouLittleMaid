package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.UserPromptContexts;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

public final class HistorySummaryPrompts {
    static final int MAX_SUMMARY_LENGTH = 1600;
    static final int MAX_SINGLE_LENGTH = 300;
    static final int MIN_MESSAGES_TO_COMPRESS = 4;

    static final String SUMMARY_SYSTEM_PROMPT = """
            You compress old in-game maid chat history into a continuity summary.
            Merge the existing summary with the older messages into one concise result.
            Output plain text only, at most 12 short bullet lines.
            Focus on: long-term facts, player preferences, important outcomes, and unresolved topics.
            Omit greetings, small talk, and redundant details. Do not use markdown code fences.
            """;

    private HistorySummaryPrompts() {
    }

    static String formatSummarySystemMessage(MaidAIChatManager manager) {
        return """
                ## Compressed Conversation Summary
                %s
                """.formatted(manager.getCompressedSummary());
    }

    static String buildSummaryRequest(String previousSummary, String historyBlock) {
        return """
                Existing summary:
                %s
                
                Older messages to compress:
                %s
                """.formatted(previousSummary, historyBlock);
    }

    public static String buildSummaryEntry(LLMMessage message) {
        Role role = message.role();
        String text = message.message();

        if (role == Role.USER) {
            // 剔除 context 部分
            text = UserPromptContexts.removeContext(text);
            return "[USER] %s".formatted(text);
        }

        if (role == Role.ASSISTANT) {
            if (StringUtils.isNotBlank(text)) {
                // 只保留 chat text 部分，减少 token 消耗
                String chatText = new ResponseChat(text).getChatText();
                return "[ASSISTANT] %s".formatted(chatText);
            }

            // 如果是 LLM 的工具调用部分
            if (message.toolCalls() != null && !message.toolCalls().isEmpty()) {
                String names = message.toolCalls().stream()
                        .map(tool -> tool.getFunction().getName())
                        .collect(Collectors.joining(", "));
                return "[TOOL_CALL] %s".formatted(names);
            }

            return StringUtils.EMPTY;
        }

        String abbreviate = StringUtils.abbreviate(text, MAX_SINGLE_LENGTH);
        if (role == Role.TOOL) {
            return "[TOOL_RESULT] %s".formatted(abbreviate);
        }

        return "[SYSTEM] %s".formatted(abbreviate);
    }
}
