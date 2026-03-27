package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchMaidFollowStateTool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchMaidSitTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class MaidStateSkill implements ISkill {
    public static final String ID = "maid_state";

    private final List<String> tools;

    public MaidStateSkill() {
        this.tools = ImmutableList.of(
                SwitchMaidFollowStateTool.TOOL_ID,
                SwitchMaidSitTool.TOOL_ID
        );
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Use to control the maid's movement state: follow/stay and sit/stand.";
    }

    @Override
    public String body(EntityMaid maid) {
        return """
                ## Maid State Control (maid_state)
                - Use when the player wants to change the maid's movement or posture state.
                - Act on both direct commands and implied intent. If the request clearly maps to a state change, switch directly instead of just explaining.
                
                ### Follow State Tool
                - Controls whether the maid follows the owner or stays at a fixed position (home mode).
                - Trigger examples: "follow me", "come with me", "stay here", "don't move", "wait here", "stop following", "guard this place", "go home".
                - Use follow=true for following the owner, follow=false for staying at the current position in home mode.
                
                ### Sit Tool
                - Controls whether the maid sits down or stands up.
                - Trigger examples: "sit down", "take a seat", "rest here", "stand up", "get up", "stop sitting".
                - Use sit=true to make the maid sit, sit=false to make the maid stand.
                - Note: A sitting maid will not move or perform some work tasks until she stands up.
                """;
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return tools;
    }
}
