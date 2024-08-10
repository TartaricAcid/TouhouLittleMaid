package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFeedAnimalTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TaskFeedAnimal implements IAttackTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "feed_animal");
    private static final int MAX_STOP_ATTACK_DISTANCE = 8;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.WHEAT.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FEED_ANIMAL.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasAssaultWeapon, this::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create(
                (target) -> !hasAssaultWeapon(maid) || farAway(target, maid));
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6f);
        BehaviorControl<Mob> attackTargetTask = MeleeAttack.create(20);

        return Lists.newArrayList(
                Pair.of(5, new MaidFeedAnimalTask(0.6f, MaidConfig.FEED_ANIMAL_MAX_NUMBER.get())),
                Pair.of(6, supplementedTask),
                Pair.of(6, findTargetTask),
                Pair.of(6, moveToTargetTask),
                Pair.of(6, attackTargetTask)
        );
    }

    private Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        long animalCount = this.getEntities(maid)
                .find(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof Animal).count();

        if (animalCount < (MaidConfig.FEED_ANIMAL_MAX_NUMBER.get() - 2)) {
            return Optional.empty();
        }

        return this.getEntities(maid)
                .find(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof Animal)
                .filter(e -> ((Animal) e).getAge() == 0)
                .filter(e -> ((Animal) e).canFallInLove())
                .filter(e -> ItemsUtil.isStackIn(maid.getAvailableInv(false), ((Animal) e)::isFood))
                .filter(maid::canPathReach)
                .findFirst();
    }

    @Override
    public boolean canAttack(EntityMaid maid, LivingEntity target) {
        return true;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("can_feed", Predicates.alwaysTrue()), Pair.of("assault_weapon", this::hasAssaultWeapon));
    }

    private NearestVisibleLivingEntities getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return maid.getMainHandItem().getAttributeModifiers().modifiers()
                .stream()
                .anyMatch(modifier -> modifier.attribute().value() == Attributes.ATTACK_DAMAGE);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
