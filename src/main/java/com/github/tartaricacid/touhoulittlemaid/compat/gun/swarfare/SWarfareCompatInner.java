package com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare;

import com.atsuishio.superbwarfare.data.gun.FireMode;
import com.atsuishio.superbwarfare.data.gun.GunData;
import com.atsuishio.superbwarfare.data.gun.GunProp;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.item.HandGrenade;
import com.atsuishio.superbwarfare.item.gun.GunItem;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidAnimationMessage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ItemStack;

import static com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask.targetConditionsTest;
import static com.github.tartaricacid.touhoulittlemaid.network.message.MaidAnimationMessage.SWF_FIRE;
import static com.github.tartaricacid.touhoulittlemaid.network.message.MaidAnimationMessage.SWF_RELOAD;

public class SWarfareCompatInner {
    static boolean isGun(ItemStack stack) {
        return stack.getItem() instanceof GunItem;
    }

    static boolean isGrenade(ItemStack stack) {
        return stack.getItem() instanceof HandGrenade;
    }

    static boolean isVehicle(Entity entity) {
        return entity instanceof VehicleEntity;
    }

    static boolean shouldHideLivingRender(LivingEntity maid) {
        if (maid.getVehicle() instanceof VehicleEntity vehicle) {
            return vehicle.hidePassenger(maid);
        }
        return false;
    }

    static boolean canSee(EntityMaid maid, LivingEntity target) {
        ItemStack handItem = maid.getMainHandItem();
        if (!(handItem.getItem() instanceof GunItem)) {
            return BehaviorUtils.canSee(maid, target);
        }
        GunData gunData = GunData.from(handItem);
        if (gunData == null) {
            return maid.canSee(target);
        }
        // 狙击枪？用远距离模式
        if (handItem.is(SWarfareCompat.SNIPER)) {
            return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_LONG_DISTANCE);
        }
        // 霰弹枪？手枪？冲锋枪？近距离模式
        if (handItem.is(SWarfareCompat.PISTOL) || handItem.is(SWarfareCompat.SMG) || handItem.is(SWarfareCompat.SHOOTGUN)) {
            return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_NEAR_DISTANCE);
        }
        // 其他情况，中等距离
        return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_MEDIUM_DISTANCE);
    }

    static int performGunAttack(EntityMaid shooter, LivingEntity target, ItemStack gunItem) {
        if (!(gunItem.getItem() instanceof GunItem)) {
            return 100;
        }
        GunData gunData = GunData.from(gunItem);
        if (gunData == null) {
            return 100;
        }
        if (gunData.shouldStartReloading(shooter)) {
            gunData.startReload();
            MaidAnimationMessage msg = new MaidAnimationMessage(shooter.getId(), SWF_RELOAD);
            NetworkHandler.sendToTrackingEntity(msg, shooter);
            return 5;
        }
        if (gunData.shouldStartBolt()) {
            gunData.startBolt();
            MaidAnimationMessage msg = new MaidAnimationMessage(shooter.getId(), SWF_RELOAD);
            NetworkHandler.sendToTrackingEntity(msg, shooter);
            return 5;
        }
        if (gunData.canShoot(shooter)) {
            // 如果是狙击枪，应用瞄准
            boolean isSniper = gunItem.is(SWarfareCompat.SNIPER);
            if (isSniper && !shooter.isAiming()) {
                shooter.setAiming(true);
                return 20;
            }
            // 如果是非狙击枪，超出 radius 范围，那么也瞄准
            if (!isSniper) {
                float distance = shooter.distanceTo(target);
                float radius = shooter.getRestrictRadius();
                if (distance <= radius && shooter.isAiming()) {
                    shooter.setAiming(false);
                    return 10;
                }
                if (distance > radius && !shooter.isAiming()) {
                    shooter.setAiming(true);
                    return 20;
                }
            }

            double rps = gunData.get(GunProp.RPM) / 60.0;
            int cooldown = (int) Math.round(20 / rps);
            FireMode fireMode = gunData.fireMode.get();
            if (fireMode == FireMode.SEMI || fireMode == FireMode.BURST && gunData.burstAmount.get() == 0) {
                cooldown += (5 + shooter.getRandom().nextInt(5));
            }

            // 将女仆的 look angle 设置好
            double x = target.getX() - shooter.getX();
            double y = target.getEyeY() - shooter.getEyeY();
            double z = target.getZ() - shooter.getZ();
            float yaw = (float) -Math.toDegrees(Math.atan2(x, z));
            float pitch = (float) -Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + z * z)));
            shooter.setXRot(pitch);
            shooter.setYRot(yaw);

            gunData.shoot(shooter, 0, shooter.isAiming(), target.getUUID());

            MaidAnimationMessage msg = new MaidAnimationMessage(shooter.getId(), SWF_FIRE);
            NetworkHandler.sendToTrackingEntity(msg, shooter);
            return cooldown;
        }

        return 100;
    }

    static void tick(EntityMaid shooter, LivingEntity target, ItemStack gunItem) {
        if (!(gunItem.getItem() instanceof GunItem)) {
            return;
        }
        GunData gunData = GunData.from(gunItem);
        if (gunData == null) {
            return;
        }
        gunData.tick(shooter, true);
    }
}
