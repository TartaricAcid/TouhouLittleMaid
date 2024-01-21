package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.base.Predicates;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class TaskMelon implements IFarmTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "melon");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.MELON_SLICE.getDefaultInstance();
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        if (block == Blocks.MELON || block == Blocks.PUMPKIN) {
            Block stem = block == Blocks.MELON ? Blocks.ATTACHED_MELON_STEM : Blocks.ATTACHED_PUMPKIN_STEM;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState offsetState = maid.level.getBlockState(cropPos.relative(direction));
                if (offsetState.is(stem)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        ItemStack mainHandItem = maid.getMainHandItem();
        if (cropState.is(Blocks.MELON) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, mainHandItem) > 0) {
            if (maid.destroyBlock(cropPos, false)) {
                mainHandItem.hurtAndBreak(1, maid, (e) -> e.broadcastBreakEvent(Hand.MAIN_HAND));
                Block.popResource(maid.level, cropPos, Items.MELON.getDefaultInstance());
            }
        } else {
            maid.destroyBlock(cropPos);
        }
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return seed;
    }

    @Override
    public double getCloseEnoughDist() {
        return 1.5;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Collections.singletonList(Pair.of("has_silk_touch", Predicates.alwaysTrue()));
    }
}
