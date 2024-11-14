package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.Collection;
import java.util.stream.Collectors;

public class PooledStringHashSet extends IntOpenHashSet {
    public PooledStringHashSet() {
    }

    public PooledStringHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public PooledStringHashSet(Collection<String> m) {
        super(m.stream().map(StringPool::computeIfAbsent).collect(Collectors.toSet()));
    }

    public PooledStringHashSet(IntOpenHashSet m) {
        super(m);
    }

    public boolean contains(String key) {
        int intKey = StringPool.getName(key);
        if (intKey == StringPool.NONE) {
            return false;
        } else {
            return super.contains(intKey);
        }
    }

    public boolean add(String key) {
        return super.add(StringPool.computeIfAbsent(key));
    }
}
