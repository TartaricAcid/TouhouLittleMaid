package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.variable;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util.MathUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class MoveInputVariable {
    public static double getVertical(IContext<Entity> context) {
        Entity entity = context.entity();
        float partialTick = context.animationEvent().getPartialTick();

        // 求出当前移动的水平分量
        double x = Mth.lerp(partialTick, entity.xo, entity.getX()) - entity.xo;
        double z = Mth.lerp(partialTick, entity.zo, entity.getZ()) - entity.zo;

        // 如果移动数值过小，那么认为没有一点，返回 0
        if (Math.sqrt(x * x + z * z) < 1.0E-4) {
            return 0;
        }

        // 计算移动角度和实体偏航角度，并作差计算出相对角度
        float moveAngleDeg = MathUtil.radiansToDegrees((float) Mth.atan2(z, x));
        float entityYawDeg = 90 - Mth.wrapDegrees(-entity.getViewYRot(partialTick));
        float relativeAnglesDeg = Mth.wrapDegrees(moveAngleDeg - entityYawDeg);

        return Mth.cos(MathUtil.degreesToRadians(relativeAnglesDeg));
    }

    public static double getHorizontal(IContext<Entity> context) {
        Entity entity = context.entity();
        float partialTick = context.animationEvent().getPartialTick();

        // 求出当前移动的水平分量
        double x = Mth.lerp(partialTick, entity.xo, entity.getX()) - entity.xo;
        double z = Mth.lerp(partialTick, entity.zo, entity.getZ()) - entity.zo;

        // 如果移动数值过小，那么认为没有一点，返回 0
        if (Math.sqrt(x * x + z * z) < 1.0E-4) {
            return 0;
        }

        // 计算移动角度和实体偏航角度，并作差计算出相对角度
        float moveAngleDeg = MathUtil.radiansToDegrees((float) Mth.atan2(z, x));
        float entityYawDeg = 90 - Mth.wrapDegrees(-entity.getViewYRot(partialTick));
        float relativeAnglesDeg = Mth.wrapDegrees(moveAngleDeg - entityYawDeg);

        return Mth.sin(MathUtil.degreesToRadians(relativeAnglesDeg));
    }
}
