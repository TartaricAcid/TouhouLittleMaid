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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TaskFeedAnimal implements IAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "feed_animal");
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
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        SupplementedTask<EntityMaid> supplementedTask = new SupplementedTask<>(this::hasAssaultWeapon,
                new ForgetAttackTargetTask<>(this::findFirstValidAttackTarget));
        FindNewAttackTargetTask<EntityMaid> findTargetTask = new FindNewAttackTargetTask<>(
                (target) -> !hasAssaultWeapon(maid) || farAway(target, maid));
        MoveToTargetTask moveToTargetTask = new MoveToTargetTask(0.6f);
        AttackTargetTask attackTargetTask = new AttackTargetTask(20);

        return Lists.newArrayList(
                Pair.of(5, new MaidFeedAnimalTask(0.6f, MaidConfig.FEED_ANIMAL_MAX_NUMBER.get())),
                Pair.of(6, supplementedTask),
                Pair.of(6, findTargetTask),
                Pair.of(6, moveToTargetTask),
                Pair.of(6, attackTargetTask)
        );
    }

    private Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        long animalCount = this.getEntities(maid).stream()
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof AnimalEntity).count();

        if (animalCount < (MaidConfig.FEED_ANIMAL_MAX_NUMBER.get() - 2)) {
            return Optional.empty();
        }

        return this.getEntities(maid).stream()
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof AnimalEntity)
                .filter(e -> ((AnimalEntity) e).getAge() == 0)
                .filter(e -> ((AnimalEntity) e).canFallInLove())
                .filter(e -> ItemsUtil.isStackIn(maid.getAvailableInv(false), ((AnimalEntity) e)::isFood))
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

    private List<LivingEntity> getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Collections.emptyList());
    }

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return maid.getMainHandItem().getAttributeModifiers(EquipmentSlotType.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}
