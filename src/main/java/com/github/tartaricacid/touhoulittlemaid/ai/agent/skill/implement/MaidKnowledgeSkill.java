package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.QueryMaidKnowledgeTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.List;
import java.util.stream.Collectors;

public class MaidKnowledgeSkill implements ISkill {
    public static final String ID = "maid_knowledge";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return """
                Use when the user's question might be answered by the knowledge base.
                Check available entries below before answering from general knowledge.
                """;
    }

    @Override
    public String body(EntityMaid maid) {
        String knowledgeList = KnowledgeRegister.getAllKnowledge().values().stream()
                .map(knowledge -> "- %s: %s".formatted(knowledge.getId(), knowledge.getDesc()))
                .collect(Collectors.joining("\n"));
        return """
                ## Maid Knowledge
                - The player is asking a question. The answer may exist in one of the knowledge entries listed below.
                - Review the available entries and pick the one most relevant to the player's question.
                - Call the knowledge tool with that entry's id to load its full content.
                - Then use the loaded content to give the player a grounded, accurate answer.
                - If none of the entries seem relevant, do not use this skill.
                
                Available knowledge:
                %s
                """.formatted(knowledgeList);
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return List.of(QueryMaidKnowledgeTool.TOOL_ID);
    }

    @Override
    public boolean trigger(EntityMaid maid) {
        return !KnowledgeRegister.getAllKnowledge().isEmpty();
    }
}
