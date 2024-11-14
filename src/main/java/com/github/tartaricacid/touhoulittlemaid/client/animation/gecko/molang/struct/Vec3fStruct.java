package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.struct;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.util.StringPool;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Struct;

public abstract class Vec3fStruct implements Struct {
    private static final int NAME_X = StringPool.computeIfAbsent("x");
    private static final int NAME_Y = StringPool.computeIfAbsent("y");
    private static final int NAME_Z = StringPool.computeIfAbsent("z");

    @Override
    public Object getProperty(int name) {
        if (name == NAME_X) {
            return getX();
        }
        if (name == NAME_Y) {
            return getY();
        }
        if (name == NAME_Z) {
            return getZ();
        }
        return null;
    }

    protected abstract float getX();

    protected abstract float getY();

    protected abstract float getZ();

    @Override
    public void putProperty(int name, Object value) {
    }

    @Override
    public String toString() {
        return String.format("vec3{x=%.2f, y=%.2f, z=%.2f}", getX(), getY(), getZ());
    }

    @Override
    public Struct copy() {
        return this;
    }
}
