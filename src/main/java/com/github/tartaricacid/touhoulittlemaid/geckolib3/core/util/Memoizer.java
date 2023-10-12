package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoizer<T, U> {
    private final Map<T, U> cache = new ConcurrentHashMap<>();

    private Memoizer() {
    }

    public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
        return new Memoizer<T, U>().doMemoize(function);
    }

    private Function<T, U> doMemoize(final Function<T, U> function) {
        return input -> cache.computeIfAbsent(input, function);
    }
}
