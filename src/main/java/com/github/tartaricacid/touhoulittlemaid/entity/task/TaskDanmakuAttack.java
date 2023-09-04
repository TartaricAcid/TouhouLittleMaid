package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class TaskDanmakuAttack implements IRangedAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "danmaku_attack");
    private static final int MAX_STOP_ATTACK_DISTANCE = 16;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return InitItems.HAKUREI_GOHEI.get().getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.attackSound(maid, InitSounds.MAID_DANMAKU_ATTACK.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasGohei, IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasGohei(maid) || farAway(target, maid));
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6f);
        BehaviorControl<EntityMaid> maidAttackStrafingTask = new MaidAttackStrafingTask();
        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetTask(2);

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, maidAttackStrafingTask),
                Pair.of(5, shootTargetTask)
        );
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        shooter.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).ifPresent(livingEntities -> {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (mainHandItem.getItem() == InitItems.HAKUREI_GOHEI.get()) {
                long entityCount = livingEntities.stream().filter(shooter::canAttack).count();
                Level level = shooter.level();
                // 分为三档
                // 1 自机狙
                // <=5 60 度扇形
                // >5 120 度扇形
                if (entityCount <= 1) {
                    DanmakuShoot.create().setWorld(level).setThrower(shooter)
                            .setTarget(target).setRandomColor().setRandomType()
                            .setDamage(2 * (distanceFactor + 1)).setGravity(0)
                            .setVelocity(0.3f * (distanceFactor + 1))
                            .setInaccuracy(0.2f).aimedShot();
                } else if (entityCount <= 5) {
                    DanmakuShoot.create().setWorld(level).setThrower(shooter)
                            .setTarget(target).setRandomColor().setRandomType()
                            .setDamage(2 * (distanceFactor + 1.2f)).setGravity(0)
                            .setVelocity(0.3f * (distanceFactor + 1))
                            .setInaccuracy(0.2f).setFanNum(8).setYawTotal(Math.PI / 3)
                            .fanShapedShot();
                } else {
                    DanmakuShoot.create().setWorld(level).setThrower(shooter)
                            .setTarget(target).setRandomColor().setRandomType()
                            .setDamage(2 * (distanceFactor + 1.5f)).setGravity(0)
                            .setVelocity(0.3f * (distanceFactor + 1))
                            .setInaccuracy(0.2f).setFanNum(32).setYawTotal(2 * Math.PI / 3)
                            .fanShapedShot();
                }
                mainHandItem.hurtAndBreak(1, shooter, (maid) -> maid.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            }
        });
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Collections.singletonList(Pair.of("has_gohei", this::hasGohei));
    }

    private boolean hasGohei(EntityMaid maid) {
        return maid.getMainHandItem().getItem() == InitItems.HAKUREI_GOHEI.get();
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
