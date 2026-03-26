package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchFollowStateTool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchWorkTaskTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

public class MaidActionSkill implements ISkill {
    public static final String ID = "maid_action";

    private final List<String> tools;

    public MaidActionSkill() {
        this.tools = ImmutableList.of(
                SwitchFollowStateTool.TOOL_ID,
                SwitchWorkTaskTool.TOOL_ID
        );
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Use to change the maid's mode: follow/home, work tasks, or combat style.";
    }

    @Override
    public String body(EntityMaid maid) {
        String taskList = TaskManager.getTaskIndex().stream()
                .map(task -> "  - %s: %s".formatted(task.getUid().toString(), task.getMaidActionSummary()))
                .collect(Collectors.joining("\n"));
        return ("""
                ## Maid Control (maid_action)
                - Use when the player wants to change the maid's current mode or task.
                - Act on both direct commands and implied intent. If the request clearly maps to a mode, switch directly instead of just explaining.
                - follow-state tool: movement and position changes. Trigger examples: "follow me", "stay here", "don't move", "wait here", "stop following", "guard this place", "go home", or any instruction about where the maid should stay or whether to follow. Use follow=true for following, follow=false for staying/home mode.
                - work-task tool: task/role changes. Common implied mappings:
                  - The player says they are hungry, need food, ask for milk, or need healing food -> feed task
                  - Play a game / board-game blocks -> board_games task
                  - Combat help -> pick the task matching the weapon style (melee, bow, crossbow, trident, gun, danmaku)
                  - Resource or utility work -> switch to the matching task if clearly identified
                - The task list may include entries from other mods. Match them by reading their summary descriptions.
                - Only choose from the registered tasks below. Do not invent task ids. Use the full namespaced id (e.g. namespace:task_name) when calling the tool.
                - If one task clearly fits, call the tool immediately. If ambiguous, ask one follow-up question.
                - Available registered tasks:
                %s
                """).formatted(taskList);
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return tools;
    }
}
