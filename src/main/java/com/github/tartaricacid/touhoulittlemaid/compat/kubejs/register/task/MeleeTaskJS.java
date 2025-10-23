package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidUseShieldTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MeleeTaskJS implements IAttackTask {
    private final Builder builder;

    public MeleeTaskJS(Builder builder) {
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
            return SoundUtil.attackSound(maid, InitSounds.MAID_ATTACK.get(), 0.5f);
        }
        return this.builder.sound;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(m ->
                isWeapon(m, m.getMainHandItem()), IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target ->
                !isWeapon(maid, maid.getMainHandItem()) || maid.distanceTo(target) > maid.getRestrictRadius());
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(builder.walkSpeed);
        BehaviorControl<Mob> attackTargetTask = MeleeAttack.create(builder.meleeCooldownTick);
        MaidUseShieldTask maidUseShieldTask = new MaidUseShieldTask();

        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, attackTargetTask),
                Pair.of(5, maidUseShieldTask));

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
                !isWeapon(maid, maid.getMainHandItem()) || farAway(target, maid));
        BehaviorControl<Mob> attackTargetTask = MeleeAttack.create(builder.meleeCooldownTick);
        MaidUseShieldTask maidUseShieldTask = new MaidUseShieldTask();

        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, attackTargetTask),
                Pair.of(5, maidUseShieldTask));

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
            return IAttackTask.super.canAttack(maid, target);
        }
        return this.builder.canAttack.test(maid, target);
    }

    @Override
    public boolean hasExtraAttack(EntityMaid maid, Entity target) {
        if (this.builder.hasExtraAttack == null) {
            return false;
        }
        return this.builder.hasExtraAttack.test(maid, target);
    }

    @Override
    public boolean doExtraAttack(EntityMaid maid, Entity target) {
        if (this.builder.doExtraAttack == null) {
            return false;
        }
        return this.builder.doExtraAttack.test(maid, target);
    }

    @Override
    public boolean isWeapon(EntityMaid maid, ItemStack stack) {
        if (this.builder.isWeapon == null) {
            return false;
        }
        return this.builder.isWeapon.test(maid, stack);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        if (!target.isAlive()) {
            return true;
        }
        boolean enable = maid.isHomeModeEnable();
        float radius = maid.getRestrictRadius();
        if (!enable && maid.getOwner() != null) {
            return maid.getOwner().distanceTo(target) > radius;
        }
        return maid.distanceTo(target) > radius;
    }

    public static class Builder extends TaskBuilder<Builder, MeleeTaskJS> {
        private @Nullable BiPredicate<EntityMaid, LivingEntity> canAttack = null;
        private @Nullable BiPredicate<EntityMaid, Entity> hasExtraAttack = null;
        private @Nullable BiPredicate<EntityMaid, Entity> doExtraAttack = null;
        private @Nullable BiPredicate<EntityMaid, ItemStack> isWeapon = null;
        private float walkSpeed = 0.6f;
        private int meleeCooldownTick = 20;

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
                Sets the condition for whether the maid has an extra attack against a target. Default is false. <br>
                设置女仆是否对目标有额外攻击的条件。默认为 false。
                """)
        public Builder hasExtraAttack(BiPredicate<EntityMaid, Entity> hasExtraAttack) {
            this.hasExtraAttack = hasExtraAttack;
            return this;
        }

        @Info(value = """
                Sets the action for the maid to perform an extra attack against a target. Default is empty. <br>
                设置女仆对目标执行额外攻击的动作。默认为空。
                """)
        public Builder doExtraAttack(BiPredicate<EntityMaid, Entity> doExtraAttack) {
            this.doExtraAttack = doExtraAttack;
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
                Sets the walk speed for the maid when moving towards a target. Default is 0.6. <br>
                设置女仆在接近目标时的移动速度。默认为 0.6。
                """)
        public Builder walkSpeed(float walkSpeed) {
            this.walkSpeed = walkSpeed;
            return this;
        }

        @Info(value = """
                Sets the cooldown tick for the attack. Default is 20 ticks. <br>
                设置攻击的冷却时间（以 tick 为单位）。默认值为 20 ticks。
                """)
        public Builder meleeCooldownTick(int meleeCooldownTick) {
            this.meleeCooldownTick = meleeCooldownTick;
            return this;
        }
    }
}

