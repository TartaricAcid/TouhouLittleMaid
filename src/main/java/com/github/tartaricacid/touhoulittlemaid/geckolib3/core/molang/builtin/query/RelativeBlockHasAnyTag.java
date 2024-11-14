package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.query;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity.EntityFunction;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.MolangUtils;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class RelativeBlockHasAnyTag extends EntityFunction {
    @Override
    protected Object eval(ExecutionContext<IContext<Entity>> ctx, ArgumentCollection arguments) {
        Entity entity = ctx.entity().entity();

        int offsetX = arguments.getAsInt(ctx, 0);
        int offsetY = arguments.getAsInt(ctx, 1);
        int offsetZ = arguments.getAsInt(ctx, 2);
        if(Math.abs(offsetX) > 8 || Math.abs(offsetY) > 8 || Math.abs(offsetZ) > 8) {
            return false;
        }

        BlockState block = ctx.entity().entity().level().getBlockState(entity.blockPosition());

        for (int i = 3; i < arguments.size(); i++) {
            ResourceLocation tagId = MolangUtils.parseResourceLocation(ctx.entity(), arguments.getAsString(ctx, i));
            if(tagId == null) {
                return null;
            }

            TagKey<Block> tag = ForgeRegistries.BLOCKS.tags().createTagKey(tagId);
            if (block.is(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size >= 4;
    }
}
