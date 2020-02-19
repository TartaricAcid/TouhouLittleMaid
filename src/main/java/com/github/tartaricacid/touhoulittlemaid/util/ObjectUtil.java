package com.github.tartaricacid.touhoulittlemaid.util;

import javax.annotation.Nullable;

public final class ObjectUtil {
    public static boolean equalNotNull(@Nullable Object a, @Nullable Object b) {
        if (a != null) {
            return a.equals(b);
        }
        return false;
    }
}
