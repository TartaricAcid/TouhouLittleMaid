package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class TaskAttack implements IAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "attack");
    private static final int MAX_STOP_ATTACK_DISTANCE = 8;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.DIAMOND_SWORD.getDefaultInstance();
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.attackSound(maid, InitSounds.MAID_ATTACK.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        SupplementedTask<EntityMaid> supplementedTask = new SupplementedTask<>(this::hasAssaultWeapon,
                new ForgetAttackTargetTask<>(IAttackTask::findFirstValidAttackTarget));
        FindNewAttackTargetTask<EntityMaid> findTargetTask = new FindNewAttackTargetTask<>(
                (target) -> !hasAssaultWeapon(maid) || farAway(target, maid));
        MoveToTargetTask moveToTargetTask = new MoveToTargetTask(0.6f);
        AttackTargetTask attackTargetTask = new AttackTargetTask(20);

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, attackTargetTask)
        );
    }

    @Override
    public boolean hasExtraAttack(EntityMaid maid, Entity target) {
        return maid.getOffhandItem().getItem() == InitItems.EXTINGUISHER.get() && target.fireImmune();
    }

    @Override
    public boolean doExtraAttack(EntityMaid maid, Entity target) {
        World world = maid.level;
        AxisAlignedBB aabb = target.getBoundingBox().inflate(1.5, 1, 1.5);
        List<EntityExtinguishingAgent> extinguishingAgents = world.getEntitiesOfClass(EntityExtinguishingAgent.class, aabb, Entity::isAlive);
        if (extinguishingAgents.isEmpty()) {
            world.addFreshEntity(new EntityExtinguishingAgent(world, target.position()));
            maid.getOffhandItem().hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(Hand.OFF_HAND));
            return true;
        }
        return false;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("assault_weapon", this::hasAssaultWeapon), Pair.of("extinguisher", this::hasExtinguisher));
    }

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return maid.getMainHandItem().getAttributeModifiers(EquipmentSlotType.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE);
    }

    private boolean hasExtinguisher(EntityMaid maid) {
        return maid.getOffhandItem().getItem() == InitItems.EXTINGUISHER.get();
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
