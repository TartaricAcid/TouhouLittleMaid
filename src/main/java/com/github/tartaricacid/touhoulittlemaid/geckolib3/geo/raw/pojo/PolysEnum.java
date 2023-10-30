package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import java.io.IOException;

public enum PolysEnum {
    // PolysEnum
    QUAD_LIST, TRI_LIST;

    public static PolysEnum forValue(String value) throws IOException {
        if ("quad_list".equals(value)) {
            return QUAD_LIST;
        }
        if ("tri_list".equals(value)) {
            return TRI_LIST;
        }
        throw new IOException("Cannot deserialize PolysEnum");
    }

    public String toValue() {
        return switch (this) {
            case QUAD_LIST -> "quad_list";
            case TRI_LIST -> "tri_list";
        };
    }
}
