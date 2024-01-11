package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IForgeShearable;

import java.util.List;
import java.util.Random;

public class MaidShearTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final float speedModifier;
    private LivingEntity shearableEntity = null;

    public MaidShearTask(float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        this.speedModifier = speedModifier;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        ItemStack mainhandItem = maid.getMainHandItem();
        shearableEntity = null;
        int maxDistToWalk = (int) maid.getRestrictRadius();

        this.getEntities(maid).stream()
                .filter(e -> e.closerThan(maid, maxDistToWalk))
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof IForgeShearable)
                .filter(e -> ((IForgeShearable) e).isShearable(mainhandItem, maid.level, e.blockPosition()))
                .filter(maid::canPathReach)
                .findFirst()
                .ifPresent(e -> {
                    shearableEntity = e;
                    BrainUtil.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0);
                });

        if (shearableEntity != null && shearableEntity.closerThan(maid, 2)) {
            Random rand = maid.getRandom();
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, mainhandItem);
            List<ItemStack> drops = ((IForgeShearable) shearableEntity).onSheared(null, mainhandItem,
                    maid.level, shearableEntity.blockPosition(), level);
            drops.forEach(stack -> {
                ItemEntity itemEntity = shearableEntity.spawnAtLocation(stack, 1.0F);
                if (itemEntity != null) {
                    itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F,
                            rand.nextFloat() * 0.05F,
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F));
                }
            });
            maid.swing(Hand.MAIN_HAND);
            mainhandItem.hurtAndBreak(1, maid, (entityMaid) -> entityMaid.broadcastBreakEvent(Hand.MAIN_HAND));
            shearableEntity = null;
        }
    }

    private List<LivingEntity> getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Lists.newArrayList());
    }
}
