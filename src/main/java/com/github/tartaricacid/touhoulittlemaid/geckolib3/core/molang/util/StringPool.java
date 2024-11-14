package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StringPool {
    public static final int NONE = Integer.MIN_VALUE;
    public static final String EMPTY = "";
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, Integer> POOL = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, String> MAP = new ConcurrentHashMap<>();

    public static int computeIfAbsent(String str) {
        return POOL.computeIfAbsent(str, k -> {
            int name = COUNTER.incrementAndGet();
            MAP.put(name, k);
            return name;
        });
    }

    public static int getName(String str) {
        return POOL.getOrDefault(str, NONE);
    }

    public static String getString(int name) {
        return MAP.getOrDefault(name, EMPTY);
    }
}
