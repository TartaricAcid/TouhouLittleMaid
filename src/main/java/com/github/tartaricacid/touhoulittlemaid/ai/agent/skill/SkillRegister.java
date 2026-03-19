package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.MaidActionSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.MaidContextSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.MaidKnowledgeSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.UseSkillSkill;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class SkillRegister {
    private static Map<String, ISkill> SKILLS = Maps.newHashMap();

    public static void init() {
        SkillRegister register = new SkillRegister();

        register.register(new UseSkillSkill());
        register.register(new MaidContextSkill());
        register.register(new MaidActionSkill());
        register.register(new MaidKnowledgeSkill());

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerAISkill(register);
        }

        SKILLS = ImmutableMap.copyOf(SKILLS);
    }

    public void register(ISkill skill) {
        SKILLS.put(skill.id(), skill);
    }

    @Nullable
    public static ISkill getSkill(String name) {
        return SKILLS.get(name);
    }

    public static boolean hasSkill(String name) {
        return SKILLS.containsKey(name);
    }

    public static Map<String, ISkill> getAllSkills() {
        return SKILLS;
    }
}
