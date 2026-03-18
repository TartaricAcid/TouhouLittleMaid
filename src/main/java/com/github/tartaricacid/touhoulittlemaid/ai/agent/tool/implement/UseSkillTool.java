package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.UseSkillSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;

import java.util.List;
import java.util.stream.Collectors;

public class UseSkillTool implements ITool<String> {
    public static final String TOOL_ID = "use_skill";
    private static final String SKILL_ID_PARAMETER_ID = "skill_id";
    private static final String SUMMARY = "Load one follow-up skill by skill id.";
    private static final Codec<String> CODEC = Codec.STRING.fieldOf(SKILL_ID_PARAMETER_ID).codec();

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
        List<ISkill> availableSkills = getAvailableSkills(maid);
        skillId.setDescription(buildDescription(maid, availableSkills));
        availableSkills.stream().map(ISkill::id).forEach(skillId::addEnumValues);
        root.addProperties(SKILL_ID_PARAMETER_ID, skillId);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public ToolResponse onCall(String result, EntityMaid maid) {
        List<ISkill> availableSkills = getAvailableSkills(maid);
        boolean valid = availableSkills.stream().anyMatch(skill -> skill.id().equals(result));
        if (!valid) {
            List<String> values = availableSkills.stream().map(ISkill::id).toList();
            String text = "unknown skill_id '%s'".formatted(result);
            return ToolErrorHelper.invalidParamToolResponse(SKILL_ID_PARAMETER_ID, values, text);
        }
        return new ToolResponse(result);
    }

    private static String buildDescription(EntityMaid maid, List<ISkill> availableSkills) {
        String skillList = availableSkills.stream()
                .map(skill -> "- %s: %s".formatted(skill.id(), skill.summary(maid)))
                .collect(Collectors.joining("\n"));
        return """
                skill_id (string, required): The id of the follow-up skill to load.
                Choose one of the currently available skill ids below.
                
                Available skills:
                %s
                """.formatted(skillList);
    }

    private static List<ISkill> getAvailableSkills(EntityMaid maid) {
        return SkillRegister.getAllSkills().values().stream()
                .filter(skill -> !UseSkillSkill.ID.equals(skill.id()))
                .filter(skill -> skill.trigger(maid))
                .toList();
    }
}
