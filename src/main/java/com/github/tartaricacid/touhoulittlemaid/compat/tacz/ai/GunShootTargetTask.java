package com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.Optional;

public class GunShootTargetTask extends Behavior<EntityMaid> {
    private int attackCooldown = -1;
    private int seeTime;

    public GunShootTargetTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
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
            if (--this.attackCooldown <= 0 && this.seeTime >= -60 && canSee) {
                ItemStack mainHandItem = owner.getMainHandItem();
                IGun iGun = IGun.getIGunOrNull(mainHandItem);
                if (iGun == null) {
                    this.attackCooldown = 100;
                    return;
                }
                ResourceLocation gunId = iGun.getGunId(mainHandItem);
                TimelessAPI.getCommonGunIndex(gunId).ifPresentOrElse(index -> this.performGunAttack(owner, target, mainHandItem, iGun, index), () -> this.attackCooldown = 100);
            }
        });
    }

    public void performGunAttack(EntityMaid shooter, LivingEntity target, ItemStack gunItem, IGun iGun, CommonGunIndex gunIndex) {
        GunData gunData = gunIndex.getGunData();

        double x = target.getX() - shooter.getX();
        double y = target.getEyeY() - shooter.getEyeY();
        double z = target.getZ() - shooter.getZ();

        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        float pitch = (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));

        IGunOperator gunOperator = IGunOperator.fromLivingEntity(shooter);
        ShootResult result = gunOperator.shoot(() -> pitch, () -> yaw);

        if (result == ShootResult.ID_NOT_EXIST || result == ShootResult.NOT_GUN) {
            this.attackCooldown = 100;
            return;
        }

        // 如果是狙击枪，应用瞄准
        String sniper = GunTabType.SNIPER.name().toLowerCase(Locale.ENGLISH);
        if (gunIndex.getType().equals(sniper) && !gunOperator.getSynIsAiming()) {
            gunOperator.aim(true);
            // 多加 2 tick，用来平衡延迟
            this.attackCooldown = Math.round(gunData.getAimTime() * 20) + 2;
            return;
        }

        // 如果是非狙击枪，就不要瞄准了，不然太超模了
        if (!gunIndex.getType().equals(sniper) && gunOperator.getSynIsAiming()) {
            gunOperator.aim(false);
            // 多加 2 tick，用来平衡延迟
            this.attackCooldown = Math.round(gunData.getAimTime() * 20) + 2;
            return;
        }

        if (result == ShootResult.NOT_DRAW) {
            gunOperator.draw(shooter::getMainHandItem);
            // 多加 2 tick，用来平衡延迟
            this.attackCooldown = Math.round(gunData.getDrawTime() * 20) + 2;
            return;
        }

        if (result == ShootResult.NEED_BOLT) {
            gunOperator.bolt();
            this.attackCooldown = Math.round(gunData.getBoltActionTime() * 20) + 2;
            return;
        }

        if (result == ShootResult.NO_AMMO) {
            gunOperator.reload();
            float emptyTime = gunData.getReloadData().getCooldown().getEmptyTime();
            this.attackCooldown = Math.round(emptyTime * 20) + 2;
            return;
        }

        FireMode fireMode = iGun.getFireMode(gunItem);
        if (fireMode == FireMode.SEMI || fireMode == FireMode.BURST) {
            this.attackCooldown = 10 + shooter.getRandom().nextInt(5);
            return;
        }

        this.attackCooldown = 2;
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        this.seeTime = 0;
        this.attackCooldown = -1;
        maid.setSwingingArms(false);
        // 停止瞄准
        this.stopAim(maid);
    }

    private void stopAim(EntityMaid maid) {
        ItemStack mainHandItem = maid.getMainHandItem();
        IGun iGun = IGun.getIGunOrNull(mainHandItem);
        if (iGun == null) {
            return;
        }
        ResourceLocation gunId = iGun.getGunId(mainHandItem);
        TimelessAPI.getCommonGunIndex(gunId).ifPresent(gunIndex -> {
            IGunOperator gunOperator = IGunOperator.fromLivingEntity(maid);
            if (gunOperator.getSynIsAiming()) {
                gunOperator.aim(false);
            }
        });
    }
}
