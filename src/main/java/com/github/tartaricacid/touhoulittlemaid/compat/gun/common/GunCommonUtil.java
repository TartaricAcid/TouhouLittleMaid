package com.github.tartaricacid.touhoulittlemaid.compat.gun.common;

import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.task.TaskGunAttack;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.tacz.TacCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
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
        return Optional.empty();
    }

    public static void tick(EntityMaid shooter, LivingEntity target, ItemStack gunItem) {
        if (SWarfareCompat.isGun(gunItem)) {
            SWarfareCompat.tick(shooter, target, gunItem);
        }
    }

    public static int performGunAttack(EntityMaid shooter, LivingEntity target, ItemStack gunItem) throws Exception {
        if (TacCompat.isGun(gunItem)) {
            return TacCompat.performGunAttack(shooter, target, gunItem);
        } else if (SWarfareCompat.isGun(gunItem)) {
            return SWarfareCompat.performGunAttack(shooter, target, gunItem);
        }
        return 100;
    }

    public static void stopAim(EntityMaid maid) {
        TacCompat.stopAim(maid);
    }
}
