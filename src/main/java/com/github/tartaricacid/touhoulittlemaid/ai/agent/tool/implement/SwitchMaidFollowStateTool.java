package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.BoolParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class SwitchMaidFollowStateTool implements ITool<SwitchMaidFollowStateTool.Result> {
    public static final String TOOL_ID = "switch_maid_follow_state";

    private static final String TOOL_DESC = "Toggle the maid's follow/home mode.";

    private static final String FOLLOW_PARAM_ID = "follow";
    private static final String FOLLOW_PARAM_DESC = "true = follow the owner; false = stay at current position (home mode).";

    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.BOOL.fieldOf(FOLLOW_PARAM_ID).forGetter(Result::follow))
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
        BoolParameter follow = BoolParameter.create();
        follow.setDescription(FOLLOW_PARAM_DESC);
        root.addProperties(FOLLOW_PARAM_ID, follow);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, SwitchMaidFollowStateTool.Result result, LLMCallback callback) {
        EntityMaid maid = callback.getMaid();
        boolean toFollow = result.follow;
        boolean isHome = maid.isHomeModeEnable();
        if (toFollow) {
            if (!isHome) {
                return callback.addToolResult("Already following the owner", toolId);
            }
            maid.restrictTo(BlockPos.ZERO, MaidConfig.MAID_NON_HOME_RANGE.get());
            maid.setHomeModeEnable(false);
            return callback.addToolResult("Follow mode enabled", toolId);
        }

        if (isHome) {
            return callback.addToolResult("Already in home mode", toolId);
        }
        maid.getSchedulePos().setHomeModeEnable(maid, maid.blockPosition());
        maid.setHomeModeEnable(true);
        return callback.addToolResult("Home mode enabled (stop following)", toolId);
    }

    public record Result(boolean follow) {
    }
}
