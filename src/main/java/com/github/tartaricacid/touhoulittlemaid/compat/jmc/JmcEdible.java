package com.github.tartaricacid.touhoulittlemaid.compat.jmc;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.data.SerializableMobEffectInstance;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.util.CakeUtil;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock.belowIsSnackStand;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

public class JmcEdible implements IMaidEdibleBlock {
    /**
     * Just More Cakes! 模组中的蛋糕标签
     */
    private static final TagKey<Block> BLOCK_JMC_CAKES = TagBlock.createTagKey(new ResourceLocation(JmcCompat.ID, "cakes"));
    private static final TagKey<Item> ITEM_JMC_CAKES = TagItem.createTagKey(new ResourceLocation(JmcCompat.ID, "cakes"));

    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos pos, BlockState state) {
        if (state.is(BLOCK_JMC_CAKES)) {
            return belowIsSnackStand(maid, pos);
        }
        return false;
    }

    @Override
    public int getFavorabilityPoints(EntityMaid maid, BlockPos pos, BlockState state) {
        return 1;
    }

    @Override
    public boolean consume(EntityMaid maid, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof BaseCakeBlock baseCake)) {
            return false;
        }

        if (!baseCake.hasBites()) {
            // 黑曜石蛋糕，吃不了还掉血
            maid.hurt(maid.damageSources().generic(), 2.0F);
            maid.playSound(SoundEvents.GENERIC_EAT);
            return false;
        }

        Level level = maid.level;
        var half = BaseThreeTieredCandleCakeBlock.HALF;

        // 优先判断三层蛋糕
        if (baseCake instanceof BaseThreeTieredCakeBlock threeTieredCake && state.getValue(half) == LOWER) {
            BlockPos above = pos.above();
            BlockState aboveState = level.getBlockState(above);

            if (aboveState.is(threeTieredCake) && aboveState.getValue(half) == UPPER) {
                // 不太懂，反正是 JMC 里三层蛋糕的特殊逻辑
                IntegerProperty property = threeTieredCake.getBites();
                if (aboveState.getValue(property) >= 4) {
                    aboveState = aboveState.setValue(property, threeTieredCake.getSlices());
                }

                eatCake(maid, above, aboveState, baseCake);
                return true;
            }
        }

        // 否则正常吃
        eatCake(maid, pos, state, baseCake);
        return true;
    }

    private void eatCake(EntityMaid maid, BlockPos pos, BlockState state, BaseCakeBlock baseCake) {
        Level level = maid.level;
        ItemStack stack = baseCake.getCloneItemStack(level, pos, state);

        int maxBites = baseCake.getSlices();
        IntegerProperty bitesProperty = Objects.requireNonNull(baseCake.getBites());
        int bites = state.getValue(bitesProperty);

        // 施加特殊效果
        if (CakeUtil.inFamily(state, ModBlocks.FIREY_CAKE_FAMILY)) {
            maid.setSecondsOnFire(ModCommonConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
        } else if (CakeUtil.inFamily(state, ModBlocks.ICE_CAKE_FAMILY)) {
            maid.clearFire();
        } else if (CakeUtil.inFamily(state, ModBlocks.CHORUS_CAKE_FAMILY)) {
            maid.playSound(SoundEvents.ENDERMAN_TELEPORT);
            Util.teleportRandomly(maid, ModCommonConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
            maid.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        } else if (CakeUtil.inFamily(state, ModBlocks.ENDER_CAKE_FAMILY)) {
            Util.teleportRandomly(maid, ModCommonConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
            maid.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }

        // 施加药水效果
        CakeEffects effects = baseCake.justMoreCakes$getCakeEffects();
        if (effects != null) {
            for (SerializableMobEffectInstance effectInstance : effects.mobEffects()) {
                Util.applyEffectFromInstance(effectInstance, maid);
            }
        }

        if (bites < maxBites) {
            int currentBites = Math.min(bites + 1, maxBites);
            level.setBlock(pos, state.setValue(bitesProperty, currentBites), Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
        }

        maid.spawnItemParticles(stack, 8);
        maid.playSound(SoundEvents.GENERIC_EAT);
    }

    @Override
    public boolean canPlaceAsFood(EntityMaid maid, ItemStack stack, int slotIndex) {
        return stack.is(ITEM_JMC_CAKES);
    }
}
