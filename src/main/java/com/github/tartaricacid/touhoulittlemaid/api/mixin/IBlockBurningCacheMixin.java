package com.github.tartaricacid.touhoulittlemaid.api.mixin;

public interface IBlockBurningCacheMixin {
    Boolean touhou_little_maid$isBurning();
    void touhou_little_maid$setBurning(boolean isBurning);
    Boolean touhou_little_maid$cannotCache();
    void touhou_little_maid$setCannotCache(boolean cannotCache);
}
