package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone;


import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.DoubleValue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.IValue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.RotationValue;

@SuppressWarnings("FieldMayBeFinal,unused")
public class RawBoneKeyFrame {
    public double startTick;
    public EasingType easingType;

    public double preX;
    public IValue preXValue;
    public double preY;
    public IValue preYValue;
    public double preZ;
    public IValue preZValue;

    public double postX;
    public IValue postXValue;
    public double postY;
    public IValue postYValue;
    public double postZ;
    public IValue postZValue;

    public boolean contiguous = true;

    public Vector3v preValue;
    public Vector3v postValue;

    public RawBoneKeyFrame() {
    }

    private IValue getValue(IValue value, double primitive, boolean isRotation, boolean flip) {
        if (value == null) {
            if (isRotation) {
                return new DoubleValue(RotationValue.processValue(primitive, flip));
            } else {
                return new DoubleValue(primitive);
            }
        }
        if (isRotation) {
            return new RotationValue(value, flip);
        } else {
            return value;
        }
    }

    public void init(boolean isRotation) {
        if (preValue != null) {
            return;
        }

        preValue = new Vector3v(
                getValue(this.preXValue, this.preX, isRotation, true),
                getValue(this.preYValue, this.preY, isRotation, true),
                getValue(this.preZValue, this.preZ, isRotation, false));
        if (contiguous) {
            postValue = preValue;
        } else {
            postValue = new Vector3v(
                    getValue(this.postXValue, this.postX, isRotation, true),
                    getValue(this.postYValue, this.postY, isRotation, true),
                    getValue(this.postZValue, this.postZ, isRotation, false));
        }
        if (easingType == null) {
            easingType = EasingType.LINEAR;
        }
    }

    public double startTick() {
        return startTick;
    }

    public EasingType easingType() {
        return easingType;
    }

    public Vector3v preValue() {
        return preValue;
    }

    public Vector3v postValue() {
        return postValue;
    }
}
