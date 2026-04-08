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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class SwitchFollowStateTool implements ITool<SwitchFollowStateTool.Result> {
    public static final String TOOL_ID = "switch_follow_state";

    private static final String TOOL_DESC = """
            Use this when the user wants to start you following or stop here.
            Set follow=true to following the user, set follow=false to stop following.
            Do not use this to make the maid sit down.
            """.trim();

    private static final String FOLLOW_PARAM_ID = "follow";

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
        root.addProperties(FOLLOW_PARAM_ID, follow);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolId, SwitchFollowStateTool.Result result, LLMCallback callback) {
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
            return callback.addToolResult("Already stop following", toolId);
        }
        maid.getSchedulePos().setHomeModeEnable(maid, maid.blockPosition());
        maid.setHomeModeEnable(true);
        return callback.addToolResult("Flow mode disabled", toolId);
    }

    @Override
    public Component invocationSummaryComponent(SwitchFollowStateTool.Result result) {
        if (result.follow()) {
            return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_follow_state.yes")
                    .withStyle(ChatFormatting.GRAY);
        } else {
            return Component.translatable("ai.touhou_little_maid.chat.tool_call.switch_follow_state.no")
                    .withStyle(ChatFormatting.GRAY);
        }
    }

    public record Result(boolean follow) {
    }
}
