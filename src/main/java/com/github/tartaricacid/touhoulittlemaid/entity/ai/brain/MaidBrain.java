package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;

public final class MaidBrain {
    public static ImmutableList<MemoryModuleType<?>> getMemoryTypes() {
        return ImmutableList.of(
                MemoryModuleType.PATH,
                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.NEAREST_HOSTILE,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.INTERACTION_TARGET
        );
    }

    public static ImmutableList<SensorType<? extends Sensor<? super EntityMaid>>> getSensorTypes() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.HURT_BY,
                InitEntities.MAID_HOSTILES_SENSOR.get()
        );
    }

    public static void registerBrainGoals(Brain<EntityMaid> brain) {
        registerCoreGoals(brain);
        registerIdleGoals(brain);
        registerPanicGoals(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
    }

    private static void registerCoreGoals(Brain<EntityMaid> brain) {
        Pair<Integer, SwimTask> swim = Pair.of(0, new SwimTask(0.5F));
        Pair<Integer, InteractWithDoorTask> interactWithDoor = Pair.of(0, new InteractWithDoorTask());
        Pair<Integer, LookTask> look = Pair.of(0, new LookTask(45, 90));
        Pair<Integer, MaidPanicTask> maidPanic = Pair.of(0, new MaidPanicTask());
        Pair<Integer, WalkToTargetTask> walkToTarget = Pair.of(1, new WalkToTargetTask());
        Pair<Integer, MaidFollowOwnerTask> followOwner = Pair.of(1, new MaidFollowOwnerTask(0.5f, 5, 2));

        brain.addActivity(Activity.CORE, ImmutableList.of(swim, interactWithDoor, look, maidPanic, walkToTarget, followOwner));
    }

    private static void registerIdleGoals(Brain<EntityMaid> brain) {
        Pair<Task<? super CreatureEntity>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> walkRandomly = Pair.of(new MaidWalkRandomlyTask(0.3f, 3, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> noLook = Pair.of(new DummyTask(20, 40), 2);

        Pair<Integer, FirstShuffledTask<CreatureEntity>> shuffled = Pair.of(1, new FirstShuffledTask<>(
                ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, walkRandomly, noLook)));
        Pair<Integer, MaidBegTask> beg = Pair.of(1, new MaidBegTask());
        Pair<Integer, FindInteractionAndLookTargetTask> getPlayer = Pair.of(1, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 6));

        brain.addActivity(Activity.IDLE, ImmutableList.of(shuffled, beg, getPlayer));
    }

    private static void registerPanicGoals(Brain<EntityMaid> brain) {
        Pair<Integer, MaidClearHurtTask> clearHurt = Pair.of(1, new MaidClearHurtTask());
        Pair<Integer, MaidRunAwayTask<? extends Entity>> runAway = Pair.of(1, MaidRunAwayTask.entity(MemoryModuleType.NEAREST_HOSTILE, 0.5f, 6, false));
        Pair<Integer, MaidRunAwayTask<? extends Entity>> runAwayHurt = Pair.of(1, MaidRunAwayTask.entity(MemoryModuleType.HURT_BY_ENTITY, 0.5f, 6, false));

        brain.addActivity(Activity.PANIC, ImmutableList.of(clearHurt, runAway, runAwayHurt));
    }
}
