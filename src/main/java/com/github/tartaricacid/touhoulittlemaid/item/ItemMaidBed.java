package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;
import static net.minecraftforge.common.util.Constants.BlockFlags.*;

public class ItemMaidBed extends BlockItem {
    public ItemMaidBed() {
        super(InitBlocks.MAID_BED.get(), (new Item.Properties()).stacksTo(1).tab(MAIN_TAB));
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, UPDATE_NEIGHBORS | RERENDER_MAIN_THREAD | BLOCK_UPDATE);
    }
}
