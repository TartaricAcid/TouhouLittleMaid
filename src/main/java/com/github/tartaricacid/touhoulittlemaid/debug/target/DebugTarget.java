package com.github.tartaricacid.touhoulittlemaid.debug.target;

import net.minecraft.core.BlockPos;
import net.minecraft.util.VisibleForDebug;

/**
 * 添加调试点
 *
 * @param pos      坐标
 * @param color    颜色
 * @param text     显示的文字
 * @param lifeTime 生命时间
 */
@VisibleForDebug
public record DebugTarget(BlockPos pos, int color, String text, int lifeTime) {
}