package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.snapshot.BoneSnapshot;


public interface IBone {
        float getRotationX();

        void setRotationX(float value);

        float getRotationY();

        void setRotationY(float value);

        float getRotationZ();

        void setRotationZ(float value);

        float getPositionX();

        void setPositionX(float value);

        float getPositionY();

        void setPositionY(float value);

        float getPositionZ();

        void setPositionZ(float value);

        float getScaleX();

        void setScaleX(float value);

        float getScaleY();

        void setScaleY(float value);

        float getScaleZ();

        void setScaleZ(float value);

        float getPivotX();

        void setPivotX(float value);

        float getPivotY();

        void setPivotY(float value);

        float getPivotZ();

        void setPivotZ(float value);

        boolean isHidden();

        void setHidden(boolean hidden);

        boolean cubesAreHidden();

        boolean childBonesAreHiddenToo();

        void setCubesHidden(boolean hidden);

        void setHidden(boolean selfHidden, boolean skipChildRendering);

        void setModelRendererName(String modelRendererName);

        void saveInitialSnapshot();

        BoneSnapshot getInitialSnapshot();

        default BoneSnapshot saveSnapshot() {
        return new BoneSnapshot(this);
    }

        String getName();
}
