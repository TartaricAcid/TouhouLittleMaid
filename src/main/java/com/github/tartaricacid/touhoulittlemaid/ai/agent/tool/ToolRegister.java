package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillRegister;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class ToolRegister {
    public static final String USE_SKILL = "use_skill";

    private static Map<String, ITool<?>> TOOLS = Maps.newHashMap();

    public static void init() {
        SkillRegister register = new SkillRegister();
        TOOLS = ImmutableMap.copyOf(TOOLS);
    }

    public void register(ITool<?> skill) {
        TOOLS.put(skill.id(), skill);
    }

    @Nullable
    public static ITool<?> getTool(String name) {
        return TOOLS.get(name);
    }

    public static Map<String, ITool<?>> getAllTools() {
        return TOOLS;
    }
}
