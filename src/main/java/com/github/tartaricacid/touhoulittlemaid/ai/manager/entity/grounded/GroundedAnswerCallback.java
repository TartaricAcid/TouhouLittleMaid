package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.grounded;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary.HistorySummaryPrompts;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.Message;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringJoiner;

public class GroundedAnswerCallback extends LLMCallback {
    /**
     * 从聊天历史中提取最近一条用户消息，并无条件附带近期上下文。
     * <p>
     * 下游 grounded answer pass 的 system prompt 已声明"如果有上下文就自行消解指代，没有就直接回答"，
     * 因此始终附带上下文不会产生副作用，同时完全避免了硬编码关键词判断追问的误判问题。
     */
    private static final int MAX_CONTEXT_MESSAGES = 4;

    public GroundedAnswerCallback(MaidAIChatManager manager, String knowledge, long waitingChatBubbleId) {
        super(manager, createGroundedAnswerMessages(manager, knowledge), true);
        this.waitingChatBubbleId = waitingChatBubbleId;
        this.needAddTools = false;
    }

    @Override
    public void onFunctionCall(Message choice, LLMClient client) {
        // 知识库回答时不处理函数调用，直接忽略（理论上也不会触发此回调）
    }

    /**
     * 创建一个空白的知识库上下文
     */
    private static List<LLMMessage> createGroundedAnswerMessages(MaidAIChatManager manager, String knowledge) {
        EntityMaid maid = manager.getMaid();
        List<LLMMessage> msg = Lists.newArrayList();

        String systemPrompt = GroundedAnswerPrompts.systemPrompt(maid, manager.chatLanguage);

        String resolvedQuestion = resolveUserQuestion(manager);
        String userPrompt = GroundedAnswerPrompts.buildUserPrompt(resolvedQuestion, knowledge);

        msg.add(LLMMessage.systemChat(maid, systemPrompt));
        msg.add(LLMMessage.userChat(maid, userPrompt));

        return msg;
    }

    private static String resolveUserQuestion(MaidAIChatManager manager) {
        String latestUser = StringUtils.EMPTY;
        List<LLMMessage> contextMessages = Lists.newArrayList();

        // getDeque 为由近及远的聊天记录
        for (LLMMessage message : manager.getHistory().getDeque()) {
            if (StringUtils.isBlank(latestUser)) {
                if (message.role() == Role.USER && StringUtils.isNotBlank(message.message())) {
                    latestUser = message.message();
                }
                continue;
            }
            contextMessages.add(message);
            if (contextMessages.size() >= MAX_CONTEXT_MESSAGES) {
                break;
            }
        }

        if (StringUtils.isBlank(latestUser)) {
            return StringUtils.EMPTY;
        }

        // 用 buildSummaryEntry 格式化上下文，过滤空条目，反转为时间正序
        StringJoiner joiner = new StringJoiner("\n");
        for (int i = contextMessages.size() - 1; i >= 0; i--) {
            String entry = HistorySummaryPrompts.buildSummaryEntry(contextMessages.get(i));
            if (StringUtils.isNotBlank(entry)) {
                joiner.add(entry);
            }
        }

        // 没有有效上下文，直接返回原始问题
        if (joiner.length() == 0) {
            return latestUser;
        }

        // 始终附带上下文，让 grounded answer LLM 自行决定是否需要消解指代
        return """
                Nearby conversation context:
                %s
                
                Current user question: %s""".formatted(joiner, latestUser);
    }
}
