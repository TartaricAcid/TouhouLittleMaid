package com.github.tartaricacid.touhoulittlemaid.ai.service.function.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
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

import java.util.Map;
import java.util.Optional;

public class SwitchWorkTaskFunction implements IFunctionCall<SwitchWorkTaskFunction.Result> {
    private static final String FUNCTION_ID = "switch_maid_work_task";
    private static final String FUNCTION_DESC = """
            Use this function to switch the maid to any available task, including non-combat tasks such as fishing, farming, torch placing, etc.
            This is the unified entry for all task changes, including combat tasks (attack, ranged_attack, crossbow_attack, danmaku_attack, trident_attack).""";
    private static final String TASK_ID_PARAMETER_ID = "task_id";
    private static final String TASK_ID_PARAMETER_DESC = """
            task_id (string, required): The specific ID of the task you want the maid to switch to.
            Examples include: idle, fishing, farm, torch, milk, shears, honey, extinguishing, feed, feed_animal,
            grass, snow, melon, cocoa, board_games, and combat tasks (attack, ranged_attack, crossbow_attack,
            danmaku_attack, trident_attack).""";
    private static final String SUCCESS = "Successfully switched to %s task";
    private static final String FAIL = "Switch failed and there is no task named %s";
    private static final String NO_CHANGE = "You're currently in %s task and don't need to switch";
    private static final String MISSING_REQUIRED = "Successfully switched to %s task, but required item is missing";
    private static final String PARTIAL = "Successfully switched to %s task, but some requirements are missing";

    @Override
    public String getId() {
        return FUNCTION_ID;
    }

    @Override
    public String getDescription(EntityMaid maid) {
        return FUNCTION_DESC;
    }

    @Override
    public Parameter addParameters(ObjectParameter root, EntityMaid maid) {
        StringParameter taskId = StringParameter.create();
        // 将所有可用任务加入枚举值（使用路径名，如 fishing、farm、torch 等）
        for (Map.Entry<ResourceLocation, IMaidTask> entry : TaskManager.getTaskMap().entrySet()) {
            taskId.addEnumValues(entry.getKey().getPath());
        }
        taskId.setDescription(TASK_ID_PARAMETER_DESC);
        root.addProperties(TASK_ID_PARAMETER_ID, taskId);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(TASK_ID_PARAMETER_ID).forGetter(Result::id)
        ).apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        String id = result.id;
        ResourceLocation taskId = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, id);
        Optional<IMaidTask> optional = TaskManager.findTask(taskId);
        if (optional.isEmpty()) {
            return new ToolResponse(FAIL.formatted(id));
        }

        IMaidTask task = optional.get();
        IMaidTask currentTask = maid.getTask();

        if (task == currentTask) {
            FunctionCallSwitchResult switchResult = task.onFunctionCallSwitch(maid);
            return switch (switchResult) {
                case NO_CHANGE -> new ToolResponse(NO_CHANGE.formatted(id));
                case MISSING_REQUIRED_ITEM -> new ToolResponse(MISSING_REQUIRED.formatted(id));
                case PARTIAL_OK -> new ToolResponse(PARTIAL.formatted(id));
                case OK -> new ToolResponse(SUCCESS.formatted(id));
            };
        }

        maid.setTask(task);
        FunctionCallSwitchResult switchResult = task.onFunctionCallSwitch(maid);
        return switch (switchResult) {
            case NO_CHANGE, OK -> new ToolResponse(SUCCESS.formatted(id));
            case MISSING_REQUIRED_ITEM -> new ToolResponse(MISSING_REQUIRED.formatted(id));
            case PARTIAL_OK -> new ToolResponse(PARTIAL.formatted(id));
        };
    }

    public record Result(String id) {
    }
}
