package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.List;

public class SwitchMaidScheduleTool implements ITool<String> {
    public static final String TOOL_ID = "switch_maid_schedule";

    private static final String TOOL_DESC = "Switch the maid's work schedule.";

    private static final String SCHEDULE_PARAM_ID = "schedule";
    private static final String SCHEDULE_PARAM_DESC = "The schedule mode: DAY, NIGHT, ALL.";


    private static final Codec<String> CODEC = Codec.STRING.fieldOf(SCHEDULE_PARAM_ID).codec();

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
        StringParameter schedule = StringParameter.create();
        schedule.setDescription(SCHEDULE_PARAM_DESC);
        Arrays.stream(MaidSchedule.values()).map(Enum::name).forEach(schedule::addEnumValues);
        root.addProperties(SCHEDULE_PARAM_ID, schedule);
        return root;
    }

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, String result, LLMCallback callback) {
        MaidSchedule target;
        try {
            target = MaidSchedule.valueOf(result.toUpperCase());
        } catch (IllegalArgumentException e) {
            List<String> values = Arrays.stream(MaidSchedule.values()).map(Enum::name).toList();
            String text = "unknown schedule '%s'".formatted(result);
            return callback.addToolResult(ITool.invalidParam(SCHEDULE_PARAM_ID, values, text), toolId);
        }

        EntityMaid maid = callback.getMaid();
        MaidSchedule current = maid.getSchedule();
        if (current == target) {
            return callback.addToolResult("Already on %s schedule".formatted(target.name()), toolId);
        }

        maid.setSchedule(target);
        return callback.addToolResult("Schedule switched to %s".formatted(target.name()), toolId);
    }
}
