package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.BoolParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SwitchMaidSitTool implements ITool<SwitchMaidSitTool.Result> {
    public static final String TOOL_ID = "switch_maid_sit";

    private static final String TOOL_DESC = "Toggle the maid's sitting posture.";

    private static final String SIT_PARAM_ID = "sit";
    private static final String SIT_PARAM_DESC = "true = sit down; false = stand up.";

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
        sit.setDescription(SIT_PARAM_DESC);
        root.addProperties(SIT_PARAM_ID, sit);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, SwitchMaidSitTool.Result result, LLMCallback callback) {
        EntityMaid maid = callback.getMaid();
        boolean toSit = result.sit;
        boolean isSitting = maid.isMaidInSittingPose();

        if (toSit) {
            if (isSitting) {
                return callback.addToolResult("Already sitting", toolId);
            }
            maid.setInSittingPose(true);
            return callback.addToolResult("Maid is now sitting", toolId);
        }

        if (!isSitting) {
            return callback.addToolResult("Already standing", toolId);
        }
        maid.setInSittingPose(false);
        return callback.addToolResult("Maid is now standing", toolId);
    }

    public record Result(boolean sit) {
    }
}
