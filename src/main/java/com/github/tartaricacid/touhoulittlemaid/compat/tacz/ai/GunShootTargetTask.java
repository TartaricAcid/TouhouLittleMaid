package com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.FireMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class GunShootTargetTask extends Behavior<EntityMaid> {
    private final int attackCooldown;
    private int attackTime = -1;
    private int seeTime;

    public GunShootTargetTask(int attackCooldown) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
        this.attackCooldown = attackCooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return IGun.mainhandHoldGun(owner) && BehaviorUtils.canSee(owner, target);
        }
        return false;
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
            BehaviorUtils.lookAtEntity(owner, target);

            boolean canSee = BehaviorUtils.canSee(owner, target);
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

            // 如果实体手部处于激活状态
            if (--this.attackTime <= 0 && this.seeTime >= -60 && canSee) {
                this.performGunAttack(owner, target);
            }
        });
    }

    public void performGunAttack(EntityMaid shooter, LivingEntity target) {
        ItemStack mainHandItem = shooter.getMainHandItem();
        IGun iGun = IGun.getIGunOrNull(mainHandItem);
        if (iGun == null) {
            return;
        }
        double x = target.getX() - shooter.getX();
        double y = target.getEyeY() - shooter.getEyeY();
        double z = target.getZ() - shooter.getZ();

        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        float pitch = (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));

        IGunOperator gunOperator = IGunOperator.fromLivingEntity(shooter);
        ShootResult result = gunOperator.shoot(() -> pitch, () -> yaw);

        if (result == ShootResult.ID_NOT_EXIST || result == ShootResult.NOT_GUN) {
            this.attackTime = 50;
            return;
        }

        if (result == ShootResult.NOT_DRAW) {
            gunOperator.draw(shooter::getMainHandItem);
            this.attackTime = this.attackCooldown;
            return;
        }

        if (result == ShootResult.NEED_BOLT) {
            gunOperator.bolt();
            this.attackTime = 20;
            return;
        }

        if (result == ShootResult.NO_AMMO) {
            gunOperator.reload();
            this.attackTime = 20;
            return;
        }

        FireMode fireMode = iGun.getFireMode(mainHandItem);
        if (fireMode == FireMode.SEMI || fireMode == FireMode.BURST) {
            this.attackTime = 8 + shooter.getRandom().nextInt(5);
            return;
        }

        this.attackTime = this.attackCooldown;
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        this.seeTime = 0;
        this.attackTime = -1;
        entityIn.setSwingingArms(false);
    }
}
