package com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing;

import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;

public class DefaultFishingType implements IFishingType {
    @Override
    public boolean isFishingRod(ItemStack itemStack) {
        return itemStack.canPerformAction(ItemAbilities.FISHING_ROD_CAST);
    }

    @Override
    public MaidFishingHook getFishingHook(EntityMaid maid, Level level, ItemStack itemStack, Vec3 pos) {
        int lureSpeedBonus = (int) (EnchantmentHelper.getFishingTimeReduction((ServerLevel) level, itemStack, maid) * 20.0F);
        int luckBonus = EnchantmentHelper.getFishingLuckBonus((ServerLevel) level, itemStack, maid);
        return new MaidFishingHook(maid, maid.level, luckBonus, lureSpeedBonus, pos);
    }
}