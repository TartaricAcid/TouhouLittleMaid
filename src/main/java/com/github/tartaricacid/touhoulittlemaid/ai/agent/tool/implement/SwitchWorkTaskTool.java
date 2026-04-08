package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.IntegerParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.api.task.FunctionCallSwitchResult;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class SwitchWorkTaskTool implements ITool<SwitchWorkTaskTool.Result> {
    public static final String TOOL_ID = "switch_work_task";

    private static final String TOOL_DESC = """
            Use this when the user wants to change the current work task.
            
            For attack tasks, should first obtain the context of nearby entities, then provide the target entity id as parameter to switch immediately after switching task.
            Non attack tasks not need to provide entity id.
            
            Reply with the entity name ONLY, omit internal data (e.g., ID, distance).
            """.trim();

    private static final String TASK_ID_PARAMETER_ID = "task_id";
    private static final String ENTITY_ID_PARAMETER_ID = "entity_id";

    private static final String ENTITY_ID_PARAMETER_DESC = "Entity id of the attack target";

    private static final String SUCCESS = "Switched to task %s";
    private static final String NO_CHANGE = "Already on task %s";
    private static final String MISSING_REQUIRED = "Switched to task %s, but a required item is missing";
    private static final String PARTIAL = "Switched to task %s, but some requirements are missing";
    private static final String SCHEDULED = "Switched to task %s, but current scheduled is %s, not in work time";

    private static final String TARGET_NOT_PROVIDED = "The task switch succeeded, but no target entity id provided";
    private static final String TARGET_NOT_FOUND = "The task switch succeeded, but no living entity with id %d was found";
    private static final String TARGET_NOT_ALLOWED = "The task switch succeeded, but cannot attack %s because it is excluded by attack rules.";
    private static final String TARGET_SUCCESS = "The task switch succeeded, and the attack target is successfully set to %s";

    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf(TASK_ID_PARAMETER_ID).forGetter(Result::id),
            Codec.INT.optionalFieldOf(ENTITY_ID_PARAMETER_ID, -1).forGetter(Result::entityId)
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
        StringParameter taskId = StringParameter.create()
                .setDescription(this.getTaskIdParameterDesc());
        IntegerParameter entityId = IntegerParameter.create()
                .setDescription(ENTITY_ID_PARAMETER_DESC);

        List<IMaidTask> tasks = TaskManager.getTaskIndex();
        tasks.stream().map(IMaidTask::getUid)
                .map(ResourceLocation::toString)
                .forEach(taskId::addEnumValues);

        root.addProperties(TASK_ID_PARAMETER_ID, taskId);
        root.addProperties(ENTITY_ID_PARAMETER_ID, entityId, false);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, SwitchWorkTaskTool.Result result, LLMCallback callback) {
        ResourceLocation taskId = result.id;
        List<IMaidTask> tasks = TaskManager.getTaskIndex();
        Optional<IMaidTask> optional = TaskManager.findTask(taskId);

        if (optional.isEmpty()) {
            List<String> values = tasks.stream()
                    .map(IMaidTask::getUid)
                    .map(ResourceLocation::toString)
                    .toList();
            String text = "Unknown task_id '%s'".formatted(taskId);
            return callback.addToolResult(ITool.invalidParam(TASK_ID_PARAMETER_ID, values, text), toolId);
        }

        EntityMaid maid = callback.getMaid();
        IMaidTask task = optional.get();
        IMaidTask currentTask = maid.getTask();
        FunctionCallSwitchResult switchResult;
        int entityId = result.entityId();

        if (task != currentTask) {
            maid.setTask(task);
        }
        switchResult = task.onFunctionCallSwitch(maid);

        // 日程表检查
        Activity activity = maid.getScheduleDetail();
        if (activity != Activity.WORK) {
            String msg = SCHEDULED.formatted(taskId, maid.getSchedule().name());
            return callback.addToolResult(msg, toolId);
        }

        if (task instanceof IAttackTask attackTask) {
            String msg = this.attackResult(maid, attackTask, entityId);
            return callback.addToolResult(msg, toolId);
        } else {
            String msg = this.switchResult(taskId, task == currentTask, switchResult);
            return callback.addToolResult(msg, toolId);
        }
    }

    @Override
    public Component invocationSummaryComponent(Result result) {
        ResourceLocation id = result.id();
        return TaskManager.findTask(id).map(task -> {
            MutableComponent name = task.getName();
            return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_work_task", name)
                    .withStyle(ChatFormatting.GRAY);
        }).orElse(Component.empty());
    }

    private String switchResult(ResourceLocation taskId, boolean sameTask, FunctionCallSwitchResult switchResult) {
        if (sameTask) {
            return switch (switchResult) {
                case NO_CHANGE -> NO_CHANGE.formatted(taskId);
                case MISSING_REQUIRED_ITEM -> MISSING_REQUIRED.formatted(taskId);
                case PARTIAL_OK -> PARTIAL.formatted(taskId);
                case OK -> SUCCESS.formatted(taskId);
            };
        }
        return switch (switchResult) {
            case NO_CHANGE, OK -> SUCCESS.formatted(taskId);
            case MISSING_REQUIRED_ITEM -> MISSING_REQUIRED.formatted(taskId);
            case PARTIAL_OK -> PARTIAL.formatted(taskId);
        };
    }

    private String attackResult(EntityMaid maid, IAttackTask attackTask, int entityId) {
        if (entityId == -1) {
            return TARGET_NOT_PROVIDED;
        }

        Entity entity = maid.level.getEntity(entityId);
        if (!(entity instanceof LivingEntity target) || !target.isAlive()) {
            return TARGET_NOT_FOUND.formatted(entityId);
        }

        String targetName = target.getName().getString();
        LivingEntity previousTarget = maid.getLastHurtByMob();
        maid.setLastHurtByMob(target);

        if (!attackTask.canAttack(maid, target)) {
            maid.setLastHurtByMob(previousTarget);
            return TARGET_NOT_ALLOWED.formatted(targetName);
        }

        maid.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        return TARGET_SUCCESS.formatted(targetName);
    }

    private String getTaskIdParameterDesc() {
        StringJoiner joiner = new StringJoiner("\n", "Brief explanation of parameters: \n", "");
        TaskManager.getTaskIndex().forEach(task -> {
            String path = task.getUid().getPath();
            String summary = task.getMaidActionSummary();
            joiner.add("- %s: %s".formatted(path, summary));
        });
        return joiner.toString();
    }

    public record Result(ResourceLocation id, int entityId) {
    }
}
