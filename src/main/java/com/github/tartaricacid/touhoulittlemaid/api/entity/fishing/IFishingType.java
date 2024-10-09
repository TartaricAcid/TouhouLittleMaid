package com.github.tartaricacid.touhoulittlemaid.api.entity.fishing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IFishingType {
    boolean isFishingRod(ItemStack itemStack);

    MaidFishingHook getFishingHook(EntityMaid maid, Level level, ItemStack itemStack, Vec3 pos);
}
