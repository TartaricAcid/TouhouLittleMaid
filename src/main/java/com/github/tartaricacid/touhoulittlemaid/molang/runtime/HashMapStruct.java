package com.github.tartaricacid.touhoulittlemaid.molang.runtime;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util.PooledStringHashMap;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util.StringPool;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;

public class HashMapStruct implements Struct {
    private final PooledStringHashMap<Object> properties;
    private final boolean isRightValue;

    public HashMapStruct() {
        this(false);
    }

    public HashMapStruct(boolean isRightValue) {
        this.properties = new PooledStringHashMap<>();
        this.isRightValue = isRightValue;
    }

    private HashMapStruct(PooledStringHashMap<Object> properties) {
        this.properties = properties;
        this.isRightValue = false;
    }

    @Override
    public Object getProperty(int name) {
        return properties.get(name);
    }

    @Override
    public void putProperty(int name, Object value) {
        properties.put(name, value);
    }

    @Override
    public Struct copy() {
        if (isRightValue) {
            return new HashMapStruct(properties);
        } else {
            return new HashMapStruct(new PooledStringHashMap<>(properties));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("struct{");
        boolean first = true;
        for (Int2ReferenceMap.Entry<Object> entry : properties.int2ReferenceEntrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(String.format("%s=%s", StringPool.getString(entry.getIntKey()), entry.getValue() == null ? "null" : entry.getValue().toString()));
        }
        builder.append("}");
        return builder.toString();
    }
}
