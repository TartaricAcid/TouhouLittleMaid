package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeDefinition;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.MaidKnowledgeSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.summary.HistorySummaryPrompts;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class QueryMaidKnowledgeTool implements ITool<String> {
    public static final String TOOL_ID = "query_maid_knowledge";

    private static final String KNOWLEDGE_ID = "knowledge_id";
    private static final Codec<String> CODEC = Codec.STRING.fieldOf(KNOWLEDGE_ID).codec();

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Load one custom knowledge entry by knowledge_id and answer from its full grounded content.";
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter knowledgeId = StringParameter.create();

        List<KnowledgeDefinition> list = getAvailableKnowledge();
        knowledgeId.setDescription(buildKnowledgeDescription(list));
        list.stream().map(KnowledgeDefinition::getId).forEach(knowledgeId::addEnumValues);

        root.addProperties(KNOWLEDGE_ID, knowledgeId);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public ToolResponse onCall(String knowledgeId, EntityMaid maid) {
        List<KnowledgeDefinition> list = getAvailableKnowledge();
        KnowledgeDefinition knowledge = KnowledgeRegister.getKnowledge(knowledgeId);

        if (knowledge == null) {
            return ToolResponse.invalidParam(KNOWLEDGE_ID,
                    list.stream().map(KnowledgeDefinition::getId).toList(),
                    "unknown knowledge_id '%s'".formatted(knowledgeId),
                    MaidKnowledgeSkill.ID);
        }

        String chatLanguage = maid.getAiChatManager().getChatLanguage();
        String body = knowledge.getResolvedBody(chatLanguage);
        String resolvedKnowledge = """
                ## Knowledge: %s
                - %s
                %s
                """.formatted(knowledge.getId(), knowledge.getDesc(), body);

        String question = resolveUserQuestion(maid);
        String toolMessage = "Loaded knowledge: %s".formatted(knowledge.getId());

        return ToolResponse.groundedAnswer(toolMessage, MaidKnowledgeSkill.ID, question, resolvedKnowledge);
    }

    /**
     * 从聊天历史中提取最近一条用户消息，并无条件附带近期上下文。
     * <p>
     * 下游 grounded answer pass 的 system prompt 已声明"如果有上下文就自行消解指代，没有就直接回答"，
     * 因此始终附带上下文不会产生副作用，同时完全避免了硬编码关键词判断追问的误判问题。
     */
    private static final int MAX_CONTEXT_MESSAGES = 4;

    private static String resolveUserQuestion(EntityMaid maid) {
        String latestUser = StringUtils.EMPTY;
        List<LLMMessage> contextMessages = Lists.newArrayList();

        Iterator<LLMMessage> iterator = maid.getAiChatManager().getHistory().getDeque().iterator();
        while (iterator.hasNext()) {
            LLMMessage message = iterator.next();
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

    private static List<KnowledgeDefinition> getAvailableKnowledge() {
        return List.copyOf(KnowledgeRegister.getAllKnowledge().values());
    }

    private static String buildKnowledgeDescription(List<KnowledgeDefinition> list) {
        String info = list.stream()
                .map(knowledge -> "- %s: %s".formatted(knowledge.getId(), knowledge.getDesc()))
                .collect(Collectors.joining("\n"));

        return """
                knowledge_id (string, required): Choose one knowledge entry to load for grounded answering.
                
                Available knowledge:
                %s
                """.formatted(StringUtils.defaultIfBlank(info, "- No knowledge is currently loaded."));
    }
}
