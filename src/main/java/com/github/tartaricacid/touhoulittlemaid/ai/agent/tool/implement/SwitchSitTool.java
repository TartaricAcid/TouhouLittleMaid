package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.BoolParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SwitchSitTool implements ITool<SwitchSitTool.Result> {
    public static final String TOOL_ID = "switch_sit";

    private static final String TOOL_DESC = """
            Use this when the user wants the maid to sit or stand.
            Set sit=true to sit. Set sit=false to stand.
            Do not use this to control follow mode.
            """.trim();

    private static final String SIT_PARAM_ID = "sit";
    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.BOOL.fieldOf(SIT_PARAM_ID).forGetter(Result::sit))
                    .apply(instance, Result::new));

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
        BoolParameter sit = BoolParameter.create();
        root.addProperties(SIT_PARAM_ID, sit);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, SwitchSitTool.Result result, LLMCallback callback) {
        EntityMaid maid = callback.getMaid();
        boolean toSit = result.sit;
        boolean isSitting = maid.isMaidInSittingPose();

        if (toSit) {
            if (isSitting) {
                return callback.addToolResult("Already sitting", toolId);
            }
            maid.setInSittingPose(true);
            return callback.addToolResult("Success sitting", toolId);
        }

        if (!isSitting) {
            return callback.addToolResult("Already standing", toolId);
        }
        maid.setInSittingPose(false);
        return callback.addToolResult("Success standing", toolId);
    }

    @Override
    public Component invocationSummaryComponent(SwitchSitTool.Result result) {
        if (result.sit()) {
            return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_sit.yes")
                    .withStyle(ChatFormatting.GRAY);
        } else {
            return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_sit.no")
                    .withStyle(ChatFormatting.GRAY);
        }
    }

    public record Result(boolean sit) {
    }
}
