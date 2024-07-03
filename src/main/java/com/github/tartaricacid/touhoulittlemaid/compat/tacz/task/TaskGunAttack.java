package com.github.tartaricacid.touhoulittlemaid.compat.tacz.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai.GunAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai.GunShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai.GunWalkToTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class TaskGunAttack implements IAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "gun_attack");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return GunItemBuilder.create().setId(new ResourceLocation(GunMod.MOD_ID, "glock_17")).build();
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
    public List<Pair<Integer, Behavior<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        RunIf<EntityMaid> supplementedTask = new RunIf<>(this::mainhandHoldGun,
                new StartAttacking<>(IAttackTask::findFirstValidAttackTarget));
        StopAttackingIfTargetInvalid<EntityMaid> findTargetTask = new StopAttackingIfTargetInvalid<>(
                (target) -> !mainhandHoldGun(maid) || farAway(target, maid));
        Behavior<EntityMaid> gunWalkTargetTask = new GunWalkToTarget(0.6f);
        Behavior<EntityMaid> gunAttackStrafingTask = new GunAttackStrafingTask();
        Behavior<EntityMaid> gunShootTargetTask = new GunShootTargetTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, gunWalkTargetTask),
                Pair.of(5, gunAttackStrafingTask),
                Pair.of(5, gunShootTargetTask)
        );
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Collections.singletonList(Pair.of("has_tacz_gun", this::mainhandHoldGun));
    }

    private boolean mainhandHoldGun(EntityMaid maid) {
        return IGun.mainhandHoldGun(maid);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > maid.getRestrictRadius();
    }
}
