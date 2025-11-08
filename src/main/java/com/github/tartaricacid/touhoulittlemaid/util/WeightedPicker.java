package com.github.tartaricacid.touhoulittlemaid.util;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

/**
 * 通用的权重抽取工具
 */
public final class WeightedPicker {
    private WeightedPicker() {
    }

    /**
     * 使用自带 Random 抽取
     */
    @Nullable
    public static <T> T pickRandom(List<T> items, ToIntFunction<T> weightFunc) {
        return pickRandom(items, weightFunc, new Random());
    }

    /**
     * 使用指定 Random 抽取（可用于确定性 seed）
     */
    @Nullable
    public static <T> T pickRandom(@Nullable List<T> items, ToIntFunction<T> weightFunc, Random rand) {
        if (items == null || items.isEmpty()) {
            return null;
        }

        int total = 0;
        for (T t : items) {
            int w = Math.max(0, weightFunc.applyAsInt(t));
            total += w;
        }

        if (total <= 0L) {
            // 全为 0，做均匀抽样
            return items.get(rand.nextInt(items.size()));
        }

        int pick = rand.nextInt(total);
        int acc = 0;
        for (T t : items) {
            int w = Math.max(0, weightFunc.applyAsInt(t));
            acc += w;
            if (pick < acc) {
                return t;
            }
        }

        // 理论上不可到达，但保险返回最后一个
        return items.getLast();
    }

    /**
     * 快速创建确定性 Random（用 seed）的方法
     */
    @Nullable
    public static <T> T pickDeterministic(List<T> items, ToIntFunction<T> weightFunc, long seed) {
        return pickRandom(items, weightFunc, new Random(seed));
    }
}
