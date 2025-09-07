package com.github.tartaricacid.touhoulittlemaid.compat.gun.common.ai;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.GunCommonUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

public class GunShootTargetTask extends Behavior<EntityMaid> {
    private int attackCooldown = -1;
    private int seeTime;

    public GunShootTargetTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        if (!GunCommonUtil.isGun(owner.getMainHandItem())) {
            return false;
        }
        return owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(owner::canSee).isPresent();
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        return entityIn.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setSwingingArms(true);
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            // 实际上按照原版mc判定是看不见的，强行看见并朝向（没关就是开了？）
            owner.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
            boolean canSee = owner.canSee(target);
            boolean seeTimeMoreThanZero = this.seeTime > 0;

            // 如果两者不一致，重置看见时间
            if (canSee != seeTimeMoreThanZero) {
                this.seeTime = 0;
            }
            // 如果看见了对方，增加看见时间，否则减少
            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            ItemStack mainHandItem = owner.getMainHandItem();
            GunCommonUtil.tick(owner, target, mainHandItem);

            // 如果实体手部处于激活状态
            if (--this.attackCooldown <= 0 && this.seeTime >= -60 && canSee) {
                try {
                    // 由于部分枪包作者可能在写 lua 脚本时没有规范书写，导致 lua 脚本抛出异常
                    // 所以这里捕获异常，避免因为 lua 脚本错误导致游戏崩溃
                    this.attackCooldown = GunCommonUtil.performGunAttack(owner, target, mainHandItem);
                } catch (Exception e) {
                    TouhouLittleMaid.LOGGER.error("Error while performing gun attack for EntityMaid: {}", owner.getUUID(), e);
                    // 如果发生异常，重置攻击冷却时间
                    this.attackCooldown = 100;
                }
            }
        });
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        this.seeTime = 0;
        this.attackCooldown = -1;
        maid.setSwingingArms(false);
        maid.setAiming(false);
        // 停止
        GunCommonUtil.onStop(maid, this);
    }
}
