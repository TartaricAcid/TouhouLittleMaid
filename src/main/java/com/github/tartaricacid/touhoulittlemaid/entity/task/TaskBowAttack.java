package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.item.ProjectileWeaponItem.ARROW_OR_FIREWORK;

public class TaskBowAttack implements IRangedAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "ranged_attack");
    private static final int MAX_STOP_ATTACK_DISTANCE = 16;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.BOW.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.attackSound(maid, InitSounds.MAID_RANGE_ATTACK.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(e -> hasProjectileWeapon(e) && hasAmmunition(e), IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasProjectileWeapon(maid) || !hasAmmunition(maid) || farAway(target, maid));
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
        Projectile projectile = getAmmunition(shooter,distanceFactor);
        if (projectile == null) return;
        boolean flag = projectile instanceof FireworkRocketEntity;
        ItemStack mainHandItem = shooter.getMainHandItem();
        if (mainHandItem.is(Items.CROSSBOW) || mainHandItem.getItem() instanceof BowItem) {
            shootProjectile(shooter,target,distanceFactor,0);
            boolean multishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT,mainHandItem) > 0;
            if (multishot) {
                shootProjectile(shooter,target,distanceFactor,10);
                shootProjectile(shooter,target,distanceFactor,-10);
            }
            shrinkAmmunition(shooter);
            int damage = flag ? 3 : 1;
            if (multishot) damage *= 3;
            mainHandItem.hurtAndBreak(damage, shooter, (maid) -> maid.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_bow", this::hasProjectileWeapon), Pair.of("has_arrow", this::hasAmmunition));
    }

    private boolean hasProjectileWeapon(EntityMaid maid) {
        return (maid.getMainHandItem().getItem() instanceof BowItem) || (maid.getMainHandItem().is(Items.CROSSBOW));
    }

    private boolean hasBow(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof BowItem;
    }

    private boolean hasAmmunition(EntityMaid maid) {
        if (hasBow(maid)) return hasArrow(maid);
        return findAmmunition(maid) >= 0;
    }

    private boolean hasArrow(EntityMaid maid) {
        return findArrow(maid) >= 0;
    }

    private int findArrow(EntityMaid maid) {
        ItemStack mainHandItem = maid.getMainHandItem();
        if (mainHandItem.getItem() instanceof BowItem) {
            CombinedInvWrapper handler = maid.getAvailableInv(true);
            return ItemsUtil.findStackSlot(handler, ((BowItem) mainHandItem.getItem()).getAllSupportedProjectiles());
        }
        return -1;
    }

    private int findAmmunition(EntityMaid maid) {
        ItemStack mainHandItem = maid.getMainHandItem();
        if(mainHandItem.is(Items.CROSSBOW) || mainHandItem.getItem() instanceof BowItem) {
            CombinedInvWrapper handler = maid.getAvailableInv(true);
            return ItemsUtil.findStackSlot(handler, ARROW_OR_FIREWORK);
        }
        return -1;
    }

    private void shootProjectile(EntityMaid shooter, LivingEntity target, float distanceFactor, float pProjectileAngle) {
        Projectile projectile = getAmmunition(shooter,distanceFactor);
        if (projectile == null) return;
        ItemStack mainHandItem = shooter.getMainHandItem();
        shooter.shootCrossbowProjectile(target, mainHandItem, projectile,pProjectileAngle);
        shooter.level.addFreshEntity(projectile);
    }

    @Nullable
    private Projectile getAmmunition(EntityMaid maid,float chargeTime) {
        int ammunitionSlot = findAmmunition(maid);
        int arrowSlot = findArrow(maid);
        if (ammunitionSlot < 0) {
            return null;
        }
        Projectile projectile;
        CombinedInvWrapper handler = maid.getAvailableInv(true);
        ItemStack mainHandItem = maid.getMainHandItem();
        ItemStack ammunitionStack = handler.getStackInSlot(ammunitionSlot);
        ItemStack arrowStack = handler.getStackInSlot(arrowSlot);
        if(mainHandItem.is(Items.CROSSBOW)){
            if (ammunitionStack.is(Items.FIREWORK_ROCKET)) {
                projectile = new FireworkRocketEntity(maid.level, ammunitionStack, maid, maid.getX(), maid.getEyeY() - (double) 0.15F, maid.getZ(), true);
            } else {
                ArrowItem arrowitem = (ArrowItem) (ammunitionStack.getItem() instanceof ArrowItem ? ammunitionStack.getItem() : Items.ARROW);
                AbstractArrow ammunition = arrowitem.createArrow(maid.level(), ammunitionStack, maid);

                ammunition.setSoundEvent(SoundEvents.CROSSBOW_HIT);
                ammunition.setShotFromCrossbow(true);
                int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, mainHandItem);
                if (i > 0) {
                    ammunition.setPierceLevel((byte) i);
                }

                projectile = ammunition;
            }
        } else {
            if (arrowSlot < 0) return null;
            AbstractArrow arrowEntity = ProjectileUtil.getMobArrow(maid, arrowStack, chargeTime);
            if (mainHandItem.getItem() instanceof BowItem) {
                arrowEntity = ((BowItem) mainHandItem.getItem()).customArrow(arrowEntity);
            }
            // 无限附魔不存在或者小于 0 时
            if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, mainHandItem) <= 0) {
                // 记得把箭设置为可以拾起状态
                arrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
            }
            projectile = arrowEntity;
        }

        return projectile;
    }

    private void shrinkAmmunition(EntityMaid maid) {
        int ammunitionSlot = findAmmunition(maid);
        int arrowSlot = findArrow(maid);
        CombinedInvWrapper handler = maid.getAvailableInv(true);
        ItemStack mainHandItem = maid.getMainHandItem();
        ItemStack ammunitionStack = handler.getStackInSlot(ammunitionSlot);
        ItemStack arrowStack = handler.getStackInSlot(arrowSlot);
        if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.INFINITY_ARROWS, mainHandItem) <= 0) {
            if (mainHandItem.is(Items.CROSSBOW)) {
                ammunitionStack.shrink(1);
                handler.setStackInSlot(ammunitionSlot,ammunitionStack);
            }else {
                arrowStack.shrink(1);
                handler.setStackInSlot(arrowSlot, arrowStack);
            }
        }
    }


    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
