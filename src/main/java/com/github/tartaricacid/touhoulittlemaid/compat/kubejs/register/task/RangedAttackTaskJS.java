package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingAnyItemTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRangedWalkToTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetAnyItemTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.tartaricacid.touhoulittlemaid.util.functional.TriConsumer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RangedAttackTaskJS implements IRangedAttackTask {
    private final Builder builder;

    public RangedAttackTaskJS(Builder builder) {
        this.builder = builder;
    }

    @Override
    public ResourceLocation getUid() {
        return this.builder.id;
    }

    @Override
    public ItemStack getIcon() {
        return this.builder.icon;
    }

    @Override
    @Nullable
    public SoundEvent getAmbientSound(EntityMaid maid) {
        if (this.builder.sound == null) {
            return SoundUtil.attackSound(maid, InitSounds.MAID_RANGE_ATTACK.get(), 0.5f);
        }
        return this.builder.sound;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(m ->
                isWeapon(m, m.getMainHandItem()), IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target ->
                !isWeapon(maid, maid.getMainHandItem()) || maid.distanceTo(target) > this.searchRadius(maid));
        BehaviorControl<EntityMaid> moveToTargetTask = MaidRangedWalkToTarget.create(builder.walkSpeed);
        BehaviorControl<EntityMaid> maidAttackStrafingTask = new MaidAttackStrafingAnyItemTask(stack ->
                isWeapon(maid, stack), builder.projectileRange, builder.walkSpeed);
        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetAnyItemTask(2, builder.chargeDurationTick, stack ->
                isWeapon(maid, stack));

        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, maidAttackStrafingTask),
                Pair.of(5, shootTargetTask));

        for (var pair : this.builder.brains) {
            tasks.add(Pair.of(pair.getFirst(), pair.getSecond().apply(this, maid)));
        }
        return tasks;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(m ->
                isWeapon(m, m.getMainHandItem()), IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target ->
                !isWeapon(maid, maid.getMainHandItem()) || maid.distanceTo(target) > this.searchRadius(maid));
        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetAnyItemTask(2, builder.chargeDurationTick, stack ->
                isWeapon(maid, stack));

        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, shootTargetTask));

        for (var pair : this.builder.rideBrains) {
            tasks.add(Pair.of(pair.getFirst(), pair.getSecond().apply(this, maid)));
        }
        return tasks;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        if (this.builder.enable == null) {
            return true;
        }
        return this.builder.enable.test(maid);
    }

    @Override
    public boolean isHidden(EntityMaid maid) {
        if (this.builder.isHidden == null) {
            return false;
        }
        return this.builder.isHidden.test(maid);
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        if (this.builder.enableLookAndRandomWalk == null) {
            return true;
        }
        return this.builder.enableLookAndRandomWalk.test(maid);
    }

    @Override
    public boolean enableEating(EntityMaid maid) {
        if (this.builder.enableEating == null) {
            return true;
        }
        return this.builder.enableEating.test(maid);
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return this.builder.enableConditionDesc;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return this.builder.conditionDesc;
    }

    @Override
    public boolean canAttack(EntityMaid maid, LivingEntity target) {
        if (this.builder.canAttack == null) {
            return IRangedAttackTask.super.canAttack(maid, target);
        }
        return this.builder.canAttack.test(maid, target);
    }

    @Override
    public boolean isWeapon(EntityMaid maid, ItemStack stack) {
        if (this.builder.isWeapon == null) {
            return false;
        }
        return this.builder.isWeapon.test(maid, stack);
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        if (this.builder.performRangedAttack != null) {
            this.builder.performRangedAttack.accept(shooter, target, distanceFactor);
        }
    }

    @Override
    public boolean canSee(EntityMaid maid, LivingEntity target) {
        return TARGET_CONDITIONS.range(this.searchRadius(maid)).test(maid, target);
    }

    @Override
    public AABB searchDimension(EntityMaid maid) {
        if (isWeapon(maid, maid.getMainHandItem())) {
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
        if (this.builder.searchRadius > 0) {
            return this.builder.searchRadius;
        }
        return IRangedAttackTask.super.searchRadius(maid);
    }

    public static class Builder extends TaskBuilder<Builder, RangedAttackTaskJS> {
        private @Nullable BiPredicate<EntityMaid, LivingEntity> canAttack = null;
        private @Nullable BiPredicate<EntityMaid, ItemStack> isWeapon = null;
        private @Nullable TriConsumer<EntityMaid, LivingEntity, Float> performRangedAttack = null;
        private float searchRadius = -1f;
        // 默认投射物射程
        private float projectileRange = 16f;
        // 默认充能时间为 20 tick
        private int chargeDurationTick = 20;
        // 默认步行速度
        private float walkSpeed = 0.5f;

        public Builder(ResourceLocation id, ItemStack icon) {
            super(id, icon);
        }

        @Info(value = """
                Sets the condition for whether the maid can attack a target. Default is all hostile entities. <br>
                设置女仆是否可以攻击目标的条件。默认为所有敌对生物。
                """)
        public Builder canAttack(BiPredicate<EntityMaid, LivingEntity> canAttack) {
            this.canAttack = canAttack;
            return this;
        }

        @Info(value = """
                Sets the condition for whether the maid considers an item as a weapon. Mandatory. <br>
                设置女仆是否将当前物品视为武器的条件。必填项。
                """)
        public Builder isWeapon(BiPredicate<EntityMaid, ItemStack> isWeapon) {
            this.isWeapon = isWeapon;
            return this;
        }

        @Info(value = """
                Sets the search radius for the maid to find targets. Default is the work range, you can increase this value for long-range attacks. <br>
                设置女仆寻找目标的搜索半径。默认为工作范围，你可以调大此数值实现超视距打击。
                """)
        public Builder searchRadius(float radius) {
            this.searchRadius = radius;
            return this;
        }

        @Info(value = """
                Sets the projectile range for the maid's ranged attack. Generally, this value is less than the search radius,
                and the maid will actively approach hostile entities until within range. Default is 16 blocks. <br>
                设置女仆远程攻击的投射物射程。一般这个数值会小于搜索半径，女仆会主动接近敌对生物，直到射程范围内。默认为 16 格。
                """)
        public Builder projectileRange(float projectileRange) {
            this.projectileRange = projectileRange;
            return this;
        }

        @Info(value = """
                Sets the charge duration for the maid's ranged attack, in ticks. Generally, this is 20 ticks. <br>
                设置女仆远程攻击的充能时间，单位为 tick。一般是 20 tick。
                """)
        public Builder chargeDurationTick(int chargeDurationTick) {
            this.chargeDurationTick = chargeDurationTick;
            return this;
        }

        @Info(value = """
                Sets the walk speed for the maid when performing a ranged attack. Since ranged attacks require frequent movement,
                this value can be slightly less than 0.6f. <br>
                设置女仆远程攻击时的行走速度。因为远程攻击走位比较频繁，所以这个数值可以略小于 0.6f。
                """)
        public Builder walkSpeed(float walkSpeed) {
            this.walkSpeed = walkSpeed;
            return this;
        }

        @Info(value = """
                Sets the action to perform when the maid performs a ranged attack. Default is empty. <br>
                设置女仆执行远程攻击时的行为。默认为空。
                """)
        public Builder performRangedAttack(TriConsumer<EntityMaid, LivingEntity, Float> performRangedAttack) {
            this.performRangedAttack = performRangedAttack;
            return this;
        }
    }
}

