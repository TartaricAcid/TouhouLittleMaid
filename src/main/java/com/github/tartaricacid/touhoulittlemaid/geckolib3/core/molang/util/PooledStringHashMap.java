package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util;

import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;

public class PooledStringHashMap<V> extends Int2ReferenceOpenHashMap<V> {
    public PooledStringHashMap() {
    }

    public PooledStringHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public PooledStringHashMap(Int2ReferenceOpenHashMap<V> m) {
        super(m);
    }

    public V get(String key) {
        int intKey = StringPool.getName(key);
        if (intKey == StringPool.NONE) {
            return null;
        } else {
            return super.get(intKey);
        }
    }

    public void put(String key, V value) {
        super.put(StringPool.computeIfAbsent(key), value);
    }
}
