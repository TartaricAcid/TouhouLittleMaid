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
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

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
        return InitSounds.MAID_DANMAKU_ATTACK.get();
    }

    @Override
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        SupplementedTask<EntityMaid> supplementedTask = new SupplementedTask<>(this::hasGohei,
                new ForgetAttackTargetTask<>(IAttackTask::findFirstValidAttackTarget));
        FindNewAttackTargetTask<EntityMaid> findTargetTask = new FindNewAttackTargetTask<>(
                (target) -> !hasGohei(maid) || farAway(target, maid));
        MoveToTargetTask moveToTargetTask = new MoveToTargetTask(0.6f);
        MaidAttackStrafingTask maidAttackStrafingTask = new MaidAttackStrafingTask();
        MaidShootTargetTask shootTargetTask = new MaidShootTargetTask(2);

        return Lists.newArrayList(
                Pair.of(2, supplementedTask),
                Pair.of(2, findTargetTask),
                Pair.of(2, moveToTargetTask),
                Pair.of(2, maidAttackStrafingTask),
                Pair.of(2, shootTargetTask)
        );
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        shooter.getBrain().getMemory(MemoryModuleType.LIVING_ENTITIES).ifPresent(livingEntities -> {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (mainHandItem.getItem() == InitItems.HAKUREI_GOHEI.get()) {
                long entityCount = livingEntities.stream().filter(shooter::canAttack).count();
                World level = shooter.level;
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
                mainHandItem.hurtAndBreak(1, shooter, (maid) -> maid.broadcastBreakEvent(Hand.MAIN_HAND));
            }
        });
    }

    @Override
    public List<ITextComponent> getDescription(EntityMaid maid) {
        return Collections.emptyList();
    }

    private boolean hasGohei(EntityMaid maid) {
        return maid.getMainHandItem().getItem() == InitItems.HAKUREI_GOHEI.get();
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
