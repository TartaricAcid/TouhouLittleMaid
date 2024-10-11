package com.github.tartaricacid.touhoulittlemaid.api.entity.fishing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IFishingType {
    /**
     * 是否持有合适的鱼竿
     */
    boolean isFishingRod(ItemStack itemStack);

    /**
     * 检查该方块是否适合放置钓鱼钩
     */
    default boolean suitableFishingHook(EntityMaid maid, Level worldIn, ItemStack rod, BlockPos blockPos) {
        return worldIn.getFluidState(blockPos).is(FluidTags.WATER);
    }

    /**
     * 获取鱼钩实体
     */
    MaidFishingHook getFishingHook(EntityMaid maid, Level level, ItemStack itemStack, Vec3 pos);
}
