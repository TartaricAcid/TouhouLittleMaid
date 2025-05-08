package com.github.tartaricacid.touhoulittlemaid.debug.target;
import net.minecraft.core.BlockPos;
public record DebugTarget(BlockPos pos, int color, String text) {
}