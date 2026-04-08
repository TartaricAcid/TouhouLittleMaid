package com.github.tartaricacid.touhoulittlemaid.compat.gun.common.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.GunCommonUtil;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.ai.GunAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.compat.gun.common.ai.GunShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRangedWalkToTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidUseShieldTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
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
import java.util.function.Predicate;

public class TaskGunAttack implements IRangedAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "gun_attack");
    public static final ItemStack ICON = InitItems.TACZ_GUN_ICON.get().getDefaultInstance();

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return ICON;
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
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(
                this::mainhandHoldGun, IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target ->
                !this.mainhandHoldGun(maid) || farAway(target, maid));
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
        // 因为骑乘载具时，也会有射击，故此处设定敌对目标使用 canStartAttacking 方法
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(
                GunCommonUtil::canStartAttacking, IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target ->
                !GunCommonUtil.canStartAttacking(maid) || farAway(target, maid));
        BehaviorControl<EntityMaid> gunShootTargetTask = new GunShootTargetTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, gunShootTargetTask)
        );
    }

    @Override
    public AABB searchDimension(EntityMaid maid) {
        if (GunCommonUtil.canStartAttacking(maid)) {
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
        return GunCommonUtil.canSee(maid, target)
                .orElseGet(() -> IRangedAttackTask.super.canSee(maid, target));
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Collections.singletonList(Pair.of("has_gun", m -> isWeapon(m, m.getMainHandItem())));
    }

    @Override
    public boolean isWeapon(EntityMaid maid, ItemStack stack) {
        return GunCommonUtil.isGun(stack);
    }

    private boolean mainhandHoldGun(EntityMaid maid) {
        ItemStack item = maid.getMainHandItem();
        return isWeapon(maid, item);
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

    @Override
    public String getMaidActionSummary() {
        return "Use gun attack entities";
    }
}
