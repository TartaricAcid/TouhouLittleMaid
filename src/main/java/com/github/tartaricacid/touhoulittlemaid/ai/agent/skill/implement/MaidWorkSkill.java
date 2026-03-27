package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchMaidScheduleTool;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SwitchMaidWorkTaskTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

public class MaidWorkSkill implements ISkill {
    public static final String ID = "maid_work";

    private final List<String> tools;

    public MaidWorkSkill() {
        this.tools = ImmutableList.of(
                SwitchMaidWorkTaskTool.TOOL_ID,
                SwitchMaidScheduleTool.TOOL_ID
        );
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Use to change the maid's tasks (e.g., idle, combat, farming, animal, utility), or schedule (day shift, night shift, all day).";
    }

    @Override
    public String body(EntityMaid maid) {
        String taskList = TaskManager.getTaskIndex().stream()
                .map(task -> "  - %s: %s".formatted(task.getUid().toString(), task.getMaidActionSummary()))
                .collect(Collectors.joining("\n"));
        return ("""
                ## Maid Work Management (maid_work)
                - Use when the player wants to change the maid's work task or work schedule.
                - Act on both direct commands and implied intent. If the request clearly maps to a task or schedule, switch directly instead of just explaining.
                
                ### Work Task Tool
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
                
                ### Schedule Tool
                - schedule tool: change when the maid works. Trigger examples: "work during the day", "switch to night shift", "work all day", "rest at night", "change to day shift".
                  - DAY: Work during daytime, rest at night (default).
                  - NIGHT: Work during nighttime, rest during the day.
                  - ALL: Work around the clock without rest.
                """).formatted(taskList);
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return tools;
    }
}
