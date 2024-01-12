package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

public class MaidInteractWithDoor extends Task<LivingEntity> {
    private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 8;
    private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2;
    @Nullable
    private PathPoint lastCheckedNode;
    private int remainingCooldown;

    public MaidInteractWithDoor() {
        super(ImmutableMap.of(MemoryModuleType.PATH, MemoryModuleStatus.VALUE_PRESENT,
                MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleStatus.REGISTERED));
    }

    protected boolean checkExtraStartConditions(ServerWorld serverWorld, LivingEntity entity) {
        Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
        if (!path.notStarted() && !path.isDone()) {
            if (!Objects.equals(this.lastCheckedNode, path.getNextNode())) {
                this.remainingCooldown = 20;
                return true;
            } else {
                if (this.remainingCooldown > 0) {
                    --this.remainingCooldown;
                }
                return this.remainingCooldown == 0;
            }
        } else {
            return false;
        }
    }

    protected void start(ServerWorld serverWorld, LivingEntity entity, long gameTime) {
        Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
        this.lastCheckedNode = path.getNextNode();
        PathPoint previousNode = path.getPreviousNode();
        PathPoint nextNode = path.getNextNode();
        BlockPos blockpos = previousNode.asBlockPos();
        BlockState blockstate = serverWorld.getBlockState(blockpos);
        if (blockstate.is(BlockTags.WOODEN_DOORS)) {
            DoorBlock doorblock = (DoorBlock) blockstate.getBlock();
            if (!doorblock.isOpen(blockstate)) {
                doorblock.setOpen(serverWorld, blockstate, blockpos, true);
            }
            this.rememberDoorToClose(serverWorld, entity, blockpos);
        }
        BlockPos blockPos = nextNode.asBlockPos();
        BlockState blockState = serverWorld.getBlockState(blockPos);
        if (blockState.is(BlockTags.WOODEN_DOORS)) {
            DoorBlock doorBlock = (DoorBlock) blockState.getBlock();
            if (!doorBlock.isOpen(blockState)) {
                doorBlock.setOpen(serverWorld, blockState, blockPos, true);
                this.rememberDoorToClose(serverWorld, entity, blockPos);
            }
        }
        closeDoorsThatIHaveOpenedOrPassedThrough(serverWorld, entity, previousNode, nextNode);
    }

    public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerWorld serverWorld, LivingEntity entity, @Nullable PathPoint pathPoint, @Nullable PathPoint pathPoint1) {
        Brain<?> brain = entity.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
            Iterator<GlobalPos> iterator = brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get().iterator();
            while (iterator.hasNext()) {
                GlobalPos globalpos = iterator.next();
                BlockPos blockpos = globalpos.pos();
                if ((pathPoint == null || !pathPoint.asBlockPos().equals(blockpos)) && (pathPoint1 == null || !pathPoint1.asBlockPos().equals(blockpos))) {
                    if (isDoorTooFarAway(serverWorld, entity, globalpos)) {
                        iterator.remove();
                    } else {
                        BlockState blockstate = serverWorld.getBlockState(blockpos);
                        if (!blockstate.is(BlockTags.WOODEN_DOORS)) {
                            iterator.remove();
                        } else {
                            DoorBlock doorblock = (DoorBlock) blockstate.getBlock();
                            if (!doorblock.isOpen(blockstate)) {
                                iterator.remove();
                            } else if (areOtherMobsComingThroughDoor(serverWorld, entity, blockpos)) {
                                iterator.remove();
                            } else {
                                doorblock.setOpen(serverWorld, blockstate, blockpos, false);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean areOtherMobsComingThroughDoor(ServerWorld serverWorld, LivingEntity entity, BlockPos blockPos) {
        Brain<?> brain = entity.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.LIVING_ENTITIES) && brain.getMemory(MemoryModuleType.LIVING_ENTITIES)
                .get().stream().filter((livingEntity) -> livingEntity.getType() == entity.getType())
                .filter((livingEntity) -> blockPos.closerThan(livingEntity.position(), MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS))
                .anyMatch((livingEntity) -> isMobComingThroughDoor(serverWorld, livingEntity, blockPos));
    }

    private static boolean isMobComingThroughDoor(ServerWorld serverWorld, LivingEntity entity, BlockPos blockPos) {
        if (!entity.getBrain().hasMemoryValue(MemoryModuleType.PATH)) {
            return false;
        } else {
            Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
            if (path.isDone()) {
                return false;
            } else {
                PathPoint pathpoint = path.getPreviousNode();
                if (pathpoint == null) {
                    return false;
                } else {
                    PathPoint nextNode = path.getNextNode();
                    return blockPos.equals(pathpoint.asBlockPos()) || blockPos.equals(nextNode.asBlockPos());
                }
            }
        }
    }

    private static boolean isDoorTooFarAway(ServerWorld serverWorld, LivingEntity entity, GlobalPos globalPos) {
        return globalPos.dimension() != serverWorld.dimension() || !globalPos.pos().closerThan(entity.position(), SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN);
    }

    private void rememberDoorToClose(ServerWorld serverWorld, LivingEntity entity, BlockPos blockPos) {
        Brain<?> brain = entity.getBrain();
        GlobalPos globalpos = GlobalPos.of(serverWorld.dimension(), blockPos);
        if (brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).isPresent()) {
            brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get().add(globalpos);
        } else {
            brain.setMemory(MemoryModuleType.DOORS_TO_CLOSE, Sets.newHashSet(globalpos));
        }
    }
}