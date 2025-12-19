package com.github.tartaricacid.touhoulittlemaid.ai.service.function.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.BoolParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class SwitchFollowStateFunction implements IFunctionCall<SwitchFollowStateFunction.Result> {
    private static final String FUNCTION_ID = "switch_maid_follow_state";
    private static final String FUNCTION_DESC = """
            Toggle the maid's follow state. Set follow=true to follow the owner (disable home mode),
            or follow=false to stay at home (enable home mode at current position).""";
    private static final String FOLLOW_PARAM_ID = "follow";
    private static final String FOLLOW_PARAM_DESC = """
            follow (boolean, required): true to follow the owner; false to stop following and enable home mode at current position.""";

    private static final String SUCCESS_FOLLOW_ON = "Follow mode enabled";
    private static final String SUCCESS_FOLLOW_OFF = "Home mode enabled (stop following)";
    private static final String NO_CHANGE_FOLLOW_ON = "Already following the owner";
    private static final String NO_CHANGE_FOLLOW_OFF = "Already in home mode";

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
        BoolParameter follow = BoolParameter.create();
        follow.setDescription(FOLLOW_PARAM_DESC);
        root.addProperties(FOLLOW_PARAM_ID, follow);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance ->
                instance.group(Codec.BOOL.fieldOf(FOLLOW_PARAM_ID).forGetter(Result::follow))
                        .apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        boolean toFollow = result.follow;
        boolean isHome = maid.isHomeModeEnable();
        if (toFollow) {
            if (!isHome) {
                return new ToolResponse(NO_CHANGE_FOLLOW_ON);
            }
            maid.restrictTo(BlockPos.ZERO, MaidConfig.MAID_NON_HOME_RANGE.get());
            maid.setHomeModeEnable(false);
            return new ToolResponse(SUCCESS_FOLLOW_ON);
        } else {
            if (isHome) {
                return new ToolResponse(NO_CHANGE_FOLLOW_OFF);
            }
            maid.getSchedulePos().setHomeModeEnable(maid, maid.blockPosition());
            maid.setHomeModeEnable(true);
            return new ToolResponse(SUCCESS_FOLLOW_OFF);
        }
    }

    public record Result(boolean follow) {
    }
}
