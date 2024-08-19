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
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static com.github.tartaricacid.touhoulittlemaid.dataGen.EnchantmentKeys.getEnchantmentLevel;

public class TaskBowAttack implements IRangedAttackTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "ranged_attack");
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
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(e -> hasBow(e) && hasArrow(e), IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasBow(maid) || !hasArrow(maid) || farAway(target, maid));
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
        AbstractArrow entityArrow = getArrow(shooter, distanceFactor);

        if (entityArrow != null) {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (mainHandItem.getItem() instanceof BowItem) {
                double x = target.getX() - shooter.getX();
                double y = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - entityArrow.position().y;
                double z = target.getZ() - shooter.getZ();
                double pitch = Math.sqrt(x * x + z * z) * 0.15D;
                entityArrow.shoot(x, y + pitch, z, 1.6F, 1);
                mainHandItem.hurtAndBreak(1, shooter, EquipmentSlot.MAINHAND);
                shooter.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
                shooter.level.addFreshEntity(entityArrow);
            }
        }
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_bow", this::hasBow), Pair.of("has_arrow", this::hasArrow));
    }

    private boolean hasBow(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof BowItem;
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

    @Nullable
    private AbstractArrow getArrow(EntityMaid maid, float chargeTime) {
        int slot = findArrow(maid);
        if (slot < 0) {
            return null;
        }

        CombinedInvWrapper handler = maid.getAvailableInv(true);
        ItemStack arrowStack = handler.getStackInSlot(slot);
        ItemStack mainHandItem = maid.getMainHandItem();
        RegistryAccess access = maid.level.registryAccess();
        // TODO: 不确定是不是要这样改
        AbstractArrow arrowEntity = ProjectileUtil.getMobArrow(maid, arrowStack, chargeTime, mainHandItem);

        if (mainHandItem.getItem() instanceof BowItem) {
            arrowEntity = ((BowItem) mainHandItem.getItem()).customArrow(arrowEntity, arrowStack, mainHandItem);
        }
        // 无限附魔不存在或者小于 0 时
        if (getEnchantmentLevel(access,Enchantments.INFINITY,mainHandItem)  <= 0) {
            arrowStack.shrink(1);
            handler.setStackInSlot(slot, arrowStack);
            // 记得把箭设置为可以拾起状态
            arrowEntity.pickup = AbstractArrow.Pickup.ALLOWED;
        }

        // 箭伤害也和好感度挂钩
        AttributeInstance attackDamage = maid.getAttribute(Attributes.ATTACK_DAMAGE);
        double attackValue = 2.0;
        if (attackDamage != null) {
            attackValue = attackDamage.getBaseValue();
        }
        float multiplier = (float) (attackValue / 2.0f);
        arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * multiplier);

        return arrowEntity;
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}