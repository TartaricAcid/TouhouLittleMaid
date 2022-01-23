package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.*;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<Block> MAID_BED = BLOCKS.register("maid_bed", BlockMaidBed::new);
    public static RegistryObject<Block> ALTAR = BLOCKS.register("altar", BlockAltar::new);
    public static RegistryObject<Block> STATUE = BLOCKS.register("statue", BlockStatue::new);
    public static RegistryObject<Block> GARAGE_KIT = BLOCKS.register("garage_kit", BlockGarageKit::new);
    public static RegistryObject<Block> MAID_BEACON = BLOCKS.register("maid_beacon", BlockMaidBeacon::new);

    public static RegistryObject<TileEntityType<TileEntityAltar>> ALTAR_TE = TILE_ENTITIES.register("altar", () -> TileEntityAltar.TYPE);
    public static RegistryObject<TileEntityType<TileEntityStatue>> STATUE_TE = TILE_ENTITIES.register("statue", () -> TileEntityStatue.TYPE);
    public static RegistryObject<TileEntityType<TileEntityGarageKit>> GARAGE_KIT_TE = TILE_ENTITIES.register("garage_kit", () -> TileEntityGarageKit.TYPE);
    public static RegistryObject<TileEntityType<TileEntityMaidBeacon>> MAID_BEACON_TE = TILE_ENTITIES.register("maid_beacon", () -> TileEntityMaidBeacon.TYPE);
}
