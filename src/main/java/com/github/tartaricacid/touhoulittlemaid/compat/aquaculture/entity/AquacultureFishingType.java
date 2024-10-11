package com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.entity;

import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import com.teammetallurgy.aquaculture.item.BaitItem;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class AquacultureFishingType implements IFishingType {
    @Override
    public boolean isFishingRod(ItemStack itemStack) {
        return itemStack.getItem() instanceof AquaFishingRodItem;
    }

    @Override
    public boolean suitableFishingHook(EntityMaid maid, Level worldIn, ItemStack rod, BlockPos blockPos) {
        FluidState fluidState = worldIn.getFluidState(blockPos);
        if (fluidState.is(FluidTags.LAVA)) {
            Hook hook = AquaFishingRodItem.getHookType(rod);
            return hook.getFluids().contains(FluidTags.LAVA);
        }
        return fluidState.is(FluidTags.WATER);
    }

    @Override
    public MaidFishingHook getFishingHook(EntityMaid maid, Level level, ItemStack rod, Vec3 pos) {
        int lureSpeed = EnchantmentHelper.getFishingSpeedBonus(rod);
        Tier tier = Tiers.WOOD;
        if (rod.getItem() instanceof AquaFishingRodItem aquaFishingRodItem) {
            tier = aquaFishingRodItem.getTier();
        }
        if (tier == AquacultureAPI.MATS.NEPTUNIUM) {
            lureSpeed += 1;
        }
        ItemStack bait = AquaFishingRodItem.getBait(rod);
        if (!bait.isEmpty()) {
            lureSpeed += ((BaitItem) bait.getItem()).getLureSpeedModifier();
        }
        lureSpeed = Math.min(5, lureSpeed);
        int luck = EnchantmentHelper.getFishingLuckBonus(rod);
        Hook hook = AquaFishingRodItem.getHookType(rod);
        if (hook != Hooks.EMPTY && hook.getLuckModifier() > 0) {
            luck += hook.getLuckModifier();
        }
        ItemStack fishingLine = AquaFishingRodItem.getFishingLine(rod);
        ItemStack bobber = AquaFishingRodItem.getBobber(rod);
        return new AquacultureFishingHook(maid, level, luck, lureSpeed, pos, hook, fishingLine, bobber, rod);
    }
}
