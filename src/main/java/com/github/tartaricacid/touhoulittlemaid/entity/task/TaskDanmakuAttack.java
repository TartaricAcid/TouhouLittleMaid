package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.init.InitEnchantments;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
    public List<Pair<Integer, Behavior<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        RunIf<EntityMaid> supplementedTask = new RunIf<>(this::hasGohei,
                new StartAttacking<>(IAttackTask::findFirstValidAttackTarget));
        StopAttackingIfTargetInvalid<EntityMaid> findTargetTask = new StopAttackingIfTargetInvalid<>(
                (target) -> !hasGohei(maid) || farAway(target, maid));
        SetWalkTargetFromAttackTargetIfTargetOutOfReach moveToTargetTask = new SetWalkTargetFromAttackTargetIfTargetOutOfReach(0.6f);
        MaidAttackStrafingTask maidAttackStrafingTask = new MaidAttackStrafingTask();
        MaidShootTargetTask shootTargetTask = new MaidShootTargetTask(2);

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
            if (ItemHakureiGohei.isGohei(mainHandItem)) {
                long entityCount = livingEntities.stream().filter(shooter::canAttack).count();
                Level level = shooter.level;
                // 分为三档
                // 1 自机狙
                // <=5 60 度扇形
                // >5 120 度扇形
                // 弹幕伤害也和好感度挂钩
                AttributeInstance attackDamage = shooter.getAttribute(Attributes.ATTACK_DAMAGE);
                float attackValue = 2.0f;
                if (attackDamage != null) {
                    attackValue = (float) attackDamage.getBaseValue();
                }
                int impedingLevel = EnchantmentHelper.getItemEnchantmentLevel(InitEnchantments.IMPEDING.get(), mainHandItem);
                int speedyLevel = EnchantmentHelper.getItemEnchantmentLevel(InitEnchantments.SPEEDY.get(), mainHandItem);
                int multiShotLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, mainHandItem);
                int endersEnderLevel = EnchantmentHelper.getItemEnchantmentLevel(InitEnchantments.ENDERS_ENDER.get(), mainHandItem);
                float speed = (0.3f * (distanceFactor + 1)) * (speedyLevel + 1);
                boolean hurtEnderman = endersEnderLevel > 0;

                if (entityCount <= 1) {
                    if (multiShotLevel > 0) {
                        DanmakuShoot.create().setWorld(level).setThrower(shooter)
                                .setTarget(target).setRandomColor().setRandomType()
                                .setDamage(attackValue * (distanceFactor + 1.2f)).setGravity(0)
                                .setVelocity(speed).setHurtEnderman(hurtEnderman)
                                .setInaccuracy(1.0f).setFanNum(3).setYawTotal(Math.PI / 12)
                                .setImpedingLevel(impedingLevel)
                                .fanShapedShot();
                    } else {
                        DanmakuShoot.create().setWorld(level).setThrower(shooter)
                                .setTarget(target).setRandomColor().setRandomType()
                                .setDamage(attackValue * (distanceFactor + 1)).setGravity(0)
                                .setVelocity(speed).setHurtEnderman(hurtEnderman)
                                .setInaccuracy(0.2f).setImpedingLevel(impedingLevel)
                                .aimedShot();
                    }
                } else if (entityCount <= 5) {
                    DanmakuShoot.create().setWorld(level).setThrower(shooter)
                            .setTarget(target).setRandomColor().setRandomType()
                            .setDamage(attackValue * (distanceFactor + 1.2f)).setGravity(0)
                            .setVelocity(speed).setHurtEnderman(hurtEnderman)
                            .setInaccuracy(0.2f).setFanNum(8).setYawTotal(Math.PI / 3)
                            .setImpedingLevel(impedingLevel)
                            .fanShapedShot();
                } else {
                    DanmakuShoot.create().setWorld(level).setThrower(shooter)
                            .setTarget(target).setRandomColor().setRandomType()
                            .setDamage(attackValue * (distanceFactor + 1.5f)).setGravity(0)
                            .setVelocity(speed).setHurtEnderman(hurtEnderman)
                            .setInaccuracy(0.2f).setFanNum(32).setYawTotal(2 * Math.PI / 3)
                            .setImpedingLevel(impedingLevel)
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
        return ItemHakureiGohei.isGohei(maid.getMainHandItem());
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
