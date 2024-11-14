package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.variable;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.IValueEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class LadderFacingVariable implements IValueEvaluator<Integer, IContext<LivingEntity>> {
    @Override
    public Integer eval(IContext<LivingEntity> ctx) {
        Optional<BlockPos> climbablePos = ctx.entity().getLastClimbablePos();
        if (climbablePos.isPresent()) {
            BlockState blockState = ctx.entity().level().getBlockState(climbablePos.get());
            Optional<Direction> optionalValue = blockState.getOptionalValue(HorizontalDirectionalBlock.FACING);
            if (optionalValue.isPresent()) {
                // 输出数字 0-3，分别对应：南-西-北-东
                return optionalValue.get().get2DDataValue();
            }
        }
        return 0;
    }
}
