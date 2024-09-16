package com.github.tartaricacid.touhoulittlemaid.compat.tacz.utils;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IGun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GunBehaviorUtils {
    // 可见性校验工具，来自于 Sensor
    // 依据枪械种类，可以区分为远、中、近三类
    private static final TargetingConditions LONG_DISTANCE_TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(128);
    private static final TargetingConditions MEDIUM_DISTANCE_TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(96);
    private static final TargetingConditions NEAR_DISTANCE_TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(64);

    //可见性方法，来自于Sensor类
    public static boolean canSee(EntityMaid maid, LivingEntity target) {
        ItemStack handItem = maid.getMainHandItem();
        IGun iGun = IGun.getIGunOrNull(handItem);
        if (iGun != null) {
            ResourceLocation gunId = iGun.getGunId(handItem);
            return TimelessAPI.getCommonGunIndex(gunId).map(index -> {
                String type = index.getType();
                // 狙击枪？用远距离模式
                String sniper = GunTabType.SNIPER.name().toLowerCase(Locale.ENGLISH);
                if (sniper.equals(type)) {
                    return LONG_DISTANCE_TARGET_CONDITIONS.test(maid, target);
                }
                // 霰弹枪？手枪？近距离模式
                String shotgun = GunTabType.SHOTGUN.name().toLowerCase(Locale.ENGLISH);
                String pistol = GunTabType.PISTOL.name().toLowerCase(Locale.ENGLISH);
                if (shotgun.equals(type) || pistol.equals(type)) {
                    return NEAR_DISTANCE_TARGET_CONDITIONS.test(maid, target);
                }
                // 其他情况，中等距离
                return MEDIUM_DISTANCE_TARGET_CONDITIONS.test(maid, target);
            }).orElse(BehaviorUtils.canSee(maid, target));
        }
        return BehaviorUtils.canSee(maid, target);
    }

    // 寻找第一个可见目标，使用独立的方法，区别于 IAttackTask
    public static Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        if (maid.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).isPresent()) {
            List<LivingEntity> list = maid.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).get();
            return list.stream().filter(e -> maid.canAttack(e) && GunBehaviorUtils.canSee(maid, e)).findAny();
        }
        return Optional.empty();
    }
}
