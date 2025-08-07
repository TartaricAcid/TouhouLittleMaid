package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidUseShieldTask;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class TaskAttack implements IAttackTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "attack");

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
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasAssaultWeapon, IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target -> !hasAssaultWeapon(maid) || farAway(target, maid));
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6f);
        BehaviorControl<Mob> attackTargetTask = MeleeAttack.create(calculateCooldown(maid));
        MaidUseShieldTask maidUseShieldTask = new MaidUseShieldTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, attackTargetTask),
                Pair.of(5, maidUseShieldTask)
        );
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasAssaultWeapon, IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(target -> !hasAssaultWeapon(maid) || farAway(target, maid));
        BehaviorControl<Mob> attackTargetTask = MeleeAttack.create(calculateCooldown(maid));
        MaidUseShieldTask maidUseShieldTask = new MaidUseShieldTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, attackTargetTask),
                Pair.of(5, maidUseShieldTask)
        );
    }

    @Override
    public boolean hasExtraAttack(EntityMaid maid, Entity target) {
        return maid.getOffhandItem().is(InitItems.EXTINGUISHER.get()) && target.fireImmune();
    }

    @Override
    public boolean doExtraAttack(EntityMaid maid, Entity target) {
        Level world = maid.level;
        AABB aabb = target.getBoundingBox().inflate(1.5, 1, 1.5);
        List<EntityExtinguishingAgent> extinguishingAgents = world.getEntitiesOfClass(EntityExtinguishingAgent.class, aabb, Entity::isAlive);
        if (extinguishingAgents.isEmpty()) {
            world.addFreshEntity(new EntityExtinguishingAgent(world, target.position()));
            maid.getOffhandItem().hurtAndBreak(1, maid, EquipmentSlot.OFFHAND);
            return true;
        }
        return false;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("assault_weapon", this::hasAssaultWeapon), Pair.of("extinguisher", this::hasExtinguisher));
    }

    @Override
    public boolean isWeapon(EntityMaid maid, ItemStack stack) {
        return stack.getAttributeModifiers().modifiers()
                .stream()
                .anyMatch(modifier -> modifier.attribute().is(Attributes.ATTACK_DAMAGE));
    }

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return isWeapon(maid, maid.getMainHandItem());
    }

    private boolean hasExtinguisher(EntityMaid maid) {
        return maid.getOffhandItem().is(InitItems.EXTINGUISHER.get());
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

    private int calculateCooldown(EntityMaid maid){
        // 1. 基础攻击速度
        double attackSpeed = 4;

        // 2. 叠加手持武器的攻击速度修正
        ItemStack weapon = maid.getMainHandItem();
        if(!weapon.isEmpty()){
            AtomicReference<Double> itemSpeed = new AtomicReference<>((double) 0);
            ItemAttributeModifiers attributeModifiers = weapon.getAttributeModifiers();
            attributeModifiers.forEach(EquipmentSlot.MAINHAND,(a,b)->{
                if(b.is(ResourceLocation.fromNamespaceAndPath("minecraft","base_attack_speed"))){
                    itemSpeed.set(b.amount());
                }
            });
            attackSpeed+=itemSpeed.get();
        }
        // 3. 叠加状态效果的影响（急迫/挖掘疲劳）
        // 急迫：每级增加10%攻击速度（公式：1 + 0.1 * 等级）
        if (maid.hasEffect(MobEffects.DIG_SPEED)) {
            int amplifier = maid.getEffect(MobEffects.DIG_SPEED).getAmplifier();
            attackSpeed *= (1.0 + 0.1 * (amplifier + 1));
        }

        // 挖掘疲劳：每级降低10%攻击速度（公式：1 - 0.1 * 等级）
        if (maid.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            int amplifier = maid.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier();
            attackSpeed *= (1.0 - 0.1 * (amplifier + 1));
        }

        // 防御性处理：确保攻击速度不会低于极小值（避免后续除零）
        attackSpeed=Math.max(attackSpeed,0.1);
        int result = (int) Math.round(20.0 / attackSpeed);
        //TouhouLittleMaid.LOGGER.info("计算结果:"+result);
        return result;
    }
}
