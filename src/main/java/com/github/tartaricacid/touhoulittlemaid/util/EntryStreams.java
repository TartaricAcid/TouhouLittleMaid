package com.github.tartaricacid.touhoulittlemaid.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Argon4W
 */
public class EntryStreams {
    public static <T1, T2, R> BiFunction<T1, T2, R> swap(BiFunction<T2, T1, R> function) {
        return (t1, t2) -> function.apply(t2, t1);
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, LinkedHashMap<K, V>> collectSequenced() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new);
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> collect() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <K, V, M extends Map<K, V>> Collector<Map.Entry<K, V>, ?, M> collect(Supplier<M> supplier) {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2, supplier);
    }

    public static <K, V> Function<K, Map.Entry<K, V>> create(Function<K, V> function) {
        return key -> Map.entry(key, function.apply(key));
    }

    public static <K, V> Function<Map.Entry<K, V>, Map.Entry<V, K>> swap() {
        return entry -> Map.entry(entry.getValue(), entry.getKey());
    }

    public static <K, V> Function<K, Map.Entry<K, V>> createFixed(V fixedValue) {
        return key -> Map.entry(key, fixedValue);
    }

    public static <K, V> Consumer<Map.Entry<K, V>> peekEntryValue(Consumer<V> consumer) {
        return entry -> consumer.accept(entry.getValue());
    }

    public static <K, V> Consumer<Map.Entry<K, V>> peekEntryValue(BiConsumer<K, V> consumer) {
        return entry -> consumer.accept(entry.getKey(), entry.getValue());
    }

    public static <K1, K2, V> Function<Map.Entry<K1, V>, Map.Entry<K2, V>> mapEntryKey(Function<K1, K2> function) {
        return entry -> Map.entry(function.apply(entry.getKey()), entry.getValue());
    }

    public static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> mapEntryValue(Function<V1, V2> function) {
        return entry -> Map.entry(entry.getKey(), function.apply(entry.getValue()));
    }

    public static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> mapEntryValue(BiFunction<K, V1, V2> function) {
        return entry -> Map.entry(entry.getKey(), function.apply(entry.getKey(), entry.getValue()));
    }

    public static <K1, V1, K2, V2> Function<Map.Entry<K1, V1>, Map.Entry<K2, V2>> mapEntry(BiFunction<K1, V1, K2> keyFunction, BiFunction<K1, V1, V2> valueFunction) {
        return entry -> Map.entry(keyFunction.apply(entry.getKey(), entry.getValue()), valueFunction.apply(entry.getKey(), entry.getValue()));
    }

    public static <K, V> Predicate<Map.Entry<K, V>> filterEntryValue(Predicate<V> predicate) {
        return entry -> predicate.test(entry.getValue());
    }
}
