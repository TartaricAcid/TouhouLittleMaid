package com.github.tartaricacid.touhoulittlemaid.compat.tacz.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai.GunAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai.GunShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRangedWalkToTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidUseShieldTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import static com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask.targetConditionsTest;

public class TaskGunAttack implements IRangedAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "gun_attack");
    private ItemStack icon;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        ResourceLocation gunId = new ResourceLocation(GunMod.MOD_ID, "glock_17");
        if (icon == null) {
            TimelessAPI.getCommonGunIndex(gunId).ifPresentOrElse(index -> this.icon = GunItemBuilder.create().setId(gunId).build(),
                    () -> this.icon = InitItems.TACZ_GUN_ICON.get().getDefaultInstance());
        }
        return this.icon;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.attackSound(maid, InitSounds.MAID_RANGE_ATTACK.get(), 0.5f);
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        return false;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::mainhandHoldGun, IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target -> !mainhandHoldGun(maid) || farAway(target, maid));
        BehaviorControl<EntityMaid> gunWalkTargetTask = MaidRangedWalkToTarget.create(0.6f);
        BehaviorControl<EntityMaid> gunAttackStrafingTask = new GunAttackStrafingTask();
        BehaviorControl<EntityMaid> gunShootTargetTask = new GunShootTargetTask();
        MaidUseShieldTask maidUseShieldTask = new MaidUseShieldTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, gunWalkTargetTask),
                Pair.of(5, gunAttackStrafingTask),
                Pair.of(5, gunShootTargetTask),
                Pair.of(5, maidUseShieldTask)
        );
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::mainhandHoldGun, IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target -> !mainhandHoldGun(maid) || farAway(target, maid));
        BehaviorControl<EntityMaid> gunShootTargetTask = new GunShootTargetTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, gunShootTargetTask)
        );
    }

    @Override
    public AABB searchDimension(EntityMaid maid) {
        if (IGun.mainhandHoldGun(maid)) {
            float searchRange = this.searchRadius(maid);
            if (maid.hasRestriction()) {
                return new AABB(maid.getRestrictCenter()).inflate(searchRange);
            } else {
                return maid.getBoundingBox().inflate(searchRange);
            }
        }
        return IRangedAttackTask.super.searchDimension(maid);
    }

    @Override
    public float searchRadius(EntityMaid maid) {
        return MaidConfig.MAID_GUN_LONG_DISTANCE.get();
    }

    @Override
    public boolean canSee(EntityMaid maid, LivingEntity target) {
        ItemStack handItem = maid.getMainHandItem();
        IGun iGun = IGun.getIGunOrNull(handItem);
        if (iGun != null) {
            ResourceLocation gunId = iGun.getGunId(handItem);
            return TimelessAPI.getCommonGunIndex(gunId).map(index -> {
                String type = index.getType();
                // 狙击枪？用远距离模式
                String sniper = GunTabType.SNIPER.name().toLowerCase(Locale.ENGLISH);
                if (sniper.equals(type)) {
                    return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_LONG_DISTANCE);
                }
                // 霰弹枪？手枪？近距离模式
                String shotgun = GunTabType.SHOTGUN.name().toLowerCase(Locale.ENGLISH);
                String pistol = GunTabType.PISTOL.name().toLowerCase(Locale.ENGLISH);
                if (shotgun.equals(type) || pistol.equals(type)) {
                    return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_NEAR_DISTANCE);
                }
                // 其他情况，中等距离
                return targetConditionsTest(maid, target, MaidConfig.MAID_GUN_MEDIUM_DISTANCE);
            }).orElse(IRangedAttackTask.super.canSee(maid, target));
        }
        return IRangedAttackTask.super.canSee(maid, target);
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Collections.singletonList(Pair.of("has_tacz_gun", this::mainhandHoldGun));
    }

    private boolean mainhandHoldGun(EntityMaid maid) {
        return IGun.mainhandHoldGun(maid);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > this.searchRadius(maid);
    }

    /**
     * 枪械射击不走原版那套逻辑，故此方法为空
     */
    @Override
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
    }
}
