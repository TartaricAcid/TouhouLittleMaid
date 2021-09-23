package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;

import java.util.Collections;
import java.util.List;

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
                MemoryModuleType.INTERACTION_TARGET,
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                InitEntities.TARGET_POS.get()
        );
    }

    public static ImmutableList<SensorType<? extends Sensor<? super EntityMaid>>> getSensorTypes() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.HURT_BY,
                InitEntities.MAID_HOSTILES_SENSOR.get(),
                InitEntities.MAID_PICKUP_ENTITIES_SENSOR.get()
        );
    }

    public static void registerBrainGoals(Brain<EntityMaid> brain, EntityMaid maid) {
        registerSchedule(brain, maid);
        registerCoreGoals(brain);
        registerPanicGoals(brain);
        registerAwaitGoals(brain);
        registerIdleGoals(brain);
        registerWorkGoals(brain, maid);
        registerRestGoals(brain);

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(maid.level.getDayTime(), maid.level.getGameTime());
    }

    private static void registerSchedule(Brain<EntityMaid> brain, EntityMaid maid) {
        switch (maid.getSchedule()) {
            case ALL:
                brain.setSchedule(InitEntities.MAID_ALL_DAY_SCHEDULES.get());
                break;
            case NIGHT:
                brain.setSchedule(InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get());
                break;
            case DAY:
            default:
                brain.setSchedule(InitEntities.MAID_DAY_SHIFT_SCHEDULES.get());
                break;
        }
    }

    private static void registerCoreGoals(Brain<EntityMaid> brain) {
        Pair<Integer, Task<? super EntityMaid>> swim = Pair.of(0, new SwimTask(0.5F));
        Pair<Integer, Task<? super EntityMaid>> look = Pair.of(0, new LookTask(45, 90));
        Pair<Integer, Task<? super EntityMaid>> maidPanic = Pair.of(0, new MaidPanicTask());
        Pair<Integer, Task<? super EntityMaid>> maidAwait = Pair.of(0, new MaidAwaitTask());
        Pair<Integer, Task<? super EntityMaid>> interactWithDoor = Pair.of(1, new InteractWithDoorTask());
        Pair<Integer, Task<? super EntityMaid>> walkToTarget = Pair.of(1, new WalkToTargetTask());
        Pair<Integer, Task<? super EntityMaid>> followOwner = Pair.of(1, new MaidFollowOwnerTask(0.5f, 5, 2));
        Pair<Integer, Task<? super EntityMaid>> pickupItem = Pair.of(1, new MaidPickupEntitiesTask(EntityMaid::isPickup, 8, 0.6f));
        Pair<Integer, Task<? super EntityMaid>> clearSleep = Pair.of(99, new MaidClearSleepTask());

        brain.addActivity(Activity.CORE, ImmutableList.of(swim, look, maidPanic, maidAwait, interactWithDoor, walkToTarget, followOwner, pickupItem, clearSleep));
    }

    private static void registerIdleGoals(Brain<EntityMaid> brain) {
        Pair<Task<? super EntityMaid>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> walkRandomly = Pair.of(new MaidWalkRandomlyTask(0.3f, 3, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> noLook = Pair.of(new DummyTask(20, 40), 2);

        Pair<Integer, Task<? super EntityMaid>> shuffled = Pair.of(1, new FirstShuffledTask<>(
                ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, walkRandomly, noLook)));
        Pair<Integer, Task<? super EntityMaid>> getPlayer = Pair.of(1, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 6));
        Pair<Integer, Task<? super EntityMaid>> beg = Pair.of(1, new MaidBegTask());
        Pair<Integer, Task<? super EntityMaid>> updateActivity = Pair.of(99, new UpdateActivityTask());

        brain.addActivity(Activity.IDLE, ImmutableList.of(shuffled, getPlayer, beg, updateActivity));
    }

    private static void registerWorkGoals(Brain<EntityMaid> brain, EntityMaid maid) {
        Pair<Integer, Task<? super EntityMaid>> updateActivity = Pair.of(99, new UpdateActivityTask());
        List<Pair<Integer, Task<? super EntityMaid>>> pairMaidList = maid.getTask().createBrainTasks(maid);
        if (pairMaidList.isEmpty()) {
            pairMaidList = Collections.singletonList(updateActivity);
        } else {
            pairMaidList.add(updateActivity);
        }
        brain.addActivity(Activity.WORK, ImmutableList.copyOf(pairMaidList));
    }

    private static void registerRestGoals(Brain<EntityMaid> brain) {
        Pair<Integer, Task<? super EntityMaid>> findBed = Pair.of(2, new MaidFindBedTask(0.6f, 12));
        Pair<Integer, Task<? super EntityMaid>> sleep = Pair.of(2, new MaidSleepTask());
        Pair<Integer, Task<? super EntityMaid>> updateActivity = Pair.of(99, new UpdateActivityTask());

        brain.addActivity(Activity.REST, ImmutableList.of(findBed, sleep, updateActivity));
    }

    private static void registerPanicGoals(Brain<EntityMaid> brain) {
        Pair<Integer, Task<? super EntityMaid>> clearHurt = Pair.of(1, new MaidClearHurtTask());
        Pair<Integer, Task<? super EntityMaid>> runAway = Pair.of(1, MaidRunAwayTask.entity(MemoryModuleType.NEAREST_HOSTILE, 0.5f, 6, false));
        Pair<Integer, Task<? super EntityMaid>> runAwayHurt = Pair.of(1, MaidRunAwayTask.entity(MemoryModuleType.HURT_BY_ENTITY, 0.5f, 6, false));

        brain.addActivity(Activity.PANIC, ImmutableList.of(clearHurt, runAway, runAwayHurt));
    }

    private static void registerAwaitGoals(Brain<EntityMaid> brain) {
        Pair<Task<? super EntityMaid>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> noLook = Pair.of(new DummyTask(30, 60), 2);

        Pair<Integer, Task<? super EntityMaid>> shuffled = Pair.of(1, new FirstShuffledTask<>(
                ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, noLook)));
        Pair<Integer, Task<? super EntityMaid>> updateActivity = Pair.of(99, new UpdateActivityTask());

        brain.addActivity(Activity.RIDE, ImmutableList.of(shuffled, updateActivity));
    }
}
