package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.IBlockBurningCacheMixin;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
@SuppressWarnings("all")
public abstract class BlockBurningCache extends BlockBehaviour implements ItemLike, IBlockExtension, IBlockBurningCacheMixin {
    @Unique
    public Boolean touhou_little_maid$isBurning = null;
    @Unique
    public Boolean touhou_little_maid$cannotCache = null;

    public BlockBurningCache(Properties properties) {
        super(properties);
    }

    @Override
    public void touhou_little_maid$setBurning(boolean isBurning) {
        touhou_little_maid$isBurning = isBurning;
    }

    @Override
    public Boolean touhou_little_maid$isBurning() {
        return touhou_little_maid$isBurning;
    }

    @Override
    public void touhou_little_maid$setCannotCache(boolean cannotCache) {
        touhou_little_maid$cannotCache = cannotCache;
    }

    @Override
    public Boolean touhou_little_maid$cannotCache() {
        return touhou_little_maid$cannotCache;
    }
}
