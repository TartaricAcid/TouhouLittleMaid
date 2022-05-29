package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemMaidBed extends BlockItem {
    public ItemMaidBed() {
        super(InitBlocks.MAID_BED.get(), (new Item.Properties()).stacksTo(1).tab(MAIN_TAB));
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_IMMEDIATE | Block.UPDATE_CLIENTS);
    }
}