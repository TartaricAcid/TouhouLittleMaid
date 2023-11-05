package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing;



import java.util.Objects;

public class EasingFunctionArgs {
    private final EasingType easingType;
    private final Double arg0;

    public EasingFunctionArgs(EasingType easingType, Double arg0) {
        this.easingType = easingType;
        this.arg0 = arg0;
    }

    public EasingType easingType() {
        return easingType;
    }

    public Double arg0() {
        return arg0;
    }

    @Override
        public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EasingFunctionArgs that = (EasingFunctionArgs) o;
        return easingType == that.easingType && Objects.equals(arg0, that.arg0);
    }
}