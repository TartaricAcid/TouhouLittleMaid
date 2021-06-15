package com.github.tartaricacid.touhoulittlemaid.item;

import com.google.common.collect.Maps;

import java.util.Map;

public final class BackpackLevel {
    public static final int EMPTY = 0;
    public static final int SMALL = 1;
    public static final int MIDDLE = 2;
    public static final int BIG = 3;

    public static final int EMPTY_CAPACITY = 6;
    public static final int SMALL_CAPACITY = 12;
    public static final int MIDDLE_CAPACITY = 24;
    public static final int BIG_CAPACITY = 36;

    public static final Map<Integer, Integer> BACKPACK_CAPACITY_MAP = Maps.newHashMap();

    static {
        BACKPACK_CAPACITY_MAP.put(EMPTY, EMPTY_CAPACITY);
        BACKPACK_CAPACITY_MAP.put(SMALL, SMALL_CAPACITY);
        BACKPACK_CAPACITY_MAP.put(MIDDLE, MIDDLE_CAPACITY);
        BACKPACK_CAPACITY_MAP.put(BIG, BIG_CAPACITY);
    }
}
