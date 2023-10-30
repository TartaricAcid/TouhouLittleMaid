package com.github.tartaricacid.touhoulittlemaid.mclib.utils;



public enum Interpolation {
    // 插值类型
    LINEAR("linear") {
        @Override
                public float interpolate(float a, float b, float x) {
            return Interpolations.lerp(a, b, x);
        }
    },
    QUAD_IN("quad_in") {
        @Override
                public float interpolate(float a, float b, float x) {
            return a + (b - a) * x * x;
        }
    },
    QUAD_OUT("quad_out") {
        @Override
                public float interpolate(float a, float b, float x) {
            return a - (b - a) * x * (x - 2);
        }
    },
    QUAD_INOUT("quad_inout") {
        @Override
                public float interpolate(float a, float b, float x) {
            x *= 2;
            if (x < 1F) {
                return a + (b - a) / 2 * x * x;
            }
            x -= 1;
            return a - (b - a) / 2 * (x * (x - 2) - 1);
        }
    },
    CUBIC_IN("cubic_in") {
        @Override
                public float interpolate(float a, float b, float x) {
            return a + (b - a) * x * x * x;
        }
    },
    CUBIC_OUT("cubic_out") {
        @Override
                public float interpolate(float a, float b, float x) {
            x -= 1;
            return a + (b - a) * (x * x * x + 1);
        }
    },
    CUBIC_INOUT("cubic_inout") {
        @Override
                public float interpolate(float a, float b, float x) {
            x *= 2;
            if (x < 1F) {
                return a + (b - a) / 2 * x * x * x;
            }
            x -= 2;
            return a + (b - a) / 2 * (x * x * x + 2);
        }
    },
    EXP_IN("exp_in") {
        @Override
                public float interpolate(float a, float b, float x) {
            return a + (b - a) * (float) Math.pow(2, 10 * (x - 1));
        }
    },
    EXP_OUT("exp_out") {
        @Override
                public float interpolate(float a, float b, float x) {
            return a + (b - a) * (float) (-Math.pow(2, -10 * x) + 1);
        }
    },
    EXP_INOUT("exp_inout") {
        @Override
                public float interpolate(float a, float b, float x) {
            if (x == 0) {
                return a;
            }
            if (x == 1) {
                return b;
            }
            x *= 2;
            if (x < 1F) {
                return a + (b - a) / 2 * (float) Math.pow(2, 10 * (x - 1));
            }
            x -= 1;
            return a + (b - a) / 2 * (float) (-Math.pow(2, -10 * x) + 2);
        }
    };

    public final String key;

    private Interpolation(String key) {
        this.key = key;
    }

    public abstract float interpolate(float a, float b, float x);

    public String getName() {
        return "mclib.interpolations." + this.key;
    }
}
