package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.*;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author TartaricAcid
 * @date 2019/7/7 12:07
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class MaidBlocks {
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "garage_kit")
    public static Block GARAGE_KIT;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":grid")
    public static Block GRID;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":altar")
    public static Block ALTAR;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":tombstone")
    public static Block TOMBSTONE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":maid_beacon")
    public static Block MAID_BEACON;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockGarageKit());
        event.getRegistry().register(new BlockGrid());
        event.getRegistry().register(new BlockAltar());
        event.getRegistry().register(new BlockTombstone());
        event.getRegistry().register(new BlockMaidBeacon());

        GameRegistry.registerTileEntity(TileEntityGarageKit.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "garage_kit"));
        GameRegistry.registerTileEntity(TileEntityGrid.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "grid"));
        GameRegistry.registerTileEntity(TileEntityAltar.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar"));
        GameRegistry.registerTileEntity(TileEntityTombstone.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "tombstone"));
        GameRegistry.registerTileEntity(TileEntityMaidBeacon.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_beacon"));
    }
}
