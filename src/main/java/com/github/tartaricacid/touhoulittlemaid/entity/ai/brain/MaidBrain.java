package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

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
                MemoryModuleType.ATTACK_TARGET,
                MemoryModuleType.ATTACK_COOLING_DOWN,
                InitEntities.TARGET_POS.get()
        );
    }

    public static ImmutableList<SensorType<? extends Sensor<? super EntityMaid>>> getSensorTypes() {
        return ImmutableList.of(
                InitEntities.MAID_NEAREST_LIVING_ENTITY_SENSOR.get(),
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
        brain.updateActivityFromSchedule(maid.level().getDayTime(), maid.level().getGameTime());
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
        Pair<Integer, BehaviorControl<? super EntityMaid>> swim = Pair.of(0, new Swim(0.8f));
        Pair<Integer, BehaviorControl<? super EntityMaid>> look = Pair.of(0, new LookAtTargetSink(45, 90));
        Pair<Integer, BehaviorControl<? super EntityMaid>> maidPanic = Pair.of(1, new MaidPanicTask());
        Pair<Integer, BehaviorControl<? super EntityMaid>> maidAwait = Pair.of(1, new MaidAwaitTask());
        Pair<Integer, BehaviorControl<? super EntityMaid>> interactWithDoor = Pair.of(2, MaidInteractWithDoor.create());
        Pair<Integer, BehaviorControl<? super EntityMaid>> walkToTarget = Pair.of(2, new MoveToTargetSink());
        Pair<Integer, BehaviorControl<? super EntityMaid>> followOwner = Pair.of(3, new MaidFollowOwnerTask(0.5f, 2));
        Pair<Integer, BehaviorControl<? super EntityMaid>> healSelf = Pair.of(3, new MaidHealSelfTask());
        Pair<Integer, BehaviorControl<? super EntityMaid>> pickupItem = Pair.of(10, new MaidPickupEntitiesTask(EntityMaid::isPickup, 0.6f));
        Pair<Integer, BehaviorControl<? super EntityMaid>> clearSleep = Pair.of(99, new MaidClearSleepTask());

        brain.addActivity(Activity.CORE, ImmutableList.of(swim, look, maidPanic, maidAwait, interactWithDoor, walkToTarget, followOwner, healSelf, pickupItem, clearSleep));
    }

    private static void registerIdleGoals(Brain<EntityMaid> brain) {
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToPlayer = Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToMaid = Pair.of(SetEntityLookTarget.create(EntityMaid.TYPE, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToWolf = Pair.of(SetEntityLookTarget.create(EntityType.WOLF, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToCat = Pair.of(SetEntityLookTarget.create(EntityType.CAT, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToParrot = Pair.of(SetEntityLookTarget.create(EntityType.PARROT, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> walkRandomly = Pair.of(RandomStroll.stroll(0.3f, 5, 3), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> noLook = Pair.of(new DoNothing(40, 80), 2);
        BehaviorControl<EntityMaid> firstShuffledTask = new MaidRunOne(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, walkRandomly, noLook));

        Pair<Integer, BehaviorControl<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        Pair<Integer, BehaviorControl<? super EntityMaid>> homeMeal = Pair.of(6, new MaidFindHomeMealTask(0.6f, 2));
        Pair<Integer, BehaviorControl<? super EntityMaid>> joy = Pair.of(7, new MaidJoyTask(0.6f, 2));
        Pair<Integer, BehaviorControl<? super EntityMaid>> supplemented = Pair.of(10, firstShuffledTask);
        Pair<Integer, BehaviorControl<? super EntityMaid>> updateActivity = Pair.of(99, new MaidUpdateActivityFromSchedule());

        brain.addActivity(Activity.IDLE, ImmutableList.of(beg, homeMeal, joy, supplemented, updateActivity));
    }

    private static void registerWorkGoals(Brain<EntityMaid> brain, EntityMaid maid) {
        Pair<Integer, BehaviorControl<? super EntityMaid>> updateActivity = Pair.of(99, new MaidUpdateActivityFromSchedule());
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> pairMaidList = maid.getTask().createBrainTasks(maid);
        if (pairMaidList.isEmpty()) {
            pairMaidList = Lists.newArrayList(updateActivity);
        } else {
            pairMaidList.add(updateActivity);
        }
        pairMaidList.add(Pair.of(6, new MaidBegTask()));
        pairMaidList.add(Pair.of(7, new MaidWorkMealTask()));
        brain.addActivity(Activity.WORK, ImmutableList.copyOf(pairMaidList));
    }

    private static void registerRestGoals(Brain<EntityMaid> brain) {
        Pair<Integer, BehaviorControl<? super EntityMaid>> bed = Pair.of(5, new MaidBedTask(0.6f, 2));
        Pair<Integer, BehaviorControl<? super EntityMaid>> updateActivity = Pair.of(99, new MaidUpdateActivityFromSchedule());

        brain.addActivity(Activity.REST, ImmutableList.of(bed, updateActivity));
    }

    private static void registerPanicGoals(Brain<EntityMaid> brain) {
        Pair<Integer, BehaviorControl<? super EntityMaid>> clearHurt = Pair.of(5, new MaidClearHurtTask());
        Pair<Integer, BehaviorControl<? super EntityMaid>> runAway = Pair.of(5, MaidRunAwayTask.entity(MemoryModuleType.NEAREST_HOSTILE, 0.7f, false));
        Pair<Integer, BehaviorControl<? super EntityMaid>> runAwayHurt = Pair.of(5, MaidRunAwayTask.entity(MemoryModuleType.HURT_BY_ENTITY, 0.7f, false));

        brain.addActivity(Activity.PANIC, ImmutableList.of(clearHurt, runAway, runAwayHurt));
    }

    private static void registerAwaitGoals(Brain<EntityMaid> brain) {
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToPlayer = Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToMaid = Pair.of(SetEntityLookTarget.create(EntityMaid.TYPE, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToWolf = Pair.of(SetEntityLookTarget.create(EntityType.WOLF, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToCat = Pair.of(SetEntityLookTarget.create(EntityType.CAT, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToParrot = Pair.of(SetEntityLookTarget.create(EntityType.PARROT, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> noLook = Pair.of(new DoNothing(30, 60), 2);

        Pair<Integer, BehaviorControl<? super EntityMaid>> shuffled = Pair.of(5, new MaidRunOne(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, noLook)));
        Pair<Integer, BehaviorControl<? super EntityMaid>> updateActivity = Pair.of(99, new MaidUpdateActivityFromSchedule());

        brain.addActivity(Activity.RIDE, ImmutableList.of(shuffled, updateActivity));
    }
}
