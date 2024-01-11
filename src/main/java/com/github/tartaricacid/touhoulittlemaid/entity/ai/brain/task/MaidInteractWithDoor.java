package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.OptionalBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.*;

public class MaidInteractWithDoor {
    private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 20;
    private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 8;
    private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2;

    public static BehaviorControl<LivingEntity> create() {
        MutableObject<Node> mutableObject = new MutableObject<>();
        MutableInt mutableInt = new MutableInt();
        return BehaviorBuilder.create((instance) -> instance.group(instance.present(MemoryModuleType.PATH), instance.registered(MemoryModuleType.DOORS_TO_CLOSE),
                instance.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply(instance, (pathMemory, doorToCloseMemory, livingEntityMemory) -> (serverLevel, entity, time) -> {
            Path path = instance.get(pathMemory);
            Optional<Set<GlobalPos>> doorToClosePos = instance.tryGet(doorToCloseMemory);
            if (!path.notStarted() && !path.isDone()) {
                if (Objects.equals(mutableObject.getValue(), path.getNextNode())) {
                    mutableInt.setValue(COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE);
                } else if (mutableInt.decrementAndGet() > 0) {
                    return false;
                }
                mutableObject.setValue(path.getNextNode());
                Node previousNode = path.getPreviousNode();
                Node nextNode = path.getNextNode();
                BlockPos previousNodeBlockPos = previousNode.asBlockPos();
                BlockState previousNodeBlockState = serverLevel.getBlockState(previousNodeBlockPos);
                if (previousNodeBlockState.is(BlockTags.WOODEN_DOORS, (stateBase) -> stateBase.getBlock() instanceof DoorBlock)) {
                    DoorBlock doorBlock = (DoorBlock) previousNodeBlockState.getBlock();
                    if (!doorBlock.isOpen(previousNodeBlockState)) {
                        doorBlock.setOpen(entity, serverLevel, previousNodeBlockState, previousNodeBlockPos, true);
                    }
                    doorToClosePos = rememberDoorToClose(doorToCloseMemory, doorToClosePos, serverLevel, previousNodeBlockPos);
                }
                BlockPos nextNodeBlockPos = nextNode.asBlockPos();
                BlockState nextNodeBlockState = serverLevel.getBlockState(nextNodeBlockPos);
                if (nextNodeBlockState.is(BlockTags.WOODEN_DOORS, (stateBase) -> stateBase.getBlock() instanceof DoorBlock)) {
                    DoorBlock doorBlock = (DoorBlock) nextNodeBlockState.getBlock();
                    if (!doorBlock.isOpen(nextNodeBlockState)) {
                        doorBlock.setOpen(entity, serverLevel, nextNodeBlockState, nextNodeBlockPos, true);
                        doorToClosePos = rememberDoorToClose(doorToCloseMemory, doorToClosePos, serverLevel, nextNodeBlockPos);
                    }
                }
                doorToClosePos.ifPresent((doorPos) -> closeDoorsThatIHaveOpenedOrPassedThrough(serverLevel, entity, previousNode, nextNode, doorPos, instance.tryGet(livingEntityMemory)));
                return true;
            } else {
                return false;
            }
        }));
    }

    public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel serverLevel, LivingEntity entity, @Nullable Node previous, @Nullable Node next, Set<GlobalPos> doorPositions, Optional<List<LivingEntity>> nearestLivingEntities) {
        Iterator<GlobalPos> doorPosIterator = doorPositions.iterator();
        while (doorPosIterator.hasNext()) {
            GlobalPos globalPos = doorPosIterator.next();
            BlockPos blockPos = globalPos.pos();
            if ((previous == null || !previous.asBlockPos().equals(blockPos)) && (next == null || !next.asBlockPos().equals(blockPos))) {
                if (isDoorTooFarAway(serverLevel, entity, globalPos)) {
                    doorPosIterator.remove();
                } else {
                    BlockState blockstate = serverLevel.getBlockState(blockPos);
                    if (!blockstate.is(BlockTags.WOODEN_DOORS, (stateBase) -> stateBase.getBlock() instanceof DoorBlock)) {
                        doorPosIterator.remove();
                    } else {
                        DoorBlock doorblock = (DoorBlock) blockstate.getBlock();
                        if (!doorblock.isOpen(blockstate)) {
                            doorPosIterator.remove();
                        } else if (areOtherMobsComingThroughDoor(entity, blockPos, nearestLivingEntities)) {
                            doorPosIterator.remove();
                        } else {
                            doorblock.setOpen(entity, serverLevel, blockstate, blockPos, false);
                            doorPosIterator.remove();
                        }
                    }
                }
            }
        }

    }

    private static boolean areOtherMobsComingThroughDoor(LivingEntity entity, BlockPos pos, Optional<List<LivingEntity>> nearestLivingEntities) {
        return nearestLivingEntities.map(entities -> entities.stream()
                .filter((livingEntity) -> livingEntity.getType() == entity.getType())
                .filter((livingEntity) -> pos.closerToCenterThan(livingEntity.position(), MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS))
                .anyMatch((livingEntity) -> isMobComingThroughDoor(livingEntity.getBrain(), pos))).orElse(false);
    }

    private static boolean isMobComingThroughDoor(Brain<?> brain, BlockPos pos) {
        if (!brain.hasMemoryValue(MemoryModuleType.PATH)) {
            return false;
        } else {
            Path path = brain.getMemory(MemoryModuleType.PATH).get();
            if (path.isDone()) {
                return false;
            } else {
                Node previousNode = path.getPreviousNode();
                if (previousNode == null) {
                    return false;
                } else {
                    Node nextNode = path.getNextNode();
                    return pos.equals(previousNode.asBlockPos()) || pos.equals(nextNode.asBlockPos());
                }
            }
        }
    }

    private static boolean isDoorTooFarAway(ServerLevel level, LivingEntity entity, GlobalPos pos) {
        return pos.dimension() != level.dimension() || !pos.pos().closerToCenterThan(entity.position(), SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN);
    }

    private static Optional<Set<GlobalPos>> rememberDoorToClose(MemoryAccessor<OptionalBox.Mu, Set<GlobalPos>> doorsToClose, Optional<Set<GlobalPos>> doorPositions, ServerLevel level, BlockPos blockPos) {
        GlobalPos globalPos = GlobalPos.of(level.dimension(), blockPos);
        return Optional.of(doorPositions.map((pos) -> {
            pos.add(globalPos);
            return pos;
        }).orElseGet(() -> {
            Set<GlobalPos> posSet = Sets.newHashSet(globalPos);
            doorsToClose.set(posSet);
            return posSet;
        }));
    }
}
