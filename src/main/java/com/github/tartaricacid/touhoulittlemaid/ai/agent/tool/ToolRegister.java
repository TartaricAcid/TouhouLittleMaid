package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.QueryMaidContextTool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchFollowStateTool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchWorkTaskTool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.UseSkillTool;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class ToolRegister {
    private static Map<String, ITool<?>> TOOLS = Maps.newLinkedHashMap();

    public static void init() {
        ToolRegister register = new ToolRegister();

        register.register(new UseSkillTool());
        register.register(new QueryMaidContextTool());
        register.register(new SwitchFollowStateTool());
        register.register(new SwitchWorkTaskTool());

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerAITool(register);
        }

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
