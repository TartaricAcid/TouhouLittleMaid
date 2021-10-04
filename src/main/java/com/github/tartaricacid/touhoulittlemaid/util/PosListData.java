package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public final class PosListData {
    private final List<BlockPos> data = Lists.newArrayList();

    public ListNBT serialize() {
        ListNBT nbt = new ListNBT();
        for (BlockPos pos : data) {
            nbt.add(NBTUtil.writeBlockPos(pos));
        }
        return nbt;
    }

    public void deserialize(ListNBT nbt) {
        data.clear();
        for (int i = 0; i < nbt.size(); i++) {
            data.add(NBTUtil.readBlockPos(nbt.getCompound(i)));
        }
    }

    public List<BlockPos> getData() {
        return data;
    }

    public void add(BlockPos pos) {
        this.data.add(pos);
    }
}
