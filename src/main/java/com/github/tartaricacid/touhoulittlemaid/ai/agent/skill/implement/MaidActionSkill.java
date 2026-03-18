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
        return "This skill is maid control. Use it when the player wants you to change maid state or behavior, such as follow mode or work or combat task.";
    }

    @Override
    public String body(EntityMaid maid) {
        String taskList = TaskManager.getTaskIndex().stream()
                .map(task -> "  - %s: %s".formatted(task.getUid().getPath(), task.getMaidActionSummary()))
                .collect(Collectors.joining("\n"));
        return ("""
                ## Maid Control (maid_action)
                - Use this skill only when the player wants you to change your behavior or mode.
                - For follow or stay-home requests, use the follow-state tool.
                - For work or combat role changes, use the work-task tool.
                - If the player wants a change but the target mode is unclear, ask one concise follow-up question before calling a tool.
                - Available registered tasks:
                %s
                """).formatted(taskList);
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return tools;
    }
}
