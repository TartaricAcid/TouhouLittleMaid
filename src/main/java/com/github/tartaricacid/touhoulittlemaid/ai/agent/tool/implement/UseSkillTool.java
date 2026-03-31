package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillInstance;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillLoader;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.grounded.GroundedAnswerCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;

import java.util.List;

public class UseSkillTool implements ITool<String> {
    public static final String TOOL_ID = "use_skill";
    private static final String NAME_PARAMETER_ID = "name";
    private static final String SUMMARY = """
            Load a skill or execute a slash command to get detailed instructions for a specific task.
            
            Skills and commands provide specialized knowledge and step-by-step guidance.
            Use this when a task matches an available skill's or command's description.
            
            **How to use:**
            - Call with a skill name: name='code-review'
            - Call with a command name (without leading slash): name='publish'
            - The tool will return detailed instructions with your context applied.
            
            """;
    private static final Codec<String> CODEC = Codec.STRING.fieldOf(NAME_PARAMETER_ID).codec();

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return SUMMARY;
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter skillId = StringParameter.create();
        skillId.setDescription("The skill name (e.g., 'code-review' or 'publish')");
        root.addProperties(NAME_PARAMETER_ID, skillId);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, String result, LLMCallback callback) {
        SkillInstance selected = SkillLoader.getSkill(result);

        if (selected == null) {
            List<String> values = Lists.newArrayList(SkillLoader.getAllSkills().keySet());
            String text = "unknown skill_id '%s'".formatted(result);
            String invalidMsg = ITool.invalidParam(NAME_PARAMETER_ID, values, text);
            return callback.addToolResult(invalidMsg, toolId);
        }

        // 如果是知识库查询，那么需要新建空白回调
        if (selected.isKnowledgeType()) {
            return new GroundedAnswerCallback(callback.getChatManager(), selected.body(), callback.getWaitingChatBubbleId());
        }

        // 普通 skill 回调
        return callback.addToolResult(selected.body(), toolId);
    }
}
