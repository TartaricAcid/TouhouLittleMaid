package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.FenceGateBlockUtil;
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
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.*;

public class MaidInteractWithFenceGate {
    private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 3;
    private static final double SKIP_CLOSING_FENCE_GATE_IF_FURTHER_AWAY_THAN = 8;
    private static final double MAX_DISTANCE_TO_HOLD_FENCE_GATE_OPEN_FOR_OTHER_MOBS = 2;

    public static BehaviorControl<LivingEntity> create() {
        MutableObject<Node> mutableObject = new MutableObject<>();
        MutableInt mutableInt = new MutableInt();
        return BehaviorBuilder.create((instance) -> instance.group(instance.present(MemoryModuleType.PATH), instance.registered(InitEntities.FENCE_GATE_TO_CLOSE.get()),
                instance.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply(instance, (pathMemory, fenceGateToCloseMemory, livingEntityMemory) -> (serverLevel, entity, time) -> {
            Path path = instance.get(pathMemory);
            Optional<Set<GlobalPos>> fenceGateToClosePos = instance.tryGet(fenceGateToCloseMemory);
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
                if (previousNodeBlockState.is(BlockTags.FENCE_GATES, (stateBase) -> stateBase.getBlock() instanceof FenceGateBlock)) {
                    if (!FenceGateBlockUtil.isOpen(previousNodeBlockState)) {
                        FenceGateBlockUtil.setOpen(entity, serverLevel, previousNodeBlockState, previousNodeBlockPos, true);
                    }
                    fenceGateToClosePos = rememberFenceGateToClose(fenceGateToCloseMemory, fenceGateToClosePos, serverLevel, previousNodeBlockPos);
                }

                BlockPos nextNodeBlockPos = nextNode.asBlockPos();
                BlockState nextNodeBlockState = serverLevel.getBlockState(nextNodeBlockPos);
                if (nextNodeBlockState.is(BlockTags.FENCE_GATES, (stateBase) -> stateBase.getBlock() instanceof FenceGateBlock)) {
                    if (!FenceGateBlockUtil.isOpen(nextNodeBlockState)) {
                        FenceGateBlockUtil.setOpen(entity, serverLevel, nextNodeBlockState, nextNodeBlockPos, true);
                        fenceGateToClosePos = rememberFenceGateToClose(fenceGateToCloseMemory, fenceGateToClosePos, serverLevel, nextNodeBlockPos);
                    }
                }
                fenceGateToClosePos.ifPresent((fenceGatePos) -> closeFenceGatesThatIHaveOpenedOrPassedThrough(serverLevel, entity, previousNode, nextNode, fenceGatePos, instance.tryGet(livingEntityMemory)));
                return true;
            } else {
                return false;
            }
        }));
    }

    public static void closeFenceGatesThatIHaveOpenedOrPassedThrough(ServerLevel serverLevel, LivingEntity entity, @Nullable Node previous, @Nullable Node next, Set<GlobalPos> fenceGatePositions, Optional<List<LivingEntity>> nearestLivingEntities) {
        Iterator<GlobalPos> fenceGatePosIterator = fenceGatePositions.iterator();
        while (fenceGatePosIterator.hasNext()) {
            GlobalPos globalPos = fenceGatePosIterator.next();
            BlockPos blockPos = globalPos.pos();
            if ((previous == null || !previous.asBlockPos().equals(blockPos) || (next != null && entity.blockPosition().equals(next.asBlockPos())))
                    && (next == null || !next.asBlockPos().equals(blockPos))) {
                if (isFenceGateTooFarAway(serverLevel, entity, globalPos)) {
                    fenceGatePosIterator.remove();
                } else {
                    BlockState blockstate = serverLevel.getBlockState(blockPos);
                    if (!(blockstate.is(BlockTags.FENCE_GATES, (stateBase) -> stateBase.getBlock() instanceof FenceGateBlock))) {
                        fenceGatePosIterator.remove();
                    } else {
                        if (!FenceGateBlockUtil.isOpen(blockstate)) {
                            fenceGatePosIterator.remove();
                        } else if (areOtherMobsComingThroughFenceGate(entity, blockPos, nearestLivingEntities)) {
                            fenceGatePosIterator.remove();
                        } else {
                            FenceGateBlockUtil.setOpen(entity, serverLevel, blockstate, blockPos, false);
                            fenceGatePosIterator.remove();
                        }
                    }
                }
            }
        }

    }

    private static boolean areOtherMobsComingThroughFenceGate(LivingEntity entity, BlockPos pos, Optional<List<LivingEntity>> nearestLivingEntities) {
        return nearestLivingEntities.map(entities -> entities.stream()
                .filter((livingEntity) -> livingEntity.getType() == entity.getType())
                .filter((livingEntity) -> pos.closerToCenterThan(livingEntity.position(), MAX_DISTANCE_TO_HOLD_FENCE_GATE_OPEN_FOR_OTHER_MOBS))
                .anyMatch((livingEntity) -> isMobComingThroughFenceGate(livingEntity.getBrain(), pos))).orElse(false);
    }

    private static boolean isMobComingThroughFenceGate(Brain<?> brain, BlockPos pos) {
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

    private static boolean isFenceGateTooFarAway(ServerLevel level, LivingEntity entity, GlobalPos pos) {
        return pos.dimension() != level.dimension() || !pos.pos().closerToCenterThan(entity.position(), SKIP_CLOSING_FENCE_GATE_IF_FURTHER_AWAY_THAN);
    }

    private static Optional<Set<GlobalPos>> rememberFenceGateToClose(MemoryAccessor<OptionalBox.Mu, Set<GlobalPos>> fenceGateToClose, Optional<Set<GlobalPos>> fenceGatePositions, ServerLevel level, BlockPos blockPos) {
        GlobalPos globalPos = GlobalPos.of(level.dimension(), blockPos);
        return Optional.of(fenceGatePositions.map((pos) -> {
            pos.add(globalPos);
            return pos;
        }).orElseGet(() -> {
            Set<GlobalPos> posSet = Sets.newHashSet(globalPos);
            fenceGateToClose.set(posSet);
            return posSet;
        }));
    }
}
