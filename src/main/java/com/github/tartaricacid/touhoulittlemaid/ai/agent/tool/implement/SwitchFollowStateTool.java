package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.BoolParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class SwitchFollowStateTool implements ITool<SwitchFollowStateTool.Result> {
    public static final String TOOL_ID = "switch_maid_follow_state";

    private static final String TOOL_DESC = "Set the maid's follow or stay/home state. Use when the player asks the maid to follow, stay, wait, stop, or go home.";

    private static final String FOLLOW_PARAM_ID = "follow";
    private static final String FOLLOW_PARAM_DESC = """
            follow (boolean, required): true = follow the owner (e.g. "follow me", "come with me"); false = stop following and stay at the current position in home mode (e.g. "stay here", "don't move", "wait here", "stop following").
            """;

    private static final String SUCCESS_FOLLOW_ON = "Follow mode enabled";
    private static final String SUCCESS_FOLLOW_OFF = "Home mode enabled (stop following)";
    private static final String NO_CHANGE_FOLLOW_ON = "Already following the owner";
    private static final String NO_CHANGE_FOLLOW_OFF = "Already in home mode";

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
    public ToolResponse onCall(Result result, EntityMaid maid) {
        boolean toFollow = result.follow;
        boolean isHome = maid.isHomeModeEnable();
        if (toFollow) {
            if (!isHome) {
                return new ToolResponse(NO_CHANGE_FOLLOW_ON);
            }
            maid.restrictTo(BlockPos.ZERO, MaidConfig.MAID_NON_HOME_RANGE.get());
            maid.setHomeModeEnable(false);
            return new ToolResponse(SUCCESS_FOLLOW_ON);
        }

        if (isHome) {
            return new ToolResponse(NO_CHANGE_FOLLOW_OFF);
        }
        maid.getSchedulePos().setHomeModeEnable(maid, maid.blockPosition());
        maid.setHomeModeEnable(true);
        return new ToolResponse(SUCCESS_FOLLOW_OFF);
    }

    public record Result(boolean follow) {
    }
}
