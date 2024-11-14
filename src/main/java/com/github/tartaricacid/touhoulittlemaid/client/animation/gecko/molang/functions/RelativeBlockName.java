package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity.EntityFunction;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class RelativeBlockName extends EntityFunction {
    @Override
    protected Object eval(ExecutionContext<IContext<Entity>> ctx, ArgumentCollection arguments) {
        Entity entity = ctx.entity().entity();

        int offsetX = arguments.getAsInt(ctx, 0);
        int offsetY = arguments.getAsInt(ctx, 1);
        int offsetZ = arguments.getAsInt(ctx, 2);
        if (Math.abs(offsetX) > 8 || Math.abs(offsetY) > 8 || Math.abs(offsetZ) > 8) {
            return false;
        }

        BlockPos pos = new BlockPos((int) entity.getX() + offsetX,
                (int) entity.getY() + offsetY,
                (int) entity.getZ() + offsetZ);
        BlockState block = ctx.entity().entity().level().getBlockState(pos);
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block.getBlock());
        if (blockId == null) {
            return null;
        }

        return blockId.toString();
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 3;
    }
}
