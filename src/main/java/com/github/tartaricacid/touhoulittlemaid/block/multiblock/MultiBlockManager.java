package com.github.tartaricacid.touhoulittlemaid.block.multiblock;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public final class MultiBlockManager {
    private static List<IMultiBlock> MULTI_BLOCK_LIST;

    private MultiBlockManager() {
        MULTI_BLOCK_LIST = Lists.newArrayList();
    }

    public static void init() {
        MultiBlockManager manager = new MultiBlockManager();
        manager.add(new MultiBlockAltar());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addMultiBlock(manager);
        }
        MULTI_BLOCK_LIST = ImmutableList.copyOf(MULTI_BLOCK_LIST);
    }

    public static List<IMultiBlock> getMultiBlockList() {
        return MULTI_BLOCK_LIST;
    }

    public void add(IMultiBlock multiBlock) {
        MULTI_BLOCK_LIST.add(multiBlock);
    }
}
