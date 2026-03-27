package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.MaidWorkSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.api.task.FunctionCallSwitchResult;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class SwitchMaidWorkTaskTool implements ITool<SwitchMaidWorkTaskTool.Result> {
    public static final String TOOL_ID = "switch_maid_work_task";

    private static final String TOOL_DESC = "Switch the maid to a specific task by task_id.";

    private static final String TASK_ID_PARAMETER_ID = "task_id";
    private static final String TASK_ID_PARAMETER_DESC = "The full namespaced task id to switch to.";

    private static final String SUCCESS = "Successfully switched to %s task";
    private static final String NO_CHANGE = "Already on %s task, no switch needed";
    private static final String MISSING_REQUIRED = "Successfully switched to %s task, but required item is missing";
    private static final String PARTIAL = "Successfully switched to %s task, but some requirements are missing";

    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf(TASK_ID_PARAMETER_ID).forGetter(Result::id)
    ).apply(instance, Result::new));

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return TOOL_DESC;
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter taskId = StringParameter.create();

        List<IMaidTask> tasks = getAvailableTasks();
        tasks.stream().map(IMaidTask::getUid)
                .map(ResourceLocation::toString)
                .forEach(taskId::addEnumValues);

        taskId.setDescription(TASK_ID_PARAMETER_DESC);
        root.addProperties(TASK_ID_PARAMETER_ID, taskId);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public ToolResponse onCall(Result result, EntityMaid maid) {
        ResourceLocation taskId = result.id;
        List<IMaidTask> tasks = getAvailableTasks();
        Optional<IMaidTask> optional = TaskManager.findTask(taskId);

        if (optional.isEmpty()) {
            List<String> values = tasks.stream()
                    .map(IMaidTask::getUid)
                    .map(ResourceLocation::toString)
                    .toList();
            String text = "unknown task_id '%s'".formatted(taskId);
            return ToolResponse.invalidParam(TASK_ID_PARAMETER_ID, values, text, MaidWorkSkill.ID);
        }

        IMaidTask task = optional.get();
        IMaidTask currentTask = maid.getTask();

        if (task == currentTask) {
            FunctionCallSwitchResult switchResult = task.onFunctionCallSwitch(maid);
            return switch (switchResult) {
                case NO_CHANGE -> new ToolResponse(NO_CHANGE.formatted(taskId));
                case MISSING_REQUIRED_ITEM -> new ToolResponse(MISSING_REQUIRED.formatted(taskId));
                case PARTIAL_OK -> new ToolResponse(PARTIAL.formatted(taskId));
                case OK -> new ToolResponse(SUCCESS.formatted(taskId));
            };
        }

        maid.setTask(task);
        FunctionCallSwitchResult switchResult = task.onFunctionCallSwitch(maid);
        return switch (switchResult) {
            case NO_CHANGE, OK -> new ToolResponse(SUCCESS.formatted(taskId));
            case MISSING_REQUIRED_ITEM -> new ToolResponse(MISSING_REQUIRED.formatted(taskId));
            case PARTIAL_OK -> new ToolResponse(PARTIAL.formatted(taskId));
        };
    }

    private static List<IMaidTask> getAvailableTasks() {
        return TaskManager.getTaskIndex();
    }

    public record Result(ResourceLocation id) {
    }
}
