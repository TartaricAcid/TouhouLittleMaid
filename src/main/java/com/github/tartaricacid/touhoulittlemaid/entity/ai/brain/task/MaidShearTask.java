package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.IForgeShearable;

import java.util.List;

public class MaidShearTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final int maxDistToWalk;
    private final float speedModifier;
    private LivingEntity shearableEntity = null;

    public MaidShearTask(int maxDistToWalk, float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.maxDistToWalk = maxDistToWalk;
        this.speedModifier = speedModifier;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        ItemStack mainHandItem = maid.getMainHandItem();
        shearableEntity = null;

        if (!(mainHandItem.getItem() instanceof ShearsItem)) {
            return;
        }

        this.getEntities(maid)
                .find(e -> e.closerThan(maid, maxDistToWalk))
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof IForgeShearable)
                .filter(e -> ((IForgeShearable) e).isShearable(mainHandItem, maid.level(), e.blockPosition()))
                .filter(maid::canPathReach)
                .findFirst()
                .ifPresent(e -> {
                    shearableEntity = e;
                    BehaviorUtils.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0);
                });

        if (shearableEntity != null && shearableEntity.closerThan(maid, 2)) {
            RandomSource rand = maid.getRandom();
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, mainHandItem);
            List<ItemStack> drops = ((IForgeShearable) shearableEntity).onSheared(null, mainHandItem,
                    maid.level(), shearableEntity.blockPosition(), level);
            drops.forEach(stack -> {
                ItemEntity itemEntity = shearableEntity.spawnAtLocation(stack, 1.0F);
                if (itemEntity != null) {
                    itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F,
                            rand.nextFloat() * 0.05F,
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F));
                }
            });
            maid.swing(InteractionHand.MAIN_HAND);
            mainHandItem.hurtAndBreak(1, maid, (entityMaid) -> entityMaid.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            shearableEntity = null;
        }
    }

    private NearestVisibleLivingEntities getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }
}
