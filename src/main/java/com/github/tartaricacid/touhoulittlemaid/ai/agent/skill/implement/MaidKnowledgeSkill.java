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
        return "Use when the player's question may be answered by the built-in knowledge base.";
    }

    @Override
    public String body(EntityMaid maid) {
        String knowledgeList = KnowledgeRegister.getAllKnowledge().values().stream()
                .map(knowledge -> "- %s: %s".formatted(knowledge.getId(), knowledge.getDesc()))
                .collect(Collectors.joining("\n"));
        return """
                ## Maid Knowledge
                - The player is asking a question that may be answered by a knowledge entry below.
                - Pick the most relevant entry and call the knowledge tool with its id to load the full content.
                - Use the loaded content to give a grounded, accurate answer.
                - If no entry seems relevant, reply normally without this skill.

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
