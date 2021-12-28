package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

public class MaidAttackStrafingTask extends Task<EntityMaid> {
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public MaidAttackStrafingTask() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT,
                MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.VALUE_PRESENT),
                1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        return owner.getMainHandItem().getItem() instanceof ShootableItem &&
                owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
                        .filter(Entity::isAlive)
                        .filter(e -> owner.isWithinRestriction(e.blockPosition()))
                        .isPresent();
    }

    @Override
    protected void tick(ServerWorld worldIn, EntityMaid owner, long gameTime) {
        ItemStack stack = owner.getMainHandItem();
        if (!(stack.getItem() instanceof ShootableItem)) {
            return;
        }
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            int maxAttackDistance = ((ShootableItem) stack.getItem()).getDefaultProjectileRange();
            double distance = owner.distanceTo(target);

            // 如果在最大攻击距离之内，而且看见的时长足够长
            if (distance < maxAttackDistance) {
                ++this.strafingTime;
            } else {
                this.strafingTime = -1;
            }

            // 如果攻击时间也足够长，随机对走位方向和前后走位进行反转
            if (this.strafingTime >= 20) {
                if (owner.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if (owner.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            // 如果攻击时间大于 -1
            if (this.strafingTime > -1) {
                // 依据距离远近决定是否前后走位
                if (distance > maxAttackDistance * 0.5) {
                    this.strafingBackwards = false;
                } else if (distance < maxAttackDistance * 0.2) {
                    this.strafingBackwards = true;
                }

                // 应用走位
                owner.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                owner.yRot = MathHelper.rotateIfNecessary(owner.yRot, owner.yHeadRot, 0.0F);
                BrainUtil.lookAtEntity(owner, target);
            } else {
                // 否则只朝向攻击目标
                BrainUtil.lookAtEntity(owner, target);
            }
        });
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setSwingingArms(true);
    }

    @Override
    protected void stop(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setSwingingArms(false);
        entityIn.getMoveControl().strafe(0, 0);
    }

    @Override
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        return this.checkExtraStartConditions(worldIn, entityIn);
    }
}
