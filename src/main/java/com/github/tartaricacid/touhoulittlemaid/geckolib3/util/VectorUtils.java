package com.github.tartaricacid.touhoulittlemaid.geckolib3.util;

import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.joml.Vector3f;

public class VectorUtils {
    public static Vec3 fromArray(double[] array) {
        Validate.validIndex(ArrayUtils.toObject(array), 2);
        return new Vec3(array[0], array[1], array[2]);
    }

    public static Vector3f fromArray(float[] array) {
        Validate.validIndex(ArrayUtils.toObject(array), 2);
        return new Vector3f(array[0], array[1], array[2]);
    }

    public static Vector3f convertDoubleToFloat(Vec3 vector) {
        return new Vector3f((float) vector.x, (float) vector.y, (float) vector.z);
    }

    public static Vec3 convertFloatToDouble(Vector3f vector) {
        return new Vec3(vector.x(), vector.y(), vector.z());
    }
}