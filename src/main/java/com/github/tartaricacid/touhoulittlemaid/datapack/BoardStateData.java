package com.github.tartaricacid.touhoulittlemaid.datapack;

import com.github.tartaricacid.touhoulittlemaid.datapack.pojo.BoardStateRecord;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BoardStateData {
    private static final List<BoardStateRecord> CHESS_RECORDS = Lists.newArrayList();
    private static final List<BoardStateRecord> XIANGQI_RECORDS = Lists.newArrayList();
    private static final List<BoardStateRecord> GOMOKU_RECORDS = Lists.newArrayList();

    public static void clear() {
        CHESS_RECORDS.clear();
        XIANGQI_RECORDS.clear();
        GOMOKU_RECORDS.clear();
    }

    public static void addChessRecords(List<BoardStateRecord> records) {
        CHESS_RECORDS.addAll(records);
    }

    public static void addXiangqiRecords(List<BoardStateRecord> records) {
        XIANGQI_RECORDS.addAll(records);
    }

    public static void addGomokuRecords(List<BoardStateRecord> records) {
        GOMOKU_RECORDS.addAll(records);
    }

    public static List<BoardStateRecord> getChessRecords() {
        return CHESS_RECORDS;
    }

    public static List<BoardStateRecord> getXiangqiRecords() {
        return XIANGQI_RECORDS;
    }

    public static List<BoardStateRecord> getGomokuRecords() {
        return GOMOKU_RECORDS;
    }

    public static List<BoardStateRecord> getRecordsByItem(ItemStack stack) {
        Item item = stack.getItem();
        if (item == InitItems.WCHESS_BOARD_STATE.get()) {
            return CHESS_RECORDS;
        }
        if (item == InitItems.CCHESS_BOARD_STATE.get()) {
            return XIANGQI_RECORDS;
        }
        if (item == InitItems.GOMOKU_BOARD_STATE.get()) {
            return GOMOKU_RECORDS;
        }
        return Lists.newArrayList();
    }
}
