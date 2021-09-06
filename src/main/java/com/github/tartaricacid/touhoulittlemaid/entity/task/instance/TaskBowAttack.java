package com.github.tartaricacid.touhoulittlemaid.entity.task.instance;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        return InitSounds.MAID_RANGE_ATTACK.get();
    }

    @Override
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        SupplementedTask<EntityMaid> supplementedTask = new SupplementedTask<>(this::hasBow,
                new ForgetAttackTargetTask<>(this::findRandomValidAttackTarget));
        FindNewAttackTargetTask<EntityMaid> findTargetTask = new FindNewAttackTargetTask<>(
                (target) -> !hasBow(maid) || farAway(target, maid));
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
        AbstractArrowEntity entityArrow = getArrow(shooter, distanceFactor);

        if (entityArrow != null) {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (mainHandItem.getItem() instanceof BowItem) {
                double x = target.getX() - shooter.getX();
                double y = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - entityArrow.position().y;
                double z = target.getZ() - shooter.getZ();
                double pitch = MathHelper.sqrt(x * x + z * z) * 0.15D;
                entityArrow.shoot(x, y + pitch, z, 1.6F, 1);
                mainHandItem.hurtAndBreak(1, shooter, (maid) -> maid.broadcastBreakEvent(Hand.MAIN_HAND));
                shooter.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
                shooter.level.addFreshEntity(entityArrow);
            }
        }
    }

    @Override
    public List<ITextComponent> getDescription() {
        return Collections.emptyList();
    }

    private boolean hasBow(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof BowItem;
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
    private AbstractArrowEntity getArrow(EntityMaid maid, float chargeTime) {
        int slot = findArrow(maid);
        if (slot < 0) {
            return null;
        }

        CombinedInvWrapper handler = maid.getAvailableInv(true);
        ItemStack arrowStack = handler.getStackInSlot(slot);
        ItemStack mainHandItem = maid.getMainHandItem();
        AbstractArrowEntity arrowEntity = ProjectileHelper.getMobArrow(maid, arrowStack, chargeTime);

        if (mainHandItem.getItem() instanceof BowItem) {
            arrowEntity = ((BowItem) mainHandItem.getItem()).customArrow(arrowEntity);
        }
        // 无限附魔不存在或者小于 0 时
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, mainHandItem) <= 0) {
            arrowStack.shrink(1);
            handler.setStackInSlot(slot, arrowStack);
            // 记得把箭设置为可以拾起状态
            arrowEntity.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
        }

        return arrowEntity;
    }

    private Optional<? extends LivingEntity> findRandomValidAttackTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).flatMap(
                mobs -> mobs.stream().filter(maid::canAttack).findFirst());
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
