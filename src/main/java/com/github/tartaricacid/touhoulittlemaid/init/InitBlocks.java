package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.*;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class InitBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TouhouLittleMaid.MOD_ID);

    public static DeferredBlock<Block> MAID_BED = BLOCKS.register("maid_bed", BlockMaidBed::new);
    public static DeferredBlock<Block> ALTAR = BLOCKS.register("altar", BlockAltar::new);
    public static DeferredBlock<Block> STATUE = BLOCKS.register("statue", BlockStatue::new);
    public static DeferredBlock<Block> GARAGE_KIT = BLOCKS.register("garage_kit", BlockGarageKit::new);
    public static DeferredBlock<Block> MAID_BEACON = BLOCKS.register("maid_beacon", BlockMaidBeacon::new);
    public static DeferredBlock<Block> MODEL_SWITCHER = BLOCKS.register("model_switcher", BlockModelSwitcher::new);
    public static DeferredBlock<Block> PICNIC_MAT = BLOCKS.register("picnic_mat", BlockPicnicMat::new);
    public static DeferredBlock<Block> GOMOKU = BLOCKS.register("gomoku", BlockGomoku::new);
    public static DeferredBlock<Block> KEYBOARD = BLOCKS.register("keyboard", BlockKeyboard::new);
    public static DeferredBlock<Block> BOOKSHELF = BLOCKS.register("bookshelf", BlockBookshelf::new);
    public static DeferredBlock<Block> COMPUTER = BLOCKS.register("computer", BlockComputer::new);
    public static DeferredBlock<Block> SHRINE = BLOCKS.register("shrine", BlockShrine::new);
    public static DeferredBlock<Block> SCARECROW = BLOCKS.register("scarecrow", BlockScarecrow::new);

    public static Supplier<BlockEntityType<TileEntityAltar>> ALTAR_TE = TILE_ENTITIES.register("altar", () -> TileEntityAltar.TYPE);
    public static Supplier<BlockEntityType<TileEntityStatue>> STATUE_TE = TILE_ENTITIES.register("statue", () -> TileEntityStatue.TYPE);
    public static Supplier<BlockEntityType<TileEntityGarageKit>> GARAGE_KIT_TE = TILE_ENTITIES.register("garage_kit", () -> TileEntityGarageKit.TYPE);
    public static Supplier<BlockEntityType<TileEntityMaidBeacon>> MAID_BEACON_TE = TILE_ENTITIES.register("maid_beacon", () -> TileEntityMaidBeacon.TYPE);
    public static Supplier<BlockEntityType<TileEntityModelSwitcher>> MODEL_SWITCHER_TE = TILE_ENTITIES.register("model_switcher", () -> TileEntityModelSwitcher.TYPE);
    public static Supplier<BlockEntityType<TileEntityGomoku>> GOMOKU_TE = TILE_ENTITIES.register("gomoku", () -> TileEntityGomoku.TYPE);
    public static Supplier<BlockEntityType<TileEntityKeyboard>> KEYBOARD_TE = TILE_ENTITIES.register("keyboard", () -> TileEntityKeyboard.TYPE);
    public static Supplier<BlockEntityType<TileEntityBookshelf>> BOOKSHELF_TE = TILE_ENTITIES.register("bookshelf", () -> TileEntityBookshelf.TYPE);
    public static Supplier<BlockEntityType<TileEntityComputer>> COMPUTER_TE = TILE_ENTITIES.register("computer", () -> TileEntityComputer.TYPE);
    public static Supplier<BlockEntityType<TileEntityShrine>> SHRINE_TE = TILE_ENTITIES.register("shrine", () -> TileEntityShrine.TYPE);
    public static Supplier<BlockEntityType<TileEntityPicnicMat>> PICNIC_MAT_TE = TILE_ENTITIES.register("picnic_mat", () -> TileEntityPicnicMat.TYPE);
}
