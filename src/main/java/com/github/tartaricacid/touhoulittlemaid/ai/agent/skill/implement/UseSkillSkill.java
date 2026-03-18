package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.UseSkillTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UseSkillSkill implements ISkill {
    public static final String ID = "use_skill";

    private final List<String> tools;

    public UseSkillSkill() {
        this.tools = Collections.singletonList(UseSkillTool.TOOL_ID);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Root routing skill that lists all currently available follow-up skills and lets the model select one by id.";
    }

    @Override
    public String body(EntityMaid maid) {
        String skillList = SkillRegister.getAllSkills().values().stream()
                .filter(skill -> !ID.equals(skill.id()))
                .filter(skill -> skill.trigger(maid))
                .map(skill -> "- %s: %s".formatted(skill.id(), skill.summary(maid)))
                .collect(Collectors.joining("\n"));
        return """
                ## Use Skill
                - This is the root routing skill.
                - Review the available skills below, then call the use_skill tool with the target skill id.
                - Only load one follow-up skill at a time.
                
                Available skills:
                %s
                """.formatted(skillList);
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return this.tools;
    }
}
