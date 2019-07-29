package com.github.tartaricacid.touhoulittlemaid.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityGrid extends TileEntity {

    public final ItemStackHandler handler = new ItemStackHandler(9) {
        public int getSlotLimit(int slot) {
            return 1;
        };
    };

    private void refresh() {
        markDirty();
        if (world != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }
}
