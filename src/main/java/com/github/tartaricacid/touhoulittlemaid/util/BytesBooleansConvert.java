package com.github.tartaricacid.touhoulittlemaid.util;

import javax.annotation.Nullable;

public final class BytesBooleansConvert {
    public static boolean[] bytes2Booleans(@Nullable byte[] bytes, int slotNum) {
        if (bytes == null) {
            bytes = new byte[slotNum];
        }
        boolean[] out = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            out[i] = (bytes[i] != 0);
        }
        return out;
    }

    public static byte[] booleans2Bytes(boolean[] booleans) {
        byte[] out = new byte[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            out[i] = (byte) (booleans[i] ? 1 : 0);
        }
        return out;
    }
}
