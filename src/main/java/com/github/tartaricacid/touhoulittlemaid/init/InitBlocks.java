package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.*;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<Block> MAID_BED = BLOCKS.register("maid_bed", BlockMaidBed::new);
    public static RegistryObject<Block> ALTAR = BLOCKS.register("altar", BlockAltar::new);
    public static RegistryObject<Block> STATUE = BLOCKS.register("statue", BlockStatue::new);
    public static RegistryObject<Block> GARAGE_KIT = BLOCKS.register("garage_kit", BlockGarageKit::new);
    public static RegistryObject<Block> MAID_BEACON = BLOCKS.register("maid_beacon", BlockMaidBeacon::new);
    public static RegistryObject<Block> MODEL_SWITCHER = BLOCKS.register("model_switcher", BlockModelSwitcher::new);

    public static RegistryObject<BlockEntityType<TileEntityAltar>> ALTAR_TE = TILE_ENTITIES.register("altar", () -> TileEntityAltar.TYPE);
    public static RegistryObject<BlockEntityType<TileEntityStatue>> STATUE_TE = TILE_ENTITIES.register("statue", () -> TileEntityStatue.TYPE);
    public static RegistryObject<BlockEntityType<TileEntityGarageKit>> GARAGE_KIT_TE = TILE_ENTITIES.register("garage_kit", () -> TileEntityGarageKit.TYPE);
    public static RegistryObject<BlockEntityType<TileEntityMaidBeacon>> MAID_BEACON_TE = TILE_ENTITIES.register("maid_beacon", () -> TileEntityMaidBeacon.TYPE);
    public static RegistryObject<BlockEntityType<TileEntityModelSwitcher>> MODEL_SWITCHER_TE = TILE_ENTITIES.register("model_switcher", () -> TileEntityModelSwitcher.TYPE);
}
