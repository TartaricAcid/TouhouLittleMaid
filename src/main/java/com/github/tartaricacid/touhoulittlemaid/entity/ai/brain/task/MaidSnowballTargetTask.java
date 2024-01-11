package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Items;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MaidSnowballTargetTask extends Task<EntityMaid> {
    private static final float CHANCE_STOPPING = 1 / 32F;
    private final int attackCooldown;
    private boolean canThrow = false;
    private int attackTime = -1;

    public MaidSnowballTargetTask(int attackCooldown) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
        this.attackCooldown = attackCooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return owner.isHolding(item -> item instanceof SnowballItem || item == Items.AIR)
                    && BrainUtil.canSee(owner, target)
                    && inMaxDistance(owner);
        }
        return false;
    }

    @Override
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        return chanceStop(entityIn)
                && entityIn.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)
                && isCurrentTargetInSameLevel(entityIn)
                && isCurrentTargetAlive(entityIn)
                && this.checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        if (entityIn.getMainHandItem().isEmpty()) {
            entityIn.setItemInHand(Hand.MAIN_HAND, Items.SNOWBALL.getDefaultInstance());
            return;
        }
        if (!(entityIn.getMainHandItem().getItem() instanceof SnowballItem) && entityIn.getOffhandItem().isEmpty()) {
            entityIn.setItemInHand(Hand.OFF_HAND, Items.SNOWBALL.getDefaultInstance());
        }
    }

    @Override
    protected void tick(ServerWorld worldIn, EntityMaid owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            boolean canSee = BrainUtil.canSee(owner, target);
            if (canThrow && canSee) {
                canThrow = false;
                if (owner.getMainHandItem().getItem() instanceof SnowballItem) {
                    owner.swing(Hand.MAIN_HAND);
                } else {
                    owner.swing(Hand.OFF_HAND);
                }
                BrainUtil.lookAtEntity(owner, target);
                performRangedAttack(owner, target);
                this.attackTime = this.attackCooldown + owner.getRandom().nextInt(this.attackCooldown);
            } else if (--this.attackTime <= 0) {
                this.canThrow = true;
            }
        });
    }

    private void performRangedAttack(EntityMaid shooter, LivingEntity target) {
        SnowballEntity snowball = new SnowballEntity(shooter.level, shooter);
        double x = target.getX() - shooter.getX();
        double y = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - snowball.position().y;
        double z = target.getZ() - shooter.getZ();
        double pitch = MathHelper.sqrt(x * x + z * z) * 0.15D;
        snowball.shoot(x, y + pitch, z, 1.6F, 1);
        shooter.playSound(SoundEvents.SNOWBALL_THROW, 0.5F, 0.4F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
        shooter.level.addFreshEntity(snowball);
    }

    @Override
    protected void stop(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
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
