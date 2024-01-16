package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SnowballItem;

import java.util.Optional;

public class MaidSnowballTargetTask extends Behavior<EntityMaid> {
    private static final float CHANCE_STOPPING = 1 / 32F;
    private final int attackCooldown;
    private boolean canThrow = false;
    private int attackTime = -1;

    public MaidSnowballTargetTask(int attackCooldown) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
        this.attackCooldown = attackCooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return owner.isHolding(item -> item.getItem() instanceof SnowballItem || item.isEmpty()) && BehaviorUtils.canSee(owner, target) && inMaxDistance(owner);
        }
        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        return chanceStop(entityIn) && entityIn.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && isCurrentTargetInSameLevel(entityIn) && isCurrentTargetAlive(entityIn) && this.checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        if (entityIn.getMainHandItem().isEmpty()) {
            entityIn.setItemInHand(InteractionHand.MAIN_HAND, Items.SNOWBALL.getDefaultInstance());
            return;
        }
        if (!(entityIn.getMainHandItem().getItem() instanceof SnowballItem) && entityIn.getOffhandItem().isEmpty()) {
            entityIn.setItemInHand(InteractionHand.OFF_HAND, Items.SNOWBALL.getDefaultInstance());
        }
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            boolean canSee = BehaviorUtils.canSee(owner, target);
            if (canThrow && canSee) {
                canThrow = false;
                if (owner.getMainHandItem().getItem() instanceof SnowballItem) {
                    owner.swing(InteractionHand.MAIN_HAND);
                } else {
                    owner.swing(InteractionHand.OFF_HAND);
                }
                BehaviorUtils.lookAtEntity(owner, target);
                performRangedAttack(owner, target);
                this.attackTime = this.attackCooldown + owner.getRandom().nextInt(this.attackCooldown);
            } else if (--this.attackTime <= 0) {
                this.canThrow = true;
            }
        });
    }

    private void performRangedAttack(EntityMaid shooter, LivingEntity target) {
        Snowball snowball = new Snowball(shooter.level, shooter);
        double x = target.getX() - shooter.getX();
        double y = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - snowball.position().y;
        double z = target.getZ() - shooter.getZ();
        double pitch = Math.sqrt(x * x + z * z) * 0.15D;
        snowball.shoot(x, y + pitch, z, 1.6F, 1);
        shooter.playSound(SoundEvents.SNOWBALL_THROW, 0.5F, 0.4F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
        shooter.level.addFreshEntity(snowball);
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        this.attackTime = -1;
        this.canThrow = false;
        clearAttackTarget(entityIn);
    }

    private boolean isCurrentTargetInSameLevel(LivingEntity entity) {
        Optional<LivingEntity> optional = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional.isPresent() && optional.get().level == entity.level;
    }

    private boolean isCurrentTargetAlive(LivingEntity entity) {
        Optional<LivingEntity> optional = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional.isPresent() && optional.get().isAlive();
    }

    private boolean inMaxDistance(EntityMaid entity) {
        int maxStopAttackDistance = (int) entity.getRestrictRadius();
        Optional<LivingEntity> optional = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional.isPresent() && entity.distanceTo(optional.get()) <= maxStopAttackDistance;
    }

    private boolean chanceStop(LivingEntity entity) {
        return entity.getRandom().nextFloat() > CHANCE_STOPPING;
    }

    private void clearAttackTarget(LivingEntity entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
    }
}