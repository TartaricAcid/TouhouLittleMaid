package com.github.tartaricacid.touhoulittlemaid.entity.task.crop;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.ISpecialCropHandler;
import com.github.tartaricacid.touhoulittlemaid.compat.kaleidoscope.KaleidoscopeCompat;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class SpecialCropManager {
    private static Map<Item, ISpecialCropHandler> ITEM_SEED_HANDLERS = Maps.newHashMap();
    private static Map<Block, ISpecialCropHandler> BLOCK_CROP_HANDLERS = Maps.newHashMap();

    private SpecialCropManager() {
    }

    public static void init() {
        SpecialCropManager manager = new SpecialCropManager();

        manager.add(Items.NETHER_WART, Blocks.NETHER_WART, new NetherWartCropHandler());
        KaleidoscopeCompat.addCropHandlers(manager);
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerSpecialCropHandler(manager);
        }

        ITEM_SEED_HANDLERS = ImmutableMap.copyOf(ITEM_SEED_HANDLERS);
        BLOCK_CROP_HANDLERS = ImmutableMap.copyOf(BLOCK_CROP_HANDLERS);
    }

    public static Map<Item, ISpecialCropHandler> getItemSeedHandlers() {
        return ITEM_SEED_HANDLERS;
    }

    public static Map<Block, ISpecialCropHandler> getBlockCropHandlers() {
        return BLOCK_CROP_HANDLERS;
    }

    public void add(Item seed, Block crop, ISpecialCropHandler handler) {
        ITEM_SEED_HANDLERS.put(seed, handler);
        BLOCK_CROP_HANDLERS.put(crop, handler);
    }

    public void addSeed(Item seed, ISpecialCropHandler handler) {
        ITEM_SEED_HANDLERS.put(seed, handler);
    }

    public void addCrop(Block crop, ISpecialCropHandler handler) {
        BLOCK_CROP_HANDLERS.put(crop, handler);
    }
}
