package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class SwitchScheduleTool implements ITool<String> {
    public static final String TOOL_ID = "switch_schedule";

    private static final String TOOL_DESC = """
            Use this when user wants to change the schedule.
            Before using this tool, Should first obtain the context of game time and self schedule.
            """.trim();

    private static final String SCHEDULE_PARAM_ID = "schedule";
    private static final String SCHEDULE_PARAM_DESC = """
            - DAY: 06:00 ~ 18:00 Work, 18:00 ~ 22:00 Leisure, 22:00 ~ 06:00 Rest
            - NIGHT: 18:00 ~ 06:00 Work, 06:00 ~ 14:00 Rest, 14:00 ~ 18:00 Leisure
            - ALL: 00:00 ~ 24:00 Work
            Choose one of the enum values exposed in this schema.
            """.trim();


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
            String text = "Unknown schedule '%s'".formatted(result);
            return callback.addToolResult(ITool.invalidParam(SCHEDULE_PARAM_ID, values, text), toolId);
        }

        EntityMaid maid = callback.getMaid();
        MaidSchedule current = maid.getSchedule();
        if (current == target) {
            return callback.addToolResult("Already on %s schedule.".formatted(target.name()), toolId);
        }

        maid.setSchedule(target);
        return callback.addToolResult("Schedule switched to %s.".formatted(target.name()), toolId);
    }

    @Override
    public Component invocationSummaryComponent(String result) {
        MaidSchedule target = MaidSchedule.valueOf(result.toUpperCase());
        switch (target) {
            case DAY -> {
                return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_schedule.day")
                        .withStyle(ChatFormatting.GRAY);
            }
            case NIGHT -> {
                return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_schedule.night")
                        .withStyle(ChatFormatting.GRAY);
            }
            default -> {
                return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_schedule.all")
                        .withStyle(ChatFormatting.GRAY);
            }
        }
    }
}
