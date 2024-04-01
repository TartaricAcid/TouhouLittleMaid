package com.github.tartaricacid.touhoulittlemaid.entity.poi;

import com.github.tartaricacid.touhoulittlemaid.block.BlockPicnicMat;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.Set;

public final class MaidPoiManager {
    private static final Set<BlockState> JOYS = ImmutableList.of(InitBlocks.COMPUTER.get(), InitBlocks.BOOKSHELF.get(), InitBlocks.KEYBOARD.get(), InitBlocks.GOMOKU.get())
            .stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
            .collect(ImmutableSet.toImmutableSet());
    private static final Set<BlockState> BEDS = ImmutableList.of(InitBlocks.MAID_BED.get())
            .stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
            .filter(blockState -> blockState.getValue(BedBlock.PART) == BedPart.HEAD)
            .filter(blockState -> !blockState.getValue(BedBlock.OCCUPIED))
            .collect(ImmutableSet.toImmutableSet());
    private static final Set<BlockState> HOME_MEAL = ImmutableList.of(InitBlocks.PICNIC_MAT.get())
            .stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
            .filter(blockState -> blockState.getValue(BlockPicnicMat.PART).isCenter())
            .collect(ImmutableSet.toImmutableSet());

    public static PoiType getMaidBed() {
        return new PoiType(BEDS, 0, 1);
    }

    public static PoiType getJoyBlock() {
        return new PoiType(JOYS, 0, 1);
    }

    public static PoiType getHomeMeal() {
        return new PoiType(HOME_MEAL, 0, 1);
    }
}
