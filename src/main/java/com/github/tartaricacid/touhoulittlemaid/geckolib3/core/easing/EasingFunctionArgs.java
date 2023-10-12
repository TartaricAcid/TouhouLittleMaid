package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing;


import java.util.Objects;

public record EasingFunctionArgs(EasingType easingType, Double arg0) {
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