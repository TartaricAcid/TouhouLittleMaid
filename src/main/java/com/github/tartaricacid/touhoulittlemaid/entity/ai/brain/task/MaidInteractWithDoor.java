package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

public class MaidInteractWithDoor extends Behavior<LivingEntity> {
    private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 3;
    private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 8;
    private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2;
    @Nullable
    private Node lastCheckedNode;
    private int remainingCooldown;

    public MaidInteractWithDoor() {
        super(ImmutableMap.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverWorld, LivingEntity entity) {
        Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
        if (!path.notStarted() && !path.isDone()) {
            if (!Objects.equals(this.lastCheckedNode, path.getNextNode())) {
                this.remainingCooldown = COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE;
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

    @Override
    protected void start(ServerLevel serverWorld, LivingEntity entity, long gameTime) {
        Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
        this.lastCheckedNode = path.getNextNode();
        Node previousNode = path.getPreviousNode();
        Node nextNode = path.getNextNode();
        BlockPos blockpos = previousNode.asBlockPos();
        BlockState blockstate = serverWorld.getBlockState(blockpos);
        if (blockstate.is(BlockTags.WOODEN_DOORS)) {
            DoorBlock doorblock = (DoorBlock) blockstate.getBlock();
            if (!doorblock.isOpen(blockstate)) {
                doorblock.setOpen(entity, serverWorld, blockstate, blockpos, true);
            }
            this.rememberDoorToClose(serverWorld, entity, blockpos);
        }
        BlockPos blockPos = nextNode.asBlockPos();
        BlockState blockState = serverWorld.getBlockState(blockPos);
        if (blockState.is(BlockTags.WOODEN_DOORS)) {
            DoorBlock doorBlock = (DoorBlock) blockState.getBlock();
            if (!doorBlock.isOpen(blockState)) {
                doorBlock.setOpen(entity, serverWorld, blockState, blockPos, true);
                this.rememberDoorToClose(serverWorld, entity, blockPos);
            }
        }
        closeDoorsThatIHaveOpenedOrPassedThrough(serverWorld, entity, previousNode, nextNode);
    }

    public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel serverWorld, LivingEntity entity, @Nullable Node pathPoint, @Nullable Node pathPoint1) {
        Brain<?> brain = entity.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
            Iterator<GlobalPos> iterator = brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get().iterator();
            while (iterator.hasNext()) {
                GlobalPos globalpos = iterator.next();
                BlockPos blockpos = globalpos.pos();
                if ((pathPoint == null || !pathPoint.asBlockPos().equals(blockpos) || (pathPoint1 != null && entity.blockPosition().equals(pathPoint1.asBlockPos())))
                        && (pathPoint1 == null || !pathPoint1.asBlockPos().equals(blockpos))) {
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
                                doorblock.setOpen(entity, serverWorld, blockstate, blockpos, false);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean areOtherMobsComingThroughDoor(ServerLevel serverWorld, LivingEntity entity, BlockPos blockPos) {
        Brain<?> brain = entity.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.NEAREST_LIVING_ENTITIES) && brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
                .get().stream().filter((livingEntity) -> livingEntity.getType() == entity.getType())
                .filter((livingEntity) -> blockPos.closerToCenterThan(livingEntity.position(), MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS))
                .anyMatch((livingEntity) -> isMobComingThroughDoor(serverWorld, livingEntity, blockPos));
    }

    private static boolean isMobComingThroughDoor(ServerLevel serverWorld, LivingEntity entity, BlockPos blockPos) {
        if (!entity.getBrain().hasMemoryValue(MemoryModuleType.PATH)) {
            return false;
        } else {
            Path path = entity.getBrain().getMemory(MemoryModuleType.PATH).get();
            if (path.isDone()) {
                return false;
            } else {
                Node pathpoint = path.getPreviousNode();
                if (pathpoint == null) {
                    return false;
                } else {
                    Node nextNode = path.getNextNode();
                    return blockPos.equals(pathpoint.asBlockPos()) || blockPos.equals(nextNode.asBlockPos());
                }
            }
        }
    }

    private static boolean isDoorTooFarAway(ServerLevel serverWorld, LivingEntity entity, GlobalPos globalPos) {
        return globalPos.dimension() != serverWorld.dimension() || !globalPos.pos().closerToCenterThan(entity.position(), SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN);
    }

    private void rememberDoorToClose(ServerLevel serverWorld, LivingEntity entity, BlockPos blockPos) {
        Brain<?> brain = entity.getBrain();
        GlobalPos globalpos = GlobalPos.of(serverWorld.dimension(), blockPos);
        if (brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).isPresent()) {
            brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get().add(globalpos);
        } else {
            brain.setMemory(MemoryModuleType.DOORS_TO_CLOSE, Sets.newHashSet(globalpos));
        }
    }
}