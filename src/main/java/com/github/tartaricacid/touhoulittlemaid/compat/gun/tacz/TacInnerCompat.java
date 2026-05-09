package com.github.tartaricacid.touhoulittlemaid.compat.gun.tacz;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask.targetConditionsTest;

public class TacInnerCompat {
    @Nullable
    static ResourceLocation getGunId(ItemStack stack) {
        IGun iGun = IGun.getIGunOrNull(stack);
        if (iGun != null) {
            return iGun.getGunId(stack);
        }
        return null;
    }

    static boolean isGun(ItemStack itemStack) {
        return itemStack.getItem() instanceof IGun;
    }

    static boolean canSee(EntityMaid maid, LivingEntity target) {
        ItemStack handItem = maid.getMainHandItem();
        IGun iGun = IGun.getIGunOrNull(handItem);
        if (iGun == null) {
            return BehaviorUtils.canSee(maid, target);
        }
        ResourceLocation gunId = iGun.getGunId(handItem);
        return TimelessAPI.getCommonGunIndex(gunId).map(index -> {
            String type = index.getType();
            // 狙击枪？用远距离模式
            String sniper = GunTabType.SNIPER.name().toLowerCase(Locale.ENGLISH);
            if (sniper.equals(type)) {
                return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_LONG_DISTANCE);
            }
            // 霰弹枪？手枪？冲锋枪？近距离模式
            String shotgun = GunTabType.SHOTGUN.name().toLowerCase(Locale.ENGLISH);
            String pistol = GunTabType.PISTOL.name().toLowerCase(Locale.ENGLISH);
            String smg = GunTabType.SMG.name().toLowerCase(Locale.ENGLISH);
            if (shotgun.equals(type) || pistol.equals(type) || smg.equals(type)) {
                return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_NEAR_DISTANCE);
            }
            // 其他情况，中等距离
            return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_MEDIUM_DISTANCE);
        }).orElse(BehaviorUtils.canSee(maid, target));
    }

    static int performGunAttack(EntityMaid shooter, LivingEntity target, ItemStack gunItem) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return 100;
        }
        ResourceLocation gunId = iGun.getGunId(gunItem);
        Optional<CommonGunIndex> optional = TimelessAPI.getCommonGunIndex(gunId);
        if (optional.isEmpty()) {
            return 100;
        }
        CommonGunIndex gunIndex = optional.get();
        GunData gunData = gunIndex.getGunData();

        double x = target.getX() - shooter.getX();
        double y = target.getEyeY() - shooter.getEyeY();
        double z = target.getZ() - shooter.getZ();

        float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
        float pitch = (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));

        float radius = shooter.getRestrictRadius();

        IGunOperator gunOperator = IGunOperator.fromLivingEntity(shooter);
        ShootResult result = gunOperator.shoot(() -> pitch, () -> yaw);

        if (result == ShootResult.ID_NOT_EXIST || result == ShootResult.NOT_GUN) {
            return 100;
        }

        // 如果是狙击枪，应用瞄准
        String sniper = GunTabType.SNIPER.name().toLowerCase(Locale.ENGLISH);
        if (gunIndex.getType().equals(sniper) && !gunOperator.getSynIsAiming()) {
            gunOperator.aim(true);
            // 多加 2 tick，用来平衡延迟
            return Math.round(gunData.getAimTime() * 20) + 2;
        }

        // 如果是非狙击枪，超出 radius 范围，那么也瞄准
        if (!gunIndex.getType().equals(sniper)) {
            float distance = shooter.distanceTo(target);
            if (distance <= radius && gunOperator.getSynIsAiming()) {
                gunOperator.aim(false);
                // 多加 2 tick，用来平衡延迟
                return Math.round(gunData.getAimTime() * 20) + 2;
            }
            if (distance > radius && !gunOperator.getSynIsAiming()) {
                gunOperator.aim(true);
                // 多加 2 tick，用来平衡延迟
                return Math.round(gunData.getAimTime() * 20) + 2;
            }
        }

        if (result == ShootResult.NOT_DRAW) {
            gunOperator.draw(shooter::getMainHandItem);
            // 多加 2 tick，用来平衡延迟
            return Math.round(gunData.getDrawTime() * 20) + 2;
        }

        if (result == ShootResult.NEED_BOLT) {
            gunOperator.bolt();
            return Math.round(gunData.getBoltActionTime() * 20) + 2;
        }

        if (result == ShootResult.NO_AMMO) {
            // reload 不会触发 MaidRequestItemEvent，此处手动请求弹药
            var availableInv = shooter.getAvailableInv(true);
            ItemsUtil.findStackSlot(availableInv, stack -> {
                IAmmo ammo = IAmmo.getIAmmoOrNull(stack);
                return ammo != null && ammo.isAmmoOfGun(gunItem, stack);
            });
            gunOperator.reload();
            float emptyTime = gunData.getReloadData().getCooldown().getEmptyTime();
            return Math.round(emptyTime * 20) + 2;
        }

        FireMode fireMode = iGun.getFireMode(gunItem);
        if (fireMode == FireMode.SEMI || fireMode == FireMode.BURST) {
            return 10 + shooter.getRandom().nextInt(5);
        }

        return 2;
    }

    static void stopAim(EntityMaid maid) {
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
