package com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing;

import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;

public class DefaultFishingType implements IFishingType {
    @Override
    public boolean isFishingRod(ItemStack itemStack) {
        return itemStack.canPerformAction(ToolActions.FISHING_ROD_CAST);
    }

    @Override
    public MaidFishingHook getFishingHook(EntityMaid maid, Level level, ItemStack itemStack, Vec3 pos) {
        int lureSpeedBonus = EnchantmentHelper.getFishingSpeedBonus(itemStack);
        int luckBonus = EnchantmentHelper.getFishingLuckBonus(itemStack);
        return new MaidFishingHook(maid, maid.level, luckBonus, lureSpeedBonus, pos);
    }
}
