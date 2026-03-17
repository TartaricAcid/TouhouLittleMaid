package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class SkillRegister {
    public static final String USE_SKILL = "use_skill";

    private static Map<String, ISkill> SKILLS = Maps.newHashMap();

    public static void init() {
        SkillRegister register = new SkillRegister();
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
