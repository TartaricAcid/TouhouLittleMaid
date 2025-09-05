package com.github.tartaricacid.touhoulittlemaid.compat.gun.common;

import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.task.TaskGunAttack;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.tacz.TacCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GunCommonUtil {
    public static void initAndAddTask(TaskManager manager) {
        boolean tacz = TacCompat.init();
        boolean swf = SWarfareCompat.init();
        if (tacz || swf) {
            manager.add(new TaskGunAttack());
        }
    }

    public static boolean isGun(ItemStack stack) {
        return SWarfareCompat.isGun(stack) || TacCompat.isGun(stack);
    }

    /**
     * 不单单判断枪械，还判断女仆是否在载具上 <br>
     * 女仆在载具上时也可以开火
     */
    public static boolean canStartAttacking(EntityMaid maid) {
        ItemStack item = maid.getMainHandItem();
        if (isGun(item)) {
            return true;
        }
        Entity vehicle = maid.getVehicle();
        if (vehicle != null) {
            return SWarfareCompat.isVehicle(vehicle);
        }
        LivingEntity owner = maid.getOwner();
        // 如果女仆在非一号位，那么 getVehicle 会返回 null
        // 故需要通过此方式判断女仆是否在载具上
        if (owner instanceof Player player) {
            Entity playerVehicle = player.getVehicle();
            if (playerVehicle != null && playerVehicle.getPassengers().contains(maid)) {
                return SWarfareCompat.isVehicle(playerVehicle);
            }
        }
        return false;
    }

    @Nullable
    public static ResourceLocation getGunId(ItemStack stack) {
        if (SWarfareCompat.isGun(stack)) {
            return SWarfareCompat.getGunId(stack);
        }
        if (TacCompat.isGun(stack)) {
            return TacCompat.getGunId(stack);
        }
        return null;
    }

    public static Optional<Boolean> canSee(EntityMaid maid, LivingEntity target) {
        ItemStack handItem = maid.getMainHandItem();
        if (TacCompat.isGun(handItem)) {
            return Optional.of(TacCompat.canSee(maid, target));
        }
        if (SWarfareCompat.isGun(handItem)) {
            return Optional.of(SWarfareCompat.canSee(maid, target));
        }
        return SWarfareCompat.canVehicleSee(maid, target);
    }

    public static void tick(EntityMaid shooter, LivingEntity target, ItemStack gunItem) {
        if (SWarfareCompat.isGun(gunItem)) {
            SWarfareCompat.tick(shooter, target, gunItem);
        }
    }

    public static int performGunAttack(EntityMaid shooter, LivingEntity target, ItemStack gunItem) throws Exception {
        if (TacCompat.isGun(gunItem)) {
            return TacCompat.performGunAttack(shooter, target, gunItem);
        }
        if (SWarfareCompat.isGun(gunItem)) {
            return SWarfareCompat.performGunAttack(shooter, target, gunItem);
        }
        return 100;
    }

    public static void stopAim(EntityMaid maid) {
        TacCompat.stopAim(maid);
    }
}
