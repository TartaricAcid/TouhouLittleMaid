package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.QueryMaidContextTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.List;
import java.util.stream.Collectors;

public class MaidContextSkill implements ISkill {
    public static final String ID = "maid_context";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "When requesting live game context about the maid or her surroundings (e.g., items, attributes, world state, owner, nearby entities), retrieve only one category of information at a time.";
    }

    @Override
    public String body(EntityMaid maid) {
        String categories = MaidContextRegister.getAllCategories().stream()
                .map(category -> "- %s: %s".formatted(category.id(), category.summary()))
                .collect(Collectors.joining("\n"));
        return """
                ## Maid Context
                - Use this skill when you need any live context about the maid or her environment.
                - Categories may be added by other mods — always check the full list below before deciding.
                - Do not request all context at once. First choose the single most relevant category.
                - Then call the context tool with the category id to load only that context group.
                
                Available categories:
                %s
                """.formatted(categories);
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return List.of(QueryMaidContextTool.TOOL_ID);
    }
}
