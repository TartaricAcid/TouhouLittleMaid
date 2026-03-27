package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement.MaidWorkSkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
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
    public ToolResponse onCall(String result, EntityMaid maid) {
        MaidSchedule target;
        try {
            target = MaidSchedule.valueOf(result.toUpperCase());
        } catch (IllegalArgumentException e) {
            List<String> values = Arrays.stream(MaidSchedule.values()).map(Enum::name).toList();
            String text = "unknown schedule '%s'".formatted(result);
            return ToolResponse.invalidParam(SCHEDULE_PARAM_ID, values, text, MaidWorkSkill.ID);
        }

        MaidSchedule current = maid.getSchedule();
        if (current == target) {
            return new ToolResponse("Already on %s schedule".formatted(target.name()));
        }

        maid.setSchedule(target);
        return new ToolResponse("Schedule switched to %s".formatted(target.name()));
    }
}
