package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockAltar;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockTombstone;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityTombstone;
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

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockGarageKit());
        event.getRegistry().register(new BlockGrid());
        event.getRegistry().register(new BlockAltar());
        event.getRegistry().register(new BlockTombstone());

        GameRegistry.registerTileEntity(TileEntityGarageKit.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "garage_kit"));
        GameRegistry.registerTileEntity(TileEntityGrid.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "grid"));
        GameRegistry.registerTileEntity(TileEntityAltar.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar"));
        GameRegistry.registerTileEntity(TileEntityTombstone.class, new ResourceLocation(TouhouLittleMaid.MOD_ID, "tombstone"));
    }
}
